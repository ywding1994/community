package com.ywding1994.community.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ywding1994.community.service.DataService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@Api(tags = "网站数据统计接口")
public class DataController {

    @Resource
    private DataService dataService;

    @RequestMapping(path = "/data", method = { RequestMethod.GET, RequestMethod.POST })
    @ApiOperation(value = "请求网站数据统计页面")
    public String getDataPage() {
        return "/site/admin/data";
    }

    @RequestMapping(path = "/data/uv", method = RequestMethod.POST)
    @ApiOperation(value = "统计网站UV", httpMethod = "POST")
    public String getUV(Model model,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam @ApiParam("起始日期") Date start,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam @ApiParam("终止日期") Date end) {
        long uv = dataService.calculateUV(start, end);
        model.addAttribute("uvResult", uv);
        model.addAttribute("uvStartDate", start);
        model.addAttribute("uvEndDate", end);
        return "forward:/data";
    }

    @RequestMapping(path = "/data/dau", method = RequestMethod.POST)
    @ApiOperation(value = "统计网站DAU", httpMethod = "POST")
    public String getDAU(Model model,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam @ApiParam("起始日期") Date start,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam @ApiParam("终止日期") Date end) {
        long dau = dataService.calculateDAU(start, end);
        model.addAttribute("dauResult", dau);
        model.addAttribute("dauStartDate", start);
        model.addAttribute("dauEndDate", end);
        return "forward:/data";
    }

}
