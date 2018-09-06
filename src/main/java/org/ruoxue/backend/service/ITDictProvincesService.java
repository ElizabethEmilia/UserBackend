package org.ruoxue.backend.service;

import com.baomidou.mybatisplus.service.IService;
import org.ruoxue.backend.bean.TDictProvinces;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-09-04
 */
public interface ITDictProvincesService extends IService<TDictProvinces> {

    Object getProvince();

    Object jsonToCVS(String json, String filename);
	
}
