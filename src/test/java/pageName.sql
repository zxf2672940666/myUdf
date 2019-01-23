select
  case
    when parse_url(`p`.`uri`, 'HOST') = 'app.mafengwo.cn' or `p`.`uri` regexp 'app.mafengwo.cn' then
    case
      when parse_url(`p`.`uri`, 'PATH') regexp '/user/comment_list' then '用户点评列表' --http://app.mafengwo.cn/user/comment_list?_ouri=http%253A%252F%252Fm.mafengwo.cn%252Fnb%252Fpublic%252Fsharejump.php%253Ftype%253D29%2526uid%253D93999970&user_id=93999970
      when `j`.`page_name` <> '' then `j`.`page_name`
      else null
    end
    when parse_url(`p`.`uri`, 'HOST') = 'payitf.mafengwo.cn' then case
      when parse_url(`p`.`uri`, 'PATH') regexp 'cloudauth/material' then 'pay_four_points_check'
      when parse_url(`p`.`uri`, 'PATH') regexp 'esign/person/auth' then 'pay_face_check'
      when parse_url(`p`.`uri`, 'PATH') regexp 'order/pay_v2/jumppay' then 'pay_jump_to_third'
      when parse_url(`p`.`uri`, 'PATH') regexp '/pay_v2/gopay' then 'pay_jump_to_paycenter'
      when parse_url(`p`.`uri`, 'PATH') regexp '/return/' then 'pay_back_to_order' --是用户主动返回 一般没支付成功
      when parse_url(`p`.`uri`, 'PATH') regexp '/pay_v2/guide' then 'pay_sucess' --guide 收银台 付完款自动回调，也有情况是 没支付成功 ，刷新收银台
      when parse_url(`p`.`uri`, 'PATH') regexp '/pay_v2/history' then 'pay_back_to_order' --没支付成功，从收银台返回业务下单页（或者自定义）
      when parse_url(`p`.`uri`, 'PATH') regexp '/order/pay_v2/404' then 'pay_fail_404'
      else null
    end
    when parse_url(`p`.`uri`, 'HOST') = 'passport.mafengwo.cn' then case
      when parse_url(`p`.`uri`, 'PATH') regexp 'forget' then 'account_forget'
      when parse_url(`p`.`uri`, 'QUERY', 'return_url') <> '' then 'account_login'
      when parse_url(`p`.`uri`, 'PATH') regexp 'passport.mafengwo.cn/$' then 'account_login'
      when parse_url(`p`.`uri`, 'PATH') regexp 'reset' then 'account_reset'
      when parse_url(`p`.`uri`, 'PATH') regexp 'setting/wallet/' then 'account_wallet'
      when parse_url(`p`.`uri`, 'PATH') regexp '/wechat/auth/' then 'account_wechat_auth'
      when parse_url(`p`.`uri`, 'PATH') regexp '/qq/auth/' then 'account_qq_auth'
      when parse_url(`p`.`uri`, 'PATH') regexp '/qq' then 'account_qq_auth'
      when parse_url(`p`.`uri`, 'PATH') regexp '/wechat' then 'account_wechat_auth'
      when parse_url(`p`.`uri`, 'PATH') regexp '/setting/security/' then 'account_security'
      when parse_url(`p`.`uri`, 'PATH') regexp '/' then 'account_login'
      else null
    end
    when parse_url(`p`.`uri`, 'HOST') = 'w.mafengwo.cn' then --regexp_extract(parse_url(uri,'PATH'),'/([^/]+)/(?:index|edit|detail|list|undefined)\.html',1)
    case
      --when j.page_name='通用浏览器' then
      --case
      -- 容错机制，非补全机制
      when parse_url(`p`.`uri`, 'PATH') regexp 'calendar2019' then '马蜂窝台历' --https://w.mafengwo.cn/calendar2019/preheat
      when parse_url(`p`.`uri`, 'PATH') regexp '2018-gq-light/' then '点亮全球'
      when parse_url(`p`.`uri`, 'PATH') regexp '/activity_light_global/' then '点亮全球'
      when parse_url(`p`.`uri`, 'PATH') regexp '/activity_footballbaby_home/' then '世界杯足球宝贝'
      when parse_url(`p`.`uri`, 'PATH') regexp '/sales_promotion/' then '电商促销活动' --https://w.mafengwo.cn/sales_promotion/recruit/index
      when parse_url(`p`.`uri`, 'PATH') regexp '/activity_fancy_note/' then '游记活动'
      when parse_url(`p`.`uri`, 'PATH') regexp '/mfw_activity_new/' then '社区活动'
      when parse_url(`p`.`uri`, 'PATH') regexp 'aps/book/' then '品牌活动' --https://w.mafengwo.cn/aps/book/unknowtravel/v2/index.html
      when parse_url(`p`.`uri`, 'PATH') regexp 'activity_honeycomb' then '品牌活动' --https://w.mafengwo.cn/activity_honeycomb/
      when parse_url(`p`.`uri`, 'PATH') regexp 'activity_group_intro' then '未知饭局' --https://w.mafengwo.cn/activity_group_intro/479729
      when parse_url(`p`.`uri`, 'PATH') regexp '/h5-best-answerer/' then '问答活动' --https://w.mafengwo.cn/h5-best-answerer/index.html#/
      when parse_url(`p`.`uri`, 'PATH') regexp '/find_traveler/' then '问答活动' --答题大赛寻找旅行X星人    https://w.mafengwo.cn/find_traveler/index.html?id=10#/
      when parse_url(`p`.`uri`, 'PATH') regexp '/activity_qList/' then '问答活动'
      when parse_url(`p`.`uri`, 'PATH') regexp '/worldcup2018/' then '世界杯活动'
      when parse_url(`p`.`uri`, 'PATH') regexp '/worldcup_task/' then '世界杯活动任务'
      when parse_url(`p`.`uri`, 'PATH') regexp '/activity_qList/index' then '问答精选讨论'
      when parse_url(`p`.`uri`, 'PATH') regexp '/daka/' then '用户打卡页'
      when parse_url(`p`.`uri`, 'PATH') regexp '/gy-delicacy/' then '目的地活动'
      when parse_url(`p`.`uri`, 'PATH') regexp '/activity_reserve_note/' then '游记活动' --https://w.mafengwo.cn/activity_reserve_note/index
      when parse_url(`p`.`uri`, 'PATH') regexp '/activity_dt_destination/' then '问答活动' --https://w.mafengwo.cn/activity_dt_destination/index.html?id=48#/
      when `p`.`uri` regexp '/sales_fe/index.html#/wxapp/laxin/index' then 'app下载' --https://w.mafengwo.cn/sales_fe/index.html#/wxapp/laxin/index
      --容错机制下，对重点内容补全
      when parse_url(`p`.`uri`, 'PATH') regexp '/group_h5/article_detail/' then '小组介绍页' --小组介绍页   https://w.mafengwo.cn/group_h5/article_detail/******
      when parse_url(`p`.`uri`, 'PATH') regexp '/group_h5/comment_list/' then '小组评论列表页' --小组评论列表页 https://w.mafengwo.cn/group_h5/comment_list/*******
      when parse_url(`p`.`uri`, 'PATH') regexp '/group_h5/group_list/' then '小组列表页' --小组列表页   https://w.mafengwo.cn/group_h5/group_list/*******
      when parse_url(`p`.`uri`, 'PATH') regexp '/group_h5/group_my/' then '我的小组页' --我的小组页   https://w.mafengwo.cn/group_h5/group_my/
      when `p`.`uri` regexp 'group_h5/publish\\?id=' then '发布活动页面' --发布活动页面  https://w.mafengwo.cn/group_h5/publish?id=******
      when parse_url(`p`.`uri`, 'PATH') regexp '/group_h5/publish' then '小组活动发布页' --小组活动发布页 https://w.mafengwo.cn/group_h5/publish
      when parse_url(`p`.`uri`, 'PATH') regexp '/activity_city/home' then '目的地同城活动主页面' --目的地同城活动主页面    https://w.mafengwo.cn/activity_city/home?activity_id=****
      when parse_url(`p`.`uri`, 'PATH') regexp '/activity_city/Home' then '目的地同城活动主页面'
      when parse_url(`p`.`uri`, 'PATH') regexp 'activity_city/launch' then '同城活动发起页面' --同城活动发起页面    https://w.mafengwo.cn/activity_city/launch?mdd_id=1&mddId=******
      when `p`.`uri` regexp '/activity_city/\\?mdd_id=' then '目的地同城活动列表页' --全部同城活动列表页   https://w.mafengwo.cn/activity_city/list
      when parse_url(`p`.`uri`, 'PATH') regexp '/activity_city/list' then '全部同城活动列表页' --目的地同城活动列表页  https://w.mafengwo.cn/activity_city/list?mdd_id=*****
      when parse_url(`p`.`uri`, 'PATH') regexp '/activity_city/manage' then '同城活动管理页' --https://w.mafengwo.cn/activity_city/manage?activity_id=60615&user_type=1
      when parse_url(`p`.`uri`, 'PATH') regexp '/sales_exp_report/index\\.html' then '旅行体验首页' -- https://w.mafengwo.cn/sales_exp_report/index.html?from=order_center
      when parse_url(`p`.`uri`, 'PATH') regexp 'sales_exp_report/detail\\.html' then '旅行体验详情页'
      when parse_url(`p`.`uri`, 'PATH') regexp 'sales_exp_report/comment' then '旅行体验点评' --https://w.mafengwo.cn/sales_exp_report/comment.html?report_id=90802
      when parse_url(`p`.`uri`, 'PATH') regexp 'trans_car_flight/index.html' then '接送机首页' --https://w.mafengwo.cn/trans_car_flight/index.html#/
      when parse_url(`p`.`uri`, 'PATH') regexp '/trans_car_flight/orderWrite.html' then '接送机提交订单' --https://w.mafengwo.cn/trans_car_flight/orderWrite.html?carTypeCode=300005&carTypeName=%E8%B1%AA%E5%8D%8E5%E5%BA%A7&vendorCode=YITU8&merchantPriceMark=40e867db2f389e1f&price=653&activityPrice=0&bizType=1&airportCode=TPE&depAddress=%E6%A1%83%E5%9B%AD%E6%9C%BA%E5%9C%BA&depLon=121.233002&depLat=25.0777&arrAddress=269%E5%8F%B0%E6%B9%BE%E5%AE%9C%E8%98%AD%E7%B8%A3%E5%86%AC%E5%B1%B1%E9%84%89%E8%8C%84%E8%8B%B3%E8%B7%AF219%E8%99%9F&cityName=%E5%8F%B0%E5%8C%97&arrLon=121.8061505&arrLat=24.6562023&serviceTime=2018-11-02%2023%3A55%3A00&baggageCount=2&seatCount=4&insurancePrice=0&liftcardPrice=0&guidePrice=0&childrenPrice=0&flightNo=CI928&airlineCompany=%25E4%25B8%25AD%25E5%258D%258E%25E8%2588%25AA%25E7%25A9%25BA&tagInfo=%E4%B8%AD%E6%96%87%E5%8F%B8%E6%9C%BA&serviceTimeBj=2018-11-01%2021%3A55%3A00&imgUrl=https%3A%2F%2Fp4-q.mafengwo.net%2Fs11%2FM00%2F90%2FC6%2FwKgBEFtn7WuAD_1jAAAwSnetlKU456.png
      when `p`.`uri` regexp 'sales_customize/scheme_info\\?demand_id' then '电商定制方案详情'
      when parse_url(`p`.`uri`, 'PATH') regexp '/sales_customize/demand_submission' then '电商定制需求提交' --https://w.mafengwo.cn/sales_customize/demand_submission?line_mdd_id=14407&entry_type=&entry_id=&hash_key=
      when parse_url(`p`.`uri`, 'PATH') regexp '/sales_customize/mdd_entrance' then '电商目的地定制频道' --https://w.mafengwo.cn/sales_customize/mdd_entrance?line_mdd_id=10065&track_id=1000
      when parse_url(`p`.`uri`, 'PATH') regexp '/sales_customize/submit_success' then '电商定制需求提交成功' --https://w.mafengwo.cn/sales_customize/submit_success?demand_id=1615341583524497
      when parse_url(`p`.`uri`, 'PATH') regexp '/sales_customize/contract' then '电商定制查看合同'
      when parse_url(`p`.`uri`, 'PATH') regexp '/sales_customize/scheme' then '电商定制方案详情' -- https://w.mafengwo.cn/sales_customize/scheme?id=1615301099196474
      when parse_url(`p`.`uri`, 'PATH') regexp '/trans_flight_lpc/content.html' then '大交通频道-特价机票首页' ----- update by SynHao at 20180914 09:28
      when parse_url(`p`.`uri`, 'PATH') regexp '/trans_flight_lpc/stroke.html' then '大交通频道-特价机票行程页'
      when parse_url(`p`.`uri`, 'PATH') regexp '/trans_car_flight/selectCar.html' then '接送机租车频道-接送机选车页' --https://w.mafengwo.cn//trans_car_flight/selectCar.html
      when parse_url(`p`.`uri`, 'PATH') regexp '/trans_car_flight/activity.html' then '接送机租车频道-大促活动' --https://w.mafengwo.cn//trans_car_flight/selectCar.html
      when parse_url(`p`.`uri`, 'PATH') regexp '/sales/shop/page/impression' then '电商店铺印象'
      when parse_url(`p`.`uri`, 'PATH') regexp '/sales/order/booking/supply' then '交易频道-出行信息完善页'
      when `p`.`uri` regexp '/sales_fe/index.html#/cityPlay/' then '电商城市玩法' --https://w.mafengwo.cn/sales_fe/index.html#/cityPlay/10183
      when `p`.`uri` regexp '/sales_fe/index.html#/soptTicket' then '电商门票' --https://w.mafengwo.cn/sales_fe/index.html#/soptTicket?poi_id=6102028&cdid=945&mi=12562%7C972
      when `p`.`uri` regexp '/sales_fe/index.html\\?\\d+#/soptTicket' then '电商门票' --https://w.mafengwo.cn/sales_fe/index.html#/soptTicket?poi_id=6102028&cdid=945&mi=12562%7C972
      when `p`.`uri` regexp '/sales_car/index.html#/car/' then '大交通-接送机首页' --https://w.mafengwo.cn/sales_car/index.html#/car/osd
      when `p`.`uri` regexp '/sales_fe/index.html#/temai' then '电商特卖' --https://w.mafengwo.cn/sales_fe/index.html#/temai
      when `p`.`uri` regexp '/sales_fe/index.html#/guideAlbum' then '电商攻略' --https://w.mafengwo.cn/sales_fe/index.html#/guideAlbum?id=466
      when `p`.`uri` regexp '/sales_fe/index.html#/newUserGift' then '电商优惠券'
      when `p`.`uri` regexp '/sales_fe/index.html#/popularList' then '附近好店人气榜'
      when `p`.`uri` regexp '/sales_fe/index.html#/vip/' then '金卡会员'
      when `p`.`uri` regexp '/sales_fe/index.html#/wanfa/detail' then '电商玩法详情页' --https://w.mafengwo.cn/sales_fe/index.html#/wanfa/detail/333
      when `p`.`uri` regexp '/sales_train/index.html#/train/detail/\\d+' then '大交通火车票订单详情' --https://w.mafengwo.cn/sales_train/index.html#/train/detail/1615185184145766
      when `p`.`uri` regexp '/sales_fe/index.html#/quickbook/' then '电商必体验产品列表页' --https://w.mafengwo.cn/sales_fe/index.html#/quickbook/10035/49646
      when `p`.`uri` regexp '/sales_train/index.html#/train/list' then '大交通火车票列表页' --https://w.mafengwo.cn/sales_train/index.html#/train/list?depart_code=beijing&depart_name=%E5%8C%97%E4%BA%AC&dest_code=shanghai&dest_name=%E4%B8%8A%E6%B5%B7&depart_date=2018-10-25&high_speed_rail=0
      when `p`.`uri` regexp '/sales_fe/index.html#/packageHub' then '电商打包产品' --https://w.mafengwo.cn/sales_fe/index.html#/packageHub?id=118
      when `p`.`uri` regexp 'sales_fe/index.html#/ticketCommentDetail' then '电商门票点评' --  https://w.mafengwo.cn/sales_fe/index.html#/ticketCommentDetail?poi_id=5429475
      when `p`.`uri` regexp '/sales_fe/index.html\\?_time=\\d+#/shop/trends/' then '电商店铺动态' --70    https://w.mafengwo.cn/sales_fe/index.html?_time=1540224566#/shop/trends/51935
      when `p`.`uri` regexp '/sales_fe/index.html\\?_time=\\d+#/shop/video' then '电商店铺视频' --https://w.mafengwo.cn/sales_fe/index.html?_time=1540651516#/shop/video/22704
      else null --end
      --else j.page_name
    end
    when parse_url(`p`.`uri`, 'HOST') = 'imfw.cn' then 'short_url'
    when parse_url(`p`.`uri`, 'HOST') = 'topic.mafengwo.cn' then 'topic_activity'
    when parse_url(`p`.`uri`, 'HOST') in ('www.mafengwo.cn', 'm.mafengwo.cn') then case
      --酒店
      when parse_url(`p`.`uri`, 'PATH') regexp '/hotel/|/hotelzd/|/youyu/|/hotel_zx/' then case
        --when j.page_name='通用浏览器' or j.page_name='酒店下单通用浏览器' then
        --case
        when `p`.`uri` regexp 'from=app_travelguide_ios' then '跳转中间页'
        when `p`.`uri` regexp 'from=app_travelguide_android' then '跳转中间页'
        when `p`.`uri` regexp 'from=app_ios' then '跳转中间页'
        when `p`.`uri` regexp 'from=app_android' then '跳转中间页' --https://m.mafengwo.cn/hotel/room?sLabel=20184003279259&poi_id=95489&check_in=2019-01-01&check_out=2019-01-08&adults_num=2&children_num=0&children_age=&ota=17&ota_id=17
        when parse_url(`p`.`uri`, 'PATH') regexp 'hotel/booking/go2booking.php' then 'OTA酒店预订'
        when parse_url(`p`.`uri`, 'PATH') regexp 'hotel/room' then case
          when parse_url(`p`.`uri`, 'REF') regexp 'room' then '酒店下单流程-房型列表页'
          when parse_url(`p`.`uri`, 'REF') regexp 'booking_result' then '酒店下单流程-预订结果页'
          when parse_url(`p`.`uri`, 'REF') regexp 'booking_form' then '酒店下单流程-表单页'
          when parse_url(`p`.`uri`, 'REF') regexp 'payment' then '酒店下单流程-支付页'
          when parse_url(`p`.`uri`, 'REF') regexp 'booking_faq' then '酒店下单流程-咨询页'
          else '酒店下单流程-房型列表页'
        end
        when parse_url(`p`.`uri`, 'PATH') regexp 'hotel/booking_result' then case
          when parse_url(`p`.`uri`, 'REF') regexp 'room' then '酒店下单流程-房型列表页'
          when parse_url(`p`.`uri`, 'REF') regexp 'booking_result' then '酒店下单流程-预订结果页'
          when parse_url(`p`.`uri`, 'REF') regexp 'booking_form' then '酒店下单流程-表单页'
          when parse_url(`p`.`uri`, 'REF') regexp 'payment' then '酒店下单流程-支付页'
          when parse_url(`p`.`uri`, 'REF') regexp 'booking_faq' then '酒店下单流程-咨询页'
          else '酒店下单流程-预订结果页'
        end
        when parse_url(`p`.`uri`, 'PATH') regexp 'hotel/booking_form' then case
          when parse_url(`p`.`uri`, 'REF') regexp 'room' then '酒店下单流程-房型列表页'
          when parse_url(`p`.`uri`, 'REF') regexp 'booking_result' then '酒店下单流程-预订结果页'
          when parse_url(`p`.`uri`, 'REF') regexp 'booking_form' then '酒店下单流程-表单页'
          when parse_url(`p`.`uri`, 'REF') regexp 'payment' then '酒店下单流程-支付页'
          when parse_url(`p`.`uri`, 'REF') regexp 'booking_faq' then '酒店下单流程-咨询页'
          else '酒店下单流程-表单页'
        end
        when parse_url(`p`.`uri`, 'PATH') regexp 'hotel/payment' then case
          when parse_url(`p`.`uri`, 'REF') regexp 'room' then '酒店下单流程-房型列表页'
          when parse_url(`p`.`uri`, 'REF') regexp 'booking_result' then '酒店下单流程-预订结果页'
          when parse_url(`p`.`uri`, 'REF') regexp 'booking_form' then '酒店下单流程-表单页'
          when parse_url(`p`.`uri`, 'REF') regexp 'payment' then '酒店下单流程-支付页'
          when parse_url(`p`.`uri`, 'REF') regexp 'booking_faq' then '酒店下单流程-咨询页'
          else '酒店下单流程-支付页'
        end
        when parse_url(`p`.`uri`, 'PATH') regexp '/hotel/order_detail/index' then '酒店订单详情'
        when parse_url(`p`.`uri`, 'PATH') regexp '/youyu/order_detail.php' then '酒店订单详情'
        when parse_url(`p`.`uri`, 'PATH') regexp '/hotel/booking_faq' then '酒店预定帮助'
        when parse_url(`p`.`uri`, 'PATH') regexp 'hotel/pickupinfo' then '酒店接机介绍' --https://m.mafengwo.cn/hotel/pickupinfo
        when parse_url(`p`.`uri`, 'PATH') regexp '/hotelzd/index.php/detail' then '酒店直订订单详情页' --  http://m.mafengwo.cn/hotelzd/index.php/info?id=7807383&from=app&sLabel=5a60a52c-0704-1df9-cde7-e3056ffbf2d6_1540200008&sCheckIn=2018-10-22&sCheckOut=2018-10-23&iLogId=20184005111470&sFrom=app_travelguide_android
        when parse_url(`p`.`uri`, 'PATH') regexp 'hotelzd/index_v2.php/detail' then '酒店直订订单详情页'
        when parse_url(`p`.`uri`, 'PATH') regexp '/hotelzd/index.php' then '酒店直订酒店详情页' --  http://m.mafengwo.cn/hotelzd/index.php/info?id=7807383&from=app&sLabel=5a60a52c-0704-1df9-cde7-e3056ffbf2d6_1540200008&sCheckIn=2018-10-22&sCheckOut=2018-10-23&iLogId=20184005111470&sFrom=app_travelguide_android
        when parse_url(`p`.`uri`, 'PATH') regexp 'hotelzd/index_v2.php' then '酒店直订酒店详情页' --https://m.mafengwo.cn/hotelzd/index_v2.php?iHotelId=15751569&from=app&sLabel=5bbab09e-a479-6c36-b9ee-87d7cd4935d2_1540391839&sCheckIn=2018-11-13&sCheckOut=2018-11-19&iLogId=20184005663101&sFrom=app_travelguiHotelIde_androiHotelId
        when parse_url(`p`.`uri`, 'PATH') regexp '/hotelzd/create_order.php' then '酒店直订提交订单页' --/hotelzd/create_order.php
        when parse_url(`p`.`uri`, 'PATH') regexp '/youyu/order_detail.php' then '酒店订单详情' --https://m.mafengwo.cn/youyu/order_detail.php?sOrderId=51181014016470199
        when parse_url(`p`.`uri`, 'PATH') regexp '/hotel_zx/order/detail.php' then '酒店订单详情' --http://www.mafengwo.cn/hotel_zx/order/detail.php?sOrderId=51181002015228174&sVCode=f275a4916087f6b1b04cbb7754c24d0f&sLang=en
        when parse_url(`p`.`uri`, 'PATH') regexp '/hotel/feature_gonglve/' then '酒店特色攻略' --/hotel/feature_gonglve/1
        when parse_url(`p`.`uri`, 'PATH') regexp '/hotel/\\d+/' then '酒店列表页' --https://m.mafengwo.cn/hotel/15284/
        when parse_url(`p`.`uri`, 'PATH') regexp '/hotel/license' then '酒店合作伙伴' --https://m.mafengwo.cn/hotel/license#/otaList
        when parse_url(`p`.`uri`, 'PATH') regexp '/hotel/\\d+\\.html' then '酒店详情页'
        when parse_url(`p`.`uri`, 'PATH') regexp '/hotel/area_\\d+\\.html' then '酒店列表页' --https://m.mafengwo.cn/hotel/area_46042.html
        when parse_url(`p`.`uri`, 'PATH') regexp '/hotel/feature/\\d+/' then '酒店特色列表页' --/hotel/feature/10460/
        when parse_url(`p`.`uri`, 'PATH') regexp '/hotel/feature_mdd/\\d+/' then '酒店目的地特色列表页' --/hotel/feature_mdd/10208/25769/
        when parse_url(`p`.`uri`, 'PATH') regexp '/hotelzd/pay.php' then '酒店直订支付页'
        when parse_url(`p`.`uri`, 'PATH') regexp '/hotelzd/success.php' then '酒店直订订单支付成功' --https://www.mafengwo.cn/hotelzd/success.php?sOrderId=51181022016776848&anonymous=62bf995473930d814f80cb5acd1c8426
        when parse_url(`p`.`uri`, 'PATH') regexp '/hotel/activity/' then '酒店大促活动' --https://www.mafengwo.cn/hotelzd/success.php?sOrderId=51181022016776848&anonymous=62bf995473930d814f80cb5acd1c8426
        --end
        --else j.page_name
      end
      when parse_url(`p`.`uri`, 'PATH') regexp '/flight/' then case
        --when j.page_name='通用浏览器' then
        --case
        when parse_url(`p`.`uri`, 'PATH') regexp '/flight/book' then '机票提交订单页' --https://m.mafengwo.cn/flight/book?departCity=%E5%8C%97%E4%BA%AC&departCode=BJS&destCity=%E6%B5%B7%E5%8F%A3&destCode=HAK&type=roundWay&otaId=101&comeFromGo=&comeFromBack=&departDate=2019-02-03&destDate=2019-02-12&selected=115a663246d9c9d2979b03a7592b8e0a&seat_go_selected=6943294b2b7c951948b00d9c308d47c4&source=app_home&with_child=0
        when parse_url(`p`.`uri`, 'PATH') regexp '/flight/inter/book' then '国际机票提交订单页'
        when parse_url(`p`.`uri`, 'PATH') regexp '/flight/order/detail' then '机票订单详情' --https://m.mafengwo.cn/flight/order/detail?order_id=51180804011294109
        when parse_url(`p`.`uri`, 'PATH') regexp '/flight/inter/order/detail' then '国际机票订单详情' --https://m.mafengwo.cn/flight/inter/order/detail?order_id=51181009015980639#/index
        when parse_url(`p`.`uri`, 'PATH') regexp '/flight/guide' then '机票乘机指南'
        when parse_url(`p`.`uri`, 'PATH') regexp '/flight/list' then '机票列表页' --https://m.mafengwo.cn/flight/list?type=oneWay&with_child=0&departCode=HGH&departCity=%E6%9D%AD%E5%B7%9E&destCode=CGQ&destCity=%E9%95%BF%E6%98%A5&departDate=2018-10-31&source=app_home&status=0&adult_nums=1
        when parse_url(`p`.`uri`, 'PATH') regexp '/flight/inter/seats' then '国际机票航班信息' --https://m.mafengwo.cn/flight/inter/seats?status=0&adult_nums=1&departCode=HKT&departCity=%E6%99%AE%E5%90%89%E5%B2%9B&destCode=BKK&destCity=%E6%9B%BC%E8%B0%B7&departDate=2018-10-30&source=app_home&child_nums=0&baby_nums=0&dep_date_flightNo=20181030FD3026&curFlightListCacheKey=HKTBKK-20181030-1%230%230-All
        when parse_url(`p`.`uri`, 'PATH') regexp '/flight/order/list' then '机票订单列表页' --https://m.mafengwo.cn/flight/order/list
        when parse_url(`p`.`uri`, 'PATH') regexp '/flight/seats' then '机票航班信息' --  https://m.mafengwo.cn/flight/seats?type=oneWay&departCode=WNZ&departCity=%E6%B8%A9%E5%B7%9E&destCode=BJS&destCity=%E5%8C%97%E4%BA%AC&departDate=2018-10-27&source=bs_low_route&status=0&adult_nums=1&with_child=0&flightNo=GJ8976&curFlightListCacheKey=WNZBJS-20181027-1%230%230-All
        when parse_url(`p`.`uri`, 'PATH') regexp '/flight/$' then '电商机票首页'
        when parse_url(`p`.`uri`, 'PATH') regexp '/flight/inter/list' then '国际机票列表页' --https://m.mafengwo.cn/flight/inter/list?departCity=%E5%B9%BF%E5%B7%9E&departCode=CAN&destCity=%E6%97%A7%E9%87%91%E5%B1%B1&destCode=SFO&departDate=2019-03-28&destDate=2019-05-30&adult_nums=2&child_nums=0&baby_nums=0&status=2&source=app_home&seat_class=&flightNo=FM9302-MU589&curFlightListCacheKey=CANSFO-20190328-20190530-2%230%230-All&timeSlot=07%3A30-10%3A30&airlineCompanyName=%E4%B8%8A%E8%88%AA
        else null --end
        --else j.page_name
      end --电商
      when parse_url(`p`.`uri`, 'PATH') regexp '/sales/|/localdeals/|/cruise/' then case
        --when j.page_name='通用浏览器' then
        --case
        when parse_url(`p`.`uri`, 'PATH') regexp 'sales/\\d+\\.html' then '电商产品详情页'
        when parse_url(`p`.`uri`, 'PATH') regexp 'sales/uhelp/doc' then '用户帮助中心-电商' --   https://m.mafengwo.cn/sales/uhelp/doc
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/list.php' then '电商产品列表页' --https://m.mafengwo.cn/sales/list.php?keyword=%E6%8B%89%E6%96%AF%E7%BB%B4%E5%8A%A0%E6%96%AF+%E6%B5%B7%E8%88%AA
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/around.php' then '电商周边游频道页' --https://m.mafengwo.cn/sales/around.php?mdd_id=10065&from=mdd
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/around_tag_list' then '电商周边游标签频道页' --https://m.mafengwo.cn/sales/around_tag_list?mdd_id=10035
        when parse_url(`p`.`uri`, 'PATH') regexp '/localdeals/quickbuy' then '电商极速预订产品列表页'
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/fengqiang/index' then '电商蜂抢首页' --https://m.mafengwo.cn/sales/fengqiang/index?mddid=10728
        when parse_url(`p`.`uri`, 'PATH') regexp 'sales/wind_vane/article' then '电商玩法详情页' --https://m.mafengwo.cn/sales/wind_vane/article?id=1006
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/wind_vane/theme' then '电商玩法主题'
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/ski/\\d+\\.html' then '电商滑雪详情页' --/sales/ski/10072.html
        when parse_url(`p`.`uri`, 'PATH') regexp '/localdeals/\\d+/' then '电商当地游列表页' --https://m.mafengwo.cn/localdeals/10083/?cid=1010900
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/\\d+/' then '电商首页'
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/visa/$' then '电商签证首页' --http://www.mafengwo.cn/sales/visa/
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/alliance.php' then '电商商家入驻' ---http://www.mafengwo.cn/sales/alliance.php?step=1&mfw_sid=test_union_0
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/$' then '电商首页' --
        when parse_url(`p`.`uri`, 'PATH') regexp '/localdeals/\d+\\-' then '电商当地游列表页' --https://m.mafengwo.cn/localdeals/0-0-M11030-0-0-0-0-0.html?from=localdeals_index
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/\d+\\-' then '电商列表页' --http://www.mafengwo.cn/sales/0-0-M10067-4-0-0-0-0.html
        --订单
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/order[_/]detail' then '电商订单详情'
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/booking.php' then '电商订单确认' -- 点击预订按钮后的页面
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/refund.php' then '电商订单退款页' --https://m.mafengwo.cn/sales/refund.php?order_id=2455310201805287807534
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/reconfirm.php' then '电商订单确认页' --https://m.mafengwo.cn/sales/reconfirm.php?oid=2183414201805287809949
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/order/refund' then '电商退款' --https://m.mafengwo.cn/sales/order/refund?order_id=2412068201805127604007
        when parse_url(`p`.`uri`, 'PATH') regexp 'sales/booking/supply' then '电商预订页' --https://m.mafengwo.cn/sales/booking/supply?id=2202081201806047905121
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/order/booking/supply' then '电商预订页' --https://m.mafengwo.cn/sales/order/booking/supply?id=21579172018102411144052
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/order/booking.php' then '电商预订页' --?skus=%7B%221310162%22%3A%7B%22t%22%3A%7B%2215%22%3A1%7D%2C%22d%22%3A%7B%22s%22%3A%222018-08-07%22%7D%7D%7D&room_num=0
        --点评
        when parse_url(`p`.`uri`, 'PATH') regexp 'sales/c/comment/success' then '电商点评提交成功'
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/shop/handler/commentDetail' then '电商点评详情' --https://m.mafengwo.cn/sales/shop/handler/commentDetail?comment_id=3818999
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/c/comment/list' then '电商产品点评列表' --https://m.mafengwo.cn/sales/c/comment/list_2248547.html
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/c/comment/guide' then '电商点评引导攻略' --https://m.mafengwo.cn/sales/c/comment/guide.html
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/c/faq/list' then '电商问答列表' -- https://m.mafengwo.cn/sales/c/faq/list_2372807.html
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/c/comment/\\d+' then '电商产品点评详情'
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/c/comment/add_\\d+' then '电商发布产品点评'
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/c/faq/detail_\\d+' then '电商问答详情'
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/c/faq/add_question' then '电商提问'
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/c/faq/add_answer' then '电商回答' --https://m.mafengwo.cn/sales/c/faq/add_answer_13780268.html
        --商家统计
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/stat/ota_order_info.php' then '电商商家端订单详情'
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/stat/order.php' then '电商商家端订单列表' --https://m.mafengwo.cn/sales/stat/order.php?ota_id=1527
        when parse_url(`p`.`uri`, 'PATH') regexp 'sales/stat/home.php' then '电商我的店铺' --https://m.mafengwo.cn/sales/stat/home.php?ota_id=57287
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/stat/index.php' then '电商店铺统计页' --https://m.mafengwo.cn/sales/stat/index.php?ota_id=1870
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/stat/sku.php' then '电商商家SKU统计' --https://m.mafengwo.cn/sales/stat/sku.php?ota_id=61102
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/stat/topsellers.php' then '电商商家端topseller统计'
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/stat/pv.php' then '电商商家端PV统计'
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/stat/gmv.php' then '电商商家端GMV统计' --店铺
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/shop/\\d+' then '电商店铺首页' --/sales/shop/2712
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/shop/page/index' then '电商店铺首页' --https://m.mafengwo.cn/sales/shop/page/index?iId=55529
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/shop/page/cate' then '电商店铺分类' --https://m.mafengwo.cn/sales/shop/page/cate?iId=57485
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/shop/page/list' then '电商店铺列表' --https://m.mafengwo.cn/sales/shop/page/list?iId=57146&iCateId=42830&iCateType=2
        when parse_url(`p`.`uri`, 'PATH') regexp 'sales/ota/activity' then '电商店铺专题页' --https://m.mafengwo.cn/sales/ota/activity?id=962
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/shop/page/impression' then '电商店铺印象' --https://m.mafengwo.cn/sales/shop/page/impression?iId=2995
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/shop/migrate/store' then '电商店铺迁移' --https://m.mafengwo.cn/sales/shop/migrate/store
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/shop/page/license' then '电商店铺营业执照' --https://m.mafengwo.cn/sales/shop/page/license?iId=70633
        --商家
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/ota_order.php' then '电商商家订单管理页' --https://m.mafengwo.cn/sales/ota_order.php?type=0&ota_id=52043
        when parse_url(`p`.`uri`, 'PATH') regexp 'sales/order[_/]contract' then '电商订单合同签署页' --https://m.mafengwo.cn/sales/order_contract?order_id=2084122201805287806479
        --优惠券
        when parse_url(`p`.`uri`, 'PATH') regexp '/localdeals/get_coupon.php' then '电商优惠券'
        when parse_url(`p`.`uri`, 'PATH') regexp 'sales/newcomer.php' then '电商新手红包' --https://m.mafengwo.cn/sales/newcomer.php
        when parse_url(`p`.`uri`, 'PATH') regexp 'sales/coupon' then '我的优惠券' --https://m.mafengwo.cn/sales/coupon
        --大促
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/activity/promotion/\\d+' then '电商促销活动' --/sales/activity/promotion/158/
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/app_promotion' then '电商促销活动' --https://m.mafengwo.cn/sales/app_promotion?timestamp=1527818725
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales/activity' then '电商促销活动' --特别
        when parse_url(`p`.`uri`, 'PATH') regexp '/localdeals/mdd_topic_' then '自由行攻略'
        when parse_url(`p`.`uri`, 'PATH') regexp '/localdeals/mdd_\\d+' then '当地游首页' --https://www.mafengwo.cn/localdeals/mdd_10186/?sFrom=mdd--/localdeals/mdd
        when parse_url(`p`.`uri`, 'PATH') regexp '/localdeals/\\d+\\.html' then '电商详情页'
        when parse_url(`p`.`uri`, 'PATH') regexp '/sales$' then '商城首页'
        when parse_url(`p`.`uri`, 'PATH') regexp '/cruise/' then '邮轮频道'
        else null --end
        --else j.page_name
      end
      when parse_url(`p`.`uri`, 'PATH') regexp '/gtree/' then case
        when parse_url(`p`.`uri`, 'PATH') regexp 'gtree/like/index' then '攻略树' --https://m.mafengwo.cn/gtree/like/index?mddid=10444&type=1
        else null
      end --POI
      when parse_url(`p`.`uri`, 'PATH') regexp '/poi/|/goods/' then case
        --when j.page_name='通用浏览器' then
        --case
        when parse_url(`p`.`uri`, 'PATH') regexp 'poi/treasure/index' then 'POI美食宝藏' --https://m.mafengwo.cn/poi/treasure/index?poiid=5423392
        when parse_url(`p`.`uri`, 'PATH') regexp '/poi/poi-nojump.php' then 'POI详情' --https://m.mafengwo.cn/poi/poi-nojump.php?poiid=823
        when parse_url(`p`.`uri`, 'PATH') regexp 'poi/attraction/\\d+' then 'POI当地玩乐' --https://m.mafengwo.cn/poi/attraction/61
        when parse_url(`p`.`uri`, 'PATH') regexp 'poi/free_lunch/' then 'POI详情' --https://m.mafengwo.cn/poi/free_lunch/index?id=48&from=banner
        when parse_url(`p`.`uri`, 'PATH') regexp '/poi/sub_guide_' then 'POI导航' --/poi/sub_guide_14795.html
        when parse_url(`p`.`uri`, 'PATH') regexp 'poi/addpoi.php' then '添加POI' --https://m.mafengwo.cn/poi/addpoi.php?frome_type=weng
        when parse_url(`p`.`uri`, 'PATH') regexp 'poi/info/\\d+-simple.html' then 'POI详情' --http://m.mafengwo.cn/poi/info/2716-simple.html
        when parse_url(`p`.`uri`, 'PATH') regexp '/poi/detail' then 'POI详情' --https://m.mafengwo.cn/poi/poi/detail?poiid=3936
        when parse_url(`p`.`uri`, 'PATH') regexp '/goods/tag/' then 'POI单品标签列表页'
        when parse_url(`p`.`uri`, 'PATH') regexp '/goods/\\d+' then 'POI单品详情页'
        when parse_url(`p`.`uri`, 'PATH') regexp '/goods/recommend/' then 'POI单品推荐列表页' --https://m.mafengwo.cn/goods/recommend/1034/10765.html
        when parse_url(`p`.`uri`, 'PATH') regexp '/poi/sub_guide' then 'POI地图导览' --
        when parse_url(`p`.`uri`, 'PATH') regexp '/poi/info/\\d+' then 'POI详情' --https://m.mafengwo.cn/poi/info/3474-more.html#bgFlot_ticket
        when parse_url(`p`.`uri`, 'PATH') regexp '/poi/\\d+\\.html' then 'POI详情' --https://m.mafengwo.cn/poi/33283412.html
        when parse_url(`p`.`uri`, 'PATH') regexp '/poi/correct/' then 'POI纠错' --  http://m.mafengwo.cn/poi/correct/correct_gps?poiid=32023790
        when parse_url(`p`.`uri`, 'PATH') regexp '/poi/comment' then 'POI点评' --  https://m.mafengwo.cn/poi/comment_5502854.html
        else null --end
        --else j.page_name
      end --攻略
      when parse_url(`p`.`uri`, 'PATH') regexp '/guide/|/gonglve/' then case
        --when j.page_name='通用浏览器' then
        --case
        when parse_url(`p`.`uri`, 'PATH') regexp 'gl/guide/article/info/index' then '目的地攻略' --https://m.mafengwo.cn/gl/guide/article/info/index?article_id=6332
        when parse_url(`p`.`uri`, 'PATH') regexp '/gonglve/ziyouxing/\\d+\\.html' then '自由行攻略'
        when parse_url(`p`.`uri`, 'PATH') regexp 'gonglve/ziyouxing/public/home/index' then '攻略号主页' --  https://m.mafengwo.cn/gonglve/ziyouxing/public/home/index?public_id=813
        when parse_url(`p`.`uri`, 'PATH') regexp '/gonglve/ziyouxing/activity/' then '攻略活动' --https://m.mafengwo.cn/gonglve/ziyouxing/activity/public/index
        when parse_url(`p`.`uri`, 'PATH') regexp '/gonglve/ziyouxing/public/' then '攻略发布'
        when parse_url(`p`.`uri`, 'PATH') regexp '/gonglve/ziyouxing/comment/\\d+' then '自由行攻略点评详情' --/gonglve/ziyouxing/comment/75250.html
        when parse_url(`p`.`uri`, 'PATH') regexp '/gonglve/ziyouxing/public/index/user' then '攻略开放平台首页' --https://m.mafengwo.cn/gonglve/ziyouxing/public/index/user
        when parse_url(`p`.`uri`, 'PATH') regexp '/gonglve/ziyouxing/mdd_\\d+' then '目的地自由行攻略'
        when parse_url(`p`.`uri`, 'PATH') regexp '/gl/catalog/index' then '目的地攻略详情' --https://m.mafengwo.cn/gl/catalog/index?id=160&catalog_id=3785
        when parse_url(`p`.`uri`, 'PATH') regexp '/gonglve/zt-\\d+' then '主题攻略' --  http://www.mafengwo.cn/gonglve/zt-317.html
        when parse_url(`p`.`uri`, 'PATH') regexp '/gonglve/mdd-\\d+' then '目的地攻略' --  http://www.mafengwo.cn/gonglve/mdd-10030.html
        when parse_url(`p`.`uri`, 'PATH') regexp '/gonglve/ziyouxing/modify/' then '自由行攻略编辑' --/gonglve/ziyouxing/modify/
        else null --end
        --else j.page_name
      end --目的地
      when parse_url(`p`.`uri`, 'PATH') regexp '/mdd/|/baike/|/jd/|/cy/|/nb/activity/' then case
        --when j.page_name='通用浏览器' then
        --case
        when parse_url(`p`.`uri`, 'PATH') regexp 'mdd/meal/japan' then '日本点餐助手' --https://m.mafengwo.cn/mdd/meal/japan
        when parse_url(`p`.`uri`, 'PATH') regexp 'mdd/topic/([^/]+)' then '目的的活动' --https://m.mafengwo.cn/mdd/topic/lighting?sChannel=notice100418&sFrom=94361844
        when parse_url(`p`.`uri`, 'PATH') regexp '/mdd/list.html' then '目的地列表' --https://m.mafengwo.cn/mdd/list.html
        when parse_url(`p`.`uri`, 'PATH') regexp '/mdd/activity/' then '目的地活动' --https://m.mafengwo.cn/mdd/list.html
        when parse_url(`p`.`uri`, 'PATH') regexp '/mdd/cityroute/' then '目的地玩法'
        when parse_url(`p`.`uri`, 'PATH') regexp '/mdd/route/\\d+' then '目的地线路'
        when parse_url(`p`.`uri`, 'PATH') regexp '/baike/' then '目的地百科' -- /baike/10189_62.html
        when parse_url(`p`.`uri`, 'PATH') regexp '/jd/' then '目的地景点' --https://m.mafengwo.cn/jd/11042/gonglve.html?sExt=gonglve
        when parse_url(`p`.`uri`, 'PATH') regexp '/cy/' then '目的地餐饮' -- https://m.mafengwo.cn/cy/10065/gonglve.html
        when parse_url(`p`.`uri`, 'PATH') regexp '/nb/activity/' then '各种活动' --https://m.mafengwo.cn/nb/activity/hanabi/indexmdd
        when parse_url(`p`.`uri`, 'PATH') regexp '/mdd/article.php' then '小组活动详情' --http://m.mafengwo.cn/mdd/article.php?id=648244
        when parse_url(`p`.`uri`, 'PATH') regexp '/mdd/map/\\d+\\.html' then '目的地地图' --http://www.mafengwo.cn/mdd/map/11042.html
        else null --end
        --else j.page_name
      end --用户
      when parse_url(`p`.`uri`, 'PATH') regexp '/user/|user_wallet|home/user|/u/|/plan/' then case
        --when j.page_name='通用浏览器' then
        --case
        when parse_url(`p`.`uri`, 'PATH') regexp 'user/profile/weng' then '用户个人主页' --https://m.mafengwo.cn/user/profile/weng?uid=636812
        when parse_url(`p`.`uri`, 'PATH') regexp 'user_wallet' then '我的钱包' --https://m.mafengwo.cn/user_wallet/
        when parse_url(`p`.`uri`, 'PATH') regexp 'home/user.php' then '用户个人主页' --https://m.mafengwo.cn/home/user.php?alias=dewy207
        when parse_url(`p`.`uri`, 'PATH') regexp '/u/' then '用户个人主页'
        when parse_url(`p`.`uri`, 'PATH') regexp '/plan/route.php' then '用户个人主页'
        when parse_url(`p`.`uri`, 'PATH') regexp '/user/lv.php' then '用户等级'
        else null --end
        --else j.page_name
      end --钱包
      when parse_url(`p`.`uri`, 'PATH') regexp '/wallet' then case
        --when j.page_name='通用浏览器' then
        --case
        when parse_url(`p`.`uri`, 'PATH') regexp 'wallet_detail' then '我的钱包' --https://m.mafengwo.cn/wallet_detail/
        when parse_url(`p`.`uri`, 'PATH') regexp 'wallet_sfycr' then '信用飞-空中钱包'
        else null --end
        --else j.page_name
      end --保险
      when parse_url(`p`.`uri`, 'PATH') regexp '/insurance/' then case
        --when j.page_name='通用浏览器' then
        --case
        when parse_url(`p`.`uri`, 'PATH') regexp 'insurance/pdf/\\d+\\.pdf' then '商城保险条款'
        when parse_url(`p`.`uri`, 'PATH') regexp '/insurance/$' then '电商保险频道' --https://m.mafengwo.cn/about/licence.html
        when parse_url(`p`.`uri`, 'PATH') regexp '/insurance/detail/' then '电商保险详情'
        when parse_url(`p`.`uri`, 'PATH') regexp '/insurance/buy/' then '电商保险填单页' --https://m.mafengwo.cn/insurance/buy/210264/
        when parse_url(`p`.`uri`, 'PATH') regexp '/insurance/order/' then '电商保险订单详情' --https://m.mafengwo.cn/insurance/order/61630/
        else null --end
        --else j.page_name
      end --资讯
      when parse_url(`p`.`uri`, 'PATH') regexp '/travel-news' then case
        --when j.page_name='通用浏览器' then
        --case
        when parse_url(`p`.`uri`, 'PATH') regexp 'travel-news/$' then '资讯首页' --http://www.mafengwo.cn/travel-news/?tagId=1
        when parse_url(`p`.`uri`, 'PATH') regexp 'travel-news/\\d+\\.html' then '资讯详情页' --  https://m.mafengwo.cn/travel-news/1429578.html
        else null --end
        --else j.page_name
      end --搜索
      when parse_url(`p`.`uri`, 'PATH') regexp '/search/' then case
        --when j.page_name='通用浏览器' then
        --case
        when parse_url(`p`.`uri`, 'PATH') regexp '/nb/search/theme/mixed' then '搜索主题词'
        when parse_url(`p`.`uri`, 'PATH') regexp '/nb/search/theme/typed' then '搜索主题词查看更多'
        when parse_url(`p`.`uri`, 'PATH') regexp 'search/s.php' then '搜索结果页' --https://m.mafengwo.cn/search/s.php?q=%E5%8F%A4%E9%95%87&t=sales&seid=32978730-D089-4232-9CF8-26A47314715E&mxid=0&mid=0&mname=&kt=1
        else null --end
        --else j.page_name
      end --订单中心
      when parse_url(`p`.`uri`, 'PATH') regexp '/order_center/' then case
        --when j.page_name='通用浏览器' then
        --case
        when parse_url(`p`.`uri`, 'PATH') regexp '/order_center/index.php' then '我的订单'
        when parse_url(`p`.`uri`, 'PATH') regexp '/order_center/index' then '我的订单'
        when parse_url(`p`.`uri`, 'PATH') regexp 'order_center/index/view_order' then '我的订单' --https://m.mafengwo.cn/order_center/index/view_order?order_id=1601671966556750&busi_type=customize&_refer=list
        when parse_url(`p`.`uri`, 'PATH') regexp '/order_center?/$' then '我的订单' --https://m.mafengwo.cn/order_center/
        when parse_url(`p`.`uri`, 'PATH') regexp 'order_center/index/index' then '手机号查询订单' --https://m.mafengwo.cn/order_center/index/index?query_by_mobile=15211655208&code=0fb9151ba102d1ddc1697c452941f17b#
        when parse_url(`p`.`uri`, 'PATH') regexp 'order_center/index/query_using_mobile' then '手机号查询订单' --https://m.mafengwo.cn/order_center/index/query_using_mobile
        else null --end
        --else j.page_name
      end --社区打卡活动
      when parse_url(`p`.`uri`, 'PATH') regexp '/daka/' then case
        --when j.page_name='通用浏览器' then
        --case
        when parse_url(`p`.`uri`, 'PATH') regexp '/daka/task' then '用户打卡任务页'
        when parse_url(`p`.`uri`, 'PATH') regexp '/daka?/$' then '用户打卡页'
        when parse_url(`p`.`uri`, 'PATH') regexp '/daka/patch/index' then '用户补签页'
        when parse_url(`p`.`uri`, 'PATH') regexp '/daka/patch/success' then '用户补签成功'
        when parse_url(`p`.`uri`, 'PATH') regexp '/daka/patch' then '用户补签页'
        else null --end
        --else j.page_name
      end --嗡嗡
      when parse_url(`p`.`uri`, 'PATH') regexp '/weng/|/gl/article/weng|/weng-web/|/ww/activity/|/wengs/' then case
        --when j.page_name='通用浏览器' then
        --case
        when parse_url(`p`.`uri`, 'PATH') regexp '/weng/detail/comment_list' then '嗡嗡评论列表页' --/weng/detail/comment_list
        when parse_url(`p`.`uri`, 'PATH') regexp '/weng/detail$' then '嗡嗡详情页' --https://m.mafengwo.cn/weng/detail?id=1609760670214417
        when parse_url(`p`.`uri`, 'PATH') regexp '/gl/article/weng' then '嗡嗡周刊页' --  https://m.mafengwo.cn/gl/article/weng?id=7795
        when parse_url(`p`.`uri`, 'PATH') regexp '/weng-web/summary2017' then '嗡嗡活动页' --https://m.mafengwo.cn/weng-web/summary2017
        when parse_url(`p`.`uri`, 'PATH') regexp '/weng-web/yearbook2017' then '嗡嗡活动页' --https://m.mafengwo.cn/weng-web/yearbook2017?uid=704545
        when parse_url(`p`.`uri`, 'PATH') regexp '/ww/activity/' then '嗡嗡活动页' --https://m.mafengwo.cn/ww/activity/questionnaire/index
        when parse_url(`p`.`uri`, 'PATH') regexp '/wengs/invitation2.php' then '嗡嗡跳转页' --https://m.mafengwo.cn/wengs/invitation2.php
        else null --end
        --else j.page_name
      end --问答
      when parse_url(`p`.`uri`, 'PATH') regexp '/wenda/|/qa/|interest/list/list|subject/list/list|/ifu/' then case
        --when j.page_name='通用浏览器' then
        --case
        when parse_url(`p`.`uri`, 'PATH') regexp 'wenda/list/hot_discuss' then '讨论类问题列表' --https://m.mafengwo.cn/wenda/list/hot_discuss
        when parse_url(`p`.`uri`, 'PATH') regexp 'wenda/expert/apply' then '问答指路人申请填写页' --https://m.mafengwo.cn/wenda/expert/apply
        when parse_url(`p`.`uri`, 'PATH') regexp '/wenda/expert/index' then '问答指路人申请页'
        when parse_url(`p`.`uri`, 'PATH') regexp '/qa/expert_apply.php' then '问答指路人申请页' --/qa/expert_apply.php
        when parse_url(`p`.`uri`, 'PATH') regexp 'wenda/hot_topic.php' then '讨论类问题列表' --/wenda/hot_topic.php
        when parse_url(`p`.`uri`, 'PATH') regexp 'wenda/list/topic_question' then '话题类问题列表' --https://m.mafengwo.cn/wenda/list/topic_question?qid=11284973
        when parse_url(`p`.`uri`, 'PATH') regexp 'wenda/list/topic$' then '话题类话题列表' --https://m.mafengwo.cn/wenda/list/topic?topic_id=1001
        when parse_url(`p`.`uri`, 'PATH') regexp '/wenda/list/topic_question' then '讨论类问题详情页'
        when parse_url(`p`.`uri`, 'PATH') regexp '/wenda/list/hot_discuss' then '问答热门问题讨论页' --https://m.mafengwo.cn/wenda/list/hot_discuss
        when parse_url(`p`.`uri`, 'PATH') regexp '/wenda/list/search' then '问答列表搜索结果页' --  https://m.mafengwo.cn/wenda/list/search?key=****
        when parse_url(`p`.`uri`, 'PATH') regexp '/qa/draft_list' then '问答草稿箱'
        when parse_url(`p`.`uri`, 'PATH') regexp '/wenda/user_task/' then '问答打卡活动'
        when parse_url(`p`.`uri`, 'PATH') regexp '/wenda/replyask.php' then '问答回答页' --https://m.mafengwo.cn/wenda/replyask.php?qid=13753260
        when parse_url(`p`.`uri`, 'PATH') regexp '/wenda/$' then '问答首页'
        when parse_url(`p`.`uri`, 'PATH') regexp '/wenda/detail' then '问答详情页'
        when parse_url(`p`.`uri`, 'PATH') regexp 'interest/list/list' then '兴趣类问题列表页' --https://m.mafengwo.cn/interest/list/list?id=1056
        when parse_url(`p`.`uri`, 'PATH') regexp 'subject/list/list' then '主题类问题列表' --https://m.mafengwo.cn/subject/list/list?id=8
        when parse_url(`p`.`uri`, 'PATH') regexp '/ifu/\\d+\\.html' then 'IFU问答' --https://m.mafengwo.cn/ifu/883490.html
        when parse_url(`p`.`uri`, 'PATH') regexp '/wenda/list/publish' then '问答提问' --https://m.mafengwo.cn/wenda/list/publish
        when parse_url(`p`.`uri`, 'PATH') regexp '/wenda/area\\-\\d+.html' then '问答区域列表页' --   https://m.mafengwo.cn/wenda/area-12700.html
        else null --end
        --else j.page_name
      end --游记
      when parse_url(`p`.`uri`, 'PATH') regexp '/apps_note/|/note/|^/i/|/yj/|/group/info.php' then case
        --when j.page_name='通用浏览器' then
        --case
        when parse_url(`p`.`uri`, 'PATH') regexp '/apps_note/activity/theme/index$' then '目的地主题游记主题列表' --https://m.mafengwo.cn/apps_note/activity/theme/index?type=1&id=981
        when parse_url(`p`.`uri`, 'PATH') regexp '/apps_note/activity/theme/index/list' then '目的地主题游记游记列表' --https://m.mafengwo.cn/apps_note/activity/theme/index/list2?mddid=10099
        when parse_url(`p`.`uri`, 'PATH') regexp '/apps_note/activity/theme/index/list2' then '目的地主题游记游记列表'
        when parse_url(`p`.`uri`, 'PATH') regexp '/apps_note/activity/([^/]+)/' then '游记活动' --/apps_note/activity/subject/2018qingming/detail
        when parse_url(`p`.`uri`, 'PATH') regexp 'apps_note/columnist/item/show' then '游记活动' ---https://m.mafengwo.cn/apps_note/columnist/item/show?id=56079997
        when parse_url(`p`.`uri`, 'PATH') regexp '/note/activity/topic/index' then '游记活动'
        when parse_url(`p`.`uri`, 'PATH') regexp 'note/mddtop/apply/index/' then '宝藏游记申请' --https://m.mafengwo.cn/note/mddtop/apply/index/main?from=mdd_top
        when parse_url(`p`.`uri`, 'PATH') regexp 'note/create_index.php' then '写游记' ---http://www.mafengwo.cn/note/create_index.php?savebox=1
        when parse_url(`p`.`uri`, 'PATH') regexp '/note/create.php' then '写游记' --http://www.mafengwo.cn/note/create.php
        when parse_url(`p`.`uri`, 'PATH') regexp '/note/create.php/modify/' then '编辑游记' --/note/create.php/modify/
        when parse_url(`p`.`uri`, 'PATH') regexp 'note/upload_photo' then '游记上传图片' --https://m.mafengwo.cn/note/upload_photo_new.php?k=note_modify_qrcode_upimg32699593842132151
        when parse_url(`p`.`uri`, 'PATH') regexp 'note/upload_photo_new' then '游记上传图片' --https://m.mafengwo.cn/note/upload_photo_new.php?
        when parse_url(`p`.`uri`, 'PATH') regexp '/note/activity/([^/]+)' then '游记活动'
        when parse_url(`p`.`uri`, 'PATH') regexp '/note/task/' then '游记打卡活动'
        when parse_url(`p`.`uri`, 'PATH') regexp '/note/ginfo.php' then '游记详情'
        when parse_url(`p`.`uri`, 'PATH') regexp '/note/detail.php' then '游记详情' --http://www.mafengwo.cn/note/detail.php?iId=10355838&iPage=2&h=64707714
        when parse_url(`p`.`uri`, 'PATH') regexp '/i/\\d+' then '游记详情'
        when parse_url(`p`.`uri`, 'PATH') regexp '/yj/' then '游记列表页'
        when parse_url(`p`.`uri`, 'PATH') regexp '/group/info.php' then '游记详情' --http://www.mafengwo.cn/group/info.php?iid=3091513&page=1&h=40950218&t=0.7963559799827635
        else null --end
        --else j.page_name
      end --电商定制
      when parse_url(`p`.`uri`, 'PATH') regexp '/refer_line/|/customize/' then case
        --when j.page_name='通用浏览器' then
        --case
        --
        when parse_url(`p`.`uri`, 'PATH') regexp 'refer_line/mdd_entrance.php'
        and parse_url(`p`.`uri`, 'QUERY', 'track_id') <> '' then '电商目的地定制频道' --https://m.mafengwo.cn/refer_line/mdd_entrance.php?line_mdd_id=10183&track_id=10004
        when parse_url(`p`.`uri`, 'PATH') regexp 'refer_line/mdd_entrance.php'
        and parse_url(`p`.`uri`, 'QUERY', 'track_id') = '' then '电商商城定制频道' --https://m.mafengwo.cn/refer_line/mdd_entrance.php?line_mdd_id=10183
        when parse_url(`p`.`uri`, 'PATH') regexp 'customize/v2/quoteList' then '电商定制报价列表' --https://m.mafengwo.cn/customize/v2/quoteList?scheme_id=1599685466453166
        when parse_url(`p`.`uri`, 'PATH') regexp 'customize/v2/applyReceipt' then '电商定制开发票' --https://m.mafengwo.cn/customize/v2/applyReceipt?demand_id=1600151854618675
        when parse_url(`p`.`uri`, 'PATH') regexp 'customize/v2$'
        and parse_url(`p`.`uri`, 'QUERY', 'demand_id') <> '' then '电商定制方案详情' --https://m.mafengwo.cn/customize/v2?demand_id=1601708463074562
        when parse_url(`p`.`uri`, 'PATH') regexp 'customize/v2/signContract' then '电商定制查看合同' --https://m.mafengwo.cn/customize/v2/signContract?demand_id=1601350237474134
        when parse_url(`p`.`uri`, 'PATH') regexp 'refer_line/demand_submission.php' then '电商定制需求提交' --https://m.mafengwo.cn/refer_line/demand_submission.php?entry_type=guide&entry_id=69510
        when parse_url(`p`.`uri`, 'PATH') regexp 'refer_line/mall_entrance.php' then '电商定制频道' --https://m.mafengwo.cn/refer_line/mall_entrance.php?departure_mdd_id=10195
        when parse_url(`p`.`uri`, 'PATH') regexp 'refer_line/submit_success.php' then '电商定制需求提交成功' --https://m.mafengwo.cn/refer_line/submit_success.php?demand_id=1601672123776673
        when parse_url(`p`.`uri`, 'PATH') regexp 'refer_line/mdd_channel_whats.php' then '电商定制介绍' --https://m.mafengwo.cn/refer_line/mdd_channel_whats.php?line_mdd_id=14731&entry_id=2232120&track_id=10004
        when parse_url(`p`.`uri`, 'PATH') regexp 'customize/v2/quoteList' then '电商定制报价列表' --https://m.mafengwo.cn/customize/v2/quoteList?scheme_id=1601673407199697
        when parse_url(`p`.`uri`, 'PATH') regexp 'refer_line/aggregation.php' then '电商目的地定制频道' --https://m.mafengwo.cn/refer_line/aggregation.php?line_mdd_id=15148&track_id=10002
        when parse_url(`p`.`uri`, 'PATH') regexp 'refer_line/aggregation_country.php' then '电商目的地定制频道' --https://m.mafengwo.cn/refer_line/aggregation_country.php?line_mdd_id=10180&track_id=10002
        when parse_url(`p`.`uri`, 'PATH') regexp 'refer_line/aggregation_country_multi.php' then '电商目的地定制频道' --https://m.mafengwo.cn/refer_line/aggregation_country_multi.php?line_mdd_id=14431&track_id=10002
        when parse_url(`p`.`uri`, 'PATH') regexp 'customize/scheme.php' then '电商定制方案详情' ---https://m.mafengwo.cn/customize/scheme.php?id=1601672077129927 方案详情
        when parse_url(`p`.`uri`, 'PATH') regexp 'customize/scheme/index' then '电商定制方案详情' ---http://m.mafengwo.cn/customize/scheme/index?id=1599428965964019 方案详情 产品形态
        when parse_url(`p`.`uri`, 'PATH') regexp 'refer_line/service_intro.php' then '电商定制特色服务' ---https://m.mafengwo.cn/refer_line/service_intro.php 特色服务
        when parse_url(`p`.`uri`, 'PATH') regexp 'refer_line/mdd_entrance.php' then '电商目的地定制频道' --https://m.mafengwo.cn/refer_line/mdd_entrance.php?line_mdd_id=10051
        else null --end
        --else j.page_name
      end --视频
      when parse_url(`p`.`uri`, 'PATH') regexp '/movie/' then case
        --when j.page_name='通用浏览器' then
        --case
        when parse_url(`p`.`uri`, 'PATH') regexp '/movie/detail/' then '视频详情页' --  https://m.mafengwo.cn/movie/detail/428596.html
        else null --end
        --else j.page_name
      end --支付
      when parse_url(`p`.`uri`, 'PATH') regexp '/pay/' then case
        --when j.page_name='通用浏览器' then
        --case
        when parse_url(`p`.`uri`, 'PATH') regexp '/pay/success.php' then '支付成功事件' --  https://m.mafengwo.cn/movie/detail/428596.html
        else null --end
        --else j.page_name
      end --其他
      --when j.page_name='通用浏览器' then
      --case
      when parse_url(`p`.`uri`, 'PATH') regexp '/search.php' then '搜索主页' -- https://m.mafengwo.cn/search.php
      when parse_url(`p`.`uri`, 'PATH') regexp '/msg/' then '消息' -- http://www.mafengwo.cn/msg/sms/index
      when parse_url(`p`.`uri`, 'PATH') regexp '/mall/' then '马蜂窝周边' --https://m.mafengwo.cn/mall/
      when parse_url(`p`.`uri`, 'PATH') regexp '/auction/' then '社区拍卖' --  http://www.mafengwo.cn/auction/?date=2018-10-09
      when parse_url(`p`.`uri`, 'PATH') regexp '/traveller/' then '旅行家频道' --https://m.mafengwo.cn/traveller/traveller_user/index?id=140
      when parse_url(`p`.`uri`, 'PATH') regexp '/app_rss/tag' then '聚合标签列表页' --/app_rss/tag
      when parse_url(`p`.`uri`, 'PATH') regexp '/together/' then '结伴活动'
      when parse_url(`p`.`uri`, 'PATH') regexp '/club/' then '蜂首俱乐部'
      when parse_url(`p`.`uri`, 'PATH') regexp '/show/' then '真人秀'
      when parse_url(`p`.`uri`, 'PATH') regexp '/radio/' then '电台'
      when parse_url(`p`.`uri`, 'PATH') regexp '/friend/' then '粉丝'
      when parse_url(`p`.`uri`, 'PATH') regexp '/setting/' then '个人设置' --设置收货地址   https://m.mafengwo.cn/setting/receipt/list?app=**&obj=******
      when parse_url(`p`.`uri`, 'PATH') regexp '/callcenter/' then '客服中心'
      when parse_url(`p`.`uri`, 'PATH') regexp '^/photo/' then '图片页' --https://m.mafengwo.cn/photo/10101/scenery_2952983/162759050.html
      when parse_url(`p`.`uri`, 'PATH') regexp '/home/vip_show.php' then 'VIP'
      when parse_url(`p`.`uri`, 'PATH') regexp '/game/wager.php' then '分歧终端机'
      when parse_url(`p`.`uri`, 'PATH') regexp '/aps/book/' then '品牌活动'
      when parse_url(`p`.`uri`, 'PATH') regexp '/order_center' then '订单中心'
      when parse_url(`p`.`uri`, 'PATH') regexp '/group/glist.php' then '所有小组'
      when parse_url(`p`.`uri`, 'PATH') regexp '/gl/article/comment_list' then '评论列表页' --https://m.mafengwo.cn/gl/article/comment_list?id=8002
      when parse_url(`p`.`uri`, 'PATH') regexp '/gl/article/index' then '文章攻略' -- https://m.mafengwo.cn/gl/article/index?id=264&type=4
      when parse_url(`p`.`uri`, 'PATH') regexp '/gl/catalog/index' then '文章攻略' -- https://m.mafengwo.cn/gl/catalog/index?id=160&catalog_id=3785
      when parse_url(`p`.`uri`, 'PATH') regexp '/gl/article/public' then '文章攻略' --https://m.mafengwo.cn/gl/article/public?public_id=1198
      when parse_url(`p`.`uri`, 'PATH') regexp '/gl/article/public_user_list' then '文章攻略列表' -- https://m.mafengwo.cn/gl/article/public_user_list
      when parse_url(`p`.`uri`, 'PATH') regexp '/gl/article/preview' then '文章攻略' --https://m.mafengwo.cn/gl/article/preview?id=1222
      when parse_url(`p`.`uri`, 'PATH') regexp '/weixin/article' then '文章攻略' --https://www.mafengwo.cn/weixin/article-3404.html
      when parse_url(`p`.`uri`, 'PATH') regexp 'weather/\\d+\\.html' then '目的地天气' --https://m.mafengwo.cn/weather/11047.html
      when parse_url(`p`.`uri`, 'PATH') regexp 'about/licence' then '营业执照' --https://m.mafengwo.cn/about/licence.html
      when parse_url(`p`.`uri`, 'PATH') regexp '/about/agreement' then '服务协议' ---/about/agreement.html
      when parse_url(`p`.`uri`, 'PATH') regexp '/activity/index.php' then '社区活动' --https://m.mafengwo.cn/activity/index.php
      when parse_url(`p`.`uri`, 'PATH') regexp '/mfw_activity/index' then '社区活动'
      when parse_url(`p`.`uri`, 'PATH') regexp '/activity/college_entrance/' then '社区活动' --https://m.mafengwo.cn/activity/college_entrance/index/enter
      when parse_url(`p`.`uri`, 'PATH') regexp '/account/$' then '忘记密码' --https://m.mafengwo.cn/account/?app=travelguide
      when parse_url(`p`.`uri`, 'PATH') regexp '/g/i/\\d+' then '小组活动详情'
      when parse_url(`p`.`uri`, 'PATH') regexp '/g/\\d+.html' then '小组活动列表'
      when parse_url(`p`.`uri`, 'PATH') regexp 'short_goto.php' then '用户打卡页' --https://www.mafengwo.cn/short_goto.php?id=1696556
      --when parse_url(uri,'PATH') regexp 'setting/receipt/list' then '用户收获地址列表'--https://m.mafengwo.cn/setting/receipt/list?app=36&obj=20180520
      when parse_url(`p`.`uri`, 'PATH') regexp 'nb/public/sharejump.php' then 'sharejump'
      when parse_url(`p`.`uri`, 'PATH') regexp 'nb/public/jump_bridge.php' then 'jump_bridge' --基本都是淘宝--https://m.mafengwo.cn/nb/public/jump_bridge.php?url=http://h5.m.taobao.com/awp/core/detail.htm?id=543158497409
      when parse_url(`p`.`uri`, 'PATH') regexp 'nb/public/scaning.php' then 'scaning'
      when parse_url(`p`.`uri`, 'PATH') regexp '/404.php' then 'fail_404' --when parse_url(uri,'PATH') regexp '/mobile/catalog//1.html' then '自由行攻略'--/mobile/catalog//1.html
      --when parse_url(uri,'PATH') regexp 'traveller/traveller_list/index' then '旅行家专栏'--https://m.mafengwo.cn/traveller/traveller_list/index
      --when parse_url(uri,'PATH') regexp 'traveller/article/comment_list' then '旅行家点评'--https://m.mafengwo.cn/traveller/article/comment_list?id=441
      --when parse_url(uri,'PATH') regexp '/traveller/article/index' then '旅行家首页'
      when parse_url(`p`.`uri`, 'PATH') regexp '/im/qa/index$' then '酒店常见问题列表' --https://m.mafengwo.cn/im/qa/index?type=27&typeinfo=%7B%22hotel_order%22%3A%227350346%22%7D&tid=9&_refer=list
      when parse_url(`p`.`uri`, 'PATH') regexp 'im/qa/index/detail' then '酒店常见问题详情' --https://m.mafengwo.cn/im/qa/index/detail?tid=9&id=279&type=27&typeinfo=%7B%22hotel_order%22:%228002058%22%7D&leaf=1
      when parse_url(`p`.`uri`, 'PATH') regexp '/im/feedback/index.php' then '常见问题列表' --https://m.mafengwo.cn/im/feedback/index.php
      when parse_url(`p`.`uri`, 'PATH') regexp 'im/feedback/detail' then '常见问题详情' --https://m.mafengwo.cn/im/feedback/detail?subtype=5
      when parse_url(`p`.`uri`, 'PATH') regexp 'im/weiquan/index' then '用户维权常见问题列表' --https://m.mafengwo.cn/im/weiquan/index
      when parse_url(`p`.`uri`, 'PATH') regexp 'im/weiquan/detail' then '用户维权常见问题详情' --https://m.mafengwo.cn/im/weiquan/detail
      when parse_url(`p`.`uri`, 'PATH') regexp 'agreement.php' then '服务协议' --http://m.mafengwo.cn/agreement.php
      when parse_url(`p`.`uri`, 'PATH') regexp '/s/agreement\\.html' then '服务协议' --/s/agreement.html
      when parse_url(`p`.`uri`, 'PATH') regexp '/app/$' then 'APP下载中间页' --http://m.mafengwo.cn/app/?type=gonglve
      when parse_url(`p`.`uri`, 'PATH') regexp '/app/down/gl/' then 'APP下载中间页' --https://m.mafengwo.cn/app/down/gl/?type=pc
      when parse_url(`p`.`uri`, 'PATH') regexp '/cs.php' then 'app下载' --
      when parse_url(`p`.`uri`, 'PATH') regexp '/app/download/' then 'app下载' --
      --when parse_url(uri,'PATH') regexp 'nb/activity/shop/index' then '马蜂窝纪念品商店'--https://m.mafengwo.cn/nb/activity/shop/index
      when parse_url(`p`.`uri`, 'PATH') regexp '/oad/iad.php' then '品牌活动' --when regexp_replace(regexp_replace(parse_url(p.uri,'PATH'),'\\d+|html',''),'/$','')<>'' then regexp_replace(regexp_replace(parse_url(p.uri,'PATH'),'\\d+|html',''),'/$','')
      --可解析出url参数且url为马蜂窝
      when regexp_replace(
        regexp_replace(parse_url(`p`.`uri`, 'PATH'), '\\d+|html', ''),
        '/$',
        ''
      ) <> '' then '可解析出url参数且url为马蜂窝' --when reflect('java.net.URLDecoder', 'decode',parse_url(p.uri,'QUERY','url'),'UTF-8') like '%mafengwo%' then if(regexp_replace(parse_url(reflect('java.net.URLDecoder', 'decode',parse_url(p.uri,'QUERY','url'),'UTF-8'),'PATH'),'/$','')<>'',regexp_replace(regexp_replace(parse_url(reflect('java.net.URLDecoder', 'decode',parse_url(p.uri,'QUERY','url'),'UTF-8'),'PATH'),'/$',''),'\\d+|html',''),regexp_replace(regexp_replace(regexp_extract(reflect('java.net.URLDecoder', 'decode',parse_url(p.uri,'QUERY','url'),'UTF-8'),'//([^\\?]+)/',1),'/$',''),'\\d+|html',''))
      --可解析出url参数且url不为马蜂窝
      --when regexp_replace(reflect('java.net.URLDecoder', 'decode',parse_url(p.uri,'QUERY','url'),'UTF-8'),'/$','')<>'' and reflect('java.net.URLDecoder', 'decode',parse_url(p.uri,'QUERY','url'),'UTF-8') not like '%mafengwo%' then regexp_replace(parse_url(reflect('java.net.URLDecoder', 'decode',parse_url(p.uri,'QUERY','url'),'UTF-8'),'HOST'),'/$','')
      --非网址类型
      --when regexp_replace(regexp_extract(p.uri,'//([^\\?]+)/',1),'/$','')<>'' then regexp_replace(regexp_replace(regexp_extract(p.uri,'//([^\\?]+)/',1),'/$',''),'\\d+|html','')
      else null --end
      --else page_name
    end
    when parse_url(`p`.`uri`, 'HOST') regexp 'mafengwo' then '马蜂窝新域名页面'
    when `p`.`uri` regexp '/hotel/order_detail/index' then '酒店订单详情'
    when `p`.`uri` regexp '/hotel/activity' then '酒店大促活动'
    else '非马蜂窝页面'
  end as `page_name`
from
  a