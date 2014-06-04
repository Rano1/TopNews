package com.topnews.tool;

import java.util.ArrayList;
import java.util.List;

import com.topnews.bean.CityEntity;
//import com.topnews.bean.NewsClassify;
import com.topnews.bean.NewsEntity;

public class Constants {
	/*
	 * 获取新闻列表
	 */
	public static ArrayList<NewsEntity> getNewsList() {
		ArrayList<NewsEntity> newsList = new ArrayList<NewsEntity>();
		for(int i =0 ; i < 10 ; i++){
			NewsEntity news = new NewsEntity();
			news.setId(i);
			news.setNewsId(i);
			news.setCollectStatus(false);
			news.setCommentNum(i + 10);
			news.setInterestedStatus(true);
			news.setLikeStatus(true);
			news.setReadStatus(false);
			news.setNewsCategory("推荐");
			news.setNewsCategoryId(1);
			news.setSource_url("http://news.sina.com.cn/c/2014-05-05/134230063386.shtml");
			news.setTitle("可以用谷歌眼镜做的10件酷事：导航、玩游戏");
			List<String> url_list = new ArrayList<String>();
			if(i%2 == 1){
				String url1 = "http://infopic.gtimg.com/qq_news/digi/pics/102/102066/102066094_400_640.jpg";
				String url2 = "http://infopic.gtimg.com/qq_news/digi/pics/102/102066/102066096_400_640.jpg";
				String url3 = "http://infopic.gtimg.com/qq_news/digi/pics/102/102066/102066099_400_640.jpg";
				news.setPicOne(url1);
				news.setPicTwo(url2);
				news.setPicThr(url3);
				news.setSource_url("http://tech.sina.com.cn/zl/post/detail/i/2013-11-06/pid_8436571.htm?from=groupmessage&isappinstalled=0");
				url_list.add(url1);
				url_list.add(url2);
				url_list.add(url3);
			}else{
				news.setTitle("AA用车:智能短租租车平台");
				String url = "http://r3.sinaimg.cn/2/2014/0417/a7/6/92478595/580x1000x75x0.jpg";
				news.setPicOne(url);
				url_list.add(url);
			}
			news.setPicList(url_list);
			news.setPublishTime(Long.valueOf(i));
			news.setReadStatus(false);
			news.setSource("手机腾讯网");
			news.setSummary("腾讯数码讯（编译：Gin）谷歌眼镜可能是目前最酷的可穿戴数码设备，你可以戴着它去任何地方（只要法律法规允许或是没有引起众怒），作为手机的第二块“增强现实显示屏”来使用。另外，虽然它仍未正式销售，但谷歌近日在美国市场举行了仅限一天的开放购买活动，价格则为1500美元（约合人民币9330元），虽然仍十分昂贵，但至少可以满足一些尝鲜者的需求，也预示着谷歌眼镜的公开大规模销售离我们越来越近了。");
			news.setMark(i);
			if(i == 4){
				news.setTitle("部落战争强势回归");
				news.setLocal("推广");
				news.setIsLarge(true);
				String url = "http://imgt2.bdstatic.com/it/u=3269155243,2604389213&fm=21&gp=0.jpg";
				news.setSource_url("http://games.sina.com.cn/zl/duanpian/2014-05-21/141297.shtml");
				news.setPicOne(url);
				url_list.clear();
				url_list.add(url);
			}else{
				news.setIsLarge(false);
			}
			if(i == 2){
				news.setComment("评论部分，说的非常好。");
			}
			
			if(i <= 2){
				news.setPublishTime(Long.valueOf(DateTools.getTime()));
			}else if(i >2 && i <= 5){
				news.setPublishTime(Long.valueOf(DateTools.getTime()) - 86400);
			}else{
				news.setPublishTime(Long.valueOf(DateTools.getTime()) - 86400 * 2);
			}
			newsList.add(news);
		}
		return newsList;
	}
	
	/** mark=0 ：推荐 */
	public final static int mark_recom = 0;
	/** mark=1 ：热门 */
	public final static int mark_hot = 1;
	/** mark=2 ：首发 */
	public final static int mark_frist = 2;
	/** mark=3 ：独家 */
	public final static int mark_exclusive = 3;
	/** mark=4 ：收藏 */
	public final static int mark_favor = 4;
	
	/*
	 * 获取城市列表
	 */
	public static ArrayList<CityEntity> getCityList(){
		ArrayList<CityEntity> cityList =new ArrayList<CityEntity>();
		CityEntity city1 = new CityEntity(1, "安吉", 'A');
		CityEntity city2 = new CityEntity(2, "北京", 'B');
		CityEntity city3 = new CityEntity(3, "长春", 'C');
		CityEntity city4 = new CityEntity(4, "长沙", 'C');
		CityEntity city5 = new CityEntity(5, "大连", 'D');
		CityEntity city6 = new CityEntity(6, "哈尔滨", 'H');
		CityEntity city7 = new CityEntity(7, "杭州", 'H');
		CityEntity city8 = new CityEntity(8, "金沙江", 'J');
		CityEntity city9 = new CityEntity(9, "江门", 'J');
		CityEntity city10 = new CityEntity(10, "山东", 'S');
		CityEntity city11 = new CityEntity(11, "三亚", 'S');
		CityEntity city12 = new CityEntity(12, "义乌", 'Y');
		CityEntity city13 = new CityEntity(13, "舟山", 'Z');
		cityList.add(city1);
		cityList.add(city2);
		cityList.add(city3);
		cityList.add(city4);
		cityList.add(city5);
		cityList.add(city6);
		cityList.add(city7);
		cityList.add(city8);
		cityList.add(city9);
		cityList.add(city10);
		cityList.add(city11);
		cityList.add(city12);
		cityList.add(city13);
		return cityList;
	}
	/* 频道中区域 如杭州 对应的栏目ID */
	public final static int CHANNEL_CITY = 3;
}
