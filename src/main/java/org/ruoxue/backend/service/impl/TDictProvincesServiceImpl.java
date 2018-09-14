package org.ruoxue.backend.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.json.CDL;
import org.json.JSONArray;
import org.ruoxue.backend.bean.TDictProvinces;
import org.ruoxue.backend.mapper.TDictProvincesMapper;
import org.ruoxue.backend.service.ITDictProvincesService;
import org.ruoxue.backend.util.XunBinKit;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fengjb
 * @since 2018-09-04
 */
@Service
public class TDictProvincesServiceImpl extends ServiceImpl<TDictProvincesMapper, TDictProvinces> implements ITDictProvincesService {

    @Override
    public Object jsonToCVS(String json, String filename) {
        JSONArray jsonArray = new JSONArray(json);
        String csv =CDL.toString(jsonArray);
        System.out.println("--------------csv: " + csv);

        HttpServletResponse response = XunBinKit.getResponse();
        response.setHeader("Content-Type", "application/download");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".csv");

        return csv;
    }

}
