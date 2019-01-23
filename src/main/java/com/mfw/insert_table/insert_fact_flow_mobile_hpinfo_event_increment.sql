set hive.vectorized.execution.enabled = true;
set hive.vectorized.execution.reduce.enabled = true;
set mapred.max.split.size = 256000000;
set hive.vectorized.execution.reduce.enabled = true;

insert overwrite table mfw_dwd.fact_flow_mobile_hpinfo_event_increment partition (dt='${hivevar:exe_date}')
select a.open_udid as device_id

      ,case when a.uid is null  or a.uid='(null)' then 0 else a.uid end as uid
      ,a.umddid as u_loc_mdd_id
      ,a.sys_ver as sys_ver
      ,case when a.device_type='ios' then 'APPLE' when a.device_type='android' then upper(a.brand) end as device_brand
      ,a.hardware_model as device_model
      ,a.device_token as device_token
      ,a.device_type as device_type
      ,a.app_ver as app_ver
      ,a.net as net_type
      ,case when a.device_type='ios' then 'App Store' when a.device_type='android' then a.channel_origin end as channel_origin
      ,case when a.device_type='ios' then 'App Store' when a.device_type='android' then a.channel_code end as channel_code
      ,a.client_ip as client_ip
      ,a.ip_server as service_ip
      ,a.isp as isp
      ,a.sdk_ver as sdk_ver
      ,a.mfw_env as mfw_env
      ,from_unixtime(a.ctime,'yyyy-MM-dd HH:mm:ss') as service_time
      ,a.hour as service_hour
      ,a.launch_guid as launch_guid
      ,a.day_first as day_first
      ,a.day_before as day_before
      ,a.lat  as lat
      ,a.lng  as lng
      ,a.city as city
      ,a.province as province
      ,from_unixtime(a.event_time,'yyyy-MM-dd HH:mm:ss') as event_time
      ,a.event_guid as event_guid
      ,a.event_code as event_code
      ,cast(if(get_json_object(a.attr,'$.channel_id')='',null,get_json_object(a.attr,'$.channel_id')) as bigint) as channel_id  --bigint
      ,if(get_json_object(a.attr,'$.duration')='',null,get_json_object(a.attr,'$.duration')) as dutarion     --double
      ,cast(if(get_json_object(a.attr,'$.is_ad')='',null,get_json_object(a.attr,'$.is_ad')) as bigint) as is_ad  --往后加字段  --bigint
      ,if(get_json_object(a.attr,'$.item_id')='',null,get_json_object(a.attr,'$.item_id')) as item_id
      ,if(get_json_object(a.attr,'$.group_item_id')='',null,get_json_object(a.attr,'$.group_item_id')) as group_item_id
      ,case when a.event_code='weng_click' then if(get_json_object(a.attr,'$.weng_id')='',null,get_json_object(a.attr,'$.weng_id')) else if(get_json_object(a.attr,'$.item_business_id')='',null,get_json_object(a.attr,'$.item_business_id')) end as item_business_id
      ,if(get_json_object(a.attr,'$.group_business_id')='',null,get_json_object(a.attr,'$.group_business_id')) as group_business_id
      ,if(get_json_object(a.attr,'$.model_id')='',null,get_json_object(a.attr,'$.model_id')) as model_id
      ,case when a.event_code in ('sales_report_click','weng_click') then if(get_json_object(a.attr,'$.author_uid')='',null,get_json_object(a.attr,'$.author_uid')) else if(get_json_object(a.attr,'$.author_id')='',null,get_json_object(a.attr,'$.author_id')) end as author_id
      ,if(get_json_object(a.attr,'$.module_name')='',null,get_json_object(a.attr,'$.module_name')) as module_name
      ,if(get_json_object(a.attr,'$.item_title')='',null,get_json_object(a.attr,'$.item_title')) as item_title
      ,case when a.event_code in ('home_article_group_show','home_article_group_click') then if(get_json_object(a.attr,'$.group_type')='',null,get_json_object(a.attr,'$.group_type')) else if(get_json_object(a.attr,'$.item_type')='',null,get_json_object(a.attr,'$.item_type')) end as item_type
      ,case when a.event_code in ('home_article_group_show','home_article_group_click') then if(get_json_object(a.attr,'$.item_abtest')='',null,split(get_json_object(a.attr,'$.item_abtest'),'_')[0]) else if(get_json_object(a.attr,'$.abtest')='',null,split(get_json_object(a.attr,'$.abtest'),'_')[0]) end as abtest_type
      ,case when a.event_code in ('home_article_group_show','home_article_group_click') then if(get_json_object(a.attr,'$.item_abtest')='',null,split(get_json_object(a.attr,'$.item_abtest'),'_')[1]) else if(get_json_object(a.attr,'$.abtest')='',null,split(get_json_object(a.attr,'$.abtest'),'_')[1]) end as abtest
      ,if(get_json_object(a.attr,'$.group_abtest')='',null,split(get_json_object(a.attr,'$.group_abtest'),'_')[0]) as group_abtest
      ,if(get_json_object(a.attr,'$.group_abtest')='',null,split(get_json_object(a.attr,'$.group_abtest'),'_')[1])
      ,if(get_json_object(a.attr,'$.is_default')='',null,get_json_object(a.attr,'$.is_default')) as is_default
      ,if(get_json_object(a.attr,'$.is_in')='',null,get_json_object(a.attr,'$.is_in')) as is_in
      ,if(get_json_object(a.attr,'$.refresh_type')='',null,get_json_object(a.attr,'$.refresh_type')) as refresh_type
      ,if(get_json_object(a.attr,'$.refresh_range')='',null,get_json_object(a.attr,'$.refresh_range')) as refresh_range
      ,cast(if(get_json_object(a.attr,'$.index_page')='',null,get_json_object(a.attr,'$.index_page')) as bigint)  as index_page --bigint
      ,cast(if(get_json_object(a.attr,'$.load_num')='',null,get_json_object(a.attr,'$.load_num')) as bigint) as load_num --bigint
      ,if(get_json_object(a.attr,'$.item_source')='',null,get_json_object(a.attr,'$.item_source')) as item_source
      ,if(get_json_object(a.attr,'$.item_url')='',null,get_json_object(a.attr,'$.item_url')) as item_url
      ,if(get_json_object(a.attr,'$.item_uri')='',null,get_json_object(a.attr,'$.item_uri')) as item_uri
      ,cast(if(get_json_object(a.attr,'$.item_index')='',null,get_json_object(a.attr,'$.item_index')) as bigint)  as item_index--int
      ,cast(if(get_json_object(a.attr,'$.group_index')='',null,get_json_object(a.attr,'$.group_index')) as bigint) as group_index --int
      ,cast(if(get_json_object(a.attr,'$.pic_index')='',null,get_json_object(a.attr,'$.pic_index')) as bigint)  as pic_index --int
      ,cast(if(get_json_object(a.attr,'$.page_index')='',null,get_json_object(a.attr,'$.page_index')) as bigint)  as page_index --int
      ,case when a.event_code in ('home_article_group_show','home_article_group_click') then  if(get_json_object(a.attr,'$.item_extra')='',null,get_json_object(a.attr,'$.item_extra')) when a.event_code='home_article_time' then if(get_json_object(a.attr,'$.channel_extra')='',null,get_json_object(a.attr,'$.channel_extra')) else if(get_json_object(a.attr,'$.extra')='',null,get_json_object(a.attr,'$.extra')) end as extra
      ,if(get_json_object(a.attr,'$.group_extra')='',null,get_json_object(a.attr,'$.group_extra')) as group_extra
      ,if(get_json_object(a.attr,'$.page_name')='',null,get_json_object(a.attr,'$.page_name')) as page_name
      ,if(get_json_object(a.attr,'$.identifier')='',null,get_json_object(a.attr,'$.identifier')) as page_guid--以上
      ,if(get_json_object(a.attr,'$.attr_tpi')='',null,get_json_object(a.attr,'$.attr_tpi')) as parent_page_guid
      ,a.puri as parent_uri
      ,a.uri as uri
      ,a.ref as referer_uri
      ,if(get_json_object(a.attr,'$._tl')='',null,get_json_object(a.attr,'$._tl')) as trigger_level
      ,if(get_json_object(a.attr,'$._tp')='',null,get_json_object(a.attr,'$._tp')) as parent_name
      ,if(get_json_object(a.attr,'$._tpa')='',null,get_json_object(a.attr,'$._tpa')) as trigger_path
      ,if(get_json_object(a.attr,'$._tpt')='',null,get_json_object(a.attr,'$._tpt')) as trigger_point
      ,if(get_json_object(a.attr,'$._turi')='',null,get_json_object(a.attr,'$._turi')) as parent_url
      ,a.app_code as app_code
      ,''
      ,''
      ,''
      ,''
      ,''
      ,''
      ,''
      ,''
      ,''
      ,''
      ,''
      ,0
      ,0
      ,array('')
      ,map('key','value')
      ,''
      ,0
      ,0
      ,array('')
      ,map('key','value')
from default.mobile_event_parquet a
where a.dt = '${hivevar:exe_date}'
  and a.catalog='default'
  and a.event_code in ('home_article_time','home_article_list_click','home_refresh','home_article_group_show','home_article_group_click','home_article_channel_switch','home_article_load','sales_report_click','click_home_article','weng_click')
  and a.app_code in ('cn.mafengwo.www','com.mfw.roadbook');

insert into table mfw_dwd.fact_flow_mobile_hpinfo_event_increment partition (dt='${hivevar:exe_date}')
  select a.open_udid
      ,case when a.uid is null  or a.uid='(null)' then 0 else a.uid end
      ,a.umddid
      ,a.sys_ver
      ,case when a.device_type='ios' then 'APPLE' when a.device_type='android' then upper(a.brand) end
      ,a.hardware_model
      ,a.device_token
      ,a.device_type
      ,a.app_ver
      ,a.net
      ,case when a.device_type='ios' then 'App Store' when a.device_type='android' then a.channel_origin end
      ,case when a.device_type='ios' then 'App Store' when a.device_type='android' then a.channel_code end
      ,a.client_ip
      ,a.ip_server
      ,a.isp
      ,a.sdk_ver
      ,a.mfw_env
      ,from_unixtime(a.ctime,'yyyy-MM-dd HH:mm:ss')
      ,a.hour
      ,a.launch_guid
      ,a.day_first
      ,a.day_before
      ,a.lat
      ,a.lng
      ,a.city
      ,a.province
      ,from_unixtime(a.event_time,'yyyy-MM-dd HH:mm:ss')
      ,a.event_guid
      ,a.event_code
      ,cast(if(get_json_object(a.attr,'$.channel_id')='',null,get_json_object(a.attr,'$.channel_id')) as bigint)  --bigint
      ,if(get_json_object(a.attr,'$.duration')='',null,get_json_object(a.attr,'$.duration'))     --double
      ,cast(if(get_json_object(a.attr,'$.is_ad')='',null,get_json_object(a.attr,'$.is_ad')) as bigint)  --往后加字段  --bigint
      ,if(get_json_object(a.attr,'$.item_id')='',null,get_json_object(a.attr,'$.item_id'))
      ,if(get_json_object(a.attr,'$.group_item_id')='',null,get_json_object(a.attr,'$.group_item_id'))
      ,if(get_json_object(a.attr,'$.item_business_id')='',null,get_json_object(a.attr,'$.item_business_id'))
      ,if(get_json_object(a.attr,'$.group_business_id')='',null,get_json_object(a.attr,'$.group_business_id'))
      ,if(get_json_object(a.attr,'$.model_id')='',null,get_json_object(a.attr,'$.model_id'))
      ,if(get_json_object(a.attr,'$.author_id')='',null,get_json_object(a.attr,'$.author_id'))
      ,if(get_json_object(a.attr,'$.module_name')='',null,get_json_object(a.attr,'$.module_name'))
      ,if(get_json_object(a.attr,'$.item_title')='',null,get_json_object(a.attr,'$.item_title'))
      ,if(get_json_object(a.attr,'$.item_type')='',null,get_json_object(a.attr,'$.item_type'))
      ,if(get_json_object(a.attr,'$.abtest')='',null,split(get_json_object(a.attr,'$.abtest'),'_')[0])
      ,if(get_json_object(a.attr,'$.abtest')='',null,split(get_json_object(a.attr,'$.abtest'),'_')[1])
      ,if(get_json_object(a.attr,'$.group_abtest')='',null,split(get_json_object(a.attr,'$.group_abtest'),'_')[0])
      ,if(get_json_object(a.attr,'$.group_abtest')='',null,split(get_json_object(a.attr,'$.group_abtest'),'_')[1])
      ,if(get_json_object(a.attr,'$.is_default')='',null,get_json_object(a.attr,'$.is_default'))
      ,if(get_json_object(a.attr,'$.is_in')='',null,get_json_object(a.attr,'$.is_in'))
      ,if(get_json_object(a.attr,'$.refresh_type')='',null,get_json_object(a.attr,'$.refresh_type'))
      ,if(get_json_object(a.attr,'$.refresh_range')='',null,get_json_object(a.attr,'$.refresh_range'))
      ,cast(if(get_json_object(a.attr,'$.index_page')='',null,get_json_object(a.attr,'$.index_page')) as bigint)  --bigint
      ,cast(if(get_json_object(a.attr,'$.load_num')='',null,get_json_object(a.attr,'$.load_num')) as bigint) --bigint
      ,if(get_json_object(a.attr,'$.item_source')='',null,get_json_object(a.attr,'$.item_source'))
      ,if(get_json_object(a.attr,'$.item_url')='',null,get_json_object(a.attr,'$.item_url'))
      ,if(get_json_object(a.attr,'$.item_uri')='',null,get_json_object(a.attr,'$.item_uri'))
      ,cast(if(get_json_object(a.attr,'$.item_index')='',null,get_json_object(a.attr,'$.item_index')) as bigint) --int
      ,cast(if(get_json_object(a.attr,'$.group_index')='',null,get_json_object(a.attr,'$.group_index')) as bigint) --int
      ,cast(if(get_json_object(a.attr,'$.pic_index')='',null,get_json_object(a.attr,'$.pic_index')) as bigint)  --int
      ,cast(if(get_json_object(a.attr,'$.page_index')='',null,get_json_object(a.attr,'$.page_index')) as bigint) --int
      ,if(get_json_object(a.attr,'$.extra')='',null,get_json_object(a.attr,'$.extra'))
      ,if(get_json_object(a.attr,'$.group_extra')='',null,get_json_object(a.attr,'$.group_extra'))
      ,if(get_json_object(a.attr,'$.page_name')='',null,get_json_object(a.attr,'$.page_name'))
      ,if(get_json_object(a.attr,'$.identifier')='',null,get_json_object(a.attr,'$.identifier'))
      ,if(get_json_object(a.attr,'$.attr_tpi')='',null,get_json_object(a.attr,'$.attr_tpi'))
      ,a.puri
      ,a.uri
      ,a.ref
      ,get_json_object(a.attr,'$._tl')
      ,get_json_object(a.attr,'$._tp')
      ,get_json_object(a.attr,'$._tpa')
      ,get_json_object(a.attr,'$._tpt')
      ,get_json_object(a.attr,'$._turi')
      ,a.app_code
      ,''
      ,''
      ,''
      ,''
      ,''
      ,''
      ,''
      ,''
      ,''
      ,''
      ,''
      ,0
      ,0
      ,array('')
      ,map('key','value')
      ,''
      ,0
      ,0
      ,array('')
      ,map('key','value')
      from default.mobile_event_parquet a
where a.dt = '${hivevar:exe_date}' and a.catalog='home_module_click' and  a.event_code='home_module_click' and a.app_code in ('cn.mafengwo.www','com.mfw.roadbook');


insert into table mfw_dwd.fact_flow_mobile_hpinfo_event_increment partition (dt='${hivevar:exe_date}')
  select a.open_udid
      ,case when a.uid is null  or a.uid='(null)' then 0 else a.uid end
      ,a.umddid
      ,a.sys_ver
      ,case when a.device_type='ios' then 'APPLE' when a.device_type='android' then upper(a.brand) end
      ,a.hardware_model
      ,a.device_token
      ,a.device_type
      ,a.app_ver
      ,a.net
      ,case when a.device_type='ios' then 'App Store' when a.device_type='android' then a.channel_origin end
      ,case when a.device_type='ios' then 'App Store' when a.device_type='android' then a.channel_code end
      ,a.client_ip
      ,a.ip_server
      ,a.isp
      ,a.sdk_ver
      ,a.mfw_env
      ,from_unixtime(a.ctime,'yyyy-MM-dd HH:mm:ss')
      ,a.hour
      ,a.launch_guid
      ,a.day_first
      ,a.day_before
      ,a.lat
      ,a.lng
      ,a.city
      ,a.province
      ,from_unixtime(a.event_time,'yyyy-MM-dd HH:mm:ss')
      ,a.event_guid
      ,a.event_code
      ,cast(if(get_json_object(a.attr,'$.channel_id')='',null,get_json_object(a.attr,'$.channel_id')) as bigint)  --bigint
      ,if(get_json_object(a.attr,'$.duration')='',null,get_json_object(a.attr,'$.duration'))     --double
      ,cast(if(get_json_object(a.attr,'$.is_ad')='',null,get_json_object(a.attr,'$.is_ad')) as bigint)  --往后加字段  --bigint
      ,if(get_json_object(a.attr,'$.item_id')='',null,get_json_object(a.attr,'$.item_id'))
      ,if(get_json_object(a.attr,'$.group_item_id')='',null,get_json_object(a.attr,'$.group_item_id'))
      ,if(get_json_object(a.attr,'$.item_business_id')='',null,get_json_object(a.attr,'$.item_business_id'))
      ,if(get_json_object(a.attr,'$.group_business_id')='',null,get_json_object(a.attr,'$.group_business_id'))
      ,if(get_json_object(a.attr,'$.model_id')='',null,get_json_object(a.attr,'$.model_id'))
      ,if(get_json_object(a.attr,'$.author_id')='',null,get_json_object(a.attr,'$.author_id'))
      ,if(get_json_object(a.attr,'$.module_name')='',null,get_json_object(a.attr,'$.module_name'))
      ,if(get_json_object(a.attr,'$.item_title')='',null,get_json_object(a.attr,'$.item_title'))
      ,if(get_json_object(a.attr,'$.item_type')='',null,get_json_object(a.attr,'$.item_type'))
      ,if(get_json_object(a.attr,'$.abtest')='',null,split(get_json_object(a.attr,'$.abtest'),'_')[0])
      ,if(get_json_object(a.attr,'$.abtest')='',null,split(get_json_object(a.attr,'$.abtest'),'_')[1])
      ,if(get_json_object(a.attr,'$.group_abtest')='',null,split(get_json_object(a.attr,'$.group_abtest'),'_')[0])
      ,if(get_json_object(a.attr,'$.group_abtest')='',null,split(get_json_object(a.attr,'$.group_abtest'),'_')[1])
      ,if(get_json_object(a.attr,'$.is_default')='',null,get_json_object(a.attr,'$.is_default'))
      ,if(get_json_object(a.attr,'$.is_in')='',null,get_json_object(a.attr,'$.is_in'))
      ,if(get_json_object(a.attr,'$.refresh_type')='',null,get_json_object(a.attr,'$.refresh_type'))
      ,if(get_json_object(a.attr,'$.refresh_range')='',null,get_json_object(a.attr,'$.refresh_range'))
      ,cast(if(get_json_object(a.attr,'$.index_page')='',null,get_json_object(a.attr,'$.index_page')) as bigint)  --bigint
      ,cast(if(get_json_object(a.attr,'$.load_num')='',null,get_json_object(a.attr,'$.load_num')) as bigint) --bigint
      ,if(get_json_object(a.attr,'$.item_source')='',null,get_json_object(a.attr,'$.item_source'))
      ,if(get_json_object(a.attr,'$.item_url')='',null,get_json_object(a.attr,'$.item_url'))
      ,if(get_json_object(a.attr,'$.item_uri')='',null,get_json_object(a.attr,'$.item_uri'))
      ,cast(if(get_json_object(a.attr,'$.item_index')='',null,get_json_object(a.attr,'$.item_index')) as bigint) --int
      ,cast(if(get_json_object(a.attr,'$.group_index')='',null,get_json_object(a.attr,'$.group_index')) as bigint) --int
      ,cast(if(get_json_object(a.attr,'$.pic_index')='',null,get_json_object(a.attr,'$.pic_index')) as bigint)  --int
      ,cast(if(get_json_object(a.attr,'$.page_index')='',null,get_json_object(a.attr,'$.page_index')) as bigint) --int
      ,if(get_json_object(a.attr,'$.extra')='',null,get_json_object(a.attr,'$.extra'))
      ,if(get_json_object(a.attr,'$.group_extra')='',null,get_json_object(a.attr,'$.group_extra'))
      ,if(get_json_object(a.attr,'$.page_name')='',null,get_json_object(a.attr,'$.page_name'))
      ,if(get_json_object(a.attr,'$.identifier')='',null,get_json_object(a.attr,'$.identifier')) --以上
      ,if(get_json_object(a.attr,'$.attr_tpi')='',null,get_json_object(a.attr,'$.attr_tpi'))
      ,a.puri
      ,a.uri
      ,a.ref
      ,get_json_object(a.attr,'$._tl')
      ,get_json_object(a.attr,'$._tp')
      ,get_json_object(a.attr,'$._tpa')
      ,get_json_object(a.attr,'$._tpt')
      ,get_json_object(a.attr,'$._turi')
      ,a.app_code
      ,''
      ,''
      ,''
      ,''
      ,''
      ,''
      ,''
      ,''
      ,''
      ,''
      ,''
      ,0
      ,0
      ,array('')
      ,map('key','value')
      ,''
      ,0
      ,0
      ,array('')
      ,map('key','value')
      from default.mobile_event_parquet a
where a.dt = '${hivevar:exe_date}' and a.catalog='home_article_list_show' and  a.event_code='home_article_list_show' and a.app_code in ('cn.mafengwo.www','com.mfw.roadbook');
