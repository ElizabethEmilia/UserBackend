package org.ruoxue.backend.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TDictCities;
import org.ruoxue.backend.mapper.TDictCitiesMapper;
import org.ruoxue.backend.mapper.TDictProvincesMapper;
import org.ruoxue.backend.service.ITDictCitiesService;
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
public class TDictCitiesServiceImpl extends ServiceImpl<TDictCitiesMapper, TDictCities> implements ITDictCitiesService {

    @Resource
    private TDictCitiesMapper dictCitiesMapper;

    @Resource
    private TDictProvincesMapper dictProvincesMapper;

    @Override
    public Object getCityByProvince(String province) {
//        获取省份id
        Integer provinceCode = dictProvincesMapper.getProcinceCodeByName(province);
//        获取省份下所有的市
        List<String> list = dictCitiesMapper.getCityByProvinceId(provinceCode);
        return ResultUtil.success(list);
    }
}
