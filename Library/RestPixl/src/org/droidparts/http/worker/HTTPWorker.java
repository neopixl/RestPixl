
package org.droidparts.http.worker;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.auth.AuthScope;
import org.droidparts.http.CookieJar;

public abstract class HTTPWorker {

	protected static final int SOCKET_OPERATION_TIMEOUT = 60 * 1000;

	protected final HashMap<String, ArrayList<String>> headers = new HashMap<String, ArrayList<String>>();
	protected final String userAgent;

	public HTTPWorker(String userAgent) {
		this.userAgent = userAgent;
	}

	public final void putHeader(String key, String val) {
		if (val != null) {
			if (!headers.containsKey(key)) {
				headers.put(key, new ArrayList<String>());
			}
			headers.get(key).add(val);
		} else {
			headers.remove(key);
		}
	}

	public abstract void setCookieJar(CookieJar cookieJar);

	public abstract void authenticateBasic(String user, String password,
			AuthScope scope);

	protected static final boolean isErrorResponseCode(int responseCode) {
		return responseCode >= 400;
	}

}
