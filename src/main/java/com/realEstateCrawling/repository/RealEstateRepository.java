package com.realEstateCrawling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realEstateCrawling.vo.RealEstate;
import com.realEstateCrawling.vo.RealEstatePK;

@Repository
public interface RealEstateRepository extends JpaRepository<RealEstate, RealEstatePK> {

}
