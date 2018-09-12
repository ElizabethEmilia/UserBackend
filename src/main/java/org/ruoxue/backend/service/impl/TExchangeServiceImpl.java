package org.ruoxue.backend.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TExchange;
import org.ruoxue.backend.bean.TPublicCharge;
import org.ruoxue.backend.common.constant.Constant;
import org.ruoxue.backend.mapper.TExchangeMapper;
import org.ruoxue.backend.service.ITExchangeService;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class TExchangeServiceImpl extends ServiceImpl<TExchangeMapper, TExchange> implements ITExchangeService {

    @Resource
    private TExchangeMapper exchangeMapper;

    @Override
    public Object onlinecharge(Integer uid, Integer page, Integer size) {

        if (ToolUtil.isEmpty(uid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<TExchange> list = exchangeMapper.getOnlinecharge(uid, page, size);

        return XunBinKit.returnResult(list.size() > 0, -2, list, "查询成功", "查询失败");
    }

    @Override
    public Object publicchargeStatus(Integer uid, Integer page, Integer size, String status) {
        if (ToolUtil.isEmpty(uid) || ToolUtil.isEmpty(status)) {
            return ResultUtil.error(-1, "参数错误");
        }

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("all", null);
        map.put("pending", Constant.PublicChargeState.WAITING);
        map.put("confirmed", Constant.PublicChargeState.CONFIRMED);
        map.put("cancelled", Constant.PublicChargeState.CANCELED);

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

        List<TPublicCharge> list = exchangeMapper.getPublicCharge(uid, page, size, map.get(status));

        return XunBinKit.returnResult(list.size() > 0, -3, list, "查询成功", "查询失败");
    }
}
