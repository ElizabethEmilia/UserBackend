package org.ruoxue.backend.service.impl;

import org.ruoxue.backend.bean.TCustomer;
import org.ruoxue.backend.mapper.TCustomerMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.service.ITCustomerService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Service
public class TCustomerServiceImpl extends ServiceImpl<TCustomerMapper, TCustomer> implements ITCustomerService {
	
}
