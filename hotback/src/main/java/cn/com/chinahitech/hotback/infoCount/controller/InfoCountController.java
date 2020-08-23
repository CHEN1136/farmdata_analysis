package cn.com.chinahitech.hotback.infoCount.controller;


import cn.com.chinahitech.hotback.infoCount.entity.InfoCount;
import cn.com.chinahitech.hotback.infoCount.service.IInfoCountService;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@RequestMapping("/infoCount")
@Api(value = "数据监控操作接口",tags = {"数据监控操作接口说明"})
@CrossOrigin
public class InfoCountController {

    @Autowired
    private IInfoCountService infoCountService;

    @GetMapping("/getInfoCount/{date}")
    @ApiOperation(value ="获取数据监控信息", notes = "检索info_count")
    @ApiImplicitParam(name="date",value = "日期",dataType = "String",paramType = "path",example = "2020-07-20")
    public String getInfoCountByDate(@PathVariable("date")String date){
        Map<String,Object> map = new HashMap<>();
        try{
            List<InfoCount> counts = infoCountService.getInfoCountByDate(date);
            map.put("status","200");
            map.put("data",counts);
        }catch (Exception e){
            map.put("status","500");
            map.put("ErrorMsg",e.getMessage());
        }
        return JSON.toJSONString(map);
    }

    @GetMapping("/getInfoCount/")
    @ApiOperation(value ="获取所有数据监控信息", notes = "检索info_count")
    public String getInfoCount( ){
        Map<String,Object> map = new HashMap<>();
        try{
            List<InfoCount> counts = infoCountService.getInfoCount();
            map.put("status","200");
            map.put("data",counts);
        }catch (Exception e){
            map.put("status","500");
            map.put("ErrorMsg",e.getMessage());
        }
        return JSON.toJSONString(map);
    }
}
