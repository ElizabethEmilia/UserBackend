package org.ruoxue.backend.service.impl;

import org.json.JSONArray;
import org.ruoxue.backend.bean.TArea;
import org.ruoxue.backend.mapper.TAreaMapper;
import org.ruoxue.backend.service.ITAreaService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fengjb
 * @since 2018-09-08
 */
@Service
public class TAreaServiceImpl extends ServiceImpl<TAreaMapper, TArea> implements ITAreaService {

    @Resource
    TAreaMapper mapper;

    public Object getProvince() {
        return ResultUtil.success((mapper.getProvince()));
    }

    public Object getCity(String province) {
        return ResultUtil.success((mapper.getCity(province)));
    }

    public Object getDistrict(String province, String city) {
        return ResultUtil.success((mapper.getDistrict(province, city)));
    }

}
