package com.tuoyingtao.easypoiexceltools.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.tuoyingtao.easypoiexceltools.entity.ExcelVerifyEntity;
import com.tuoyingtao.easypoiexceltools.group.ExcelVerify;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author tuoyingtao
 * @create 2022-11-01 17:55
 */
public class ProductVo extends ExcelVerifyEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Excel(name = "商品ID", width = 10, type = 10)
    private Long id;

    @Excel(name = "商品编号", width = 20)
    @NotEmpty(message = "商品编号不能为空", groups = {ExcelVerify.class})
    private String productSn;

    @Excel(name = "商品名称", width = 20)
    @NotEmpty(message = "商品名称不能为空", groups = {ExcelVerify.class})
    private String name;

    @Excel(name = "商品副标题", width = 20)
    @NotEmpty(message = "商品副标题不能为空", groups = {ExcelVerify.class})
    private String subTitle;

    @Excel(name = "品牌名称", width = 20)
    @NotEmpty(message = "品牌名称不能为空", groups = {ExcelVerify.class})
    private String brandName;

    @Excel(name = "价格", width = 10, type = 10)
    @NotEmpty(message = "价格不能为空", groups = {ExcelVerify.class})
    private String price;

    @Excel(name = "购买数量", width = 10, suffix = "件")
    @NotEmpty(message = "购买数量不能为空", groups = {ExcelVerify.class})
    private String count;

    public ProductVo() {
    }

    public ProductVo(Long id, String productSn, String name, String subTitle, String brandName, String price, String count) {
        this.id = id;
        this.productSn = productSn;
        this.name = name;
        this.subTitle = subTitle;
        this.brandName = brandName;
        this.price = price;
        this.count = count;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("ProductVo [");
        builder.append("    id=")
                .append(id);
        builder.append(",    productSn=")
                .append(productSn);
        builder.append(",    name=")
                .append(name);
        builder.append(",    subTitle=")
                .append(subTitle);
        builder.append(",    brandName=")
                .append(brandName);
        builder.append(",    price=")
                .append(price);
        builder.append(",    count=")
                .append(count);
        builder.append(']');
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductVo productVo = (ProductVo) o;
        return Objects.equals(id, productVo.id) && Objects.equals(productSn, productVo.productSn) && Objects.equals(name, productVo.name) && Objects.equals(subTitle, productVo.subTitle) && Objects.equals(brandName, productVo.brandName) && Objects.equals(price, productVo.price) && Objects.equals(count, productVo.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productSn, name, subTitle, brandName, price, count);
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
