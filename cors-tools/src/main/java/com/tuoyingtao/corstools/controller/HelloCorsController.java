package com.tuoyingtao.corstools.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 方法四：局部跨域
 * @author tuoyingtao
 * @create 2022-10-13 18:26
 */
@RestController
@RequestMapping("/cors/")
//@CrossOrigin(origins = "*")
public class HelloCorsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloCorsController.class);

    /**
     * 配置类跨域测试API
     * @return
     */
    @RequestMapping(value = "configCors", method = RequestMethod.GET)
    public String hello(HttpServletRequest request) {
        String token = request.getHeader("token");
        LOGGER.info("token:{}", token);
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                LOGGER.info("cookie.name:{},cookie.value:{}", cookie.getName(), cookie.getValue());
            }
        }
        return "Hello cors";
    }

    /**
     * 注解跨域
     * @return
     */
    @CrossOrigin(value = "http://192.168.50.117:8080") //指定具体ip允许跨域
    @RequestMapping(value = "annotationCors", method = RequestMethod.GET)
    public String annotationCors() {
        return "Hello annotationCors";
    }

    /**
     * 手动设置响应头跨域
     * @param response
     * @return
     */
    @RequestMapping(value = "responseCors", method = RequestMethod.GET)
    public String responseCors(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin","http://192.168.50.117:8080");
        response.addHeader("Access-Control-Allow-Credentials","true");
        response.addHeader("Access-Control-Allow-Methods","GET");
        response.addHeader("Access-Control-Max-Age","3600");
        response.addHeader("Access-Control-Allow-Headers","Content-Type, Origin, X-Requested-With, Accept");
        return "Hello responseCors";
    }
}
