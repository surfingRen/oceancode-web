package cn.com.oceancode.test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
import org.htmlparser.filters.OrFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class DBInit {

	static JdbcTemplate jdbcTemplate = null;
	static NamedParameterJdbcTemplate template = null;

	static {

		GenericXmlApplicationContext context = new GenericXmlApplicationContext();
		context.setValidating(false);
		context.load("classpath*:applicationContext*.xml");
		context.refresh();
		jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate_mysql");
		template = new NamedParameterJdbcTemplate(jdbcTemplate);

	}

	public static void main(String[] args2) throws ClientProtocolException, IOException, JSONException {

		for (int i = 0; i < 10; i++) {
			Thread t = new Thread(new Work());
			t.start();
		}
//		 pullShopInfoFromBaiduWaimai();
//		 pullDetailFromBaiduWaimai();
	}

	public static void pullShopInfoFromBaiduWaimai() {
		try {
			initShop();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static void pullDetailFromBaiduWaimai() {
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String sql = "select * from t_shop order by id limit 1000";
			List<Map<String, Object>> list = template.queryForList(sql, paramMap);
			String url = "http://waimai.baidu.com/waimai/shop/";
			System.out.println(list.size());
			if(true)return;
			int i = 0;
			for (Map<String, Object> map : list) {
				i += 1;
				System.out.print(i+" :");
				initDetail(url, map.get("id") + "", map.get("id3") + "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void initDetail(String url, String shop_id, String id3) throws ParserException, IOException {
		URL url2 = new URL(url + id3);
		System.out.print(url+id3);
		TagNode tagNode;
		NodeList list = matchAll(url2, "div class=\"list-wrap\"");
		Map<String, Object> catMap = new HashMap<String, Object>();

		for (int i = 0; i < list.size(); i++) {
			tagNode = new TagNode();
			Node node = list.elementAt(i);
			tagNode.setText(node.toHtml());

			if ("list-wrap".equals(tagNode.getAttribute("class"))) {// 所有分类

				Node elementAt = matchAll(node, "span class=\"title").elementAt(0);
				if (elementAt == null){
					System.out.println("no info , end");
					continue;
				}
				String cat = elementAt.toPlainTextString();

				catMap = new HashMap<String, Object>();
				catMap.put("text", cat);
				catMap.put("shop_id", shop_id);
				template.update("INSERT INTO t_stuff_cat (id,text,shop_id) " + "VALUES (next value for MYCATSEQ_GLOBAL,:text,:shop_id)", catMap);
				int staff_cat_id = template.queryForInt(
						"select id from t_stuff_cat where text = :text and shop_id = :shop_id limit 1", catMap);

//				System.out.print(cat);

				// List<Map<String, Object>> staffList = new
				// ArrayList<Map<String, Object>>();

				Map<String, Object> staff = new HashMap<String, Object>();

				SimpleNodeIterator itemList = matchAll(node, "li class=\"list-item\"").elements();

				while (itemList.hasMoreNodes()) {

					staff = new HashMap<String, Object>();

					Node nextNode = itemList.nextNode();
//					System.out.println(nextNode.getText());
					String item_data = getNodeAttr(nextNode, "data");

					String item_data_sid = getNodeAttr(nextNode, "data-sid");

					String item_img = getNodeAttr(matchAll(nextNode, "div class=\"bg-img\"").elementAt(0), "style");

					Node elementAt2 = matchAll(nextNode, "h3 ").elementAt(0);
					String item_title = elementAt2.toPlainTextString();
					String item_desc = getNodeAttr(elementAt2, "data-content");

					String item_price;
					try {
						item_price = matchAll(nextNode, "div class=\"m-price\"").elementAt(0).toPlainTextString()
								.replace("&#165;", "").replace("\n", "");
					} catch (Exception e) {
						item_price = "-";
					}

					String item_recommend = matchAll(nextNode, "span class=\"sales-count\"").elementAt(0)
							.toPlainTextString();
					String item_salecount = matchAll(nextNode, "span class=\"sales-count\"").elementAt(1)
							.toPlainTextString();

					staff.put("name", item_title);
					staff.put("shop_id", shop_id);
					staff.put("cat", cat);
					staff.put("cat_id", staff_cat_id);
					staff.put("data", item_data);
					staff.put("id3", item_data_sid.replace("item_", ""));
					staff.put("money2", item_price);
					staff.put("note", item_desc);
					staff.put("sales_count", item_salecount);
					staff.put("recommend", item_recommend);
					staff.put("photos", item_img);
//					System.out.println(staff);
					template.update(
							"INSERT INTO t_stuff (id,cat_id ,shop_id,name, money2, id3 , note, photos, sales_count,recommend) "
									+ "VALUES (next value for MYCATSEQ_GLOBAL,:cat_id,:shop_id,:name, :money2, :id3 , :note, :photos,:sales_count,:recommend)",
							staff);
				}
				

			}

			// if ("b-info fl".equals(tagNode.getAttribute("class"))) {
			// NodeList list2 =
			// tagNode.getChildren().elementAt(5).getChildren();
			// System.out.println(list2.elementAt(1).toHtml());
			// }
		}
		System.out.println("end");
	}

	public static String getNodeAttr(Node node, String attr) {
		TagNode tagNode = new TagNode();
		if (node == null)
			return "";
		tagNode.setText(node.toHtml());
		return tagNode.getAttribute(attr);
	}

	private static NodeList matchAll(URL url2, final String match) throws ParserException, IOException {
		Parser parser = new Parser((HttpURLConnection) url2.openConnection());
		parser.setEncoding("UTF-8");
		TagNode tagNode = null;
		// 得到所有经过过滤的标签
		NodeList list = parser.extractAllNodesThatMatch(new NodeFilter() {
			public boolean accept(Node node) {
				if (node.getText().startsWith(match)) {
					return true;
				} else {
					return false;
				}
			}
		});
		return list;
	}

	private static NodeList getNodeListByMatchStarted(Node elementAt, final String match) {
		NodeList list2 = elementAt.getChildren().extractAllNodesThatMatch(new NodeFilter() {
			public boolean accept(Node arg0) {
				if (arg0.getText().startsWith(match)) {
					return true;
				} else {
					return false;
				}
			}
		});
		return list2;
	}

	@SuppressWarnings("serial")
	public static NodeList matchAll(String html, final String match) throws ParserException {
		return new Parser(html).extractAllNodesThatMatch(new NodeFilter() {
			public boolean accept(Node arg0) {
				if (arg0.getText().startsWith(match)) {
					return true;
				} else {
					return false;
				}
			}
		});
	}

	@SuppressWarnings("serial")
	public static NodeList matchAll(Node node, final String match) throws ParserException {
		return new Parser(node.toHtml()).extractAllNodesThatMatch(new NodeFilter() {
			public boolean accept(Node arg0) {
				if (arg0.getText().startsWith(match)) {
					return true;
				} else {
					return false;
				}
			}
		});
	}

	public static void initUser() {
		HashMap<String, Object> args = new HashMap<String, Object>();
		Map<String, Object> address = new HashMap<String, Object>();
		long now = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {// 15824 33410
			address = RandomValue.getAddress();
			args = new HashMap<String, Object>();
			args.put("name", address.get("name"));
			args.put("phone", address.get("tel"));
			args.put("sex", address.get("sex"));
			args.put("email", address.get("email"));
			if (i % 1000 == 0) {
				System.out.println(i + " cost :" + (System.currentTimeMillis() - now));
			}
			template.update("INSERT INTO t_user (id, name, phone, email, sex) VALUES (next value for MYCATSEQ_GLOBAL, :name, :phone, :email , :sex)", args);
		}
		System.out.println(System.currentTimeMillis() - now);
	}

	private static void initShop() throws ClientProtocolException, IOException, JSONException {
		HashMap<String, Object> args = new HashMap<String, Object>();
		Map<String, Object> address = new HashMap<String, Object>();
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();

		int page = 1;
		for (int a = 1; a <= 12; a++) {// 2016年03月26日13:18:24 此时 百度只有12页数据
			page = a;
			HttpGet httpGet = new HttpGet(
					"http://waimai.baidu.com/waimai/shoplist/00a2bee7926a2f09?display=json&page=" + page + "&count=40");
			System.out.println(httpGet.getRequestLine());
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
				for (int i = 0; i < jsonArray.length(); i++) {
					System.out.print(jsonArray.getJSONObject(i).get("shop_name"));
					System.out.print(jsonArray.getJSONObject(i).get("shop_announcement"));
					System.out.print(jsonArray.getJSONObject(i).get("logo_url"));
					System.out.print(" id:" + jsonArray.getJSONObject(i).get("shop_id"));
					System.out.print(" source:" + jsonArray.getJSONObject(i).get("source_name"));
					System.out.print(jsonArray.getJSONObject(i).get("shop_lng"));
					System.out.print(jsonArray.getJSONObject(i).get("shop_lat"));
					System.out.print(jsonArray.getJSONObject(i).get("category"));
					System.out.println(jsonArray.getJSONObject(i).get("is_online"));
					address = RandomValue.getAddress();
					args = new HashMap<String, Object>();
					args.put("id3", jsonArray.getJSONObject(i).get("shop_id"));
					args.put("name", address.get("name"));
					args.put("phone", address.get("tel"));
					args.put("email", address.get("email"));
					args.put("address", address.get("road"));
					args.put("nickname", jsonArray.getJSONObject(i).get("shop_name"));
					args.put("note", jsonArray.getJSONObject(i).get("shop_announcement"));
					args.put("lng", jsonArray.getJSONObject(i).get("shop_lng"));
					args.put("lat", jsonArray.getJSONObject(i).get("shop_lat"));
					args.put("source", jsonArray.getJSONObject(i).get("source_name"));
					template.update(
							"INSERT INTO t_shop (id,id3,name, phone, email,nickname,address,note,lng,lat,source) "
									+ "VALUES (next value for MYCATSEQ_GLOBAL,:id3,:name, :phone, :email, :nickname, :address, :note,:lng, :lat, :source)",
							args);
				}
			}
		}

	}

}
