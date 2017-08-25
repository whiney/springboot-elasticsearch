package com.elasticsearch.controller;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregatorBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Map;

/**
 * Created by baishuai on 2017/8/24.
 */
@RestController
public class TestController {

    @Resource
    TransportClient client;//注入es操作对象

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String test(){

        Map<String,Object> map = Collections.emptyMap();

        Script script = new Script(ScriptType.INLINE, "painless","params._value0 > 0",map);  //提前定义好查询销量是否大于1000的脚本，类似SQL里面的having

        long beginTime = System.currentTimeMillis();

        SearchResponse sr = client.prepareSearch("dm_di").setTypes("sale") //要查询的表
                .setQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery("store_name.keyword", "xxx旗舰店"))  //挨个设置查询条件，没有就不加，如果是字符串类型的，要加keyword后缀
                        .must(QueryBuilders.termQuery("department_name.keyword", "玎"))
                        .must(QueryBuilders.termQuery("category_name.keyword", "T恤"))
                        .must(QueryBuilders.rangeQuery("pay_date").gt("2017-03-07").lt("2017-07-09"))
                ).addAggregation(
                        AggregationBuilders.terms("by_product_code").field("product_code.keyword").size(500) //按货号分组，最多查500个货号.SKU直接改字段名字就可以
                                .subAggregation(AggregationBuilders.terms("by_store_name").field("store_name.keyword").size(50) //按店铺分组，不显示店铺可以过滤掉这一行，下边相应减少一个循环
                                        .subAggregation(AggregationBuilders.sum("total_sales").field("quantity"))  //分组计算销量汇总
                                        .subAggregation(AggregationBuilders.sum("total_sales_amount").field("amount_actual"))  //分组计算实付款汇总，需要加其他汇总的在这里依次加
                                        .subAggregation(PipelineAggregatorBuilders.bucketSelector("sales_bucket_filter",script,"total_sales")))//查询是否大于指定值
                                .order(Terms.Order.compound(Terms.Order.aggregation("total_calculate_sale_amount",false)))) //分组排序

                .execute().actionGet();

        Terms terms = sr.getAggregations().get("by_product_code");   //查询遍历第一个根据货号分组的aggregation

        System.out.println(terms.getBuckets().size());
        for (Terms.Bucket entry : terms.getBuckets()) {
            System.out.println("------------------");
            System.out.println("【 " + entry.getKey() + " 】订单数 : " + entry.getDocCount() );

            Terms subTerms = entry.getAggregations().get("by_store_name");    //查询遍历第二个根据店铺分组的aggregation
            for (Terms.Bucket subEntry : subTerms.getBuckets()) {
                Sum sum1 = subEntry.getAggregations().get("total_sales"); //取得销量的汇总
                double total_sales = sum1.getValue();
                System.out.println(subEntry.getKey() + " 订单数:  " + subEntry.getDocCount() + "  销量: " + total_sales); //店铺和订单数量和销量
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("查询耗时" + ( endTime - beginTime ) + "毫秒");

        return "Hello,elasticsearch";
    }

}
