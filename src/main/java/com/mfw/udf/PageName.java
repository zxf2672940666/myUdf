package com.mfw.udf;


import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageName extends UDF {

    public Text evaluate(Text url, Text name) {
        if(url==null){
            return new Text("非马蜂窝页面");
        }
        String u = url.toString();
        String name1;
        if(null==name){
            name1=null;
        }else{
            name1 = name.toString();
        }
        String pagename = "";
        if (urlParse(u, "host", "app.mafengwo.cn") || regpa("app.mafengwo.cn", u)) {
            //1.host="app.mafengwo.cn"
            if (urlParse(u, "path", "/user/comment_list")) {
                pagename = "用户点评列表";
            } else if (name1 != "") {
                pagename = name1;
            } else {
                pagename = null;
            }
        } else if (urlParse(u, "host", "payitf.mafengwo.cn")) {
            //2.host="payitf.mafengwo.cn"
            if (urlParse(u, "path", "cloudauth/material")) {
                pagename = "pay_four_points_check";
            } else if (urlParse(u, "path", "esign/person/auth")) {
                pagename = "pay_face_check";
            } else if (urlParse(u, "path", "order/pay_v2/jumppay")) {
                pagename = "pay_jump_to_third";
            } else if (urlParse(u, "path", "/pay_v2/gopay")) {
                pagename = "pay_jump_to_paycenter";
            } else if (urlParse(u, "path", "/return/")) {
                pagename = "pay_back_to_order";
            } //是用户主动返回 一般没支付成功
            else if (urlParse(u, "path", "/pay_v2/guide")) {
                pagename = "pay_sucess";
            } //guide 收银台 付完款自动回调，也有情况是 没支付成功 ，刷新收银台
            else if (urlParse(u, "path", "/pay_v2/history")) {
                pagename = "pay_back_to_order";
            } //没支付成功，从收银台返回业务下单页（或者自定义）
            else if (urlParse(u, "path", "/order/pay_v2/404")) {
                pagename = "pay_fail_404";
            } else {
                pagename = null;
            }
        } else if (urlParse(u, "host", "passport.mafengwo.cn")) {
            //3.host="passport.mafengwo.cn"
            if (urlParse(u, "path", "forget")) {
                pagename = "account_forget";
            } else if (getUrl(u, "query", "return_url")!=null&&!getUrl(u, "query", "return_url").equals("")) {
                pagename = "account_login";
            } else if (urlParse(u, "path", "passport.mafengwo.cn/$")) {
                pagename = "account_login";
            } else if (urlParse(u, "path", "reset")) {
                pagename = "account_reset";
            } else if (urlParse(u, "path", "setting/wallet/")) {
                pagename = "account_wallet";
            } else if (urlParse(u, "path", "/wechat/auth/")) {
                pagename = "account_wechat_auth";
            } else if (urlParse(u, "path", "/qq/auth/")) {
                pagename = "account_qq_auth";
            } else if (urlParse(u, "path", "/qq")) {
                pagename = "account_qq_auth";
            } else if (urlParse(u, "path", "/wechat")) {
                pagename = "account_wechat_auth";
            } else if (urlParse(u, "path", "/setting/security/")) {
                pagename = "account_security";
            } else if (urlParse(u, "path", "/")) {
                pagename = "account_login";
            } else {
                pagename = null;
            }
        } else if (urlParse(u, "host", "w.mafengwo.cn")) {
            //4.host="w.mafengwo.cn"
            if (urlParse(u, "path", "calendar2019")) {
                pagename = "马蜂窝台历";
            } //https://w.mafengwo.cn/calendar2019/preheat
            else if (urlParse(u, "path", "2018-gq-light/")) {
                pagename = "点亮全球";
            } else if (urlParse(u, "path", "/activity_light_global/")) {
                pagename = "点亮全球";
            } else if (urlParse(u, "path", "/activity_footballbaby_home/")) {
                pagename = "世界杯足球宝贝";
            } else if (urlParse(u, "path", "/sales_promotion/activity/sales_baby/")) {
                pagename = "营销活动页";
            } else if (urlParse(u, "path", "/sales_promotion/activity/thomas")) {
                pagename = "营销活动页";
            } else if (urlParse(u, "path", "/sales_promotion/")) {
                pagename = "促销活动页";
            } else if (urlParse(u, "path", "//sales_promotion/")) {
                pagename = "促销活动页";
            } //https://w.mafengwo.cn/sales_promotion/recruit/index
            else if (urlParse(u, "path", "/activity_fancy_note/")) {
                pagename = "游记活动";
            } else if (urlParse(u, "path", "/mfw_activity_new/")) {
                pagename = "社区活动";
            } else if (urlParse(u, "path", "aps/book/")) {
                pagename = "品牌活动";
            } //https://w.mafengwo.cn/aps/book/unknowtravel/v2/index.html
            else if (urlParse(u, "path", "activity_honeycomb")) {
                pagename = "品牌活动";
            } //https://w.mafengwo.cn/activity_honeycomb/
            else if (urlParse(u, "path", "activity_group_intro")) {
                pagename = "未知饭局";
            } //https://w.mafengwo.cn/activity_group_intro/479729
            else if (urlParse(u, "path", "/h5-best-answerer/")) {
                pagename = "问答活动";
            } //https://w.mafengwo.cn/h5-best-answerer/index.html#/
            else if (urlParse(u, "path", "/find_traveler/")) {
                pagename = "问答活动";
            } //答题大赛寻找旅行X星人    https://w.mafengwo.cn/find_traveler/index.html?id=10#/
            else if (urlParse(u, "path", "/activity_qList/")) {
                pagename = "问答活动";
            } else if (urlParse(u, "path", "/worldcup2018/")) {
                pagename = "世界杯活动";
            } else if (urlParse(u, "path", "/worldcup_task/")) {
                pagename = "世界杯活动任务";
            } else if (urlParse(u, "path", "/activity_qList/index")) {
                pagename = "问答精选讨论";
            } else if (urlParse(u, "path", "/daka/")) {
                pagename = "用户打卡页";
            } else if (urlParse(u, "path", "/gy-delicacy/")) {
                pagename = "目的地活动";
            } else if (urlParse(u, "path", "/activity_reserve_note/")) {
                pagename = "游记活动";
            } //https://w.mafengwo.cn/activity_reserve_note/index
            else if (urlParse(u, "path", "/activity_dt_destination/")) {
                pagename = "问答活动";
            } //https://w.mafengwo.cn/activity_dt_destination/index.html?id=48#/
            else if (regpa("/sales_fe/index.html#/wxapp/laxin/index", u)) {
                pagename = "app下载";
            } //https://w.mafengwo.cn/sales_fe/index.html#/wxapp/laxin/index
            else if (urlParse(u, "path", "/group_h5/article_detail/")) {
                pagename = "小组介绍页";
            } //小组介绍页   https://w.mafengwo.cn/group_h5/article_detail/******
            else if (urlParse(u, "path", "/group_h5/comment_list/")) {
                pagename = "小组评论列表页";
            } //小组评论列表页 https://w.mafengwo.cn/group_h5/comment_list/*******
            else if (urlParse(u, "path", "/group_h5/group_list/")) {
                pagename = "小组列表页";
            } //小组列表页   https://w.mafengwo.cn/group_h5/group_list/*******
            else if (urlParse(u, "path", "/group_h5/group_my/")) {
                pagename = "我的小组页";
            } //我的小组页   https://w.mafengwo.cn/group_h5/group_my/
            else if (regpa("group_h5/publish\\?id=", u)) {
                pagename = "发布活动页面";
            } //发布活动页面  https://w.mafengwo.cn/group_h5/publish?id=******
            else if (urlParse(u, "path", "/group_h5/publish")) {
                pagename = "小组活动发布页";
            } //小组活动发布页 https://w.mafengwo.cn/group_h5/publish
            else if (urlParse(u, "path", "/activity_city/home")) {
                pagename = "目的地同城活动主页面";
            } //目的地同城活动主页面    https://w.mafengwo.cn/activity_city/home?activity_id=****
            else if (urlParse(u, "path", "/activity_city/Home")) {
                pagename = "目的地同城活动主页面";
            } else if (urlParse(u, "path", "activity_city/launch")) {
                pagename = "同城活动发起页面";
            } //同城活动发起页面    https://w.mafengwo.cn/activity_city/launch?mdd_id=1&mddId=******
            else if (regpa("/activity_city/\\?mdd_id=", u)) {
                pagename = "目的地同城活动列表页";
            } //全部同城活动列表页   https://w.mafengwo.cn/activity_city/list
            else if (urlParse(u, "path", "/activity_city/list")) {
                pagename = "全部同城活动列表页";
            } //目的地同城活动列表页  https://w.mafengwo.cn/activity_city/list?mdd_id=*****
            else if (urlParse(u, "path", "/activity_city/manage")) {
                pagename = "同城活动管理页";
            } //https://w.mafengwo.cn/activity_city/manage?activity_id=60615&user_type=1
            else if (urlParse(u, "path", "/sales_exp_report/index\\.html")) {
                pagename = "旅行体验首页";
            } // https://w.mafengwo.cn/sales_exp_report/index.html?from=order_center
            else if (urlParse(u, "path", "sales_exp_report/detail\\.html")) {
                pagename = "旅行体验详情页";
            } else if (urlParse(u, "path", "sales_exp_report/comment")) {
                pagename = "旅行体验点评";
            } //https://w.mafengwo.cn/sales_exp_report/comment.html?report_id=90802
            else if (urlParse(u, "path", "trans_car_flight/index.html")) {
                pagename = "接送机首页";
            } //https://w.mafengwo.cn/trans_car_flight/index.html#/
            else if (urlParse(u, "path", "/trans_car_flight/orderWrite.html")) {
                pagename = "接送机提交订单";
            } //https://w.mafengwo.cn/trans_car_flight/orderWrite.html?carTypeCode=300005&carTypeName=%E8%B1%AA%E5%8D%8E5%E5%BA%A7&vendorCode=YITU8&merchantPriceMark=40e867db2f389e1f&price=653&activityPrice=0&bizType=1&airportCode=TPE&depAddress=%E6%A1%83%E5%9B%AD%E6%9C%BA%E5%9C%BA&depLon=121.233002&depLat=25.0777&arrAddress=269%E5%8F%B0%E6%B9%BE%E5%AE%9C%E8%98%AD%E7%B8%A3%E5%86%AC%E5%B1%B1%E9%84%89%E8%8C%84%E8%8B%B3%E8%B7%AF219%E8%99%9F&cityName=%E5%8F%B0%E5%8C%97&arrLon=121.8061505&arrLat=24.6562023&serviceTime=2018-11-02%2023%3A55%3A00&baggageCount=2&seatCount=4&insurancePrice=0&liftcardPrice=0&guidePrice=0&childrenPrice=0&flightNo=CI928&airlineCompany=%25E4%25B8%25AD%25E5%258D%258E%25E8%2588%25AA%25E7%25A9%25BA&tagInfo=%E4%B8%AD%E6%96%87%E5%8F%B8%E6%9C%BA&serviceTimeBj=2018-11-01%2021%3A55%3A00&imgUrl=https%3A%2F%2Fp4-q.mafengwo.net%2Fs11%2FM00%2F90%2FC6%2FwKgBEFtn7WuAD_1jAAAwSnetlKU456.png
            else if (regpa("sales_customize/scheme_info\\?demand_id", u)) {
                pagename = "电商定制方案详情";
            } else if (urlParse(u, "path", "/sales_customize/demand_submission")) {
                pagename = "电商定制需求提交";
            } //https://w.mafengwo.cn/sales_customize/demand_submission?line_mdd_id=14407&entry_type=&entry_id=&hash_key=
            else if (urlParse(u, "path", "/sales_customize/mdd_entrance")) {
                pagename = "电商目的地定制频道";
            } //https://w.mafengwo.cn/sales_customize/mdd_entrance?line_mdd_id=10065&track_id=1000
            else if (urlParse(u, "path", "/sales_customize/submit_success")) {
                pagename = "电商定制需求提交成功";
            } //https://w.mafengwo.cn/sales_customize/submit_success?demand_id=1615341583524497
            else if (urlParse(u, "path", "/sales_customize/contract")) {
                pagename = "电商定制查看合同";
            } else if (urlParse(u, "path", "/sales_customize/scheme")) {
                pagename = "电商定制方案详情";
            } // https://w.mafengwo.cn/sales_customize/scheme?id=1615301099196474
            else if (urlParse(u, "path", "/trans_flight_lpc/amap.html")) {
                pagename = "探索世界地图页";
            } ////- update by SynHao at 20180914 09:28
            else if (urlParse(u, "path", "/trans_flight_lpc/content.html")) {
                pagename = "大交通频道-特价机票首页";
            }
            else if (urlParse(u, "path", "/trans_flight_lpc/stroke.html")) {
                pagename = "大交通频道-特价机票行程页";
            } else if (urlParse(u, "path", "/trans_car_flight/selectCar.html")) {
                pagename = "接送机租车频道-接送机选车页";
            } //https://w.mafengwo.cn//trans_car_flight/selectCar.html
            else if (urlParse(u, "path", "/trans_car_flight/activity.html")) {
                pagename = "接送机租车频道-大促活动";
            } //https://w.mafengwo.cn//trans_car_flight/selectCar.html
            else if (urlParse(u, "path", "/sales/shop/page/impression")) {
                pagename = "电商店铺印象";
            } else if (urlParse(u, "path", "/sales/order/booking/supply")) {
                pagename = "交易频道-出行信息完善页";
            } else if (regpa("/sales_fe/index.html#/cityPlay/", u)) {
                pagename = "电商城市玩法";
            } //https://w.mafengwo.cn/sales_fe/index.html#/cityPlay/10183
            else if (regpa("/sales_fe/index.html#/soptTicket", u)) {
                pagename = "电商门票";
            } //https://w.mafengwo.cn/sales_fe/index.html#/soptTicket?poi_id=6102028&cdid=945&mi=12562%7C972
            else if (regpa("/sales_fe/index.html\\?\\d+#/soptTicket", u)) {
                pagename = "电商门票";
            } //https://w.mafengwo.cn/sales_fe/index.html#/soptTicket?poi_id=6102028&cdid=945&mi=12562%7C972
            else if (regpa("/sales_car/index.html#/car/", u)) {
                pagename = "大交通-接送机首页";
            } //https://w.mafengwo.cn/sales_car/index.html#/car/osd
            else if (regpa("/sales_fe/index.html#/temai", u)) {
                pagename = "电商特卖";
            } //https://w.mafengwo.cn/sales_fe/index.html#/temai
            else if (regpa("/sales_fe/index.html#/guideAlbum", u)) {
                pagename = "电商攻略";
            } //https://w.mafengwo.cn/sales_fe/index.html#/guideAlbum?id=466
            else if (regpa("/sales_fe/index.html#/newUserGift", u)) {
                pagename = "电商优惠券";
            } else if (regpa("/sales_fe/index.html#/popularList", u)) {
                pagename = "附近好店人气榜";
            } else if (regpa("/sales_fe/index.html#/vip/", u)) {
                pagename = "金卡会员";
            } else if (regpa("/sales_fe/index.html#/wanfa/detail", u)) {
                pagename = "电商玩法详情页";
            } //https://w.mafengwo.cn/sales_fe/index.html#/wanfa/detail/333
            else if (regpa("/sales_train/index.html#/train/detail/\\d+", u)) {
                pagename = "大交通火车票订单详情";
            } //https://w.mafengwo.cn/sales_train/index.html#/train/detail/1615185184145766
            else if (regpa("/sales_fe/index.html#/quickbook/", u)) {
                pagename = "电商必体验产品列表页";
            } //https://w.mafengwo.cn/sales_fe/index.html#/quickbook/10035/49646
            else if (regpa("/sales_train/index.html#/train/list", u)) {
                pagename = "大交通火车票列表页";
            }
            else if (regpa("/sales_train/index.html#/train/booking", u)) {
                pagename = "火车票预订页";
            }  //https://w.mafengwo.cn/sales_train/index.html#/train/list?depart_code=beijing&depart_name=%E5%8C%97%E4%BA%AC&dest_code=shanghai&dest_name=%E4%B8%8A%E6%B5%B7&depart_date=2018-10-25&high_speed_rail=0
            else if (regpa("/sales_fe/index.html#/packageHub", u)) {
                pagename = "电商打包产品";
            } //https://w.mafengwo.cn/sales_fe/index.html#/packageHub?id=118
            else if (regpa("sales_fe/index.html#/ticketCommentDetail", u)) {
                pagename = "电商门票点评";
            } //  https://w.mafengwo.cn/sales_fe/index.html#/ticketCommentDetail?poi_id=5429475
            else if (regpa("/sales_fe/index.html\\?_time=\\d+#/shop/trends/", u)) {
                pagename = "电商店铺动态";
            } //70    https://w.mafengwo.cn/sales_fe/index.html?_time=1540224566#/shop/trends/51935
            else if (regpa("/sales_fe/index.html\\?_time=\\d+#/shop/video", u)) {
                pagename = "电商店铺视频";
            } //https://w.mafengwo.cn/sales_fe/index.html?_time=1540651516#/shop/video/22704
            else if (urlParse(u, "path", "/activity_city/myActivity")) {
                pagename = "我的活动";
            } //add 201901
            else if (urlParse(u, "path", "/group_h5/group_my")) {
                pagename = "我的小组";
            } //add 201901
            else if (urlParse(u, "path", "/hotel_open_platform/")) {
                pagename = "开放平台";
            } //add 201901
            else if (urlParse(u, "path", "/mshequ/medal/index")) {
                pagename = "我的旅行勋章";
            } //add 201901
            else if (urlParse(u, "path", "/mshequ/passport/bind_phone")) {
                pagename = "绑定手机号";
            } //add 201901
            else if (urlParse(u, "path", "/sales_customize/answer")) {
                pagename = "定制旅行频道-问答导流页";
            } //add 201901
            else if (urlParse(u, "path", "/sales_customize/jump_back")) {
                pagename = "定制旅行频道- 技术功能页（无业务信息）";
            } //add 201901
            else if (urlParse(u, "path", "/sales_exp_report/list.html")) {
                pagename = "商城旅行体验-频道首页";
            } //add 201901
            else if (urlParse(u, "path", "/sfe-app/cmt_complete.html")) {
                pagename = "点评完成";
            } //add 201901
            else if (urlParse(u, "path", "/sfe-app/cmt_edit.html")) {
                pagename = "写点评";
            } //add 201901
            else if (urlParse(u, "path", "/sfe-app/qa_answer.html")) {
                pagename = "电商-提交问答页";
            } //add 201901
            else if (urlParse(u, "path", "/sfe-app/qa_detail.html")) {
                pagename = "问答详情 ";
            } //add 201901
            else if (urlParse(u, "path", "/sfe-app/qa_question.html")) {
                pagename = "电商-问答提问页";
            } //add 201901
            else if (urlParse(u, "path", "/sfe-app/quickbook.html")) {
                pagename = "极速预订频道-极速预订产品列表页";
            } //add 201901
            else if (urlParse(u, "path", "/sfe-app/gentuan_list.html")) {
                pagename = "跟团游频道-跟团游列表页";
            }
            else if (urlParse(u, "path", "/sfe-app/mdd_channel.html")) {
                pagename = "跟团游频道-目的地聚合页";
            }
            else if (urlParse(u, "path", "/trans_car_flight/game.html")) {
                pagename = "接送机租车频道-疯狂小汽车活动页";
            } //add 201901
            else if (urlParse(u, "path", "/trans_car_flight/orderDetail.html")) {
                pagename = "接送机租车频道-接送机订单详情页";
            }
            else if (urlParse(u, "path", "/mtraffic/flightchn/list.html?")) {
                pagename = "国内机票列表页";
            } //add 201901
            else {
                pagename = null;
            } //end
        } else if (urlParse(u, "host", "imfw.cn")) {
            pagename = "short_url";
        } else if (urlParse(u, "host", "topic.mafengwo.cn")) {
            pagename = "topic_activity";
        } else if (urlParse(u, "host", "www.mafengwo.cn") || urlParse(u, "host", "m.mafengwo.cn")) {
            if (urlParse(u, "path", "/hotel/|/hotelzd/|/youyu/|/hotel_zx/")) {
                if (regpa("from=app_travelguide_ios", u)) {
                    pagename = "跳转中间页";
                } else if (regpa("from=app_travelguide_android", u)) {
                    pagename = "跳转中间页";
                } else if (regpa("from=app_ios", u)) {
                    pagename = "跳转中间页";
                } else if (regpa("from=app_android", u)) {
                    pagename = "跳转中间页";
                } //https://m.mafengwo.cn/hotel/room?sLabel=20184003279259&poi_id=95489&check_in=2019-01-01&check_out=2019-01-08&adults_num=2&children_num=0&children_age=&ota=17&ota_id=17
                else if (urlParse(u, "path", "hotel/booking/go2booking.php")) {
                    pagename = "OTA酒店预订";
                } else if (urlParse(u, "path", "hotel/room")) {
                    if (urlParse(u, "ref", "room")) {
                        pagename = "酒店下单流程-房型列表页";
                    } else if (urlParse(u, "ref", "booking_result")) {
                        pagename = "酒店下单流程-预订结果页";
                    } else if (urlParse(u, "ref", "booking_form")) {
                        pagename = "酒店下单流程-表单页";
                    } else if (urlParse(u, "ref", "payment")) {
                        pagename = "酒店下单流程-支付页";
                    } else if (urlParse(u, "ref", "booking_faq")) {
                        pagename = "酒店下单流程-咨询页";
                    } else {
                        pagename = "酒店下单流程-房型列表页";
                    }
                } else if (urlParse(u, "path", "hotel/booking_result")) {
                    if (urlParse(u, "ref", "room")) {
                        pagename = "酒店下单流程-房型列表页";
                    } else if (urlParse(u, "ref", "booking_result")) {
                        pagename = "酒店下单流程-预订结果页";
                    } else if (urlParse(u, "ref", "booking_form")) {
                        pagename = "酒店下单流程-表单页";
                    } else if (urlParse(u, "ref", "payment")) {
                        pagename = "酒店下单流程-支付页";
                    } else if (urlParse(u, "ref", "booking_faq")) {
                        pagename = "酒店下单流程-咨询页";
                    } else {
                        pagename = "酒店下单流程-预订结果页";
                    }
                } else if (urlParse(u, "path", "hotel/booking_form")) {
                    if (urlParse(u, "ref", "room")) {
                        pagename = "酒店下单流程-房型列表页";
                    } else if (urlParse(u, "ref", "booking_result")) {
                        pagename = "酒店下单流程-预订结果页";
                    } else if (urlParse(u, "ref", "booking_form")) {
                        pagename = "酒店下单流程-表单页";
                    } else if (urlParse(u, "ref", "payment")) {
                        pagename = "酒店下单流程-支付页";
                    } else if (urlParse(u, "ref", "booking_faq")) {
                        pagename = "酒店下单流程-咨询页";
                    } else {
                        pagename = "酒店下单流程-表单页";
                    }
                } else if (urlParse(u, "path", "hotel/payment")) {
                    if (urlParse(u, "ref", "room")) {
                        pagename = "酒店下单流程-房型列表页";
                    } else if (urlParse(u, "ref", "booking_result")) {
                        pagename = "酒店下单流程-预订结果页";
                    } else if (urlParse(u, "ref", "booking_form")) {
                        pagename = "酒店下单流程-表单页";
                    } else if (urlParse(u, "ref", "payment")) {
                        pagename = "酒店下单流程-支付页";
                    } else if (urlParse(u, "ref", "booking_faq")) {
                        pagename = "酒店下单流程-咨询页";
                    } else {
                        pagename = "酒店下单流程-支付页";
                    }
                } else if (urlParse(u, "path", "/hotel/order_detail/index")) {
                    pagename = "酒店订单详情";
                } else if (urlParse(u, "path", "/youyu/order_detail.php")) {
                    pagename = "酒店订单详情";
                } else if (urlParse(u, "path", "/hotel/booking_faq")) {
                    pagename = "酒店预定帮助";
                } else if (urlParse(u, "path", "hotel/pickupinfo")) {
                    pagename = "酒店接机介绍";
                } //https://m.mafengwo.cn/hotel/pickupinfo
                else if (urlParse(u, "path", "/hotelzd/index.php/detail")) {
                    pagename = "酒店直订订单详情页";
                } //  http://m.mafengwo.cn/hotelzd/index.php/info?id=7807383&from=app&sLabel=5a60a52c-0704-1df9-cde7-e3056ffbf2d6_1540200008&sCheckIn=2018-10-22&sCheckOut=2018-10-23&iLogId=20184005111470&sFrom=app_travelguide_android
                else if (urlParse(u, "path", "hotelzd/index_v2.php/detail")) {
                    pagename = "酒店直订订单详情页";
                } else if (urlParse(u, "path", "/hotelzd/index.php")) {
                    pagename = "酒店直订酒店详情页";
                } //  http://m.mafengwo.cn/hotelzd/index.php/info?id=7807383&from=app&sLabel=5a60a52c-0704-1df9-cde7-e3056ffbf2d6_1540200008&sCheckIn=2018-10-22&sCheckOut=2018-10-23&iLogId=20184005111470&sFrom=app_travelguide_android
                else if (urlParse(u, "path", "hotelzd/index_v2.php")) {
                    pagename = "酒店直订酒店详情页";
                } //https://m.mafengwo.cn/hotelzd/index_v2.php?iHotelId=15751569&from=app&sLabel=5bbab09e-a479-6c36-b9ee-87d7cd4935d2_1540391839&sCheckIn=2018-11-13&sCheckOut=2018-11-19&iLogId=20184005663101&sFrom=app_travelguiHotelIde_androiHotelId
                else if (urlParse(u, "path", "/hotelzd/create_order.php")) {
                    pagename = "酒店直订提交订单页";
                } ///hotelzd/create_order.php
                else if (urlParse(u, "path", "/youyu/order_detail.php")) {
                    pagename = "酒店订单详情";
                } //https://m.mafengwo.cn/youyu/order_detail.php?sOrderId=51181014016470199
                else if (urlParse(u, "path", "/hotel_zx/order/detail.php")) {
                    pagename = "酒店订单详情";
                } //http://www.mafengwo.cn/hotel_zx/order/detail.php?sOrderId=51181002015228174&sVCode=f275a4916087f6b1b04cbb7754c24d0f&sLang=en
                else if (urlParse(u, "path", "/hotel/feature_gonglve/")) {
                    pagename = "酒店特色攻略";
                } ///hotel/feature_gonglve/1
                else if (urlParse(u, "path", "/hotel/\\d+/")) {
                    pagename = "酒店列表页";
                } //https://m.mafengwo.cn/hotel/15284/
                else if (urlParse(u, "path", "/hotel/license")) {
                    pagename = "商家资质";
                } //https://m.mafengwo.cn/hotel/license#/otaList
                else if (urlParse(u, "path", "/hotel/\\d+\\.html")) {
                    pagename = "酒店详情页";
                } else if (urlParse(u, "path", "/hotel/area_\\d+\\.html")) {
                    pagename = "酒店列表页";
                } //https://m.mafengwo.cn/hotel/area_46042.html
                else if (urlParse(u, "path", "/hotel/feature/\\d+/")) {
                    pagename = "酒店特色列表页";
                } ///hotel/feature/10460/
                else if (urlParse(u, "path", "/hotel/feature_mdd/\\d+/")) {
                    pagename = "酒店目的地特色列表页";
                } ///hotel/feature_mdd/10208/25769/
                else if (urlParse(u, "path", "/hotelzd/pay.php")) {
                    pagename = "酒店直订支付页";
                } else if (urlParse(u, "path", "/hotelzd/success.php")) {
                    pagename = "酒店直订订单支付成功";
                } //https://www.mafengwo.cn/hotelzd/success.php?sOrderId=51181022016776848&anonymous=62bf995473930d814f80cb5acd1c8426
                else if (urlParse(u, "path", "/hotel/activity/2018_0815")) {
                    pagename = "8.15酒店大促";
                }
                else if (urlParse(u, "path", "/hotel/activity/2018_1111")) {
                    pagename = "11.11酒店寻梦季";
                }
                else if (urlParse(u, "path", "/hotel/activity/2019_0110")) {
                    pagename = "1.10-1.11酒店年货节";
                }
                else if (urlParse(u, "path", "/hotel/activity/")) {
                    pagename = "酒店大促活动";
                } //https://www.mafengwo.cn/hotelzd/success.php?sOrderId=51181022016776848&anonymous=62bf995473930d814f80cb5acd1c8426
                else if (urlParse(u, "path", "/hotel/get_coupon")) {
                    pagename = "优惠券落地页";
                } //add 201901
                else if (urlParse(u, "path", "/hotel/qa/add_question")) {
                    pagename = "酒店问答创建问题页";
                } //add 201901
                else if (urlParse(u, "path", "/hotel/qa/detail")) {
                    pagename = "问答详情页";
                }
                else if (urlParse(u, "path", "/hotel/qa/list")) {
                    pagename = "酒店问答详情页";
                } //add 201901
                else if (urlParse(u, "path", "/hotel_zx/hotel/index.php")) {
                    pagename = "有鱼详情页";
                } //add 201901
                else if (urlParse(u, "path", "/hotel/list_map")) {
                    pagename = "列表地图页";
                } else {
                    pagename = null;
                }
            } else if (urlParse(u, "path", "/flight/")) {
                if (urlParse(u, "path", "/flight/book")) {
                    pagename = "机票提交订单页";
                } //https://m.mafengwo.cn/flight/book?departCity=%E5%8C%97%E4%BA%AC&departCode=BJS&destCity=%E6%B5%B7%E5%8F%A3&destCode=HAK&type=roundWay&otaId=101&comeFromGo=&comeFromBack=&departDate=2019-02-03&destDate=2019-02-12&selected=115a663246d9c9d2979b03a7592b8e0a&seat_go_selected=6943294b2b7c951948b00d9c308d47c4&source=app_home&with_child=0
                else if (urlParse(u, "path", "/flight/inter/book")) {
                    pagename = "国际机票提交订单页";
                } else if (urlParse(u, "path", "/flight/order/detail")) {
                    pagename = "机票订单详情";
                } //https://m.mafengwo.cn/flight/order/detail?order_id=51180804011294109
                else if (urlParse(u, "path", "/flight/inter/order/detail")) {
                    pagename = "国际机票订单详情";
                } //https://m.mafengwo.cn/flight/inter/order/detail?order_id=51181009015980639#/index
                else if (urlParse(u, "path", "/flight/guide")) {
                    pagename = "机票乘机指南";
                } else if (urlParse(u, "path", "/flight/list")) {
                    pagename = "机票列表页";
                } //https://m.mafengwo.cn/flight/list?type=oneWay&with_child=0&departCode=HGH&departCity=%E6%9D%AD%E5%B7%9E&destCode=CGQ&destCity=%E9%95%BF%E6%98%A5&departDate=2018-10-31&source=app_home&status=0&adult_nums=1
                else if (urlParse(u, "path", "/flight/inter/seats")) {
                    pagename = "国际机票航班信息";
                } //https://m.mafengwo.cn/flight/inter/seats?status=0&adult_nums=1&departCode=HKT&departCity=%E6%99%AE%E5%90%89%E5%B2%9B&destCode=BKK&destCity=%E6%9B%BC%E8%B0%B7&departDate=2018-10-30&source=app_home&child_nums=0&baby_nums=0&dep_date_flightNo=20181030FD3026&curFlightListCacheKey=HKTBKK-20181030-1%230%230-All
                else if (urlParse(u, "path", "/flight/order/list")) {
                    pagename = "机票订单列表页";
                } //https://m.mafengwo.cn/flight/order/list
                else if (urlParse(u, "path", "/flight/seats")) {
                    pagename = "机票航班信息";
                } //  https://m.mafengwo.cn/flight/seats?type=oneWay&departCode=WNZ&departCity=%E6%B8%A9%E5%B7%9E&destCode=BJS&destCity=%E5%8C%97%E4%BA%AC&departDate=2018-10-27&source=bs_low_route&status=0&adult_nums=1&with_child=0&flightNo=GJ8976&curFlightListCacheKey=WNZBJS-20181027-1%230%230-All
                else if (urlParse(u, "path", "/flight/$")) {
                    pagename = "电商机票首页";
                } else if (urlParse(u, "path", "/flight/inter/list")) {
                    pagename = "国际机票列表页";
                } //https://m.mafengwo.cn/flight/inter/list?departCity=%E5%B9%BF%E5%B7%9E&departCode=CAN&destCity=%E6%97%A7%E9%87%91%E5%B1%B1&destCode=SFO&departDate=2019-03-28&destDate=2019-05-30&adult_nums=2&child_nums=0&baby_nums=0&status=2&source=app_home&seat_class=&flightNo=FM9302-MU589&curFlightListCacheKey=CANSFO-20190328-20190530-2%230%230-All&timeSlot=07%3A30-10%3A30&airlineCompanyName=%E4%B8%8A%E8%88%AA
                else {
                    pagename = null;
                } //end
            } //电商
            else if (urlParse(u, "path", "/sales/|/localdeals/|/cruise/")) {
                if (urlParse(u, "path", "sales/\\d+\\.html")) {
                    pagename = "电商产品详情页";
                } else if (urlParse(u, "path", "sales/uhelp/doc")) {
                    pagename = "用户帮助中心-电商";
                } //   https://m.mafengwo.cn/sales/uhelp/doc
                else if (urlParse(u, "path", "/sales/list.php")) {
                    pagename = "电商产品列表页";
                } //https://m.mafengwo.cn/sales/list.php?keyword=%E6%8B%89%E6%96%AF%E7%BB%B4%E5%8A%A0%E6%96%AF+%E6%B5%B7%E8%88%AA
                else if (urlParse(u, "path", "/sales/around.php")) {
                    pagename = "电商周边游频道页";
                } //https://m.mafengwo.cn/sales/around.php?mdd_id=10065&from=mdd
                else if (urlParse(u, "path", "/sales/around_tag_list")) {
                    pagename = "电商周边游标签频道页";
                } //https://m.mafengwo.cn/sales/around_tag_list?mdd_id=10035
                else if (urlParse(u, "path", "/localdeals/quickbuy")) {
                    pagename = "电商极速预订产品列表页";
                } else if (urlParse(u, "path", "/sales/fengqiang/index")) {
                    pagename = "电商蜂抢首页";
                } //https://m.mafengwo.cn/sales/fengqiang/index?mddid=10728
                else if (urlParse(u, "path", "sales/wind_vane/article")) {
                    pagename = "电商玩法详情页";
                } //https://m.mafengwo.cn/sales/wind_vane/article?id=1006
                else if (urlParse(u, "path", "/sales/wind_vane/theme")) {
                    pagename = "电商玩法主题";
                } else if (urlParse(u, "path", "/sales/ski/\\d+\\.html")) {
                    pagename = "电商滑雪详情页";
                } ///sales/ski/10072.html
                else if (urlParse(u, "path", "/localdeals/\\d+/")) {
                    pagename = "电商当地游列表页";
                } //https://m.mafengwo.cn/localdeals/10083/?cid=1010900
                else if (urlParse(u, "path", "/sales/\\d+/")) {
                    pagename = "电商首页";
                } else if (urlParse(u, "path", "/sales/visa/$")) {
                    pagename = "电商签证首页";
                } //http://www.mafengwo.cn/sales/visa/
                else if (urlParse(u, "path", "/sales/alliance.php")) {
                    pagename = "电商商家入驻";
                } //-http://www.mafengwo.cn/sales/alliance.php?step=1&mfw_sid=test_union_0
                else if (urlParse(u, "path", "/sales/$")) {
                    pagename = "电商首页";
                } //
                else if (urlParse(u, "path", "/localdeals/\\d+\\-")) {
                    pagename = "电商当地游列表页";
                } //https://m.mafengwo.cn/localdeals/0-0-M11030-0-0-0-0-0.html?from=localdeals_index
                else if (urlParse(u, "path", "/sales/\\d+\\-")) {
                    pagename = "电商列表页";
                } //http://www.mafengwo.cn/sales/0-0-M10067-4-0-0-0-0.html
                //订单
                else if (urlParse(u, "path", "/sales/order[_/]detail")) {
                    pagename = "电商订单详情";
                } else if (urlParse(u, "path", "/sales/booking.php")) {
                    pagename = "电商订单确认";
                } // 点击预订按钮后的页面
                else if (urlParse(u, "path", "/sales/refund.php")) {
                    pagename = "电商订单退款页";
                } //https://m.mafengwo.cn/sales/refund.php?order_id=2455310201805287807534
                else if (urlParse(u, "path", "/sales/reconfirm.php")) {
                    pagename = "电商订单确认页";
                } //https://m.mafengwo.cn/sales/reconfirm.php?oid=2183414201805287809949
                else if (urlParse(u, "path", "/sales/order/refund")) {
                    pagename = "电商退款";
                } //https://m.mafengwo.cn/sales/order/refund?order_id=2412068201805127604007
                else if (urlParse(u, "path", "sales/booking/supply")) {
                    pagename = "电商预订页";
                } //https://m.mafengwo.cn/sales/booking/supply?id=2202081201806047905121
                else if (urlParse(u, "path", "/sales/order/booking/supply")) {
                    pagename = "电商预订页";
                } //https://m.mafengwo.cn/sales/order/booking/supply?id=21579172018102411144052
                else if (urlParse(u, "path", "/sales/order/booking.php")) {
                    pagename = "电商预订页";
                } //?skus=%7B%221310162%22%3A%7B%22t%22%3A%7B%2215%22%3A1%7D%2C%22d%22%3A%7B%22s%22%3A%222018-08-07%22%7D%7D%7D&room_num=0
                //点评
                else if (urlParse(u, "path", "sales/c/comment/success")) {
                    pagename = "电商点评提交成功";
                } else if (urlParse(u, "path", "/sales/shop/handler/commentDetail")) {
                    pagename = "电商点评详情";
                } //https://m.mafengwo.cn/sales/shop/handler/commentDetail?comment_id=3818999
                else if (urlParse(u, "path", "/sales/c/comment/list")) {
                    pagename = "电商产品点评列表";
                } //https://m.mafengwo.cn/sales/c/comment/list_2248547.html
                else if (urlParse(u, "path", "/sales/c/comment/guide")) {
                    pagename = "电商点评引导攻略";
                } //https://m.mafengwo.cn/sales/c/comment/guide.html
                else if (urlParse(u, "path", "/sales/c/faq/list")) {
                    pagename = "电商问答列表";
                } // https://m.mafengwo.cn/sales/c/faq/list_2372807.html
                else if (urlParse(u, "path", "/sales/c/comment/\\d+")) {
                    pagename = "电商产品点评详情";
                } else if (urlParse(u, "path", "/sales/c/comment/add_\\d+")) {
                    pagename = "电商发布产品点评";
                } else if (urlParse(u, "path", "/sales/c/faq/detail_\\d+")) {
                    pagename = "电商问答详情";
                } else if (urlParse(u, "path", "/sales/c/faq/add_question")) {
                    pagename = "电商提问";
                } else if (urlParse(u, "path", "/sales/c/faq/add_answer")) {
                    pagename = "电商回答";
                } //https://m.mafengwo.cn/sales/c/faq/add_answer_13780268.html
                //商家统计
                else if (urlParse(u, "path", "/sales/stat/ota_order_info.php")) {
                    pagename = "电商商家端订单详情";
                } else if (urlParse(u, "path", "/sales/stat/order.php")) {
                    pagename = "电商商家端订单列表";
                } //https://m.mafengwo.cn/sales/stat/order.php?ota_id=1527
                else if (urlParse(u, "path", "sales/stat/home.php")) {
                    pagename = "电商我的店铺";
                } //https://m.mafengwo.cn/sales/stat/home.php?ota_id=57287
                else if (urlParse(u, "path", "/sales/stat/index.php")) {
                    pagename = "电商店铺统计页";
                } //https://m.mafengwo.cn/sales/stat/index.php?ota_id=1870
                else if (urlParse(u, "path", "/sales/stat/sku.php")) {
                    pagename = "电商商家SKU统计";
                } //https://m.mafengwo.cn/sales/stat/sku.php?ota_id=61102
                else if (urlParse(u, "path", "/sales/stat/topsellers.php")) {
                    pagename = "电商商家端topseller统计";
                } else if (urlParse(u, "path", "/sales/stat/pv.php")) {
                    pagename = "电商商家端PV统计";
                } else if (urlParse(u, "path", "/sales/stat/gmv.php")) {
                    pagename = "电商商家端GMV统计";
                } //店铺
                else if (urlParse(u, "path", "/sales/shop/\\d+")) {
                    pagename = "电商店铺首页";
                } ///sales/shop/2712
                else if (urlParse(u, "path", "/sales/shop/page/index")) {
                    pagename = "电商店铺首页";
                } //https://m.mafengwo.cn/sales/shop/page/index?iId=55529
                else if (urlParse(u, "path", "/sales/shop/page/cate")) {
                    pagename = "电商店铺分类";
                } //https://m.mafengwo.cn/sales/shop/page/cate?iId=57485
                else if (urlParse(u, "path", "/sales/shop/page/list")) {
                    pagename = "电商店铺列表";
                } //https://m.mafengwo.cn/sales/shop/page/list?iId=57146&iCateId=42830&iCateType=2
                else if (urlParse(u, "path", "sales/ota/activity")) {
                    pagename = "电商店铺专题页";
                } //https://m.mafengwo.cn/sales/ota/activity?id=962
                else if (urlParse(u, "path", "/sales/shop/page/impression")) {
                    pagename = "电商店铺印象";
                } //https://m.mafengwo.cn/sales/shop/page/impression?iId=2995
                else if (urlParse(u, "path", "/sales/shop/migrate/store")) {
                    pagename = "电商店铺迁移";
                } //https://m.mafengwo.cn/sales/shop/migrate/store
                else if (urlParse(u, "path", "/sales/shop/page/license")) {
                    pagename = "电商店铺营业执照";
                } //https://m.mafengwo.cn/sales/shop/page/license?iId=70633
                //商家
                else if (urlParse(u, "path", "/sales/ota_order.php")) {
                    pagename = "电商商家订单管理页";
                } //https://m.mafengwo.cn/sales/ota_order.php?type=0&ota_id=52043
                else if (urlParse(u, "path", "sales/order[_/]contract")) {
                    pagename = "电商订单合同签署页";
                } //https://m.mafengwo.cn/sales/order_contract?order_id=2084122201805287806479
                //优惠券
                else if (urlParse(u, "path", "/localdeals/get_coupon.php")) {
                    pagename = "电商优惠券";
                } else if (urlParse(u, "path", "sales/newcomer.php")) {
                    pagename = "电商新手红包";
                } //https://m.mafengwo.cn/sales/newcomer.php
                else if (urlParse(u, "path", "sales/coupon")) {
                    pagename = "我的优惠券";
                } //https://m.mafengwo.cn/sales/coupon
                //大促
                else if (urlParse(u, "path", "/sales/activity/honey_center/")) {
                    pagename = "蜂蜜活动页";
                }
                else if (urlParse(u, "path", "/sales/activity/daka/")) {
                    pagename = "蜂蜜活动页";
                }
                else if (urlParse(u, "path", "/sales/activity/promotion/\\d+")) {
                    pagename = "营销活动页";
                } ///sales/activity/promotion/158/
                else if (urlParse(u, "path", "/sales/app_promotion")) {
                    pagename = "营销活动页";
                } //https://m.mafengwo.cn/sales/app_promotion?timestamp=1527818725
                else if (urlParse(u, "path", "/sales/activity")) {
                    pagename = "营销活动页";
                } //特别
                else if (urlParse(u, "path", "/localdeals/mdd_topic_")) {
                    pagename = "自由行攻略";
                } else if (urlParse(u, "path", "/localdeals/mdd_\\d+")) {
                    pagename = "当地游首页";
                } //https://www.mafengwo.cn/localdeals/mdd_10186/?sFrom=mdd///localdeals/mdd
                else if (urlParse(u, "path", "/localdeals/\\d+\\.html")) {
                    pagename = "电商详情页";
                } else if (urlParse(u, "path", "/sales$")) {
                    pagename = "商城首页";
                } else if (urlParse(u, "path", "/cruise/")) {
                    pagename = "邮轮频道";
                } else if (urlParse(u, "path", "/sales/customize_scheme.php")) {
                    pagename = "定制旅行频道-定制方案详情页";
                } //add 201901
                else if (urlParse(u, "path", "/sales/order/ticketbooking")) {
                    pagename = "门票订单确认";
                } //add 201901
                else if (urlParse(u, "path", "/sales/0-0-M10184-4-0-0-0-0.html")) {
                    pagename = "韩国普通签证,韩国自由行,马蜂窝韩国自由行产品 - 马蜂窝自由行";
                } //add 201901
                else {
                    pagename = null;
                } //end
            } else if (urlParse(u, "path", "/gtree/")) {
                if (urlParse(u, "path", "gtree/like/index")) {
                    pagename = "攻略树";
                } //https://m.mafengwo.cn/gtree/like/index?mddid=10444&type=1
                else {
                    pagename = null;
                }
            } //POI
            else if (urlParse(u, "path", "/poi/|/goods/")) {
                if (urlParse(u, "path", "poi/treasure/index")) {
                    pagename = "POI美食宝藏";
                } //https://m.mafengwo.cn/poi/treasure/index?poiid=5423392
                else if (urlParse(u, "path", "/poi/poi-nojump.php")) {
                    pagename = "POI详情";
                } //https://m.mafengwo.cn/poi/poi-nojump.php?poiid=823
                else if (urlParse(u, "path", "poi/attraction/\\d+")) {
                    pagename = "POI当地玩乐";
                } //https://m.mafengwo.cn/poi/attraction/61
                else if (urlParse(u, "path", "poi/free_lunch/")) {
                    pagename = "POI详情";
                } //https://m.mafengwo.cn/poi/free_lunch/index?id=48&from=banner
                else if (urlParse(u, "path", "/poi/sub_guide_")) {
                    pagename = "POI导航";
                } ///poi/sub_guide_14795.html
                else if (urlParse(u, "path", "poi/addpoi.php")) {
                    pagename = "添加POI";
                } //https://m.mafengwo.cn/poi/addpoi.php?frome_type=weng
                else if (urlParse(u, "path", "poi/info/\\d+-simple.html")) {
                    pagename = "POI详情";
                } //http://m.mafengwo.cn/poi/info/2716-simple.html
                else if (urlParse(u, "path", "/poi/detail")) {
                    pagename = "POI详情";
                } //https://m.mafengwo.cn/poi/poi/detail?poiid=3936
                else if (urlParse(u, "path", "/goods/tag/")) {
                    pagename = "POI单品标签列表页";
                } else if (urlParse(u, "path", "/goods/\\d+")) {
                    pagename = "POI单品详情页";
                } else if (urlParse(u, "path", "/goods/recommend/")) {
                    pagename = "POI单品推荐列表页";
                } //https://m.mafengwo.cn/goods/recommend/1034/10765.html
                else if (urlParse(u, "path", "/poi/sub_guide")) {
                    pagename = "POI地图导览";
                } //
                else if (urlParse(u, "path", "/poi/info/\\d+")) {
                    pagename = "POI详情";
                } //https://m.mafengwo.cn/poi/info/3474-more.html#bgFlot_ticket
                else if (urlParse(u, "path", "/poi/\\d+\\.html")) {
                    pagename = "POI详情";
                } //https://m.mafengwo.cn/poi/33283412.html
                else if (urlParse(u, "path", "/poi/correct/")) {
                    pagename = "POI纠错";
                } //  http://m.mafengwo.cn/poi/correct/correct_gps?poiid=32023790
                else if (urlParse(u, "path", "/poi/comment")) {
                    pagename = "POI点评";
                } //  https://m.mafengwo.cn/poi/comment_5502854.html
                else if (urlParse(u, "path", "poi/delicacy/index.html")) {
                    pagename = "马蜂窝之选美食榜单页";
                } //add 201901
                else if (urlParse(u, "path", "/poi/hotspring")) {
                    pagename = "温泉主题活动页";
                } //add 201901
                else if (urlParse(u, "path", "poi/poi/onlineStarList")) {
                    pagename = "POI网红打卡地列表页";
                } //add 201901
                else {
                    pagename = null;
                } //end
            } //攻略
            else if (urlParse(u, "path", "/guide/|/gonglve/")) {
                if (urlParse(u, "path", "gl/guide/article/info/index")) {
                    pagename = "目的地攻略";
                } //https://m.mafengwo.cn/gl/guide/article/info/index?article_id=6332
                else if (urlParse(u, "path", "/gonglve/ziyouxing/\\d+\\.html")) {
                    pagename = "自由行攻略";
                } else if (urlParse(u, "path", "gonglve/ziyouxing/public/home/index")) {
                    pagename = "攻略号主页";
                } //  https://m.mafengwo.cn/gonglve/ziyouxing/public/home/index?public_id=813
                else if (urlParse(u, "path", "/gonglve/ziyouxing/activity/")) {
                    pagename = "攻略活动";
                } //https://m.mafengwo.cn/gonglve/ziyouxing/activity/public/index
                else if (urlParse(u, "path", "/gonglve/ziyouxing/public/")) {
                    pagename = "攻略发布";
                } else if (urlParse(u, "path", "/gonglve/ziyouxing/comment/\\d+")) {
                    pagename = "自由行攻略点评详情";
                } ///gonglve/ziyouxing/comment/75250.html
                else if (urlParse(u, "path", "/gonglve/ziyouxing/public/index/user")) {
                    pagename = "攻略开放平台首页";
                } //https://m.mafengwo.cn/gonglve/ziyouxing/public/index/user
                else if (urlParse(u, "path", "/gonglve/ziyouxing/mdd_\\d+")) {
                    pagename = "目的地自由行攻略";
                } else if (urlParse(u, "path", "/gl/catalog/index")) {
                    pagename = "目的地攻略详情";
                } //https://m.mafengwo.cn/gl/catalog/index?id=160&catalog_id=3785
                else if (urlParse(u, "path", "/gonglve/zt-\\d+")) {
                    pagename = "主题攻略";
                } //  http://www.mafengwo.cn/gonglve/zt-317.html
                else if (urlParse(u, "path", "/gonglve/mdd-\\d+")) {
                    pagename = "目的地攻略";
                } //  http://www.mafengwo.cn/gonglve/mdd-10030.html
                else if (urlParse(u, "path", "/gonglve/ziyouxing/modify/")) {
                    pagename = "自由行攻略编辑";
                } ///gonglve/ziyouxing/modify/
                else if (urlParse(u, "path", "/gonglve/ziyouxing/user_task/recommend_guide")) {
                    pagename = "攻略打卡任务页";
                }
                else {
                    pagename = null;
                } //end
            } //目的地
            else if (urlParse(u, "path", "/mdd/|/baike/|/jd/|/cy/|/nb/activity/")) {
                if (urlParse(u, "path", "mdd/meal/japan")) {
                    pagename = "日本点餐助手";
                } //https://m.mafengwo.cn/mdd/meal/japan
                else if (urlParse(u, "path", "mdd/topic/([^/]+)")) {
                    pagename = "目的的活动";
                } //https://m.mafengwo.cn/mdd/topic/lighting?sChannel=notice100418&sFrom=94361844
                else if (urlParse(u, "path", "/mdd/list.html")) {
                    pagename = "目的地列表";
                } //https://m.mafengwo.cn/mdd/list.html
                else if (urlParse(u, "path", "/mdd/activity/")) {
                    pagename = "目的地活动";
                } //https://m.mafengwo.cn/mdd/list.html
                else if (urlParse(u, "path", "/mdd/cityroute/")) {
                    pagename = "目的地玩法";
                } else if (urlParse(u, "path", "/mdd/route/\\d+")) {
                    pagename = "目的地线路";
                } else if (urlParse(u, "path", "/baike/")) {
                    pagename = "目的地百科";
                } // /baike/10189_62.html
                else if (urlParse(u, "path", "/jd/")) {
                    pagename = "目的地景点";
                } //https://m.mafengwo.cn/jd/11042/gonglve.html?sExt=gonglve
                else if (urlParse(u, "path", "/cy/")) {
                    pagename = "目的地餐饮";
                } // https://m.mafengwo.cn/cy/10065/gonglve.html
                else if (urlParse(u, "path", "/nb/activity/")) {
                    pagename = "各种活动";
                } //https://m.mafengwo.cn/nb/activity/hanabi/indexmdd
                else if (urlParse(u, "path", "/mdd/article.php")) {
                    pagename = "小组活动详情";
                } //http://m.mafengwo.cn/mdd/article.php?id=648244
                else if (urlParse(u, "path", "/mdd/map/\\d+\\.html")) {
                    pagename = "目的地地图";
                } //http://www.mafengwo.cn/mdd/map/11042.html
                else if (urlParse(u, "path", "/mdd/filter-tag-116.html")) {
                    pagename = "目的地筛选页";
                } //add 201901
                else if (urlParse(u, "path", "/mdd/meal/spanish")) {
                    pagename = "点餐助手页";
                } //add 201901
                else if (urlParse(u, "path", "/photo/mdd/17315_9781156.html")) {
                    pagename = "POI图片浏览页";
                } //add 201901
                else {
                    pagename = null;
                } //end
            } //用户
            else if (urlParse(u, "path", "/user/|user_wallet|home/user|/u/|/plan/")) {
                if (urlParse(u, "path", "user/profile/weng")) {
                    pagename = "用户个人主页";
                } //https://m.mafengwo.cn/user/profile/weng?uid=636812
                else if (urlParse(u, "path", "user_wallet")) {
                    pagename = "我的钱包";
                } //https://m.mafengwo.cn/user_wallet/
                else if (urlParse(u, "path", "home/user.php")) {
                    pagename = "用户个人主页";
                } //https://m.mafengwo.cn/home/user.php?alias=dewy207
                else if (urlParse(u, "path", "/u/")) {
                    pagename = "用户个人主页";
                } else if (urlParse(u, "path", "/plan/route.php")) {
                    pagename = "用户个人主页";
                } else if (urlParse(u, "path", "/user/lv.php")) {
                    pagename = "用户等级";
                } else {
                    pagename = null;
                } //end
            } //钱包
            else if (urlParse(u, "path", "/wallet")) {
                if (urlParse(u, "path", "wallet_detail")) {
                    pagename = "我的钱包";
                } //https://m.mafengwo.cn/wallet_detail/
                else if (urlParse(u, "path", "wallet_sfycr")) {
                    pagename = "信用飞-空中钱包";
                } else {
                    pagename = null;
                } //end
            } //保险
            else if (urlParse(u, "path", "/insurance/")) {
                if (urlParse(u, "path", "insurance/pdf/\\d+\\.pdf")) {
                    pagename = "商城保险条款";
                } else if (urlParse(u, "path", "/insurance/$")) {
                    pagename = "电商保险频道";
                } //https://m.mafengwo.cn/about/licence.html
                else if (urlParse(u, "path", "/insurance/detail/")) {
                    pagename = "电商保险详情";
                } else if (urlParse(u, "path", "/insurance/buy/")) {
                    pagename = "电商保险填单页";
                } //https://m.mafengwo.cn/insurance/buy/210264/
                else if (urlParse(u, "path", "/insurance/order/")) {
                    pagename = "电商保险订单详情";
                } //https://m.mafengwo.cn/insurance/order/61630/
                else {
                    pagename = null;
                } //end
            } //资讯
            else if (urlParse(u, "path", "/travel-news")) {
                if (urlParse(u, "path", "travel-news/$")) {
                    pagename = "资讯首页";
                } //http://www.mafengwo.cn/travel-news/?tagId=1
                else if (urlParse(u, "path", "travel-news/\\d+\\.html")) {
                    pagename = "资讯详情页";
                } //  https://m.mafengwo.cn/travel-news/1429578.html
                else {
                    pagename = null;
                } //end
            } //搜索
            else if (urlParse(u, "path", "/search/")) {
                if (urlParse(u, "path", "/nb/search/theme/mixed")) {
                    pagename = "搜索主题词";
                } else if (urlParse(u, "path", "/nb/search/theme/typed")) {
                    pagename = "搜索主题词查看更多";
                } else if (urlParse(u, "path", "search/s.php")) {
                    pagename = "搜索结果页";
                } //https://m.mafengwo.cn/search/s.php?q=%E5%8F%A4%E9%95%87&t=sales&seid=32978730-D089-4232-9CF8-26A47314715E&mxid=0&mid=0&mname=&kt=1
                else {
                    pagename = null;
                } //end
            } //订单中心
            else if (urlParse(u, "path", "/order_center/")) {
                if (urlParse(u, "path", "/order_center/index.php")) {
                    pagename = "我的订单";
                } else if (urlParse(u, "path", "/order_center/index")) {
                    pagename = "我的订单";
                } else if (urlParse(u, "path", "order_center/index/view_order")) {
                    pagename = "我的订单";
                } //https://m.mafengwo.cn/order_center/index/view_order?order_id=1601671966556750&busi_type=customize&_refer=list
                else if (urlParse(u, "path", "/order_center?/$")) {
                    pagename = "我的订单";
                } //https://m.mafengwo.cn/order_center/
                else if (urlParse(u, "path", "order_center/index/index")) {
                    pagename = "手机号查询订单";
                } //https://m.mafengwo.cn/order_center/index/index?query_by_mobile=15211655208&code=0fb9151ba102d1ddc1697c452941f17b#
                else if (urlParse(u, "path", "order_center/index/query_using_mobile")) {
                    pagename = "手机号查询订单";
                } //https://m.mafengwo.cn/order_center/index/query_using_mobile
                else {
                    pagename = null;
                } //end
            } //社区打卡活动
            else if (urlParse(u, "path", "/daka/")) {
                if (urlParse(u, "path", "/daka/task")) {
                    pagename = "用户打卡页";
                } else if (urlParse(u, "path", "/daka?/$")) {
                    pagename = "用户打卡页";
                } else if (urlParse(u, "path", "/daka/patch/index")) {
                    pagename = "用户补签页";
                } else if (urlParse(u, "path", "/daka/patch/success")) {
                    pagename = "用户补签成功";
                } else if (urlParse(u, "path", "/daka/patch")) {
                    pagename = "用户补签页";
                } else if (urlParse(u, "path", "/mc/core/daka/RecommendFollower")) {
                    pagename = "推荐关注用户页";
                } else if (urlParse(u, "path", "/mc/core/daka/Task")) {
                    pagename = "用户打卡页";
                } else if (urlParse(u, "path", "/mc/core/daka/RecommendFollower")) {
                    pagename = "推荐关注用户页";
                }else {
                    pagename = null;
                } //end
            } //嗡嗡
            else if (urlParse(u, "path", "/weng/|/gl/article/weng|/weng-web/|/ww/activity/|/wengs/")) {
                if (urlParse(u, "path", "/weng/detail/comment_list")) {
                    pagename = "嗡嗡评论列表页";
                } ///weng/detail/comment_list
                else if (urlParse(u, "path", "/weng/detail$")) {
                    pagename = "嗡嗡详情页";
                } //https://m.mafengwo.cn/weng/detail?id=1609760670214417
                else if (urlParse(u, "path", "/gl/article/weng")) {
                    pagename = "嗡嗡周刊页";
                } //  https://m.mafengwo.cn/gl/article/weng?id=7795
                else if (urlParse(u, "path", "/weng-web/summary2017")) {
                    pagename = "嗡嗡活动页";
                } //https://m.mafengwo.cn/weng-web/summary2017
                else if (urlParse(u, "path", "/weng-web/yearbook2017")) {
                    pagename = "嗡嗡活动页";
                } //https://m.mafengwo.cn/weng-web/yearbook2017?uid=704545
                else if (urlParse(u, "path", "/ww/activity/")) {
                    pagename = "嗡嗡活动页";
                } //https://m.mafengwo.cn/ww/activity/questionnaire/index
                else if (urlParse(u, "path", "/wengs/invitation2.php")) {
                    pagename = "嗡嗡跳转页";
                } //https://m.mafengwo.cn/wengs/invitation2.php
                else if (urlParse(u, "path", "/wengs/j/t?tags=")) {
                    pagename = "聚合标签落地页";
                }
                else if (regpa("/hotel/order_detail/index", u)) {
                    pagename = "酒店订单详情";
                }//add 201901
                else {
                    pagename = null;
                } //end
            } //问答
            else if (urlParse(u, "path", "/wenda/|/qa/|interest/list/list|subject/list/list|/ifu/")) {
                if (urlParse(u, "path", "wenda/list/hot_discuss")) {
                    pagename = "讨论类问题列表";
                } //https://m.mafengwo.cn/wenda/list/hot_discuss
                else if (urlParse(u, "path", "wenda/expert/apply")) {
                    pagename = "问答指路人申请填写页";
                } //https://m.mafengwo.cn/wenda/expert/apply
                else if (urlParse(u, "path", "/wenda/expert/index")) {
                    pagename = "问答指路人申请页";
                } else if (urlParse(u, "path", "/qa/expert_apply.php")) {
                    pagename = "问答指路人申请页";
                } ///qa/expert_apply.php
                else if (urlParse(u, "path", "wenda/hot_topic.php")) {
                    pagename = "讨论类问题列表";
                } ///wenda/hot_topic.php
                else if (urlParse(u, "path", "wenda/list/topic_question")) {
                    pagename = "话题类问题列表";
                } //https://m.mafengwo.cn/wenda/list/topic_question?qid=11284973
                else if (urlParse(u, "path", "wenda/list/topic$")) {
                    pagename = "话题类话题列表";
                } //https://m.mafengwo.cn/wenda/list/topic?topic_id=1001
                else if (urlParse(u, "path", "/wenda/list/topic_question")) {
                    pagename = "讨论类问题详情页";
                } else if (urlParse(u, "path", "/wenda/list/hot_discuss")) {
                    pagename = "问答热门问题讨论页";
                } //https://m.mafengwo.cn/wenda/list/hot_discuss
                else if (urlParse(u, "path", "/wenda/list/search")) {
                    pagename = "问答列表搜索结果页";
                } //  https://m.mafengwo.cn/wenda/list/search?key=****
                else if (urlParse(u, "path", "/qa/draft_list")) {
                    pagename = "问答草稿箱";
                } else if (urlParse(u, "path", "/wenda/user_task/")) {
                    pagename = "问答打卡活动";
                } else if (urlParse(u, "path", "/wenda/replyask.php")) {
                    pagename = "问答回答页";
                } //https://m.mafengwo.cn/wenda/replyask.php?qid=13753260
                else if (urlParse(u, "path", "/wenda/$")) {
                    pagename = "问答首页";
                } else if (urlParse(u, "path", "/wenda/detail")) {
                    pagename = "问答详情页";
                } else if (urlParse(u, "path", "interest/list/list")) {
                    pagename = "兴趣类问题列表页";
                } //https://m.mafengwo.cn/interest/list/list?id=1056
                else if (urlParse(u, "path", "subject/list/list")) {
                    pagename = "主题类问题列表";
                } //https://m.mafengwo.cn/subject/list/list?id=8
                else if (urlParse(u, "path", "/ifu/\\d+\\.html")) {
                    pagename = "IFU问答";
                } //https://m.mafengwo.cn/ifu/883490.html
                else if (urlParse(u, "path", "/wenda/list/publish")) {
                    pagename = "问答提问";
                } //https://m.mafengwo.cn/wenda/list/publish
                else if (urlParse(u, "path", "/wenda/area\\-\\d+.html")) {
                    pagename = "问答区域列表页";
                } //   https://m.mafengwo.cn/wenda/area-12700.html
                else {
                    pagename = null;
                } //end
            } //游记
            else if (urlParse(u, "path", "/apps_note/|/note/|^/i/|/yj/|/group/info.php")) {
                if (urlParse(u, "path", "/apps_note/activity/theme/index$")) {
                    pagename = "目的地主题游记主题列表";
                } //https://m.mafengwo.cn/apps_note/activity/theme/index?type=1&id=981
                else if (urlParse(u, "path", "/apps_note/activity/theme/index/list")) {
                    pagename = "目的地主题游记游记列表";
                } //https://m.mafengwo.cn/apps_note/activity/theme/index/list2?mddid=10099
                else if (urlParse(u, "path", "/apps_note/activity/theme/index/list2")) {
                    pagename = "目的地主题游记游记列表";
                } else if (urlParse(u, "path", "/apps_note/activity/([^/]+)/")) {
                    pagename = "游记活动";
                } ///apps_note/activity/subject/2018qingming/detail
                else if (urlParse(u, "path", "apps_note/columnist/item/show")) {
                    pagename = "游记活动";
                } //-https://m.mafengwo.cn/apps_note/columnist/item/show?id=56079997
                else if (urlParse(u, "path", "/note/activity/topic/index")) {
                    pagename = "游记活动";
                } else if (urlParse(u, "path", "note/mddtop/apply/index/")) {
                    pagename = "宝藏游记申请";
                } //https://m.mafengwo.cn/note/mddtop/apply/index/main?from=mdd_top
                else if (urlParse(u, "path", "note/create_index.php")) {
                    pagename = "写游记";
                } //-http://www.mafengwo.cn/note/create_index.php?savebox=1
                else if (urlParse(u, "path", "/note/create.php")) {
                    pagename = "写游记";
                } //http://www.mafengwo.cn/note/create.php
                else if (urlParse(u, "path", "/note/create.php/modify/")) {
                    pagename = "编辑游记";
                } ///note/create.php/modify/
                else if (urlParse(u, "path", "note/upload_photo")) {
                    pagename = "游记上传图片";
                } //https://m.mafengwo.cn/note/upload_photo_new.php?k=note_modify_qrcode_upimg32699593842132151
                else if (urlParse(u, "path", "note/upload_photo_new")) {
                    pagename = "游记上传图片";
                } //https://m.mafengwo.cn/note/upload_photo_new.php?
                else if (urlParse(u, "path", "/note/activity/([^/]+)")) {
                    pagename = "游记活动";
                } else if (urlParse(u, "path", "/note/task/")) {
                    pagename = "游记打卡活动";
                } else if (urlParse(u, "path", "/note/ginfo.php")) {
                    pagename = "游记详情";
                } else if (urlParse(u, "path", "/note/detail.php")) {
                    pagename = "游记详情";
                } //http://www.mafengwo.cn/note/detail.php?iId=10355838&iPage=2&h=64707714
                else if (urlParse(u, "path", "/i/\\d+")) {
                    pagename = "游记详情";
                } else if (urlParse(u, "path", "/yj/")) {
                    pagename = "游记列表页";
                } else if (urlParse(u, "path", "/group/info.php")) {
                    pagename = "游记详情";
                } //http://www.mafengwo.cn/group/info.php?iid=3091513&page=1&h=40950218&t=0.7963559799827635
                else {
                    pagename = null;
                } //end
            } //电商定制
            else if (urlParse(u, "path", "/refer_line/|/customize/")) {
                if (urlParse(u, "path", "refer_line/mdd_entrance.php")&&getUrl(u, "query", "track_id")!=null && !getUrl(u,"query","track_id").equals("")) {
                    pagename = "电商目的地定制频道";
                } else if (urlParse(u, "path", "refer_line/mdd_entrance.php")&&getUrl(u, "query", "track_id")!=null && getUrl(u,"query","track_id").equals("")) {
                    pagename = "电商商城定制频道";
                } else if (urlParse(u, "path", "customize/v2/quoteList")) {
                    pagename = "电商定制报价列表";
                } //https://m.mafengwo.cn/customize/v2/quoteList?scheme_id=1599685466453166
                else if (urlParse(u, "path", "customize/v2/applyReceipt")) {
                    pagename = "电商定制开发票";
                } //https://m.mafengwo.cn/customize/v2/applyReceipt?demand_id=1600151854618675
                else if (urlParse(u, "path", "customize/v2$")&&getUrl(u, "query", "demand_id")!=null && !getUrl(u,"query","demand_id").equals("")) {
                    pagename = "电商定制方案详情";
                } else if (urlParse(u, "path", "customize/v2/signContract")) {
                    pagename = "电商定制查看合同";
                } //https://m.mafengwo.cn/customize/v2/signContract?demand_id=1601350237474134
                else if (urlParse(u, "path", "refer_line/demand_submission.php")) {
                    pagename = "电商定制需求提交";
                } //https://m.mafengwo.cn/refer_line/demand_submission.php?entry_type=guide&entry_id=69510
                else if (urlParse(u, "path", "refer_line/mall_entrance.php")) {
                    pagename = "电商定制频道";
                } //https://m.mafengwo.cn/refer_line/mall_entrance.php?departure_mdd_id=10195
                else if (urlParse(u, "path", "refer_line/submit_success.php")) {
                    pagename = "电商定制需求提交成功";
                } //https://m.mafengwo.cn/refer_line/submit_success.php?demand_id=1601672123776673
                else if (urlParse(u, "path", "refer_line/mdd_channel_whats.php")) {
                    pagename = "电商定制介绍";
                } //https://m.mafengwo.cn/refer_line/mdd_channel_whats.php?line_mdd_id=14731&entry_id=2232120&track_id=10004
                else if (urlParse(u, "path", "customize/v2/quoteList")) {
                    pagename = "电商定制报价列表";
                } //https://m.mafengwo.cn/customize/v2/quoteList?scheme_id=1601673407199697
                else if (urlParse(u, "path", "refer_line/aggregation.php")) {
                    pagename = "电商目的地定制频道";
                } //https://m.mafengwo.cn/refer_line/aggregation.php?line_mdd_id=15148&track_id=10002
                else if (urlParse(u, "path", "refer_line/aggregation_country.php")) {
                    pagename = "电商目的地定制频道";
                } //https://m.mafengwo.cn/refer_line/aggregation_country.php?line_mdd_id=10180&track_id=10002
                else if (urlParse(u, "path", "refer_line/aggregation_country_multi.php")) {
                    pagename = "电商目的地定制频道";
                } //https://m.mafengwo.cn/refer_line/aggregation_country_multi.php?line_mdd_id=14431&track_id=10002
                else if (urlParse(u, "path", "customize/scheme.php")) {
                    pagename = "电商定制方案详情";
                } //-https://m.mafengwo.cn/customize/scheme.php?id=1601672077129927 方案详情
                else if (urlParse(u, "path", "customize/scheme/index")) {
                    pagename = "电商定制方案详情";
                } //-http://m.mafengwo.cn/customize/scheme/index?id=1599428965964019 方案详情 产品形态
                else if (urlParse(u, "path", "refer_line/service_intro.php")) {
                    pagename = "电商定制特色服务";
                } //-https://m.mafengwo.cn/refer_line/service_intro.php 特色服务
                else if (urlParse(u, "path", "refer_line/mdd_entrance.php")) {
                    pagename = "电商目的地定制频道";
                } //https://m.mafengwo.cn/refer_line/mdd_entrance.php?line_mdd_id=10051
                else if (urlParse(u, "path", "/customize/customize_demand.php")) {
                    pagename = "定制旅行频道-定制需求提交页";
                } //add 201901
                else {
                    pagename = null;
                } //end
            } //视频
            else if (urlParse(u, "path", "/movie/")) {
                if (urlParse(u, "path", "/movie/detail/")) {
                    pagename = "视频详情页";
                } //  https://m.mafengwo.cn/movie/detail/428596.html
                else {
                    pagename = null;
                } //end
            } //支付
            else if (urlParse(u, "path", "/pay/")) {
                if (urlParse(u, "path", "/pay/success.php")) {
                    pagename = "支付成功事件";
                } //  https://m.mafengwo.cn/movie/detail/428596.html
                else {
                    pagename = null;
                } //end
            } //民宿
            else if (urlParse(u, "path", "homestay")){
                if (regpa("homestay/\\?action=create_order", u)) {
                    pagename = "民宿预订";
                }else if (regpa("homestay\\?action=order_detail", u)) {
                    pagename = "民宿订单详情";
                }else {
                    pagename=null;
                }
            }
            else if (urlParse(u, "path", "/search.php")) {
                pagename = "搜索主页";
            } // https://m.mafengwo.cn/search.php
            else if (urlParse(u, "path", "/msg/")) {
                pagename = "消息";
            } // http://www.mafengwo.cn/msg/sms/index
            else if (urlParse(u, "path", "/mall/")) {
                pagename = "马蜂窝周边";
            } //https://m.mafengwo.cn/mall/
            else if (urlParse(u, "path", "/auction/")) {
                pagename = "社区拍卖";
            } //  http://www.mafengwo.cn/auction/?date=2018-10-09
            else if (urlParse(u, "path", "/traveller/")) {
                pagename = "旅行家频道";
            } //https://m.mafengwo.cn/traveller/traveller_user/index?id=140
            else if (urlParse(u, "path", "/app_rss/tag")) {
                pagename = "聚合标签列表页";
            } ///app_rss/tag
            else if (urlParse(u, "path", "/together/")) {
                pagename = "结伴活动";
            } else if (urlParse(u, "path", "/club/")) {
                pagename = "蜂首俱乐部";
            } else if (urlParse(u, "path", "/show/")) {
                pagename = "真人秀";
            } else if (urlParse(u, "path", "/radio/")) {
                pagename = "电台";
            } else if (urlParse(u, "path", "/friend/")) {
                pagename = "粉丝";
            } else if (urlParse(u, "path", "/setting/")) {
                pagename = "个人设置";
            } //设置收货地址   https://m.mafengwo.cn/setting/receipt/list?app=**&obj=******
            else if (urlParse(u, "path", "/callcenter/")) {
                pagename = "客服中心";
            } else if (urlParse(u, "path", "^/photo/")) {
                pagename = "图片页";
            } //https://m.mafengwo.cn/photo/10101/scenery_2952983/162759050.html
            else if (urlParse(u, "path", "/home/vip_show.php")) {
                pagename = "VIP";
            } else if (urlParse(u, "path", "/game/wager.php")) {
                pagename = "分歧终端机";
            } else if (urlParse(u, "path", "/aps/book/")) {
                pagename = "品牌活动";
            } else if (urlParse(u, "path", "/order_center")) {
                pagename = "订单中心";
            } else if (urlParse(u, "path", "/group/glist.php")) {
                pagename = "所有小组";
            } else if (urlParse(u, "path", "/gl/article/comment_list")) {
                pagename = "评论列表页";
            } //https://m.mafengwo.cn/gl/article/comment_list?id=8002
            else if (urlParse(u, "path", "/gl/article/index")) {
                pagename = "文章攻略";
            } // https://m.mafengwo.cn/gl/article/index?id=264&type=4
            else if (urlParse(u, "path", "/gl/catalog/index")) {
                pagename = "文章攻略";
            } // https://m.mafengwo.cn/gl/catalog/index?id=160&catalog_id=3785
            else if (urlParse(u, "path", "/gl/article/public")) {
                pagename = "文章攻略";
            } //https://m.mafengwo.cn/gl/article/public?public_id=1198
            else if (urlParse(u, "path", "/gl/article/public_user_list")) {
                pagename = "文章攻略列表";
            } // https://m.mafengwo.cn/gl/article/public_user_list
            else if (urlParse(u, "path", "/gl/article/preview")) {
                pagename = "文章攻略";
            } //https://m.mafengwo.cn/gl/article/preview?id=1222
            else if (urlParse(u, "path", "/weixin/article")) {
                pagename = "文章攻略";
            } //https://www.mafengwo.cn/weixin/article-3404.html
            else if (urlParse(u, "path", "weather/\\d+\\.html")) {
                pagename = "目的地天气";
            } //https://m.mafengwo.cn/weather/11047.html
            else if (urlParse(u, "path", "about/licence")) {
                pagename = "营业执照";
            } //https://m.mafengwo.cn/about/licence.html
            else if (urlParse(u, "path", "/about/agreement")) {
                pagename = "服务协议";
            } //-/about/agreement.html
            else if (urlParse(u, "path", "/activity/index.php")) {
                pagename = "社区活动";
            } //https://m.mafengwo.cn/activity/index.php
            else if (urlParse(u, "path", "/mfw_activity/index")) {
                pagename = "社区活动";
            } else if (urlParse(u, "path", "/activity/college_entrance/")) {
                pagename = "社区活动";
            } //https://m.mafengwo.cn/activity/college_entrance/index/enter
            else if (urlParse(u, "path", "/account/$")) {
                pagename = "忘记密码";
            } //https://m.mafengwo.cn/account/?app=travelguide
            else if (urlParse(u, "path", "/g/i/\\d+")) {
                pagename = "小组活动详情";
            } else if (urlParse(u, "path", "/g/\\d+.html")) {
                pagename = "小组活动列表";
            } else if (urlParse(u, "path", "short_goto.php")) {
                pagename = "用户打卡页";
            } //https://www.mafengwo.cn/short_goto.php?id=1696556
            else if (urlParse(u, "path", "nb/public/sharejump.php")) {
                pagename = "sharejump";
            } else if (urlParse(u, "path", "nb/public/jump_bridge.php")) {
                pagename = "jump_bridge";
            } //基本都是淘宝//https://m.mafengwo.cn/nb/public/jump_bridge.php?url=http://h5.m.taobao.com/awp/core/detail.htm?id=543158497409
            else if (urlParse(u, "path", "nb/public/scaning.php")) {
                pagename = "scaning";
            } else if (urlParse(u, "path", "/404.php")) {
                pagename = "fail_404";
            } //else if (parse_url(uri,"PATH") regexp "/mobile/catalog//1.html")) { pagename = "自由行攻略"///mobile/catalog//1.html
            else if (urlParse(u, "path", "/im/qa/index$")) {
                pagename = "酒店常见问题列表";
            } //https://m.mafengwo.cn/im/qa/index?type=27&typeinfo=%7B%22hotel_order%22%3A%227350346%22%7D&tid=9&_refer=list
            else if (urlParse(u, "path", "im/qa/index/detail")) {
                pagename = "酒店常见问题详情";
            } //https://m.mafengwo.cn/im/qa/index/detail?tid=9&id=279&type=27&typeinfo=%7B%22hotel_order%22:%228002058%22%7D&leaf=1
            else if (urlParse(u, "path", "/im/feedback/index.php")) {
                pagename = "常见问题列表";
            } //https://m.mafengwo.cn/im/feedback/index.php
            else if (urlParse(u, "path", "im/feedback/detail")) {
                pagename = "常见问题详情";
            } //https://m.mafengwo.cn/im/feedback/detail?subtype=5
            else if (urlParse(u, "path", "im/weiquan/index")) {
                pagename = "用户维权常见问题列表";
            } //https://m.mafengwo.cn/im/weiquan/index
            else if (urlParse(u, "path", "im/weiquan/detail")) {
                pagename = "用户维权常见问题详情";
            } //https://m.mafengwo.cn/im/weiquan/detail
            else if (urlParse(u, "path", "agreement.php")) {
                pagename = "服务协议";
            } //http://m.mafengwo.cn/agreement.php
            else if (urlParse(u, "path", "/s/agreement\\.html")) {
                pagename = "服务协议";
            } ///s/agreement.html
            else if (urlParse(u, "path", "/app/$")) {
                pagename = "APP下载中间页";
            } //http://m.mafengwo.cn/app/?type=gonglve
            else if (urlParse(u, "path", "/app/down/gl/")) {
                pagename = "APP下载中间页";
            } //https://m.mafengwo.cn/app/down/gl/?type=pc
            else if (urlParse(u, "path", "/cs.php")) {
                pagename = "app下载";
            } //
            else if (urlParse(u, "path", "/app/download/")) {
                pagename = "app下载";
            } //
            else if (urlParse(u, "path", "/oad/iad.php")) {
                pagename = "品牌活动";
            } //else if (regexp_replace(regexp_replace(parse_url(p.uri,"PATH"),"\\d+|html",""),"/$","")<>"" ) { pagename = regexp_replace(regexp_replace(parse_url(p.uri,"PATH"),"\\d+|html",""),"/$","")
            else if ("/404".equals(getUrl(u,"path",null))) {
                pagename = "错误页";
            } //add 201901
            else if (urlParse(u, "path", "mc/core/medal/index")) {
                pagename = "用户勋章页";
            }
            else if (urlParse(u, "path", "/mmobile/wanfamobile/detail")) {
                pagename = "新玩法详情页";
            }
            else if (regReplace(u) != "") {
                pagename = "可解析出url参数且url为马蜂窝";
            } else {
                pagename = null;
            }
        } else if (hostreg(u, "mafengwo")) {
            pagename = "马蜂窝新域名页面";
        } else if (regpa("/hotel/order_detail/index", u)) {
            pagename = "酒店订单详情";
        } else if (regpa("/hotel/activity", u)) {
            pagename = "酒店大促活动";
        } else {
            pagename = "非马蜂窝页面";
        }
        if (pagename == null) {
            return null;
        }
        Text text = new Text(pagename);
        return text;}

   // 传入uri，uri的参数类型，判断该uri的该参数是否匹配reg正则。其中host是判断是否等于reg
    public Boolean urlParse(String url, String type, String reg) {
        return urlParse(url, type, reg, null);
    }

    private Boolean urlParse(String url, String type, String reg, String quekey) {
        URL url1 = null;
        try {
            url1 = new URL(url);
        } catch (MalformedURLException e) {
            return false;
        }
        String path = url1.getPath();
        String host = url1.getHost();
        String query = url1.getQuery();
        String ref = url1.getRef();
        String quevalue = "";
        if (quekey != null && query != null) {
            String[] s = query.split("&");
            for (String s1 : s) {
                if(s1.split("=").length>1) {
                    if(s1.split("=")[0].equals(quekey)) {
                        quevalue = s1.split("=")[1];
                        break;
                    }
                }
            }
        }
        boolean flag = false;
        Pattern pattern = Pattern.compile(reg);
        if (type.equals("path")) {
            if (path == null) {
                return false;
            }
            Matcher matcher = pattern.matcher(path);
            flag = matcher.find();
        } else if (type.equals("host")) {
            flag = reg.equals(host);
        } else if (type.equals("query")) {
            Matcher matcher = pattern.matcher(quevalue);
            flag = matcher.find();
        } else if (type.equals("ref")) {
            if (ref == null) {
                return false;
            }
            Matcher matcher = pattern.matcher(ref);
            flag = matcher.find();
        }
        return flag;

    }

    //获取url各个参数
    public String getUrl(String url,String type,String quekey){
        URL url1 = null;
        try {
            url1 = new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }
        String path = url1.getPath();
        String host = url1.getHost();
        String query = url1.getQuery();
        String ref = url1.getRef();
        String quevalue = null;
        if (quekey != null && query != null) {
            String[] s = query.split("&");
            for (String s1 : s) {
                if (s1.contains(quekey)) {
                    quevalue = s1.split("=")[1];
                    break;
                }
            }
        }
        if (type.equals("path")) {
           return path;
        } else if (type.equals("host")) {
            return host;
        } else if (type.equals("query")) {
           return quevalue;
        } else if (type.equals("ref")) {
           return ref;
        }else{
            return null;
        }
    }

    //判断字符串u是否匹配正则表达式reg
    public boolean regpa(String reg, String u) {
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(u);
        return matcher.find();
    }

    //判断uri 的 host是否匹配 reg正则
    private boolean hostreg(String u, String reg) {
        URL url1 = null;
        try {
            url1 = new URL(u);
        } catch (MalformedURLException e) {
            return false;
        }
        String host = url1.getHost();
        if (host == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(host);
        return matcher.find();
    }


    // 对uri path 的一些字符串做 特定的替换
    public String regReplace(String url) {
        URL url1 = null;
        try {
            url1 = new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }
        String path = url1.getPath();
        String path2 = path.replace("\\d+|html", "").replace("/$", "");
        return path2;
    }

    public static void main(String[] args) {
       String uri="http://app.mafengwo.cn/traffic/index?_ouri=http%3A%2F%2Fm.mafengwo.cn%2Fnb%2Fpublic%2Fsharejump.php%3Ftype%3D1500%26source%3Dapp_home&source=app_home";
        Text text=new Text(uri);
        PageName pageName=new PageName();
        System.out.println(pageName.evaluate(text,new Text("大交通频道-首页")));

    }


}
