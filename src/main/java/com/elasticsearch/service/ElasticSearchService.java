package com.elasticsearch.service;


/**
 * Created by baishuai on 2018/12/18
 */
public interface ElasticSearchService {

    void insertById(String index, String type, String id, String jsonStr);

    void updateById(String index, String type, String id, String jsonStr);

    void deleteById(String index, String type, String id);

}
