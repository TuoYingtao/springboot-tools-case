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
public class OrderEntity implements Serializable {
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
    private MemberEntity memberEntity;

    @ExcelCollection(name = "商品信息")
    private List<ProductEntity> productEntityList;

    public OrderEntity() {
    }

    public OrderEntity(Long id, String orderSn, Date createTime, String receiverAddress, MemberEntity memberEntity, List<ProductEntity> productEntityList) {
        this.id = id;
        this.orderSn = orderSn;
        this.createTime = createTime;
        this.receiverAddress = receiverAddress;
        this.memberEntity = memberEntity;
        this.productEntityList = productEntityList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("OrderEntity [");
        builder.append("    id=")
                .append(id);
        builder.append(",    orderSn=")
                .append(orderSn);
        builder.append(",    createTime=")
                .append(createTime);
        builder.append(",    receiverAddress=")
                .append(receiverAddress);
        builder.append(",    memberEntity=")
                .append(memberEntity);
        builder.append(",    productEntityList=")
                .append(productEntityList);
        builder.append(']');
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(orderSn, that.orderSn) && Objects.equals(createTime, that.createTime) && Objects.equals(receiverAddress, that.receiverAddress) && Objects.equals(memberEntity, that.memberEntity) && Objects.equals(productEntityList, that.productEntityList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderSn, createTime, receiverAddress, memberEntity, productEntityList);
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

    public MemberEntity getMemberEntity() {
        return memberEntity;
    }

    public void setMemberEntity(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
    }

    public List<ProductEntity> getProductEntityList() {
        return productEntityList;
    }

    public void setProductEntityList(List<ProductEntity> productEntityList) {
        this.productEntityList = productEntityList;
    }
}
