
package org.droidparts.http.worker;

import static com.neopixl.restpixl.NPRestPixlConf.BUFFER_SIZE;
import static com.neopixl.restpixl.NPRestPixlConf.FORMAT;
import static org.apache.http.client.params.CookiePolicy.BROWSER_COMPATIBILITY;

import java.io.BufferedInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.droidparts.http.CookieJar;
import org.droidparts.http.HTTPException;
import org.droidparts.http.HTTPResponse;

import android.util.Pair;

// For API < 10
public class HttpClientWorker extends HTTPWorker {

	private final DefaultHttpClient httpClient;

	private DefaultHttpClient getThreadSafeClient() {
		DefaultHttpClient client = new DefaultHttpClient();
		ClientConnectionManager mgr = client.getConnectionManager();
		HttpParams params = client.getParams();

		client = new DefaultHttpClient(new ThreadSafeClientConnManager(params,
				mgr.getSchemeRegistry()), params);

		return client;
	}

	public HttpClientWorker(String userAgent) {
		super(userAgent);
		httpClient = getThreadSafeClient();
		HttpParams params = httpClient.getParams();
		HttpProtocolParams.setUserAgent(params, userAgent);
		HttpConnectionParams.setStaleCheckingEnabled(params, false);
		HttpClientParams.setRedirecting(params, false);
		HttpConnectionParams.setConnectionTimeout(params,
				SOCKET_OPERATION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_OPERATION_TIMEOUT);
		HttpConnectionParams.setSocketBufferSize(params, BUFFER_SIZE);
		HttpClientParams.setCookiePolicy(params, BROWSER_COMPATIBILITY);
	}

	@Override
	public void setCookieJar(CookieJar cookieJar) {
		httpClient.setCookieStore(cookieJar);
	}

	@Override
	public void authenticateBasic(String user, String password, AuthScope scope) {
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
				user, password);
		httpClient.getCredentialsProvider().setCredentials(scope, credentials);
	}

	public DefaultHttpClient getHttpClient() {
		return httpClient;
	}

	public static StringEntity buildStringEntity(String contentType, String data)
			throws HTTPException {
		try {
			StringEntity entity = new StringEntity(data, FORMAT);
			entity.setContentType(contentType);
			return entity;
		} catch (UnsupportedEncodingException e) {
			throw new HTTPException(e);
		}
	}

	public HTTPResponse getReponse(HttpUriRequest req) throws HTTPException {
		HTTPResponse response = new HTTPResponse();
		HttpResponse resp = getHttpResponse(req);
		response.code = getResponseCodeOrThrow(resp);
		response.headers = getHeaders(resp);
		response.body = HTTPInputStream.getInstance(resp).readAndClose();
		return response;
	}

	public Pair<Integer, BufferedInputStream> getInputStream(String uri)
			throws HTTPException {
		HttpGet req = new HttpGet(uri);
		HttpResponse resp = getHttpResponse(req);
		HttpEntity entity = resp.getEntity();
		// 2G limit
		int contentLength = (int) entity.getContentLength();
		HTTPInputStream is = HTTPInputStream.getInstance(resp);
		return new Pair<Integer, BufferedInputStream>(contentLength, is);
	}

	private HttpResponse getHttpResponse(HttpUriRequest req)
			throws HTTPException {
		for (String key : headers.keySet()) {
			for (String val : headers.get(key)) {
				req.addHeader(key, val);
			}
		}
		req.setHeader("Accept-Encoding", "gzip,deflate");
		req.setHeader("Content-Type", "application/json; charset=utf-8");
		try {
			return httpClient.execute(req);
		} catch (Exception e) {
			throw new HTTPException(e);
		}
	}

	private static int getResponseCodeOrThrow(HttpResponse resp)
			throws HTTPException {
		int respCode = resp.getStatusLine().getStatusCode();
		if (isErrorResponseCode(respCode)) {
			String respBody = HTTPInputStream.getInstance(resp).readAndClose();
			throw new HTTPException(respCode, respBody);
		}
		return respCode;
	}

	private static Map<String, List<String>> getHeaders(HttpResponse resp) {
		HashMap<String, List<String>> headers = new HashMap<String, List<String>>();
		for (Header header : resp.getAllHeaders()) {
			String name = header.getName();
			if (!headers.containsKey(name)) {
				headers.put(name, new ArrayList<String>());
			}
			headers.get(name).add(header.getValue());
		}
		return headers;
	}

}
