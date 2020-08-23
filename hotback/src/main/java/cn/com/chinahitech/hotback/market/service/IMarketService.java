package cn.com.chinahitech.hotback.market.service;

import cn.com.chinahitech.hotback.market.entity.Market;
import com.baomidou.mybatisplus.extension.service.IService;


import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhuminghang
 * @since 2020-07-31
 */
public interface IMarketService extends IService<Market> {

    List<Market> getRegions();

    List<Market> getMarketsByRegion(String region);
}
