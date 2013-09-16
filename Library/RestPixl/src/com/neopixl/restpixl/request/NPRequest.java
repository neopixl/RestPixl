package com.neopixl.restpixl.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.droidparts.http.HTTPException;
import org.droidparts.http.HTTPResponse;

import android.content.Context;
import android.util.Log;

import com.neopixl.restpixl.NPRESTManager;
import com.neopixl.restpixl.NPRequestEnum;
import com.neopixl.restpixl.async.NPAsynchTask;
import com.neopixl.restpixl.async.NPParamResolver;

public class NPRequest extends NPAsynchTask<String, Float, Void>{

	//HTTP Protocol
	private static final String NP_HTTP_PROTOCOL = "http://";
	private static final String NP_HTTPS_PROTOCOL = "https://";

	/**
	 * Url service
	 */
	private String url;

	/**
	 * Delegate
	 */
	private Context context;

	/**
	 * Encode url
	 */
	private boolean encode;

	/**
	 * Auth
	 */
	private String user;
	private String pwd;

	/**
	 * Parameter in URL
	 */
	private List<NPParamResolver> resolvers;

	/**
	 * If request finished (can be finished with error)
	 */
	private boolean finish;

	/**
	 * Headers requests
	 */
	private HashMap<String, String> mHeaders;

	/**
	 * method (GET/POST/PUT/DELETE)
	 */
	private NPRequestEnum method;

	/**
	 * Rest manager
	 */
	private NPRESTManager mRestManager;

	/**
	 * Data input
	 */
	private String data;

	/**
	 * Response
	 */

	private HTTPResponse mResponse;

	/**
	 * Response http code
	 * http://en.wikipedia.org/wiki/List_of_HTTP_status_codes
	 */

	private int httpCode = -2;

	/**
	 * Delegate
	 */

	private NPRequestListener listener;
	
	private String contentType;

	/**
	 * Don't use this class directly. Use child (NPGETRequest,NPPOSTRequest,NPPUTRequest,NPDELETERequest)
	 * @param <b>context</b> Activity (callbacks)
	 * @param <b>url</b> Url service
	 * @param <b>resolvers</b> Parameter(s) in URL
	 * @param <b>encode</b> encode url with FORMAT paramater in {@link Conf}
	 * @param <b>user</b> auth user
	 * @param <b>pwd</b> auth password
	 * @param <b>headers</b> requests header(s)
	 * @param <b>data</b> request data (json for example)
	 * @param <b>method</b> GET/POST/PUT/DELETE
	 */

	public NPRequest(Context context, String newUrl,List<NPParamResolver> resolvers, 
			boolean mEncode, String user, String pwd, HashMap<String, String> headers, String data, NPRequestEnum method) {
		super();

		if(context != null){
			setContext(context);
		}else{
			Log.w("RestPixl","no context setted");
		}

		if(newUrl != null){
			if(!newUrl.contains(NP_HTTP_PROTOCOL)){
				if(!newUrl.contains(NP_HTTPS_PROTOCOL)){
					String protocolAdded = NP_HTTP_PROTOCOL;
					Log.i("RestPixl","no protocol "+protocolAdded.replace("/", "").replace(":", "")+" founded. Added "+protocolAdded+" protocol for url:"+newUrl);
					setUrl(protocolAdded+newUrl);
				}else{
					setUrl(newUrl);
				}
			}else{
				setUrl(newUrl);
			}
		}

		if(resolvers!=null){
			setResolvers(resolvers);
		}
		
		//Default type
		setContentType("application/json; charset=utf-8");

		setEncode(mEncode);

		if(user != null){
			setUser(user);
		}else{
			//Used to prevent if last request used auth
			setUser("");
		}

		if(pwd != null){
			setPwd(pwd);
		}else{
			//Used to prevent if last request used auth
			setPwd("");
		}

		if(headers!=null){
			setmHeaders(headers);
		}else{
			setmHeaders(new HashMap<String, String>());
		}

		if(method!=null){
			setMethod(method);
		}else{
			Log.w("RestPixl","no request method found");
		}

		if(data!=null){
			setData(data);
		}
	}

	@Override
	protected Void doInBackground(String... params) {
		call();
		return null;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);

		if(getListener()==null){
			return;
		}

		if(getHttpCode()==0){
			//No network
			getListener().requestFailedNoNetwork(this);
		}else if(getHttpCode()==200){
			//Success
			getListener().requestSuccess(this);
		}else{
			//!=200
			//Failed
			getListener().requestFailedWithErrorCode(this);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Float... values) {
		super.onProgressUpdate(values);
	}

	/**
	 * Add param in header request (example: Key -> "Accept-Encoding"  Value ->"compress, gzip")
	 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html
	 * @param headers
	 */

	public void addHeaders(HashMap<String, String> headers) {
		if(headers==null) {
			return;
		}else{
			getmHeaders().putAll(headers);

			Log.i("RestPixl","headers : "+mHeaders);
		}
	}

	/**
	 * Public Solve url with parameter
	 * @return solved url with List<ParamResolver> resolvers
	 */

	public String prepareRequestUrlToSend()
	{
		return resolvedUrl(isEncode());
	}

	/**
	 * Call webService + set response & http code response
	 */

	private void call()
	{
		String userUsed = getUser();
		String pwdUsed = getPwd();

		if(userUsed != null && pwdUsed != null){
			getRESTManager().getRestClient().authenticateBasic(userUsed, pwdUsed);
		}

		String urlCalled = prepareRequestUrlToSend();

		try {

			switch (getMethod()) {

			case GET:
				setmResponse(getRESTManager().getRestClient().get(urlCalled));

				break;

			case POST:
				
				for(String key : getmHeaders().keySet()) {
					NPRESTManager.getInstance(getContext()).getRestClient().putHeader(key, getmHeaders().get(key));
				}
				setmResponse(getRESTManager().getRestClient().post(urlCalled, getContentType(), getData()));

				break;

			case PUT:
				for(String key : getmHeaders().keySet()) {
					NPRESTManager.getInstance(getContext()).getRestClient().putHeader(key, getmHeaders().get(key));
				}

				setmResponse(getRESTManager().getRestClient().put(urlCalled, getContentType(), getData()));

				break;

			case DELETE:

				for(String key : getmHeaders().keySet()) {
					NPRESTManager.getInstance(getContext()).getRestClient().putHeader(key, getmHeaders().get(key));
				}

				setmResponse(getRESTManager().getRestClient().delete(urlCalled));

				break;

			default:
				//By default, we perform a GET method
				setmResponse(getRESTManager().getRestClient().get(urlCalled));

				break;

			}

			setHttpCode(200);

		} catch (HTTPException e) {
			// != 200
			setHttpCode(e.getResponseCode());

			Log.e("RestPixl","http error : "+ getHttpCode() + " url : "+resolvedUrl(isEncode()));

			e.printStackTrace();

		}

		setFinish(true);

	}

	/**
	 * Add <b>content type</b> in request header
	 * @return "application/json; charset= {@link Conf}.FORMAT
	 */
	private String getContentType()
	{
		return contentType;
	}

	/**
	 * Encode url with {@link Conf}.FORMAT
	 * @param Url to encode
	 * @return url encoded
	 */

	private String encodeURIcomponent(String s)
	{
		try {
			return URLEncoder.encode(s, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Private Solve url with parameter
	 * @return solved url with List<ParamResolver> resolvers
	 */

	private String resolvedUrl(boolean encode) {
		String u = this.url;
		List<NPParamResolver> pR = getResolvers();
		if (pR != null) {
			for (NPParamResolver resolver : pR){
				if(encode){
					u = u.replace(resolver.paramKey(), encodeURIcomponent(resolver.value()));
				}else{
					u = u.replace(resolver.paramKey(), resolver.value());
				}

			}
		}
		return u;
	}

	/**
	 * Return Activity which implement NPRequestDelegate
	 * @return Activity if context setted
	 */

	private NPRequestListener getListener()
	{
		if(this.listener != null){
			return listener;
		}

		if(getContext() != null){
			try{
				return (NPRequestListener)getContext();
			}catch(ClassCastException e){
				Log.i("RestPixl","Warn, "+NPRequestListener.class.getName()+" doesn't implement in "+getContext());
				return null;
			}

		}else{
			return null;
		}
	}

	/**
	 * 
	 * @param delegate
	 */
	public void setListener(NPRequestListener listener) {
		this.listener = listener;
	}

	/**
	 * RESTManager which contains RestClient
	 * Used to send <b>GET/POST/PUT/DELETE</b> request
	 * @return NPRESTManager object
	 */

	private NPRESTManager getRESTManager()
	{
		NPRESTManager manager = getmRestManager();

		if(manager != null){
			return manager;
		}else{
			if(getContext() != null){
				setmRestManager(NPRESTManager.getInstance(getContext()));
				return getmRestManager();
			}else{
				Log.i("RestPixl",NPRESTManager.class.getName() +" need a context to work!");
				return null;
			}
		}
	}

	/**
	 * getUrl()
	 * @return <b>String</b> Get Unresolved Url
	 */

	public String getUrl() {
		return url;
	}

	/**
	 * isFinish()
	 * @return <b>boolean</b> return <b>true</b> if request is finished
	 */

	public boolean isFinish() {
		return finish;
	}

	/**
	 * getContext()
	 * @return <b>Context</b> get request Context
	 */

	public Context getContext() {
		return context;
	}

	/**
	 * getmResponse()
	 * @return <b>HTTPResponse</b> get request response
	 */

	public HTTPResponse getmResponse() {
		return mResponse;
	}

	/**
	 * getHttpCode()
	 * @return <b>int</b> get http code response
	 */

	public int getHttpCode() {
		return httpCode;
	}

	public boolean haveData()
	{
		if(getData()!=null){
			return true;
		}else{
			return false;
		}
	}

	public boolean isEncode() {
		return encode;
	}

	public List<NPParamResolver> getResolvers() {
		return resolvers;
	}

	public HashMap<String, String> getmHeaders() {
		return mHeaders;
	}

	public String getUser() {
		return user;
	}

	public String getPwd() {
		return pwd;
	}

	public String getData() {
		return data;
	}

	private void setUrl(String url) {
		this.url = url;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	private void setEncode(boolean encode) {
		this.encode = encode;
	}

	private void setUser(String user) {
		this.user = user;
	}

	private void setPwd(String pwd) {
		this.pwd = pwd;
	}

	private void setResolvers(List<NPParamResolver> resolvers) {
		this.resolvers = resolvers;
	}

	private void setFinish(boolean finish) {
		this.finish = finish;
	}

	private void setContext(Context context) {
		this.context = context;
	}

	private void setmHeaders(HashMap<String, String> mHeaders) {
		this.mHeaders = mHeaders;
	}

	private NPRequestEnum getMethod() {
		return method;
	}

	private void setMethod(NPRequestEnum method) {
		this.method = method;
	}

	private void setmResponse(HTTPResponse mResponse) {
		this.mResponse = mResponse;
	}

	private NPRESTManager getmRestManager() {
		//Use getRESTManager()
		return mRestManager;
	}

	private void setmRestManager(NPRESTManager mRestManager) {
		this.mRestManager = mRestManager;
	}

	private void setData(String data) {
		this.data = data;
	}

	private void setHttpCode(int httpErrorCode) {
		this.httpCode = httpErrorCode;
	}

}
