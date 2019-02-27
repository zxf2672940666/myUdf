package com.mfw.udf;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import static com.mfw.utils.UrlUtil.*;

/**
 * @program: myUdf
 * @description: 用于解析page_item_id
 * @author: Liusengen
 * @create: 2019-02-27 17:03
 **/
public class PageItemID extends UDF {
    public Text evaluate(Text a,Text attr){
        if(null==a){
            return null;
        }
        String uri=a.toString();
        String piid="";
        String mddid="";
        if(attr==null){
            mddid=null;
        }else {
            mddid  = getmddid(attr.toString());
        }
        // host --> app.mafengwo.cn
        if("app.mafengwo.cn".equals(getUriparameter(uri,"host"))){
            if(parse_url(uri,"path","/poi/asking_card|/poi/map")){
                if(!"".equals(getUriparameter(uri, "poi_id"))){piid=getUriparameter(uri,"poi_id");}
                else if(!"".equals(getUriparameter(uri, "poiid"))){piid=getUriparameter(uri,"poiid");}
                else if(!"".equals(getUriparameter(uri, "id"))){piid=getUriparameter(uri,"id");}
                else{piid=null;}
            }else if(parse_url(uri,"path","/poi/photo_list")){
                piid=getUriparameter(uri,"poi_id");
            }else if(parse_url(uri,"path","/poi/comment/photo_list|/poi/comment_detail|/poi/comment_list|/poi/create_comment")){
                 if(!"".equals(getUriparameter(uri, "poi_id"))){piid=getUriparameter(uri,"poi_id");}
                 else if(!"".equals(getUriparameter(uri, "poiid"))){piid=getUriparameter(uri,"poiid");}
                 else if(!"".equals(getUriparameter(uri, "id"))){piid=getUriparameter(uri,"id");}
                 else{piid=null;}
            }else if(parse_url(uri,"path","/hotel/detail|/hotel/facility_list|/hotel/review_list")){
                if(!"".equals(getUriparameter(uri, "hotel_id"))){piid=getUriparameter(uri,"hotel_id");}
                 else if(!"".equals(getUriparameter(uri, "poi_id"))){piid=getUriparameter(uri,"poi_id");}
                 else{piid=null;}
            }else if(parse_url(uri,"path","/guide/detail|/guide/menu|/guide/set|/guide/search")){
                piid=getUriparameter(uri,"book_id");
            }else if(parse_url(uri,"path","/guide/article|/guide/sales")){
                if(!"".equals(getUriparameter(uri, "guide_article_id"))){piid=getUriparameter(uri,"guide_article_id");}
                 else if(!"".equals(getUriparameter(uri, "guide_id"))){piid=getUriparameter(uri,"guide_id");}
                 else{piid=null;}
            }else if(parse_url(uri,"path","/localdeal/mdd_topic")){
                piid= matchInt(getUriparameter(uri,"path"),"(\\d+)");
            }else if(parse_url(uri,"path","/sales/product_detail")){
                piid=getUriparameter(uri,"sale_id");
            }else if(parse_url(uri,"path","/travel_note/detail")){
                if(getUriparameter(uri,"travelnote_id") != null){piid=getUriparameter(uri,"travelnote_id");}
                 else if(getUriparameter(uri,"travelNote_id") != null){piid=getUriparameter(uri,"travelNote_id");}
                 else{piid=null;}
            } else if(parse_url(uri,"path","/qa/answer_detail")){
                if(!"".equals(getUriparameter(uri, "question_id"))){piid=getUriparameter(uri,"question_id");}
                 else if(!"".equals(getUriparameter(uri, "questionId"))){piid=getUriparameter(uri,"questionId");}
                 else if(!"".equals(getUriparameter(uri, "qid"))){piid=getUriparameter(uri,"qid");}
                 else if(!"".equals(getUriparameter(uri, "topic_id"))){piid=getUriparameter(uri,"topic_id");}
                 else{piid=null;}
            }else if(parse_url(uri,"path","/weng/detail")){
                piid= matchInt(uri,"(\\d+)");
            }else if(parse_url(uri,"path","/weng/recommend_detail")){
                piid= matchInt(uri,"(\\d+)");
            }else if(parse_url(uri,"path","/video/detail")){
                piid=getUriparameter(uri,"video_id");
            }else if(parse_url(uri,"path","/video/page_detail")){
                piid=getUriparameter(uri,"video_id");
            }else if(parse_url(uri,"path","/travel_note/video_play")){
                piid=getUriparameter(uri,"id");
            }else if(parse_url(uri,"path","/weng/video/preview")){
                piid=getUriparameter(uri,"id");
            }else if(parse_url(uri,"path","/mdd/detail|/local/index")){
                piid=mddid;
            }  else if(parse_url(uri,"path","/qa/question_detail")){piid=getUriparameter(uri,"questionId");}
            else if(parse_url(uri,"path", "/poi/travel_route/detail" )){piid=getUriparameter(uri,"tr_id");}
            else if(parse_url(uri,"path", "/homestay/detail" )){piid=getUriparameter(uri,"homestay_id");}
            else{piid=null;}
        }
        // host --> m.mafengwo.cn  www.mafengwo.cn
        else if("m.mafengwo.cn".equals(getUriparameter(uri,"host"))|| "www.mafengwo.cn".equals(getUriparameter(uri,"host"))){
            if(parse_url(uri,"path","/mdd/detail")){
                piid=mddid;
            }else if(parse_url(uri,"path","/mmobile/wanfamobile/detail")){
                piid=getUriparameter(uri,"id");
            }else if(parse_url(uri,"path","poi/treasure/index")){
                piid=getUriparameter(uri,"poiid");
            }else if(parse_url(uri,"path","/poi/poi-nojuma.php")){
                piid=getUriparameter(uri,"poiid");
            }else if(parse_url(uri,"path","poi/free_lunch/")){
                piid=getUriparameter(uri,"id");
            }else if(parse_url(uri,"path","/poi/sub_guide_")){
                piid= matchInt(getUriparameter(uri,"path"),"(\\d+)");
            }else if(parse_url(uri,"path","poi/info/\\d+-simple.html")){
                piid= matchInt(getUriparameter(uri,"path"),"(\\d+)");
            }else if(parse_url(uri,"path","/poi/detail")){
                 if(!"".equals(getUriparameter(uri, "poiid"))){piid=getUriparameter(uri,"poiid");}
                 else if(!"".equals(getUriparameter(uri, "poi_id"))){piid=getUriparameter(uri,"poi_id");}
                 else{piid=null;}
            }else if(parse_url(uri,"path","/poi/info/\\d+")){
                piid= matchInt(getUriparameter(uri,"path"),"(\\d+)");
            }else if(parse_url(uri,"path","/poi/\\d+\\.html")){
                piid= matchInt(getUriparameter(uri,"path"),"(\\d+)");
            }else if(parse_url(uri,"path","/poi/correct/")){
                piid=getUriparameter(uri,"poiid");
            }else if(parse_url(uri,"path","/poi/comment")){
                piid=getUriparameter(uri,"poiid");
            }else if(parse_url(uri,"path","/hotelzd/index.php")){
                piid=getUriparameter(uri,"id");
            }else if(parse_url(uri,"path","hotelzd/index_v2.php")){
                piid=getUriparameter(uri,"iHotelId");
            }else if(parse_url(uri,"path","/hotel/\\d+\\.html")){
                piid= matchInt(getUriparameter(uri,"path"),"(\\d+)");
            }else if(parse_url(uri,"path","/flight/inter/seats")){
                piid=getUriparameter(uri,"destCode");//https://m.mafengwo.cn/flight/inter/seats?status=0&adult_nums=1&departCode=HKT&departCity=%E6%99%AE%E5%90%89%E5%B2%9B&destCode=BKK&destCity=%E6%9B%BC%E8%B0%B7&departDate=2018-10-30&source=app_home&child_nums=0&baby_nums=0&dep_date_flightNo=20181030FD3026&curFlightListCacheKey=HKTBKK-20181030-1%230%230-All
            }else if(parse_url(uri,"path","/flight/seats")){
                piid=getUriparameter(uri,"destCode");
            }else if(parse_url(uri,"path","/sales/\\d+\\.html")){
                piid= matchInt(getUriparameter(uri,"path"),"(\\d+)");
            }else if(parse_url(uri,"path","/sales/ski/\\d+\\.html")){
                piid= matchInt(getUriparameter(uri,"path"),"(\\d+)");
            }else if(parse_url(uri,"path","/localdeals/\\d+\\.html")){
                piid= matchInt(getUriparameter(uri,"path"),"(\\d+)");
            }else if(parse_url(uri,"path","/cruise/\\d+.html")){
                piid= matchInt(getUriparameter(uri,"path"),"(\\d+)");
            }else if(parse_url(uri,"path","sales/product_detail")){
                piid= matchInt(uri,"sale_id=(\\d+)");
            }else if(parse_url(uri,"path","gl/guide/article/info/index")){
                piid=getUriparameter(uri,"article_id");
            }else if(parse_url(uri,"path","/gonglve/ziyouxing/\\d+\\.html")){
                piid= matchInt(getUriparameter(uri,"path"),"(\\d+)");
            }else if(parse_url(uri,"path","gonglve/ziyouxing/public/home/index")){
                piid=getUriparameter(uri,"public_id");
            }else if(parse_url(uri,"path","/gl/catalog/index")){
                piid=getUriparameter(uri,"id");
            }else if(parse_url(uri,"path","/gonglve/zt\\-\\d+")){
                piid= matchInt(getUriparameter(uri,"path"),"(\\d+)");
            }else if(parse_url(uri,"path","/gonglve/mdd\\-\\d+")){
                piid= matchInt(getUriparameter(uri,"path"),"(\\d+)");
            }else if(parse_url(uri,"path","/localdeals/mdd_topic_")){
                piid= matchInt(getUriparameter(uri,"path"),"(\\d+)");
            }else if(parse_url(uri,"path","/gl/article/index")){
                piid=getUriparameter(uri,"id");
            }else if(parse_url(uri,"path","/gl/catalog/index")){
                piid=getUriparameter(uri,"id");
            }else if(parse_url(uri,"path","/gl/article/public")){
                piid=getUriparameter(uri,"public_id");
            }else if(parse_url(uri,"path","/gl/article/preview")){
                piid=getUriparameter(uri,"id");
            }else if(parse_url(uri,"path","/weixin/article")){
                piid= matchInt(getUriparameter(uri,"path"),"(\\d+)");
            }else if(parse_url(uri,"path","/insurance/detail/")){
                piid= matchInt(getUriparameter(uri,"path"),"(\\d+)");
            }else if(parse_url(uri,"path","/weng/detail/comment_list")){
                piid="weng_detail";
            }else if(parse_url(uri,"path","/weng/detail$")){
                piid=getUriparameter(uri,"id");
            }else if(parse_url(uri,"path","/wenda/replyask.php")){
                piid=getUriparameter(uri,"qid");
            }else if(parse_url(uri,"path","/wenda/detail")){
                 if(matchInt(getUriparameter(uri,"path"),"(\\d+)")!=null){piid=matchInt(getUriparameter(uri,"path"),"(\\d+)");}
                 else{piid=null;}
            }else if(parse_url(uri,"path","/g/i/(\\d+)\\.html")){
                piid=matchInt(uri,"(\\d+)");
            }else if(parse_url(uri,"path","/mdd/article\\.php")){
                piid=matchInt(uri,"(\\d+)");
            }else if(parse_url(uri, "path","/activity_city/home")){
                piid=getUriparameter(uri,"activity_id");
            }else if(parse_url(uri, "path","/activity_city/Home")){
                piid=getUriparameter(uri,"activity_id");
            }else if(parse_url(uri,"path","/note/detail.php")){
                if(!"".equals(getUriparameter(uri, "iId"))){piid=getUriparameter(uri,"iId");}
                 else if(!"".equals(getUriparameter(uri, "iid"))){piid=getUriparameter(uri,"iid");}
                 else if(!"".equals(getUriparameter(uri, "id"))){piid=getUriparameter(uri,"id");}
                 else{piid=null;}
            }else if(parse_url(uri,"path","^/i/\\d+")){
                piid="note_detail";
            }else if(parse_url(uri,"path","/group/info.php")){
                 if(!"".equals(getUriparameter(uri, "iId"))){piid=getUriparameter(uri,"iId");}
                 else if(!"".equals(getUriparameter(uri, "iid"))){piid=getUriparameter(uri,"iid");}
                 else if(!"".equals(getUriparameter(uri, "id"))){piid=getUriparameter(uri,"id");}
                 else{piid=null;}
            }else if(parse_url(uri,"path","customize/v2$") && !"".equals(getUriparameter(uri, "demand_id"))){
                piid=getUriparameter(uri,"demand_id");
            }else if(parse_url(uri,"path","customize/scheme.php")){
                piid=getUriparameter(uri,"id");
            }else if(parse_url(uri,"path","customize/scheme/index")){
                piid=getUriparameter(uri,"id");
            }else if(parse_url(uri,"path","/movie/detail/")){
                piid= matchInt(getUriparameter(uri,"path"),"(\\d+)");
            }else if(parse_url(uri,"path","/travel-news/(\\d+)\\.html")){
                piid=matchInt(uri,"(\\d+)");
            }else if(parse_url(uri,"uri","/nb/h5/poi_base_info.php")){
                piid=getUriparameter(uri,"poi_id");
            }else if(parse_url(uri,"uri","/localdeals/mdd_topic_(\\d+)")){
                piid=matchInt(uri,"(\\d+)");
            }else{piid=null;}
        }
        // host --> payitf.mafengwo.cn
        else if("payitf.mafengwo.cn".equals(getUriparameter(uri, "host"))){
            if(!"".equals(getUriparameter(uri, "order_id"))){
               piid=getUriparameter(uri,"order_id");
            }else if(!"".equals(getUriparameter(uri, "redirect_url"))){
                    if(!"".equals(getUriparameter(getUriparameter(uri, "redirect_url"), "order_id"))){
                        piid=getUriparameter(getUriparameter(uri,"redirect_url"),"order_id");
                    }else if(!"".equals(getUriparameter(getUriparameter(uri, "redirect_url"), "demand_id"))){
                        piid=getUriparameter(getUriparameter(uri,"redirect_url"),"demand_id");
                    }else if(getUriparameter(getUriparameter(uri,"redirect_url"),"redirect_url")!=""){
                        piid=getUriparameter(getUriparameter(getUriparameter(uri,"redirect_url"),"redirect_url"),"demand_id");
                    }
                  else{piid=null;}
            }else if(getUriparameter(uri,"demand_id")!=""){
                piid=getUriparameter(uri,"demand_id");
            }else if(parse_url(uri,"uri","mafengwo.cn/return/")){
                piid=matchInt(uri,"(\\d+)");
            }
            else{piid=null; }
        }
        // host --> w.mafengwo.cn
        else if("w.mafengwo.cn".equals(getUriparameter(uri, "host"))){
            if(parse_url(uri,"uri","sales_customize/scheme_info\\?demand_id")){
                piid=getUriparameter(uri,"demand_id");
            }else if(parse_url(uri, "path","/sales_customize/scheme")){
                piid=getUriparameter(uri,"id");
            }else{piid=null;}
        }
        else{piid=null;}

        if(piid==null){
            return null;
        }
        return new Text(piid);
    }

    private String getmddid(String jsonstr) {
        if (jsonstr == null || jsonstr.length() == 0) {
            return null;
        }
        com.alibaba.fastjson.JSONObject jObj=null;
        try {
            jObj = JSON.parseObject(jsonstr);
        }catch (Exception e){
            return null;
        }
        if(null==jObj){
            return null;
        }
        return jObj.getString("mddid");
    }

    public static void main(String[] args) {
        PageItemID pageItemID=new PageItemID();
        String a="";
        Text text1=new Text(a);
        Text evaluate = pageItemID.evaluate(text1, new Text(""));
        System.out.println(evaluate);
    }


}
