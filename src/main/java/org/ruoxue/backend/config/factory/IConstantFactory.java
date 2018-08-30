package org.ruoxue.backend.config.factory;

import org.ruoxue.backend.bean.TUser;

/**
 * 常量生产工厂的接口
 *
 * @author fengjb
 * @date 2018-08-29
 */
public interface IConstantFactory {

    /**
     * 根据用户id获取用户bean
     */
    TUser getTUserById(Integer userId);



}
