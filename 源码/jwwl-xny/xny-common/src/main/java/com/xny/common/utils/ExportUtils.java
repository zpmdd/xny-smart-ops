package com.xny.common.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ExportUtils {
    /**
     *
     * @param response
     * @param list
     * @param clazz
     * @param <T>
     */
    public static <T> void export(HttpServletResponse response, List<T> list, Class<T> clazz,String fileName) {
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            EasyExcel.write(response.getOutputStream(), clazz)
                    .excelType(ExcelTypeEnum.XLSX)
                    .registerWriteHandler(getHorizontalCellStyleStrategy())
                    .registerWriteHandler(new CustomCellWriteHandler())
                    .sheet("sheet").doWrite(list);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("数据导出异常");
        }
    }

    public static <T> void export(HttpServletResponse response, List<ExportDTO> list,String fileName) {
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            ServletOutputStream outputStream = response.getOutputStream();

            ExcelWriter excelWriter = EasyExcel.write(outputStream)
                    .excelType(ExcelTypeEnum.XLSX)
                    .registerWriteHandler(getHorizontalCellStyleStrategy())
                    .registerWriteHandler(new CustomCellWriteHandler())
                    .build();

            for (int i = 0; i < list.size(); i++) {
                ExportDTO dto = list.get(i);
                // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样。这里注意DemoData.class 可以每次都变，我这里为了方便 所以用的同一个class
                // 实际上可以一直变
                WriteSheet writeSheet = EasyExcel.writerSheet(i, dto.getSheetName()).head(dto.getClazz()).build();
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                excelWriter.write(dto.getList(), writeSheet);
            }

            excelWriter.finish();
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(excelWriter);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("数据导出异常");
        }
    }

    public static void export(HttpServletResponse response, ScenicAnalysis scenicAnalysis) {
        try {
            String fileName = URLEncoder.encode("数据列表", "UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            ServletOutputStream outputStream = response.getOutputStream();
            //控制 导出字段 未控制顺序
            EasyExcel.write(outputStream)
                    .head(head(scenicAnalysis.getHeadList()))
                    .includeColumnFieldNames(scenicAnalysis.getColumnList())
                    .sheet("sheet")
                    .doWrite(dataList(scenicAnalysis.getDataList(),scenicAnalysis.getColumnList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义导出 简单表头
     * @param response
     * @param heads
     * @param contents
     * @param fileName
     * @param <T>
     */
    public static <T> void exportSimpleHead(HttpServletResponse response, List<String> heads, List<List<String>> contents,String fileName) {
        List<List<String>> headList = new ArrayList<>();
        for (String head : heads) {
            List<String> list = new ArrayList<>();
            list.add(head);
            headList.add(list);
        }

        export(response,headList,contents,fileName);
    }

    /**
     * 参考文档 https://blog.csdn.net/qq_34934815/article/details/122601147
     * @return
     */
    private static HorizontalCellStyleStrategy getHorizontalCellStyleStrategy() {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景设置为白色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        //边框
        headWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        headWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        headWriteCellStyle.setBorderRight(BorderStyle.THIN);
        headWriteCellStyle.setBorderTop(BorderStyle.THIN);
        //自动换行
        headWriteCellStyle.setWrapped(true);
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setBold(true);
        headWriteFont.setFontName("宋体");
        headWriteFont.setFontHeightInPoints((short)12);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 背景绿色
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //边框
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        //自动换行
        contentWriteCellStyle.setWrapped(true);
        //文字
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short)12);
        contentWriteFont.setFontName("宋体");
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }


    public static class CustomCellWriteHandler  extends AbstractColumnWidthStyleStrategy {
        @Override
        protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {
            // 简单设置
            Sheet sheet = writeSheetHolder.getSheet();
            sheet.setColumnWidth(cell.getColumnIndex(), 6000);
        }
    }


    /**
     * 根据传入的字段获取对应的get方法，如name,对应的getName方法
     * @param fieldName  字段名
     * @param person    对象
     * @return
     */
    private static Object getFieldValue(String fieldName, Object person) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = person.getClass().getMethod(getter);
            return method.invoke(person);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 设置表格信息
     * @param dataList  查询出的数据
     * @param fileList  需要显示的字段
     * @return
     */
    private static List<List<Object>> dataList(List<Object> dataList, List<String> fileList) {
        List<List<Object>> list = new ArrayList<>();
        for (Object person : dataList) {
            List<Object> data = new ArrayList<>();
            for (String fieldName : fileList) {
                /**通过反射根据需要显示的字段，获取对应的属性值*/
                data.add(getFieldValue(fieldName, person));
            }
            list.add(data);
        }
        return list;
    }

    /**
     * 设置Excel头
     * @param headList  Excel头信息
     * @return
     */
    private static List<List<String>> head(List<String> headList) {
        List<List<String>> list = new ArrayList<>();
        for (String value : headList) {
            List<String> head = new ArrayList<>();
            head.add(value);
            list.add(head);
        }
        return list;
    }

    /**
     * 自定义导出
     * @param response
     * @param heads
     * @param contents
     * @param <T>
     */
    public static <T> void export(HttpServletResponse response, List<List<String>> heads, List<List<String>> contents,String fileName) {
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

            EasyExcel.write(response.getOutputStream()).head(heads)
                    .excelType(ExcelTypeEnum.XLSX)
                    .registerWriteHandler(getHorizontalCellStyleStrategy())
                    .registerWriteHandler(new CustomCellWriteHandler())
                    .sheet("sheet").doWrite(contents);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("数据导出异常");
        }
    }

    @Data
    @Accessors(chain = true)
    public static class ScenicAnalysis{
        //需要导出的列的集合
        private List dataList;

        //需要导出的列的集合
        private List<String> columnList;

        //需要导出的列的集合
        private List<String> headList;
    }




    @Data
    @Accessors(chain = true)
    public static class ExportDTO{
        private String sheetName;

        private List list;

        private Class clazz;
    }

}
