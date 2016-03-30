package cn.com.oceancode.test;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class DbTest {

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

	public static void main(String[] args2) {

		initUser();
		initShop();
		
	}

	private static void initUser() {
		HashMap<String, Object> args = new HashMap<String, Object>();
		Map<String, Object> address = new HashMap<String, Object>();
		for (int i = 0; i < 1000; i++) {
			address = RandomValue.getAddress();
			args = new HashMap<String, Object>();
			args.put("name", address.get("name"));
			args.put("phone", address.get("tel"));
			args.put("email", address.get("email"));
			System.out.println(address);
			template.update("INSERT INTO t_user (name, phone, email) " + "VALUES (:name, :phone, :email)", args);
		}
	}
	
	private static void initShop() {
		HashMap<String, Object> args = new HashMap<String, Object>();
		Map<String, Object> address = new HashMap<String, Object>();
		for (int i = 0; i < 199; i++) {
			address = RandomValue.getAddress();
			args = new HashMap<String, Object>();
			args.put("name", address.get("name"));
			args.put("phone", address.get("tel"));
			args.put("email", address.get("email"));
			args.put("address", address.get("road"));
			args.put("nickname", address.get("name")+"小店");
			System.out.println(address);
			template.update("INSERT INTO t_shop (name, phone, email,nickname,address) " + "VALUES (:name, :phone, :email,:nickname,:address)", args);
		}
	}

}
