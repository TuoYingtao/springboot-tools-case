package com.tuoyingtao.easypoiexceltools;

import com.tuoyingtao.easypoiexceltools.entity.MemberEntity;
import com.tuoyingtao.easypoiexceltools.entity.OrderEntity;
import com.tuoyingtao.easypoiexceltools.util.DataJsonUtil;
import com.tuoyingtao.easypoiexceltools.util.LocalJsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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

}
