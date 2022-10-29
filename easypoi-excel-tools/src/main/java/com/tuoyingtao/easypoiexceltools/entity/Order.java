package com.tuoyingtao.easypoiexceltools.entity;


import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    @Excel(name = "会员信息", width = 20)
    private Member member;
    @Excel(name = "商品信息", width = 20)
    private List<Product> productList;
}
