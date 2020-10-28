package com.realEstateCrawling.crawler.impl;

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
import com.realEstateCrawling.crawler.Crawler;
import com.realEstateCrawling.crawler.RegionCoordinate;
import com.realEstateCrawling.vo.RealEstate;

public class Zigbang implements Crawler {

	@Override
	public List<RealEstate> crawl(RegionCoordinate region)
			throws URISyntaxException, ClientProtocolException, IOException, InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Double>> makeCoordinates(RegionCoordinate region) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<URI> makeIdURIs(List<Map<String, Double>> coordinates) throws URISyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> crawlRealEstateIds(List<URI> uris) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> parseIds(String content) throws JsonMappingException, JsonProcessingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<URI> makeRealEstateURIs(Set<String> ids) throws URISyntaxException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RealEstate> crawlRealEstates(List<URI> urls) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RealEstate parseRealEstate(String content) throws JsonMappingException, JsonProcessingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpRequestBase setRequestOptions(HttpRequestBase request) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	

}
