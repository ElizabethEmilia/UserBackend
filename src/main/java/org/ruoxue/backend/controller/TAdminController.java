package org.ruoxue.backend.controller;


import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.ApiOperation;
import org.ruoxue.backend.bean.TAdmin;
import org.ruoxue.backend.bean.base.TOperationLog;
import org.ruoxue.backend.common.controller.BaseController;
import org.ruoxue.backend.config.factory.PageFactory;
import org.ruoxue.backend.service.ITAdminService;
import org.ruoxue.backend.util.ToolUtil;
import org.ruoxue.backend.wrapper.TAdminWarpper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 管理员控制器
 *
 * @author fengjb
 * @since 2018-08-30
 */
@Controller
@RequestMapping("/tAdmin")
public class TAdminController extends BaseController {

    private String PREFIX = "/html/";

    private static final String MSG_NULL = "该数据为空";

    @Resource
    private ITAdminService adminService;

    @RequestMapping(value = "/showAdd", method = RequestMethod.GET)
    public String showAdminAdd(){
        return PREFIX + "admin_add.html";
    }

    @RequestMapping(value = "/showUpdate/{id}", method = RequestMethod.GET)
    public String showAdminUpdate(@PathVariable Integer id, Map map){
//        查出该管理员
        TAdmin admin = adminService.selectById(id);
        if(ToolUtil.isEmpty(admin)){
            return MSG_NULL;
        }
//        将该管理员信息传给前端
        map.put("admin", admin);
        return PREFIX + "admin_update.html";
    }

    @ApiOperation("根据TAdmin实体添加管理员")
    @RequestMapping(value = "/handleAdd", method = RequestMethod.POST)
    public @ResponseBody Object handleAdminAdd(@Valid TAdmin admin){
//        将业务交给service层
        return adminService.handleAdminAdd(admin);
    }

    @ApiOperation("根据管理员实体修改管理员")
    @RequestMapping(value = "/handleUpdate", method = RequestMethod.POST)
    public @ResponseBody Object handleAdminUpdate(@Valid TAdmin admin){
        return adminService.handleAdminUpdate(admin);
    }

    @ApiOperation("根据管理员id删除一个管理员")
    @RequestMapping(value = "/handleRemove/{id}", method = RequestMethod.GET)
    public @ResponseBody Object handleAdminRemove(@PathVariable Integer id){
        return adminService.handleAdminRemove(id);
    }

    @ApiOperation("利用mybatis-plus分页插件对list进行分页")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody Object handleAdminSelect(){
        Page<TOperationLog> page = new PageFactory<TOperationLog>().defaultPage();
//        获取管理员列表
        List<Map<String, Object>> list = adminService.getAdminList(page);
        page.setRecords((List<TOperationLog>) new TAdminWarpper(list).warp());
        return super.packForBT(page);

    }



	
}
