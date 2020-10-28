package com.realEstateCrawling.service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

public interface CrawlingService {
	 int crawl(String site, String region) throws ClientProtocolException, URISyntaxException, IOException, InterruptedException;
}
