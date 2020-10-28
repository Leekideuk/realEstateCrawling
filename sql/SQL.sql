#부동산 테이블 생성
create table real_estate(
	site varchar(50) not null,
    id varchar(100) not null,
    url varchar(255),
    address varchar(100),
    jibun_address varchar(100),
    road_address varchar(100),
    latitude varchar(50),
    longitude varchar(50),
    sale_type varchar(50),
    trading_cost varchar(50),
    charter_cost varchar(50),
    monthly_rent varchar(50),
    maintanence_cost varchar(50),
    building_type varchar(50),
    room_type varchar(50),
    exclusive_area varchar(50),
    completion_date varchar(50),
    approval_date varchar(50),
    room_count varchar(50)	,
    bath_room_count varchar(50),
    floor varchar(50),
    building_floor varchar(50),
    is_available_elevator varchar(50),
    is_available_parking varchar(50),
    is_available_pet varchar(50),
    direction varchar(50),
    options varchar(255),
    registered_date timestamp,
    
    primary key (site, id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


