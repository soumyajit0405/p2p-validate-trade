

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpConnectorHelper {
	private static final String USER_AGENT = "Mozilla/5.0";

	private static final String GET_URL = "https://localhost:9090/SpringMVCExample";

	private static final String POST_URL = "http://168.61.22.57:6380/createuser";

	
	public static void main(String[] args) throws IOException, JSONException {

		// sendGET();
		System.out.println("GET DONE");
		JSONObject inputDetails1 = new JSONObject(); 
		inputDetails1.put("orderid", "fe9d87fd36aaf234645e2294f69f4ea78b6830d0ad374d5d575bdae39e48d5cde3a3a8");
		
		 new HttpConnectorHelper().sendPostWithTokenForOrder("http://159.89.175.110:6380/getOrder",inputDetails1,1,"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiJmZTlkODc0MmZhNzA1NjYwMzI1OWJjZGVmMGIwY2JmMTM4ZTNjY2U3ZmYxOGZiNjY3NzMzOWEyZGQ2YTY3MTE3YTk5N2NiIiwiaWF0IjoxNTg5NDYyMjk5fQ.E3_NdDQxgg9OcJAG7c8hDgvSmX7Xw7jlOGRvR3VJ348");
		System.out.println("POST DONE");
	}
	public HashMap<String,String> sendPost(String url, JSONObject params, int paramsFlag) throws IOException {
		HashMap<String,String> map = new HashMap<>();
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
	//	con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		System.out.println(params.toString());
		os.write(params.toString().getBytes());	
		os.flush();
		os.close();
		if(paramsFlag  > 0) {
			
		} else {
			
		}
		
		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			inputLine = response.toString().substring(1, response.toString().length()-1);           //remove curly brackets
			String[] keyValuePairs = inputLine.split(",");              //split the string to creat key-value pairs
			System.out.println("test "+inputLine.toString());

			for(String pair : keyValuePairs)                        //iterate over the pairs
			{
				
				String[] entry = pair.split(":");      
				//split the pairs to get key and value
				String[] keys = entry[0].split("\"");
				String[] values = entry[1].split("\"");
				map.put(keys[1].trim(), values[1].trim());
			    
			    //add them to the hashmap and trim whitespaces
			}
			in.close();
			
			// print result
			System.out.println(response.toString());
		} else {
			System.out.println("POST request not worked");
		}
		return map;
	}
	
	public JSONObject sendRequestToAgent(String url, JSONObject params, int paramsFlag) throws IOException,JSONException {
		JSONObject responseFromAgent = null;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
	//	con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		System.out.println(params.toString());
		os.write(params.toString().getBytes());	
		os.flush();
		os.close();
		if(paramsFlag  > 0) {
			
		} else {
			
		}
		
		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			responseFromAgent = new JSONObject(response.toString());
						
			in.close();
			
			// print result
			System.out.println(response.toString());
		} else {
			System.out.println("POST request not worked");
			return responseFromAgent;
		}
		return responseFromAgent;
	}
	
	public HashMap<String,String> sendPostWithToken(String url, JSONObject params, int paramsFlag, String token) throws IOException {
		HashMap<String,String> map = new HashMap<>();
		URL obj = new URL(url);
		System.out.println(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		//con.setRequestProperty("Authorization", "Bearer "+token);
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		System.out.println(params.toString());
		os.write(params.toString().getBytes());	
		os.flush();
		os.close();
		if(paramsFlag  > 0) {
			
		} else {
			
		}
		
		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			inputLine = response.toString().substring(1, response.toString().length()-1);           //remove curly brackets
			String[] keyValuePairs = inputLine.split(",");              //split the string to creat key-value pairs
			              

			for(String pair : keyValuePairs)                        //iterate over the pairs
			{
				
				String[] entry = pair.split(":");      
				//split the pairs to get key and value
				String[] keys = entry[0].split("\"");
				String[] values = entry[1].split("\"");
				map.put(keys[1].trim(), values[1].trim());
			    
			    //add them to the hashmap and trim whitespaces
			}
			in.close();
			con.disconnect();
			
			// print result
			System.out.println(response.toString());
		} else {
			System.out.println("POST request not worked");
		}
		return map;
	}
	
	public JSONObject sendPostWithTokenForOrder(String url, JSONObject params, int paramsFlag, String token) throws IOException {
		HashMap<String,String> map = new HashMap<>();
		JSONObject jsonObject = null;
		try {
		String splitToken ="\"Status\""+":"+"\"ORDER_EXIST\""+","+"\"ORDER_DATA\""+":{";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", "Bearer "+token);
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		System.out.println(params.toString());
		os.write(params.toString().getBytes());	
		os.flush();
		os.close();
		if(paramsFlag  > 0) {
			
		} else {
			
		}
		
		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			 jsonObject = new JSONObject(response.toString());
			 jsonObject = (JSONObject)(jsonObject.get("ORDER_DATA"));
			 
			in.close();
			con.disconnect();
			
			// print result
			System.out.println(response.toString());
		} else {
			System.out.println("POST request not worked");
		}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

}