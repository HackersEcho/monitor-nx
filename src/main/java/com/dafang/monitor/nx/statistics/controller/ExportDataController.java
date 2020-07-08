package com.dafang.monitor.nx.statistics.controller;

import com.dafang.monitor.nx.config.Apicp;
import com.dafang.monitor.nx.statistics.entity.dto.ResuleDto;
import com.dafang.monitor.nx.statistics.entity.po.ExportDataParam;
import com.dafang.monitor.nx.statistics.service.ExportDataService;
import com.dafang.monitor.nx.utils.CommonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;


/**
 * @description:
 * @author: renxin
 * @createDate: 2020/06/16
 * @version: 1.0
 */
@RestController
@Api(value = "exportData",tags = {"导出数据处理"})
@RequestMapping(value = "common/")
public class ExportDataController {
   @Autowired
    private ExportDataService exportDataService;
    @PostMapping(value = "exportData")
    @ApiOperation(value = "导出",notes = "导出数据")
    public ResuleDto<File> exportData(@Apicp("table,fileNames,col,colStr,colTextStr," +
            "startTime,endTime,fileType,regions,min,max") @RequestBody ExportDataParam params){
        ResuleDto<File> resuleDto = new ResuleDto<>();
        params.setCondition(CommonUtils.getCondition(params.getRegions()));
        //得到返回文件流
        File file = exportDataService.exportData(params);
        resuleDto.setRespData(file);
        return resuleDto;
    }
    @GetMapping(value = "download")
    @ApiOperation(value = "获取导出地址",notes = "导出数据")
    public ResponseEntity<byte[]> download(HttpServletRequest request) throws IOException {
        String filePath = request.getParameter("filePath");
        File file = new File(filePath);

        HttpHeaders headers = new HttpHeaders();
        String fileName = new String(file.getName().getBytes("UTF-8"), "iso-8859-1");// 为了解决中文名称乱码问题
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }

}
