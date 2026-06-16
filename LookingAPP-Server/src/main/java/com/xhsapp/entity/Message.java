package com.xhsapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("messages")
public class Message {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer type;      // 0=系统 1=点赞 2=评论 3=粉丝 4=私信
    private String title;
    private String content;
    private String avatar;
    private String time;
    private Boolean isRead;
}
