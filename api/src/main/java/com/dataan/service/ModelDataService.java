package com.dataan.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author zhan bing liang
 * @date 2024/7/8 8:53
 */
public interface ModelDataService {
    /**
     * 新增数据
     * @param modelName
     * @param datas
     * @return
     */
    List<Map<String, Object>> createModelData(String modelName, List<Map<String, Object>> datas);

    /**
     * 修改数据
     * @param modelName
     * @param id
     * @param data
     */
    void updateModelData(String modelName, String id, Map<String, Object> data);

    /**
     * 删除数据
     * @param modelName
     * @param id
     */
    void deleteModelData(String modelName, String id);

    /**
     * 获取数据列表
     * @param allParams
     * @param modelName
     * @param page
     * @param size
     * @param orderColumn

     * @return
     */
    List<Map<String, Object>> getModelDataList(String modelName, Integer page, Integer size, String orderColumn, Boolean desc, Map<String, Object> searchParams);

    /**
     * 获取总数据量
     * @param modelName
     * @return
     */
    Integer getTotalCount(String modelName);

    void exportModelDataTemplate(String modelName, HttpServletResponse response) throws IOException;

    Workbook exportModelDataList(String modelName, List<Map<String, Object>> res) throws IOException;
}
