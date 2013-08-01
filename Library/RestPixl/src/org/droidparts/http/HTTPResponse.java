
package org.droidparts.http;

import java.util.List;
import java.util.Map;

public class HTTPResponse {

	public int code;
	public String body;
	public Map<String, List<String>> headers;

	@Override
	public int hashCode() {
		return (code + body).hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o instanceof HTTPResponse) {
			return hashCode() == o.hashCode();
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "Response code: " + code + ", body: " + body;
	}

}
