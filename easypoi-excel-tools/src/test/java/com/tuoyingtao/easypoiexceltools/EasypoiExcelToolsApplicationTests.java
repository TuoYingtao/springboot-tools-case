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
        ArrayList<MemberEntity> entities = new ArrayList<>();
        List<MemberEntity> memberEntities = LocalJsonUtil.readLargerJsonData();
        List<MemberEntity> memberEntities1 = LocalJsonUtil.readLargerJsonData();
        entities.addAll(memberEntities);
        entities.addAll(memberEntities1);
        System.out.println(entities);
//        System.out.println(memberLargerData);
//        String path = ResourceUtils.getURL("classpath:json").getPath();
//        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("json/members1.json");
//        ClassPathResource resource = new ClassPathResource("json");
//        String resourcePath = resource.getPath() + "//members2.json";
//        String path1 = resource.getFile().getPath() + "\\members3.json";
    }

}
