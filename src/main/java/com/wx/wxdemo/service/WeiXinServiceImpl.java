package com.wx.wxdemo.service;

import com.alibaba.fastjson.JSONObject;
import com.wx.wxdemo.util.HttpUtil;
import com.wx.wxdemo.util.JsonUtil;
import com.wx.wxdemo.util.SHA1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class WeiXinServiceImpl implements WeiXinService {
    public  static final String wxAppId= "xxx";
    public  static final String wxAppSecreat= "xxx";
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public Map getCfg() throws Exception {
        HashMap<String, Object> resMap = new HashMap<>();
        //获取access_token 有效期7200s
        String access_token = getWxAccessToken();
        log.info("access_token:"+access_token);
        //获取jsapi_ticket 有效期7200s
        String jsapi_ticket = getJsapiTicket(access_token);
        log.info("jsapi_ticket:"+jsapi_ticket);
        //获得jsapi_ticket之后，就可以生成JS-SDK权限验证的签名了。
        // noncestr  16位随机字符串
        //jsapi_ticket
        //timestamp 时间戳
        //url 当前网页的URL，不包含#及其后面部分

        String noncestr= UUID.randomUUID().toString().replace("-","").substring(0,16);
        log.info("noncestr:"+noncestr);
        Long timestamp=new Date().getTime()/1000;
        log.info("timestamp:"+timestamp);
        String url="http://xxx/";//js安全域名
        log.info("url:"+url);
        //对所有待签名参数按照字段名的ASCII 码从小到大排序（字典序）后，使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串
        String string1="jsapi_ticket="+jsapi_ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;
        log.info("string1:"+string1);
        //获取签名
        String signature= null;
        try {
            signature = SHA1.getSHA1(string1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("signature:"+signature);
        resMap.put("appId",wxAppId);
        resMap.put("timestamp",timestamp);
        resMap.put("nonceStr",noncestr);
        resMap.put("signature",signature);
        log.info("res:"+resMap.toString());
        return resMap;
    }


    /**
     * 获取 jsapi_ticket
     * @param access_token
     * @return
     */
    private String getJsapiTicket(String access_token) {
        String wx_jsapi_ticket="";
        //先从缓存中获取wx_access_token 如果有直接返回 没有调用接口获取
        boolean exist_wx_jsapi_ticket = redisUtil.hasKey("wx_jsapi_ticket");
        if(exist_wx_jsapi_ticket){
            wx_jsapi_ticket=(String) redisUtil.get("wx_jsapi_ticket");
            long expire = redisUtil.getExpire("wx_jsapi_ticket");
            if(expire<=300L){//如果过期时间小于5分钟 刷新token
                //获取access_token
                String extra_url="?access_token="+access_token+"&type=jsapi";
                String access_token_url="https://api.weixin.qq.com/cgi-bin/ticket/getticket"+extra_url;
                log.info("请求地址："+access_token_url);
                String res = HttpUtil.httpGet(access_token_url, null);
                log.info("请求结果："+res);
                JSONObject rsObj = JsonUtil.toObject(res, JSONObject.class);
                String ticket = (String) rsObj.get("ticket");
                Long expires_in = Long.parseLong(rsObj.get("expires_in").toString());
                Integer errcode = (Integer) rsObj.get("errcode");
                String errmsg = (String) rsObj.get("errmsg");
                if(ticket!=null){
                    wx_jsapi_ticket=ticket;
                    redisUtil.del("wx_jsapi_ticket");
                    redisUtil.set("wx_jsapi_ticket", wx_jsapi_ticket, expires_in);
                    return wx_jsapi_ticket;
                }else if(errcode!=0){
                    log.error("获取access_token异常，errcode="+errcode+",errmsg="+errmsg);
                }

            }
            return wx_jsapi_ticket;
        }else {
            //获取access_token
            String extra_url="?access_token="+access_token+"&type=jsapi";
            String access_token_url="https://api.weixin.qq.com/cgi-bin/ticket/getticket"+extra_url;
            log.info("请求地址："+access_token_url);
            String res = HttpUtil.httpGet(access_token_url, null);
            log.info("请求结果："+res);
            JSONObject rsObj = JsonUtil.toObject(res, JSONObject.class);
            String ticket = (String) rsObj.get("ticket");
            Long expires_in = Long.parseLong(rsObj.get("expires_in").toString());
            Integer errcode = (Integer) rsObj.get("errcode");
            String errmsg = (String) rsObj.get("errmsg");
            if(ticket!=null){
                wx_jsapi_ticket=ticket;
                redisUtil.set("wx_jsapi_ticket", wx_jsapi_ticket, expires_in);
            }else if(errcode!=1){
                log.error("获取access_token异常，errcode="+errcode+",errmsg="+errmsg);
            }
        }
        return wx_jsapi_ticket;
    }

    /**
     * 获取微信accessToken
     * @return
     */
    private String getWxAccessToken() {
        String wx_access_token="";
        //先从缓存中获取wx_access_token 如果有直接返回 没有调用接口获取
        boolean exist_wx_access_token = redisUtil.hasKey("wx_access_token");
        if(exist_wx_access_token){
            wx_access_token=(String) redisUtil.get("wx_access_token");
            long expire = redisUtil.getExpire("wx_access_token");
            if(expire<=300L){//如果过期时间小于5分钟 刷新token
                //获取access_token
                String extra_url="?grant_type=client_credential&appid="+wxAppId+"&secret="+wxAppSecreat;
                String access_token_url="https://api.weixin.qq.com/cgi-bin/token"+extra_url;
                log.info("请求地址："+access_token_url);
                String res = HttpUtil.httpGet(access_token_url, null);
                log.info("请求结果："+res);
                JSONObject rsObj = JsonUtil.toObject(res, JSONObject.class);
                String access_token = (String) rsObj.get("access_token");
                Long expires_in = Long.parseLong(rsObj.get("expires_in").toString()) ;
                String errcode = (String) rsObj.get("errcode");
                String errmsg = (String) rsObj.get("errmsg");
                if(access_token!=null){
                    wx_access_token=access_token;
                    redisUtil.del("wx_access_token");
                    redisUtil.set("wx_access_token", wx_access_token, expires_in);
                    return wx_access_token;
                }else if(errcode!=null){
                    log.error("获取access_token异常，errcode="+errcode+",errmsg="+errmsg);
                }

            }
            return wx_access_token;
        }else {
            //获取access_token
            String extra_url="?grant_type=client_credential&appid="+wxAppId+"&secret="+wxAppSecreat;
            String access_token_url="https://api.weixin.qq.com/cgi-bin/token"+extra_url;
            log.info("请求地址："+access_token_url);
            String res = HttpUtil.httpGet(access_token_url, null);
            log.info("请求结果："+res);
            JSONObject rsObj = JsonUtil.toObject(res, JSONObject.class);
            String access_token = (String) rsObj.get("access_token");
            Long expires_in = Long.parseLong(rsObj.get("expires_in").toString());
            String errcode = (String) rsObj.get("errcode");
            String errmsg = (String) rsObj.get("errmsg");
            if(access_token!=null){
                wx_access_token=access_token;
                redisUtil.set("wx_access_token", wx_access_token, expires_in);
            }else if(errcode!=null){
                log.error("获取access_token异常，errcode="+errcode+",errmsg="+errmsg);
            }
        }
        return wx_access_token;
    }
}
