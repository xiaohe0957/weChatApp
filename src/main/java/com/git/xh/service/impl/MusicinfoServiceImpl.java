package com.git.xh.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.git.xh.entity.MusicInfo;
import com.git.xh.entity.UserLiked;
import com.git.xh.entity.WxUser;
import com.git.xh.mapper.MusicInfoMapper;
import com.git.xh.service.MusicinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class MusicinfoServiceImpl implements MusicinfoService {

    @Autowired(required = false)
    MusicInfoMapper musicinfoMapper;

    @Override
    public void addMusic(MusicInfo info) {
        musicinfoMapper.insert(info);
    }

    @Override
    public List<MusicInfo> getAllMusic() {
        return musicinfoMapper.selectList(null);
    }

    @Override
    public List<MusicInfo>  findMusic(String key) {
        return musicinfoMapper.selectMusic(key);
    }

    @Override
    public void updateMusic(MusicInfo musicInfo) {
        musicInfo.setScore(musicInfo.getScore()+1);
        musicinfoMapper.update(musicInfo,new UpdateWrapper<MusicInfo>().eq("song",musicInfo.getSong()));
    }

    @Override
    public void LikedMusic(String musicInfo, String wxUser,String type) {
        MusicInfo music = JSONObject.parseObject(musicInfo,MusicInfo.class);
        WxUser user = JSONObject.parseObject(wxUser,WxUser.class);
        if ("liked".equalsIgnoreCase(type)){
            musicinfoMapper.likedMusic(music.getId(),user.getOpenId());
        }
        else {
            musicinfoMapper.deleteLikedMusic(music.getId(),user.getOpenId());
        }

    }

    @Override
    public UserLiked getLiked(String openId, Integer musicId) {
        return musicinfoMapper.selectLiked(openId,musicId);
    }

    @Override
    public List<MusicInfo> findUserAllLiked(String userId) {
        return musicinfoMapper.selectAllLiked(userId);
    }
}
