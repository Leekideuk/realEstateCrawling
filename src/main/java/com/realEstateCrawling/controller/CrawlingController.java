package com.realEstateCrawling.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.realEstateCrawling.service.CrawlingService;

@RestController
public class CrawlingController {
	
	@Autowired
	CrawlingService crawlingService;
	
	@RequestMapping(value="crawl", method=RequestMethod.POST)
	public String crawl(String site, String region) throws ClientProtocolException, URISyntaxException, IOException, InterruptedException {
		int cnt = crawlingService.crawl(site, region);
		
		return ""+cnt;
	}
}
