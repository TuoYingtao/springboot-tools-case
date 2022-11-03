package com.tuoyingtao.easypoiexceltools.entity;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;

import java.io.Serializable;

/**
 * EasyPoi 导入Excel验证实体类
 * @author tuoyingtao
 * @create 2022-11-01 14:51
 */
public class ExcelVerifyEntity implements IExcelDataModel, IExcelModel, Serializable {
    private static final long serialVersionUID = 2L;

    /** 行号 */
    private Integer rowNum;

    /** 错误消息 */
    private String errorMsg;

    @Override
    public Integer getRowNum() {
        return this.rowNum;
    }

    @Override
    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
