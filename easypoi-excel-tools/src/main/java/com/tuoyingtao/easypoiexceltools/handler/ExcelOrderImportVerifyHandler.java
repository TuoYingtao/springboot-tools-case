package com.tuoyingtao.easypoiexceltools.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.afterturn.easypoi.util.PoiValidationUtil;
import com.tuoyingtao.easypoiexceltools.entity.OrderEntity;
import com.tuoyingtao.easypoiexceltools.group.ExcelVerify;
import com.tuoyingtao.easypoiexceltools.util.DataJsonUtil;
import com.tuoyingtao.easypoiexceltools.vo.OrderVo;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Excel 导入去重验证处理器
 * @author tuoyingtao
 * @create 2022-11-01 18:05
 */
@Component
public class ExcelOrderImportVerifyHandler implements IExcelVerifyHandler<OrderVo> {
    private final ThreadLocal<Map<Integer, String>> threadLocal = new ThreadLocal<>();

    private final StringBuilder stringBuilder = new StringBuilder();

    @Override
    public ExcelVerifyHandlerResult verifyHandler(OrderVo orderVo) {
        StringJoiner stringJoiner = new StringJoiner(",");
        /** 导入数据查重 */
        Map<Integer, String> threadLocalVal = threadLocal.get();
        if (threadLocalVal == null) {
            threadLocalVal = new HashMap<>();
        }
        // 验证数据是否重复
        boolean isSameItem = threadLocalVal.entrySet().stream().anyMatch(e -> {
            if (e.getValue().equals(orderVo.getOrderSn())) {
                stringBuilder.append("【").append(orderVo.getRowNum() + 1).append("】")
                        .append("数据与第").append(e.getKey()).append("行重复");
                // 添加错误信息
                stringJoiner.add(stringBuilder.substring(0, stringBuilder.length()));
                // 清空StringBuilder
                stringBuilder.delete(0, stringBuilder.length());
                return true;
            }
            return false;
        });
        // 添加当前行的数据到ThreadLocal中
        threadLocalVal.put(orderVo.getRowNum() + 1, orderVo.getOrderSn());
        threadLocal.set(threadLocalVal);
        // 导入的数据中有重复的
        if (isSameItem) {
            // Excel导入处理返回结果
            return new ExcelVerifyHandlerResult(false, stringJoiner.toString());
        }

        /** 验证数据库数据是否重复 */
        Boolean verification = sameVerification(orderVo.getOrderSn());
        if (verification) {
            stringJoiner.add("数据与数据库数据重复");
        }
        /**
         * 因为是嵌套集合IExcelVerifyHandler处理器只被调用一次，
         * 所以在处理器每一次执行时，通过PoiValidationUtil进行校验Collection中的对象
         */
        // 由于是嵌套对象它是没有行号的，需要我们手动给他设置一个与父对象相同的行号
        orderVo.getMemberVo().setRowNum(orderVo.getRowNum());
        nestCollectionVerify(stringJoiner, orderVo.getMemberVo());
        orderVo.getProductVoList().forEach(productVo -> nestCollectionVerify(stringJoiner, productVo));
        // 根据错误信息对Excel导入的数据结果集做处理
        if (stringJoiner.length() != 0) {
            // Excel导入处理返回结果
            return new ExcelVerifyHandlerResult(false, stringJoiner.toString());
        }
        // Excel导入处理返回结果
        return new ExcelVerifyHandlerResult(true);
    }

    private Boolean sameVerification(String orderSn) {
        List<OrderEntity> orderList = DataJsonUtil.getOrderList();
        return orderList.stream().anyMatch(e -> e.getOrderSn().equals(orderSn));
    }

    /**
     * PoiValidationUtil 进行校验 Collection 中的对象
     */
    private void nestCollectionVerify(StringJoiner stringJoiner, Object obj) {
        // 调用JSR 303 Validation 对有@Excel所标识的数据进行验证
        String validationMsg = PoiValidationUtil.validation(obj, new Class[]{ ExcelVerify.class });
        if (StringUtils.isNotEmpty(validationMsg)) {
            try {
                // 获取当前类的父类
                Class<?> superclass = obj.getClass().getSuperclass();
                // 获取父类属性
                Field rowNum = superclass.getDeclaredField("rowNum");
                Field errorMsg = superclass.getDeclaredField("errorMsg");
                // 设置属性允许访问
                rowNum.setAccessible(true);
                errorMsg.setAccessible(true);
                // 设置错误信息
                errorMsg.set(obj, validationMsg);
                // 获取错误行号
                Object roNumValue = rowNum.get(obj);
                if (roNumValue != null) {
                    stringBuilder.append("【").append(((Integer) roNumValue + 1)).append("】");
                }
                stringBuilder.append(validationMsg);
                // 添加错误信息
                stringJoiner.add(stringBuilder.substring(0, stringBuilder.length()));
                // 清空StringBuilder
                stringBuilder.delete(0, stringBuilder.length());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
