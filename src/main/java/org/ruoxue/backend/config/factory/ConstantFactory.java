package org.ruoxue.backend.config.factory;

import org.ruoxue.backend.bean.TUser;
import org.ruoxue.backend.config.SpringContextHolder;
import org.ruoxue.backend.mapper.TUserMapper;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * 常量的生产工厂
 *
 * @author fengshuonan
 * @date 2017年2月13日 下午10:55:21
 */
@Component
@DependsOn("springContextHolder")
public class ConstantFactory implements IConstantFactory {

    private TUserMapper tUserMapper = SpringContextHolder.getBean(TUserMapper.class);

    public static IConstantFactory me() {
        return SpringContextHolder.getBean("constantFactory");
    }

    /**
     * 根据用户id获取用户名称
     *
     * @author stylefeng
     * @Date 2017/5/9 23:41
     */

    @Override
    public TUser getTUserById(Integer userId) {
        TUser user = tUserMapper.selectById(userId);
        if (user != null) {
            return user;
        } else {
            return null;
        }
    }
}
