package hu.caiwan.shop.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;

/**
 * Profile-dependent properties
 * http://stackoverflow.com/questions/12691812/can-propertysources-be-chosen-by-
 * spring-profile
 * 
 * @author caiwan
 */

@Configuration
@PropertySource("classpath:application.properties")
public class GlobalConfiguration {

	private static Logger LOGGER = LoggerFactory.getLogger("GlobalConfig");
	
	@Configuration
	@Profile("dev")
	@PropertySources({ @PropertySource("file:${hu.caiwan.shop.root}/conf/eo.application.dev.properties") })
	public static class Development {
		// nothing needed here
	}

	@Configuration
	@Profile("live")
	@PropertySources({ @PropertySource("file:${hu.caiwan.shop.root}/conf/eo.application.properties") })
	public static class Live {
		// nothing needed here
	}

	@Autowired
	private Environment env;

	@SuppressWarnings("rawtypes")
	public Map<String, Object> listProperties() {
		Map<String, Object> map = new HashMap<>();
		for (Iterator it = ((AbstractEnvironment) env).getPropertySources().iterator(); it.hasNext();) {
			Object ps = it.next();
			if (ps instanceof MapPropertySource) {
				map.putAll(((MapPropertySource) ps).getSource());
			}
		}

		Set<Entry<String, Object>> entries = map.entrySet();
		for (Entry e : entries) {
			LOGGER.info("key: " + e.getKey() + "Value: " + e.getValue());
		}

		return map;
	}

}
