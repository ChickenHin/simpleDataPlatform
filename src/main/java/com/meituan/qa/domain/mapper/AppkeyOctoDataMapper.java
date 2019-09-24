package com.meituan.qa.domain.mapper;

import com.meituan.qa.bean.AppkeyOctoData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppkeyOctoDataMapper {

    void insertAppkeyOctoData(@Param(value = "appkey") String appkey, @Param(value = "tp95") int tp95, @Param(value = "tp99") int tp99, @Param(value = "successrate") double successrate, @Param(value = "visitCount") int visitCount, @Param(value = "datatime") String datatime);

    List<AppkeyOctoData> selectAppkeyOctoData(@Param(value = "appkey") String appkey, @Param(value = "startTime") String startTime, @Param(value = "endTime") String endTime);
}
