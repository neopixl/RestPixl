
package org.droidparts.http;

import static org.droidparts.http.worker.HttpURLConnectionWorker.DELETE;
import static org.droidparts.http.worker.HttpURLConnectionWorker.GET;
import static org.droidparts.http.worker.HttpURLConnectionWorker.POST;
import static org.droidparts.http.worker.HttpURLConnectionWorker.PUT;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;

import org.apache.http.auth.AuthScope;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.droidparts.http.worker.HTTPWorker;
import org.droidparts.http.worker.HttpClientWorker;
import org.droidparts.http.worker.HttpURLConnectionWorker;

import com.neopixl.logger.NPLog;
import com.neopixl.restpixl.NPRestPixlPreferences;

import android.content.Context;
import android.os.Build;
import android.util.Pair;

public class RESTClient {

	private final Context ctx;

	private final boolean forceApacheHttpClient;

	private final HttpClientWorker httpClientWorker;
	private final HttpURLConnectionWorker httpURLConnectionWorker;
	private static volatile CookieJar cookieJar;

	public static String getUserAgent(String nameHint,Context ctx) {
		
		return ((nameHint != null) ? nameHint : NPRestPixlPreferences.getPackageName(ctx))+" "+NPRestPixlPreferences.getVersionName(ctx)+ "("+String.valueOf(NPRestPixlPreferences.getVersionCode(ctx))+") "+ " (Android-RestPixl "
				+ Build.VERSION.RELEASE + "; " + Build.MODEL + " Build/"
				+ Build.ID + ")";
	}

	public RESTClient(Context ctx) {
		this(ctx, getUserAgent(null,ctx), false);
	}

	public RESTClient(Context ctx, String userAgent,
			boolean forceApacheHttpClient) {
		this.ctx = ctx.getApplicationContext();
		this.forceApacheHttpClient = forceApacheHttpClient;
		httpClientWorker = useHttpURLConnection() ? null
				: new HttpClientWorker(userAgent);
		httpURLConnectionWorker = useHttpURLConnection() ? new HttpURLConnectionWorker(
				userAgent) : null;
		if (cookieJar == null) {
			cookieJar = new CookieJar(ctx);
		}
		if (Build.VERSION.SDK_INT >= 14) {
			HttpURLConnectionWorker.setHttpResponseCacheEnabled(ctx, true);
		}
	}

	public Context getContext() {
		return ctx;
	}

	public void setCookieCacheEnabled(boolean enabled, boolean persistent) {
		cookieJar.setPersistent(persistent);
		getWorker().setCookieJar(enabled ? cookieJar : null);
	}

	public void putHeader(String key, String value) {
		getWorker().putHeader(key, value);
	}

	public void authenticateBasic(String username, String password) {
		authenticateBasic(username, password, AuthScope.ANY);
	}

	public void authenticateBasic(String username, String password,
			AuthScope scope) {
		getWorker().authenticateBasic(username, password, scope);
	}

	//

	public HTTPResponse get(String uri) throws HTTPException {
		NPLog.i("GET on " + uri);
		HTTPResponse response;
		if (useHttpURLConnection()) {
			HttpURLConnection conn = httpURLConnectionWorker.getConnection(uri,
					GET);
			response = HttpURLConnectionWorker.getReponse(conn);
		} else {
			HttpGet req = new HttpGet(uri);
			response = httpClientWorker.getReponse(req);
		}
		NPLog.d(response);
		return response;
	}

	public HTTPResponse post(String uri, String contentType, String data)
			throws HTTPException {
		NPLog.i("POST on " + uri + ", data: " + data);
		HTTPResponse response;
		if (useHttpURLConnection()) {
			HttpURLConnection conn = httpURLConnectionWorker.getConnection(uri,
					POST);
			HttpURLConnectionWorker.postOrPut(conn, contentType, data);
			response = HttpURLConnectionWorker.getReponse(conn);
		} else {
			HttpPost req = new HttpPost(uri);
			req.setEntity(HttpClientWorker.buildStringEntity(contentType, data));
			response = httpClientWorker.getReponse(req);
		}
		NPLog.d(response);
		return response;
	}

	public HTTPResponse put(String uri, String contentType, String data)
			throws HTTPException {
		NPLog.i("PUT on " + uri + ", data: " + data);
		HTTPResponse response;
		if (useHttpURLConnection()) {
			HttpURLConnection conn = httpURLConnectionWorker.getConnection(uri,
					PUT);
			HttpURLConnectionWorker.postOrPut(conn, contentType, data);
			response = HttpURLConnectionWorker.getReponse(conn);
		} else {
			HttpPut req = new HttpPut(uri);
			req.setEntity(HttpClientWorker.buildStringEntity(contentType, data));
			response = httpClientWorker.getReponse(req);
		}
		NPLog.d(response);
		return response;
	}

	public HTTPResponse delete(String uri) throws HTTPException {
		NPLog.i("DELETE on " + uri);
		HTTPResponse response;
		if (useHttpURLConnection()) {
			HttpURLConnection conn = httpURLConnectionWorker.getConnection(uri,
					DELETE);
			response = HttpURLConnectionWorker.getReponse(conn);
		} else {
			HttpDelete req = new HttpDelete(uri);
			response = httpClientWorker.getReponse(req);
		}
		NPLog.d(response);
		return response;
	}

	public Pair<Integer, BufferedInputStream> getInputStream(String uri)
			throws HTTPException {
		NPLog.i("InputStream on " + uri);
		Pair<Integer, BufferedInputStream> resp = null;
		if (useHttpURLConnection()) {
			resp = httpURLConnectionWorker.getInputStream(uri);
		} else {
			resp = httpClientWorker.getInputStream(uri);
		}
		NPLog.d("Content-Length: " + resp.first);
		return resp;
	}

	//

	protected final HTTPWorker getWorker() {
		HTTPWorker worker = (httpClientWorker != null) ? httpClientWorker
				: httpURLConnectionWorker;
		return worker;
	}

	private boolean useHttpURLConnection() {
		// http://android-developers.blogspot.com/2011/09/androids-http-clients.html
		boolean recentAndroid = Build.VERSION.SDK_INT >= 10;
		return recentAndroid && !forceApacheHttpClient;
	}
}
