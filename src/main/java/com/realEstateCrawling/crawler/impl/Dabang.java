package com.realEstateCrawling.crawler.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realEstateCrawling.crawler.Crawler;
import com.realEstateCrawling.crawler.RegionCoordinate;
import com.realEstateCrawling.vo.RealEstate;

import lombok.Getter;
import lombok.ToString;

public class Dabang implements Crawler {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final static String SITE = "dabang";
	private final static String BASE_ID_API_URI = "https://www.dabangapp.com/api/3/room/list/multi-room/bbox";
	private final static String BASE_REAL_ESTATE_API_URI = "https://www.dabangapp.com/api/3/room/detail2";
	private final static String BASE_REAL_ESTATE_URI = "https://www.dabangapp.com/room";

	//파라미터 정보
	private final static String API_VERSION = "3.0.1";
	private final static String CALL_TYPE = "web";
	private final static String FILTERTS = "{\"multi_room_type\":[0,1,2],\"selling_type\":[0,1,2],\"deposit_range\":[0,999999],\"price_range\":[0,999999],\"trade_range\":[0,999999],\"maintenance_cost_range\":[0,999999],\"room_size\":[0,999999],\"supply_space_range\":[0,999999],\"room_floor_multi\":[1,2,3,4,5,6,7,-1,0],\"division\":false,\"duplex\":false,\"room_type\":[1,2],\"enter_date_range\":[0,999999],\"parking_average_range\":[0,999999],\"household_num_range\":[0,999999],\"parking\":false,\"animal\":false,\"short_lease\":false,\"full_option\":false,\"built_in\":false,\"elevator\":false,\"balcony\":false,\"loan\":false,\"safety\":false,\"pano\":false,\"deal_type\":[0,1]}";
	private final static String PAGE = "1";
	private final static String USE_MAP = "kakao";
	private final static String VERSION = "1";
	private final static String ZOOM = "9";
	
	@Override
	public List<RealEstate> crawl(RegionCoordinate region) throws URISyntaxException, ClientProtocolException, IOException, InterruptedException {
		//1.크롤링할 좌표정보를 만든다.
		List<Map<String, Double>> coordinates = makeCoordinates(region);
		//2.ID를 크롤링할 URL을 만든다.
		List<URI> idURIs = makeIdURIs(coordinates);
		//3.부동산 ID를 크롤링한다.
		Set<String> ids = crawlRealEstateIds(idURIs);
		//4.부동산을 크롤링할 URL을 만든다.
		List<URI> realEstateURIs = makeRealEstateURIs(ids); 
		//5.부동산 ID를 이용하여 개별 정보를 크롤링한다.
		List<RealEstate> realEstates = crawlRealEstates(realEstateURIs);
		logger.debug("크롤링 할 ID cnt[{}] 크롤링 할 부동산 URI cnt[{}] 크롤링 한 부동산 cnt[{}]", ids.size(), realEstateURIs.size(), realEstates.size());
		
		return realEstates;
	}
	
	@Override
	public List<Map<String, Double>> makeCoordinates(RegionCoordinate region){
		List<Map<String, Double>> retCoordinates = new ArrayList<Map<String, Double>>();

		List<Double> latitudes = region.makeLatitudes();
		List<Double> longitudes =  region.makeLongitudes();
		for(int i=1; i<latitudes.size(); i++) {
			for(int j=1; j<longitudes.size(); j++) {
				Map<String, Double> map = new HashMap<String, Double>();
				map.put("left", latitudes.get(i-1));
				map.put("bottom", longitudes.get(j-1));
				map.put("right", latitudes.get(i));
				map.put("top", longitudes.get(j));
				retCoordinates.add(map);
			}
		}
		
		return retCoordinates;
	}
	
	@Override
	public List<URI> makeIdURIs(List<Map<String, Double>> coordinates) throws URISyntaxException{
		List<URI> idURIs = new ArrayList<>();
		
		for(Map<String,Double> coordinate : coordinates) {
			URI uri = new URI(BASE_ID_API_URI);
			String location = makeLocationStr(coordinate);
			uri = new URIBuilder(uri)
					.addParameter("api_version", API_VERSION)
					.addParameter("call_type", CALL_TYPE)
					.addParameter("filters", FILTERTS)
					.addParameter("location", location)
					.addParameter("page", PAGE)
					.addParameter("use_map", USE_MAP)
					.addParameter("version", VERSION)
					.addParameter("zoom", ZOOM)
					.build();
			idURIs.add(uri);
		}
		return idURIs;
	}
	
	private String makeLocationStr(Map<String, Double> coordinate) {
		StringBuffer locationStr = new StringBuffer();
		locationStr
			.append("[[")
			.append(coordinate.get("bottom"))
			.append(",")
			.append(coordinate.get("left"))
			.append("],[")
			.append(coordinate.get("top"))
			.append(",")
			.append(coordinate.get("right"))
			.append("]]");
		return locationStr.toString();
	}
	
	@Override
	public Set<String> crawlRealEstateIds(List<URI> uris) {
		Set<String> ids = new LinkedHashSet<>();
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet();
		setRequestOptions(httpGet);
		HttpResponse response;
		HttpEntity entity;
		String content;
		
		for(URI uri : uris) {
			/**
			 * getNextPage() 이전에 에러 발생할 경우 무한 루프 발생 가능함.
			 * getNextPage() 이후 새로운 page에 대한 URI 설정시 에러 발생할 경우 무한 루프 발생 가능함.
			 * errorCnt를 추가하여 동일한 uri에 대하여 5번 넘게 에러 발생할 경우 해당 uri는 통과함. 
			 */
			int errorCnt = 0;
			while(true) {
				try {
					if(errorCnt > 5) { break; }
					httpGet.setURI(uri);
					response = httpClient.execute(httpGet);
					entity = response.getEntity();
					content = EntityUtils.toString(entity);
					ids.addAll(parseIds(content));
					Integer nextPage = getNextPage(content);
					if(nextPage == 0) { break; }
					
					uri = new URIBuilder(uri).setParameter("page", nextPage.toString()).build();
					errorCnt=0;
				}catch(Exception e) {
					errorCnt++;
					logger.debug("errorCnt[{}] uri[{}] crawlRealEstateIds error[{}]", errorCnt, uri.toString(), e);
				}
			}
		}
		return ids;
	}
	
	public List<String> parseIds(String content) throws JsonMappingException, JsonProcessingException{
		List<String> ids = new ArrayList<>();
		
		ObjectMapper mapper = new ObjectMapper();
		Map<?,?> contentMap = mapper.readValue(content, Map.class);
		//TODO rooms == null 일 경우 예외 처리 해야할 듯.
		@SuppressWarnings("unchecked")
		List<Map<?,?>> rooms = (List<Map<?,?>>) contentMap.get("rooms");
		String id;
		for(Map<?,?> room : rooms) {
			id = (String) room.get("id");
			ids.add(id);
		}
		return ids;
	}
	
	/**
	 * next_page를 반환하며 next_page가 없을 경우 0을 반환한다.
	 */
	private Integer getNextPage(String content) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		//content가 json 형태가 아니면 예외 발생함.
		Map<?, ?> map = mapper.readValue(content, Map.class);
		Integer nextPage = (Integer) map.get("next_page");
		
		if(nextPage == null) {
			nextPage = 0;
		}
		return nextPage;
	}
	
	@Override
	public List<URI> makeRealEstateURIs(Set<String> ids) throws URISyntaxException{
		List<URI> realEstateURIs = new ArrayList<>();
			
		for(String id : ids) {
			URI uri = new URI(BASE_REAL_ESTATE_API_URI);
			uri = new URIBuilder(uri)
					.addParameter("api_version", API_VERSION)
					.addParameter("call_type", CALL_TYPE)
					.addParameter("room_id", id)
					.addParameter("use_map", USE_MAP)
					.addParameter("version", VERSION)
					.build();
			realEstateURIs.add(uri);
		}
		return realEstateURIs;
	}

	@Override
	public List<RealEstate> crawlRealEstates(List<URI> uris) {
		List<RealEstate> realEstates = new ArrayList<>();
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet();
		setRequestOptions(httpGet);
		HttpResponse response;
		HttpEntity entity;
		String content;
		
		RealEstate realEstate;
		int cnt = 0;
		for(URI uri : uris) {
			try {
				cnt++;
				httpGet.setURI(uri);
				response = httpClient.execute(httpGet);
				entity = response.getEntity();
				content = EntityUtils.toString(entity);
				realEstate = parseRealEstate(content);
				realEstate.getPK().setSite(SITE);
				realEstate.setUrl(BASE_REAL_ESTATE_URI+"/"+realEstate.getPK().getId());
				realEstates.add(realEstate);
				if(cnt%100 == 0) {
					logger.debug("crawlRealEstates 처리 중 cnt[{}]", cnt);
				}
			} catch(Exception e) {
				logger.debug("uri[{}] crawlRealEstates error[{}]",uri.toString(), e);
			}
		}
		return realEstates;
	}
		
	
	@Override
	public RealEstate parseRealEstate(String content) throws JsonMappingException, JsonProcessingException {
		RealEstate realEstate = new RealEstate();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readValue(content, JsonNode.class);
		String room = node.get("room").toString();
		ResponsedDbang rDabang = mapper.readValue(room, ResponsedDbang.class);
		realEstate = rDabang.toRealEstate();
		return realEstate;
	}
	
	@Override
	public HttpRequestBase setRequestOptions(HttpRequestBase request) {
		request.setHeader(HttpHeaders.ACCEPT, "application/json, text/plain, */*");
		request.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");
		request.setHeader(HttpHeaders.ACCEPT_LANGUAGE, "ko-KR,ko;q=0.9,en;q=0.8,en-US;q=0.7");
		request.setHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36");
		
		RequestConfig config = RequestConfig.custom()
								.setSocketTimeout(10*1000)
								.setConnectTimeout(10*1000)
								.setConnectionRequestTimeout(10*1000)
								.build();
		request.setConfig(config);
		
		return request;
	}
	
}

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
class ResponsedDbang{
	private String id;
	
	private String address;
	private String full_jibun_address2_str;
	private String full_road_address2_str;
	private List<String> location;
	
	private List<List<String>> price_info;
	private String maintenance_cost_str;
	
	private List<String> building_use_types_str;
	private String room_type_main_str;
	private String room_size;
	private String building_approval_date_str;
	private String beds_num;
	private String bath_num;
	
	private String room_floor_str;
	private String building_floor_str;
	private String elevator_str;
	private String parking_str;
	private String animal_str;
	private String direction_str;
	private List<Map<String,String>> room_options;
	
	public RealEstate toRealEstate() {
		RealEstate e = new RealEstate();
		e.getPK().setId(id);
		
		e.setAddress(address);
		e.setJibunAddress(full_jibun_address2_str);
		e.setRoadAddress(full_road_address2_str);
		e.setLongitude(location.get(0));
		e.setLatitude(location.get(1));
		
		List<String> priceInfo = price_info.get(0);
		switch(priceInfo.get(2)) {
		case "0":
			e.setSaleType("월세");
			e.setCharterCost(priceInfo.get(0));	//보증금	
			e.setMonthlyRent(priceInfo.get(1));	//월세
			break;
		case "1":
			e.setSaleType("전세");
			e.setCharterCost(priceInfo.get(0));	//전세
			e.setMonthlyRent(priceInfo.get(1));	//반전세?
			break;
		case "2":
			e.setSaleType("매매");
			e.setTradingCost(priceInfo.get(0));	//매매금
		}
		e.setMaintanenceCost(maintenance_cost_str);
		
		StringBuffer buildingType = new StringBuffer();
		building_use_types_str.forEach(i -> e.setBuildingType(buildingType.append(i).append(";").toString()));
		e.setRoomType(room_type_main_str);
		e.setExclusiveArea(room_size);
		e.setApprovalDate(building_approval_date_str);
		e.setRoomCount(beds_num);
		e.setBathRoomCount(bath_num);
		
		e.setFloor(room_floor_str);
		e.setBuildingFloor(building_floor_str);
		e.setIsAvailableElevator(elevator_str);
		e.setIsAvailableParking(parking_str);
		e.setIsAvailablePet(animal_str);
		e.setDirection(direction_str);
		StringBuffer option = new StringBuffer();
		if(room_options != null) {
			room_options.forEach(map -> option.append(map.get("name")).append(";"));
		}
		e.setOptions(option.toString());
		
		return e;
	}
}
