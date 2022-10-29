package com.tuoyingtao.easypoiexceltools.controller;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.tuoyingtao.easypoiexceltools.entity.Member;
import com.tuoyingtao.easypoiexceltools.util.LocalJsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author tuoyingtao
 * @create 2022-10-28 18:04
 */
@Controller
@RequestMapping("/easyPoi/")
@Api(tags = "EasyPoiController")
public class EasyPoiController {

    @ApiOperation(value = "导出会员列表Excel")
    @RequestMapping(path = "exportMemberList", method = RequestMethod.GET)
    public void exportMemberList(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
        List<Member> memberList = LocalJsonUtil.getListFromJson("json/members.json", Member.class);
        ExportParams exportParams = new ExportParams("会员列表", "会员列表", ExcelType.XSSF);
        modelMap.put(NormalExcelConstants.DATA_LIST, memberList);
        modelMap.put(NormalExcelConstants.CLASS, Member.class);
        modelMap.put(NormalExcelConstants.PARAMS, exportParams);
        modelMap.put(NormalExcelConstants.FILE_NAME, "memberList");
        PoiBaseView.render(modelMap, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }
}
