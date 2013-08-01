
package org.droidparts.http;

import org.apache.http.HttpStatus;

public class HTTPException extends Exception {

	private static final long serialVersionUID = 1L;

	private int respCode = -1;

	public HTTPException(Throwable cause) {
		super(cause);
	}

	public HTTPException(int respCode, String respBody) {
		super(respBody);
		this.respCode = respCode;
	}

	/**
	 * @see HttpStatus
	 */
	public int getResponseCode() {
		return respCode;
	}

	@Override
	public String toString() {
		if (respCode != -1) {
			StringBuilder sb = new StringBuilder();
			sb.append("Response code: ");
			sb.append(respCode);
			sb.append(", body: ");
			sb.append(getMessage());
			return sb.toString();
		} else {
			return super.toString();
		}
	}

}
