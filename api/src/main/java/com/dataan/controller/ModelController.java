package com.dataan.controller;

import com.dataan.annotation.SqlArgument;
import com.dataan.entity.Model;
import com.dataan.service.ModelService;
import com.dataan.utils.MyBeanUtils;
import com.dataan.utils.Response;
import com.dataan.vo.ModelDetailVo;
import com.dataan.vo.ModelRequestVo;
import com.dataan.vo.ModelVo;
import com.dataan.vo.Pageable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhan bing liang
 * @date 2024/6/26 15:08
 */
@RestController
@RequestMapping("/model")
public class ModelController {


    @Autowired
    private ModelService modelService;
    void f() {
        g(k());
    }
    void g(int x) {

    }

    int  k() {
        return 1;
    }
    @PostMapping
    public ResponseEntity<ModelVo> createModel(@RequestBody @Valid ModelRequestVo modelRequestVo) {
        Model model = MyBeanUtils.copyProperties(modelRequestVo, Model::new);
        Model res = modelService.createModel(model);
        return Response.created(MyBeanUtils.copyProperties(res, ModelVo::new));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateModel(@PathVariable String id,@RequestBody @Valid ModelRequestVo modelRequestVo) {
        Model model = MyBeanUtils.copyProperties(modelRequestVo, Model::new);
        model.setId(id);
        modelService.updateModel(model);
        return Response.noContent();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModel(@PathVariable String id) {
        modelService.deleteModel(id);
        return Response.noContent();
    }
    @GetMapping("/all")
    public ResponseEntity<Pageable<ModelVo>> getModelList(@RequestParam(required = false) String name
        , @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date createTimeStart
        , @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date createTimeEnd
        , @RequestParam(required = false, defaultValue = "1") Integer page
        , @RequestParam(required = false, defaultValue = "10") Integer size
        , @SqlArgument @RequestParam(required = false,defaultValue = "create_time")  String orderColumn
        , @RequestParam(required = false,defaultValue = "true")Boolean desc) {
        List<Model> res = modelService.getModelList(name,page,size,createTimeStart,createTimeEnd,orderColumn,desc);
        return Response.ok(new Pageable<>(MyBeanUtils.copyListProperties(res, ModelVo::new),modelService.getTotalCount(),page,size ));
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<Model> getModelById(@PathVariable String id) {
        Model res = modelService.getModelById(id);
        ModelDetailVo modelDetailVo = MyBeanUtils.copyProperties(res, ModelDetailVo::new);
        modelDetailVo.setFields(modelDetailVo.getFields().stream().filter(s->!"id".equals(s.getName())).collect(Collectors.toList()));
        return Response.ok(res);
    }

}
