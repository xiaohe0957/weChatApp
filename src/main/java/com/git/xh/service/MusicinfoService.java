package com.git.xh.service;


import com.git.xh.entity.MusicInfo;
import com.git.xh.entity.UserLiked;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xh
 * @since 2021-04-21
 */
public interface MusicinfoService  {

    void addMusic(MusicInfo info);

    List<MusicInfo>  findMusic(String key);

    List<MusicInfo> getAllMusic();

    void updateMusic(MusicInfo musicInfo);

    void LikedMusic(String musicInfo, String wxUser,String type);

    UserLiked getLiked(String openId, Integer musicId);

    List<MusicInfo> findUserAllLiked(String userId);
}
