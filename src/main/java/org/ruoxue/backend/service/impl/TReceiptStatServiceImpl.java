package org.ruoxue.backend.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TReceiptStat;
import org.ruoxue.backend.mapper.TReceiptStatMapper;
import org.ruoxue.backend.service.ITReceiptStatService;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Service
public class TReceiptStatServiceImpl extends ServiceImpl<TReceiptStatMapper, TReceiptStat> implements ITReceiptStatService {

    @Resource
    private TReceiptStatMapper receiptStatMapper;

    @Override
    public Object receiptStat(String uid) {
        if (ToolUtil.isEmpty(uid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        Integer userid = XunBinKit.getUidByString(uid);

        List<TReceiptStat> list = receiptStatMapper.listReceiptStat(userid);

        return XunBinKit.returnResult(list.size() > 0, -2, list, "查询成功", "查询失败");
    }
}
