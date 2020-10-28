package com.realEstateCrawling.vo;

import java.time.LocalDateTime;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
public class RealEstate {
	@EmbeddedId
	private RealEstatePK PK;
	private String url;
	
	private String address; //(시 구 동)
	private String jibunAddress; //지번주소
	private String roadAddress; //도로명주소
	private String latitude;
	private String longitude;
	
	private String saleType; //판매유형 월세,전세,매매
	private String tradingCost; //매매금액
	private String charterCost; //보증금액
	private String monthlyRent; //월세금액
	private String maintanenceCost; //관리비
	
	private String buildingType; //건물유형 단독주택, 오피스텔, 공동주택 ...
	private String roomType; //원룸, 오피스텔, 빌라 ...
	private String exclusiveArea; //전용면적
	private String completionDate; //준공일
	private String approvalDate; //사용승인일
	private String roomCount; //방 개수
	private String bathRoomCount; //화장실 개수
	private String floor; //층수
	private String buildingFloor; //건물층수
	private String isAvailableElevator; //엘리베이터 가능 유무
	private String isAvailableParking; //주차 가능 유무
	private String isAvailablePet; //애완동물 가능 유무
	private String direction; //방향
	private String options; //옵션
	
	private LocalDateTime registeredDate; //크롤링 후 데이터 등록일
	
	public RealEstate() {
		this.registeredDate = LocalDateTime.now();
		this.PK = new RealEstatePK();
	};
}


