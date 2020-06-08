package com.dafang.monitor.nx.utils;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.xssf.usermodel.*;

import java.awt.*;
import java.util.List;
/**
 * poi导出EXCEL工具类
 * @author jxd
 *
 */
public class ExpExcelUtils {
	/**
	 * 创建excel表格
	 * @param wb  工作簿
	 * @param tableName  excel头名字
	 * @param sheetName  excel sheet名字
	 * @param sheetHead  excel表头
	 * @param color      excel表头颜色
	 * @param rowSize    行数
	 * @param val        值
	 */
	public static void createExcel(XSSFWorkbook wb,String tableName,String sheetName,Object[] sheetHead,Color color,int rowSize,List<List<String>> val){
		XSSFSheet sheet=wb.createSheet(sheetName);
		XSSFRow row1 = sheet.createRow((short)0);
		for (int i = 0; i < sheetHead.length; i++) {
			cteateCell(wb,row1,(short)i,HSSFCellStyle.ALIGN_CENTER_SELECTION,sheetHead[i].toString(),color);
		}
		for(int i=0;i<rowSize;i++){
			XSSFRow row2 = sheet.createRow((short)i+1);
			for (int j = 0; j < val.get(i).size(); j++) {
				cteateCell(wb,row2,(short)j,HSSFCellStyle.ALIGN_CENTER_SELECTION,val.get(i).get(j),color);
			}
		}
	} 
	/**
	 * 创建excel列
	 * @param wb    工作簿
	 * @param row   行数
	 * @param align 对齐方式
	 * @param val   值
	 * @param color 表头颜色
	 */
	 public static void cteateCell(XSSFWorkbook wb,XSSFRow row,short column,short align,String val,Color color) {
		 	XSSFFont font = wb.createFont();
		 	font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		 	XSSFCell cell=row.createCell(column);
	        try {
				cell.setCellValue(Double.parseDouble(val)); //如果是数字格式就用数字转换
			} catch (Exception e) {
				cell.setCellValue(val); //否则就直接使用原来的格式
			}
	        XSSFCellStyle cellstyle=wb.createCellStyle();
	        cellstyle.setAlignment(align);
	        cell.setCellStyle(cellstyle);
	 }
}
