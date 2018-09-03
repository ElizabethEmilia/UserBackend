package org.ruoxue.backend.service.impl;

import org.ruoxue.backend.bean.TSignin;
import org.ruoxue.backend.mapper.TSigninMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.service.ITSigninService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Service
public class TSigninServiceImpl extends ServiceImpl<TSigninMapper, TSignin> implements ITSigninService {

    @Resource
    private TSigninMapper signinMapper;

    @Override
    public TSignin getSigninByUid(Integer id) {
        return signinMapper.getSigninByUid(id);
    }
}
