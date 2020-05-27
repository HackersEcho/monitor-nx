package com.dafang.monitor.nx.product;


import com.dafang.monitor.nx.product.entity.po.DirectoryParams;
import com.dafang.monitor.nx.product.entity.po.Product;
import com.dafang.monitor.nx.product.entity.po.ProductParams;
import com.dafang.monitor.nx.product.mapper.DirectoryMapper;
import com.dafang.monitor.nx.utils.DocToPdfUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description:模板的公共模板
 * @author: echo
 * @createDate: 2020/4/17
 * @version: 1.0
 */
public abstract class TemplateAbstract {

    @Autowired
    DirectoryMapper mapper;
    private static final Log log = LogFactory.getLog(TemplateAbstract.class);

    // 受保护的属性放在init()数据初始化里面赋值
    protected String templateName;//模板名称
//    private String filePath = "D:\\product";// 文件生成的目标路径，例如：D:/wordFile/
//    private String filePath = "W:\\newkmlfile\\Product";// 文件生成的目标路径，例如：D:/wordFile/
    private String filePath = "D:\\product\\";// 文件生成的目标路径，例如：D:/wordFile/
    protected String fileName;//文件名称
    protected String startData;
    protected String endData;
    protected String startMonthDay;
    protected String endMonthDay;
    protected String year;//当前年
    protected String season;//季
    protected String month;//月
    protected String day;//日
    protected List<Product> baseData;//所有数据
    protected List<Product> perenList;//常年值数据
    protected List<Product> currentList;//当年数据
    protected List<Product> baseDayData;//所有数据
    protected List<Product> perenDayList;//常年值数据
    protected List<Product> currentDayList;//当年数据

    // 所有产品的入口
    public void entrance(ProductParams params){
        String path = filePath;
        init(params);
        Map<String,Object> dataMap = getDatas(params);
        String doc = fileName+".doc";
        String pdf = fileName+".pdf";
        String word = createWord(dataMap, templateName, doc);
        if (word == null){
            log.error(filePath+fileName+"生成失败！");
        }else{
            log.info(filePath+fileName+"生产成功！");
            //pdf转换
            DocToPdfUtils.wordToPdf(filePath+doc,filePath+pdf);
            //入库
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String createTime = df.format(date);
            String[] files = {doc,pdf};
            path = path.replaceAll("\\\\","\\\\\\\\");
            for (String file : files) {
                DirectoryParams deleteParams = new DirectoryParams(file);
                mapper.deleteDirectories(deleteParams);
                DirectoryParams insertParams = new DirectoryParams(file,path+file,createTime,params.getProductId(),file.substring(file.length()-3,file.length()));
                mapper.insertDirectories(insertParams);
            }
            log.info("end");
        }
    }

    // 初始化数据
    protected abstract void init(ProductParams params);
    // 获取所有的数据,放入map集合
    protected abstract Map<String, Object> getDatas(ProductParams params);
    /*
     *
     *生成word文件
     * @param dataMap word中需要展示的动态数据，用map集合来保存
     * @param templateName word模板名称，例如：06test.ftl
     * @param filePath 文件生成的目标路径，例如：D:/wordFile/
     * @param fileName 生成的文件名称，例如：test.doc
     * @return java.lang.String
     * @author echo
     * @date 2020/4/17
     */
    private String createWord(Map<String, Object> dataMap, String templateName, String fileName){
        try {
            //创建配置实例
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);

            //设置编码
            configuration.setDefaultEncoding("UTF-8");

            //ftl模板文件统一放至 resources\template
            configuration.setClassForTemplateLoading(this.getClass(),"/template");

            //获取模板
            Template template = configuration.getTemplate(templateName);

            //输出文件
            File outFile = new File(filePath+ File.separator+fileName);

            //如果输出目标文件夹不存在，则创建
            if (!outFile.getParentFile().exists()){
                outFile.getParentFile().mkdirs();
            }

            //将模板和数据模型合并生成文件
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));

            //生成文件
            template.process(dataMap, out);

            //关闭流
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String WJ=filePath+fileName;
        return WJ;
    }
}
