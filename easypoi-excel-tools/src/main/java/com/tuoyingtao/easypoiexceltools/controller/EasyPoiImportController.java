package com.tuoyingtao.easypoiexceltools.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.tuoyingtao.easypoiexceltools.common.api.R;
import com.tuoyingtao.easypoiexceltools.entity.Member;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author TuoYingtao
 * @create 2022-10-30 10:47
 */
@Controller
@Api(tags = "EasyPoiImportController", description = "导入Excel列表")
@RequestMapping("/easyPoiImport/")
public class EasyPoiImportController {

    @ApiOperation("Excel导入到会员列表")
    @RequestMapping(value = "importMember", method = RequestMethod.POST)
    @ResponseBody
    public R importEasyPoi(@RequestPart("file") MultipartFile file) {
        String[] fileNames = file.getOriginalFilename().split("\\.");
        String postfix = fileNames[fileNames.length - 1].toUpperCase();
        if (!postfix.matches("XLS|XLSX")){
            return R.error("文件格式不正确");
        }
        ImportParams importParams = new ImportParams();
        importParams.setTitleRows(1);
        importParams.setHeadRows(1);
        try {
            List<Member> members = ExcelImportUtil.importExcel(file.getInputStream(), Member.class, importParams);
            return R.ok().put("data",members);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导入失败");
        }
    }
}
