package com.git.xh.entity;

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
public class WxUser {
    private String country;
    private String gender;
    private String province;
    private String city;
    @TableField("avatarUrl")
    private String avatarUrl;
    @TableId("openId")
    private String openId;
    @TableField("nickName")
    private String nickName;
    private String language;
}
