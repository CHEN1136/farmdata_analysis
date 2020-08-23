package cn.com.chinahitech.hotback.infoCount.service.impl;

import cn.com.chinahitech.hotback.infoCount.entity.InfoCount;
import cn.com.chinahitech.hotback.infoCount.mapper.InfoCountMapper;
import cn.com.chinahitech.hotback.infoCount.service.IInfoCountService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.sampled.Line;
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
public class InfoCountServiceImpl extends ServiceImpl<InfoCountMapper, InfoCount> implements IInfoCountService {

    @Autowired
    InfoCountMapper infoCountMapper;

    @Override
    public List<InfoCount> getInfoCountByDate(String date) {
        QueryWrapper<InfoCount> wrapper = new QueryWrapper<>();
        wrapper.like("date",date);
        return infoCountMapper.selectList(wrapper);
    }

    @Override
    public List<InfoCount> getInfoCount() {
        QueryWrapper<InfoCount> wrapper = new QueryWrapper<>();
        return  infoCountMapper.selectList(wrapper);
    }
}
