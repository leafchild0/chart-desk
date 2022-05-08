package chart.desk.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * On any fetching exception
 *
 * @author vmalyshev
 */
public class FetchingException extends ResponseStatusException {

	public FetchingException(HttpStatus status, String reason) {

		super(status, reason);
	}
}
