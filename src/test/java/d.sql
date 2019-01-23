
set hive.vectorized.execution.enabled = true;
set hive.vectorized.execution.reduce.enabled = true;
set mapred.max.split.size = 256000000;
set hive.vectorized.execution.reduce.enabled = true;

add jar /home/penghao/liusengen/jsonclear2.jar;
CREATE temporary function ArrayToMap as 'com.mfw.ArrayToMap';
CREATE temporary function json_trigger as 'com.mfw.JsonTrigger';
CREATE temporary function json_item as 'com.mfw.JsonDetails';

--insert overwrite table mfw_dwd.fact_flow_mobile_sc_event_increment partition (dt='20181226')
INSERT OVERWRITE LOCAL DIRECTORY '/home/penghao/liusengen/out1'
select a.open_udid --设备ID
      ,'open_udid' --设备ID类型
      ,case when a.uid is null  or a.uid='(null)' then 0 else a.uid end --用户ID --待确定规则
      ,case when a.device_type='ios' then a.idfa
            when a.device_type='android' then a.android_id end --IDFA或安卓ID
      ,a.umddid --用户目的地ID
      ,'保留字段' --用户意图目的地ID
      ,a.sys_ver --系统版本号
      ,case when a.device_type='ios' then 'APPLE'
            when a.device_type='android' then upper(a.brand) end --设备品牌
      ,a.hardware_model --设备型号
      ,a.device_token --设备推送码
      ,'保留字段' --推送ID
      ,a.device_type --设备类型
      ,a.app_ver --客户端版本号
      ,coalesce(cast(regexp_extract(a.app_ver,'^(\\d+)',1) as int),0)*10000
       +coalesce(cast(regexp_extract(a.app_ver,'^\\d+\\.(\\d+)',1) as int),0)*100
       +coalesce(cast(regexp_extract(a.app_ver,'(\\d+)$',1) as int),0) --客户端版本修正版
      ,coalesce(cast(regexp_extract(a.app_ver,'^(\\d+)',1) as int),0) --客户端主版本号
      ,a.dev_ver --客户端开发版本号
      ,a.net --网络类型
      ,case when a.device_type='ios' then 'App Store'
            when a.device_type='android' then a.channel_origin end --安装渠道
      ,case when a.device_type='ios' then 'App Store'
            when a.device_type='android' then a.channel_code end --升级渠道
      ,'保留字段' --渠道
      ,'保留字段' --打开渠道
      ,a.client_ip --客户端IP
      ,a.ip_server --服务端IP
      ,a.isp --网络服务运营商
      ,a.sdk_ver --SDK版本
      ,a.mfw_env --客户端环境
      ,from_unixtime(a.ctime,'yyyy-MM-dd HH:mm:ss') --服务端时间
      ,a.hour --服务端小时
      ,a.launch_guid --启动唯一ID
      ,a.day_first --距第一次启动的天数
      ,a.day_before --距上一次启动的天数
      ,'保留字段' --设备首次启动时间
      ,a.lat --纬度
      ,a.lng --经度
      ,a.city --城市
      ,a.province --省
      ,from_unixtime(a.event_time,'yyyy-MM-dd HH:mm:ss') --事件时间
      ,substr(from_unixtime(a.event_time,'yyyy-MM-dd HH:mm:ss'),12,2) --事件小时
      ,substr(from_unixtime(a.event_time,'yyyy-MM-dd HH:mm:ss'),15,2) --事件分钟
      ,a.event_guid --事件ID
      ,a.event_code --事件名称
      ,case when get_json_object(a.attr,'$.pos_id') = '' then null else get_json_object(a.attr,'$.pos_id') end --资源位ID
      ,case when split(get_json_object(a.attr,'$.pos_id'),'\\.')[0] = '' then null else split(get_json_object(a.attr,'$.pos_id'),'\\.')[0] end --attr.pos_id以.分割后取第1位  资源位页面业务类型
      ,case when split(get_json_object(a.attr,'$.pos_id'),'\\.')[1] = '' then null else split(get_json_object(a.attr,'$.pos_id'),'\\.')[1] end --attr.pos_id以.分割后取第2位  资源位页面类型
      ,case when split(get_json_object(a.attr,'$.pos_id'),'\\.')[2] = '' then null else split(get_json_object(a.attr,'$.pos_id'),'\\.')[2] end --attr.pos_id以.分割后取第3位  资源位模块
      ,case when split(get_json_object(a.attr,'$.pos_id'),'\\.')[3] = '' then null else split(get_json_object(a.attr,'$.pos_id'),'\\.')[3] end --attr.pos_id以.分割后取第4位  资源位序列
      ,case when get_json_object(a.attr,'$.prm_id') = '' then null else get_json_object(a.attr,'$.prm_id') end  --资源位业务系统ID
      ,case when split(get_json_object(a.attr,'$.prm_id'),'\\.')[0] = '' then null else split(get_json_object(a.attr,'$.prm_id'),'\\.')[0] end --attr.prm_id以.分割后的第1位  资源位业务系统代号
      ,case when get_json_object(a.attr,'$.module_name') = '' then null else get_json_object(a.attr,'$.module_name') end --资源位所在模块名称
      ,str_to_map(ArrayToMap(get_json_object(a.attr,'$.item_type'),get_json_object(a.attr,'$.item_id')),',',':')
      ,case when get_json_object(a.attr,'$.item_name') = '' then null else get_json_object(a.attr,'$.item_name') end  --资源位名称
      ,case when get_json_object(a.attr,'$.item_source') = '' then null else get_json_object(a.attr,'$.item_source') end  --资源位类型
      ,case when get_json_object(a.attr,'$.item_uri') = '' then null else get_json_object(a.attr,'$.item_uri') end  --资源位的URI
      ,if(json_item(a.attr,'detail') is null,null,str_to_map(json_item(a.attr,'detail'),',','=')) as item_detail --'保留字段'-- ,get_json_object(a.attr,'$.item_detail'),值为单层json,转换为Map
      ,if(json_item(a.attr,'detail') is null,null,str_to_map(json_item(a.attr,'item_info'),',','=')) as item_info --'保留字段'-- ,get_json_object(a.attr,'$.item_info'),值为单层json,转换为MAP
      ,case when get_json_object(a.attr,'$.show_cycle_id') = '' then null else get_json_object(a.attr,'$.show_cycle_id') end  --展示周期ID
      ,case when get_json_object(a.attr,'$.show_cycle_type') = '' then null else get_json_object(a.attr,'$.show_cycle_type') end  --展示周期类型
      ,case when get_json_object(a.attr,'$.sub_pos_id') = '' then null else get_json_object(a.attr,'$.sub_pos_id') end --子资源位ID
      ,case when split(get_json_object(a.attr,'$.sub_pos_id'),'\\.')[0] = '' then null else split(get_json_object(a.attr,'$.sub_pos_id'),'\\.')[0] end  --attr.sub_pos_id以.分割后取第1位 --子资源位模块
      ,case when split(get_json_object(a.attr,'$.sub_pos_id'),'\\.')[1] = '' then null else split(get_json_object(a.attr,'$.sub_pos_id'),'\\.')[1] end  --attr.sub_pos_id以.分割后取第2位 --子资源位序列
      ,case when get_json_object(a.attr,'$.sub_module_name') = '' then null else get_json_object(a.attr,'$.sub_module_name') end  --资源位所在子模块名称
      ,case when get_json_object(a.attr,'$._tpi') = '' then null else get_json_object(a.attr,'$._tpi') end  --页面唯一ID
      ,'' --get_json_object(a.attr,'$._tpi') --父页面唯一ID
      ,a.puri --父URI
      ,parse_url(a.puri,'HOST') --父页面域名
      ,parse_url(a.puri,'PATH') --父页面路径
      ,'保留字段' --父页面去参URI
      ,'' --get_json_object(a.attr,'$._tp') 父页面名称
      ,'保留字段' --basic.pplv1（暂定，还没值） 父页面一级分类
      ,'保留字段' --basic.pplv2（暂定，还没值） 父页面二级分类
      ,'保留字段' --basic.pplv3（暂定，还没值） 父页面三级分类
      ,'保留字段' --basic.ppt（暂定，还没值） 父页面类型
      ,'保留字段' --basic.ppc（暂定，还没值） 父页面频道
      ,map("ppii","ppit") --父页面参数
      ,a.uri  --URI
      ,parse_url(a.uri,'HOST') --域名
      ,parse_url(a.uri,'PATH') --页面路径
      ,'保留字段' --去参URI
      ,case when get_json_object(a.attr,'$._tp') = '' then null else get_json_object(a.attr,'$._tp') end --页面名称
      ,'保留字段' --basic.plv1（暂定，还没值） 页面一级分类
      ,'保留字段' --basic.plv2（暂定，还没值） 页面二级分类
      ,'保留字段' --basic.plv3（暂定，还没值） 页面三级分类
      ,'保留字段' --basic.pt（暂定，还没值） 页面类型
      ,'保留字段' --basic.pc（暂定，还没值） 页面频道
      ,'保留字段' --页面资源位ID
      ,'保留字段' --页面资源位业务
      ,'保留字段' --页面资源位页面
      ,'保留字段' --页面资源位模块
      ,'保留字段' --页面资源位序列
      ,'保留字段' --页面业务系统ID
      ,'保留字段' --页面业务系统
      ,map("pii","pit") --页面参数
      ,'保留字段' --basic.xxx(还未定),保留字段 转换非原生URI
      -- ,'保留字段' --basic.abtest（目前还没值） --页面A/B Test
      ,a.ref --上一页URI
      ,parse_url(a.ref,'HOST') --上一页页面域名
      ,parse_url(a.ref,'PATH') --上一页页面路径
      ,'保留字段' -- ,基于ref解析 --上一页面去参URI
      ,''  --上一页页面名称
      ,'保留字段' --basic.rplv1（暂定，还没值）
      ,'保留字段' --basic.rplv2（暂定，还没值）
      ,'保留字段' --basic.rplv3（暂定，还没值）
      ,'保留字段' --basic.rpt（暂定，还没值）
      ,'保留字段' --basic.rpc（暂定，还没值）
      ,map("rpii","rpit") --父页面参数
      ,case when get_json_object(a.attr,'$._wakeby') = '' then null else get_json_object(a.attr,'$._wakeby') end   --是否从H5页面唤醒
      ,case when get_json_object(a.attr,'$._tl') = '' then null else get_json_object(a.attr,'$._tl') end  --访问层级
      ,case when get_json_object(a.attr,'$._tpa') = '' then null else get_json_object(a.attr,'$._tpa') end  --触发点路径
      ,case when get_json_object(a.attr,'$._tpt') = '' then null else get_json_object(a.attr,'$._tpt') end  --触发点
      ,json_trigger(a.attr) as trigger_link --细化触发点
      ,json_trigger(a.attr,1) as trigger_names --细化优化触发点
      ,case when get_json_object(a.attr,'$.in') = '' then null else get_json_object(a.attr,'$.in') end  --进入方式
      ,case when get_json_object(a.attr,'$.out') = '' then null else get_json_object(a.attr,'$.out') end  --退出方式
      ,case when get_json_object(a.attr,'$.index_in_launch') = '' then null else get_json_object(a.attr,'$.index_in_launch') end  --页面排序
      ,case when get_json_object(a.attr,'$.type') = '' then null else get_json_object(a.attr,'$.type') end  --页面是否为原生
      ,case when a.app_code='cn.mafengwo.www'
            then cast(get_json_object(a.attr,'$.duration') as double)
            else cast(get_json_object(a.attr,'$.duration')as double)/1000 end --页面停留时长
      ,case when get_json_object(a.attr,'$.mddid') = '' then null else get_json_object(a.attr,'$.mddid') end  --页面目的地ID
      ,case when get_json_object(get_json_object(a.attr,'$.travel_status'),'$.status') = '' then null else get_json_object(get_json_object(a.attr,'$.travel_status'),'$.status') end  --旅行状态
      ,case when get_json_object(get_json_object(a.attr,'$.travel_status'),'$.mddid') = '' then null else get_json_object(get_json_object(a.attr,'$.travel_status'),'$.mddid') end  --旅行目的地ID
      ,case when get_json_object(get_json_object(a.attr,'$.travel_status'),'$.strategy') = '' then null else get_json_object(get_json_object(a.attr,'$.travel_status'),'$.strategy') end  --旅行目的地策略
      ,'策略待定' --搜索关键字
      ,'保留字段' --order_id
      ,'保留字段' --attr.cd1 自定义扩展字段1
      ,'保留字段' --attr.cd2 自定义扩展字段2
      ,'保留字段' --attr.cd3 自定义扩展字段3
      ,'保留字段' --attr.cd4 自定义扩展字段4
      ,'保留字段' --attr.cd5 自定义扩展字段5
      ,'保留字段' --attr.cd6 自定义扩展字段6
      ,'保留字段' --attr.cd7 自定义扩展字段7
      ,'保留字段' --attr.cd8 自定义扩展字段8
      ,'保留字段' --attr.cd9 自定义扩展字段9
      ,'保留字段' --attr.cd10 自定义扩展字段10
      ,'保留字段' --attr.cd11 自定义扩展字段11
      ,'保留字段' --attr.cd12 自定义扩展字段12
      ,'保留字段' --attr.cd13 自定义扩展字段13
      ,'保留字段' --attr.cd14 自定义扩展字段14
      ,'保留字段' --attr.cd15 自定义扩展字段15
      ,'保留字段' --attr.cd16 自定义扩展字段16
      ,'保留字段' --attr.cd17 自定义扩展字段17
      ,'保留字段' --attr.cd18 自定义扩展字段18
      ,'保留字段' --attr.cd19 自定义扩展字段19
      ,'保留字段' --attr.cd20 自定义扩展字段20
      ,case when a.event_code like 'click%' then 'click'
            when a.event_code like 'show%'  then 'show'
            end --事件类型
      ,'APP' --数据站点
      ,a.app_code --客户端APP标识
  from default.mobile_event_parquet a
 where a.dt = '20181226' and catalog = 'default'
   and (a.event_code like 'click%' or a.event_code like 'show%')
   and a.app_code in ('cn.mafengwo.www','com.mfw.roadbook','cn.mafengwo.www.ipad') ;




