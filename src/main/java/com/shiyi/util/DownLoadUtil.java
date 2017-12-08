package com.shiyi.util;


import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;



/**
 * @Description:下载网络图片
 * @author: chenjiangpeng
 * @Param:
 * @Return:
 * @Date: 2017/7/21
 */
public class DownLoadUtil {

    /**
     * 传入要下载的图片的url列表，将url所对应的图片下载到本地
     *
     * @param
     */

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(DownLoadUtil.class);

    public static File downloadPicture(String urlString) throws Exception {
        URL url = null;
        byte[] data = null;
        url = new URL(urlString);
       // String savePath = "d:/upload/auth/";// TODO
        String path=ClassLoader.getSystemClassLoader().getResource("").getPath();
        path=path.substring(0,path.indexOf("/classes"));
        path+=File.separator+"upload"+File.separator;
        System.out.println(path);
        String fileName=UUIDUtil.get32UUID();
        String temp=fileName+urlString.substring(urlString.lastIndexOf("."),urlString.length());
        String fPath=path+temp;
        File file=new File(fPath);
        File fileParent = file.getParentFile();
        if(!fileParent.exists())
        {
            fileParent.mkdir();
        }
        file.createNewFile();
        DataInputStream dis = new DataInputStream(url.openStream());
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int length;
        //开始填充数据
        while((length = dis.read(buffer))>0){
            fos.write(buffer,0,length);
        }
        dis.close();
        fos.close();
        return  file;
    }

    public static void main(String[] args) {
        try {
            downloadPicture("http://woshua.oss-cn-shanghai.aliyuncs.com/app/android/idcard/20170913/7acb76ed-efdd-4592-b75c-b19c6fdca72f.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 传入要下载的图片的url列表，将url所对应的图片取出字节流转成Base64String
     *
     * @param
     */
    public static String downloadPictureBase64(String urlString) throws Exception {
        URL url = new URL(urlString);
        URLConnection conn = (URLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setReadTimeout(5000);
        conn.setConnectTimeout(5000);
       // conn.setRequestMethod("GET");
        conn.connect();
        logger.info("urlString"+urlString);
        InputStream input = conn.getInputStream();
        byte[] buffer=null;
        try{
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = input.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            buffer = swapStream.toByteArray();
        } finally{
            //关闭输入流等（略）
            input.close();
        }
        //File file=new File("D:/3.txt");
        //FileOutputStream fileOutputStream=new FileOutputStream(file);
        //fileOutputStream.write(Base64.encodeBase64String(buffer).getBytes());
        //fileOutputStream.flush();
        //fileOutputStream.close();
        return  Base64.encodeBase64String(buffer);
        // return ApacheCodecBase64Util.encodeByteToString(buffer);
    }


    public static boolean generateImage(String imgStr, String path) {
        if (imgStr == null) return false;
        try {
            // 解密
            byte[] b = ApacheCodecBase64Util.decodeStringToByte(imgStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
               if (b[i] < 0) {
                 b[i] += 256;
               }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
       }
    }




}
