package com.neopixl.restpixlsample;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.neopixl.logger.NPLog;
import com.neopixl.restpixl.async.NPBackgroundLoadingManager;
import com.neopixl.restpixl.request.NPGETRequest;
import com.neopixl.restpixl.request.NPPOSTRequest;
import com.neopixl.restpixl.request.NPRequest;
import com.neopixl.restpixl.request.NPRequestListener;
import com.neopixl.restpixlsample.domain.Mooder;

public class MainActivity extends Activity implements NPRequestListener {

	private static String RESTPIXL_SAMPLE_GET_URL = "http://intent.alwaysdata.net/mooders.json";
	private static String RESTPIXL_SAMPLE_POST_URL = "http://posttestserver.com/post.php";


	private Context context;

	//View element
	private TextView textViewContentJson;
	private Button buttonGet;
	private Button buttonPost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setContext(this);

		textViewContentJson = (TextView) findViewById(R.id.txtViewJsonContent);
		buttonGet = (Button) findViewById(R.id.buttonGet);
		buttonGet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				launchGETRequest();

			}
		});
		buttonPost = (Button) findViewById(R.id.buttonPost);
		buttonPost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				launchPOSTRequest();

			}
		});


	}

	private void launchGETRequest()
	{
		textViewContentJson.setText("");
		//Get Request
		NPGETRequest request = new NPGETRequest(getContext(),RESTPIXL_SAMPLE_GET_URL,null,true,null,null,null);

		request.setListener(this);

		NPBackgroundLoadingManager.queueInteractiveRequest(request);
	}

	private void launchPOSTRequest()
	{
		textViewContentJson.setText("");
		//Post Request

		Mooder aMooder = new Mooder("id", "Demolliens", "Olivier", "http://noUrl", null, null);

		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();

		String value = gson.toJson(aMooder);

		NPPOSTRequest request = new NPPOSTRequest(getContext(),RESTPIXL_SAMPLE_POST_URL,null,true,null,value,null);

		request.setListener(this);

		NPBackgroundLoadingManager.queueInteractiveRequest(request);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public void requestFailedNoNetwork(NPRequest rQ) {
		NPLog.e("");
		textViewContentJson.setText("No network");
	}


	@Override
	public void requestFailedWithErrorCode(NPRequest rQ) {
		NPLog.e("");
		textViewContentJson.setText(rQ.getHttpCode());
	}


	@Override
	public void requestSuccess(NPRequest rQ) {
		NPLog.i("");

		if(rQ.getUrl().equals(RESTPIXL_SAMPLE_GET_URL)){
			//
			Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();

			List<Mooder> mooderList = gson.fromJson(rQ.getmResponse().body, new TypeToken<List<Mooder>>(){}.getType());

			textViewContentJson.setText(mooderList.toString());
			mooderList = null;
		}else if(rQ.getUrl().equals(RESTPIXL_SAMPLE_POST_URL)){
			textViewContentJson.setText(rQ.getmResponse().body);
		}
	}


	public Context getContext() {
		return context;
	}


	public void setContext(Context context) {
		this.context = context;
	}

}
