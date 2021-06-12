package com.git.xh.service.impl;

import com.git.xh.entity.WxUser;
import com.git.xh.mapper.WxUserMapper;
import com.git.xh.service.WxuserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xh
 * @since 2021-04-21
 */
@Service
public class WxuserServiceImpl implements WxuserService {

    @Autowired(required = false)
    WxUserMapper wxuserMapper;
    @Override
    public void add(WxUser o) {
        wxuserMapper.insert(o);
    }

    @Override
    public WxUser getWxuser(String openid) {
        return wxuserMapper.selectById(openid);
    }

    @Override
    public List<WxUser> getAllWxuser() {
        return wxuserMapper.selectList(null);
    }
}
