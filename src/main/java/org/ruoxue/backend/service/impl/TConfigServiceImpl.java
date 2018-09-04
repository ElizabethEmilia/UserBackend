package org.ruoxue.backend.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TConfig;
import org.ruoxue.backend.mapper.TAdminMapper;
import org.ruoxue.backend.mapper.TConfigMapper;
import org.ruoxue.backend.mapper.TCustomerMapper;
import org.ruoxue.backend.mapper.TSigninMapper;
import org.ruoxue.backend.service.ITConfigService;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
public class TConfigServiceImpl extends ServiceImpl<TConfigMapper, TConfig> implements ITConfigService {

    @Resource
    private TConfigMapper configMapper;

    @Resource
    private TAdminMapper adminMapper;

    @Resource
    private TCustomerMapper customerMapper;

    @Resource
    private TSigninMapper signinMapper;

    @Override
    public List<TConfig> getTConfig() {
        return configMapper.getConfigs();
    }

    @Override
    public Object setting() {
//        获取系统配置
        List<TConfig> list = configMapper.getConfigs();
        if(ToolUtil.isEmpty(list)){
            list = new ArrayList<TConfig>();
        }
        return ResultUtil.success(list);
    }

    @Override
    public Object settingKey(String key) {

        if(ToolUtil.isEmpty(key)){
            return ResultUtil.error(-1, "参数错误");
        }

        String value = configMapper.getConfigByName(key);
        if(ToolUtil.isEmpty(value)){
            return ResultUtil.error(-2, "该配置中系统中找不到");
        }
        return ResultUtil.success(value);
    }

    @Override
    public Object updateSettingKey(String key, String value) {

        if(ToolUtil.isEmpty(key) || ToolUtil.isEmpty(value)){
            return ResultUtil.error(-1, "参数错误");
        }

//        获取config
        TConfig config = configMapper.getTConfigByName(key);
        if(ToolUtil.isEmpty(config)){
            return ResultUtil.error(-2, "该配置中系统中找不到");
        }

//        修改配置
        config.setValue(value);
        boolean b = config.updateById();

        if(b){
            return ResultUtil.success(0, "修改配置成功");
        } else {
            return ResultUtil.error(-3, "修改配置失败");
        }


    }

    @Override
    public Object clearCache() {
//        获取所有要删除的管理员
        List<Integer> admins = adminMapper.getRemoveAdminLids();

//        获取所有要删除的客户
        List<Integer> customers = customerMapper.getRemoveCustomerLids();

//        所有要删除的记录
        admins.addAll(customers);

//        删除所有的管理员
        adminMapper.removeAdmin();

//        删除所有的用户
        customerMapper.removeCustomer();

//        删除所有status为3的sign的记录
        admins.forEach(id -> signinMapper.removeSign(id) );

        String result = "您总共清除了" + admins.size() + "条数据";

        return ResultUtil.success(result);
    }
}
