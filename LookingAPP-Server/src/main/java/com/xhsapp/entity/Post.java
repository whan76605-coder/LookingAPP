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
@TableName("posts")
public class Post {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String title;
    private String content;
    private String imageUrl;
    private String category;
    private String authorName;
    private String authorAvatar;
    private Integer likes;
    private Integer comments;
    private Integer shares;
    private Boolean isLiked;
    private Boolean isCollected;
    private String createTime;
    private Boolean isDraft;
    private String draftTime;
    private Integer postType;  // 0=图文 1=纯文案 2=视频
    private String videoResource;  // 本地视频 raw 资源名
}
