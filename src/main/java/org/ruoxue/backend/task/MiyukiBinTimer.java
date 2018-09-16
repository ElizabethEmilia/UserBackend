package org.ruoxue.backend.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *  定时器,专门用来泡定时任务
 */
@Component
public class MiyukiBinTimer {

    /**
     *
     *  更改服务包过期用户的状态以及服务包过期时间
     *  触发时间：每天0点后
     */
    @Scheduled(cron = "0 0 0 * * ?")              //定时任务,项目跑起来之后自动到时间执行
//    @RequestMapping(value = "", method = RequestMethod.GET)
    public void toSystem() {

    }

    /**
     *
     *  在年收入预选的操作人设置为“系统”
     *  修改所有客户所有公司的年收入预选状态为“待选择0”
     *  触发时间：每年1月1日0点后
     */
    @Scheduled(cron = "0 0 0 1 1 ?")
//    @RequestMapping(value = "", method = RequestMethod.GET)
    public void toSelect() {

    }

}
