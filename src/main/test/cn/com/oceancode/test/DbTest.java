package cn.com.oceancode.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class DbTest {

	public static void main(String[] args2) {
		GenericXmlApplicationContext context = new GenericXmlApplicationContext();  
        context.setValidating(false);  
        context.load("classpath*:applicationContext*.xml");  
        context.refresh();  
		JdbcTemplate jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate_mysql");
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		Map<String, Object> args = new HashMap<String, Object>();
		List<Map<String, Object>> queryForList = template
				.queryForList("select * from t_code", args);
	}
}
