package org.ruoxue.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TOrder;
import org.ruoxue.backend.common.constant.Constant;
import org.ruoxue.backend.mapper.TLogsMapper;
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

    @Resource
    private TLogsMapper logsMapper;

    @Override
    public Object ordersStatus(Integer uid, Integer page, Integer size, String status, Integer count) {

        if (ToolUtil.isEmpty(uid) || ToolUtil.isEmpty(status)) {
            return ResultUtil.error(-1, "参数错误");
        }

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("all", null);
        map.put("pending", Constant.PaymentStatus.UNPAIED);
        map.put("paid", Constant.PaymentStatus.PAIED);
        map.put("cancelled", Constant.PaymentStatus.CANCELED);

        if(!map.containsKey(status)){
            XunBinKit.returnCode(404, "Not Found");
            return null;
        }

        if (ToolUtil.isNotEmpty(count)) {
            return ResultUtil.success(orderMapper.countListOrderByStatus(uid, map.get(status)));
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<TOrder> list = orderMapper.listOrderByStatus(uid, page, size, map.get(status));

        return ResultUtil.success(list);
    }

    @Override
    public Object listOrder(Integer cid, Integer type, Integer page, Integer size, String status, Date start, Date end, Integer count) {

        if (ToolUtil.isEmpty(status)) {
            return ResultUtil.error(-1, "参数错误");
        }

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("all", null);
        map.put("pending", Constant.PaymentStatus.UNPAIED);
        map.put("paid", Constant.PaymentStatus.PAIED);
        map.put("cancelled", Constant.PaymentStatus.CANCELED);

        if(!map.containsKey(status)){
            XunBinKit.returnCode(404, "Not Found");
            return null;
        }

        if (ToolUtil.isNotEmpty(count)) {
            return ResultUtil.success(orderMapper.countListOrder(cid, type, map.get(status), start, end, XunBinKit.getUid()));
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<Map<String, Object>> list = orderMapper.listOrder(cid, type, page, size, map.get(status), start, end, XunBinKit.getUid());

        return ResultUtil.success(list);
    }

    @Override
    public Object addCustomerOrder(Integer cid, JSONObject jsonObject) {

        if (ToolUtil.isEmpty(cid)) {
            return ResultUtil.error(-1, "参数错误");
        }

//        获取参数
        String type = jsonObject.getString("type");
        String name = jsonObject.getString("note");
        Double amount = jsonObject.getDouble("amount");

        /// TODO: 增加订单名称

        TOrder order = new TOrder();
        order.setTmCreate(new Date());
        order.setCid(cid);
        order.setAmount(amount);
        order.setRunning("");
        order.setNote(name);
        order.setTmPaid(new Date());
        order.setType(type);
        order.setStatus(Constant.PaymentStatus.PAIED);
        order.insert();

        return ResultUtil.success();
    }
}
