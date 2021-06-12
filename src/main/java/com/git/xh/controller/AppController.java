package com.git.xh.controller;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.git.xh.Aspect.Annotation.loginCheck;
import com.git.xh.entity.MusicInfo;
import com.git.xh.entity.WxUser;
import com.git.xh.service.MusicinfoService;
import com.git.xh.service.WxuserService;
import com.git.xh.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.codehaus.xfire.util.Base64;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author heqi
 */
@RestController
@RequestMapping("/app")
@Slf4j
public class AppController {
//    public static final Map<String,String> songMap = new HashMap<>();
//    public static final ArrayList<String> singerList = new ArrayList<>();
    public static final ArrayList<String> songList = new ArrayList<>();

    @Autowired
    @Resource
    private RedisTemplate redisTemplate;
    @Value("${spring.ids.appId}")
    private String app_id;
    @Value("${spring.ids.secret}")
    private String secret_id;
    @Value("${spring.ids.name}")
    private String name;
    @Value("${spring.ids.password}")
    private String pwd;

    @Autowired
    private MusicinfoService musicinfoService;

    @Autowired
    private WxuserService wxuserService;

//    static {
//        songList.add("小众情人");
//        songList.add("高山低谷");
//        songList.add("等你下课");
//        songList.add("黑色毛衣");
//        songList.add("一路向北");
//        songList.add("彩虹");
//        songList.add("夜曲");
//        songList.add("阿拉斯加海湾");
//        songList.add("Touch");
//        songList.add("吴哥窟");
//        songList.add("给我一个理由忘记");
//        songList.add("幸福了然后呢");
//        songList.add("会痛的石头");
//        songList.add("海芋恋");
//        songList.add("我还想她");
//        songList.add("野孩子");
//        songList.add("给你呀");
//        songList.add("水星记");
//        songList.add("大雾");
//        songList.add("安和桥");
//        songList.add("红色高跟鞋");
//        songList.add("晚婚");


//        songMap.put("小众情人","林奕匡");
//        songMap.put("高山低谷","林奕匡");
//
//        songMap.put("等你下课","周杰伦");
//        songMap.put("黑色毛衣","周杰伦");
//        songMap.put("一路向北","周杰伦");
//        songMap.put("彩虹","周杰伦");
//        songMap.put("夜曲","周杰伦");
//
//        songMap.put("阿拉斯加海湾","蓝心雨");
//
//        songMap.put("Touch","3CU");
//
//        songMap.put("吴哥窟","吴雨霏");
//
//        songMap.put("给我一个理由忘记","A-lin");
//        songMap.put("幸福了然后呢","A-lin");
//
//        songMap.put("会痛的石头","萧敬腾");
//        songMap.put("海芋恋","萧敬腾");
//
//        songMap.put("我还想她","林俊杰");
//
//        songMap.put("野孩子","杨千嬅");
//
//        songMap.put("给你呀","蒋小呢");
//
//        songMap.put("水星记","郭顶");
//
//        songMap.put("大雾","张一乔");
//
//        songMap.put("安和桥","宇西");
//
//        songMap.put("红色高跟鞋","蔡健雅");
//
//        songMap.put("晚婚","江蕙");


//        singerList.add("林奕匡");
//        singerList.add("周杰伦");
//        singerList.add("林俊杰");
//        singerList.add("萧敬腾");
//        singerList.add("蓝心雨");
//        singerList.add("吴雨霏");
//        singerList.add("3CU");
//        singerList.add("杨千嬅");
//        singerList.add("蒋小呢");
//        singerList.add("郭顶");
//        singerList.add("张一乔");
//        singerList.add("蔡健雅");
//        singerList.add("宇西");
//        singerList.add("江蕙");
//        singerList.add("A-lin");


//    }

    @PostMapping("/search")
    public List<MusicInfo> search(@RequestParam("key") String key){
        ArrayList<MusicInfo> list = new ArrayList<>();
//        Map<String,String> map = null;
        // 搜索歌手
//        if (singerList.contains(key)) {
//            map = redisTemplate.opsForHash().entries(key);
//            if (map!=null){
//                map.forEach((K,V)->{
//                    MusicInfo info = new MusicInfo(key,K,V);
//                    list.add(info);
//                });
//            }
//        }
//        // 搜索的是歌曲,只能是单曲
//        if (songList.contains(key)){
//            String singer = songMap.get(key);
//            String songAddr = (String)redisTemplate.opsForHash().get(songMap.get(key), key);
//            if (songAddr!=null){
//                MusicInfo musicInfo = new MusicInfo(singer,key,songAddr);
//                list.add(musicInfo);
//            }
//        }
        // 如果是歌名
        MusicInfo o = (MusicInfo)redisTemplate.opsForValue().get(key);
        if (o!=null){
            list.add(o);
        }

        if (o==null){
            List<MusicInfo> info = musicinfoService.findMusic(key);
            list.addAll(info);
        }
        return list;
    }


    @loginCheck
    @PostMapping("/randomPlay")
    public List<MusicInfo> randomPlay(){
        int i = RandomUtil.getRandomInt(songList.size());
        return this.getMusic(i);
    }

    @loginCheck
    @PostMapping("/CutMusic")
    public List<MusicInfo> cutMusic(@RequestParam("song") String name, @RequestParam("type") String type){
        int index = -2;
//        Object[] songs = songMap.keySet().toArray();
//        for (int i = 0; i < songs.length; i++) {
//            if (name.equals(songs[i])){
//                index = i;
//                break;
//            }
//        }

        for (int i = 0; i < songList.size(); i++) {
            if (name.equals(songList.get(i))){
                index = i;
                break;
            }
        }
        if ("next".equals(type)){
            if (++index==songList.size()){
                index = 0;
            }
        }else {
            if (--index==-1){
                index = songList.size()-1;
            }
        }
        return this.getMusic(index);
    }

    @PostMapping("/tops")
    public ArrayList<MusicInfo> tops(){
        Set<DefaultTypedTuple> tops = redisTemplate.opsForZSet().reverseRangeByScoreWithScores("music", 0, Double.MAX_VALUE);
        ArrayList<MusicInfo> list = new ArrayList<>();
        if (tops!=null){
            tops.forEach(
                typedTuple -> {
                    String song = (String)typedTuple.getValue();
//                    String singer = songMap.get(song);
                    MusicInfo musicInfo = (MusicInfo)redisTemplate.opsForValue().get(song);
//                    String songAddr = (String)redisTemplate.opsForHash().get(singer, song);
//                    list.add(new MusicInfo(singer,song,songAddr,typedTuple.getScore()));
                    list.add(musicInfo);
                }
            );
        }
        return list;
    }


    private List<MusicInfo> getMusic(int i){
        List<MusicInfo> list = new ArrayList<>();
//        String song = (String)songMap.keySet().toArray()[i];
//
//        String singer = songMap.get(song);
//        String songAddr = (String)redisTemplate.opsForHash().get(singer, song);
//        if (songAddr!=null&&!"".equals(songAddr)){
//            list.add(new MusicInfo(singer,song,songAddr));
//        }
        String song = songList.get(i);
        MusicInfo musicInfo = (MusicInfo)redisTemplate.opsForValue().get(song);
        if (musicInfo!=null){
            list.add(musicInfo);
        }
        return list;
    }

//    @GetMapping("/getAllMusicIddex")
//    public int getAllMusicIddex(){
//        return songMap.size();
//    }

    @PostMapping("/toLogin")
    public WxUser login(@RequestBody String infoData){
        JSONObject dataObj = JSONObject.parseObject(infoData);
        String  code =  (String) dataObj.get("code");
        JSONObject appIdObj = this.getOpenData(code);
        String openid = (String)appIdObj.get("openid");
        JSONObject user = (JSONObject)dataObj.get("userInfo");
        user.put("openId",openid);
        WxUser wxUser = JSONObject.toJavaObject(user, WxUser.class);

//        userMap.put(openid,wxUser);
//        redisTemplate.opsForValue().set(openid,wxUser);

        wxuserService.add(wxUser);
        redisTemplate.opsForValue().set(openid,wxUser);

        return wxUser;
    }


    /**
     *  getUserInfo 获取用户信息
     */
    @PostMapping("/onLogin")
    public WxUser old_login(@RequestBody String infoData){
        JSONObject dataObj = JSONObject.parseObject(infoData);
        String  code =  (String) dataObj.get("code");
        String  encryptedData =  (String) dataObj.get("encryptedData");
        String  iv =  (String) dataObj.get("iv");
        JSONObject appIdObj = this.getOpenData(code);
        String sessionKey = (String) appIdObj.get("session_key");
        JSONObject userInfo = this.getUserInfo(encryptedData, sessionKey, iv);
        return JSONObject.toJavaObject(userInfo, WxUser.class);
    }


    @PostMapping("/getWxUser")
    public WxUser getWxUser(@RequestParam("code") String code){
        JSONObject object = this.getOpenData(code);
//        WxUser user = userMap.get(object.getString("openid"));
//        if (user==null){
//            user = (WxUser)redisTemplate.opsForValue().get(object.getString("openid"));
//        }
        WxUser user = (WxUser)redisTemplate.opsForValue().get(object.getString("openid"));
        if (user==null){
            user = wxuserService.getWxuser(object.getString("openid"));
        }
        return user;
    }


    public JSONObject getOpenData(String code){
        String appId=app_id;
        String secret=secret_id;
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+appId+"&secret="+secret+"&js_code="+code+"&grant_type=authorization_code";
        String res= HttpUtil.get(url);
        return JSONObject.parseObject(res);
    }

    /**
     * 获取信息
     */
    public JSONObject getUserInfo(String encryptedData,String sessionKey,String iv){
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param musicInfo
     * 更新歌曲score
     */
    @PostMapping("/UpdateMusicScore")
    @loginCheck
    public List<MusicInfo> UpdateMusicScore(@RequestBody MusicInfo musicInfo){
//        Double score = musicInfo.getScore();
//        if (score==null) {
//            score = redisTemplate.opsForZSet().score("music", musicInfo.getSong());
//        }
//        redisTemplate.opsForZSet().add("music",musicInfo.getSong(),++score);
        ArrayList<MusicInfo> list = new ArrayList<>();
        musicinfoService.updateMusic(musicInfo);
        list.add(musicInfo);
        return list;
    }



    /**
     * @param musicInfo
     * 更新歌曲score
     */

    @PostMapping("/LikedMusic")
    public void LikedMusic(@RequestParam("musicInfo") String musicInfo,@RequestParam("userInfo") String wxUser,@RequestParam("type") String type){

        musicinfoService.LikedMusic(musicInfo,wxUser,type);
    }

    @GetMapping("/getUserAllLiked")
    public List<MusicInfo> getUserAllLiked(@RequestParam("userId") String userId){
        List<MusicInfo> list = new ArrayList<>();
        if (!StringUtils.isEmpty(userId)){
            list = musicinfoService.findUserAllLiked(userId);
        }
        return list;
    }

    @PostMapping("/listenTogether")
    public MusicInfo listenTogether(){
        int i = RandomUtil.getRandomInt(songList.size());
        MusicInfo info = this.getMusic(i).get(0);
        try {
            Boolean together = redisTemplate.opsForValue().setIfAbsent("listenTogether", Thread.currentThread().getId(), 60, TimeUnit.SECONDS);
            if (together){
                redisTemplate.opsForList().leftPush("musicTogether",info);
                redisTemplate.expire("musicTogether",60,TimeUnit.SECONDS);
                while (true){
                    if (redisTemplate.opsForList().size("musicTogether")==0){
                        break;
                    }
                    Thread.sleep(100);
                }
                return info;
            }else {
                info = (MusicInfo)redisTemplate.opsForList().rightPop("musicTogether");
                while (true){
                    if (info!=null){
                        break;
                    }
                    Thread.sleep(150);
                }
                return info;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
        }
        return null;
    }

    @PostMapping("/deleteKey")
    public void deleteKey(){
        redisTemplate.delete("musicTogether");
    }


//    @PostConstruct
    @Async("cron=0 0 0 * * ? ")
    public void updateMusicInfoToRedis(){
        List<MusicInfo> list = musicinfoService.getAllMusic();
        List<WxUser> wxUsers = wxuserService.getAllWxuser();
        list.forEach(
            i->{
                songList.add(i.getSong());
                redisTemplate.opsForValue().set(i.getSong(),i);
                redisTemplate.opsForZSet().add("music",i.getSong(),i.getScore());
            }
        );

        wxUsers.forEach(
                u->{
                    redisTemplate.opsForValue().set(u.getOpenId(),u);
                }
        );
        log.info("更新歌曲、用户信息到redis成功");
    }

    @PostMapping("/xxx")
    public void xxx(){
        songList.forEach(
                i->{
                    MusicInfo info = (MusicInfo)redisTemplate.opsForValue().get(i);
                    musicinfoService.addMusic(info);
                }
        );
    }

    @PostMapping("/system")
    public Boolean xh(@RequestBody String musicInfo){
        JSONObject object = JSONObject.parseObject(musicInfo);
        if (name.equals(object.get("username"))&&pwd.equals(object.getString("password"))){
            String singer = (String)object.get("singer");
            String song = (String)object.get("song");
            String songAddr = (String)object.get("songAddr");
            Double score = Double.valueOf((String) object.get("score"));
            MusicInfo info = new MusicInfo(singer, song, songAddr, score);
            musicinfoService.addMusic(info);
            return true;
        }
        return false;
    }

    // 本小程序是一款简易音乐播放器，实现了音乐播放器的基本功能。
    // 包括用户授权登录。
    // 首页头像转动，切换歌曲 暂停继续播放。
    // 排行榜页热度排行，点击播放。每天凌晨四点定时更新排行榜
    // 搜索页通过搜索歌曲、歌手进行单曲播放。
    // 管理员上传歌曲等。

    // wxss,js,wxml  springboot redis mybatis-plus mysql 阿里oss

    /**
     *  问题1： 刚开始工作的时候在一次做操作日志的时候，使用aop切入controller层没有生效，
     *          在业务层切入却能生效，通过百度发现需要给mvc配置文件加入自动代理的配置。
     *          后来才明白，当spring加载父容器的时候就会去找切入点，但是这个时候切入的
     *          controller是在子容器中的，父容器是无法访问子容器，所以就无法进行增强。
     *
     *
     *  问题2： 有一次在做文件上传时，后台怎么都接收不到文件，检查了文件上传解析器的配置，
     *          都没有问题，在我本地同样的代码能接收到文件。最后查看web.xml 发现里
     *          面配置了一个MultipartFilter文件上传过滤器，debug发现这个过滤器会尝试从
     *          ioc取一个名叫filterMultipartResolver的文件上传解析器，如果没有取到就使用
     *          StandardServletMultipartResolver 标准的文件上传解析器解析请求。
     *          而mvc配置文件中配置的是CommonsMultipartResolver 公共的文件上传解析器。
     *          最后将MultipartFilter注释掉解决问题。
     *
     * 问题3：  有一次在做表单提交的时候，后台功能正常，ajax却报错。请求状态变为cancel。
     *          就开始猜测是不是异步导致。把ajax异步改成同步后能正常执行。后来百度，
     *          有人说form表单内使用button提交。使得form的action与button产生冲突，
     *          后续将button移至form外，使用异步也能正常执行。
     *
    */
}
