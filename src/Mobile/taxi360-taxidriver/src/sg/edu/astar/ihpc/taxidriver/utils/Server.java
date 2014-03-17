package sg.edu.astar.ihpc.taxidriver.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import sg.edu.astar.ihpc.taxidriver.entity.Response;
import android.util.Log;

public class Server {
	
	public static final String POST="POST";
	public static final String PUT="PUT";
	public static final String DELETE="DELETE";
	public static final String GET="GET";
	public static final String HEAD="HEAD";
	public static final String TRACE="TRACE";
	public static final String TAG="Server.java";
	
	//private CheckConnectivity server;
	private static Server instance=null;
	private Response response;
	private org.codehaus.jackson.map.ObjectWriter writer;
	private String request;
	private DefaultHttpClient httpClient;
	private StringBuilder stringBuilder;
	private HttpResponse httpResponse;
	private HttpDelete httpDelete;
	private HttpGet httpGet;
	private HttpPost httpPost;
	private HttpEntity httpEntity;
	private HttpPut httpPut;
	private StringEntity se;
	private boolean isBody;
	
	private Server(){
		//server= new CheckConnectivity();
	}
	
	public static synchronized Server getInstance() {
		if (instance == null) {
			instance = new Server ();
			instance.writer=new ObjectMapper().writer().withDefaultPrettyPrinter();
		}
		return instance;
	}
	
	public Response connect(String type, String url, Object body){
		
		try {
			isBody=true;
			response= new Response();
			request=writer.writeValueAsString(body);
			Log.d(TAG,"type="+type+", url= "+url+", request= "+request);
			se = new StringEntity( request);  
	        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			httpClient = new DefaultHttpClient();
			stringBuilder = new StringBuilder();
			callHttpMethod(type, url);
			response.setCode(httpResponse.getStatusLine().getStatusCode());
			response.setStatus(checkResponseStatus(httpResponse));
			if(stringBuilder.length()!=0)
				response.setResponse(stringBuilder.toString());
			
		} catch (JsonGenerationException e) {
			Log.d(TAG,"JsonGenerationException= "+e.getMessage());
		} catch (JsonMappingException e) {
			Log.d(TAG,"JsonMappingException= "+e.getMessage());
		} catch (IOException e) {
			Log.d(TAG,"IOException= "+e.getMessage());
		} catch (Exception e) {
			Log.d(TAG,"Exception= "+e.getMessage());
		}

		Log.d(TAG,"type="+type+", url= "+url+", response= "+response);
		return response;
	}
	
	private void getRequest(String url) throws Exception{
		httpGet = new HttpGet(url);
		httpResponse = httpClient.execute(httpGet);
        this.getResponse();
	}
	
	private void postRequest(String url) throws ClientProtocolException, IOException  {
		httpPost  = new HttpPost(url);
		if(isBody)
			httpPost.setEntity(se);
        httpResponse = httpClient.execute(httpPost);
        this.getResponse();
	}
		
	private void putRequest(String url) throws ClientProtocolException, IOException  {
		httpPut=new HttpPut(url);
		if(isBody)
			httpPut.setEntity(se);
		httpResponse = httpClient.execute(httpPut);
        this.getResponse();		
	}
		
	private void deleteRequest(String url) throws ClientProtocolException, IOException{
		httpDelete = new HttpDelete(url);
		httpResponse = httpClient.execute(httpDelete);	
        this.getResponse();		
	}
	
	private void headRequest(String url ) throws Exception{
		throw new Exception("Yet to be Coded.");
	}	
	private void traceRequest(String url) throws Exception{
		throw new Exception("Yet to be Coded.");
	}
	
	private void getResponse() throws IllegalStateException, IOException{
        /*Checking response */
        if(httpResponse!=null){
    		httpEntity = httpResponse.getEntity();
            InputStream in = httpEntity.getContent(); 
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            in.close();//Get the data in the entity
            Log.d(TAG, "response="+stringBuilder.toString());
        }
	}

	
	//HTTP check response for the Request method
	public Boolean checkResponseStatus(HttpResponse httpResponse2){
		if ((httpResponse2.getStatusLine().getStatusCode())>=200&&(httpResponse2.getStatusLine().getStatusCode())<300)
			return true;
		else
			return false;		
	}
	
	public Response connect(String type, String url){
		isBody=false;
		response= new Response();
		try {
			httpClient = new DefaultHttpClient();
			stringBuilder = new StringBuilder();
			Log.d(TAG,"type="+type+", url= "+url);
			callHttpMethod(type, url);
			response.setCode(httpResponse.getStatusLine().getStatusCode());
			response.setStatus(checkResponseStatus(httpResponse));
			if(stringBuilder.length()!=0)
				response.setResponse(stringBuilder.toString());
		} catch (Exception e) {
			Log.d(TAG,"Exception= "+e.getMessage());
		}
		return response;
	}
	
	private void callHttpMethod(String type, String url) throws Exception{
			if(GET.equals(type))
				getRequest(url);
			else if(POST.equals(type))
				postRequest(url);
			else if(PUT.equals(type))
				putRequest(url);
			else if(DELETE.equals(type))
				deleteRequest(url);
			else if(HEAD.equals(type))
				headRequest(url);
			else if(TRACE.equals(type))
				traceRequest(url);
			else 
				throw new Exception("Unknown Type Exception");		
	}
	




	//Used to check whether able to connect to server
	public boolean isConnectedToServer() {
		
		try{
			String url="http://137.132.247.133:8080/taxi360-war/api/common/validateOTP";
			int timeout=5000;
	        URL myUrl = new URL(url);
	        URLConnection connection = myUrl.openConnection();
	        connection.setConnectTimeout(timeout);
	        connection.connect();
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}

}
