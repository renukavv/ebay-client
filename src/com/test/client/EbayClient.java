package com.test.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

public class EbayClient {

	public String postMessage() throws IOException {

		final StringBuffer content = new StringBuffer();
		URL url = new URL("http://svcs.ebay.com/services/search/FindingService/v1");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/JSON");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("X-EBAY-SOA-OPERATION-NAME", "findItemsByProduct");
		conn.setRequestProperty("X-EBAY-SOA-SECURITY-APPNAME", "**************");
		conn.setRequestProperty("X-EBAY-SOA-GLOBAL-ID", "EBAY-US");
		conn.setRequestProperty("X-EBAY-SOA-SERVICE-VERSION", "1.0.0");
		conn.setRequestProperty("X-EBAY-SOA-REQUEST-DATA-FORMAT", "XML");
		conn.setRequestProperty("X-EBAY-SOA-RESPONSE-DATA-FORMAT", "JSON");
		

		// Send  the Post request
		conn.setDoOutput(true);

		DataOutputStream os = new DataOutputStream(conn.getOutputStream());

		final String input = "<findItemsByProductRequest xmlns=\"http://www.ebay.com/marketplace/search/v1/services\">\r\n"
				+ "<itemFilter> ItemFilter\r\n" + "    <name> MinPrice </name>\r\n"
				+ "    <paramName> Currency </paramName>\r\n" + "    <paramValue> USD </paramValue>\r\n"
				+ "    <value> 100 </value>\r\n" + "  </itemFilter>\r\n"
				+ "  <productId type=\"UPC\">753759077600</productId>\r\n" + "</findItemsByProductRequest>";
		os.writeBytes(input);
		os.flush();
		os.close();

		// Read the Response
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		String output;

		while ((output = br.readLine()) != null) {
			content.append(output);
		}
		br.close();

		return content.toString();

	}

	public static void main(String[] args) throws JSONException {

		try {
			EbayClient instance = new EbayClient();
			String response = instance.postMessage();

			JSONObject result = new JSONObject(response);

			System.out.println("Title: "+result.getJSONArray("findItemsByProductResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getJSONArray("item").getJSONObject(0).getJSONArray("title").getString(0));
			System.out.println("Current Price: "+
					result.getJSONArray("findItemsByProductResponse").getJSONObject(0).getJSONArray("searchResult")
							.getJSONObject(0).getJSONArray("item").getJSONObject(0).getJSONArray("sellingStatus")
							.getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).get("__value__"));

		} catch (IOException ex) {
			System.out.println(ex);
		}

	}
}
