package com.genohm.boinq.web.servlet;

import java.net.MalformedURLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.servlets.ProxyServlet;

public class FusekiProxyServlet extends ProxyServlet {
	private String sourcePath;
	private String proxyHost;
	private int proxyPort;
	private String proxyPath;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		proxyHost = config.getInitParameter("proxyHost");
		proxyPort = Integer.parseInt(config.getInitParameter("proxyPort"));
		proxyPath = config.getInitParameter("proxyPath");
		sourcePath = config.getInitParameter("sourcePath");
	}
	
	@Override
	protected HttpURI proxyHttpURI(String scheme, String serverName, int serverPort, String uri) throws MalformedURLException {
		String newUri = proxyPath + stripSource(uri);
		return super.proxyHttpURI(scheme, proxyHost, proxyPort, newUri);
	}
	
	protected String stripSource(String uri) {
		return uri.replaceFirst(sourcePath, "");
	}
	
}
