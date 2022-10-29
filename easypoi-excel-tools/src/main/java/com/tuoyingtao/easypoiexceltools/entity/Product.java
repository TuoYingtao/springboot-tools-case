package com.tuoyingtao.easypoiexceltools.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品
 *
 * @author tuoyingtao
 * @create 2022-10-28 11:13
 */
@ExcelTarget("product")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    @Excel(name = "商品ID", width = 10, type = 10)
    private Long id;
    @Excel(name = "商品编号", width = 20)
    private String productSn;
    @Excel(name = "商品名称", width = 20)
    private String name;
    @Excel(name = "商品副标题", width = 20)
    private String subTitle;
    @Excel(name = "品牌名称", width = 20)
    private String brandName;
    @Excel(name = "价格", width = 10)
    private BigDecimal price;
    @Excel(name = "购买数量", width = 10, suffix = "件")
    private Integer count;

}
