package com.example.xhsapp.utils;

import com.example.xhsapp.model.Category;
import com.example.xhsapp.model.Comment;
import com.example.xhsapp.model.Message;
import com.example.xhsapp.model.Post;

import java.util.ArrayList;
import java.util.List;

public class DataProvider {

    private static final String[] IMAGES = {
            "https://images.unsplash.com/photo-1528360983277-13d401cdc186?w=400",
            "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=400",
            "https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?w=400",
            "https://images.unsplash.com/photo-1519125323398-675f0ddb6308?w=400",
            "https://images.unsplash.com/photo-1490481651871-ab68de25d43d?w=400",
            "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=400",
            "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400",
            "https://images.unsplash.com/photo-1518770660439-4636190af475?w=400",
            "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400",
            "https://images.unsplash.com/photo-1522202176988-66273c2fd55f?w=400",
            "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=400",
            "https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=400",
            "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=400",
            "https://images.unsplash.com/photo-1482049016688-2d3e1b311543?w=400",
            "https://images.unsplash.com/photo-1543353071-873f17a7a088?w=400",
            "https://images.unsplash.com/photo-1600891964599-f61ba0e24092?w=400",
            "https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=400",
            "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=400",
            "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=400",
            "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=400",
            "https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?w=400",
            "https://images.unsplash.com/photo-1488646953014-85cb44e25828?w=400",
            "https://images.unsplash.com/photo-1533105079780-92b9be482077?w=400",
            "https://images.unsplash.com/photo-1472396961693-142e6e269027?w=400",
            "https://images.unsplash.com/photo-1517849845537-4d257902454a?w=400",
            "https://images.unsplash.com/photo-1548199973-03cce0bbc87b?w=400",
            "https://images.unsplash.com/photo-1526336024174-e58f5cdd8e13?w=400",
            "https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=400",
            "https://images.unsplash.com/photo-1485965120184-e220f721d03e?w=400",
            "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=400",
            "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=400",
            "https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=400",
            "https://images.unsplash.com/photo-1478145046317-39f10e56b5e9?w=400",
            "https://images.unsplash.com/photo-1554048612-b6a482bc67e5?w=400",
            "https://images.unsplash.com/photo-1497436072909-60f360e1d4b1?w=400",
            "https://images.unsplash.com/photo-1519389950473-47ba0277781c?w=400",
            "https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=400",
            "https://images.unsplash.com/photo-1488190211105-8b0e65b80b4e?w=400",
            "https://images.unsplash.com/photo-1457369804613-52c61a468e7d?w=400",
            "https://images.unsplash.com/photo-1558618666-fcd25c85f82e?w=400",
    };

    private static final String[] AVATARS = {
            "https://i.pravatar.cc/100?img=1",
            "https://i.pravatar.cc/100?img=5",
            "https://i.pravatar.cc/100?img=9",
            "https://i.pravatar.cc/100?img=12",
            "https://i.pravatar.cc/100?img=20",
            "https://i.pravatar.cc/100?img=32",
            "https://i.pravatar.cc/100?img=40",
            "https://i.pravatar.cc/100?img=45",
            "https://i.pravatar.cc/100?img=50",
            "https://i.pravatar.cc/100?img=55",
            "https://i.pravatar.cc/100?img=60",
            "https://i.pravatar.cc/100?img=65",
    };

    public static List<Post> getMockPosts() {
        List<Post> list = new ArrayList<>();

        list.add(makePost(1, 0, "日本镰仓 | 来一场治愈之旅",
                "坐上江之电，穿过海岸线和居民区，耳边是电车叮叮当当的声音。镰仓高校前站的十字路口，就是灌篮高手那个经典场景。一定要在傍晚时分来，夕阳洒在海面上，整个城市都是金色的。推荐路线：镰仓站 -> 鹤冈八幡宫 -> 长谷寺 -> 镰仓大佛 -> 江之岛。",
                IMAGES[0], "旅行", "小熊旅行日记", AVATARS[0], 12000, 256, 98, "06-12"));
        list.add(makePost(2, 0, "10个提升幸福感的生活习惯",
                "坚持了半年，真的改变了我的生活质量：1. 每天早起15分钟冥想 2. 喝够8杯水 3. 睡前写下3件感恩的事 4. 每周断网半天 5. 把床铺整理干净 6. 每天走8000步 7. 少刷短视频 8. 学一道新菜 9. 每周读一本书 10. 定期断舍离。最重要的是#4，信息过载是现代人焦虑的源头。",
                IMAGES[3], "生活", "一只小月亮", AVATARS[1], 25000, 380, 150, "06-11"));
        list.add(makePost(3, 0, "在家也能做出餐厅级牛排",
                "完美牛排秘诀大公开！选2cm厚安格斯牛排，提前1小时室温回温。铸铁锅烧到冒烟，高温煎每面90秒，然后转小火加黄油、蒜瓣、迷迭香，用勺子不断浇淋。最后醒肉5分钟再切，肉汁全锁在里面！",
                IMAGES[1], "美食", "料理小当家", AVATARS[2], 9870, 201, 87, "06-11"));
        list.add(makePost(4, 0, "中国最美的10个湖泊",
                "走遍大江南北，这10个湖泊绝对不能错过：纳木错的圣洁蓝、青海湖的油菜花海、喀纳斯的翡翠绿、九寨沟的五彩池、泸沽湖的摩梭风情、西湖的烟雨朦胧……每一个都是大自然的杰作！",
                IMAGES[2], "旅行", "旅行家小明", AVATARS[3], 123000, 1560, 420, "06-10"));
        list.add(makePost(5, 0, "豆瓣2024年度高分书单",
                "今年读过最好的5本书：《原则》瑞·达利欧的人生算法；《思考快与慢》认识你的大脑两套系统；《人类简史》7万年人类文明全景；《原子习惯》微小改变的力量；《心流》找到最优体验。每一本都值得反复读。",
                IMAGES[37], "阅读", "书虫阿宝", AVATARS[4], 86000, 920, 310, "06-10"));
        list.add(makePost(6, 0, "坚持跑步30天的变化",
                "体重从78kg降到74kg，睡眠质量大幅提升，皮肤也变好了。最重要的是心态——以前遇到困难就想逃避，现在觉得跑完5公里都能坚持，这点事算什么。给新手的建议：不要追求速度，先培养习惯，第一周只跑15分钟。",
                IMAGES[5], "运动", "健身达人Leo", AVATARS[5], 61000, 780, 230, "06-09"));
        list.add(makePost(7, 0, "法式复古穿搭指南",
                "不需要大牌，几件基础单品就能穿出法式优雅。核心单品：条纹衫、白衬衫、直筒牛仔裤、驼色风衣、芭蕾平底鞋。关键在于颜色搭配——黑、白、驼、藏蓝四色为主，配饰选金色或珍珠。整体风格是精心打理过的随意感。",
                IMAGES[4], "穿搭", "时尚博主Mia", AVATARS[1], 34000, 456, 189, "06-09"));
        list.add(makePost(8, 0, "AI工具让工作效率翻倍",
                "分享我每天都在用的5个AI神器：ChatGPT写周报和邮件、Midjourney做PPT配图、Notion AI整理会议纪要、Copilot写代码、ElevenLabs做播客配音。以前一天的工作量，现在半天就能搞定，剩下时间用来深度思考。",
                IMAGES[35], "科技", "科技达人Kevin", AVATARS[3], 78000, 890, 345, "06-08"));
        list.add(makePost(9, 0, "成都美食地图完全版",
                "在成都吃了整整7天！火锅必去大龙燚和蜀大侠，串串推荐冒椒火辣，抄手要吃龙抄手的老店，担担面去洞子口张老二。还有隐藏菜单——曹家巷的甜水面、文殊院的素斋、玉林路的小酒馆。",
                IMAGES[12], "美食", "吃货小胖", AVATARS[2], 156000, 2100, 680, "06-08"));
        list.add(makePost(10, 0, "手机摄影构图技巧",
                "掌握这几个构图原则，手机也能拍大片：1. 三分法则——把主体放在交叉点 2. 前景框架——用门窗/树枝做天然画框 3. 引导线——利用道路/栏杆引导视线 4. 留白——给画面呼吸感 5. 俯拍——食物和静物的最佳角度。",
                IMAGES[32], "摄影", "摄影师老王", AVATARS[4], 45000, 567, 210, "06-07"));
        list.add(makePost(11, 0, "极简主义生活实践一年后",
                "断舍离了80%的物品后，我最大的收获不是空间变大了，而是决策疲劳消失了。衣柜里只有30件衣服，但每一件都很喜欢；厨房只有必要的锅碗，反而更愿意做饭了。少即是多，是真的。",
                IMAGES[16], "生活", "简约生活家", AVATARS[0], 29000, 340, 125, "06-07"));
        list.add(makePost(12, 0, "云南大理4日自由行攻略",
                "人均1500元玩转大理！Day1: 古城漫步+人民路小吃 Day2: 环洱海骑行(一定要租电动车) Day3: 苍山索道+寂照庵吃素斋 Day4: 喜洲古镇看白族建筑。住宿推荐古城南门附近，交通方便且安静。",
                IMAGES[8], "旅行", "旅行家小明", AVATARS[3], 92000, 1230, 490, "06-06"));
        list.add(makePost(13, 0, "夏日必去的海边城市",
                "国内这几个海滨城市，夏天去太绝了！厦门——文艺与海风交织；青岛——红瓦绿树配啤酒；三亚——椰林沙滩热带风；大连——北方最浪漫的城市；北海——涠洲岛的火山地貌独一无二。",
                IMAGES[10], "旅行", "海边拾贝人", AVATARS[6], 45000, 620, 180, "06-06"));
        list.add(makePost(14, 0, "猫咪行为学入门",
                "我家猫主子最近总在凌晨4点跑酷，研究了才发现这是猫咪的掠食者节律。分享几个读懂猫咪的小知识：慢眨眼=信任、尾巴竖起来=开心、踩奶=安全感爆棚、把死老鼠放你床上=它觉得你不会捕猎需要照顾...",
                IMAGES[23], "宠物", "猫奴阿喵", AVATARS[7], 21000, 340, 120, "06-05"));
        list.add(makePost(15, 0, "北欧风家居布置灵感",
                "用宜家预算装出北欧风的高级感！核心要素：大面积白色+原木色+绿植点缀。客厅用浅灰布艺沙发配胡桃木茶几，墙面挂几幅简约线条画，角落里放一盆龟背竹。灯光很重要——选2700K暖黄光，多点布灯而不是一盏大灯。",
                IMAGES[17], "家居", "宅家设计师", AVATARS[8], 38000, 480, 165, "06-05"));
        list.add(makePost(16, 0, "徒步雨崩 | 身体在地狱眼睛在天堂",
                "雨崩村的秋天美到不真实。从飞来寺徒步进村，一路上雪山就在眼前，经幡在风中飘扬。冰湖线是最难但最值得的——海拔爬升1000米，但当冰湖出现在眼前的那一刻，所有疲惫都值得了。",
                IMAGES[11], "旅行", "野驴子户外", AVATARS[3], 67000, 890, 320, "06-04"));
        list.add(makePost(17, 0, "一锅搞定的懒人煲仔饭",
                "电饭煲就能做的懒人煲仔饭，腊肠的油渗透到米饭里，加上锅底那层焦香的锅巴，绝了！做法：米泡30分钟 -> 铺腊肠片 -> 正常煮饭模式 -> 最后5分钟打入一个鸡蛋 -> 淋上酱汁(生抽+蚝油+糖+香油)。",
                IMAGES[13], "美食", "深夜食堂阿飞", AVATARS[2], 15600, 230, 78, "06-04"));
        list.add(makePost(18, 0, "自学钢琴3个月能弹什么？",
                "零基础成人学琴的真实记录！3个月每天练40分钟，现在已经能弹《River Flows in You》和《致爱丽丝》前半段。分享几点心得：成人学琴不用从小汤开始，直接练自己喜欢的曲子更有动力；手型很重要但不用过度纠结；节拍器是你的好朋友。",
                IMAGES[29], "音乐", "练琴日记", AVATARS[9], 9800, 156, 55, "06-03"));
        list.add(makePost(19, 0, "敏感肌护肤品红黑榜",
                "油敏肌10年血泪史整理的红黑榜。红榜：珂润面霜(修复屏障)、EltaMD洗面奶(氨基酸洁面)、修丽可色修(去红痘印)。黑榜：含酒精的爽肤水会越用越红、撕拉面膜毁屏障、高浓度酸类需要建立耐受。最重要的是——防晒防晒防晒！",
                IMAGES[26], "美妆", "护肤课代表", AVATARS[10], 52000, 670, 240, "06-03"));
        list.add(makePost(20, 0, "杭州西湖深度游",
                "去了N次西湖后总结的最美路线：断桥残雪 -> 白堤 -> 孤山 -> 曲院风荷 -> 苏堤春晓 -> 花港观鱼 -> 雷峰塔。全程约6公里，走走停停3-4小时。最佳季节是三月和十月，三月樱花季美到心醉。",
                IMAGES[20], "旅行", "江南游子", AVATARS[0], 31000, 420, 140, "06-02"));
        list.add(makePost(21, 0, "程序员健身一年的变化",
                "从每天久坐12小时的典型码农到有6块腹肌的蜕变。核心方法：早上6点半起床练1小时(推荐PPL训练计划)，饮食高蛋白低碳水，每天保证7小时睡眠。最大的变化不是身材，而是精力——以前下午3点就犯困，现在一整天都精神饱满。",
                IMAGES[39], "运动", "码农变型男", AVATARS[5], 72000, 930, 350, "06-02"));
        list.add(makePost(22, 0, "小众香薰蜡烛推荐",
                "这些国货香薰品牌完全不输Diptyque：观夏的昆仑煮雪(松木+雪松的清冷感)、To Summer的栀子花(还原度极高)、MOMAEK的雨后花园(青草+泥土的湿润气息)。几十块就能让整个房间充满高级感。",
                IMAGES[27], "美妆", "气味收藏家", AVATARS[10], 11300, 189, 67, "06-01"));
        list.add(makePost(23, 0, "复古胶片相机入坑指南",
                "数码拍腻了？试试胶片！新手推荐：佳能AE-1(操控好，镜头群丰富)、奥林巴斯OM-1(小巧精致)、康泰时T2(傻瓜机天花板但溢价严重)。冲扫推荐诺日士扫描，色彩最接近胶片原本的味道。一卷36张，会逼着你慢下来认真构图。",
                IMAGES[33], "摄影", "胶片大叔", AVATARS[4], 18900, 290, 105, "06-01"));
        list.add(makePost(24, 0, "重庆3D魔幻城市拍照点",
                "这几个机位拍出来绝对刷爆朋友圈：洪崖洞对面江滩拍夜景、李子坝轻轨穿楼、鹅岭二厂天台俯瞰全城、海棠溪筒子楼(少年的你取景地)、南山一棵树拍渝中半岛。重庆真的太适合拍照了！",
                IMAGES[21], "旅行", "城市漫游者", AVATARS[6], 41000, 560, 200, "05-31"));
        list.add(makePost(25, 0, "iPad做电子手帐一年后",
                "用GoodNotes+Notability做电子手帐真的太香了。好处：可以无限修改、贴纸素材免费下载、一键搜索历史记录、iCloud同步多设备查看。分享我的排版公式：标题用荧光笔打底+黑字、正文用0.4mm钢笔、装饰用同色系贴纸。",
                IMAGES[34], "生活", "手帐少女", AVATARS[1], 13400, 210, 76, "05-31"));
        list.add(makePost(26, 0, "狗狗分离焦虑怎么办",
                "疫情期间养的小狗，复工后出现了严重的分离焦虑——拆家、狂叫、不吃东西。试了很多方法，最有效的是：1. 脱敏训练——假装出门30秒就回来，慢慢拉长时间 2. 出门前不给太多关注 3. 留下带有你气味的旧衣服 4. 益智玩具分散注意力。",
                IMAGES[24], "宠物", "铲屎官老张", AVATARS[7], 8800, 145, 52, "05-30"));
        list.add(makePost(27, 0, "搭建个人知识管理系统",
                "用Obsidian+Readwise搭建了第二大脑。工作流：碎片信息 -> Readwise收集 -> 每周整理到Obsidian -> 用双链建立知识图谱 -> 定期回顾和输出。3个月后发现自己对很多话题都能形成体系化的观点了，不再是零散的知道一些。",
                IMAGES[37], "科技", "知识管理控", AVATARS[8], 25600, 370, 130, "05-30"));
        list.add(makePost(28, 0, "绝美冷门旅行地推荐",
                "厌倦了人挤人的网红景点？这几个地方人少景美：福建霞浦(滩涂摄影天堂)、贵州荔波(地球腰带上的绿宝石)、四川色达(红色佛国)、甘肃扎尕那(东方伊甸园)、浙江松阳(最后的江南秘境)。趁它们还没火起来，赶紧去！",
                IMAGES[22], "旅行", "冷门景点猎人", AVATARS[3], 89000, 1150, 400, "05-29"));

        // Video posts (postType=2)
        list.add(makeVideoPost(29, "AI改变世界的10种方式",
                "从 ChatGPT 到自动驾驶，从医疗诊断到气候预测，AI 正以前所未有的速度重塑我们的世界。本视频带你了解 AI 在未来十年将如何改变每个人的生活。",
                IMAGES[35], "科技", "科技达人Kevin", AVATARS[3], 92000, 1100, 450, "06-12", "video_keji"));
        list.add(makeVideoPost(30, "重庆火锅探店：藏在巷子里的老味道",
                "跟着本地人吃最地道的重庆火锅！九宫格牛油锅底，鲜毛肚涮10秒，配上蒜泥香油碟，一口下去全是幸福感。",
                IMAGES[12], "美食", "吃货小胖", AVATARS[2], 215000, 3400, 1200, "06-08", "video_meishi"));
        list.add(makeVideoPost(31, "黄金时刻：城市风光摄影技巧",
                "日出日落前后的一小时被称为摄影的「黄金时刻」。本期教你如何利用逆光、剪影和暖色调，拍出刷爆朋友圈的城市大片。",
                IMAGES[32], "摄影", "风光摄影师小北", AVATARS[4], 56000, 780, 290, "06-04", "video_sheying"));
        list.add(makeVideoPost(32, "零基础吉他入门：学会第一首歌",
                "选一把合适的吉他，调准音，学会 C、Am、F、G 四个和弦，10分钟就能弹唱《小星星》。音乐，从来都不晚开始。",
                IMAGES[29], "音乐", "尤克里里教室", AVATARS[9], 45000, 670, 230, "06-02", "video_yinyue"));
        list.add(makeVideoPost(33, "北海道冬日绝美雪景",
                "冬天的北海道简直是童话世界！小樽的雪灯之路、札幌的白色圣诞、富良野的滑雪场、登别的地狱谷温泉。最推荐在露天温泉里看雪花飘落，那种感觉用文字形容不出来。",
                IMAGES[0], "旅行", "小熊旅行日记", AVATARS[0], 168000, 2400, 680, "06-10", "video_lvxing"));
        list.add(makeVideoPost(34, "美妆博主的一日妆容教程",
                "手把手教你画出适合日常出街的自然淡妆！从保湿打底到定妆完成，每一步都有详细讲解。重点在于眼妆——用哑光大地色打底，再加一点珠光提亮眼头，整个眼睛瞬间有神。",
                IMAGES[26], "美妆", "美妆博主Coco", AVATARS[10], 134000, 1800, 520, "06-06", "video_meizhuang"));
        list.add(makeVideoPost(35, "HIIT高强度间歇训练跟练",
                "15分钟燃脂暴汗训练，无需器械在家就能做！包含热身拉伸→高强度间歇→拉伸放松三个阶段。每个动作30秒，休息10秒，跟着视频节奏一起练。坚持一个月，腹部线条明显收紧。",
                IMAGES[5], "运动", "健身达人Leo", AVATARS[5], 156000, 2100, 780, "06-03", "video_yundong"));
        list.add(makeVideoPost(36, "治愈系读书日常 | 周末书单推荐",
                "一个安静的周末午后，窗外下着小雨，泡一杯热茶，翻开一本喜欢的书。本期推荐5本适合周末沉浸式阅读的好书，涵盖小说、散文、心理学，每一本都能让你的心安静下来。",
                IMAGES[37], "阅读", "书虫阿宝", AVATARS[4], 72000, 950, 310, "06-01", "video_yuedu"));

        // Text-only posts (postType=1) - 6 posts
        list.add(makePost(37, 1, "深夜emo：关于30岁的焦虑",
                "30岁生日那晚，一个人在阳台上坐了很久。不是害怕变老，而是突然意识到时间过得比想象中快太多。20岁时觉得30岁好远，现在回头看，十年好像就是一眨眼。\n\n身边的人结婚的结婚，生娃的生娃，买房的买房。而我还在租来的小公寓里吃着外卖追着剧。有时候觉得自己很失败，但转念一想——人生又不是比赛，哪有什么应该的样子。\n\n每个年龄段都有属于它的迷茫，重要的是别停下脚步。",
                "", "生活", "城市独居者", AVATARS[1], 45000, 890, 0, "06-11"));
        list.add(makePost(38, 1, "给刚毕业的自己的10句话",
                "1. 第一份工作不重要，重要的是你学到了什么\n2. 存钱，哪怕每个月只存500块\n3. 学会做饭，外卖吃多了会腻也会穷\n4. 别为了合群而合群，高质量的独处比低质量的社交珍贵\n5. 保持阅读习惯，哪怕每天只读10页\n6. 身体是一切的本钱，别透支\n7. 不要和别人比较，每个人都有自己的时区\n8. 勇敢尝试，20多岁是试错成本最低的年纪\n9. 学会拒绝\n10. 给你爸妈打个电话，他们很想你",
                "", "生活", "过来人的忠告", AVATARS[8], 156000, 3200, 0, "06-09"));
        list.add(makePost(39, 1, "裸辞后我后悔了吗？",
                "上个月裸辞了，很多人问我后不后悔。\n\n说实话，第一个星期特别爽——睡到自然醒，不用挤地铁，不用开无聊的周会。但第二周开始焦虑了：存款在减少，简历投了没回音，朋友圈里大家都在忙而我无所事事。\n\n不过我不后悔。那份工作已经消耗了我所有的热情，每天醒来第一件事就是看还有几天到周末。与其在错误的地方耗尽自己，不如停下来重新想想自己想要什么。\n\n现在每天投简历、学新技能、运动、看书。虽然不确定未来会怎样，但至少现在的我是为自己而活的。",
                "", "职场", "勇敢裸辞人", AVATARS[10], 123000, 2600, 0, "06-07"));
        list.add(makePost(40, 1, "一个内向者的社交指南",
                "作为INTJ型人格，社交对我来说从来不是一件轻松的事。但工作后不得不面对各种社交场合，总结了一些适合内向者的生存法则：\n\n1. 不需要变成外向的人，内向不是缺点\n2. 学会充电——重要的社交活动前给自己留独处时间\n3. 做自己擅长的社交方式——如果聊天不行，就做一个好的倾听者\n4. 提前准备几个话题，避免尬聊\n5. 设定撤退时间，比如参加聚会时告诉自己待1小时就可以走\n6. 不要用酒精壮胆，那只会让你事后更焦虑\n\n内向者的细腻和深度思考，是这个世界非常需要的东西。",
                "", "职场", "安静的力量", AVATARS[9], 67000, 1450, 0, "06-05"));
        list.add(makePost(41, 1, "关于分手后的自我重建",
                "三个月前结束了5年的感情。\n\n最难熬的不是分开那一刻，而是之后的日常——习惯性地想分享今天发生的事，却发现那个人已经不在了；路过一起去过的餐厅，心里会咯噔一下；听到某首歌，眼泪会不自觉地流下来。\n\n但时间真的是最好的药。现在回头看，分手反而让我重新认识了自己——原来我可以一个人看电影、一个人旅行、一个人吃火锅。原来我没有想象中那么脆弱。\n\n如果你也正在经历分手，我想说：难过是正常的，但请相信你会好起来的。在等待伤口愈合的日子里，好好爱自己。",
                "", "情感", "一个在自愈的人", AVATARS[6], 198000, 4500, 0, "06-03"));
        list.add(makePost(42, 1, "什么样的城市值得留下？",
                "北漂5年了，最近一直在思考这个问题。\n\n衡量一个城市值不值得留下，我觉得不是看GDP和房价，而是看这些：\n\n- 凌晨1点你加班结束，还能不能找到一家开着的小面馆\n- 周末想出去走走，有没有不用坐很久车就能到的好去处\n- 能不能在这里找到和你价值观相似的人\n- 下班后除了回家睡觉，还有没有别的生活方式\n- 你和这座城市有没有产生情感联结\n\n我还在找答案。你呢，什么样的城市让你觉得值得留下？",
                "", "城市", "北漂青年日记", AVATARS[3], 78000, 2100, 0, "06-01"));

        return list;
    }

    private static Post makePost(int id, int postType, String title, String content,
                                  String imageUrl, String category, String authorName,
                                  String authorAvatar, int likes, int comments,
                                  int shares, String createTime) {
        Post p = new Post();
        p.id = id;
        p.postType = postType;
        p.title = title;
        p.content = content;
        p.imageUrl = imageUrl;
        p.category = category;
        p.authorName = authorName;
        p.authorAvatar = authorAvatar;
        p.likes = likes;
        p.comments = comments;
        p.shares = shares;
        p.createTime = createTime;
        p.isLiked = false;
        p.isCollected = false;
        p.isDraft = false;
        return p;
    }

    private static Post makeVideoPost(int id, String title, String content,
                                       String imageUrl, String category, String authorName,
                                       String authorAvatar, int likes, int comments,
                                       int shares, String createTime, String videoResource) {
        Post p = makePost(id, 2, title, content, imageUrl, category, authorName,
                authorAvatar, likes, comments, shares, createTime);
        p.videoResource = videoResource;
        return p;
    }

    public static List<Post> getCollectedPosts() {
        List<Post> all = getMockPosts();
        List<Post> collected = new ArrayList<>();
        for (int i = 0; i < Math.min(6, all.size()); i++) {
            Post p = all.get(i);
            p.isCollected = true;
            collected.add(p);
        }
        return collected;
    }

    public static List<Category> getCategories() {
        List<Category> list = new ArrayList<>();
        list.add(new Category(0, "全部", "", 0));
        list.add(new Category(1, "旅行", IMAGES[0], 1287));
        list.add(new Category(2, "美食", IMAGES[1], 956));
        list.add(new Category(3, "阅读", IMAGES[6], 753));
        list.add(new Category(4, "科技", IMAGES[7], 646));
        list.add(new Category(5, "运动", IMAGES[5], 532));
        list.add(new Category(6, "摄影", IMAGES[8], 407));
        list.add(new Category(7, "生活", IMAGES[3], 892));
        list.add(new Category(8, "宠物", IMAGES[23], 345));
        list.add(new Category(9, "美妆", IMAGES[26], 428));
        list.add(new Category(10, "家居", IMAGES[16], 267));
        list.add(new Category(11, "音乐", IMAGES[29], 198));
        return list;
    }

    public static List<Comment> getComments(int postId) {
        List<Comment> list = new ArrayList<>();
        list.add(new Comment(1, postId, "一只小月亮", AVATARS[1], "真的好美啊！想去~", 55, "06-12"));
        list.add(new Comment(2, postId, "旅行家小明", AVATARS[3], "收藏了，下次去打卡！感谢分享", 32, "06-12"));
        list.add(new Comment(3, postId, "美食达人", AVATARS[2], "好的，谢谢分享~", 18, "06-11"));
        list.add(new Comment(4, postId, "科技达人Kevin", AVATARS[3], "博主拍的照片太美了！请问用什么相机？", 12, "06-11"));
        list.add(new Comment(5, postId, "简约生活家", AVATARS[0], "已经种草了，准备下个月去！", 8, "06-10"));
        list.add(new Comment(6, postId, "健身达人Leo", AVATARS[5], "风景好美，感觉站在那里整个人都治愈了", 5, "06-10"));
        list.add(new Comment(7, postId, "猫奴阿喵", AVATARS[7], "好棒的分享！关注了~", 3, "06-09"));
        return list;
    }

    public static List<Message> getMessages() {
        List<Message> list = new ArrayList<>();
        list.add(new Message(1, 0, "系统通知", "欢迎来到Looking！探索你所热爱的生活", "", "今天"));
        list.add(new Message(2, 1, "点赞通知", "有人点赞了你的内容", "", "11:29"));
        list.add(new Message(3, 2, "评论通知", "有人评论了你的内容", "", "10:15"));
        list.add(new Message(4, 3, "粉丝通知", "有人关注了你", "", "昨天"));
        list.add(new Message(5, 4, "一只小月亮", "你的照片拍得好好看，想问你用什么相机拍的呀~", AVATARS[1], "12:30"));
        list.add(new Message(6, 4, "旅行家小明", "看了你的大理攻略，下周就出发！还有什么避坑建议吗？", AVATARS[3], "11:20"));
        list.add(new Message(7, 4, "美食达人", "好的，谢谢分享~下次去成都就按这个吃", AVATARS[2], "昨天"));
        list.add(new Message(8, 4, "猫奴阿喵", "你家猫好可爱！能问一下是什么品种吗？", AVATARS[7], "前天"));
        list.add(new Message(9, 4, "健身达人Leo", "一起约跑吗？我也在北四环", AVATARS[5], "前天"));
        return list;
    }

    public static List<String> getHotSearch() {
        List<String> list = new ArrayList<>();
        list.add("中国最美的10个湖泊");
        list.add("夏日必去的海边城市");
        list.add("如何拍出好看的旅行照片");
        list.add("适合一个人旅行的地方");
        list.add("提升生活幸福感的20件小事");
        list.add("裸辞是一种什么体验");
        list.add("成都火锅店测评");
        list.add("30天腹肌训练挑战");
        return list;
    }

    public static List<String> getBannerImages() {
        List<String> list = new ArrayList<>();
        list.add(IMAGES[10]);
        list.add(IMAGES[0]);
        list.add(IMAGES[2]);
        list.add(IMAGES[4]);
        list.add(IMAGES[11]);
        return list;
    }
}
