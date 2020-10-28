package com.realEstateCrawling.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import com.realEstateCrawling.crawler.RegionCoordinate;
import com.realEstateCrawling.crawler.impl.Dabang;
import com.realEstateCrawling.service.CrawlingService;

@SpringBootTest
public class CrawlingServiceImplTests {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	CrawlingService crawlingService;
	
	@Test
	void crawlTest() {
		String site = "dabang";
		String region = "NWS";
		
		try {
			crawlingService.crawl(site, region);
			
		}catch(Exception e) {
			logger.debug("error -------------------------------------");
			logger.debug("{}",e);
		}
	}
	
	@Test
	void selectCrawlerTest(){
		assertEquals(true, ReflectionTestUtils.invokeMethod(new CrawlingServiceImpl(), "selectCrawler", "dabang").getClass().isInstance(new Dabang()));
		Assertions.assertThrows(IllegalArgumentException.class, () -> ReflectionTestUtils.invokeMethod(new CrawlingServiceImpl(), "selectCrawler", "aaa"));
	}
	
	@Test
	void selectRegion() {
		assertEquals(true, ReflectionTestUtils.invokeMethod(new CrawlingServiceImpl(), "selectRegionCoordinate", "NWN").getClass().isInstance(RegionCoordinate.NWN));
		Assertions.assertThrows(IllegalArgumentException.class, () -> ReflectionTestUtils.invokeMethod(new CrawlingServiceImpl(), "selectRegionCoordinate", "aaa"));
	}
}
