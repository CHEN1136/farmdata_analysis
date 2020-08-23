package cn.com.chinahitech.hotback.price.service;

import cn.com.chinahitech.hotback.price.entity.Price;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
public interface IPriceService extends IService<Price> {

    IPage<Price> getPriceByMarketAndVariety(String market, String variety, int pageNum);

    List<Price> getAvgPriceByRegion(String vegetable);

    List<Price> getPriceByMarketAndVariety(String market, String variety);

    int getCount(String market, String variety, String region, String category);
}
