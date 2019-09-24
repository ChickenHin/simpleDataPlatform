package com.meituan.qa.util;

import com.alibaba.fastjson.JSONObject;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class XmlUtil {

    public void createXml() throws DocumentException {
        Document document = DocumentHelper.createDocument();
        document.addElement("config");

        writeXml(document);
    }

    public Document readXml() {
//        InputStream stream = getClass().getClassLoader().getResourceAsStream("config.xml");
        InputStream stream = this.getClass().getResourceAsStream("/config.xml");
//        File stream = new File("/Users/zhangxin/Downloads/indexMonitor/target/classes/config.xml");
        if (stream != null) {
            try {
                SAXReader reader = new SAXReader();
                Document document = reader.read(stream);
                return document;
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private void writeXml(Document document) {
        try {
            String configPath = System.getProperty("user.dir")+"/src/main/resources/config.xml";

            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            format.setExpandEmptyElements(true);
            XMLWriter output = new XMLWriter(new FileWriter(new File(configPath)), format);
            output.write(document);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addApartmentToXml(String apartment) {
//        String configPath = this.getClass().getClassLoader().getResource("config.xml").getPath();
        Document document = readXml();
        Element configELe = document.getRootElement();

        if (configELe.element(apartment) == null) {
            configELe.addElement(apartment);
        }

        writeXml(document);

    }

    public void addAppkeyToXml(String apartment, String appkeys) {
        List<String> appkeyList = Arrays.asList(appkeys.split(","));

        Document document = readXml();
        Element configEle = document.getRootElement();
        Element apartmentEle = configEle.element(apartment);

        for (String appkey : appkeyList) {
            if (apartmentEle.element(appkey) == null) {
                apartmentEle.addElement(appkey);
            }
        }

        writeXml(document);

    }

    public void addApiToXml(String apartment, String appkey, String apis, Boolean isOpt) {
        List<String> apiList = Arrays.asList(apis.split(","));

        Document document = readXml();
        Element configEle = document.getRootElement();
        Element apartmentEle = configEle.element(apartment);
        Element appkeyEle = apartmentEle.element(appkey);

        for (String api : apiList) {
            if (appkeyEle.element(api) == null) {
                appkeyEle.addElement(api).addAttribute("opt", isOpt.toString());
            }
        }

        writeXml(document);

    }

    public List<String> readApartmentInXml() {
        List<String> apartmentList = new ArrayList<>();

        Document document = readXml();
        Element configEle = document.getRootElement();

        for (Iterator<Element> it = configEle.elementIterator(); it.hasNext();) {
            Element apartmentEle = it.next();
            apartmentList.add(apartmentEle.getName());
        }

        return apartmentList;
    }

    public List<JSONObject> readAppkeyAndApiInXml(String apartment) {
        List<JSONObject> appkeyList = new ArrayList<>();

        Document document = readXml();
        Element configEle = document.getRootElement();
        Element apartmentEle = configEle.element(apartment);

        for (Iterator<Element> it = apartmentEle.elementIterator(); it.hasNext();) {
            JSONObject appkeyAndApi = new JSONObject();
            Element appkeyEle = it.next();

            String appkey = appkeyEle.getName();
            appkeyAndApi.put("appkey", appkey);

            List<JSONObject> apiList = readApiByAppkey(apartment, appkey);
            appkeyAndApi.put("apiList", apiList);

            appkeyList.add(appkeyAndApi);
        }

        return appkeyList;

    }

    public List<JSONObject> readApiByAppkey(String apartment, String appkey) {
        List<JSONObject> apiList = new ArrayList<>();

        Document document = readXml();
        Element configEle = document.getRootElement();
        Element apartmentEle = configEle.element(apartment);
        Element appkeyEle = apartmentEle.element(appkey);

        for (Iterator<Element> it = appkeyEle.elementIterator(); it.hasNext(); ) {
            JSONObject apiObj = new JSONObject();
            Element apiEle = it.next();
            apiObj.put("api", apiEle.getName());

            if (Boolean.parseBoolean(apiEle.attributeValue("opt"))) {
                apiObj.put("opt", true);
            } else {
                apiObj.put("opt", false);
            }

            apiList.add(apiObj);
        }

        return apiList;

    }

    public List<String> readAppkeyInXml(String apartment) {
        List<String> appkeyList = new ArrayList<>();

        Document document = readXml();
        Element configEle = document.getRootElement();
        Element apartmentEle = configEle.element(apartment);

        for (Iterator<Element> it = apartmentEle.elementIterator(); it.hasNext();) {
            Element appkeyEle = it.next();
            appkeyList.add(appkeyEle.getName());
        }

        return appkeyList;

    }

    public List<String> readApiInXml(String apartment) {
        List<String> apiList = new ArrayList<>();

        Document document = readXml();
        Element configEle = document.getRootElement();
        Element apartmentEle = configEle.element(apartment);

        List<String> appkeyList = readAppkeyInXml(apartment);
        for(String appkey : appkeyList) {
            Element appkeyEle = apartmentEle.element(appkey);

            for (Iterator<Element> it = appkeyEle.elementIterator(); it.hasNext(); ) {
                Element apiEle = it.next();
                apiList.add(apiEle.getName());
            }
        }

        return apiList;

    }

    public List<String> readOptApiInXml(String apartment) {
        List<String> apiList = new ArrayList<>();

        Document document = readXml();
        Element configEle = document.getRootElement();
        Element apartmentEle = configEle.element(apartment);

        List<String> appkeyList = readAppkeyInXml(apartment);
        for(String appkey : appkeyList) {
            Element appkeyEle = apartmentEle.element(appkey);

            for (Iterator<Element> it = appkeyEle.elementIterator(); it.hasNext(); ) {
                Element apiEle = it.next();
                if (Boolean.parseBoolean(apiEle.attributeValue("opt"))) {
                    apiList.add(apiEle.getName());
                }
            }
        }

        return apiList;
    }


//    public static void main(String [] a) {
//
////        XmlUtil test = new XmlUtil();
////        System.out.println("appkey: " + test.readAppkeyInXml("CRM"));
////        System.out.println("api: " + test.readApiInXml("CRM"));
////        System.out.println("opt: " + test.readOptApiInXml("CRM"));
//
//
//
//    }
}
