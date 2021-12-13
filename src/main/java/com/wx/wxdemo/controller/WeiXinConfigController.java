package com.wx.wxdemo.controller;

import com.wx.wxdemo.service.WeiXinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@Api(tags = "微信分享配置信息")
@RestController
public class WeiXinConfigController {

    @Autowired
    WeiXinService weiXinService;

    @ApiOperation(value = "获取微信分享配置信息", notes = "")
    @RequestMapping(value = "/cfg", method = RequestMethod.GET)
    public Map activityRecords(HttpServletRequest request) throws Exception {
        log.info("获取配置信息");
        Map map = weiXinService.getCfg();
        return map;
    }
}
