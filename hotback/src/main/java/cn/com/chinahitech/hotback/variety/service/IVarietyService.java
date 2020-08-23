package cn.com.chinahitech.hotback.variety.service;

import cn.com.chinahitech.hotback.variety.entity.Variety;
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
public interface IVarietyService extends IService<Variety> {

    List<Variety> getCategories();

    List<Variety> getVarietiesByCategory(String catagory);
}
