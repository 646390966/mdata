package com.dataan.mapper;

import com.dataan.entity.Model;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

/**
 * @author zhan bing liang
 * @date 2024/4/15 15:08
 */
@Mapper
public interface ModelMapper {
    /**
     * 新建模型
     * @param model
     */
    void createModel(Model model);

    /**
     * 删除模型
     * @param id
     */
    void deleteModel(String id);

    /**
     * 获取模型列表
     * @param name
     * @param page
     * @param size
     * @param createTimeStart
     * @param createTimeEnd
     * @param orderColumn
     * @param desc
     * @return
     */
    public List<Model> getModelList(String name, Date createTimeStart, Date createTimeEnd, String orderColumn, Boolean desc, RowBounds rowBounds);

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
     * 修改模型
     * @param model
     */
    void updateModel(Model model);

    /**
     * 通过名称获取模型
     * @param name
     * @return
     */
    Model getModelByName(String name);
}
