package org.ruoxue.backend.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TComSetProgress;
import org.ruoxue.backend.bean.TCompany;
import org.ruoxue.backend.mapper.TComSetProgressMapper;
import org.ruoxue.backend.mapper.TCompanyMapper;
import org.ruoxue.backend.service.ITComSetProgressService;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Service
public class TComSetProgressServiceImpl extends ServiceImpl<TComSetProgressMapper, TComSetProgress> implements ITComSetProgressService {

    @Resource
    private TComSetProgressMapper comSetProgressMapper;

    @Resource
    private TCompanyMapper companyMapper;

    @Override
    public Object getSetUp(String uid, Integer cid) {
//        非空验证
        XunBinKit.isEmpty(uid, cid);
//        处理uid
        Integer userid = XunBinKit.getUidByString(uid);
//        获取公司设立进度
        TComSetProgress comSetProgress = comSetProgressMapper.getSetUp(userid, cid);

        if(ToolUtil.isEmpty(comSetProgress)){
            return ResultUtil.error(-2, "查不到公司设立进度");
        } else {
            return ResultUtil.success(comSetProgress);
        }

    }

    @Override
    public Object addSetUp(String uid, Integer cid, Integer status, String note) {
//        非空验证
        XunBinKit.isEmpty(uid, cid, status, note);

//        插入一个设立进度
        TComSetProgress comSetProgress = new TComSetProgress();
        comSetProgress.setCid(cid);
        comSetProgress.setNote(note);
        comSetProgress.setStatus(status);
        comSetProgress.setTm(new Date());
        comSetProgress.setUid(Integer.parseInt(uid));
        boolean b = comSetProgress.insert();

//        返回结果
        return XunBinKit.returnResult(b, -2, null,"添加成功", "添加失败");

    }

    @Override
    public Object deleteSetUp(String uid, Integer cid) {
//        非空验证
        XunBinKit.isEmpty(uid, cid);

//        处理uid
        Integer userid = XunBinKit.getUidByString(uid);

//        删除设立进度
        Integer len = comSetProgressMapper.deleteSetUp(userid, cid);

//        获取公司实体
        TCompany company = companyMapper.getCompany(userid, cid);

//        获取最后一个公司进度
        String status = comSetProgressMapper.getStatusByLast();

//        更新公司进度
        company.setStatus(status);
        company.updateById();

        return XunBinKit.returnResult(len == 1, -2, null, "删除成功", "删除失败");
    }
}
