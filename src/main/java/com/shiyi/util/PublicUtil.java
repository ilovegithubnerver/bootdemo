package com.shiyi.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/** 
 * 说明：IP处理
 * 创建人：FH Q313596790
 * 修改时间：2014年9月20日
 * @version
 */
public class PublicUtil {
	
	public static void main(String[] args) {
		System.out.println("本机的ip=" + PublicUtil.getIp());
	}
	
	public static String getPorjectPath(){
		String nowpath = "";
		nowpath=System.getProperty("user.dir")+"/";
		
		return nowpath;
	}
	
	/**
	 * 获取本机访问地址
	 * @return
	 */
	public static String getIp(){
		String ip = "";
		try {
			InetAddress inet = InetAddress.getLocalHost();
			ip = inet.getHostAddress();
			//System.out.println("本机的ip=" + ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		return ip;
	}
	
	public static String sortMap(Map<String, String> m){
	 StringBuffer  sb=new StringBuffer("");
	 for (Map.Entry<String, String> o : m.entrySet()) 
     { 
		 sb.append("&").append(o.getKey()).append("=").append(m.entrySet());
      //rs o.getKey() + "=" + o.getValue()); 
     } 
	 return sb.toString();
	 
	}
	
	
	
	public static String sortTreeMap(Map<String, String> map){
		   String str="";
			Set<String> keySet = map.keySet();
		    Iterator<String> iter = keySet.iterator();
		    while (iter.hasNext()) {
		        String key = iter.next();
		        str+="&"+key + "=" + map.get(key);
		    }
	       return str;
	}
	
}