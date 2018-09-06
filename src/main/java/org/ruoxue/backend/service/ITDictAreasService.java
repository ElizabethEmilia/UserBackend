package org.ruoxue.backend.service;

import org.ruoxue.backend.bean.TDictAreas;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-09-04
 */
public interface ITDictAreasService extends IService<TDictAreas> {

    Object getDistrictByCityId(String city);

}
