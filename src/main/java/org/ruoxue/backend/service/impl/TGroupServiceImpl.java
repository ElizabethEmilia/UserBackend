package org.ruoxue.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TGroup;
import org.ruoxue.backend.mapper.TGroupMapper;
import org.ruoxue.backend.service.ITGroupService;
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
public class TGroupServiceImpl extends ServiceImpl<TGroupMapper, TGroup> implements ITGroupService {

    @Resource
    private TGroupMapper groupMapper;

    @Override
    public Object listGroup(Integer page, Integer size) {
        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<Map<String, Object>> list = groupMapper.listGroup(page, size);

        return ResultUtil.success(list);
    }

    @Override
    public Object listSimple() {

        List<Map<String, Object>> list = groupMapper.listSimple();

        return ResultUtil.success(list);
    }

    @Override
    public Object groupAdd(JSONObject jsonObject) {

//        获取参数
        String name = jsonObject.getString("name");
        String remark = jsonObject.getString("remark");

        if (ToolUtil.isEmpty(name) || ToolUtil.isEmpty(remark)) {
            return ResultUtil.error(-1, "参数错误");
        }

        TGroup group = new TGroup();
        group.setName(name);
        group.setRemark(remark);
        boolean b = group.insert();

        return XunBinKit.returnResult(b, -2, null, "添加成功", "添加失败");
    }

    @Override
    public Object groupRemove(Integer gid) {

        if (ToolUtil.isEmpty(gid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        Integer len = groupMapper.removeGroup(gid);

        return XunBinKit.returnResult(len > 0, -2, null, "删除成功", "删除失败");
    }

    @Override
    public Object groupUpdate(Integer gid, String name, String remark) {

        if (ToolUtil.isEmpty(gid) || ToolUtil.isEmpty(name) || ToolUtil.isEmpty(remark)) {
            return ResultUtil.error(-1, "参数错误");
        }

        Integer len = groupMapper.updateGroup(gid, name, remark);

        return XunBinKit.returnResult(len > 0, -2, null, "修改成功", "修改失败");
    }

    @Override
    public Object listGroupAdmin(Integer gid, Integer page, Integer size) {

        if ( ToolUtil.isEmpty(gid) ) {
            return ResultUtil.error(-1, "参数错误");
        }

        if ( ToolUtil.isEmpty(page) ) {
            page = 1;
        }
        if ( ToolUtil.isEmpty(size) ) {
            size = 10;
        }
        page = (page - 1) * size;

        List<Map<String, Object>> list = groupMapper.listGroupAdmin(gid, page, size);

        return ResultUtil.success(list);
    }

    @Override
    public Object listGroupCustomer(Integer gid, Integer page, Integer size) {
        if (ToolUtil.isEmpty(gid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<Map<String, Object>> list = groupMapper.listGroupCustomer(gid, page, size);

        return ResultUtil.success(list);
    }
}
