package com.meituan.qa.service;

import com.meituan.qa.bean.ApiData;
import com.meituan.qa.bean.AppkeyData;
import com.meituan.qa.bean.AppkeyOctoData;
import com.meituan.qa.domain.mapper.ApiOctoDataMapper;
import com.meituan.qa.domain.mapper.AppkeyOctoDataMapper;
import com.meituan.qa.util.MapUtil;
import com.meituan.qa.util.XmlUtil;
import com.meituan.qa.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class DataReadService {
    @Resource
    private AppkeyOctoDataMapper appkeyOctoDataMapper;

    @Resource
    private ApiOctoDataMapper apiOctoDataMapper;

    @Autowired
    public XmlUtil xmlUtil;

    public Map<String, List<AppkeyData>> getAppkeyOctoData(String apartment, String startTime, String endTime) {

        Map<String, List<AppkeyData>> appkeyMapData = new HashMap<>();
        List<String> appkeyList = xmlUtil.readAppkeyInXml(apartment);

        for (String appkey : appkeyList) {
            // 一个appkey按时间聚合的数据
            List<AppkeyOctoData> listByDatatime = appkeyOctoDataMapper.selectAppkeyOctoData(appkey, startTime, endTime);

            // 一个appkey的所有数据按时间做映射
            Map<String, AppkeyData> timeMapVals = new HashMap<>();
            for(AppkeyOctoData l : listByDatatime) {
                String timeKey = l.getDatatime();
                if (!timeMapVals.containsKey(timeKey)) {
                    AppkeyData appkeyData = new AppkeyData();
                    appkeyData.setTp95(l.getTp95());
                    appkeyData.setTp99(l.getTp99());
                    appkeyData.setSuccessrate(l.getSuccessrate());
                    appkeyData.setVisitCount(l.getVisitCount());

                    timeMapVals.put(timeKey, appkeyData);
                }
            }

            // 查找在时间区间内哪一天的数据不存在
            Date startDate = DateUtil.strToDate(startTime);
            Date endDate = DateUtil.strToDate(endTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            Date dateTmp = startDate;
            while(dateTmp.getTime() < endDate.getTime()) {
                dateTmp = calendar.getTime();
                String strTmp = DateUtil.dateToStr(dateTmp);
                if (!timeMapVals.containsKey(strTmp)) {
                    timeMapVals.put(strTmp, new AppkeyData());
                }
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            // map按时间排序，得到按时间排序的数据list
            Map<String, AppkeyData> sortTimeMapVals = MapUtil.sortMapByKey(timeMapVals);
            List<AppkeyData> appkeyDataList = new ArrayList<>();
            for (Map.Entry<String, AppkeyData> entry : sortTimeMapVals.entrySet()) {
                appkeyDataList.add(entry.getValue());
            }

            appkeyMapData.put(appkey, appkeyDataList);
        }

        return appkeyMapData;
    }
//TODO 没有的数据存成0
    public Map<String, List<ApiData>> getApiOctoData(String apartment, String startTime, String endTime) {

        Map<String, List<ApiData>> apiMapData = new HashMap<>();
        List<String> apiList = xmlUtil.readApiInXml(apartment);

        for(String api : apiList) {
            List<ApiData> listByDatatime = apiOctoDataMapper.selectApiOctoData(api, startTime, endTime);
            apiMapData.put(api, listByDatatime);
        }

        return apiMapData;
    }

    public Map<String, List<ApiData>> getOptApiOctoData(String apartment, String startTime, String endTime) {

        Map<String, List<ApiData>> apiMapData = new HashMap<>();
        List<String> apiList = xmlUtil.readOptApiInXml(apartment);

        for(String api : apiList) {
            List<ApiData> listByDatatime = apiOctoDataMapper.selectApiOctoData(api, startTime, endTime);
            apiMapData.put(api, listByDatatime);
        }

        return apiMapData;
    }
}
