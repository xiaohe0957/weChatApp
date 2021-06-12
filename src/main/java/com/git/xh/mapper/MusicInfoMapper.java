package com.git.xh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.git.xh.entity.MusicInfo;
import com.git.xh.entity.UserLiked;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xh
 * @since 2021-04-21
 */
public interface MusicInfoMapper extends BaseMapper<MusicInfo> {


    @Select("select * from music_info where singer like concat('%',#{key},'%') or song like concat('%',#{key},'%')")
    List<MusicInfo> selectMusic(String key);

    @Insert("insert into user_liked values (null,#{openId},#{id})")
    void likedMusic(@Param("id") Integer id, @Param("openId") String openId);


    @Select("select * from user_liked where openId=#{openId} and musicId=#{musicId}")
    UserLiked selectLiked(@Param("openId") String openId, @Param("musicId") Integer musicId);

    @Delete("delete from user_liked where openId=#{openId} and musicId=#{id}")
    void deleteLikedMusic(@Param("id") Integer id, @Param("openId") String openId);

    @Select("select * from music_info a where  EXISTS (select musicId from user_liked b where b.openid = #{userId} and b.musicId = a.id) ")
    List<MusicInfo> selectAllLiked(String userId);
}
