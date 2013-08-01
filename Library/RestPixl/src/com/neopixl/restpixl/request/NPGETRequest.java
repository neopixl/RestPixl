package com.neopixl.restpixl.request;

import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.neopixl.restpixl.NPRequestEnum;
import com.neopixl.restpixl.async.NPParamResolver;

/**
 * NPGETRequest
 * @author Olivier Demolliens - Neopixl
 * Easy GET request
 */

public class NPGETRequest extends NPRequest{

	/**
	 * <br>NPGETRequest</br>
	 * <br>Used with <b><em>GET</em></b> request</br>
	 * @param <b>context</b> Activity (callbacks)
	 * @param <b>url</b> Url service
	 * @param <b>resolvers</b> Parameter(s) in URL
	 * @param <b>encode</b> encode url with FORMAT paramater in {@link Conf}
	 * @param <b>user</b> auth user
	 * @param <b>pwd</b> auth password
	 * @param <b>headers</b> requests header(s)
	 */
	public NPGETRequest(Context context, String url,
			List<NPParamResolver> resolvers, boolean encode, String user,
			String pwd, HashMap<String, String> headers) {
		super(context, url, resolvers, encode, user, pwd, headers, null,NPRequestEnum.GET);
	}

	/**
	 * <br>rebuildNPGETRequestWith(NPGETRequest request, NPRequestDelegate delegate)</br>
	 * <br>Use to rebuild NPDELETERequest object</br>
	 * @param <b>request</b> already launched
	 * @param <b>delegate</b> activity callback
	 * @return new NPGETRequest with parameter from other NPGETRequest
	 */
	public static NPGETRequest rebuildNPGETRequestWith(NPGETRequest request, NPRequestListener delegate){
		return new NPGETRequest(request.getContext(), request.getUrl(), request.getResolvers(), request.isEncode(), request.getUser(), request.getPwd(), request.getmHeaders());
	}
}