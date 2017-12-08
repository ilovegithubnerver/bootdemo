package com.shiyi.service.impl;


import com.shiyi.service.CheckCodeService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 验证码保存工具服务
 * 
 * sms code存储在redis服务器中，key格式：emp:smscode:userflag:tel, value: 短信验证码
 * 
 * @author heqingqi
 * */
@Service("checkCodeService")
public class CheckCodeServiceImpl implements InitializingBean, DisposableBean, CheckCodeService {

	private static Logger logger = Logger.getLogger(CheckCodeServiceImpl.class);

	private Hashtable<String, AtomicInteger> lockIp = new Hashtable<String, AtomicInteger>();
	
	private Object lock = new Object();
	

	


	private  final   String postUrl="http://cf.51welink.com/submitdata/service.asmx/g_Submit";
	
	private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

	public int incrementAndGet(String ip) {
		AtomicInteger count = lockIp.get(ip);
		if (count == null) {
			synchronized (lock) {
				count = lockIp.get(ip);
				if (count == null) {
					lockIp.put(ip, new AtomicInteger(1));
					return 1;
				} else {
					return count.incrementAndGet();
				}
			}
		} else
			return count.incrementAndGet();
	}


	public Boolean sendSMSWithContent(String appType, String tel, String content) throws Exception{
		if(StringUtils.isBlank(tel))
			throw new Exception("tel 不能为空!");

		boolean isSend = sendSms(tel,content);
		if(isSend)
			logger.info("sms send success. appType:["+appType+"],tel:["+StringUtils.left(tel, 3)+"******"+StringUtils.right(tel, 4)+"],code["+content+"]");
		else
			logger.info("sms send failed. appType:["+appType+"],tel:["+StringUtils.left(tel, 3)+"******"+StringUtils.right(tel, 4)+"],code["+content+"]");
		return  isSend;
	}






	@Override
	public void destroy() throws Exception {
		service.shutdown();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Runnable runnable = new Runnable() {
			public void run() {
				Enumeration<String> em = lockIp.keys();
				while (em.hasMoreElements()) {
					String ip = em.nextElement();
					AtomicInteger count = lockIp.get(ip);
					if (count.get() > 100)
						count.set(1);
				}
			}
		};

		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);// 小时
		// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
		service.scheduleAtFixedRate(runnable, 24 - hour, 24, TimeUnit.HOURS);

	}



	 public    Boolean sendSms (String tel,String content){
		 try {
			 StringBuilder postData = new StringBuilder();
			 postData.append("sname=").append("888").append("&spwd=").append("123456abc").append("&scorpid=")
					 .append("&sprdid=").append("1012888").append("&sdst=").append(tel).append("&smsg=")
					 .append(java.net.URLEncoder.encode(content));
			 //发送POST请求
			 URL url = new URL(postUrl);
			 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			 conn.setRequestMethod("POST");
			 conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			 conn.setRequestProperty("Connection", "Keep-Alive");
			 conn.setUseCaches(false);
			 conn.setDoOutput(true);

			 conn.setRequestProperty("Content-Length", "" + postData.length());
			 OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			 out.write(postData.toString());
			 out.flush();
			 out.close();

			 //获取响应状态
			 if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				 System.out.println("connect failed!");
				 return false;
			 }
			 //获取响应内容体
			 String line;
			 StringBuffer result = new StringBuffer();
			 BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			 while ((line = in.readLine()) != null) {
				 result.append( line + "\n");
			 }
			 in.close();
			 logger.info("-------"+result);

			 return true;
		 } catch (IOException e) {
			 logger.error("短信异常"+e.getMessage());
			 return false;
		 }
	 }
	 }

