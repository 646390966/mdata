package com.dataan.controller;

import com.dataan.service.ModelDataService;
import com.dataan.utils.Response;
import com.dataan.vo.Pageable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhan bing liang
 * @date 2024/7/8 8:49
 */
@RestController
@RequestMapping("/model-data")
public class ModelDataController {

    @Autowired
    private ModelDataService modelDataService;

    @PostMapping("/{modelName}")
    public ResponseEntity<List<Map<String,Object>>> createModelData(@PathVariable String modelName, @RequestBody  List<Map<String,Object>> datas) {
        List<Map<String,Object>>  res = modelDataService.createModelData(modelName,datas);
        return Response.created(res);
    }

    @PutMapping("/{modelName}/{id}")
    public ResponseEntity<Void> updateModelData(@PathVariable String modelName, @PathVariable String id,@RequestBody Map<String,Object> data) {
        modelDataService.updateModelData(modelName,id,data);
        return Response.noContent();
    }

    @DeleteMapping("/{modelName}/{id}")
    public ResponseEntity<Void> deleteModelData(@PathVariable String modelName, @PathVariable String id) {
        modelDataService.deleteModelData(modelName,id);
        return Response.noContent();
    }


    @GetMapping("/{modelName}/all")
    public ResponseEntity<Pageable<Map<String,Object>>> getModelDataList(@PathVariable String modelName,@RequestParam Map<String, Object> allParams) {
        Integer page = Integer.parseInt(allParams.getOrDefault("page",1).toString());
        Integer size = Integer.parseInt(allParams.getOrDefault("size",10).toString());
        String orderColumn = allParams.getOrDefault("orderColumn","").toString();
        Boolean desc = allParams.getOrDefault("order","true").toString().equals("true");
        allParams.remove("page");
        allParams.remove("size");
        allParams.remove("orderColumn");
        allParams.remove("order");
        List<Map<String,Object>>  res = modelDataService.getModelDataList(modelName,page,size,orderColumn,desc,allParams);
        return Response.ok(new Pageable<>(res,modelDataService.getTotalCount(modelName),page,size ));
    }

    @GetMapping("/{modelName}/export/template")
    public void exportModelDataTemplate(@PathVariable String modelName,HttpServletResponse response) throws IOException {
        modelDataService.exportModelDataTemplate(modelName,response);


    }

    @GetMapping("/{modelName}/export/all")
    public void exportModelDataList(@PathVariable String modelName,@RequestParam Map<String, Object> allParams, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> allData = getModelDataList(modelName, allParams).getBody().getData();

        Workbook workbook = modelDataService.exportModelDataList(modelName,allData);

        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", String.format("attachment;filename=%s.xlsx", java.net.URLEncoder.encode(modelName, "UTF-8")));
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }
}
