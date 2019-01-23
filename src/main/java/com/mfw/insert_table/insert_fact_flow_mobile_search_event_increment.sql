set hive.vectorized.execution.enabled = true;
set hive.vectorized.execution.reduce.enabled = true;
set mapred.max.split.size = 256000000;
set hive.vectorized.execution.reduce.enabled = true;

insert overwrite table mfw_dwd.fact_flow_mobile_search_event_increment partition (dt='20190116')
select a.open_udid as device_id --设备ID
      ,case when a.uid is null  or a.uid='(null)' then 0 else a.uid end as uid --用户ID
      ,a.umddid as u_loc_mdd_id --用户目的地IDa.sys_ver --系统版本号
      ,a.sys_ver as sys_ver --系统版本号
      ,case when a.device_type='ios' then 'APPLE' when a.device_type='android' then upper(a.brand) end as device_brand --设备品牌
      ,a.hardware_model as device_model --设备型号
      ,a.device_type as device_type --设备类型
      ,a.app_ver as app_ver --客户端版本号
      ,a.dev_ver as dev_ver --客户端开发版本号
      ,a.net as net_type --网络类型
      ,case when a.device_type='ios' then 'App Store' when a.device_type='android' then a.channel_origin end as channel_origin --安装渠道
      ,case when a.device_type='ios' then 'App Store' when a.device_type='android' then a.channel_code end as channel_code --升级渠道
      ,a.isp as isp --网络服务运营商
      ,a.sdk_ver as sdk_ver --SDK版本
      ,a.mfw_env as mfw_env --客户端环境
      ,from_unixtime(a.ctime,'yyyy-MM-dd HH:mm:ss') as service_time --服务端时间
      ,a.hour as service_hour --服务端小时
      ,a.launch_guid as launch_guid --启动唯一ID
      ,a.day_first as day_first --距第一次启动的天数
      ,a.day_before as day_before --距上一次启动的天数
      ,a.lat as lat --纬度
      ,a.lng as lng --经度
      ,a.city as city --城市
      ,a.province as province --省
      ,from_unixtime(a.event_time,'yyyy-MM-dd HH:mm:ss') as event_time --事件时间
      ,a.event_guid as event_guid --事件ID
      ,a.event_code as event_code --事件名称
      ,substr(from_unixtime(a.event_time,'yyyy-MM-dd HH:mm:ss'),15,2) as event_hour --事件分钟
      ,case when get_json_object(a.attr,'$._tpi') = '' then null else get_json_object(a.attr,'$._tpi') end  as page_guid --页面唯一ID
      ,'' as parent_page_guid --get_json_object(a.attr,'$._tpi') --父页面唯一ID
      ,a.puri as parent_uri --父URI
     ,'' --get_json_object(a.attr,'$._tp') --父页面名称
      ,'保留字段' --basic.pplv1（暂定，还没值） --父页面一级分类
      ,'保留字段' --basic.pplv2（暂定，还没值） --父页面二级分类
      ,'保留字段' --basic.pplv3（暂定，还没值） --父页面三级分类
      ,a.uri as uri --URI
      ,case when get_json_object(a.attr,'$._tp') = '' then null else get_json_object(a.attr,'$._tp') end as page_name --页面名称
      ,'保留字段' --basic.plv1（暂定，还没值） --页面一级分类
      ,'保留字段' --basic.plv2（暂定，还没值） --页面二级分类
      ,'保留字段' --basic.plv3（暂定，还没值） --页面三级分类
      ,'保留字段' -- ,基于ref解析 --上一页面去参URI
      ,case when get_json_object(a.attr,'$._tpa') = '' then null else get_json_object(a.attr,'$._tpa') end as trigger_path --触发点路径
      ,case when get_json_object(a.attr,'$._tpt') = '' then null else get_json_object(a.attr,'$._tpt') end as trigger_point --触发点
      ,case when get_json_object(a.attr,'$.in') = '' then null else get_json_object(a.attr,'$.in') end as inner_type --进入方式
      ,case when a.event_code like 'click%' then 'click' when a.event_code like 'show%'  then 'show' end as event_type --事件类型
      ,a.app_code as app_code --客户端APP标识
      ,if(get_json_object(a.attr,'$.keyword')='',null,get_json_object(a.attr,'$.keyword'))
      ,if(get_json_object(a.attr,'$.mddid')='',null,get_json_object(a.attr,'$.mddid'))
      ,if(get_json_object(a.attr,'$.come_from')='',null,get_json_object(a.attr,'$.come_from'))
      ,if(get_json_object(a.attr,'$.search_scope')='',null,get_json_object(a.attr,'$.search_scope'))
      ,if(get_json_object(a.attr,'$.session_id')='',null,get_json_object(a.attr,'$.session_id'))
      ,if(get_json_object(a.attr,'$.tab_type')='',null,get_json_object(a.attr,'$.tab_type'))
      ,if(get_json_object(a.attr,'$.keyword_extra')='',null,get_json_object(a.attr,'$.keyword_extra'))
      ,if(get_json_object(a.attr,'$.intention_new')='',null,get_json_object(a.attr,'$.intention_new'))
      ,if(get_json_object(a.attr,'$.module_index')='',null,get_json_object(a.attr,'$.module_index'))
      ,if(get_json_object(a.attr,'$.module_name')='',null,get_json_object(a.attr,'$.module_name'))
      ,if(get_json_object(a.attr,'$.item_index')='',null,get_json_object(a.attr,'$.item_index'))
      ,if(get_json_object(a.attr,'$.item_name')='',null,get_json_object(a.attr,'$.item_name'))
      ,if(get_json_object(a.attr,'$.business_type')='',null,get_json_object(a.attr,'$.business_type'))
      ,if(get_json_object(a.attr,'$.result_type')='',null,get_json_object(a.attr,'$.result_type'))
      ,if(get_json_object(a.attr,'$.item_business_id')='',null,get_json_object(a.attr,'$.item_business_id'))
      ,if(get_json_object(a.attr,'$.keyword_mddid')='',null,get_json_object(a.attr,'$.keyword_mddid'))
      ,if(get_json_object(a.attr,'$.jump_url')='',null,get_json_object(a.attr,'$.jump_url'))
      ,if(get_json_object(a.attr,'$.action')='',null,get_json_object(a.attr,'$.action'))
      ,if(get_json_object(a.attr,'$.suggest_word_clicked')='',null,get_json_object(a.attr,'$.suggest_word_clicked'))
      ,if(get_json_object(a.attr,'$.type')='',null,get_json_object(a.attr,'$.type'))
      ,'保留字段' --attr.cd1 --自定义扩展字段1
      ,'保留字段' --attr.cd2 --自定义扩展字段2
      ,'保留字段' --attr.cd3 --自定义扩展字段3
      ,'保留字段' --attr.cd4 --自定义扩展字段4
      ,'保留字段' --attr.cd5 --自定义扩展字段5
      ,'保留字段' --attr.cd6 --自定义扩展字段6
      ,'保留字段' --attr.cd7 --自定义扩展字段7
      ,'保留字段' --attr.cd8 --自定义扩展字段8
      ,'保留字段' --attr.cd9 --自定义扩展字段9
      ,'保留字段' --attr.cd10 --自定义扩展字段10
      ,'保留字段' --attr.cd11 --自定义扩展字段11
      ,0
      ,0
      ,array('')
      ,map('key','value')
      ,''
      ,0
      ,0
      ,array('')
      ,map('key','value')
 from default.mobile_event_parquet a where a.dt = '20190116' and catalog = 'default'
and event_code in ('search'
,'search_result_hit'
,'search_result_click'
,'search_result_item_show'
,'search_load'
,'search_module_click'
,'search_suggest_click'
,'search_more_click');






