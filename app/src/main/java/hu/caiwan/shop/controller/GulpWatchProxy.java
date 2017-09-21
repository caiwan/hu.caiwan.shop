package hu.caiwan.shop.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

import hu.caiwan.utils.proxy.BasicRewriteRules;
import hu.caiwan.utils.proxy.RequestProxy;
import hu.caiwan.utils.proxy.RewriteRules;

/**
 * A proxy controller that bounces every HTTP request to the running GULP task
 * at port 3000. During the npm development gulp will serve the static content
 * instead of packing it into a jar.
 * 
 * @author caiwan
 *
 */

@Controller
@RequestMapping("/web")
public class GulpWatchProxy {

	private RequestProxy watchProxy;

	private static final Logger LOGGER = LoggerFactory.getLogger(GulpWatchProxy.class);

	@PostConstruct
	public void setup() throws URISyntaxException {
		final RewriteRules rules = new BasicRewriteRules();
		this.watchProxy = new RequestProxy(new URI("http://localhost:3000/"), HttpClients.createDefault(), rules);
	}

	@RequestMapping("/**")
	public void proxy(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		HttpMethod method = HttpMethod.resolve(request.getMethod());
		
		Matcher matcher = Pattern.compile("web/(.*)").matcher(restOfTheUrl);
		
		if (matcher.find()){
			String url = matcher.group(1);
			LOGGER.info("Redirecting requerst: " + url);
			
			watchProxy.render(request, response, url, method);
		}
		


	}

}
