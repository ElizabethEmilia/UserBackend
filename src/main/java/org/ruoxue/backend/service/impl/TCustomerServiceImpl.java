package org.ruoxue.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.ruoxue.backend.bean.TCustomer;
import org.ruoxue.backend.mapper.MainMapper;
import org.ruoxue.backend.mapper.TCustomerMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.service.ITCustomerService;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Service
public class TCustomerServiceImpl extends ServiceImpl<TCustomerMapper, TCustomer> implements ITCustomerService {

    @Resource
    private TCustomerMapper customerMapper;

    @Resource
    private MainMapper mainMapper;

    @Override
    public Object CustomerRegister(JSONObject jsonObject) {
//        获取参数
        String name = jsonObject.getString("name");
        String password = jsonObject.getString("password");
        String phone = jsonObject.getString("phone");
        Integer industry = jsonObject.getInteger("industry");
        Integer type = jsonObject.getInteger("type");
        String msgcode = jsonObject.getString("msgcode");

//        非空检验
        if(ToolUtil.isEmpty(name) || ToolUtil.isEmpty(password) || ToolUtil.isEmpty(phone) || ToolUtil.isEmpty(industry) || ToolUtil.isEmpty(type) || ToolUtil.isEmpty(msgcode)) {
            return ResultUtil.error(-1, "请检查您的参数");
        }

        TCustomer customer = mainMapper.getTCustomerByName(name);
        if(ToolUtil.isNotEmpty(customer)){
            return ResultUtil.error(-2, "该用户已注册");
        }

//        将客户插入数据库中

        return null;
    }
}
