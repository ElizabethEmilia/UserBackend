package org.ruoxue.backend.config.factory;

import com.baomidou.mybatisplus.plugins.Page;
import org.ruoxue.backend.common.state.Order;
import org.ruoxue.backend.util.HttpKit;
import org.ruoxue.backend.util.ToolUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * BootStrap Table默认的分页参数创建
 *
 * @author fengjb
 * @date 2017-04-05 22:25
 */
public class PageFactory<T> {

    public Page<T> defaultPage() {
        HttpServletRequest request = HttpKit.getRequest();
        int limit = Integer.valueOf(request.getParameter("limit"));
        int offset = Integer.valueOf(request.getParameter("offset"));
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");
        if(ToolUtil.isEmpty(sort)){
            Page<T> page = new Page<T>((offset / limit + 1), limit);
            page.setOpenSort(false);
            return page;
        }else{
            Page<T> page = new Page<T>((offset / limit + 1), limit, sort);
            if(Order.ASC.getDes().equals(order)){
                page.setAsc(true);
            }else{
                page.setAsc(false);
            }
            return page;
        }
    }
}
