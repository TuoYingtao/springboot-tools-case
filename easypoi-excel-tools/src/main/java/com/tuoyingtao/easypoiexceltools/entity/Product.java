package com.tuoyingtao.easypoiexceltools.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

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
    @Excel(name = "价格", width = 10, type = 10)
    private BigDecimal price;
    @Excel(name = "购买数量", width = 10, suffix = "件")
    private Integer count;

    public Product() {
    }

    public Product(Long id, String productSn, String name, String subTitle, String brandName, BigDecimal price, Integer count) {
        this.id = id;
        this.productSn = productSn;
        this.name = name;
        this.subTitle = subTitle;
        this.brandName = brandName;
        this.price = price;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductSn() {
        return productSn;
    }

    public void setProductSn(String productSn) {
        this.productSn = productSn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(productSn, product.productSn) && Objects.equals(name, product.name) && Objects.equals(subTitle, product.subTitle) && Objects.equals(brandName, product.brandName) && Objects.equals(price, product.price) && Objects.equals(count, product.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productSn, name, subTitle, brandName, price, count);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productSn='" + productSn + '\'' +
                ", name='" + name + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", brandName='" + brandName + '\'' +
                ", price=" + price +
                ", count=" + count +
                '}';
    }
}
