package fastcampus.scheduling._core.util;

import static fastcampus.scheduling._core.errors.ErrorMessage.INNER_SERVER_ERROR;

import fastcampus.scheduling._core.errors.exception.Exception500;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class HttpRequestUtil {

	private static final String[] IP_HEADER_CANDIDATES = {
			"X-Forwarded-For",
			"Proxy-Client-IP",
			"WL-Proxy-Client-IP",
			"HTTP_X_FORWARDED_FOR",
			"HTTP_X_FORWARDED",
			"HTTP_X_CLUSTER_CLIENT_IP",
			"HTTP_CLIENT_IP",
			"HTTP_FORWARDED_FOR",
			"HTTP_FORWARDED",
			"HTTP_VIA",
			"REMOTE_ADDR"
	};

	public static Map<String, String> getAccessInfo() {

		request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String ip = getClientIpAddressIfServletRequestExist(request);

		String agent = request.getHeader("USER-AGENT");
		String browser = getClientBrowser(agent);

		Map<String, String> accessInfo = new HashMap<>();
		accessInfo.put("ip", ip);
		accessInfo.put("browser", browser);
		return accessInfo;
	}

	private static String getClientBrowser(String userAgent) {
		String browser = "";

		if (userAgent.indexOf("Trident/7.0") > -1) {
			browser = "ie11";
		}
		else if (userAgent.indexOf("MSIE 10") > -1) {
			browser = "ie10";
		}
		else if (userAgent.indexOf("MSIE 9") > -1) {
			browser = "ie9";
		}
		else if (userAgent.indexOf("MSIE 8") > -1) {
			browser = "ie8";
		}
		else if (userAgent.indexOf("Chrome/") > -1) {
			browser = "Chrome";
		}
		else if (userAgent.indexOf("Chrome/") == -1 && userAgent.indexOf("Safari/") >= -1) {
			browser = "Safari";
		}
		else if (userAgent.indexOf("Firefox/") >= -1) {
			browser = "Firefox";
		}
		else {
			browser ="Other";
		}
		return browser;
	}


	private static String getClientIpAddressIfServletRequestExist(HttpServletRequest request) {

		if (Objects.isNull(RequestContextHolder.getRequestAttributes())) {
			return "0.0.0.0";
		}

		try {
			for (String header : IP_HEADER_CANDIDATES) {
				String ipFromHeader = request.getHeader(header);
				if (Objects.nonNull(ipFromHeader) && !ipFromHeader.isEmpty() && !"unknown".equalsIgnoreCase(
						ipFromHeader)) {
					return ipFromHeader.split(",")[0];
				}
			}
			return request.getRemoteAddr();
		} catch (Exception exception) {
			throw new Exception500(INNER_SERVER_ERROR);
		}

	}
}