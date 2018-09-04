package org.ruoxue.backend.service;

import org.ruoxue.backend.bean.TConfig;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
public interface ITConfigService extends IService<TConfig> {

//    获取系统设置列表
    List<TConfig> getTConfig();

    Object setting();

    Object settingKey(String key);

    Object updateSettingKey(String key, String value);

    Object clearCache();

}
