package chart.desk.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * On any parse exception
 *
 * @author vmalyshev
 */
public class ParseException extends ResponseStatusException {

	public ParseException(HttpStatus status, String reason) {

		super(status, reason);
	}
}
