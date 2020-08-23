package cn.com.chinahitech.hotback.price.controller;


import cn.com.chinahitech.hotback.price.entity.Price;
import cn.com.chinahitech.hotback.price.service.IPriceService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
@RequestMapping("/price")
@Api(value = "价格查询接口",tags = {"价格查询接口操作说明"})
@CrossOrigin
public class PriceController {
    @Autowired
    IPriceService priceService;

    @GetMapping("/getPrice")
    @ApiOperation(value = "根据市场和品种返回查询结果",notes = "检索price")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "market", value = "市场名", dataType = "Sting", paramType = "query", example = "北京朝阳区大洋路综合市场"),
            @ApiImplicitParam(name = "variety", value = "品种名", dataType = "String", paramType = "query", example = "胡萝卜"),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", paramType = "query", required = false,example = "-1")
    })
    public String getPriceByMarketAndVariety(@RequestParam("market")String market,
                                             @RequestParam("variety") String variety,
                                             @RequestParam(value = "pageNum",required = false,defaultValue = "0")int pageNum){
        Map<String,Object> map = new HashMap<>();
        try{
            Object prices = null;
            if (pageNum != -1){
                prices = (IPage<Price>)priceService.getPriceByMarketAndVariety(market,variety,pageNum);
            }else{
                prices = (List<Price>)priceService.getPriceByMarketAndVariety(market,variety);
            }
            map.put("status","200");
            map.put("data",prices);
        }catch (Exception e){
            map.put("status","500");
            map.put("ErrorMsg",e.getMessage());
        }
        return JSON.toJSONString(map);
    }

    @GetMapping("/getAvgPrice/{vegetable}")
    @ApiOperation(value = "根据地区返回平均价格",notes = "检索price")
    @ApiImplicitParam(name = "vegetable",value = "品种名",dataType = "String",paramType = "path",example = "胡萝卜")
    public String getAvgPriceByRegion(@PathVariable(name = "vegetable")String vegetable){
        Map<String,Object> map = new HashMap<>();
        try{
            List<Price> prices = priceService.getAvgPriceByRegion(vegetable);
            map.put("status","200");
            map.put("data",prices);
        }catch (Exception e){
            map.put("status","500");
            map.put("ErrorMsg",e.getMessage());
        }
        return JSON.toJSONString(map);
    }

    @GetMapping("/getCount")
    @ApiOperation(value = "根据市场、品种、品类、地区返回记录数量",notes = "检索price")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "market", value = "市场名", dataType = "Sting", paramType = "query", example = "北京朝阳区大洋路综合市场"),
            @ApiImplicitParam(name = "variety", value = "品种名", dataType = "String", paramType = "query", example = "胡萝卜"),
            @ApiImplicitParam(name = "region", value = "地区", dataType = "String", paramType = "query",example = "北京市"),
            @ApiImplicitParam(name = "category",value = "品类",dataType = "String",paramType = "query",example = "根菜类")
    })
    public String getNumByMarketAndVarietyAndRegionAndCategory(@RequestParam(value = "market",defaultValue = "")String market,
                                             @RequestParam(value = "variety",defaultValue = "") String variety,
                                             @RequestParam(value = "region",defaultValue = "")String region,
                                             @RequestParam(value = "category",defaultValue = "")String category){
        Map<String,Object> map = new HashMap<>();
        try{
            int count = priceService.getCount(market,variety,region,category);
            map.put("status","200");
            map.put("data",count);
        }catch (Exception e){
            map.put("status","500");
            map.put("ErrorMsg",e.getMessage());
        }
        return JSON.toJSONString(map);
    }
}
