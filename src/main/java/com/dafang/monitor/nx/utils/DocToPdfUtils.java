package com.dafang.monitor.nx.utils;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;

public class DocToPdfUtils {

    private static final Log log = LogFactory.getLog(DocToPdfUtils.class);

    public static void main(String[] args) {
        String docPath = "E:\\zyj\\Product\\2019年02月01日-2019年01月01日重要气候信息.doc";
        String pdfPath = "E:\\zyj\\Product\\2019年02月01日-2019年01月01日重要气候信息.pdf";
        wordToPdf(docPath,pdfPath);
    }

    /**
     * word转pdf
     * @param docPath word路径
     * @param pdfPath pdf路径
     */
    public static void wordToPdf(String docPath, String pdfPath){
        long st = System.currentTimeMillis();
        //加载word示例文档
        Document document = new Document();
        document.loadFromFile(docPath);
        //保存为PDF格式
        document.saveToFile(pdfPath, FileFormat.PDF);
        long et = System.currentTimeMillis();
        long time = (et - st)/1000;
        log.info(pdfPath + "生成成功，用时" + time + "秒");
    }
}

