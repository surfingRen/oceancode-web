package cn.com.oceancode.test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class HtmlParser {

	public static void main(String[] args) throws ParseException, JSONException, ParserException, MalformedURLException, IOException {
//		h1();
//		h2();
		
		Parser parser = new Parser(
				(HttpURLConnection) (new URL("http://waimai.baidu.com/waimai/shop/1458294993")).openConnection());
		parser.setEncoding("UTF-8");
		NodeFilter frameFilter = new NodeFilter() {
			public boolean accept(Node node) {
				if (node.getText().startsWith("h3   data-title")) {
					System.out.println(node.getText());
					return true;
				} else {
					return false;
				}
			}
		};
		
		parser.extractAllNodesThatMatch(frameFilter);
		
	}

	private static void h2() {
		try {


			Parser parser = new Parser(
					(HttpURLConnection) (new URL("http://waimai.baidu.com/waimai/shop/1487474619")).openConnection());
			parser.setEncoding("UTF-8");
			NodeFilter frameFilter = new NodeFilter() {
				public boolean accept(Node node) {
					System.out.println(node.getText());
					if (node.getText().startsWith("li class=\"list-item\"")) {
						return true;
					} else {
						return false;
					}
				}
			};
			TagNode tagNode = null;
			// 得到所有经过过滤的标签
			NodeList list = parser.extractAllNodesThatMatch(frameFilter);
			for (int i = 0; i < list.size(); i++) {
				NodeList list2 = list.elementAt(i).getChildren().extractAllNodesThatMatch(new NodeFilter() {
					public boolean accept(Node arg0) {
						if (arg0.getText().startsWith("div class=\"bg-img\"")) {
							return true;
						} else {
							return false;
						}
					}
				});
				tagNode = new TagNode();
				tagNode.setText(list.elementAt(i).toHtml());
				System.out.println(tagNode.getAttribute("data"));
				for (int j = 0; j < list2.size(); j++) {
					tagNode = new TagNode();
					tagNode.setText(list2.elementAt(j).toHtml());
					System.out.println(tagNode.getAttribute("style"));
				}
			}

		} catch (Exception e) {
			System.out.println("Exception:" + e);
		}
	}

	private static void h1() throws ParseException, JSONException {
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();

//		HttpGet httpGet = new HttpGet("http://waimai.baidu.com/waimai/shop/1487474619");
		int page = 1;
		HttpGet httpGet = new HttpGet("http://waimai.baidu.com/waimai/shoplist/00a2bee7926a2f09?display=json&page="+page+"&count=10");
//		http://waimai.baidu.com/shopui/shopcertificate?shop_id=17867188208576217211
//		http://waimai.baidu.com/shopui/?qt=shopcomment&shop_id=17867188208576217211
		System.out.println(httpGet.getRequestLine());
		try {
			// 执行get请求
			HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
			// 获取响应消息实体
			HttpEntity entity = httpResponse.getEntity();

			// 响应状态
			System.out.println("status:" + httpResponse.getStatusLine());
			// 判断响应实体是否为空
			if (entity != null) {
				System.out.println("contentEncoding:" + entity.getContentEncoding());
				String string = EntityUtils.toString(entity);
				System.out.println("response content:" + string);
				JSONObject jsonobj = new JSONObject(string);
				JSONArray jsonArray = jsonobj.getJSONObject("result").getJSONArray("shop_info");
				for(int i=0;i<jsonArray.length();i++){
					System.out.print(jsonArray.getJSONObject(i).get("shop_name"));
					System.out.print(jsonArray.getJSONObject(i).get("shop_announcement"));
					System.out.print(jsonArray.getJSONObject(i).get("logo_url"));
					System.out.print(jsonArray.getJSONObject(i).get("shop_id"));
					System.out.print(jsonArray.getJSONObject(i).get("source_name"));
					System.out.print(jsonArray.getJSONObject(i).get("shop_lng"));
					System.out.print(jsonArray.getJSONObject(i).get("shop_lat"));
					System.out.print(jsonArray.getJSONObject(i).get("category"));
					System.out.println(jsonArray.getJSONObject(i).get("is_online"));
				}
			}
/*			end_time: "",
			average_service_score: 0,
			comment_service_num: 0,
			average_dish_score: 0,
			comment_dish_num: 0,
			supplier_id: "",
			bussiness_status_reason: "",
			delivery_regions: "",
			takeout_deliver_regions_type: "",
			origial_region_type: "",
			release_id: "1495706310",
			is_online: 1,
			saled: 391,
			advance_need_order_day: 0,
			sort: 0,
			is_collected: false,
			activity_ids: [
			"10201603182122891",
			"176505"
			],
			front_logistics_text: "百度专送",
			front_logistics_type: "baidulogistics",
			welfare_info: [],
			welfare_basic_info: [],
			welfare_act_info: [],
			welfare_spe_info: [ ],
			welfare_couponamout_info: [ ],
			is_certificated: 0,
			discount_info: {},
			coupon_info: {},
			invoice_info: {},
			bd_express: 1,
			shop_name: "赣南村（张扬路）",
			shop_announcement: "本店欢迎您下单，用餐高峰请提前下单，谢谢！",
			logo_url: "http://webmap1.map.bdimg.com/static/waimai/images/shopcard_default_bg_7236c9d.png",
			takeout_price: "30",
			takeout_cost: "5",
			comment_num: 0,
			brand: "",
			delivery_time: "40",
			start_time: "10:45",
			bussiness_status_text: "",
			distance: 93.404129291131,
			bussiness_status: 2,
			shop_id: "1495706310",
			saled_month: 67,
			average_score: 4.7,
			is_new: 0,
			is_recommend: 0,
			is_favorited: 0,
			shop_lng: "13528311.77",
			shop_lat: "3640841.97",
			source_name: "baidu",
			is_store: 0,
			category: "餐饮",
			category_flag: 0,
			top_hot_dish: [ ]*/
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭流并释放资源
				closeableHttpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
