package com.tuoyingtao.easypoiexceltools.handler;

import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;
import cn.hutool.core.util.StrUtil;
import com.tuoyingtao.easypoiexceltools.entity.MemberEntity;
import org.springframework.stereotype.Component;

/**
 * 导出自定义字段处理器
 * @author TuoYingtao
 * @create 2022-10-30 20:41
 */
@Component
public class MemberExcelDataHandler extends ExcelDataHandlerDefaultImpl<MemberEntity> {

    /**
     * 导出处理方法
     * @param obj 实体类
     * @param name 字段转译名称
     * @param value 当前值
     * @return
     */
    @Override
    public Object exportHandler(MemberEntity obj, String name, Object value) {
        if ("昵称".equals(name)) {
            String emptyValue = "暂未设置";
            if (value == null) {
                return super.exportHandler(obj, name, emptyValue);
            }
            if (value instanceof String && StrUtil.isBlank((String) value)) {
                return super.exportHandler(obj, name, emptyValue);
            }
        }
        return super.exportHandler(obj, name, value);
    }

    /**
     * 导入处理方法
     * @param obj 实体类
     * @param name 字段转译名称
     * @param value 当前值
     * @return
     */
    @Override
    public Object importHandler(MemberEntity obj, String name, Object value) {
        return super.importHandler(obj, name, value);
    }
}
