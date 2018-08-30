package org.ruoxue.backend.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TAdmin;
import org.ruoxue.backend.bean.base.TOperationLog;
import org.ruoxue.backend.mapper.TAdminMapper;
import org.ruoxue.backend.service.ITAdminService;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
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
 * @since 2018-08-30
 */
@Service
public class TAdminServiceImpl extends ServiceImpl<TAdminMapper, TAdmin> implements ITAdminService {

    @Resource
    private TAdminMapper adminMapper;

    @Override
    public Object handleAdminAdd(TAdmin admin) {
        //        非空检验
        if(ToolUtil.isEmpty(admin)){
            return ResultUtil.error(-1, "该数据为空");
        }
//        将该管理员插入库中
        boolean b = admin.insert();
        if(b){
            return ResultUtil.success();
        } else {
            return ResultUtil.error(-2, "插入数据失败");
        }

    }

    @Override
    public Object handleAdminUpdate(TAdmin admin) {
//        非空检验
        if(ToolUtil.isEmpty(admin)){
            return ResultUtil.error(-1, "该数据为空");
        }
//        将该管理员修改信息插入库中
        boolean b = admin.updateById();
        if(b){
            return ResultUtil.success();
        } else {
            return ResultUtil.error(-2, "更新管理员信息失败");
        }
    }

    @Override
    public Object handleAdminRemove(Integer id) {
        if(ToolUtil.isEmpty(id)){
            return ResultUtil.error(-1, "请检查您的参数");
        }
        TAdmin admin = adminMapper.selectById(id);
        if(ToolUtil.isEmpty(admin)){
            return ResultUtil.error(-2, "该管理员不存在");
        }
//        删除一个管理员
        boolean b = admin.deleteById();
        if(b){
            return ResultUtil.success();
        } else {
            return ResultUtil.error(-3, "删除失败,可能是数据已不存在");
        }

    }

    @Override
    public List<Map<String, Object>> getAdminList(Page<TOperationLog> page) {
        return adminMapper.getAdminList(page, page.getOrderByField(), page.isAsc());
    }


}
