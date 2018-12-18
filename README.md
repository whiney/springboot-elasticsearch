# springboot-elasticsearch
> 基于springboot的web项目，通过elasticsearch提供的Java API 进行查询操作.

### 起因
项目在一个查询要在亚秒级计算（分组、累加、平均）大量数据的结果。官方提供的API过于简单，自己在做项目中遇到了一些坑，并总结了一些API的使用，简单分享一下。
#### 服务版本
+ [elasticsearch 6.2.3](https://www.elastic.co/downloads/past-releases#)
+ Java 1.8
+ springBoot 2.0 +

### 首先安装一个自己的elasticsearch服务
RPM方式安装并启动访问
```
sudo rpm --install xxx.rpm

sudo service elasticsearch start

访问：xxxx:9200

{
  "name" : "X5m9ca9",
  "cluster_name" : "elasticsearch",
  "cluster_uuid" : "Llayl6QoQUSl4JcMIt0L5g",
  "version" : {
    "number" : "5.5.1",
    "build_hash" : "19c13d0",
    "build_date" : "2017-07-18T20:44:24.823Z",
    "build_snapshot" : false,
    "lucene_version" : "6.6.0"
  },
  "tagline" : "You Know, for Search"
}

安装启动完毕
```
### 程序
**demo是基于springboot快速构建了一个web应用，通过单元测试的方式，来验证方法**

#### 参考链接
[Search API](https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-search.html)

[BulkProcessor](https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-bulk-processor.html)

有一些坑是我领导踩得，部分代码已得授权。