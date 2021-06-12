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
public class UserLiked {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("openId")
    private String openId;
    @TableField("musicId")
    private Integer musicId;
}
