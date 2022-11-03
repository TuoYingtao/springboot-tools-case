package com.tuoyingtao.easypoiexceltools.util;

import com.tuoyingtao.easypoiexceltools.entity.MemberEntity;
import com.tuoyingtao.easypoiexceltools.entity.OrderEntity;
import com.tuoyingtao.easypoiexceltools.entity.ProductEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 假数据处理
 * @author TuoYingtao
 * @create 2022-10-30 12:04
 */
public class DataJsonUtil {

    private static List<OrderEntity> orderEntityList;
    private static List<MemberEntity> memberEntityList;
    private static List<ProductEntity> productEntityList;

    static  {
        orderEntityList = LocalJsonUtil.getListFromJson("json/orders.json", OrderEntity.class);
        memberEntityList = LocalJsonUtil.getListFromJson("json/members.json", MemberEntity.class);
        productEntityList = LocalJsonUtil.getListFromJson("json/products.json", ProductEntity.class);
    }

    /**
     * 会员列表
     * @return
     */
    public static List<MemberEntity> getMemberList() {
        return memberEntityList;
    }

    /**
     * 商品列表
     * @return
     */
    public static List<ProductEntity> getProductList() {
        return productEntityList;
    }

    /**
     * 生产订单列表
     * @return
     */
    public static List<OrderEntity> getOrderList() {
        return orderEntityList.stream().map(order -> {
            if (productEntityList.size() != 0) {
                order.setProductEntityList(productEntityList);
            }
            if (memberEntityList.size() != 0) {
                order.setMemberEntity(memberEntityList.get((int) Math.round(Math.random() * (memberEntityList.size() - 1))));
            }
            return order;
        }).collect(Collectors.toList());
    }
}
