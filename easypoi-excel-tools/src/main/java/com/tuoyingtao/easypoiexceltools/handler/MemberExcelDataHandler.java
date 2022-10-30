package com.tuoyingtao.easypoiexceltools.handler;

import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;
import cn.hutool.core.util.StrUtil;
import com.tuoyingtao.easypoiexceltools.entity.Member;

/**
 * 自定义字段处理器
 * @author TuoYingtao
 * @create 2022-10-30 20:41
 */
public class MemberExcelDataHandler extends ExcelDataHandlerDefaultImpl<Member> {

    @Override
    public Object exportHandler(Member obj, String name, Object value) {
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

    @Override
    public Object importHandler(Member obj, String name, Object value) {
        return super.importHandler(obj, name, value);
    }
}
