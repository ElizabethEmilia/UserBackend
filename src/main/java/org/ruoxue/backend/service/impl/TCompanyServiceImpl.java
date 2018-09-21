package org.ruoxue.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TComCert;
import org.ruoxue.backend.bean.TCompany;
import org.ruoxue.backend.bean.TExpectedIncome;
import org.ruoxue.backend.common.constant.Constant;
import org.ruoxue.backend.mapper.TCompanyMapper;
import org.ruoxue.backend.mapper.TLogsMapper;
import org.ruoxue.backend.service.ITCompanyService;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
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

    @Resource
    private TLogsMapper logsMapper;

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
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        page = (page - 1) * size;
        if(size == 0){
//          全部列表
            list = companyMapper.listCompanyAll(userid);

        } else {
            list = companyMapper.listCompany(userid, page, size);
        }

//       判断到期时间，返回paid = 0/1;
        for (Map<String, Object> map : list) {
            Date endDate = (Date) map.get("tax_pack_end");
            if (endDate == null || endDate.getTime() < new Date().getTime()) {
                map.put("expired", 1);
            } else {
                map.put("expired", 0);
            }
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

        logsMapper.addLog(-1, "修改公司信息", 1);

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

        logsMapper.addLog(-1, "删除公司信息", 1);

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

        logsMapper.addLog(-1, "删除所有公司", 1);

        return ResultUtil.success(0,"您一共删除了" + len + "个公司");
    }

    @Override
    public Object addCompony(JSONObject jsonObject, Integer uid) {

//        获取参数
        String lpname = jsonObject.getString("lpname");
        Integer parent = jsonObject.getInteger("parent");
        String tax_type = jsonObject.getString("taxType");
        String vat_type = jsonObject.getString("vatType");
        Double pre_tax_ratio = jsonObject.getDouble("preTaxRatio");
        Double va_tax_ratio = jsonObject.getDouble("vaTaxRatio");
        Double vatr_freq = jsonObject.getDouble("vatrFreq");
        Double cb_tax = jsonObject.getDouble("cbTax");
        Double ea_tax = jsonObject.getDouble("eaTax");
        Double lea_tax = jsonObject.getDouble("leaTax");
        Double river_tax = jsonObject.getDouble("riverTax");
        String ent_org_type = jsonObject.getString("entOrgType");
        String inv_type = jsonObject.getString("invType");
        String business_type = jsonObject.getString("businessType");
        String name = jsonObject.getString("name");

        if(ToolUtil.isEmpty(uid)){
            return ResultUtil.error(-1, "参数错误");
        }

        TCompany company = new TCompany();
        company.setTaxPackEnd(new Date());
        company.setTaxPackStart(new Date());
        company.setParent(parent);
        company.setUid(uid);
        company.setStatus("");
        company.setCbTax(cb_tax);
        company.setEaTax(ea_tax);
        company.setBusinessType(business_type);
        company.setLeaTax(lea_tax);
        company.setInvType(inv_type);
        company.setLpname(lpname);
        company.setPreTaxRatio(pre_tax_ratio);
        company.setRiverTax(river_tax);
        company.setTaxType(tax_type);
        company.setVaTaxRatio(va_tax_ratio);
        company.setVatType(vat_type);
        company.setYsaRange(0);
        company.setVatrFreq(vatr_freq);
        company.setEntOrgType(ent_org_type);
        company.setName(name);

        logsMapper.addLog(-1, "添加一个公司", 1);

        System.out.println("-------------company: " + company);

        boolean b = company.insert();

        TExpectedIncome expectedIncome = new TExpectedIncome();
        expectedIncome.setCid(company.getId());
        expectedIncome.setUid(uid);
        expectedIncome.setYsaRange(Constant.PreTaxStallsStatus.UNSELECTED);
        expectedIncome.setTmActivate(new Date());
        expectedIncome.setTmInactivate(XunBinKit.getYearLastTime());
        expectedIncome.setStatus(0);
        expectedIncome.setOper("系统");
        expectedIncome.setTmOp(new Date());
        expectedIncome.insert();

        if(b){
            return ResultUtil.success(0, "添加成功");
        } else {
            return ResultUtil.error(-2, "添加失败");
        }
    }

    @Override
    public Object listCompanys(String search, Integer page, Integer size) {
        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<Map<String, Object>> list = companyMapper.listCompanys(search, page, size, XunBinKit.getUid());

        return ResultUtil.success(list);
    }

    @Override
    public Object getCompanyInfo(Integer cid) {
        if (ToolUtil.isEmpty(cid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        TCompany company = companyMapper.getCompanyById(cid);

        return XunBinKit.returnResult(ToolUtil.isNotEmpty(company), -2, company, "查询成功", "查询失败");
    }

    @Override
    public Object getCompanySetUp(Integer cid, Integer page, Integer size) {
        if (ToolUtil.isEmpty(cid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<Map<String, Object>> list = companyMapper.listCompanySetUp(cid, page, size);

        return ResultUtil.success(list);
    }

    @Override
    public Object getCompanyCert(Integer cid, Integer page, Integer size) {
        if (ToolUtil.isEmpty(cid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<Map<String, Object>> list = companyMapper.listComCertByCid(cid, page, size);

        return ResultUtil.success(list);
    }

    @Override
    public Object countCompanyCertById(Integer cid, Integer id) {

        if (ToolUtil.isEmpty(cid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        TComCert comCert = companyMapper.getComCertById(cid, id);

        return XunBinKit.returnResult(ToolUtil.isNotEmpty(comCert), -2, comCert, "查询成功", "查询失败");
    }

    @Override
    public Object countCompany() {

        Integer uid = XunBinKit.getUid();

        if (ToolUtil.isEmpty(uid)) {
            return ResultUtil.error(-1, "用户未登录");
        }

        Integer count = companyMapper.countCompanyByUid(uid);

        return ResultUtil.success(count);
    }

    @Override
    public Object removeSetupState(Integer cid, Integer setupid) {
        Integer uid = XunBinKit.getUid();

        if (ToolUtil.isEmpty(uid)) {
            return ResultUtil.error(-1, "用户未登录");
        }

        if (ToolUtil.isEmpty(setupid)) {
            return ResultUtil.error(-1, "参数错误");
        }

        return ResultUtil.result(
                companyMapper.removeSetupState(cid, setupid) ? 0 : 1,
                null, "删除失败"
        );

    }


}
