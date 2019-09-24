package com.meituan.qa.service;

import com.alibaba.fastjson.JSONObject;
import com.meituan.qa.util.XmlUtil;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ConfigManageService {
    @Autowired
    public XmlUtil xmlUtil;

    public boolean addApartment(String apartment) {
        if (apartment.isEmpty()) {
            return false;
        }

        boolean isAddSucceed = true;

        try {
            xmlUtil.addApartmentToXml(apartment);
        } catch (Exception e) {
            isAddSucceed = false;
        }

        return isAddSucceed;
    }

    public boolean addAppkey(String apartment, String appkeys) {
        if (apartment.isEmpty() || appkeys.isEmpty()) {
            return false;
        }

        boolean isAddSucceed = true;

        try {
            xmlUtil.addAppkeyToXml(apartment, appkeys);
        } catch (Exception e) {
            isAddSucceed = false;
        }

        return isAddSucceed;
    }

    public boolean addApi(String apartment, String appkey, String apis, Boolean isOpt) {
        if (apartment.isEmpty() || appkey.isEmpty() || apis.isEmpty()) {
            return false;
        }

        boolean isAddSucceed = true;

        try {
            xmlUtil.addApiToXml(apartment, appkey, apis, isOpt);
        } catch (Exception e) {
            isAddSucceed = false;
        }

        return isAddSucceed;
    }

    public List<String> getApartment() {
        return xmlUtil.readApartmentInXml();
    }

    public List<JSONObject> getAppkeyAndApi(String apartment) { return xmlUtil.readAppkeyAndApiInXml(apartment); }
}
