package org.ruoxue.backend.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TCompany;
import org.ruoxue.backend.bean.TTaxAccount;
import org.ruoxue.backend.mapper.TCompanyMapper;
import org.ruoxue.backend.mapper.TTaxAccountMapper;
import org.ruoxue.backend.service.ITTaxAccountService;
import org.ruoxue.backend.util.ResultUtil;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class TTaxAccountServiceImpl extends ServiceImpl<TTaxAccountMapper, TTaxAccount> implements ITTaxAccountService {

    @Resource
    private TTaxAccountMapper taxAccountMapper;

    @Resource
    private TCompanyMapper companyMapper;

    @Override
    public Object listTaxStat(Integer cid, Date yfrom, Date yto, Integer page, Integer size, Integer count) {

        if (ToolUtil.isNotEmpty(count)) {
            return ResultUtil.success(taxAccountMapper.countListGroup(cid, yfrom, yto, XunBinKit.getUid()));
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

//        根据年份，公司分组，获取所有组
//        [{year=2017, name=中国联通, sumAmount=4.0, cid=1}, {year=2018, name=中国联通, sumAmount=7.0, cid=1}, {year=2017, name=中国移动, sumAmount=5.0, cid=2}, {year=2018, name=中国移动, sumAmount=2.0, cid=2}]
        List<Map<String, Object>> listGroup = taxAccountMapper.listGroup(page, size, cid, yfrom, yto, XunBinKit.getUid());
        for (Map<String, Object> map : listGroup) {
            Double dou = taxAccountMapper.getRatioByTimeAndCid((Integer) map.get("cid"), map.get("year") + "", XunBinKit.getUid());
            map.put("preTaxRatio", dou);
        }

        return ResultUtil.success(listGroup);
    }

    @Override
    public Object listTaxDetail(Integer cid, Date mfrom, Date mto, Integer page, Integer size, Integer count) {

        if (ToolUtil.isNotEmpty(count)) {
            return ResultUtil.success(taxAccountMapper.countListTaxDetail(cid, mfrom, mto, XunBinKit.getUid()));
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<Map<String, Object>> list = taxAccountMapper.listTaxDetail(page, size, cid, mfrom, mto, XunBinKit.getUid());

        return ResultUtil.success(list);
    }


    @Override
    public Object listTaxStatAdmin(Integer cid, Date yfrom, Date yto, Integer page, Integer size, Integer count) {

        if (ToolUtil.isEmpty(cid)) {
            return ResultUtil.error(-1, "查询不到该公司");
        }

        if (ToolUtil.isNotEmpty(count)) {
            return ResultUtil.success(taxAccountMapper.countListGroup(cid, yfrom, yto, XunBinKit.getUid()));
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

//        根据年份，公司分组，获取所有组
        List<Map<String, Object>> listGroup = taxAccountMapper.listGroup(page, size, cid, yfrom, yto, XunBinKit.getUid());
        for (Map<String, Object> map : listGroup) {
            Double dou = taxAccountMapper.getRatioByTimeAndCid((Integer) map.get("cid"), map.get("year") + "", XunBinKit.getUid());
            map.put("preTaxRatio", dou);
        }

        return ResultUtil.success(listGroup);
    }

    @Override
    public Object listTaxDetailAdmin(Integer cid, Date mfrom, Date mto, Integer page, Integer size, Integer count) {

        if (ToolUtil.isEmpty(cid)) {
            return ResultUtil.error(-1, "该公司不存在");
        }

        if (ToolUtil.isNotEmpty(count)) {
            return ResultUtil.success(taxAccountMapper.countListTaxDetail(cid, mfrom, mto, XunBinKit.getUid()));
        }

        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;

        List<Map<String, Object>> list = taxAccountMapper.listTaxDetail(page, size, cid, mfrom, mto, XunBinKit.getUid());

        return ResultUtil.success(list);
    }

    @Override
    public Object updateTax(Integer cid, Integer uid, Double tax) {

        if (ToolUtil.isEmpty(cid) || ToolUtil.isEmpty(tax)) {
            return ResultUtil.error(-1, "参数错误");
        }

        TCompany company = companyMapper.getCompanyById(cid);

        if (ToolUtil.isEmpty(company)) {
            return ResultUtil.error(-2, "该公司不存在");
        }

        Double ratio = company.getPreTaxRatio();

        if (ToolUtil.isEmpty(ratio)) {
            return ResultUtil.error(-3, "该公司没有设置税金率");
        }

        TTaxAccount taxAccount = new TTaxAccount();
        taxAccount.setBalance(tax);
        taxAccount.setCid(cid);
        taxAccount.setUid(uid);
        taxAccount.setTyAptRatio(ratio);
        boolean b = taxAccount.insert();

        return XunBinKit.returnResult(b, -4, null, "操作成功", "操作失败");
    }
}
