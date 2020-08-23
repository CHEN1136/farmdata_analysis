package cn.com.chinahitech.hotback.variety.service.impl;

import cn.com.chinahitech.hotback.variety.entity.Variety;
import cn.com.chinahitech.hotback.variety.mapper.VarietyMapper;
import cn.com.chinahitech.hotback.variety.service.IVarietyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class VarityServiceImpl extends ServiceImpl<VarietyMapper, Variety> implements IVarietyService {

    @Autowired
    VarietyMapper varietyMapper;

    @Override
    public List<Variety> getCategories() {
        QueryWrapper<Variety> wrapper = new QueryWrapper<Variety>();
        wrapper.select("distinct category_name").orderByDesc("category_name");
        return varietyMapper.selectList(wrapper);
    }

    @Override
    public List<Variety> getVarietiesByCategory(String category) {
        QueryWrapper<Variety> wrapper = new QueryWrapper<Variety>();
        wrapper.select("variety_name")
                .like("category_name",category);
        return varietyMapper.selectList(wrapper);
    }
}
