package com.mozat.morange.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

public class HttpUtil {
	private static final Logger log = Logger.getLogger("sys");

	private static int timeOut = 3 * 1000;

	public static String doGet(String link) throws Exception {
		log.info(String.format("doGet %s", link));
		StringBuilder sb = new StringBuilder();
		HttpURLConnection conn = null;
		BufferedReader br = null;
		try {
			URL url = new URL(link);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(timeOut);
			// conn.setReadTimeout(timeOut);
			conn.connect();
			br = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), Charset.forName("UTF-8")));
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (MalformedURLException e) {
			log.error("[doGet] link : " + link);
			log.error("[doGet] exception : ", e);
			e.printStackTrace();
			throw e;

		} catch (IOException e) {
			log.error("[doGet] link : " + link);
			log.error("[doGet] exception : " + e);
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (Exception e) {
				log.error("[doGet] exception : " + e);
			}
			try {
				if (conn != null)
					conn.disconnect();
			} catch (Exception e) {
				log.error("[doGet] exception : " + e);
			}

		}
		return sb.toString();
	}

	public static String doPost2(String link, byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		HttpURLConnection conn = null;
		BufferedReader br = null;
		DataOutputStream wr = null;
		try {
			URL url = new URL(link);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(timeOut);

			wr = new DataOutputStream(conn.getOutputStream());
			wr.write(bytes);
			wr.flush();
			br = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), Charset.forName("UTF-8")));
			String data;
			while ((data = br.readLine()) != null) {
				sb.append(data);
			}
		} catch (MalformedURLException e) {
			log.error("[doPost] exception : " + e);
			e.printStackTrace();

		} catch (IOException e) {
			log.error("[doPost] exception : " + e);
			e.printStackTrace();
		} finally {
			try {
				if (wr != null)
					wr.close();
			} catch (Exception e) {
				log.error("[doGet] exception : " + e);
			}
			try {
				if (br != null)
					br.close();
			} catch (Exception e) {
				log.error("[doGet] exception : " + e);
			}
			try {
				if (conn != null)
					conn.disconnect();
			} catch (Exception e) {
				log.error("[doGet] exception : " + e);
			}

		}
		return sb.toString();
	}

	public static String doPost(String link, String param) {
		StringBuilder sb = new StringBuilder();
		System.out.println("doPost " + link);
		HttpURLConnection conn = null;
		BufferedReader br = null;
		PrintWriter pw = null;
		try {
			URL url = new URL(link);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(timeOut);
			pw = new PrintWriter(conn.getOutputStream());
			pw.write(param);
			pw.flush();
			br = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), Charset.forName("UTF-8")));
			String data;
			while ((data = br.readLine()) != null) {
				sb.append(data);
			}
		} catch (MalformedURLException e) {
			log.error(String.format("[doPost] link : %s params : %s", link,
					param));
			log.error("[doPost] exception : " + e);
			e.printStackTrace();

		} catch (IOException e) {
			log.error(String.format("[doPost] link : %s params : %s", link,
					param));
			log.error("[doPost] exception : " + e);
			e.printStackTrace();
		} finally {
			try {
				if (pw != null)
					pw.close();
			} catch (Exception e) {
				log.error("[doGet] exception : " + e);
			}
			try {
				if (br != null)
					br.close();
			} catch (Exception e) {
				log.error("[doGet] exception : " + e);
			}
			try {
				if (conn != null)
					conn.disconnect();
			} catch (Exception e) {
				log.error("[doGet] exception : " + e);
			}

		}
		return sb.toString();
	}
}
