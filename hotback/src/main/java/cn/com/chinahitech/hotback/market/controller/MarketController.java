package cn.com.chinahitech.hotback.market.controller;


import cn.com.chinahitech.hotback.market.entity.Market;
import cn.com.chinahitech.hotback.market.service.IMarketService;
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
@RequestMapping("/market")
@Api(value = "市场信息查询接口" , tags = {"市场信息查询接口操作说明"})
@CrossOrigin
public class MarketController {
    @Autowired
    private IMarketService marketService;

    @GetMapping(value = "/getRegions")
    @ApiOperation(value = "获取所有省份", notes = "检索market")
    public String getRegions(){
        Map<String,Object> map = new HashMap<>();
        try{
            List<Market> regions = marketService.getRegions();
            map.put("status","200");
            map.put("data",regions);
        }catch (Exception e){
            map.put("status","500");
            map.put("ErrorMsg","检索出错："+e.getMessage());
        }
        String jsonStr = JSON.toJSONString(map);
        return jsonStr;
    }

    @GetMapping(value = "/getMarkets/{region}")
    @ApiOperation(value = "根据省份筛选市场", notes = "检索market")
    @ApiImplicitParam(name="region",value = "省份",dataType = "String",paramType = "path",example = "北京")
    public String getMarketsByRegions(@PathVariable(value = "region")String region){
        Map<String,Object> map = new HashMap<>();
        try{
            List<Market> markets = marketService.getMarketsByRegion(region);
            map.put("status","200");
            map.put("data",markets);
        }catch (Exception e){
            map.put("status","500");
            map.put("ErrorMsg","检索出错："+e.getMessage());
        }
        String jsonStr = JSON.toJSONString(map);
        return jsonStr;
    }
}
