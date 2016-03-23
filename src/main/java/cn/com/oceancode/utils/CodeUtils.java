package cn.com.oceancode.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class CodeUtils {
	private static Map<String, Object> m;

	static{
		reload();
	}
	
	public static String getValue(String type, String key) {
		if (m == null) {
			reload();
		}
		return (String) m.get(type + key);
	}

	public static String reload() {
		m = new HashMap<String, Object>();
		JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringContextUtil.getBean("jdbcTemplate_pg");
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
		Map<String, Object> args = new HashMap<String, Object>();
		List<Map<String, Object>> list = template.queryForList("select type||key as key , value from crm_code ", args);
		for (Map<String, Object> map : list) {
			m.put((String) map.get("key"), map.get("value"));
		}
		return list.size()+"";
	}

}
