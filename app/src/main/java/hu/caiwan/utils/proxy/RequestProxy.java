package hu.caiwan.utils.proxy;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

/**
 * Forwards an HTTP request towards a given client 
 * @author caiwan
 *
 */
public class RequestProxy {

	private static final Logger LOGGER = LoggerFactory.getLogger(RequestProxy.class);

	private static final String URL_PARAMETER_ENCODING = "UTF-8";

	private final URI serverURI;
	private final CloseableHttpClient httpClient;
	private final RewriteRules rules;

	/**
	 * 
	 * @param serverURI base url of the remote server we send the request towards  
	 * @param httpClient current HTTP client @see HttpClients
	 * @param rules instance of a filtering rule class @see RequestProxy
	 */
	public RequestProxy(URI serverURI, CloseableHttpClient httpClient, RewriteRules rules) {
		super();
		this.serverURI = serverURI;
		this.httpClient = httpClient;
		this.rules = rules;

		//
		// TODO Test server connection
		// HttpPost httppost = new HttpPost(serverURI);
		// Map <String, String> params = new HashMap<>();
		// params.put("", "")
		// httppost.setEntity(new UrlEncodedFormEntity());
	}

	/**
	 * Renders the incoming HTTP request to fill it with the remote servers response base address
	 * @param servletRequest @see HttpServletResponse
	 * @param servletResponse @see HttpServletRequest
	 * @param method @see HttpMethod
	 * @throws Exception
	 */
	public void render(HttpServletRequest servletRequest, HttpServletResponse servletResponse, HttpMethod method)
			throws Exception {
		render(servletRequest, servletResponse, this.serverURI, method);
	}

	/**
	 * Renders the incoming HTTP request to fill it with the remote servers response in relative adess of the remote base address 
	 * @param servletRequest incoming request @see HttpServletRequest
	 * @param servletResponse response we @see HttpServletResponse
	 * @param endpoint realtive endpoint to the server base address 
	 * @param method @see HttpMethod
	 * @throws Exception
	 */
	public void render(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String endpoint,
			HttpMethod method) throws Exception {
		String baseURI = "http://" + this.serverURI.getHost() + ":" + this.serverURI.getPort() + "/" + endpoint;
		LOGGER.info("Redirecting request: " + baseURI);
		render(servletRequest, servletResponse, new URI(baseURI), method);
	}

	/**
	 * Renders the incoming HTTP request to fill it with the remote servers response
	 * @param servletRequest incoming request @see HttpServletRequest
	 * @param servletResponse response we @see HttpServletResponse
	 * @param uri full remote uri we want to porxy the request to
	 * @param method incoming HTTP method @see HttpMethod
	 * @throws Exception
	 */

	private void render(HttpServletRequest servletRequest, HttpServletResponse servletResponse, URI uri,
			HttpMethod method) throws Exception {
		HttpRequestBase httpRequest;
		Map<String, String[]> params = this.rules.getServletRequestParams(servletRequest);

		if (method == HttpMethod.GET) {
			httpRequest = new HttpGet(uri);
		} else if (method == HttpMethod.POST) {
			final HttpPost httpPost = new HttpPost(uri);
			httpRequest = httpPost;
			httpPost.setEntity(this.rules.getQueryFromMap(params, URL_PARAMETER_ENCODING));
		} else {
			throw new NullPointerException("Invalid request type " + method);
		}

		try (CloseableHttpResponse httpresponse = httpClient.execute(httpRequest)) {
			HttpEntity entity = httpresponse.getEntity();
			String contentType = "application/octet-stream";
			if (entity.getContentType() != null)
				contentType = entity.getContentType().getValue();

			LOGGER.info("Req type: " + contentType);

			servletResponse.setContentType(contentType);
			copyAndClose(entity, servletResponse);
		} finally {
			// .. do some clenaup if needed
		}
	}

	/** -- non api functions --- */
	/**
	 * Copies the remote response to the incoming request handler 
	 * @param in
	 * @param response
	 * @throws IOException
	 */
	private static void copyAndClose(HttpEntity in, HttpServletResponse response) throws IOException {
		try (ServletOutputStream out = response.getOutputStream()) {
			in.writeTo(out);
		}
	}

}
