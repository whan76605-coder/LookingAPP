# Looking App — 功能全景介绍

## App 定位

**Looking** 是一款仿小红书风格的移动端内容社区 App，核心用处是：

- **图文/视频/纯文案**三种形式的笔记发布与浏览
- 旅行、美食、阅读、科技、运动、摄影、生活、宠物、美妆、家居、音乐等 12 个分类的内容发现
- 点赞、收藏、评论、分享、关注作者等社区互动
- AI 智能对话助手（DeepSeek 大模型）
- 实时通知中心（点赞/评论/粉丝/系统消息/私信）

Android 端 + Spring Boot 后端完整全栈项目，总计 213 个源文件。

| 端 | 源文件数 |
|---|---|
| Android（java + xml + resource） | 182  |
| 后端 Spring Boot（java + yml + sql） | 31 |
| **合计** | **213** |

## 1. 启动页 SplashActivity
- 3 秒品牌闪屏 → 自动跳转首页

## 2. 首页 HomeFragment（推荐流）
- **瀑布流帖子列表**：双列 StaggeredGrid，交错高度
- **Banner 轮播**：顶部 ViewPager2 自动轮播 + 无限循环
- **顶部快捷入口**：热门 / 分类 / 发现 / 视频 / 阅读，一键跳转
- **Tab 切换**：推荐（全量帖子）→ 关注（仅已关注作者）
- **右上角**：通知铃铛 → NoticeActivity / AI 助手 → ChatActivity / 随机刷新
- **下拉刷新**：SwipeRefreshLayout
- **帖子三态**：图文（封面图）/ 纯文案（文字预览）/ 视频（封面+标签）
- **点赞**：点击心形图标本地即刻更新，Room 持久化
- **列表项动画**：item_slide_up 渐入动画

## 3. 帖子详情 PostDetailActivity
- **图文帖**：全屏大图 + 点击放大
- **视频帖**：VideoView 播放，自动播放 + 循环
  - 进度条拖动
  - 播放/暂停
  - 倍速切换（0.5x → 1x → 1.5x → 2x）
  - 控制栏 3 秒自动隐藏
  - 右手势滑动调节音量
- **底部互动栏**：点赞 / 收藏 / 分享 / 更多
- **评论区**：查看评论 + 发表评论
- **作者信息**：头像→查看主页 / 关注/取消关注
- **我的帖子可删除**：ivMore 弹窗删除

## 4. 发布 PublishActivity
- 标题 + 正文输入
- 图文/纯文案/视频三种类型
- 保存草稿 + 草稿箱恢复

## 5. AI 助手 ChatActivity
- 接入 DeepSeek API，实时对话
- 打字中显示"思考中..."状态

## 6. 通知中心 NoticeActivity
- 系统通知（点赞/评论/粉丝/系统消息）
- 全部已读一键标记
- 复用 MessageAdapter

## 7. 底部导航（4 个 Tab）

| Tab | 功能 |
|---|---|
| 首页 | 推荐 / 关注 瀑布流 |
| 分类 | CategoryFragment，左侧分类 + 右侧帖子 |
| 消息 | MessageFragment，系统通知 + 私信，未读红点 + 粗体 |
| 我的 | ProfileFragment，个人信息 + 作品管理 |

## 8. 消息 MessageFragment
- 通知列表（5 类）：系统 / 点赞 / 评论 / 粉丝 / 私信
- 未读红点 + 标题加粗
- 点击标已读 + 弹窗详情
- 底部导航红点实时更新：进入消息页 / 标记已读 / onResume 均刷新
- 红点数字 = 数据库 `isRead=0` 条数

## 9. 我的 ProfileFragment
- **个人信息**：头像 + 昵称 + 简介 + 编辑资料
- **数据面板**：关注数 / 粉丝数 / 获赞数（仅统计自己作品点赞）
  - 点击关注/粉丝 → 列表弹窗
  - 点击获赞 → 仅显示自己作品数据
- **4 个 Tab**：发布 / 收藏 / 视频 / 赞过
- **功能入口**：
  - 我的收藏 → 切换到收藏 Tab
  - 浏览历史 → 弹窗列表 + 清空
  - 草稿箱 → 角标数 + 弹窗选择继续编辑
  - 获赞作品 → 仅自己作品数据弹窗
  - 夜间模式 → 一键切换
  - 帮助与反馈
- **未登录兜底**：引导登录

## 10. 搜索 SearchActivity
- 搜索帖子（标题/内容/分类/作者）
- 热门搜索标签
- 搜索历史持久化

## 11. 热门 HotActivity & 阅读 ReadActivity
- 热门：支持 mode=video 切换纯视频流
- 阅读：纯文案浏览模式

## 12. 认证 LoginActivity / RegisterActivity
- 登录 / 注册
- MD5 密码加密
- SharedPreferences 持久化登录态
- 支持后端 API 或本地 Fallback

## 13. 设置 SettingsActivity
- 夜间模式开关
- 通知开关
- 清理缓存
- 退出登录
- 当前版本号

---

### 一、数据存储技术

| 技术 | 用途 | 位置 |
|---|---|---|
| **Room**（Android 本地数据库） | 帖子/评论/消息的离线缓存与草稿持久化，4 个 DAO + 1 个 AppDatabase | Android `database/` 包 |
| **SharedPreferences**（SpUtils） | 登录态、用户信息、夜间模式、通知开关、搜索历史、浏览历史、已关注作者列表 | Android `utils/SpUtils.java` |
| **MySQL**（后端关系型数据库） | 5 张表：posts / users / comments / messages / categories，schema.sql 自动建表 + 预填充 | 后端 `resources/sql/schema.sql` |
| **MyBatis-Plus**（ORM） | 5 个 Mapper 继承 BaseMapper，零 XML，LambdaQueryWrapper 动态查询 | 后端 `mapper/` 包 |

### 二、网络访问技术

| 技术 | 用途 |
|---|---|
| **Retrofit2 + OkHttp3** | HTTP 客户端，日志拦截器，5s 连接 / 10s 读写超时，Gson 序列化 |
| **ApiInterface**（Retrofit 接口） | 15 个 API 端点：CRUD 帖子/评论/消息/分类、登录注册 |
| **ApiService**（数据层） | API 优先 → Room 缓存 → DataProvider Mock 兜底，三级数据策略 |
| **RetroCallback 适配器** | Response → ApiResponse 统一解析，onOk/onFail 回调分离 |
| **Spring Boot REST API** | 5 个 Controller，统一 Result<T> {code, message, data} 响应 |
| **CORS 跨域配置** | CorsFilter 全局放行，allowCredentials + allowedOriginPatterns |
| **DeepSeek API** | OkHttp 裸调 /chat/completions，Bearer Token 鉴权 |
| **Glide** | 图片加载：圆角裁剪 + 交叉淡入 + 占位图 |

### 三、事件处理技术

| 技术 | 用途 |
|---|---|
| **OnClickListener / OnItemClickListener** | 按钮、列表项点击，适配器回调模式 |
| **TabLayout.OnTabSelectedListener** | 首页推荐/关注切换、个人页发布/收藏/视频/赞过切换 |
| **BottomNavigationView.OnItemSelectedListener** | 底部 4 Tab 导航，消息 Tab 触发红点刷新 |
| **SwipeRefreshLayout.OnRefreshListener** | 首页下拉刷新 |
| **ViewPager2 + Handler.postDelayed** | Banner 3.5 秒自动轮播 + 无限循环 |
| **SeekBar.OnSeekBarChangeListener** | 视频进度拖动、音量调节 |
| **Handler + Runnable** | 播放进度 300ms 定时更新 / 控制栏 3 秒自动隐藏 |
| **GestureDetector + onTouchEvent** | 视频页右手势垂直滑动调音量 |
| **MediaPlayer 回调** | onPrepared / onCompletion / onError 视频生命周期 |
| **onActivityResult** | 发布完成后刷新首页 |
| **BroadcastReceiver** | 网络状态变化监测 |
| **Java 反射**（setPlaybackParams） | VideoView 0.5x/1x/1.5x/2x 倍速播放 |
| **ExecutorService 线程池** | diskIO×3 + networkIO×2 统一异步管理 |
| **AlertDialog + 自定义 View** | 关注/粉丝列表、删除确认、草稿箱、通知详情、帮助反馈 |

---

## 技术要点

| 层 | 方案 |
|---|---|
| 网络 | Retrofit + OkHttp |
| 本地 DB | Room（Post/Comment/Message） — 数据存储 |
| 图片 | Glide（圆形裁剪/交叉淡入） — 网络访问 |
| UI | Material Design + DataBinding + BottomNavigation — 事件处理 |
| 架构 | API 优先 → Room 缓存 → Mock 兜底 |
| 后端 | Spring Boot + MyBatis-Plus + MySQL |

## 当前数据

- **42 条帖子**：28 篇图文 + 8 条视频 + 6 条纯文案
- **8 段视频**：168MB 内嵌 raw 资源（旅行/美食/音乐/美妆/运动/阅读/摄影/科技）
- **12 个分类** + **42×7 条评论** + **9 条消息**
