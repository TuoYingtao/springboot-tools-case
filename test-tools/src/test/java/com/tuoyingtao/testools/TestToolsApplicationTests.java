package com.tuoyingtao.testools;

import cn.hutool.core.comparator.VersionComparator;
import cn.hutool.http.useragent.UserAgentUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestToolsApplicationTests {

    String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36 NetType/WIFI MicroMessenger/7.0.20.1781(0x6700143B) WindowsWechat(0x63090621) XWEB/8379 Flue";

    @Test
    void contextLoads() {
        System.out.println(SpringBootVersion.getVersion());
        System.out.println(VersionComparator.INSTANCE.compare("2.7", "2.6"));

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
