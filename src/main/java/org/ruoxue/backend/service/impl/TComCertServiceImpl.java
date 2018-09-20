package org.ruoxue.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ruoxue.backend.bean.TComCert;
import org.ruoxue.backend.mapper.TComCertMapper;
import org.ruoxue.backend.mapper.TLogsMapper;
import org.ruoxue.backend.service.ITComCertService;
import org.ruoxue.backend.util.Base64Util;
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
public class TComCertServiceImpl extends ServiceImpl<TComCertMapper, TComCert> implements ITComCertService {

    @Resource
    private TComCertMapper comCertMapper;

    @Resource
    private TLogsMapper logsMapper;

    @Override
    public Object listCert(String uid, Integer cid, Integer page, Integer size) {
        if(ToolUtil.isEmpty(uid) || ToolUtil.isEmpty(cid)){
            return ResultUtil.error(-1, "参数错误");
        }
//        处理uid
        Integer userid = XunBinKit.getUidByString(uid);
        if(ToolUtil.isEmpty(page)){
            page = 1;
        }
        if(ToolUtil.isEmpty(size)){
            size = 10;
        }
        page = (page - 1) * size;
//        查询所有证件列表
        List<Map<String, Object>> list =  comCertMapper.listCert(userid, cid, page, size);
        if(list.size() == 0){
            return ResultUtil.error(-2, "该客户证件列表为空");
        } else {
            return ResultUtil.success(list);
        }
    }

    @Override
    public Object getCert(String uid, Integer certid) {
        if(ToolUtil.isEmpty(uid) || ToolUtil.isEmpty(certid)){
            return ResultUtil.error(-1, "参数错误");
        }
//        处理uid
        Integer userid = XunBinKit.getUidByString(uid);
//        获取某一个证件
        TComCert comCert = comCertMapper.getCert(userid, certid);
        if(ToolUtil.isEmpty(comCert)){
            return ResultUtil.error(-2, "该证件为空");
        } else {
            return ResultUtil.success(comCert);
        }
    }

    @Override
    public Object deleteCert(String uid, Integer certid) {
        if(ToolUtil.isEmpty(uid) || ToolUtil.isEmpty(certid)){
            return ResultUtil.error(-1, "参数错误");
        }
//        处理uid
        Integer userid = XunBinKit.getUidByString(uid);
//        删除某一个证件
        Integer len = comCertMapper.deleteCert(userid, certid);

        logsMapper.addLog(userid, "删除证件", 1);

        if(len == 1){
            return ResultUtil.success(0, "删除成功");
        } else {
            return ResultUtil.error(-2, "删除失败");
        }
    }

    @Override
    public Object addCert(JSONObject jsonObject, String uid, Integer cid) {
//        获取参数
        String certName = jsonObject.getString("certName");
        String certNo = jsonObject.getString("certNo");
        String certImg = jsonObject.getString("certImg");

//        非空验证
        if(ToolUtil.isEmpty(certImg) || ToolUtil.isEmpty(certName) || ToolUtil.isEmpty(certNo) || ToolUtil.isEmpty(uid) || ToolUtil.isEmpty(cid)){
            return ResultUtil.error(-1, "参数错误");
        }

        int iUid = -1;
        try { iUid = Integer.parseInt(uid); } catch (Exception e) { }

        TComCert comCert = new TComCert();
        comCert.setUid(iUid);
        comCert.setCid(cid);
        comCert.setCertImg(Base64Util.GenerateImageFromDataURI(certImg));
        comCert.setCertName(certName);
        comCert.setCertNo(certNo);
        comCert.setStatus(1);
        comCert.setTmUpd(new Date());

        logsMapper.addLog(iUid, "添加证件", 1);

        boolean b = comCert.insert();
        if(b){
            return ResultUtil.success(0, "上传证件成功");
        } else {
            return ResultUtil.error(-2, "上传证件失败");
        }

    }
}
