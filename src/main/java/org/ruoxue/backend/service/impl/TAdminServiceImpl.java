package org.ruoxue.backend.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TAdmin;
import org.ruoxue.backend.bean.TSignin;
import org.ruoxue.backend.mapper.TAdminMapper;
import org.ruoxue.backend.mapper.TSigninMapper;
import org.ruoxue.backend.service.ITAdminService;
import org.ruoxue.backend.util.Md5Util;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
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
@Service("TAdminServiceImpl")
public class TAdminServiceImpl extends ServiceImpl<TAdminMapper, TAdmin> implements ITAdminService {

    @Resource
    private TAdminMapper adminMapper;

    @Resource
    private TSigninMapper signinMapper;

    @Override
    public Object basicGet(Integer uid) {
//        获取admin实体
        TAdmin admin = adminMapper.getTAdminByUid(uid);

        if(ToolUtil.isEmpty(admin)){
            return ResultUtil.error(-1, "未查到该管理员信息");
        }

        return ResultUtil.success(admin);
    }

    @Override
    public Object basicPost(TAdmin admin) {
//        获取用户id
        Integer uid = XunBinKit.getUid();
        if(ToolUtil.isEmpty(uid)){
            return ResultUtil.error(-1, "用户id为空");
        }

        TAdmin adm = adminMapper.getTAdminByUid(uid);
        if(ToolUtil.isEmpty(adm)){
            ResultUtil.error(-2, "该用户信息为空");
        }

        Integer len = adminMapper.updateAdmin(admin.getName(), adm.getId());
        if(len == 1){
            return ResultUtil.success();
        } else {
            return ResultUtil.error(-3, "修改管理员信息失败");
        }

    }

    @Override
    public Object password(String old_pwd, String new_pwd) {
//        获取用户id
        Integer uid = XunBinKit.getUid();
//        获取管理员bean
        TAdmin admin = adminMapper.getTAdminByUid(uid);
//        获取signbean
        TSignin signin = signinMapper.getSigninByUid(admin.getLid());
//        获取加密后的md5原密码
        String oldMd5Pwd = Md5Util.getMD5(old_pwd);
        System.out.println("--111: " + oldMd5Pwd);
        System.out.println("--111: " + signin.getPassword());
        if(!oldMd5Pwd.equals(signin.getPassword())){
            return ResultUtil.error(-1, "原密码错误");
        }

//            将新密码加密
        String newMd5Pwd = Md5Util.getMD5(new_pwd);
        Integer len = signinMapper.updatePassword(newMd5Pwd, admin.getLid());
        if(len == 1){
            return ResultUtil.success(0, "密码修改成功");
        } else {
            return ResultUtil.error(-2, "密码修改失败");
        }

    }


}
