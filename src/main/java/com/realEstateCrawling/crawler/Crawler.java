package com.realEstateCrawling.crawler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpRequestBase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.realEstateCrawling.vo.RealEstate;

public interface Crawler {
	
	List<RealEstate> crawl(RegionCoordinate region) throws URISyntaxException, ClientProtocolException, IOException, InterruptedException;
	
	/**
	 * 크롤링할 좌표정보를 만든다.
	 */
	List<Map<String, Double>> makeCoordinates(RegionCoordinate region);
	
	/**
	 * ID를 크롤링할 URI를 만든다.
	 */
	List<URI> makeIdURIs(List<Map<String, Double>> coordinates) throws URISyntaxException;
	
	/**
	 * 부동산 ID를 크롤링한다.
	 */
	Set<String> crawlRealEstateIds(List<URI> uris);
	
	/**
	 * 응답 데이터에서 ID를 파싱한다.
	 */
	List<String> parseIds(String content) throws JsonMappingException, JsonProcessingException;
	
	/**
	 * 부동산을 크롤링할 URI를 만든다.
	 */
	List<URI> makeRealEstateURIs(Set<String> ids) throws URISyntaxException;
	
	/**
	 * 부동산 ID를 이용하여 개별 정보를 크롤링한다.
	 */
	List<RealEstate> crawlRealEstates(List<URI> urls);
	
	/**
	 * 응답 데이터에서 부동산 정보를 파싱한다.
	 */
	RealEstate parseRealEstate(String content) throws JsonMappingException, JsonProcessingException;
	
	/**
	 * httpRequest 옵션을 설정한다.
	 */
	HttpRequestBase setRequestOptions (HttpRequestBase request);
	
}
