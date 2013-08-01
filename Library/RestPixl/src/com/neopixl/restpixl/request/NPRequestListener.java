package com.neopixl.restpixl.request;

/**
 * NPRequestDelegate
 * Request delegate method
 * @author odemolliens
 * Neopixl
 */
public interface NPRequestListener {
	/**
	 * If request success (http code 200)
	 * @param request
	 */
	public void requestSuccess(NPRequest request);
	/**
	 * If request failed with http error code (http code !=200 & http code !=0)
	 * @param request
	 */
	public void requestFailedWithErrorCode(NPRequest request);
	/**
	 * If request failed because no network (http code == 0)
	 * @param request
	 */
	public void requestFailedNoNetwork(NPRequest request);
}
