package com.meituan.qa.schedule;

import com.meituan.qa.service.DataWriteService;
import com.meituan.qa.util.DateUtil;
import com.meituan.qa.util.XmlUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class PullDataTask extends TimerTask {

    @Resource
    DataWriteService dataWriteService;

    @Autowired
    XmlUtil xmlUtil;

    @Override
    public void run() {
        Date date = new Date();
        List<String> apartmentList = xmlUtil.readApartmentInXml();

        for(int i = 0; i < 30; i++) {
            String dataTime = DateUtil.dateToStr(date);
            for (String apa : apartmentList) {
                dataWriteService.pullOctoDataByUrl(apa, "https://octo.sankuai.com/data/performance?appkey=%s&day=%s&env=prod&source=server", dataTime);
            }

        }
    }
}
