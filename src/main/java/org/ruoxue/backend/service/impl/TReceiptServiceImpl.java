package org.ruoxue.backend.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TReceipt;
import org.ruoxue.backend.mapper.TReceiptMapper;
import org.ruoxue.backend.service.ITReceiptService;
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
public class TReceiptServiceImpl extends ServiceImpl<TReceiptMapper, TReceipt> implements ITReceiptService {

    @Resource
    private TReceiptMapper receiptMapper;

    @Override
    public Object listReceipt(String uid, Integer cid, Integer page, Integer size, Integer type, Integer status, Date start, Date end) {
//        非空验证
        XunBinKit.isEmpty(uid);

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

//        查询list
        List<Map<String, Object>> list = receiptMapper.listReceipt(uid, cid, page, size, type, status, start, end);

        return XunBinKit.returnResult(list.size() > 0, -2, list, "查询成功", "列表为空");
    }

    @Override
    public Object receiptRequest(String uid, Integer rid, String action) {

//        非空验证
        XunBinKit.isEmpty(uid, rid, action);

        Integer userid = XunBinKit.getUidByString(uid);

        TReceipt receipt = receiptMapper.getReceipt(userid, rid);

        if(ToolUtil.isEmpty(receipt)){
            return ResultUtil.error(-2, "开票记录不存在");
        }

//        获取对应的状态
        Integer statusCode = getCodeByAction(action);

        receipt.setStatus(statusCode);
        boolean b = receipt.updateById();

        return XunBinKit.returnResult(b, -3, null, "修改成功", "修改失败");
    }

    private Integer getCodeByAction(String action) {
//        做一个HashMap,存储路径与值得映射
        Map<String, Integer> map = new HashMap<>();
        map.put("submit", 0);
        map.put("refuse-submit", 1);
        map.put("accept", 2);
        map.put("distrib-dist", 3);
        map.put("distrib-self", 4);
        map.put("receipt", 5);
        map.put("discard", 6);
        map.put("pack", 7);
        map.put("send", 8);
        map.put("selfrecv", 9);
        map.put("refuse-packing", 10);
        map.put("recv", 11);
        System.out.println("-------------map: " + map);

        return map.get(action);

    }

}
