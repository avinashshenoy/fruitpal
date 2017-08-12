package com.fruitpal.thirdpartydata.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fruitpal.core.CommoditySourceInfo;
import com.fruitpal.thirdpartydata.ThirdPartyDataDigester;

public class FlatFileFormatReaderTest {
	
	ThirdPartyDataDigester flatFile = ThirdPartyDataDigester.getInstance(ThirdPartyDataDigester.flatFileType);

	@Test
	public void testReadPricing_EmptyDataFile() throws Exception 
	{
		String testFile = getClass().getResource("flatFileEmpty").getPath();
		Map<String, List<CommoditySourceInfo>> data = flatFile.readPricingData(testFile, null);
		assertNotNull(data);
		assertTrue(data.size() == 0);
	}
	
	@Test
	public void testReadPricing_invalidFixedPrice() throws Exception
	{
		String testFile = getClass().getResource("invalidFixedPriceDataFile").getPath();
		try
		{
			flatFile.readPricingData(testFile, null);
			fail("Should have failed to parse the invalid data");
		}
		catch (Exception e)
		{
			assertEquals("Cost data in flat file did not match expectation for cost data format.", e.getMessage());
		}
	}
	
	@Test
	public void testReadPricing_invalidVariablePrice() throws Exception
	{
		String testFile = getClass().getResource("invalidVariablePriceDataFile").getPath();
		try
		{
			flatFile.readPricingData(testFile, null);
			fail("Should have failed to parse the invalid data");
		}
		catch (Exception e)
		{
			assertEquals("Cost data in flat file did not match expectation for cost data format.", e.getMessage());
		}
	}
	
	@Test
	public void testReadPricing_missingDataField() throws Exception
	{
		String testFile = getClass().getResource("missingDataFieldFile").getPath();
		try
		{
			flatFile.readPricingData(testFile, null);
			fail("Should have failed to parse the invalid data");
		}
		catch (Exception e)
		{
			assertTrue(e.getMessage().contains("Expected to find 4 data point in the flat file format but received"));
		}
	}
	
	@Test
	public void testReadPricing_mangoEntriesDataField() throws Exception
	{
		String testFile = getClass().getResource("MangoEntriesDataFile").getPath();
		Map<String, List<CommoditySourceInfo>> data = flatFile.readPricingData(testFile, null);
		assertNotNull(data);
		
		// Expect only one commodity type in this test file, which is MANGO.
		assertTrue(data.size() == 1);
		
		List<CommoditySourceInfo> commoditySourceList = data.get("MANGO");
		assertNotNull(commoditySourceList);
		assertEquals(2, commoditySourceList.size());
		
		CommoditySourceInfo sourceInfo = commoditySourceList.get(0);
		checkCommoditySourceInfo(sourceInfo, "MANGO", "MX", 32, 1.24);
		
		CommoditySourceInfo sourceInfo2 = commoditySourceList.get(1);
		checkCommoditySourceInfo(sourceInfo2, "MANGO", "BR", 20, 1.42);
	}
	
	/*
	 * Data file with Apple and Mango data info
	 */
	@Test
	public void testReadPricing_twoCommodityEntriesDataField() throws Exception
	{
		String testFile = getClass().getResource("twoCommodityEntriesDataFile").getPath();
		Map<String, List<CommoditySourceInfo>> data = flatFile.readPricingData(testFile, null);
		assertNotNull(data);
		
		// Expect only one commodity type in this test file, which is MANGO.
		assertTrue(data.size() == 2);
		
		List<CommoditySourceInfo> commoditySourceList = data.get("MANGO");
		assertNotNull(commoditySourceList);
		assertEquals(2, commoditySourceList.size());
		
		CommoditySourceInfo sourceInfo = commoditySourceList.get(0);
		checkCommoditySourceInfo(sourceInfo, "MANGO", "MX", 32, 1.24);
		
		CommoditySourceInfo sourceInfo2 = commoditySourceList.get(1);
		checkCommoditySourceInfo(sourceInfo2, "MANGO", "BR", 20, 1.42);
		
		commoditySourceList = data.get("APPLE");
		assertNotNull(commoditySourceList);
		assertEquals(1, commoditySourceList.size());
		
		sourceInfo = commoditySourceList.get(0);
		checkCommoditySourceInfo(sourceInfo, "APPLE", "CB", 25, 1.30);
	}
	
	/*
	 * InputData and InputData2 file contents combined
	 */
	@Test
	public void testReadPricing_twoInputDataCombined() throws Exception
	{
		Map<String, List<CommoditySourceInfo>> data = flatFile.readPricingDataFromThirdParty();
		assertNotNull(data);
		
		// Expect only one commodity type in this test file, which is MANGO.
		assertTrue(data.size() == 4);
		
		// Check for data from inputData
		List<CommoditySourceInfo> commoditySourceList = data.get("MANGO");
		assertNotNull(commoditySourceList);
		assertEquals(2, commoditySourceList.size());
		
		CommoditySourceInfo sourceInfo = commoditySourceList.get(0);
		checkCommoditySourceInfo(sourceInfo, "MANGO", "MX", 32, 1.24);
		
		CommoditySourceInfo sourceInfo2 = commoditySourceList.get(1);
		checkCommoditySourceInfo(sourceInfo2, "MANGO", "BR", 20, 1.42);
		
		commoditySourceList = data.get("APPLE");
		assertNotNull(commoditySourceList);
		assertEquals(1, commoditySourceList.size());
		
		sourceInfo = commoditySourceList.get(0);
		checkCommoditySourceInfo(sourceInfo, "APPLE", "CB", 25, 1.30);
		
		// Check for data from inputData2
		commoditySourceList = data.get("ORANGE");
		assertNotNull(commoditySourceList);
		assertEquals(1, commoditySourceList.size());
		
		sourceInfo = commoditySourceList.get(0);
		checkCommoditySourceInfo(sourceInfo, "ORANGE", "MX", 32, 1.24);
		
		commoditySourceList = data.get("BANANA");
		assertNotNull(commoditySourceList);
		assertEquals(2, commoditySourceList.size());
		
		sourceInfo = commoditySourceList.get(0);
		checkCommoditySourceInfo(sourceInfo, "BANANA", "JP", 25, 1.30);
		
		sourceInfo2 = commoditySourceList.get(1);
		checkCommoditySourceInfo(sourceInfo2, "BANANA", "KN", 20, 1.42);
	}
	
	private void checkCommoditySourceInfo(CommoditySourceInfo info, String expectedName, String expectedCountryCode, double expectedFixedValue, double expectedVariableValue)
	{
		assertNotNull(info);
		assertEquals(expectedName, info.getCommodityName());
		assertEquals(expectedCountryCode, info.getCountryCode());
		assertTrue(expectedFixedValue == info.getFixedCost());
		assertTrue(expectedVariableValue == info.getVariableCost());
	}

}
