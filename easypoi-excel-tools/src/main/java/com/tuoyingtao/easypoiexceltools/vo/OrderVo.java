package com.tuoyingtao.easypoiexceltools.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import com.tuoyingtao.easypoiexceltools.entity.ExcelVerifyEntity;
import com.tuoyingtao.easypoiexceltools.group.ExcelVerify;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author tuoyingtao
 * @create 2022-11-01 17:54
 */
public class OrderVo extends ExcelVerifyEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Excel(name = "订单ID", width = 10, type = 10)
    private Long id;

    @Excel(name = "订单号", width = 20, needMerge = true)
    @NotEmpty(message = "订单号不能为空", groups = {ExcelVerify.class})
    private String orderSn;

    @Excel(name = "创建时间", width = 20, needMerge = true, format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Excel(name = "收货地址", width = 20, needMerge = true)
    @NotEmpty(message = "收货地址不能为空", groups = {ExcelVerify.class})
    private String receiverAddress;

    @ExcelEntity(name = "会员信息", show = true)
    private MemberVo memberVo;

    @ExcelCollection(name = "商品信息")
    private List<ProductVo> productVoList;

    public OrderVo() {
    }

    public OrderVo(Long id, String orderSn, Date createTime, String receiverAddress, MemberVo memberVo, List<ProductVo> productVoList) {
        this.id = id;
        this.orderSn = orderSn;
        this.createTime = createTime;
        this.receiverAddress = receiverAddress;
        this.memberVo = memberVo;
        this.productVoList = productVoList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("OrderVo [");
        builder.append("    id=")
                .append(id);
        builder.append(",    orderSn=")
                .append(orderSn);
        builder.append(",    createTime=")
                .append(createTime);
        builder.append(",    receiverAddress=")
                .append(receiverAddress);
        builder.append(",    memberVo=")
                .append(memberVo);
        builder.append(",    productVoList=")
                .append(productVoList);
        builder.append(']');
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderVo orderVo = (OrderVo) o;
        return Objects.equals(id, orderVo.id) && Objects.equals(orderSn, orderVo.orderSn) && Objects.equals(createTime, orderVo.createTime) && Objects.equals(receiverAddress, orderVo.receiverAddress) && Objects.equals(memberVo, orderVo.memberVo) && Objects.equals(productVoList, orderVo.productVoList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderSn, createTime, receiverAddress, memberVo, productVoList);
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

    public MemberVo getMemberVo() {
        return memberVo;
    }

    public void setMemberVo(MemberVo memberVo) {
        this.memberVo = memberVo;
    }

    public List<ProductVo> getProductVoList() {
        return productVoList;
    }

    public void setProductVoList(List<ProductVo> productVoList) {
        this.productVoList = productVoList;
    }
}
