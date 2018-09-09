package org.ruoxue.backend.service;

import org.ruoxue.backend.bean.TConfig;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

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
    List<Map<String, Object>> getTConfig();

    Object setting();

    Object settingKey(String key);

    Object updateSettingKey(String key, String value);

    Object clearCache();

    List<Map<String, Object>> getKeyAndValue();

}
