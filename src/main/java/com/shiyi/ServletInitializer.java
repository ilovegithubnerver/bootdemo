package com.shiyi;

/**
 * @Description:
 * @author: chenjiangpeng
 * @Param:
 * @Return:
 * @Date: 2017/9/29
 */

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;


public class ServletInitializer extends SpringBootServletInitializer {

    /**
     * 修改启动类，继承 SpringBootServletInitializer 并重写 configure 方法
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AuthApplication.class);
    }

}