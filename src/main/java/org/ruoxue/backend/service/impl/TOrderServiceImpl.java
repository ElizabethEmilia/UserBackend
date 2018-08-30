package org.ruoxue.backend.service.impl;

import org.ruoxue.backend.bean.TOrder;
import org.ruoxue.backend.mapper.TOrderMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.service.ITOrderService;
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
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements ITOrderService {
	
}
