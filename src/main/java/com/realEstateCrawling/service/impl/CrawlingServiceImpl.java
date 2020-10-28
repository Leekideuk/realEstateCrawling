package com.realEstateCrawling.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realEstateCrawling.crawler.Crawler;
import com.realEstateCrawling.crawler.RegionCoordinate;
import com.realEstateCrawling.crawler.impl.Dabang;
import com.realEstateCrawling.repository.RealEstateRepository;
import com.realEstateCrawling.service.CrawlingService;
import com.realEstateCrawling.vo.RealEstate;

@Service
public class CrawlingServiceImpl implements CrawlingService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	RealEstateRepository realEstateRepo;
	
	public int crawl(String site, String region) throws ClientProtocolException, URISyntaxException, IOException, InterruptedException {
		Crawler crawler = selectCrawler(site);
		RegionCoordinate regionCoordinate = selectRegionCoordinate(region);
		
		logger.debug("site[{}] region[{}] 크롤링 시작", site, region);
		List<RealEstate> realEstates = crawler.crawl(regionCoordinate);
		int cnt = saveRealEstates(realEstates);
		logger.debug("크롤링 DB saveAll cnt[{}]", cnt);
		
		return cnt;
	}
	
	private Crawler selectCrawler(String site) {
		switch(site) {
		case "dabang":
			return new Dabang();
		case "zigbnag":
		default:
			logger.debug("site 없음");
			throw new IllegalArgumentException("site 없음");
		}
	}
	
	private RegionCoordinate selectRegionCoordinate(String region) {
		switch(region) {
		case "NWN":
			return RegionCoordinate.NWN;
		case "NWS":
			return RegionCoordinate.NWS;
		case "NEN":
			return RegionCoordinate.NEN;
		case "NES":
			return RegionCoordinate.NES;
		case "SWN":
			return RegionCoordinate.SWN;
		case "SWS":
			return RegionCoordinate.SWS;
		case "SEN":
			return RegionCoordinate.SEN;
		case "SES":
			return RegionCoordinate.SES;
		default:
			logger.debug("region 없음");
			throw new IllegalArgumentException("region 없음");
		}
	}
	
	private int saveRealEstates(List<RealEstate> realEstates) {
		return realEstateRepo.saveAll(realEstates).size();
	}
}
