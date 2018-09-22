package org.ruoxue.backend.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TAdmin;
import org.ruoxue.backend.bean.TPending;
import org.ruoxue.backend.mapper.TPendingMapper;
import org.ruoxue.backend.service.ITPendingService;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fengjb
 * @since 2018-09-22
 */
@Service
public class TPendingServiceImpl extends ServiceImpl<TPendingMapper, TPending> implements ITPendingService {

    @Resource
    private TPendingMapper pendingMapper;

    @Override
    public Object getNotificationByUid() {

        Integer uid = XunBinKit.getUid();

        if (ToolUtil.isEmpty(uid)) {
            return ResultUtil.error(-1, "用户未登录");
        }

        List<Map<String, Object>> list = pendingMapper.getNotificationByUid(uid, 0, 0);

        return ResultUtil.success(list);
    }

    @Override
    public Object getNotification() {

        List<Map<String, Object>> list = pendingMapper.getNotification(1, 0);

        return ResultUtil.success(list);
    }

    @Override
    public Object getNotificationByGid() {

        TAdmin admin = (TAdmin) XunBinKit.getSession().getAttribute("obj");

        if (ToolUtil.isEmpty(admin)) {
            return ResultUtil.error(-1, "该管理员未登录");
        }

        Integer gid = admin.getGid();

        if (ToolUtil.isEmpty(gid)) {
            return ResultUtil.error(-2, "角色权限错误");
        }

        List<Map<String, Object>> list = pendingMapper.getNotificationByGid(gid, 1, 0);

        return ResultUtil.success(list);
    }

    @Override
    public Object getNotificationByAid() {

        Integer uid = XunBinKit.getUid();

        if (ToolUtil.isEmpty(uid)) {
            return ResultUtil.error(-1, "用户未登录");
        }

        List<Map<String, Object>> list = pendingMapper.getNotificationByAid(uid, 0, 0);

        return ResultUtil.success(list);
    }

    @Override
    public Object removeNoto(Integer nid) {

        if (ToolUtil.isEmpty(nid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        Integer len = pendingMapper.removeNoto(nid);

        return XunBinKit.returnResult(len > 0, -2, null, "Success", "Error" );
    }
}
