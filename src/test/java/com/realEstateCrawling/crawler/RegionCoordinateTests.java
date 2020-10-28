package com.realEstateCrawling.crawler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import com.realEstateCrawling.crawler.RegionCoordinate;

@SpringBootTest
public class RegionCoordinateTests {
	@Test
	void makeLatitudesTest() {
		List<Double> expected;
		expected = new ArrayList<Double>();
		expected.add(37.1);
		expected.add(37.2);
		expected.add(37.3);
		expected.add(37.4);
		expected.add(37.5);
		assertEquals(expected, RegionCoordinate.TEST_REGION_COORDINATE_1.makeLatitudes());
		
		expected = new ArrayList<Double>();
		expected.add(37.1);
		expected.add(37.3);
		expected.add(37.5);
		assertEquals(expected, RegionCoordinate.TEST_REGION_COORDINATE_2.makeLatitudes());
		
		expected = new ArrayList<Double>();
		expected.add(36.4);
		expected.add(36.6);
		expected.add(36.8);
		assertEquals(expected, RegionCoordinate.TEST_REGION_COORDINATE_5.makeLatitudes());
		
		
		assertEquals("invalid latitude range", Assertions.assertThrows(IllegalArgumentException.class, () -> RegionCoordinate.TEST_REGION_COORDINATE_3.makeLatitudes()).getMessage());
		assertEquals("invalid increment range", Assertions.assertThrows(IllegalArgumentException.class, () -> RegionCoordinate.TEST_REGION_COORDINATE_4.makeLatitudes()).getMessage());
		
	}
	
	@Test
	void makeLongitudesTest() {
		// TODO 테스트 작성하기.
	}
	
	@Test
	void roundTest() {
		assertEquals(10.1, ReflectionTestUtils.invokeMethod(RegionCoordinate.TEST_REGION_COORDINATE_1, "round", 10.12345));
		assertEquals(10.5, ReflectionTestUtils.invokeMethod(RegionCoordinate.TEST_REGION_COORDINATE_1, "round", 10.54321));
		assertEquals(10.6, ReflectionTestUtils.invokeMethod(RegionCoordinate.TEST_REGION_COORDINATE_1, "round", 10.55321));
		assertEquals(10.12, ReflectionTestUtils.invokeMethod(RegionCoordinate.TEST_REGION_COORDINATE_2, "round", 10.12345));
		assertEquals(10.13, ReflectionTestUtils.invokeMethod(RegionCoordinate.TEST_REGION_COORDINATE_2, "round", 10.125));
	}
	
}



