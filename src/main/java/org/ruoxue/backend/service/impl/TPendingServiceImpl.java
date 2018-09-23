package org.ruoxue.backend.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TAdmin;
import org.ruoxue.backend.bean.TCustomer;
import org.ruoxue.backend.bean.TPending;
import org.ruoxue.backend.mapper.TAdminMapper;
import org.ruoxue.backend.mapper.TCustomerMapper;
import org.ruoxue.backend.mapper.TPendingMapper;
import org.ruoxue.backend.service.ITPendingService;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
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

    @Resource
    private TCustomerMapper customerMapper;

    @Resource
    private TAdminMapper adminMapper;

    @Override
    public Object getNotificationByUid() {

        Integer uid = XunBinKit.getUid();

        if (ToolUtil.isEmpty(uid)) {
            return ResultUtil.error(-1, "用户未登录");
        }

        List<Map<String, Object>> list = pendingMapper.getNotificationByUid(uid, 0, 0, null);

        list = dealList(uid, list);

        return ResultUtil.success(list);
    }

    private List<Map<String, Object>> dealList(Integer uid, List<Map<String, Object>> list) {
        list.forEach(item -> {
            Integer senderaid = (Integer) item.get("senderaid");
            String user_name = "";
            if (ToolUtil.isEmpty(senderaid)) {
                TCustomer customer = customerMapper.getTCustomerByUid(uid);
                if (ToolUtil.isNotEmpty(customer)) {
                    user_name = customer.getName();
                } else {
                    user_name = " - ";
                }
            } else {
                TAdmin admin = adminMapper.getTAdminByUid(senderaid);
                if (ToolUtil.isNotEmpty(admin)) {
                    user_name = admin.getName();
                } else {
                    user_name = " - ";
                }
            }
            item.put("user_name", user_name);
            Date tm = (Date) item.get("tm");
            item.put("tm", tm.getTime());
        });
        return list;
    }

    @Override
    public Object getNotification() {

//        获取管理员id
        Integer uid = XunBinKit.getUid();

        if (ToolUtil.isEmpty(uid)) {
            return ResultUtil.error(-1, "管理员未登录");
        }

        List<Map<String, Object>> list = pendingMapper.getNotification(1, 0, uid);

        list = dealList(uid, list);

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

        List<Map<String, Object>> list = pendingMapper.getNotificationByGid(gid, 1, 0, admin.getId());

        list = dealList(admin.getId(), list);

        return ResultUtil.success(list);
    }

    @Override
    public Object getNotificationByAid() {

        Integer uid = XunBinKit.getUid();

        if (ToolUtil.isEmpty(uid)) {
            return ResultUtil.error(-1, "管理员未登录");
        }

        List<Map<String, Object>> list = pendingMapper.getNotificationByAid(uid, 0, 0, uid);

        list = dealList(uid, list);

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
