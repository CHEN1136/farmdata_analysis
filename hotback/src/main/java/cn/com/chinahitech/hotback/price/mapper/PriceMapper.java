package cn.com.chinahitech.hotback.price.mapper;

import cn.com.chinahitech.hotback.price.entity.Price;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhuminghang
 * @since 2020-07-31
 */
@Repository
public interface PriceMapper extends BaseMapper<Price> {
    @Select("select region,avg(avg_price) as avg_price,avg(highest_price) as highest_price,avg(lowest_price)as lowest_price from price where variety_name = #{vegetable} group by region")
    List<Price> findAvgPriceByRegion(@Param("vegetable")String vagetable);

    @Select("select count(*) from price where market_name like concat('%',#{market},'%') and variety_name like concat('%',#{variety},'%')")
    int countByMarketAndVariety(@Param("market") String market,@Param("variety")String variety);

    @Select("select count(*) from price where market_name like concat('%',#{market},'%') and variety_name in (select variety_name from variety where category_name like concat('%',#{category},'%'))")
    int countByMarketAndCategory(@Param("market")String market,@Param("category")String category);

    @Select("select count(*) from price where region like concat('%',#{region},'%') and variety_name in (select variety_name from variety where category_name like concat('%',#{category},'%'))")
    int countByRegionAndCategory(@Param("region")String region,@Param("category")String category);

    @Select("select count(*) from price where region like concat('%',#{region},'%') and variety_name like concat('%',#{variety},'%')")
    int countByRegionAndVariety(String region,String variety);
}
