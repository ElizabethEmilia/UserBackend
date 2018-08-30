package org.ruoxue.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.ruoxue.backend.bean.TUser;
import org.ruoxue.backend.mapper.TUserMapper;
import org.ruoxue.backend.service.TUserService;
import org.ruoxue.backend.util.ResultUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("tUserService")
public class TUserServiceImpl implements TUserService {

    @Resource
    private TUserMapper tUserMapper;

    @Override
    public Object getTUserById(Integer userid) {
//        获取参数
//        Integer userid = jsonObject.getInteger("userid");

        TUser tUser = tUserMapper.selectById(userid);

        JSONObject json = new JSONObject();
        json.put("data", tUser);
        return ResultUtil.success(json);
    }
}
