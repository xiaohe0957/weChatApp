package com.git.xh.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author heqi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MusicInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String singer;
    private String song;
    @TableField("songAddr")
    private String songAddr;
    private Double score;

    @TableField(exist=false)
    private Boolean liked;

    public MusicInfo(String singer, String song, String songAddr) {
        this.singer = singer;
        this.song = song;
        this.songAddr = songAddr;
    }

    public MusicInfo(String singer, String song, String songAddr, Double score) {
        this.singer = singer;
        this.song = song;
        this.songAddr = songAddr;
        this.score = score;
    }
}
