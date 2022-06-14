package com.kp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class CsvReader {

	public static void main(String args[]) throws Exception {
		
		File input = new File("C:\\Users\\KAPSTONE\\Desktop\\employee.csv");
		try {
			CsvSchema csv = CsvSchema.emptySchema().withHeader();
			CsvMapper csvMapper = new CsvMapper();
			MappingIterator<Map<?, ?>> mappingIterator = csvMapper.readerFor(Map.class)
					.with(csv.withColumnSeparator(',')).readValues(input);

			List<Map<?, ?>> jsonOut = mappingIterator.readAll();
			JSONArray arr = new JSONArray(jsonOut);
			System.out.println("json Array is ..."+arr);
			
				

				// HTTP Post request
				String data = arr.toString();
				// System.out.println(data);
				//byte[] postData = data.getBytes(Charset.forName("UTF-8"));
				System.out.println("post data" + data);

				String url = "http://localhost:8082/kapstone/insertUser";
				URL obj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();

				// Setting basic post request
				con.setRequestMethod("POST");
				//con.setRequestProperty("Accept-Language", "en-US,UTF-8");
				con.setRequestProperty("Content-Type", "application/json");
				//con.setRequestProperty("Accept", "*/*");
				con.setRequestProperty("Connection", "Keep-Alive");
				con.setRequestProperty("cache-control", "no-cache");
				con.setDoOutput(true);
				
				// Send post request

				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes(data);
				wr.flush();
				wr.close();
				// conditions for Response Code
				System.out.println("response status = " + con.getResponseCode());
				if (con.getResponseCode() != 200 && con.getResponseCode() != 201) {
					BufferedReader inPutError = new BufferedReader(new InputStreamReader(con.getErrorStream()));
					String inputLine;
					StringBuffer resp = new StringBuffer();

					while ((inputLine = inPutError.readLine()) != null) {
						resp.append(inputLine);
					}
					inPutError.close();
					String errRes = resp.toString();
					System.out.println("ERROR RESPONSE = " + errRes);
					JSONObject errResObject = new JSONObject(errRes);
					System.out.println(errResObject);
				} else {

					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer resp = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						resp.append(inputLine);	
					}
					in.close();
					System.out.println("user created in mongo Db response...."+resp.toString());
					System.out.println("Sending 'POST' request to URL : " + url);
					System.out.println("Post Data : " + data);
				}
			

		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}
