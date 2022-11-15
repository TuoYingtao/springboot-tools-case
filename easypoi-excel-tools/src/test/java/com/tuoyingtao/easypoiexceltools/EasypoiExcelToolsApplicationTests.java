package com.tuoyingtao.easypoiexceltools;

import com.tuoyingtao.easypoiexceltools.entity.MemberEntity;
import com.tuoyingtao.easypoiexceltools.entity.OrderEntity;
import com.tuoyingtao.easypoiexceltools.util.DataJsonUtil;
import com.tuoyingtao.easypoiexceltools.util.LocalJsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
class EasypoiExcelToolsApplicationTests {

    @Test
    void contextLoads() {
        List<MemberEntity> memberEntityList = LocalJsonUtil.getListFromJson("json/members.json", MemberEntity.class);
        System.out.println(memberEntityList);
    }

    @Test
    void getOrderList() {
        List<OrderEntity> orderEntityList = DataJsonUtil.getOrderList();
        System.out.println(orderEntityList);
    }

    @Test
    void createLargerData() throws IOException {
        try {
            long startTime = System.currentTimeMillis();
            ArrayList<MemberEntity> entities = new ArrayList<>();
            Boolean flag = true;
            Integer i = 0;
            while (flag) {
                List<MemberEntity> memberEntities = LocalJsonUtil.readLargerJsonData();
                if (memberEntities != null) {
                    entities.addAll(memberEntities);
                } else {
                    flag = false;
                }
            }
            System.out.println("总耗时：" + (System.currentTimeMillis() - startTime));
            System.out.println("执行完成");
        } finally {
            ThreadLocal<Map<String, Integer>> threadLocal = LocalJsonUtil.threadLocal;
            if (threadLocal != null) {
                threadLocal.remove();
            }
            System.out.println("清空");
        }
//        System.out.println(memberLargerData);
//        String path = ResourceUtils.getURL("classpath:json").getPath();
//        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("json/members1.json");
//        ClassPathResource resource = new ClassPathResource("json");
//        String resourcePath = resource.getPath() + "//members2.json";
//        String path1 = resource.getFile().getPath() + "\\members3.json";
    }

}
