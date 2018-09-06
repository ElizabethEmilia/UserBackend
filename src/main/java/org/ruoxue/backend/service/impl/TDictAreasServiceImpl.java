package org.ruoxue.backend.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TDictAreas;
import org.ruoxue.backend.mapper.TDictAreasMapper;
import org.ruoxue.backend.mapper.TDictCitiesMapper;
import org.ruoxue.backend.service.ITDictAreasService;
import org.ruoxue.backend.util.ResultUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fengjb
 * @since 2018-09-04
 */
@Service
public class TDictAreasServiceImpl extends ServiceImpl<TDictAreasMapper, TDictAreas> implements ITDictAreasService {

    @Resource
    private TDictAreasMapper dictAreasMapper;

    @Resource
    private TDictCitiesMapper dictCitiesMapper;

    @Override
    public Object getDistrictByCityId(String city) {
//        获取城市的id
        Integer cityId = dictCitiesMapper.getCityCodeByName(city);
//        获取市下所有的区
        List<String> list = dictAreasMapper.getDistrictByCityId(cityId);
        return ResultUtil.success(list);
    }
}
