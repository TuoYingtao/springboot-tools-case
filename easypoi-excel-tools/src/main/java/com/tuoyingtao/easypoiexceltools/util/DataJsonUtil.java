package com.tuoyingtao.easypoiexceltools.util;

import com.tuoyingtao.easypoiexceltools.entity.Member;
import com.tuoyingtao.easypoiexceltools.entity.Order;
import com.tuoyingtao.easypoiexceltools.entity.Product;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 假数据处理
 * @author TuoYingtao
 * @create 2022-10-30 12:04
 */
public class DataJsonUtil {

    private static List<Order> orderList;
    private static List<Member> memberList;
    private static List<Product> productList;

    static  {
        orderList = LocalJsonUtil.getListFromJson("json/orders.json", Order.class);
        memberList = LocalJsonUtil.getListFromJson("json/members.json", Member.class);
        productList = LocalJsonUtil.getListFromJson("json/products.json", Product.class);
    }

    /**
     * 会员列表
     * @return
     */
    public static List<Member> getMemberList() {
        return memberList;
    }

    /**
     * 商品列表
     * @return
     */
    public static List<Product> getProductList() {
        return productList;
    }

    /**
     * 生产订单列表
     * @return
     */
    public static List<Order> getOrderList() {
        return orderList.stream().map(order -> {
            if (productList.size() != 0) {
                order.setProductList(productList);
            }
            if (memberList.size() != 0) {
                order.setMember(memberList.get((int) Math.round(Math.random() * (memberList.size() - 1))));
            }
            return order;
        }).collect(Collectors.toList());
    }
}
