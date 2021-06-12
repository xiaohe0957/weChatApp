package com.git.xh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.git.xh.entity.WxUser;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xh
 * @since 2021-04-21
 */
public interface WxuserService {

    void add(WxUser o);

    WxUser getWxuser(String openid);

    List<WxUser> getAllWxuser();
}
