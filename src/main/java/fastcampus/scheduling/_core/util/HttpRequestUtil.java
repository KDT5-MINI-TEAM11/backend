package fastcampus.scheduling._core.util;

import static fastcampus.scheduling._core.errors.ErrorMessage.INNER_SERVER_ERROR;

import fastcampus.scheduling._core.errors.exception.Exception500;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;

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

	public static String getClientIpAddressIfServletRequestExist(HttpServletRequest request) {

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