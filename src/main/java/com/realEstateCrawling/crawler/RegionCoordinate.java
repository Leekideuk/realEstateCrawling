package com.realEstateCrawling.crawler;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum RegionCoordinate {
	 KOREA(33.2, 38.6, 0.1, 125.9, 129.6, 0.1, 1)
	 
	 	/** 북서북*/
	, NWN(37.2, 38.6, 0.1, 125.9, 127.7, 0.1, 1)
		/**북서남*/
	, NWS(35.9, 37.2, 0.1, 125.9, 127.7, 0.1, 1)
		/**북동북*/
	, NEN(37.2, 38.6, 0.1, 127.7, 129.6, 0.1, 1)
		/**북동남*/
	, NES(35.9, 37.2, 0.1, 127.7, 129.6, 0.1, 1)
		/**남서북*/
	, SWN(34.5, 35.9, 0.1, 125.9, 127.7, 0.1, 1)
		/**남서남*/
	, SWS(33.2 ,34.5, 0.1, 125.9, 127.7, 0.1, 1)
		/**남동북*/
	, SEN(34.5, 35.9, 0.1, 127.7, 129.6, 0.1, 1)
		/**남동남*/
	, SES(33.2, 34.5, 0.1, 127.7, 129.6, 0.1, 1)
	
	, TEST_REGION_COORDINATE_1(37.1, 37.5, 0.1, 121.6, 130, 0.1, 1)
	, TEST_REGION_COORDINATE_2(37.1, 37.5, 0.2, 121.6, 130, 0.1, 2)
	, TEST_REGION_COORDINATE_3(-91, 91, 0.2, 121.6, 130, 0.1, 1)
	, TEST_REGION_COORDINATE_4(30, 31, 0.02, 121.6, 130, 0.1, 1)
	, TEST_REGION_COORDINATE_5(36.4, 36.8, 0.2, 127.2, 127.5, 0.05, 2)
	, TEST_DABANG(36.7, 36.8, 0.1, 127.2, 127.5, 0.05, 2);
	
	private double latLeft;
	private double latRight;
	private double latIncrement;
	private double longiBottom;
	private double longiTop;
	private double longiIncrement;
	private int decimalPlace;
	
	private RegionCoordinate(double latLeft, double latRight, double latIncrement, double longiBottom, double longiTop,
			double longiIncrement, int decimalPlace) {
		this.latLeft = latLeft;
		this.latRight = latRight;
		this.latIncrement = latIncrement;
		this.longiBottom = longiBottom;
		this.longiTop = longiTop;
		this.longiIncrement = longiIncrement;
		this.decimalPlace = decimalPlace;
	}
	
	/**
	 * 위도 목록을 만든다.
	 */
	public List<Double> makeLatitudes(){
		validateLatitude(latLeft, latRight);
		validateIncrement(latIncrement);
		return makeCoordinates(latLeft, latRight, latIncrement);
	}
	
	/**
	 * 경도 목록을 만든다.
	 */
	public List<Double> makeLongitudes(){
		validateLongitude(longiBottom, longiTop);
		validateIncrement(longiIncrement);
		return makeCoordinates(longiBottom, longiTop, longiIncrement);
	}
	
	private List<Double> makeCoordinates(double start, double end, double increment){
		List<Double> coordinates = new ArrayList<Double>();
		double coordinate = start;
		while(coordinate <= end) {
			coordinates.add(coordinate);
			coordinate += increment;
			coordinate = round(coordinate);
			
		}
		return coordinates;
	}
	
	/**
	 * 소수점 이하 decimalPlace 기준으로 반올림한다.
	 */
	private double round(double target) {
		double num = Math.pow(10, decimalPlace);
		return Math.round(target*num)/num;
	}
	
	private void validateLatitude(double... latitudes) {
		for(double lat : latitudes) {
			if(Math.abs(lat) > 90)
				throw new IllegalArgumentException("invalid latitude range");
		}
	}
	
	private void validateLongitude(double...longitudes) {
		for(double longi : longitudes) {
			if(Math.abs(longi) > 180)
				throw new IllegalArgumentException("invalid longitude range");
		}
	}
	
	/**
	 * decimalPlace == 1 일때 increment < 0.05 이면 반올림 때문에 무한 루프 진행된다.
	 * decimalPlace == 2 일때 increment < 0.051
	 */
	private void validateIncrement(double increment) {
		if(increment < Math.pow(10, -decimalPlace))
			throw new IllegalArgumentException("invalid increment range");
			
	}
}
