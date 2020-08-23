package cn.com.chinahitech.hotback.infoCount.service;

import cn.com.chinahitech.hotback.infoCount.entity.InfoCount;
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
public interface IInfoCountService extends IService<InfoCount> {

    List<InfoCount> getInfoCountByDate(String date);

    List<InfoCount> getInfoCount();
}
