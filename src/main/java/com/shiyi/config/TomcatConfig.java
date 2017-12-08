package com.shiyi.config;



import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;

import java.nio.charset.Charset;


/**
 * @Description:
 * @author: chenjiangpeng
 * @Param:
 * @Return:
 * @Date: 2017/10/16
 */
public class TomcatConfig {

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.setUriEncoding(Charset.forName("utf-8"));
        return tomcat;
    }
}
