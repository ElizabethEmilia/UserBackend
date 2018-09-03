package org.ruoxue.backend.service;

import org.ruoxue.backend.bean.TSignin;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
public interface ITSigninService extends IService<TSignin> {

    TSignin getSigninByUid(Integer id);
	
}
