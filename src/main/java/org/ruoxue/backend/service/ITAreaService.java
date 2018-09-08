package org.ruoxue.backend.service;

import org.ruoxue.backend.bean.TArea;
import com.baomidou.mybatisplus.service.IService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-09-08
 */
@Service
public interface ITAreaService extends IService<TArea> {
    Object getProvince();
    Object getCity(String province);
    Object getDistrict(String province, String city);
}
