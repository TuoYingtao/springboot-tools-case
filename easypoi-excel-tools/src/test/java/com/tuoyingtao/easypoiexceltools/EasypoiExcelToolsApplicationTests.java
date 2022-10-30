package com.tuoyingtao.easypoiexceltools;

import com.tuoyingtao.easypoiexceltools.entity.Member;
import com.tuoyingtao.easypoiexceltools.entity.Order;
import com.tuoyingtao.easypoiexceltools.util.DataJsonUtil;
import com.tuoyingtao.easypoiexceltools.util.LocalJsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class EasypoiExcelToolsApplicationTests {

    @Test
    void contextLoads() {
        List<Member> memberList = LocalJsonUtil.getListFromJson("json/members.json", Member.class);
        System.out.println(memberList);
    }

    @Test
    void getOrderList() {
        List<Order> orderList = DataJsonUtil.getOrderList();
        System.out.println(orderList);
    }

}
