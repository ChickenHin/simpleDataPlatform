package com.meituan.qa.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meituan.qa.domain.mapper.ApiOctoDataMapper;
import com.meituan.qa.domain.mapper.AppkeyOctoDataMapper;
import com.meituan.qa.util.DateUtil;
import com.meituan.qa.util.HttpClientUtil;
import com.meituan.qa.util.XmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class DataWriteService {

    @Resource
    private AppkeyOctoDataMapper appkeyOctoDataMapper;

    @Resource
    private ApiOctoDataMapper apiOctoDataMapper;

    @Autowired
    public XmlUtil xmlUtil;


    public boolean pullOctoDataByUrl(String apartment, String url, String date) {
        try {
            List<String> appkeyList = xmlUtil.readAppkeyInXml(apartment);

            Map<String, JSONArray> appkeyMapData = new HashMap<>();

            for (String appkey : appkeyList) {
                String appkeyUrl = String.format(url, appkey, date);

                JSONObject response = HttpClientUtil.doGet(appkeyUrl);

                appkeyMapData.put(appkey, response.getJSONArray("data"));
            }

            insertAppkeyOctoDataInDB(appkeyMapData, date);
            insertApiOctoDataInDB(appkeyMapData, date);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean pullOctoDataByUrlInRange(String apartment, String url, String startTime, String endTime) {
        try {
            List<String> appkeyList = xmlUtil.readAppkeyInXml(apartment);

            Map<String, JSONArray> appkeyMapData = new HashMap<>();

            Date startDate = DateUtil.strToDate(startTime);
            Date endDate = DateUtil.strToDate(endTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            while (calendar.getTime().getTime() <= endDate.getTime()) {
                String date = DateUtil.dateToStr(calendar.getTime());
                for (String appkey : appkeyList) {
                    String appkeyUrl = String.format(url, appkey, date);

                    JSONObject response = HttpClientUtil.doGet(appkeyUrl);

                    appkeyMapData.put(appkey, response.getJSONArray("data"));
                }

                insertAppkeyOctoDataInDB(appkeyMapData, date);
                insertApiOctoDataInDB(appkeyMapData, date);
                calendar.add(Calendar.DATE, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void insertAppkeyOctoDataInDB(Map<String, JSONArray> appkeyMapData, String date) {
        for(String appkey : appkeyMapData.keySet()) {
            JSONObject data = appkeyMapData.get(appkey).getJSONObject(0);
            double successrate = Double.parseDouble(data.getString("successCountClientPer").replace("%", ""));
            int count = data.getInteger("count");

            JSONObject tp = data.getJSONObject("topPercentile");
            int tp95 = tp.getInteger("upper95");
            int tp99 = tp.getInteger("upper99");

            appkeyOctoDataMapper.insertAppkeyOctoData(appkey, tp95, tp99, successrate, count, date);
        }

    }

    private void insertApiOctoDataInDB(Map<String, JSONArray> appkeyMapData, String date) {
        for(String appkey : appkeyMapData.keySet()) {
            JSONArray datas = appkeyMapData.get(appkey);

            for (int i = 0; i < datas.size(); i++) {
                JSONObject data = datas.getJSONObject(i);
                String api = data.getString("spanname");

                JSONObject tp = data.getJSONObject("topPercentile");
                int tp95 = tp.getInteger("upper95");
                int tp99 = tp.getInteger("upper99");

                apiOctoDataMapper.insertApiOctoData(api, tp95, tp99, date);
            }
        }
    }

//    public static void main(String[] a) {
//        DataWriteService test = new DataWriteService();
//
//        List<String> apartmentList = XmlUtil.readApartmentInXml();
//
//        Calendar cal = Calendar.getInstance();
//
//        for (String apa : apartmentList) {
//            cal.set(2018, 10, 1, 0, 0, 0);
//            String dataTime;
//            for (int i = 0; i <= 100; i++) {
//
//                dataTime = DateUtil.dateToStr(cal.getTime());
//                System.out.println("dataTime" + dataTime);
//                test.pullOctoDataByUrl(apa, "https://octo.sankuai.com/data/performance?appkey=%s&day=%s&env=prod&source=server", dataTime);
//
//                cal.add(Calendar.DATE, 1);
//            }
//        }
//    }
}
