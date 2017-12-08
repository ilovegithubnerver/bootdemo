package com.shiyi;

import com.shiyi.controller.bean.OrderBean;
import com.shiyi.service.AuthService;
import com.shiyi.util.DownLoadUtil;
import com.shiyi.util.GenerateOidUtil;
import com.shiyi.util.MD5;
import com.shiyi.util.PageData;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthApplicationTests {

	//@Autowired
	//private AuthService authService;

	@Autowired
	private WebApplicationContext context;
	private MockMvc mvc;


	@Test
	public void contextLoads() {


		OrderBean orderBean=new OrderBean();
		orderBean.setId_card_no("456");
		orderBean.setPic_data("123");
		orderBean.setOrder_id(GenerateOidUtil.get());
		//int rs=authService.saveOrder(orderBean);
	}



	@Before
	public void setUp() throws Exception {
		//       mvc = MockMvcBuilders.standaloneSetup(new TestController()).build();
		mvc = MockMvcBuilders.webAppContextSetup(context).build();//建议使用这种
	}
	@Test
	public void test1() throws Exception {

		String base64= DownLoadUtil.downloadPictureBase64("http://woshua.oss-cn-shanghai.aliyuncs.com/app/android/idcard/20171208/91b570cc-8f1e-46f0-a19a-adee0c1c589f.png");

		TreeMap treeMap=new TreeMap<String,String>();
		treeMap.put("id_card_no","370406199408220029");
		treeMap.put("pic_data",base64);
		treeMap.put("channel_code","1024");

		StringBuilder signStr=new StringBuilder("");
		Set set= treeMap.entrySet();
		Iterator iterator=set.iterator();
		for(;iterator.hasNext();){
			Map.Entry entry= (Map.Entry) iterator.next();
			signStr.append(entry.getKey()).append("=").append(entry.getValue()).append("&");

		}
		signStr.append("key=test");
		System.out.println("signStr-----"+signStr);
		mvc.perform(MockMvcRequestBuilders.get("/cusPicVerify")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.param("id_card_no", "370406199408220029").param("pic_data", base64).param("sign", MD5.md5(signStr.toString()))
				.param("channel_code","1024")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("\"result\":\"00\"")));

	}

}
