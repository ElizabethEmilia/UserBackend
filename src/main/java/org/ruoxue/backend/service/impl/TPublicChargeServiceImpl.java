package org.ruoxue.backend.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TPublicCharge;
import org.ruoxue.backend.common.constant.Constant;
import org.ruoxue.backend.mapper.TPublicChargeMapper;
import org.ruoxue.backend.service.ITPublicChargeService;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
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
public class TPublicChargeServiceImpl extends ServiceImpl<TPublicChargeMapper, TPublicCharge> implements ITPublicChargeService {

    @Resource
    private TPublicChargeMapper publicChargeMapper;

    @Override
    public Object updatePublicchargeStatus(Integer uid, Integer pid, String status) {

        if (ToolUtil.isEmpty(uid) || ToolUtil.isEmpty(pid) || ToolUtil.isEmpty(status)) {
            return ResultUtil.error(-1, "参数错误");
        }

        Map<String, Integer> map = new HashMap<>();
        map.put("confirm", Constant.PublicChargeState.CONFIRMED);
        map.put("cancel", Constant.PublicChargeState.CANCELED);

        if(!map.containsKey(status)){
            XunBinKit.returnCode(404, "Not Found");
            return null;
        }

        Integer len = publicChargeMapper.updatePublicChangeStatus(pid, map.get(status));

        return XunBinKit.returnResult(len > 0, -2, null,"修改成功", "修改失败");
    }
}
