package com.tuoyingtao.testools;

import cn.hutool.http.useragent.UserAgentUtil;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestToolsApplicationTests {

    String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36 NetType/WIFI MicroMessenger/7.0.20.1781(0x6700143B) WindowsWechat(0x63090621) XWEB/8379 Flue";

    @Test
    void contextLoads() {
        UserAgent userAgent = UserAgent.parseUserAgentString(this.USER_AGENT);
        Browser browser = userAgent.getBrowser();
        System.out.println("浏览器对象" + browser);
        System.out.println("浏览器版本:"+userAgent.getBrowserVersion());
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();

        System.out.println("浏览器名:"+browser.getName());
        System.out.println("浏览器类型:"+browser.getBrowserType());
        System.out.println("浏览器家族:"+browser.getGroup());
        System.out.println("浏览器生产厂商:"+browser.getManufacturer());
        System.out.println("浏览器使用的渲染引擎:"+browser.getRenderingEngine());

        System.out.println("操作系统名:"+operatingSystem.getName());
        System.out.println("访问设备类型:"+operatingSystem.getDeviceType());
        System.out.println("操作系统家族:"+operatingSystem.getGroup());
        System.out.println("操作系统生产厂商:"+operatingSystem.getManufacturer());

        System.out.println("-----------------------------------");

        cn.hutool.http.useragent.UserAgent ua = UserAgentUtil.parse(USER_AGENT);
        System.out.println(ua.getBrowser().toString());
        System.out.println(ua.getVersion());
        System.out.println(ua.getEngine().toString());
        System.out.println(ua.getEngineVersion());
        System.out.println(ua.getOs().toString());
        System.out.println(ua.getPlatform().toString());
    }

}
