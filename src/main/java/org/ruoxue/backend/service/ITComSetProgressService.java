package org.ruoxue.backend.service;

import org.ruoxue.backend.bean.TComSetProgress;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fengjb
 * @since 2018-08-30
 */
public interface ITComSetProgressService extends IService<TComSetProgress> {

    Object getSetUp(String uid, Integer cid);

    Object addSetUp(String uid, Integer cid, String status, String note);

    Object deleteSetUp(String uid, Integer cid);



}
