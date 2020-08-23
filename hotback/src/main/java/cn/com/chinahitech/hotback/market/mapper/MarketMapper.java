package cn.com.chinahitech.hotback.market.mapper;

import cn.com.chinahitech.hotback.market.entity.Market;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.sql.Wrapper;
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
public interface MarketMapper extends BaseMapper<Market> {

}
