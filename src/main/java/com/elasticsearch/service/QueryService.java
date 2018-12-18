package com.elasticsearch.service;

import com.elasticsearch.Model.Es;

import java.util.List;
import java.util.Map;

/**
 * Created by baishuai on 2018/12/18
 */
public interface QueryService {

    List<Map<String, Object>> queryListFromES(Es es, int storeId, String storeName, String startDate, String endDate);

}
