package com.shiyi.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @Title 华势请求接口
 * @author yong
 * @date 2016年8月24日
 */
public class HSURLConnection {
	public static String sendHttpsPost(String url, String params) {

		DataOutputStream out = null;
		BufferedReader in = null;
		String result = "";
		URL u = null;
		HttpsURLConnection con = null;

		// 尝试发送请求
		try {
			System.out.println(params);
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
					new java.security.SecureRandom());
			u = new URL(url);
			// 打开和URL之间的连接
			con = (HttpsURLConnection) u.openConnection();
			// 设置通用的请求属性
			con.setSSLSocketFactory(sc.getSocketFactory());
			con.setHostnameVerifier(new TrustAnyHostnameVerifier());
			con.setRequestMethod("POST");
			// con.setRequestProperty("Content-Type", "application/json");
			con.setUseCaches(false);
			// 发送POST请求必须设置如下两行
			con.setDoOutput(true);
			con.setDoInput(true);

			con.connect();
			out = new DataOutputStream(con.getOutputStream());
			out.write(params.getBytes("utf-8"));
			// 刷新、关闭
			out.flush();
			out.close();
			// 读取返回内容
			// InputStream is = con.getInputStream();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(con.getInputStream(),
					"utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
				JSONObject json = JSON.parseObject(result);
				@SuppressWarnings("unused")
				String key = json.getString("key");
			}
			System.out.println(result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
				if (con != null) {
					con.disconnect();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public final static String MD5Encoder(String s, String charset) {
		try {
			byte[] btInput = s.getBytes(charset);
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < md.length; i++) {
				int val = ((int) md[i]) & 0xff;
				if (val < 16) {
					sb.append("0");
				}
				sb.append(Integer.toHexString(val));
			}
			return sb.toString();
		} catch (Exception e) {
			return null;
		}
	}

	private static class TrustAnyTrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}

	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}
	
}

