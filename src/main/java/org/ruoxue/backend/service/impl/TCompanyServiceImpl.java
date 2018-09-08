package org.ruoxue.backend.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TCompany;
import org.ruoxue.backend.mapper.TCompanyMapper;
import org.ruoxue.backend.service.ITCompanyService;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
public class TCompanyServiceImpl extends ServiceImpl<TCompanyMapper, TCompany> implements ITCompanyService {

    @Resource
    private TCompanyMapper companyMapper;

    @Override
    public Object listCompany(String uid, Integer page, Integer size) {
        if(ToolUtil.isEmpty(uid)){
            return ResultUtil.error(-1, "参数错误");
        }
//        处理uid
        Integer userid = XunBinKit.getUidByString(uid);
        /**
         *  uid为用户id,必须
         *  page, size二者均可不输入，page默认为1，size默认为10
         *  如果size==0， 则是全部列表
         */
        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        List<Map<String, Object>> list = new ArrayList<>();
        page = (page - 1) * size;
        if(size == 0){
//          全部列表
            list = companyMapper.listCompanyAll(userid);
        } else {
            list = companyMapper.listCompany(userid, page, size);
        }

        return ResultUtil.success(list);
    }

    @Override
    public Object getCompany(String uid, Integer cid) {
        if(ToolUtil.isEmpty(uid) || ToolUtil.isEmpty(cid)){
            return ResultUtil.error(-1, "参数错误");
        }
//        处理uid
        Integer userid = XunBinKit.getUidByString(uid);

        TCompany company = companyMapper.getCompany(userid, cid);
        if(ToolUtil.isEmpty(company)){
            return ResultUtil.error(-2, "查不到该公司的信息");
        }
        return ResultUtil.success(company);
    }

    @Override
    public Object updateCompany(TCompany company, String uid, Integer cid) {
        if(ToolUtil.isEmpty(company) || ToolUtil.isEmpty(uid) || ToolUtil.isEmpty(cid)){
            return ResultUtil.error(-1, "参数错误");
        }
//        处理uid
        Integer userid = XunBinKit.getUidByString(uid);

//        获取原公司实体
        TCompany com = companyMapper.getCompany(userid, cid);
        if(ToolUtil.isEmpty(com)){
            return ResultUtil.error(-2, "原公司信息为空");
        }
//        将参数塞入传入实体
        company.setUid(com.getUid());
        company.setParent(com.getParent());
        company.setTaxPackStart(com.getTaxPackStart());
        company.setTaxPackEnd(com.getTaxPackEnd());
        company.setOriTaxPackEnd(com.getOriTaxPackEnd());
        company.setOriTaxPackStart(com.getOriTaxPackStart());
        company.setTmFirstEc(com.getTmFirstEc());
        company.setStatus(com.getStatus());
        boolean b = com.updateById();
        if(b){
            return ResultUtil.success(0, "修改成功");
        } else {
            return ResultUtil.error(-3, "修改公司信息失败");
        }
    }

    @Override
    public Object deleteCompany(String uid, Integer cid) {
        if(ToolUtil.isEmpty(uid) || ToolUtil.isEmpty(cid)){
            return ResultUtil.error(-1, "参数错误");
        }
//        处理uid
        Integer userid = XunBinKit.getUidByString(uid);
//        删除一个公司
        Integer len = companyMapper.deleteCompany(userid, cid);
        if(len == 1){
            return ResultUtil.success(0, "删除成功");
        } else {
            return ResultUtil.error(-2, "删除失败");
        }
    }

    @Override
    public Object deleteCompanys(String uid) {
        if(ToolUtil.isEmpty(uid)){
            return ResultUtil.error(-1, "参数错误");
        }
//        处理uid
        Integer userid = XunBinKit.getUidByString(uid);
//        删除客户的所有公司
        Integer len = companyMapper.deleteCompanys(userid);
        return ResultUtil.success(0,"您一共删除了" + len + "个公司");
    }

    @Override
    public Object addCompony(TCompany company, String uid) {
        if(ToolUtil.isEmpty(uid) || ToolUtil.isEmpty(company)){
            return ResultUtil.error(-1, "参数错误");
        }
//        处理uid
        Integer userid = XunBinKit.getUidByString(uid);
        System.out.println("-------------company: " + company);
        company.setUid(userid);
        company.setStatus("");
        boolean b = company.insert();
        if(b){
            return ResultUtil.success(0, "添加成功");
        } else {
            return ResultUtil.error(-2, "添加失败");
        }
    }


}
