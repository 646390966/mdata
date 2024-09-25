package com.dataan.service;

import com.dataan.entity.Model;
import java.util.Date;
import java.util.List;

/**
 * @author zhan bing liang
 * @date 2024/6/27 15:16
 */
public interface ModelService {
    /**
     * 新建模型
     * @param model
     * @return
     */
    Model createModel(Model model);

    /**
     * 获取模型列表
     * @param schema
     * @param page
     * @param size
     * @param createTimeStart
     * @param createTimeEnd
     * @param orderColumn
     * @param desc
     * @return
     */
    List<Model> getModelList(String schema, Integer page, Integer size, Date createTimeStart, Date createTimeEnd, String orderColumn, Boolean desc);

    /**
     * 获取总数量
     * @return
     */
    Integer getTotalCount();

    /**
     * 通过ID获取模型
     * @param id
     * @return
     */
    Model getModelById(String id);

    /**
     * 通过名称获取模型
     * @param name
     * @return
     */
    Model getModelByName(String name);

    /**
     * 删除模型
     * @param id
     */
    void deleteModel(String id);

    /**
     * 更新模型
     * @param model
     */
    void updateModel(Model model);
}
