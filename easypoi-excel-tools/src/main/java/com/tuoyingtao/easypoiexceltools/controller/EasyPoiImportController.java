package com.tuoyingtao.easypoiexceltools.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.tuoyingtao.easypoiexceltools.common.api.R;
import com.tuoyingtao.easypoiexceltools.entity.MemberEntity;
import com.tuoyingtao.easypoiexceltools.entity.OrderEntity;
import com.tuoyingtao.easypoiexceltools.group.ExcelVerify;
import com.tuoyingtao.easypoiexceltools.handler.ExcelMemberImportVerifyHandler;
import com.tuoyingtao.easypoiexceltools.handler.ExcelOrderImportVerifyHandler;
import com.tuoyingtao.easypoiexceltools.vo.MemberVo;
import com.tuoyingtao.easypoiexceltools.vo.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author TuoYingtao
 * @create 2022-10-30 10:47
 */
@Controller
@Api(tags = "EasyPoiImportController", description = "导入Excel列表")
@RequestMapping("/easyPoiImport/")
public class EasyPoiImportController {

    @Resource
    ExcelMemberImportVerifyHandler excelMemberImportVerifyHandler;

    @Resource
    ExcelOrderImportVerifyHandler excelOrderImportVerifyHandler;

    @ApiOperation("【会员列表】Excel文件导入（非验证）")
    @RequestMapping(value = "importMember", method = RequestMethod.POST)
    @ResponseBody
    public R importEasyPoi(@RequestPart("file") MultipartFile file) {
        String[] fileNames = file.getOriginalFilename().split("\\.");
        String postfix = fileNames[fileNames.length - 1].toUpperCase();
        if (!postfix.matches("XLS|XLSX")){
            return R.error("文件格式不正确");
        }
        ImportParams importParams = new ImportParams();
        // 设置标题列占几行 默认0
        importParams.setTitleRows(1);
        // 设置字段名称占几行 即header
        importParams.setHeadRows(1);
        try {
            List<MemberEntity> memberEntities = ExcelImportUtil.importExcel(file.getInputStream(), MemberEntity.class, importParams);
            return R.ok().put("data", memberEntities);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导入失败");
        }
    }

    @ApiOperation("【订单列表】Excel文件导入嵌套集合（非验证）")
    @RequestMapping(value = "importOrder", method = RequestMethod.POST)
    @ResponseBody
    public R importOrderEasyPoi(@RequestPart("file") MultipartFile file) {
        String[] fileNames = file.getOriginalFilename().split("\\.");
        String postfix = fileNames[fileNames.length - 1].toUpperCase();
        if (!postfix.matches("XLS|XLSX")){
            return R.error("文件格式不正确");
        }
        ImportParams importParams = new ImportParams();
        // 设置标题列占几行 默认0
        importParams.setTitleRows(1);
        // 设置字段名称占几行 即header
        importParams.setHeadRows(2);
        try {
            List<OrderEntity> members = ExcelImportUtil.importExcel(file.getInputStream(), OrderEntity.class, importParams);
            return R.ok().put("data",members);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导入失败");
        }
    }



    @ApiOperation(value = "【未知】Excel文件导入为Map数据")
    @RequestMapping(value = "importMap", method = RequestMethod.POST)
    @ResponseBody
    public R importMapEasyPoi(@RequestPart("file") MultipartFile file) {
        String[] fileNames = file.getOriginalFilename().split("\\.");
        String postfix = fileNames[fileNames.length - 1].toUpperCase();
        if (!postfix.matches("XLS|XLSX")){
            return R.error("文件格式不正确");
        }
        try {
            ImportParams params = new ImportParams();
            // 设置标题列占几行 默认0
            params.setTitleRows(1);
            // 设置字段名称占几行 即header
            params.setHeadRows(2);
            List<Map<String, Object>> list = ExcelImportUtil.importExcel(file.getInputStream(), Map.class, params);
            return R.ok().put("data", list);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导入失败");
        }
    }

    @ApiOperation(value = "【会员列表】Excel文件导入（验证）")
    @RequestMapping(value = "importMemberValidator", method = RequestMethod.POST)
    @ResponseBody
    public R importMemberValidatorEasyPoi(@RequestPart("file") MultipartFile file) {
        String[] fileNames = file.getOriginalFilename().split("\\.");
        String postfix = fileNames[fileNames.length - 1].toUpperCase();
        if (!postfix.matches("XLS|XLSX")){
            return R.error("文件格式不正确");
        }
        try {
            ImportParams params = new ImportParams();
            // 设置标题列占几行 默认0
            params.setTitleRows(1);
            // 设置字段名称占几行 即header
            params.setHeadRows(1);
            // 开启Excel导入验证
            params.setNeedVerify(true);
            params.setVerifyGroup(new Class[]{ ExcelVerify.class });
            // 设置Excel导入验证器
            params.setVerifyHandler(excelMemberImportVerifyHandler);
            // importExcelMore方法返回带有校验结果
            ExcelImportResult<MemberVo> result = ExcelImportUtil.importExcelMore(file.getInputStream(), MemberVo.class, params);

            // 判断是否校验成功
            if (!result.isVerifyFail()) {
                /** ReflectionToStringBuilder */
                for (int i = 0; i < result.getList().size(); i++) {
                    System.out.println(ReflectionToStringBuilder.toString(result.getList().get(i)));
                }
                // 通过的结果集
                return R.ok().put("data", result.getList());
            }
            // 校验失败 失败的结果集
            List<MemberVo> failList = result.getFailList();
            List<String> failMessageList = new ArrayList<>();
            for (MemberVo memberVo : failList) {
                // 设置错误内容在第几行
                int line = memberVo.getRowNum() + 1;
                // 设置错误信息
                String errMessage = "第" + line + "行的错误是：" + memberVo.getErrorMsg();
                failMessageList.add(errMessage);
            }
            return R.error(20000, "导入失败").put("errorData", failMessageList);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导入失败");
        }
    }

    @ApiOperation(value = "【订单列表】Excel文件导入嵌套集合（验证）")
    @RequestMapping(value = "importOrderValidator", method = RequestMethod.POST)
    @ResponseBody
    public R importOrderNestExcel(@RequestPart("file") MultipartFile file) {
        String[] fileNames = file.getOriginalFilename().split("\\.");
        String postfix = fileNames[fileNames.length - 1].toUpperCase();
        if (!postfix.matches("XLS|XLSX")){
            return R.error("文件格式不正确");
        }
        try {
            ImportParams params = new ImportParams();
            // 设置标题列占几行 默认0
            params.setTitleRows(1);
            // 设置字段名称占几行 即header
            params.setHeadRows(2);
            // 开启Excel导入验证
            params.setNeedVerify(true);
            params.setVerifyGroup(new Class[]{ ExcelVerify.class });
            // 设置Excel导入验证器
            params.setVerifyHandler(excelOrderImportVerifyHandler);
            // 导入文件分割设置为 false 解决：Cannot add merged region B6:B8 to sheet because it overlaps with an existing merged region (B4:B6).
            params.setVerifyFileSplit(false);
            // importExcelMore方法返回带有校验结果
            ExcelImportResult<OrderVo> result = ExcelImportUtil.importExcelMore(file.getInputStream(), OrderVo.class, params);
            // 判断是否校验成功
            if (!result.isVerifyFail()) {
                // 通过的结果集
                return R.ok().put("data", result.getList());
            }
            // 校验失败 失败的结果集
            List<OrderVo> failList = result.getFailList();
            List<String> failMessageList = new ArrayList<>();
            StringBuilder stringBuilder = new StringBuilder();
            for (OrderVo orderVo : failList) {
                // 设置错误内容在第几行
                int line = orderVo.getRowNum() + 1;
                // 设置错误信息
                stringBuilder.append("第").append(line).append("行的错误是：").append(orderVo.getErrorMsg());
                // 添加错误信息
                failMessageList.add(stringBuilder.substring(0, stringBuilder.length()));
                stringBuilder.delete(0, stringBuilder.length());
            }
            return R.error(20000, "导入失败").put("errorData", failMessageList);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导入失败");
        }
    }

}
