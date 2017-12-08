package com.shiyi.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GenerateOidUtil {
	
	private static final ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<Integer>();
	private static final CountDownLatch latch = new CountDownLatch(1);
	/**
	 * 每毫秒生成订单号数量最大值，约定取整百，整千。
	 */
	public static final int maxPerMSECSize = 10;

	private static void init() {
		for (int i = 0; i < maxPerMSECSize; i++) {
			queue.offer(i);
		}
		latch.countDown();
	}

	public static Integer poll() {
		try {
			if (latch.getCount() > 0) {
				init();
				latch.await(1, TimeUnit.SECONDS);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Integer i = queue.poll();
		queue.offer(i);
		return i;
	}

	public static String get() {
		long nowLong = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date()));
		String number = maxPerMSECSize + poll() + "";
		return nowLong + number.substring(1);
	}

	public static int createRandomCharData(int length) {
		StringBuilder sb = new StringBuilder();
		Random rand = new Random();// 随机用以下三个随机生成器
		Random randdata = new Random();
		int data = 0;
		for (int i = 0; i < length; i++) {
			int index = rand.nextInt(3);
			// 目的是随机选择生成数字，大小写字母
			switch (index) {
			case 0:
				data = randdata.nextInt(10);// 仅仅会生成0~9
				sb.append(data);
				break;
			case 1:
				data = randdata.nextInt(26) + 65;// 保证只会产生65~90之间的整数
				sb.append((char) data);
				break;
			case 2:
				data = randdata.nextInt(26) + 97;// 保证只会产生97~122之间的整数
				sb.append((char) data);
				break;
			}
		}
		String result = sb.toString();
		String yyyyMMddHHmmss = DateUtil.getSimpleDateFormat(
				DateUtil.DATE_FORMAT_2).format(new Date());
		StringBuffer buffer = new StringBuffer();
		buffer.append(yyyyMMddHHmmss);
		buffer.append(result);
		return Integer.getInteger(buffer.toString());
	}

}
