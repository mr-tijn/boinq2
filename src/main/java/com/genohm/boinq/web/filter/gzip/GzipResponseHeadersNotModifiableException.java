package com.genohm.boinq.web.filter.gzip;

import javax.servlet.ServletException;

public class GzipResponseHeadersNotModifiableException extends ServletException {

	private static final long serialVersionUID = -5872854554423574437L;

	public GzipResponseHeadersNotModifiableException(String message) {
        super(message);
    }
}
