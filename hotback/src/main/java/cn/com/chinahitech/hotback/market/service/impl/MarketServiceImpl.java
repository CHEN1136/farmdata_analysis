package cn.com.chinahitech.hotback.market.service.impl;

import cn.com.chinahitech.hotback.market.entity.Market;
import cn.com.chinahitech.hotback.market.mapper.MarketMapper;
import cn.com.chinahitech.hotback.market.service.IMarketService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
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
public class MarketServiceImpl extends ServiceImpl<MarketMapper, Market> implements IMarketService {

    @Autowired
    MarketMapper marketMapper;

    @Override
    public List<Market> getRegions() {
        QueryWrapper<Market> wrapper = new QueryWrapper<>();
        wrapper.select("distinct market_region");
        List<Market> regions = marketMapper.selectList(wrapper);
        return regions;
    }

    @Override
    public List<Market> getMarketsByRegion(String region) {
        QueryWrapper<Market> wrapper = new QueryWrapper<>();
        wrapper.like("market_region",region);
        List<Market> markets = marketMapper.selectList(wrapper);
        return markets;
    }
}
