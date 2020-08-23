package cn.com.chinahitech.hotback.price.service.impl;

import cn.com.chinahitech.hotback.price.entity.Price;
import cn.com.chinahitech.hotback.price.mapper.PriceMapper;
import cn.com.chinahitech.hotback.price.service.IPriceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhuminghang
 * @since 2020-07-31
 */
@Service
public class PriceServiceImpl extends ServiceImpl<PriceMapper, Price> implements IPriceService {

    @Autowired
    PriceMapper priceMapper;

    @Value("${pageSize}")
    private int pageSize;

    @Override
    public IPage<Price> getPriceByMarketAndVariety(String market, String variety, int pageNum) {
        QueryWrapper<Price> wrapper = new QueryWrapper<>();
        wrapper.like("market_name",market).like("variety_name",variety).orderByAsc("date");
        Page<Price> pageConf = new Page(pageNum,pageSize);
        IPage<Price> result =  priceMapper.selectPage(pageConf,wrapper);
        return result;
    }

    @Override
    public List<Price> getAvgPriceByRegion(String vagetable) {
        return priceMapper.findAvgPriceByRegion(vagetable);
    }

    @Override
    public List<Price> getPriceByMarketAndVariety(String market, String variety) {
        QueryWrapper<Price> wrapper = new QueryWrapper<>();
        wrapper.like("market_name",market).like("variety_name",variety).orderByAsc("date");
        List<Price> result =  priceMapper.selectList(wrapper);
        return result;
    }

    @Override
    public int getCount(String market, String variety, String region, String category) {
        if(!market.equals("")){
            if(!variety.equals("")){
                return priceMapper.countByMarketAndVariety(market,variety);
            }
            return priceMapper.countByMarketAndCategory(market,category);
        }
        if(variety.equals("")){
            return priceMapper.countByRegionAndCategory(region, category);
        }
        return priceMapper.countByRegionAndVariety(region,variety);
    }
}
