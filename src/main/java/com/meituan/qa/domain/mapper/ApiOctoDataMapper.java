package com.meituan.qa.domain.mapper;

import com.meituan.qa.bean.ApiData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApiOctoDataMapper {

    void insertApiOctoData(@Param(value = "api") String api, @Param(value = "tp95") int tp95, @Param(value = "tp99") int tp99, @Param(value = "datatime") String datatime);

    List<ApiData> selectApiOctoData(@Param(value = "api") String api, @Param(value = "startTime") String startTime, @Param(value = "endTime") String endTime);

}
