package com.tuoyingtao.easypoiexceltools.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.tuoyingtao.easypoiexceltools.entity.MemberEntity;
import com.tuoyingtao.easypoiexceltools.util.DataJsonUtil;
import com.tuoyingtao.easypoiexceltools.vo.MemberVo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringJoiner;

/**
 * Excel 导入去重验证处理器
 * @author tuoyingtao
 * @create 2022-11-01 15:07
 */
@Component
public class ExcelMemberImportVerifyHandler implements IExcelVerifyHandler<MemberVo> {

    @Override
    public ExcelVerifyHandlerResult verifyHandler(MemberVo memberVo) {
        StringJoiner stringJoiner = new StringJoiner(",");
        // 更具姓名和手机号判断数据库中是否已经存在
        String username = memberVo.getUsername();
        String phone = memberVo.getPhone();
        Boolean verification = sameVerification(username, phone);
        if (verification) {
            stringJoiner.add("数据与数据库数据重复");
        }
        if (stringJoiner.length() != 0) {
            // Excel导入处理返回结果
            return new ExcelVerifyHandlerResult(false, stringJoiner.toString());
        }
        // Excel导入处理返回结果
        return new ExcelVerifyHandlerResult(true);
    }

    private Boolean sameVerification(String username, String phone) {
        List<MemberEntity> memberList = DataJsonUtil.getMemberList();
        // 姓名与手机号相等个数不等于0则为重复
        return memberList.stream().anyMatch(e -> e.getUsername().equals(username) && e.getPhone().equals(phone));
    }
}
