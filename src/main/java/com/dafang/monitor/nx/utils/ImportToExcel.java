package com.dafang.monitor.nx.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


public class ImportToExcel {

	/**
	 * 分文件导出Excel
	 * 
	 * @param list       数据结果集
	 * @param length     单个文件行数
	 * @param fileName   文件类型名称
	 * @param colTextStr 文件头名称
	 * @param fileNames
	 * @throws IOException
	 */
	public void toExcel(List<Map<String, Object>> list, int length, String fileName, String col, String colTextStr,
			List<String> fileNames) throws IOException {
		// 2.计算文件个数
		for (int j = 0, n = list.size() / length + 1; j < n; j++) {
			SXSSFWorkbook book = new SXSSFWorkbook();
			SXSSFSheet sheet = (SXSSFSheet) book.createSheet("日数据");
			FileOutputStream input = null;
			// 计算文件所要写入的行数
			int currtRowCount = (list.size() - j * length) > length ? length : (list.size() - j * length);
			// 生成文件名称 格式为：文件类型_生成时间_第一条数据时间_最后一条数据时间
			// 文件第一条数据时间

			String firstDataString = list.get(length * (j)).get("ObserverTime").toString();
			String lastDataString = list.get(length * (j) + (currtRowCount - 1)).get("ObserverTime").toString();
			Date firstData = null;
			Date lastData = null;
			try {
				firstData = new SimpleDateFormat("yyyy-MM-dd HH:00:00").parse(firstDataString);
				lastData = new SimpleDateFormat("yyyy-MM-dd HH:00:00").parse(lastDataString);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			String file = PropertiesConfig.readData("excelFilePath") + fileName + "_"
					+ new SimpleDateFormat("yyyyMMddHH").format(firstData) + "_"
					+ new SimpleDateFormat("yyyyMMddHH").format(lastData) + ".xls";
			fileNames.add(file);
			try {
				input = new FileOutputStream(file);
				// 在sheet里创建第一行，参数为行索引(excel的行)
				Row row = sheet.createRow(0);
				String[] colArr = colTextStr.split(",");
				// 创建Excel表头
				for (int q = 0; q < colArr.length; q++) {
					row.createCell(q).setCellValue(colArr[q]);
				}
				for (int i = 1; i <= currtRowCount; i++) {
					Map<String, Object> map = list.get(length * (j) + i - 1);
					row = sheet.createRow(i);
					for (int q = 0; q < colArr.length; q++) {
						if (map.get(col.split(",")[q]) != null
								&& StringUtils.isNotBlank(map.get(col.split(",")[q]).toString())) {
							row.createCell(q).setCellValue(map.get(col.split(",")[q]).toString());
						} else {
							row.createCell(q).setCellValue("--");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				book.write(input);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				input.flush();
				input.close();
			}
		}
		System.out.println("导出完成");
	}

	/**
	 * 4.压缩数据文件
	 * 
	 * @param srcfile 需要压缩文件序列
	 * @param zipfile 压缩后的文件名
	 */
	public void ZipFiles(File[] srcfile, File zipfile) {
		byte[] buf = new byte[1024];
		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
			for (int i = 0; i < srcfile.length; i++) {
				FileInputStream in = new FileInputStream(srcfile[i]);
				out.putNextEntry(new ZipEntry(srcfile[i].getName()));
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.closeEntry();
				in.close();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 导出csv格式
	 * 
	 * @param list
	 * @param length
	 * @param fileName
	 * @param col
	 * @param colTextStr
	 * @param fileNames
	 * @throws IOException
	 */
	public void toCSV(List<Map<String, Object>> list, int length, String fileName, String col, String colTextStr,
			List<String> fileNames) throws IOException {
		// 导出为CSV文件
		long t1 = System.currentTimeMillis();
		// 1.计算文件个数
		// 数据文件个数
		int fileCount = (list.size() % length) > 0 ? (list.size() / length) + 1 : (list.size() / length);
		for (int j = 0; j < fileCount; j++) {
			int currtRowCount = (list.size() - j * length) > length ? length : (list.size() - j * length);
			// 生成文件名称 格式为：文件类型_生成时间_第一条数据时间_最后一条数据时间
			// 文件第一条数据时间
			String firstDataString = list.get(length * (j)).get("observerTime").toString();
			// 文件第最后一条数据时间
			String lastDataString = list.get(length * (j) + (currtRowCount - 1)).get("observerTime").toString();
			Date firstData = null;
			Date lastData = null;
			try {
				firstData = new SimpleDateFormat("yyyy-MM-dd HH:00:00").parse(firstDataString);
				lastData = new SimpleDateFormat("yyyy-MM-dd HH:00:00").parse(lastDataString);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String file = PropertiesConfig.readData("excelFilePath") + fileName + "_"
					+ new SimpleDateFormat("yyyyMMddHH").format(firstData) + "_"
					+ new SimpleDateFormat("yyyyMMddHH").format(lastData) + ".csv";
			fileNames.add(file);
			CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator("\n");
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream, "gbk");
			CSVPrinter printer = new CSVPrinter(osw, format);
			// 写入文件头信息
			Object[] colArr = colTextStr.split(",");
			printer.printRecord(colArr);
			// 写入数据
			for (int i = 0; i < currtRowCount; i++) {
				Map<String, Object> map = list.get(length * (j)+i );
				String colValueString = "";
				for (int q = 0; q < colArr.length; q++) {
					if (map.get(col.split(",")[q]) != null
							&& StringUtils.isNotBlank(map.get(col.split(",")[q]).toString())) {
						colValueString += map.get(col.split(",")[q]).toString() + ",";
					} else {
						colValueString += "--";
					}
				}
				Object[] objects = colValueString.split(",");
				printer.printRecord(objects);
			}
			printer.flush();
			printer.close();
			System.gc();
		}
		long t2 = System.currentTimeMillis();
		System.out.println("CSV: " + (t2 - t1));
	}

	/**
	 * 导出txt格式
	 * 
	 * @param tempList
	 * @param i
	 * @param fie_Name
	 * @param colStr
	 * @param colTextStr
	 * @param fileNames
	 * @throws IOException
	 */
	public void toTxt(List<Map<String, Object>> list, int length, String fileName, String col, String colTextStr,
			List<String> fileNames) throws IOException {
		long t1 = System.currentTimeMillis();
		// 1.计算文件个数
		// 数据文件个数
		int fileCount = (list.size() % length) > 0 ? (list.size() / length) + 1 : (list.size() / length);
		for (int j = 0; j < fileCount; j++) {
			int currtRowCount = (list.size() - j * length) > length ? length : (list.size() - j * length);
			// 生成文件名称 格式为：文件类型_生成时间_第一条数据时间_最后一条数据时间
			// 文件第一条数据时间
			String firstDataString = list.get(length * (j)).get("ObserverTime").toString();
			// 文件第最后一条数据时间
			String lastDataString = list.get(length * (j) + (currtRowCount - 1)).get("ObserverTime").toString();
			Date firstData = null;
			Date lastData = null;
			try {
				firstData = new SimpleDateFormat("yyyy-MM-dd HH:00:00").parse(firstDataString);
				lastData = new SimpleDateFormat("yyyy-MM-dd HH:00:00").parse(lastDataString);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String file = PropertiesConfig.readData("excelFilePath") + fileName + "_"
					+ new SimpleDateFormat("yyyyMMddHH").format(firstData) + "_"
					+ new SimpleDateFormat("yyyyMMddHH").format(lastData) + ".txt";
			fileNames.add(file);

			BufferedWriter out = null;

			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
			// 写入头部
			out.write(colTextStr.replace(",", "\t\t") + "\r\n");
			// 写入数据

			for (int i = 0; i < currtRowCount; i++) {
				Map<String, Object> map = list.get(length * (j) + i);
				StringBuffer colValueString = new StringBuffer();
				Object[] colArr = colTextStr.split(",");
				for (int q = 0; q < colArr.length; q++) {
					if (map.get(col.split(",")[q]) != null
							&& StringUtils.isNotBlank(map.get(col.split(",")[q]).toString())) {
						colValueString.append(map.get(col.split(",")[q]).toString());
					} else {
						colValueString.append("--");
					}
					colValueString.append("\t");
					colValueString.append("\t");
				}
				colValueString.append("\r\n");
				out.write(colValueString.toString());
			}
			out.flush();
			out.close();
			System.gc();
		}
		long t2 = System.currentTimeMillis();
		System.out.println("CSV: " + (t2 - t1));
	}

	/**
	 * 根据传入参数进行导出
	 * 
	 * @param list
	 * @param fileName
	 * @param col
	 * @param colTextStr
	 * @param fileNames
	 * @throws IOException
	 */
	public String toCSVBySendData(List<Map<String, Object>> list, String fileName, String col, String colTextStr)
			throws IOException {

		String file = PropertiesConfig.readData("excelFilePath") + fileName + ".csv";
		CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator("\n");
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream, "gbk");
		CSVPrinter printer = new CSVPrinter(osw, format);
		// 写入文件头信息
		Object[] colArr = colTextStr.split(",");
		printer.printRecord(colArr);
		// 写入数据
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			String colValueString = "";
			for (int q = 0; q < colArr.length; q++) {
				if (map.get(col.split(",")[q]) != null
						&& StringUtils.isNotBlank(map.get(col.split(",")[q]).toString())) {
					colValueString += map.get(col.split(",")[q]).toString() + ",";
				} else {
					colValueString += "--";
				}
			}
			Object[] objects = colValueString.split(",");
			printer.printRecord(objects);
		}
		printer.flush();
		printer.close();
		System.gc();
		return file;
	}

	/**
	 * 导出txt格式
	 *
	 */
	public String toTXTBySendData(List<Map<String, Object>> list, String fileName, String col, String colTextStr)
			throws IOException {
		String file = PropertiesConfig.readData("excelFilePath") + fileName + ".txt";
		BufferedWriter out = null;

		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
		// 写入头部
		out.write(colTextStr.replace(",", "\t\t") + "\r\n");
		// 写入数据

		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			StringBuffer colValueString = new StringBuffer();
			Object[] colArr = colTextStr.split(",");
			for (int q = 0; q < colArr.length; q++) {
				if (map.get(col.split(",")[q]) != null
						&& StringUtils.isNotBlank(map.get(col.split(",")[q]).toString())) {
					colValueString.append(map.get(col.split(",")[q]).toString());
				} else {
					colValueString.append("--");
				}
				colValueString.append("\t");
				colValueString.append("\t");
			}
			colValueString.append("\r\n");
			out.write(colValueString.toString());
		}
		out.flush();
		out.close();
		System.gc();
		return file;
	}
}
