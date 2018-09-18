package org.ruoxue.backend.util;

import org.ruoxue.backend.common.constant.Constant;
import org.ruoxue.backend.mapper.TLogsMapper;
import org.ruoxue.backend.service.ITLogsService;

import javax.annotation.Resource;

public class LoopAction {
    @Resource
    static ITLogsService logsService;

    public static boolean tryUpToFiveTimes(ILoopActionHandler something, String description) {
        for (int i=0; i<5; i++) {
            if (something.doSomething())
                return true;
        }
        logsService.actionLogNow(Constant.LogUsers.SYSTEM, "执行" + description + "操作重试5次后失败", Constant.LogClass.IMPORTANT);
        return false;
    }
}
