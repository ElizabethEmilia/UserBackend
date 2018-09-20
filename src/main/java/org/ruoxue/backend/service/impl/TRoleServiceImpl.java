package org.ruoxue.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TRole;
import org.ruoxue.backend.mapper.TLogsMapper;
import org.ruoxue.backend.mapper.TRoleMapper;
import org.ruoxue.backend.service.ITRoleService;
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
 * @since 2018-09-11
 */
@Service
public class TRoleServiceImpl extends ServiceImpl<TRoleMapper, TRole> implements ITRoleService {

    @Resource
    private TRoleMapper roleMapper;

    @Resource
    private TLogsMapper logsMapper;

    @Override
    public Object list(Integer page, Integer size) {
        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<Map<String, Object>> list = roleMapper.list(page, size);

        return ResultUtil.success(list);
    }

    @Override
    public Object roleAdd(JSONObject jsonObject) {
//        获取参数
        String name = jsonObject.getString("name");
        Integer value = jsonObject.getIntValue("value");
        String remarks = jsonObject.getString("remarks");

        if(ToolUtil.isEmpty(name) || ToolUtil.isEmpty(value)){
            return ResultUtil.error(-1, "参数错误");
        }

        if(ToolUtil.isEmpty(remarks)){
            remarks = "-";
        }

        TRole role = new TRole();
        role.setName(name);
        role.setRemarks(remarks);
        role.setTemId(0);
        role.setValue(value);
        boolean b = role.insert();

        Integer uid = XunBinKit.getUid();
        logsMapper.addLog(uid, "新增角色", 1);

        return XunBinKit.returnResult(b, -2, null, "新增角色成功", "添加角色失败");
    }

    @Override
    public Object getRole(Integer roleid) {
        if(ToolUtil.isEmpty(roleid)){
            return ResultUtil.error(-1, "参数错误");
        }

        TRole role = roleMapper.getRoleById(roleid);

        return XunBinKit.returnResult(ToolUtil.isNotEmpty(role), -2, role, "查询成功", "查询失败");
    }

    @Override
    public Object removeRole(Integer roleid) {
        if(ToolUtil.isEmpty(roleid)){
            return ResultUtil.error(-1, "参数错误");
        }

        TRole role = roleMapper.getRoleById(roleid);
        if(ToolUtil.isEmpty(role)){
            return ResultUtil.error(-2, "没有查到该角色");
        }

        Integer len = roleMapper.removeRole(roleid);

        Integer uid = XunBinKit.getUid();
        logsMapper.addLog(uid, "删除角色", 1);

        return XunBinKit.returnResult(len > 0, -3, null, "删除角色成功", "删除角色失败");
    }

    @Override
    public Object updateRole(JSONObject jsonObject, Integer roleid) {
//        获取参数
        String name = jsonObject.getString("name");
        Integer value = jsonObject.getIntValue("value");
        String remarks = jsonObject.getString("remarks");

        if(ToolUtil.isEmpty(name) || ToolUtil.isEmpty(value) || ToolUtil.isEmpty(roleid)){
            return ResultUtil.error(-1, "参数错误");
        }

        if(ToolUtil.isEmpty(remarks)){
            remarks = "-";
        }

        TRole role = roleMapper.getRoleById(roleid);
        if(ToolUtil.isEmpty(role)){
            return ResultUtil.error(-2, "没有查到该角色");
        }

        Integer len = roleMapper.updateRole(name, value, remarks, roleid);
        Integer uid = XunBinKit.getUid();
        logsMapper.addLog(uid, "修改角色", 1);

        return XunBinKit.returnResult(len > 0, -3, null, "角色修改成功", "角色修改失败");
    }
}
