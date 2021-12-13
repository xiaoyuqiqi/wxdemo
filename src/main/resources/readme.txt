此项目是为分享微信朋友圈等微信网页开发提供配置创建的，
配置信息地址参考：https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/JS-SDK.html#3
即：config接口注入权限验证配置 的信息

项目启动之前有一下几个地方需要修改：
1.com.wx.wxdemo.service.WeiXinServiceImpl中签名是用到的安全域名,
  String url="http://xxx/";//js安全域名

  public  static final String wxAppId= "xxx";
  public  static final String wxAppSecreat= "xxx";


注意：
对安全域名加密时的url即String url="http://xxx/";  最后面的斜杠不要忘记

签名算法校验地址：
https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=jsapisign

开发者测试账号配置地址：https://mp.weixin.qq.com/debug/cgi-bin/sandboxinfo?action=showinfo&t=sandbox/index
