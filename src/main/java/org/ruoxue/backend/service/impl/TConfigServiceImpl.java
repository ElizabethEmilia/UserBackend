package org.ruoxue.backend.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TConfig;
import org.ruoxue.backend.mapper.TConfigMapper;
import org.ruoxue.backend.service.ITConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Service
public class TConfigServiceImpl extends ServiceImpl<TConfigMapper, TConfig> implements ITConfigService {

    @Resource
    private TConfigMapper configMapper;

    @Override
    public List<TConfig> getTConfig() {
        return configMapper.getConfigs();
    }
}
