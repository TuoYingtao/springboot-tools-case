package com.tuoyingtao.easypoiexceltools.entity;


import java.util.Date;
import java.util.List;

/**
 * 订单
 * @author tuoyingtao
 * @create 2022-10-28 11:11
 */
public class Order {
    private Long id;
    private String orderSn;
    private Date createTime;
    private String receiverAddress;
    private Member member;
    private List<Product> productList;
}
