package com.meituan.qa.controller;

import com.alibaba.fastjson.JSONObject;
import com.meituan.qa.bean.ApiData;
import com.meituan.qa.bean.AppkeyData;
import com.meituan.qa.service.ConfigManageService;
import com.meituan.qa.service.DataReadService;
import com.meituan.qa.service.DataWriteService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class ApiController {

    @Resource
    ConfigManageService configManageService;

    @Resource
    DataReadService dataReadService;

    @Resource
    DataWriteService dataWriteService;

    @RequestMapping(value = "/addApartment", method = RequestMethod.POST)
    public boolean addApartment(@RequestParam("apartment") String apartment) {
        if (apartment.isEmpty()) {
            return false;
        }
        return configManageService.addApartment(apartment);
    }

    @RequestMapping(value = "/addAppkey", method = RequestMethod.POST)
    public boolean addAppkey(@RequestParam(value = "apartment", required = true) String apartment, @RequestParam(value = "appkey", required = true) String appkey) {
        if (apartment.isEmpty() || appkey.isEmpty()) {
            return false;
        }
        return configManageService.addAppkey(apartment, appkey);
    }

    @RequestMapping(value = "/addApi", method = RequestMethod.POST)
    public boolean addApi(@RequestParam(value = "apartment", required = true) String apartment, @RequestParam(value = "appkey", required = true) String appkey, @RequestParam(value = "api", required = true) String api, @RequestParam(value = "isOpt", required = false, defaultValue = "false") Boolean isOpt) {
        if (appkey.isEmpty() || api.isEmpty()) {
            return false;
        }
        return configManageService.addApi(apartment, appkey, api, isOpt);
    }

    @RequestMapping(value = "/getApartment", method = RequestMethod.GET)
    public List<String> getApartment() {
        return configManageService.getApartment();
    }

    @RequestMapping(value = "/getAppkeyAndApi", method = RequestMethod.GET)
    public List<JSONObject> getAppkeyAndApi(@RequestParam("apartment") String apartment) {
        return configManageService.getAppkeyAndApi(apartment);
    }

    @RequestMapping(value = "/getAppkeyOctoData", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, List<AppkeyData>> getAppkeyOctoData(@RequestParam("apartment") String apartment, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        if (apartment.isEmpty()) {
            return null;
        }
        return dataReadService.getAppkeyOctoData(apartment, startTime, endTime);
    }

    @RequestMapping(value = "/getApiOctoData", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, List<ApiData>> getApiOctoData(String apartment, String startTime, String endTime) {
        if (apartment.isEmpty()) {
            return null;
        }
        return dataReadService.getApiOctoData(apartment, startTime, endTime);
    }

    @RequestMapping(value = "/getOptApiOctoData", method = RequestMethod.GET)
    public Map<String, List<ApiData>> getOptApiOctoData(String apartment, String startTime, String endTime) {
        if (apartment.isEmpty()) {
            return null;
        }
        return dataReadService.getOptApiOctoData(apartment, startTime, endTime);
    }

    @RequestMapping(value = "/pullOctoData", method = RequestMethod.GET)
    public boolean pullOctoData(@RequestParam("apartment") String apartment, @RequestParam("date") String date) {
        System.out.println("参数： " + apartment + date);
        if (apartment.isEmpty()) {
            return false;
        } else {
            return dataWriteService.pullOctoDataByUrl(apartment, "https://octo.sankuai.com/data/performance?appkey=%s&day=%s&env=prod&source=server", date);
        }
    }

    @RequestMapping(value = "/pullOctoDataInRange", method = RequestMethod.GET)
    public boolean pullOctoDataInRange(@RequestParam("apartment") String apartment, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
        System.out.println("参数： " + apartment + startTime + '-' + endTime);
        if (apartment.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
            return false;
        } else {
            return dataWriteService.pullOctoDataByUrlInRange(apartment, "https://octo.sankuai.com/data/performance?appkey=%s&day=%s&env=prod&source=server", startTime, endTime);
        }
    }
}
