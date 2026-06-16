-- ============================================================
-- LookingAPP 数据库初始化脚本
-- MySQL 5.7+ / 8.0
-- ============================================================

CREATE DATABASE IF NOT EXISTS lookingapp
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE lookingapp;

-- ============================================================
-- 建表
-- ============================================================

DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS categories;

CREATE TABLE posts (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(200)  DEFAULT '',
    content     TEXT,
    image_url   VARCHAR(500)  DEFAULT '',
    category    VARCHAR(50)   DEFAULT '',
    author_name VARCHAR(50)   DEFAULT '',
    author_avatar VARCHAR(500) DEFAULT '',
    likes       INT           DEFAULT 0,
    comments    INT           DEFAULT 0,
    shares      INT           DEFAULT 0,
    is_liked    TINYINT(1)    DEFAULT 0,
    is_collected TINYINT(1)   DEFAULT 0,
    create_time VARCHAR(20)   DEFAULT '',
    is_draft    TINYINT(1)    DEFAULT 0,
    draft_time  VARCHAR(20)   DEFAULT NULL,
    post_type      INT           DEFAULT 0,
    video_resource VARCHAR(50)   DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE users (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    username     VARCHAR(50) NOT NULL UNIQUE,
    password     VARCHAR(64) DEFAULT '',
    nickname     VARCHAR(50) DEFAULT '',
    avatar       VARCHAR(500) DEFAULT '',
    bio          VARCHAR(200) DEFAULT '',
    follow_count INT          DEFAULT 0,
    fans_count   INT          DEFAULT 0,
    like_count   INT          DEFAULT 0,
    email        VARCHAR(100) DEFAULT '',
    phone        VARCHAR(20)  DEFAULT '',
    create_time  BIGINT       DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE comments (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    post_id      INT NOT NULL,
    author_name  VARCHAR(50)  DEFAULT '',
    author_avatar VARCHAR(500) DEFAULT '',
    content      VARCHAR(500) DEFAULT '',
    likes        INT          DEFAULT 0,
    create_time  VARCHAR(20)  DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE messages (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    type    INT          NOT NULL,
    title   VARCHAR(100) DEFAULT '',
    content VARCHAR(500) DEFAULT '',
    avatar  VARCHAR(500) DEFAULT '',
    time    VARCHAR(20)  DEFAULT '',
    is_read TINYINT(1)   DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE categories (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(50)  DEFAULT '',
    icon_url VARCHAR(500) DEFAULT '',
    count   INT          DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 预填充: 分类 (12条)
-- ============================================================

INSERT INTO categories (name, icon_url, count) VALUES
('全部', '', 0),
('旅行', 'https://images.unsplash.com/photo-1528360983277-13d401cdc186?w=400', 1287),
('美食', 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=400', 956),
('阅读', 'https://images.unsplash.com/photo-1548199973-03cce0bbc87b?w=400', 753),
('科技', 'https://images.unsplash.com/photo-1488190211105-8b0e65b80b4e?w=400', 646),
('运动', 'https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400', 532),
('摄影', 'https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=400', 407),
('生活', 'https://images.unsplash.com/photo-1490481651871-ab68de25d43d?w=400', 892),
('宠物', 'https://images.unsplash.com/photo-1517849845537-4d257902454a?w=400', 345),
('美妆', 'https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=400', 428),
('家居', 'https://images.unsplash.com/photo-1600891964599-f61ba0e24092?w=400', 267),
('音乐', 'https://images.unsplash.com/photo-1507838153414-b4b713384a76?w=400', 198);

-- ============================================================
-- 预填充: 测试用户
-- ============================================================

INSERT INTO users (username, password, nickname, avatar, bio, follow_count, fans_count, like_count, create_time)
VALUES ('test', 'e10adc3949ba59abbe56e057f20f883e', '小熊旅行日记',
        'https://i.pravatar.cc/100?img=1', '热爱生活，热爱旅行', 128, 2356, 5687, 1718230000000);

-- ============================================================
-- 预填充: 帖子 (42条，与 DataProvider 完全一致)
-- ============================================================

INSERT INTO posts (id,title,content,image_url,category,author_name,author_avatar,likes,comments,shares,create_time,is_liked,is_collected,is_draft,post_type,video_resource) VALUES
(1,'日本镰仓 | 来一场治愈之旅','坐上江之电，穿过海岸线和居民区，耳边是电车叮叮当当的声音。镰仓高校前..."，推荐路线：镰仓站 -> 鹤冈八幡宫 -> 长谷寺 -> 镰仓大佛 -> 江之岛。','https://images.unsplash.com/photo-1528360983277-13d401cdc186?w=400','旅行','小熊旅行日记','https://i.pravatar.cc/100?img=1',12000,256,98,'06-12',0,0,0,0,''),
(2,'10个提升幸福感的生活习惯','坚持了半年，真的改变了我的生活质量：1. 每天早起15分钟冥想 2. 喝够8杯水 ... 最重要的是#4，信息过载是现代人焦虑的源头。','https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=400','生活','一只小月亮','https://i.pravatar.cc/100?img=5',25000,380,150,'06-11',0,0,0,0,''),
(3,'在家也能做出餐厅级牛排','完美牛排秘诀大公开！选2cm厚安格斯牛排，提前1小时室温回温。铸铁锅烧到冒烟...最后醒肉5分钟再切，肉汁全锁在里面！','https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?w=400','美食','料理小当家','https://i.pravatar.cc/100?img=9',9870,201,87,'06-11',0,0,0,0,''),
(4,'中国最美的10个湖泊','走遍大江南北，这10个湖泊绝对不能错过：纳木错的圣洁蓝、青海湖的油菜花海、喀纳斯的翡翠绿、九寨沟的五彩池...每一个都是大自然的杰作！','https://images.unsplash.com/photo-1519125323398-675f0ddb6308?w=400','旅行','旅行家小明','https://i.pravatar.cc/100?img=12',123000,1560,420,'06-10',0,0,0,0,''),
(5,'豆瓣2024年度高分书单','今年读过最好的5本书：《原则》瑞·达利欧的人生算法；《思考快与慢》认识你的大脑...每一本都值得反复读。','https://images.unsplash.com/photo-1548199973-03cce0bbc87b?w=400','阅读','书虫阿宝','https://i.pravatar.cc/100?img=20',86000,920,310,'06-10',0,0,0,0,''),
(6,'坚持跑步30天的变化','体重从78kg降到74kg，睡眠质量大幅提升...给新手的建议：不要追求速度，先培养习惯，第一周只跑15分钟。','https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400','运动','健身达人Leo','https://i.pravatar.cc/100?img=32',61000,780,230,'06-09',0,0,0,0,''),
(7,'法式复古穿搭指南','不需要大牌，几件基础单品就能穿出法式优雅。核心单品：条纹衫、白衬衫、直筒牛仔裤、驼色风衣...配饰选金色或珍珠。整体风格是精心打理过的随意感。','https://images.unsplash.com/photo-1519125323398-675f0ddb6308?w=400','穿搭','时尚博主Mia','https://i.pravatar.cc/100?img=5',34000,456,189,'06-09',0,0,0,0,''),
(8,'AI工具让工作效率翻倍','分享我每天都在用的5个AI神器：ChatGPT写周报和邮件、Midjourney做PPT配图、Notion AI整理会议纪要...以前一天的工作量，现在半天就能搞定。','https://images.unsplash.com/photo-1488190211105-8b0e65b80b4e?w=400','科技','科技达人Kevin','https://i.pravatar.cc/100?img=12',78000,890,345,'06-08',0,0,0,0,''),
(9,'成都美食地图完全版','在成都吃了整整7天！火锅必去大龙燚和蜀大侠...还有隐藏菜单——曹家巷的甜水面、文殊院的素斋、玉林路的小酒馆。','https://images.unsplash.com/photo-1518770660439-4636190af475?w=400','美食','吃货小胖','https://i.pravatar.cc/100?img=9',156000,2100,680,'06-08',0,0,0,0,''),
(10,'手机摄影构图技巧','掌握这几个构图原则，手机也能拍大片：1. 三分法则 2. 前景框架 3. 引导线 4. 留白 5. 俯拍——食物和静物的最佳角度。','https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=400','摄影','摄影师老王','https://i.pravatar.cc/100?img=20',45000,567,210,'06-07',0,0,0,0,''),
(11,'极简主义生活实践一年后','断舍离了80%的物品后，我最大的收获不是空间变大了，而是决策疲劳消失了。衣柜里只有30件衣服...少即是多，是真的。','https://images.unsplash.com/photo-1600891964599-f61ba0e24092?w=400','生活','简约生活家','https://i.pravatar.cc/100?img=1',29000,340,125,'06-07',0,0,0,0,''),
(12,'云南大理4日自由行攻略','人均1500元玩转大理！Day1: 古城漫步+人民路小吃 Day2: 环洱海骑行 Day3: 苍山索道 Day4: 喜洲古镇。住宿推荐古城南门附近。','https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=400','旅行','旅行家小明','https://i.pravatar.cc/100?img=12',92000,1230,490,'06-06',0,0,0,0,''),
(13,'夏日必去的海边城市','国内这几个海滨城市，夏天去太绝了！厦门——文艺与海风交织；青岛——红瓦绿树配啤酒；三亚——椰林沙滩热带风；大连——北方最浪漫的城市...','https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=400','旅行','海边拾贝人','https://i.pravatar.cc/100?img=40',45000,620,180,'06-06',0,0,0,0,''),
(14,'猫咪行为学入门','我家猫主子最近总在凌晨4点跑酷，研究了才发现这是猫咪的掠食者节律。分享几个读懂猫咪的小知识：慢眨眼=信任、尾巴竖起来=开心...','https://images.unsplash.com/photo-1517849845537-4d257902454a?w=400','宠物','猫奴阿喵','https://i.pravatar.cc/100?img=45',21000,340,120,'06-05',0,0,0,0,''),
(15,'北欧风家居布置灵感','用宜家预算装出北欧风的高级感！核心要素：大面积白色+原木色+绿植点缀。客厅用浅灰布艺沙发配胡桃木茶几...灯光选2700K暖黄光，多点布灯而不是一盏大灯。','https://images.unsplash.com/photo-1600891964599-f61ba0e24092?w=400','家居','宅家设计师','https://i.pravatar.cc/100?img=50',38000,480,165,'06-05',0,0,0,0,''),
(16,'徒步雨崩 | 身体在地狱眼睛在天堂','雨崩村的秋天美到不真实。从飞来寺徒步进村，一路上雪山就在眼前，经幡在风中飘扬。冰湖线是最难但最值得的。','https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=400','旅行','野驴子户外','https://i.pravatar.cc/100?img=12',67000,890,320,'06-04',0,0,0,0,''),
(17,'一锅搞定的懒人煲仔饭','电饭煲就能做的懒人煲仔饭，腊肠的油渗透到米饭里，加上锅底那层焦香的锅巴，绝了！淋上酱汁(生抽+蚝油+糖+香油)。','https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=400','美食','深夜食堂阿飞','https://i.pravatar.cc/100?img=9',15600,230,78,'06-04',0,0,0,0,''),
(18,'自学钢琴3个月能弹什么？','零基础成人学琴的真实记录！3个月每天练40分钟...成人学琴不用从小汤开始，直接练自己喜欢的曲子更有动力；节拍器是你的好朋友。','https://images.unsplash.com/photo-1507838153414-b4b713384a76?w=400','音乐','练琴日记','https://i.pravatar.cc/100?img=55',9800,156,55,'06-03',0,0,0,0,''),
(19,'敏感肌护肤品红黑榜','油敏肌10年血泪史整理的红黑榜。红榜：珂润面霜(修复屏障)...最重要的是——防晒防晒防晒！','https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=400','美妆','护肤课代表','https://i.pravatar.cc/100?img=60',52000,670,240,'06-03',0,0,0,0,''),
(20,'杭州西湖深度游','去了N次西湖后总结的最美路线：断桥残雪 -> 白堤 -> 孤山 -> 曲院风荷 -> 苏堤春晓 -> 花港观鱼 -> 雷峰塔。全程约6公里。','https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?w=400','旅行','江南游子','https://i.pravatar.cc/100?img=1',31000,420,140,'06-02',0,0,0,0,''),
(21,'程序员健身一年的变化','从每天久坐12小时的典型码农到有6块腹肌的蜕变...最大的变化不是身材，而是精力——以前下午3点就犯困，现在一整天都精神饱满。','https://images.unsplash.com/photo-1457369804613-52c61a468e7d?w=400','运动','码农变型男','https://i.pravatar.cc/100?img=32',72000,930,350,'06-02',0,0,0,0,''),
(22,'小众香薰蜡烛推荐','这些国货香薰品牌完全不输Diptyque：观夏的昆仑煮雪(松木+雪松的清冷感)、To Summer的栀子花...几十块就能让整个房间充满高级感。','https://images.unsplash.com/photo-1558618666-fcd25c85f82e?w=400','美妆','气味收藏家','https://i.pravatar.cc/100?img=60',11300,189,67,'06-01',0,0,0,0,''),
(23,'复古胶片相机入坑指南','数码拍腻了？试试胶片！新手推荐：佳能AE-1、奥林巴斯OM-1...一卷36张，会逼着你慢下来认真构图。','https://images.unsplash.com/photo-1472396961693-142e6e269027?w=400','摄影','胶片大叔','https://i.pravatar.cc/100?img=20',18900,290,105,'06-01',0,0,0,0,''),
(24,'重庆3D魔幻城市拍照点','这几个机位拍出来绝对刷爆朋友圈：洪崖洞对面江滩拍夜景、李子坝轻轨穿楼、鹅岭二厂天台俯瞰全城...重庆真的太适合拍照了！','https://images.unsplash.com/photo-1488646953014-85cb44e25828?w=400','旅行','城市漫游者','https://i.pravatar.cc/100?img=40',41000,560,200,'05-31',0,0,0,0,''),
(25,'iPad做电子手帐一年后','用GoodNotes+Notability做电子手帐真的太香了...分享我的排版公式：标题用荧光笔打底+黑字、正文用0.4mm钢笔、装饰用同色系贴纸。','https://images.unsplash.com/photo-1533105079780-92b9be482077?w=400','生活','手帐少女','https://i.pravatar.cc/100?img=5',13400,210,76,'05-31',0,0,0,0,''),
(26,'狗狗分离焦虑怎么办','疫情期间养的小狗，复工后出现了严重的分离焦虑。最有效的是：1. 脱敏训练 2. 出门前不给太多关注 3. 留下带有你气味的旧衣服 4. 益智玩具。','https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=400','宠物','铲屎官老张','https://i.pravatar.cc/100?img=45',8800,145,52,'05-30',0,0,0,0,''),
(27,'搭建个人知识管理系统','用Obsidian+Readwise搭建了第二大脑...3个月后发现自己对很多话题都能形成体系化的观点了，不再是零散的知道一些。','https://images.unsplash.com/photo-1548199973-03cce0bbc87b?w=400','科技','知识管理控','https://i.pravatar.cc/100?img=50',25600,370,130,'05-30',0,0,0,0,''),
(28,'绝美冷门旅行地推荐','厌倦了人挤人的网红景点？这几个地方人少景美：福建霞浦、贵州荔波、四川色达、甘肃扎尕那、浙江松阳。趁它们还没火起来，赶紧去！','https://images.unsplash.com/photo-1533105079780-92b9be482077?w=400','旅行','冷门景点猎人','https://i.pravatar.cc/100?img=12',89000,1150,400,'05-29',0,0,0,0,''),

-- 视频帖 (postType=2, 8条, 与Android DataProvider + res/raw一致)
(29,'AI改变世界的10种方式','从 ChatGPT 到自动驾驶，从医疗诊断到气候预测，AI 正以前所未有的速度重塑我们的世界。本视频带你了解 AI 在未来十年将如何改变每个人的生活。','https://images.unsplash.com/photo-1488190211105-8b0e65b80b4e?w=400','科技','科技达人Kevin','https://i.pravatar.cc/100?img=12',92000,1100,450,'06-12',0,0,0,2,'video_keji'),
(30,'重庆火锅探店：藏在巷子里的老味道','跟着本地人吃最地道的重庆火锅！九宫格牛油锅底，鲜毛肚涮10秒，配上蒜泥香油碟，一口下去全是幸福感。','https://images.unsplash.com/photo-1518770660439-4636190af475?w=400','美食','吃货小胖','https://i.pravatar.cc/100?img=9',215000,3400,1200,'06-08',0,0,0,2,'video_meishi'),
(31,'黄金时刻：城市风光摄影技巧','日出日落前后的一小时被称为摄影的「黄金时刻」。本期教你如何利用逆光、剪影和暖色调，拍出刷爆朋友圈的城市大片。','https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=400','摄影','风光摄影师小北','https://i.pravatar.cc/100?img=20',56000,780,290,'06-04',0,0,0,2,'video_sheying'),
(32,'零基础吉他入门：学会第一首歌','选一把合适的吉他，调准音，学会 C、Am、F、G 四个和弦，10分钟就能弹唱《小星星》。音乐，从来都不晚开始。','https://images.unsplash.com/photo-1507838153414-b4b713384a76?w=400','音乐','尤克里里教室','https://i.pravatar.cc/100?img=55',45000,670,230,'06-02',0,0,0,2,'video_yinyue'),
(33,'北海道冬日绝美雪景','冬天的北海道简直是童话世界！小樽的雪灯之路、札幌的白色圣诞、富良野的滑雪场、登别的地狱谷温泉。最推荐在露天温泉里看雪花飘落，那种感觉用文字形容不出来。','https://images.unsplash.com/photo-1528360983277-13d401cdc186?w=400','旅行','小熊旅行日记','https://i.pravatar.cc/100?img=1',168000,2400,680,'06-10',0,0,0,2,'video_lvxing'),
(34,'美妆博主的一日妆容教程','手把手教你画出适合日常出街的自然淡妆！从保湿打底到定妆完成，每一步都有详细讲解。重点在于眼妆——用哑光大地色打底，再加一点珠光提亮眼头，整个眼睛瞬间有神。','https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=400','美妆','美妆博主Coco','https://i.pravatar.cc/100?img=60',134000,1800,520,'06-06',0,0,0,2,'video_meizhuang'),
(35,'HIIT高强度间歇训练跟练','15分钟燃脂暴汗训练，无需器械在家就能做！包含热身拉伸→高强度间歇→拉伸放松三个阶段。每个动作30秒，休息10秒，跟着视频节奏一起练。坚持一个月，腹部线条明显收紧。','https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400','运动','健身达人Leo','https://i.pravatar.cc/100?img=32',156000,2100,780,'06-03',0,0,0,2,'video_yundong'),
(36,'治愈系读书日常 | 周末书单推荐','一个安静的周末午后，窗外下着小雨，泡一杯热茶，翻开一本喜欢的书。本期推荐5本适合周末沉浸式阅读的好书，涵盖小说、散文、心理学，每一本都能让你的心安静下来。','https://images.unsplash.com/photo-1548199973-03cce0bbc87b?w=400','阅读','书虫阿宝','https://i.pravatar.cc/100?img=20',72000,950,310,'06-01',0,0,0,2,'video_yuedu'),

-- 纯文案帖 (postType=1, 6条)
(37,'深夜emo：关于30岁的焦虑','30岁生日那晚，一个人在阳台上坐了很久。不是害怕变老，而是突然意识到时间过得比想象中快太多。每个年龄段都有属于它的迷茫，重要的是别停下脚步。','','生活','城市独居者','https://i.pravatar.cc/100?img=5',45000,890,0,'06-11',0,0,0,1,''),
(38,'给刚毕业的自己的10句话','1. 第一份工作不重要，重要的是你学到了什么 2. 存钱，哪怕每个月只存500块 ... 10. 给你爸妈打个电话，他们很想你','','生活','过来人的忠告','https://i.pravatar.cc/100?img=50',156000,3200,0,'06-09',0,0,0,1,''),
(39,'裸辞后我后悔了吗？','上个月裸辞了，很多人问我后不后悔。说实话，第一个星期特别爽...虽然不确定未来会怎样，但至少现在的我是为自己而活的。','','职场','勇敢裸辞人','https://i.pravatar.cc/100?img=60',123000,2600,0,'06-07',0,0,0,1,''),
(40,'一个内向者的社交指南','作为INTJ型人格，社交对我来说从来不是一件轻松的事。内向者的细腻和深度思考，是这个世界非常需要的东西。','','职场','安静的力量','https://i.pravatar.cc/100?img=55',67000,1450,0,'06-05',0,0,0,1,''),
(41,'关于分手后的自我重建','三个月前结束了5年的感情。最难熬的不是分开那一刻，而是之后的日常...如果你也正在经历分手，我想说：难过是正常的，但请相信你会好起来的。','','情感','一个在自愈的人','https://i.pravatar.cc/100?img=40',198000,4500,0,'06-03',0,0,0,1,''),
(42,'什么样的城市值得留下？','北漂5年了，最近一直在思考这个问题。你在什么样的城市会感到值得留下？','','城市','北漂青年日记','https://i.pravatar.cc/100?img=12',78000,2100,0,'06-01',0,0,0,1,'');

-- ============================================================
-- 预填充: 评论 (各帖子各7条)
-- ============================================================

INSERT INTO comments (post_id, author_name, author_avatar, content, likes, create_time) VALUES
(1,'一只小月亮','https://i.pravatar.cc/100?img=5','真的好美啊！想去~',55,'06-12'),
(1,'旅行家小明','https://i.pravatar.cc/100?img=12','收藏了，下次去打卡！感谢分享',32,'06-12'),
(1,'美食达人','https://i.pravatar.cc/100?img=9','好的，谢谢分享~',18,'06-11'),
(1,'科技达人Kevin','https://i.pravatar.cc/100?img=12','博主拍的照片太美了！请问用什么相机？',12,'06-11'),
(1,'简约生活家','https://i.pravatar.cc/100?img=1','已经种草了，准备下个月去！',8,'06-10'),
(1,'健身达人Leo','https://i.pravatar.cc/100?img=32','风景好美，感觉站在那里整个人都治愈了',5,'06-10'),
(1,'猫奴阿喵','https://i.pravatar.cc/100?img=45','好棒的分享！关注了~',3,'06-09'),
(2,'一只小月亮','https://i.pravatar.cc/100?img=5','真的好美啊！想去~',55,'06-12'),
(2,'旅行家小明','https://i.pravatar.cc/100?img=12','收藏了，下次去打卡！感谢分享',32,'06-12'),
(2,'美食达人','https://i.pravatar.cc/100?img=9','好的，谢谢分享~',18,'06-11'),
(2,'科技达人Kevin','https://i.pravatar.cc/100?img=12','博主拍的照片太美了！请问用什么相机？',12,'06-11'),
(2,'简约生活家','https://i.pravatar.cc/100?img=1','已经种草了，准备下个月去！',8,'06-10'),
(2,'健身达人Leo','https://i.pravatar.cc/100?img=32','风景好美，感觉站在那里整个人都治愈了',5,'06-10'),
(2,'猫奴阿喵','https://i.pravatar.cc/100?img=45','好棒的分享！关注了~',3,'06-09'),
(3,'一只小月亮','https://i.pravatar.cc/100?img=5','真的好美啊！想去~',55,'06-12'),
(3,'旅行家小明','https://i.pravatar.cc/100?img=12','收藏了，下次去打卡！感谢分享',32,'06-12'),
(3,'美食达人','https://i.pravatar.cc/100?img=9','好的，谢谢分享~',18,'06-11'),
(3,'科技达人Kevin','https://i.pravatar.cc/100?img=12','博主拍的照片太美了！请问用什么相机？',12,'06-11'),
(3,'简约生活家','https://i.pravatar.cc/100?img=1','已经种草了，准备下个月去！',8,'06-10'),
(3,'健身达人Leo','https://i.pravatar.cc/100?img=32','风景好美，感觉站在那里整个人都治愈了',5,'06-10'),
(3,'猫奴阿喵','https://i.pravatar.cc/100?img=45','好棒的分享！关注了~',3,'06-09'),
(4,'一只小月亮','https://i.pravatar.cc/100?img=5','真的好美啊！想去~',55,'06-12'),
(4,'旅行家小明','https://i.pravatar.cc/100?img=12','收藏了，下次去打卡！感谢分享',32,'06-12'),
(4,'美食达人','https://i.pravatar.cc/100?img=9','好的，谢谢分享~',18,'06-11'),
(4,'科技达人Kevin','https://i.pravatar.cc/100?img=12','博主拍的照片太美了！请问用什么相机？',12,'06-11'),
(4,'简约生活家','https://i.pravatar.cc/100?img=1','已经种草了，准备下个月去！',8,'06-10'),
(4,'健身达人Leo','https://i.pravatar.cc/100?img=32','风景好美，感觉站在那里整个人都治愈了',5,'06-10'),
(4,'猫奴阿喵','https://i.pravatar.cc/100?img=45','好棒的分享！关注了~',3,'06-09'),
(5,'一只小月亮','https://i.pravatar.cc/100?img=5','真的好美啊！想去~',55,'06-12'),
(5,'旅行家小明','https://i.pravatar.cc/100?img=12','收藏了，下次去打卡！感谢分享',32,'06-12'),
(5,'美食达人','https://i.pravatar.cc/100?img=9','好的，谢谢分享~',18,'06-11'),
(5,'科技达人Kevin','https://i.pravatar.cc/100?img=12','博主拍的照片太美了！请问用什么相机？',12,'06-11'),
(5,'简约生活家','https://i.pravatar.cc/100?img=1','已经种草了，准备下个月去！',8,'06-10'),
(5,'健身达人Leo','https://i.pravatar.cc/100?img=32','风景好美，感觉站在那里整个人都治愈了',5,'06-10'),
(5,'猫奴阿喵','https://i.pravatar.cc/100?img=45','好棒的分享！关注了~',3,'06-09');

-- ============================================================
-- 预填充: 消息 (9条)
-- ============================================================

INSERT INTO messages (type, title, content, avatar, time, is_read) VALUES
(0,'系统通知','欢迎来到Looking！探索你所热爱的生活','','今天',0),
(1,'点赞通知','有人点赞了你的内容','','11:29',0),
(2,'评论通知','有人评论了你的内容','','10:15',0),
(3,'粉丝通知','有人关注了你','','昨天',0),
(4,'一只小月亮','你的照片拍得好好看，想问你用什么相机拍的呀~','https://i.pravatar.cc/100?img=5','12:30',0),
(4,'旅行家小明','看了你的大理攻略，下周就出发！还有什么避坑建议吗？','https://i.pravatar.cc/100?img=12','11:20',0),
(4,'美食达人','好的，谢谢分享~下次去成都就按这个吃','https://i.pravatar.cc/100?img=9','昨天',0),
(4,'猫奴阿喵','你家猫好可爱！能问一下是什么品种吗？','https://i.pravatar.cc/100?img=45','前天',0),
(4,'健身达人Leo','一起约跑吗？我也在北四环','https://i.pravatar.cc/100?img=32','前天',0);
