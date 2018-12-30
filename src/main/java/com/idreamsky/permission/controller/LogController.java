package com.idreamsky.permission.controller;


import com.idreamsky.permission.beans.PageQuery;
import com.idreamsky.permission.common.JsonData;
import com.idreamsky.permission.param.SearchLogParam;
import com.idreamsky.permission.service.LogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author colby
 * @since 2018-12-15
 */
@Controller
@RequestMapping("/sys/log")
public class LogController {
    @Resource
    private LogService logService;

    @RequestMapping("/log.html")
    public String page(){
        return "log";
    }

    @RequestMapping("/page")
    @ResponseBody
    public JsonData searchPage(SearchLogParam param, PageQuery pageQuery){
        return JsonData.success(logService.searchPageList(param,pageQuery));
    }

    @RequestMapping("/recover")
    @ResponseBody
    public JsonData recover(@RequestParam("id") Integer logId) {
        logService.recover(logId);
        return JsonData.success();
    }
}
