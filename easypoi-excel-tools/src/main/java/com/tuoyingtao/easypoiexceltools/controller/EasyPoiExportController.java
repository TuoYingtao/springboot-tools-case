package com.tuoyingtao.easypoiexceltools.controller;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.tuoyingtao.easypoiexceltools.entity.Member;
import com.tuoyingtao.easypoiexceltools.entity.Order;
import com.tuoyingtao.easypoiexceltools.handler.ExcelDataStyleHandler;
import com.tuoyingtao.easypoiexceltools.handler.MemberExcelDataHandler;
import com.tuoyingtao.easypoiexceltools.util.DataJsonUtil;
import com.tuoyingtao.easypoiexceltools.util.LocalJsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tuoyingtao
 * @create 2022-10-28 18:04
 */
@Controller
@RequestMapping("/easyPoi/")
@Api(tags = "EasyPoiExportController", description = "导出Excel列表")
public class EasyPoiExportController {

    @ApiOperation(value = "会员列表-导出Excel")
    @RequestMapping(path = "exportMemberList", method = RequestMethod.GET)
    public void exportMemberList(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
        List<Member> memberList = DataJsonUtil.getMemberList();
        ExportParams exportParams = new ExportParams("会员列表", "会员列表", ExcelType.XSSF);
        modelMap.put(NormalExcelConstants.DATA_LIST, memberList);
        modelMap.put(NormalExcelConstants.CLASS, Member.class);
        modelMap.put(NormalExcelConstants.PARAMS, exportParams);
        modelMap.put(NormalExcelConstants.FILE_NAME, "memberList");
        PoiBaseView.render(modelMap, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation(value = "订单嵌套列表-导出Excel")
    @RequestMapping(value = "exportOrderList", method = RequestMethod.GET)
    public void exportOrderList(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
        List<Order> orderList = DataJsonUtil.getOrderList();
        ExportParams exportParams = new ExportParams("订单列表", "订单列表", ExcelType.XSSF);
        exportParams.setExclusions(new String[]{"出生日期", "订单ID", "商品ID", "性别"});
        modelMap.put(NormalExcelConstants.DATA_LIST, orderList);
        modelMap.put(NormalExcelConstants.CLASS, Order.class);
        modelMap.put(NormalExcelConstants.PARAMS, exportParams);
        modelMap.put(NormalExcelConstants.FILE_NAME, "orderList");
        PoiBaseView.render(modelMap, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation(value = "自定义字段处理器-导出Excel")
    @RequestMapping(value = "exportMemberListCustomField", method = RequestMethod.GET)
    public void exportMemberListCustomField(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
        List<Member> memberList = DataJsonUtil.getMemberList();
        ExportParams exportParams = new ExportParams("会员列表", "会员列表", ExcelType.XSSF);
        // 对导出结果进行自定义处理
        MemberExcelDataHandler memberExcelDataHandler = new MemberExcelDataHandler();
        memberExcelDataHandler.setNeedHandlerFields(new String[]{"昵称"});
        exportParams.setDataHandler(memberExcelDataHandler);
        modelMap.put(NormalExcelConstants.DATA_LIST, memberList);
        modelMap.put(NormalExcelConstants.CLASS, Member.class);
        modelMap.put(NormalExcelConstants.PARAMS, exportParams);
        modelMap.put(NormalExcelConstants.FILE_NAME, "customFieldMemberList");
        PoiBaseView.render(modelMap, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation(value = "自定义样式处理器-导出Excel")
    @RequestMapping(value = "exportMemberListCustomStyle", method = RequestMethod.GET)
    public void exportMemberListCustomStyle(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
        List<Member> memberList = DataJsonUtil.getMemberList();
        ExportParams exportParams = new ExportParams("会员列表", "会员列表", ExcelType.XSSF);
        // 对导出结果进行自定义处理
        MemberExcelDataHandler memberExcelDataHandler = new MemberExcelDataHandler();
        memberExcelDataHandler.setNeedHandlerFields(new String[]{"昵称"});
        exportParams.setDataHandler(memberExcelDataHandler);
        exportParams.setStyle(ExcelDataStyleHandler.class);
        modelMap.put(NormalExcelConstants.DATA_LIST, memberList);
        modelMap.put(NormalExcelConstants.CLASS, Member.class);
        modelMap.put(NormalExcelConstants.PARAMS, exportParams);
        modelMap.put(NormalExcelConstants.FILE_NAME, "customFieldMemberList");
        PoiBaseView.render(modelMap, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation(value = "自定义动态列-导出Excel")
    @RequestMapping(value = "exportMemberListCustomRow", method = RequestMethod.GET)
    public void exportMemberListCustomRow(HttpServletResponse response) {
        try {
            List<ExcelExportEntity> memberExcelEntity = new ArrayList<>();
            // 构建Excel对象等同于@Excel
            memberExcelEntity.add(new ExcelExportEntity("Custom_姓名", "username"));
            memberExcelEntity.add(new ExcelExportEntity("昵称", "nickname"));
            memberExcelEntity.add(new ExcelExportEntity("Custom_出生日期", "birthday"));
            memberExcelEntity.add(new ExcelExportEntity("Custom_性别", "gender"));
            memberExcelEntity.add(new ExcelExportEntity("Custom_会员用户ID", "id"));

            ExportParams exportParams = new ExportParams("订单列表", "订单列表", ExcelType.XSSF);
            // 构建字段处理器
            MemberExcelDataHandler memberExcelDataHandler = new MemberExcelDataHandler();
            memberExcelDataHandler.setNeedHandlerFields(new String[]{"昵称"});
            // 设置字段处理器
            exportParams.setDataHandler(memberExcelDataHandler);
            // 新建Excel工作簿
            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, memberExcelEntity, DataJsonUtil.getMemberList());
            response.setHeader("content-Type","application/vnd.ms-excel");
            String excelFileName = URLEncoder.encode("customRowOrderList", String.valueOf(StandardCharsets.UTF_8)) + ".xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=" + excelFileName);
            response.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO java.util.HashMap cannot be cast to cn.afterturn.easypoi.excel.entity.ExportParams
    @ApiOperation(value = "自定义嵌套-导出Excel")
    @RequestMapping(value = "exportNest", method = RequestMethod.GET)
    public void exportNest(HttpServletResponse response) {
        try {
            List<Order> orderList = DataJsonUtil.getOrderList();
            // 创建参数对象（用来设定excel得sheet得内容等信息）
            ExportParams orderExportParams = new ExportParams("订单列表", "订单列表");
            // 创建sheet1使用得map
            Map<String, Object> orderMap = new HashMap<>();
            // title的参数为ExportParams类型
            orderMap.put("title", orderExportParams);
            // 模版导出对应得实体类型
            orderMap.put("entity", Order.class);
            // sheet中要填充得数据
            orderMap.put("data", orderList);

            ExportParams memberExportParams = new ExportParams();
            memberExportParams.setSheetName("会员信息");
            Map<String, Object> memberMap = new HashMap<>();
            memberMap.put("title", memberMap);
            memberMap.put("entity", Order.class);
            memberMap.put("data", orderList);

            List<Map<String, Object>> sheetList = new ArrayList<>();
            sheetList.add(orderMap);
            sheetList.add(memberMap);

            // 新建Excel工作簿
            Workbook workbook = ExcelExportUtil.exportExcel(sheetList, ExcelType.HSSF);
            response.setHeader("content-Type","application/vnd.ms-excel");
            String excelFileName = URLEncoder.encode("exportNest", String.valueOf(StandardCharsets.UTF_8)) + ".xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=" + excelFileName);
            response.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
