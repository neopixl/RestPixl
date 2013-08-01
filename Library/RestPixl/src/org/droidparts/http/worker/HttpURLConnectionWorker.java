
package org.droidparts.http.worker;

import static com.neopixl.restpixl.NPRestPixlConf.FORMAT;
import static org.droidparts.util.io.IOUtils.silentlyClose;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.http.auth.AuthScope;
import org.droidparts.http.CookieJar;
import org.droidparts.http.HTTPException;
import org.droidparts.http.HTTPResponse;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

public class HttpURLConnectionWorker extends HTTPWorker {

	public static final String GET = "GET";
	public static final String PUT = "PUT";
	public static final String POST = "POST";
	public static final String DELETE = "DELETE";

	private Proxy proxy;
	private PasswordAuthentication passAuth;
	private AuthScope authScope;

	// ICS+
	public static void setHttpResponseCacheEnabled(Context ctx, boolean enabled) {
		File cacheDir = new File(ctx.getCacheDir(), "http");
		long cacheSize = 10 * 1024 * 1024; // 10 MiB
		try {
			Class<?> cls = Class.forName("android.net.http.HttpResponseCache");
			if (enabled) {
				cls.getMethod("install", File.class, long.class).invoke(null,
						cacheDir, cacheSize);
			} else {
				Object instance = cls.getMethod("getInstalled").invoke(null);
				if (instance != null) {
					cls.getMethod("delete").invoke(instance);
				}
			}
		} catch (Exception e) {
			Log.i("RestPixl",e.toString());
		}
	}

	public HttpURLConnectionWorker(String userAgent) {
		super(userAgent);
	}

	@Override
	public void setCookieJar(CookieJar cookieJar) {
		CookieHandler.setDefault(cookieJar);
	}

	@Override
	public void authenticateBasic(String user, String password, AuthScope scope) {
		passAuth = new PasswordAuthentication(user, password.toCharArray());
		authScope = scope;
	}

	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}

	public HttpURLConnection getConnection(String urlStr, String requestMethod)
			throws HTTPException {
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn;
			if (proxy != null) {
				conn = (HttpURLConnection) url.openConnection(proxy);
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}
			for (String key : headers.keySet()) {
				for (String val : headers.get(key)) {
					conn.addRequestProperty(key, val);
				}
			}

			conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
			conn.setRequestProperty("Connection", "Keep-Alive");

			setupBasicAuth();
			conn.setRequestMethod(requestMethod);
			if (PUT.equals(requestMethod) || POST.equals(requestMethod)) {
				conn.setDoOutput(true);
			}
			return conn;
		} catch (Exception e) {
			throw new HTTPException(e);
		}
	}

	public static void postOrPut(HttpURLConnection conn, String contentType,
			String data) throws HTTPException {
		conn.setRequestProperty("Accept-Charset", FORMAT);
		conn.setRequestProperty("Content-Type", contentType);
		conn.setRequestProperty("Connection", "Keep-Alive");

		if(data != null){
			OutputStream os = null;
			try {
				os = conn.getOutputStream();
				os.write(data.getBytes(FORMAT));
			} catch (Exception e) {
				throw new HTTPException(e);
			} finally {
				silentlyClose(os);
			}
		}
		
	}

	public static HTTPResponse getReponse(HttpURLConnection conn)
			throws HTTPException {

		HTTPResponse response = new HTTPResponse();
		response.code = connectAndGetResponseCodeOrThrow(conn);
		response.headers = conn.getHeaderFields();
		response.body = HTTPInputStream.getInstance(conn, false).readAndClose();
		return response;
	}

	public Pair<Integer, BufferedInputStream> getInputStream(String uri)
			throws HTTPException {
		HttpURLConnection conn = getConnection(uri, GET);
		HttpURLConnectionWorker.connectAndGetResponseCodeOrThrow(conn);
		int contentLength = conn.getContentLength();
		HTTPInputStream is = HTTPInputStream.getInstance(conn, false);
		return new Pair<Integer, BufferedInputStream>(contentLength, is);
	}

	private static int connectAndGetResponseCodeOrThrow(HttpURLConnection conn)
			throws HTTPException {
		try {
			conn.connect();
			int respCode = conn.getResponseCode();
			if (isErrorResponseCode(respCode)) {
				HTTPInputStream is = HTTPInputStream.getInstance(conn,
						(conn.getErrorStream() != null));
				throw new HTTPException(respCode, is.readAndClose());
			}
			return respCode;
		} catch (HTTPException e) {
			throw e;
		} catch (Exception e) {
			throw new HTTPException(e);
		}

	}

	private void setupBasicAuth() {
		if (passAuth != null) {
			Authenticator.setDefault(new FixedAuthenticator(passAuth));
			if (!AuthScope.ANY.equals(authScope)) {
				InetAddress host = null;
				if (authScope.getHost() != null) {
					try {
						host = InetAddress.getByName(authScope.getHost());
					} catch (UnknownHostException e) {
						Log.e("RestPixl","Failed to setup basic auth.");
						Log.d("RestPixl",e.toString());
						Authenticator.setDefault(null);
						return;
					}
				}
				int port = (authScope.getPort() == AuthScope.ANY_PORT) ? 0
						: authScope.getPort();
				Authenticator.requestPasswordAuthentication(host, port, null,
						authScope.getRealm(), authScope.getScheme());
			}
		}
	}

	private static class FixedAuthenticator extends Authenticator {

		private PasswordAuthentication passAuth;

		public FixedAuthenticator(PasswordAuthentication passAuth) {
			this.passAuth = passAuth;
		}

		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			try {
				return passAuth;
			} finally {
				passAuth = null;
			}
		}

	}

}
