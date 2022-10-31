package com.tuoyingtao.easypoiexceltools.handler;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.entity.params.ExcelForEachParams;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

/**
 * @author tuoyingtao
 * @create 2022-10-31 14:05
 */
public class ExcelDataStyleHandler implements IExcelExportStyler {

    /** 数据行类型 */
    private static final String HEADER_STYLES = "headerStyles";
    /** 数据行类型 */
    private static final String DATA_STYLES = "dataStyles";
    /** 标题类型 */
    private static final String TITLE_STYLES = "titleStyles";

    /** 默认字体加粗 */
    private static final Boolean DEFAULT_BOLD = false;
    /** 默认主体字体 */
    private static final String DEFAULT_FONT_NAME = "微软雅黑 Light";
    /** 默认字体大小 */
    private static final Short DEFAULT_SIZE = new Short((short) 10);

    /** 默认不换行 */
    private static final Boolean DEFAULT_NEW_LINE = false;
    /** 默认边框的颜色 */
    private static final Short DEFAULT_HSSF_COLOR = HSSFColor.HSSFColorPredefined.BLACK.getIndex();
    /** 默认边框的粗细 */
    private static final BorderStyle DEFAULT_BORDER_STYLE = BorderStyle.THIN;

    /** 表头样式 */
    private CellStyle headerStyle;
    /** 数据行样式 */
    private CellStyle dataStyle;
    /** 标题样式 */
    private CellStyle titleStyle;

    public ExcelDataStyleHandler(Workbook workbook) {
        initWorkBookStyle(workbook);
    }

    public void initWorkBookStyle(Workbook workbook) {
        this.headerStyle = initHeaderStyles(workbook);
        this.dataStyle = initDataStyles(workbook);
        this.titleStyle = initTitleStyles(workbook);
    }

    /**
     * 列表头样式
     */
    @Override
    public CellStyle getHeaderStyle(short headerColor) {
        return headerStyle;
    }

    /**
     * 标题样式
     */
    @Override
    public CellStyle getTitleStyle(short color) {
        return titleStyle;
    }

    /**
     * 获取样式方法
     */
    @Override
    public CellStyle getStyles(boolean parity, ExcelExportEntity entity) {
        return dataStyle;
    }

    /**
     * 获取样式方法
     */
    @Override
    public CellStyle getStyles(Cell cell, int dataRow, ExcelExportEntity entity, Object obj, Object data) {
        return getStyles(true, entity);
    }

    /**
     * 模板使用的样式设置
     */
    @Override
    public CellStyle getTemplateStyles(boolean isSingle, ExcelForEachParams excelForEachParams) {
        return null;
    }

    /**
     * 初始化--表头样式
     */
    private CellStyle initHeaderStyles(Workbook workbook) {
        return buildCellStyle(workbook, HEADER_STYLES);
    }

    /**
     * 初始化--数据行样式
     * @param workbook
     * @return
     */
    private CellStyle initDataStyles(Workbook workbook) {
        return buildCellStyle(workbook, DATA_STYLES);
    }

    /**
     * 初始化--标题行样式
     * @param workbook
     * @return
     */
    private CellStyle initTitleStyles(Workbook workbook) {
        return buildCellStyle(workbook, TITLE_STYLES);
    }

    /**
     * 设置单元格样式
     * @param workbook
     * @param type 类型  用来区分是数据行样式还是标题样式
     * @return
     */
    private CellStyle buildCellStyle(Workbook workbook, String type) {
        // 创建一个新的单元格样式并将其添加到工作簿的样式表中
        CellStyle style = workbook.createCellStyle();
        // 创建一个新字体并将其添加到工作簿的字体表中
        Font font = baseFont(workbook);
        if (HEADER_STYLES.equals(type)) {
            font = setFont(workbook, (short) 12, true);
        }
        if(TITLE_STYLES.equals(type)){
            font = setFont(workbook, (short) 12, true);
        }
        if(DATA_STYLES.equals(type)){
            font = baseFont(workbook);
        }
        // 设置字体样式
        style.setFont(font);
        // 设置基本样式
        baseCellStyle(style);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }



    /**
     *
     * TODO 设置单元格背景色
     * @param cellStyle
     * @param shortBackgroundColor
     * @return
     */
    public CellStyle setBackgroundColor(Workbook workbook, CellStyle cellStyle, Short shortBackgroundColor) {
        // 获取sheetAt对象，这里一个sheetAt所以角标是0
        Sheet sheetAt = workbook.getSheetAt(0);
        // 表格行数
        int lastRowNum = sheetAt.getLastRowNum();
        // 获取列数
        int physicalNumberOfCells = sheetAt.getRow(0).getPhysicalNumberOfCells();
        // 开始遍历单元格并进行判断是否渲染，从第二行开始
        for (int i = 2; i <= lastRowNum; i++) {
            // 获取每行对象
            Row row = sheetAt.getRow(i);
            for (int j = 1; j < physicalNumberOfCells; j++) {
                // 获取单元格对象
                Cell cell = row.getCell(j);
                cellStyle.setFillBackgroundColor(shortBackgroundColor);
                cell.setCellStyle(cellStyle);
            }
        }
        return cellStyle;
    }

    /**
     * 基本样式
     * @param cellStyle
     * @return
     */
    private CellStyle baseCellStyle(CellStyle cellStyle) {
        return setCellStyle(cellStyle);
    }

    public CellStyle setCellStyle(CellStyle cellStyle) {
        return setCellStyle(cellStyle, DEFAULT_BORDER_STYLE, DEFAULT_HSSF_COLOR);
    }

    public CellStyle setCellStyle(CellStyle cellStyle, BorderStyle borderStyle) {
        return setCellStyle(cellStyle, borderStyle, DEFAULT_HSSF_COLOR);
    }

    public CellStyle setCellStyle(CellStyle cellStyle, Short hssfColor) {
        return setCellStyle(cellStyle, DEFAULT_BORDER_STYLE, hssfColor, DEFAULT_NEW_LINE);
    }

    public CellStyle setCellStyle(CellStyle cellStyle, BorderStyle borderStyle, Short hssfColor) {
        return setCellStyle(cellStyle, borderStyle, hssfColor, DEFAULT_NEW_LINE);
    }

    public CellStyle setCellStyle(CellStyle cellStyle, Boolean newline) {
        return setCellStyle(cellStyle, DEFAULT_BORDER_STYLE, DEFAULT_HSSF_COLOR, newline);
    }

    public CellStyle setCellStyle(CellStyle cellStyle, BorderStyle borderStyle, Short hssfColor, Boolean newline) {
        // 设置底边框
        cellStyle.setBorderBottom(borderStyle);
        // 设置左边框
        cellStyle.setBorderLeft(borderStyle);
        // 设置右边框;
        cellStyle.setBorderRight(borderStyle);
        // 设置顶边框;
        cellStyle.setBorderTop(borderStyle);

        // 设置底边颜色
        cellStyle.setBottomBorderColor(hssfColor);
        // 设置左边框颜色;
        cellStyle.setLeftBorderColor(hssfColor);
        // 设置右边框颜色;
        cellStyle.setRightBorderColor(hssfColor);
        // 设置顶边框颜色;
        cellStyle.setTopBorderColor(hssfColor);

        // 设置自动换行;
        cellStyle.setWrapText(newline);
        return cellStyle;
    }

    /**
     * 设置字体样式
     * @param workbook
     * @return
     */
    public Font baseFont(Workbook workbook) {
        return setFont(workbook, DEFAULT_SIZE);
    }

    public Font setFont(Workbook workbook, short size) {
        return setFont(workbook, size, DEFAULT_FONT_NAME);
    }

    public Font setFont(Workbook workbook, short size, boolean isBold) {
        return setFont(workbook, size, DEFAULT_FONT_NAME, DEFAULT_BOLD);
    }

    public Font setFont(Workbook workbook, short size, String fontName) {
        return setFont(workbook, size, fontName, DEFAULT_BOLD);
    }

    public Font setFont(Workbook workbook, short size, String fontName, boolean isBold) {
        // 创建一个新字体并将其添加到工作簿的字体表中
        Font font = workbook.createFont();
        // 设置字体名称
        font.setFontName(fontName);
        // 设置字体高度
        font.setFontHeightInPoints(size);
        // 设置加粗
        font.setBold(isBold);
        return font;
    }
}
