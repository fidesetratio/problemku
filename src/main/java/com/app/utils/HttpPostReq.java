package com.app.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpPostReq {
	String keyAuthGoogle = "key=AAAAA_ObTcA:APA91bF_HaNxukHJ15wKgyrg0yrNVubFxel4uRXg0ZJZfvxjPfmmRUn0RX0_uKG_bToszxLoEd--bJYrKkvzQmRP7FbDAwHq5COiOJpZkSCgeJZQly0bE4VYljZSVHoTKHuBDyptu8OY";

	private static final Logger logger = LogManager.getLogger(HttpPostReq.class);

	public HttpPost createConnectivityGoogle(String restUrl) {
		HttpPost post = new HttpPost(restUrl);
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Authorization", keyAuthGoogle);
		post.setHeader("Accept", "application/json");
		post.setHeader("X-Stream", "true");
		return post;
	}

	public HttpPost createConnectivity(String restUrl) {
		HttpPost post = new HttpPost(restUrl);
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Accept", "application/json");
		post.setHeader("X-Stream", "true");
		return post;
	}

	public String executeReq(String jsonData, HttpPost httpPost) {
		String result = "";
		try {
			result = executeHttpRequest(jsonData, httpPost);
		} catch (UnsupportedEncodingException e) {
			logger.error("error while encoding api url : " + e);
		} catch (IOException e) {
			logger.error("ioException occured while sending http request : " + e);
		} catch (Exception e) {
			logger.error("exception occured while sending http request : " + e);
		} finally {
			httpPost.releaseConnection();
		}
		return result;
	}

	public String executeHttpRequest(String jsonData, HttpPost httpPost)
			throws UnsupportedEncodingException, IOException {
		HttpResponse response = null;
		String line = "";
		StringBuffer result = new StringBuffer();
		httpPost.setEntity(new StringEntity(jsonData));
		HttpClient client = HttpClientBuilder.create().build();
		response = client.execute(httpPost);
		logger.info("Post parameters : " + jsonData);
		logger.info("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		while ((line = reader.readLine()) != null) {
			result.append(line);
		}
		logger.info(result.toString());

		return result.toString();
	}
}