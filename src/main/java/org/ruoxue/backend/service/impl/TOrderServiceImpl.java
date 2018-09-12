package org.ruoxue.backend.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TOrder;
import org.ruoxue.backend.common.constant.Constant;
import org.ruoxue.backend.mapper.TOrderMapper;
import org.ruoxue.backend.service.ITOrderService;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Resource
    private TOrderMapper orderMapper;

    @Override
    public Object ordersStatus(Integer uid, Integer page, Integer size, String status) {

        if (ToolUtil.isEmpty(uid) || ToolUtil.isEmpty(status)) {
            return ResultUtil.error(-1, "参数错误");
        }

        Map<String, Integer> map = new HashMap<>();
        map.put("all", null);
        map.put("pending", Constant.PaymentStatus.UNPAIED);
        map.put("paid", Constant.PaymentStatus.PAIED);
        map.put("cancelled", Constant.PaymentStatus.CANCELED);

        if(!map.containsKey(status)){
            XunBinKit.returnCode(404, "Not Found");
            return null;
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<TOrder> list = orderMapper.listOrderByStatus(uid, page, size, map.get(status));

        return XunBinKit.returnResult(list.size() > 0, -2, list, "查询成功", "查询失败");
    }

    @Override
    public Object listOrder(Integer cid, Integer type, Integer page, Integer size, String status, Date start, Date end) {
        if (ToolUtil.isEmpty(status)) {
            return ResultUtil.error(-1, "参数错误");
        }

        Map<String, Integer> map = new HashMap<>();
        map.put("all", null);
        map.put("pending", Constant.PaymentStatus.UNPAIED);
        map.put("paid", Constant.PaymentStatus.PAIED);
        map.put("cancelled", Constant.PaymentStatus.CANCELED);

        if(!map.containsKey(status)){
            XunBinKit.returnCode(404, "Not Found");
            return null;
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<Map<String, Object>> list = orderMapper.listOrder(cid, type, page, size, map.get(status), start, end);

        return XunBinKit.returnResult(list.size() > 0, -2, list, "查询成功", "查询失败");
    }
}
