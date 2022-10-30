package com.tuoyingtao.easypoiexceltools.entity;


import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 订单
 * @author tuoyingtao
 * @create 2022-10-28 11:11
 */
@ExcelTarget("order")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    @Excel(name = "订单ID", width = 10, type = 10)
    private Long id;
    @Excel(name = "订单号", width = 20, needMerge = true)
    private String orderSn;
    @Excel(name = "创建时间", width = 20, needMerge = true, format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @Excel(name = "收货地址", width = 20, needMerge = true)
    private String receiverAddress;
    @ExcelEntity(name = "会员信息", show = true)
    private Member member;
    @ExcelCollection(name = "商品信息")
    private List<Product> productList;

    public Order() {
    }

    public Order(Long id, String orderSn, Date createTime, String receiverAddress, Member member, List<Product> productList) {
        this.id = id;
        this.orderSn = orderSn;
        this.createTime = createTime;
        this.receiverAddress = receiverAddress;
        this.member = member;
        this.productList = productList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(orderSn, order.orderSn) && Objects.equals(createTime, order.createTime) && Objects.equals(receiverAddress, order.receiverAddress) && Objects.equals(member, order.member) && Objects.equals(productList, order.productList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderSn, createTime, receiverAddress, member, productList);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderSn='" + orderSn + '\'' +
                ", createTime=" + createTime +
                ", receiverAddress='" + receiverAddress + '\'' +
                ", member=" + member +
                ", productList=" + productList +
                '}';
    }
}
