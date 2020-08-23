package cn.com.chinahitech.hotback.variety.controller;


import cn.com.chinahitech.hotback.variety.entity.Variety;
import cn.com.chinahitech.hotback.variety.service.IVarietyService;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhuminghang
 * @since 2020-07-31
 */
@RestController
@RequestMapping("/variety")
@Api(value = "品种信息操作接口",tags = {"品种信息操作接口说明"})
@CrossOrigin
public class VarietyController {

    @Autowired
    private IVarietyService varietyService;

    @GetMapping("/getCategories")
    @ApiOperation(value = "返回所有品类信息",notes = "检索variety")
    public String getCategories(){
        Map<String,Object> map = new HashMap<>();
        try{
            List<Variety> catagories = varietyService.getCategories();
            map.put("status","200");
            map.put("data",catagories);
        }catch (Exception e){
            map.put("status","500");
            map.put("ErrorMsg",e.getMessage());
        }
        return JSON.toJSONString(map);
    }

    @GetMapping("/getVarieties/{category}")
    @ApiOperation(value = "根据品类返回种类信息",notes = "检索variety")
    @ApiImplicitParam(name = "category", value = "品类", dataType = "String", paramType = "path",example = "瓜类")
    public String getVarietiesByCategory(@PathVariable("category")String catagory){
        Map<String,Object> map = new HashMap<>();
        try{
            List<Variety> varieties = varietyService.getVarietiesByCategory(catagory);
            map.put("status","200");
            map.put("data",varieties);
        }catch (Exception e){
            map.put("status","500");
            map.put("ErrorMsg",e.getMessage());
        }
        return JSON.toJSONString(map);
    }
}
