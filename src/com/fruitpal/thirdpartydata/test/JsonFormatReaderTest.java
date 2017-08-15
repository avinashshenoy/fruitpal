package com.fruitpal.thirdpartydata.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fruitpal.core.CommoditySourceInfo;
import com.fruitpal.thirdpartydata.ThirdPartyDataDigester;

public class JsonFormatReaderTest {

	ThirdPartyDataDigester jsonFile = ThirdPartyDataDigester.getInstance(ThirdPartyDataDigester.jsonFileType);
	
	@Test
	public void testReadPricing_EmptyDataFile() throws Exception 
	{
		String testFile = getClass().getResource("flatFileEmpty.json").getPath();
		Map<String, List<CommoditySourceInfo>> data = jsonFile.readPricingData(testFile, null);
		assertNotNull(data);
		assertTrue(data.size() == 0);
	}
	
	@Test
	public void testReadGoodJsonDataFile() throws Exception {
		String testFile = getClass().getResource("goodJsonDataFile.json").getPath();
		Map<String, List<CommoditySourceInfo>> data = jsonFile.readPricingData(testFile, null);
		assertNotNull(data);
		
		// Expect only one commodity type in this test file, which is MANGO.
		assertTrue(data.size() == 1);
		
		List<CommoditySourceInfo> commoditySourceList = data.get("MANGO");
		assertNotNull(commoditySourceList);
		assertEquals(2, commoditySourceList.size());
		
		CommoditySourceInfo sourceInfo = commoditySourceList.get(0);
		FlatFileFormatReaderTest.checkCommoditySourceInfo(sourceInfo, "MANGO", "MX", 32, 1.24);
		
		CommoditySourceInfo sourceInfo2 = commoditySourceList.get(1);
		FlatFileFormatReaderTest.checkCommoditySourceInfo(sourceInfo2, "MANGO", "BR", 20, 1.42);
	}
	
	@Test
	public void testReadPricing_invalidFixedPrice() throws Exception
	{
		String testFile = getClass().getResource("invalidFixedPriceDataFile.json").getPath();
		try
		{
			jsonFile.readPricingData(testFile, null);
			fail("Should have failed to parse the invalid data");
		}
		catch (Exception e)
		{
			assertTrue(e.getMessage().contains("Cost data in json file did not match expectation for cost data format."));
		}
	}
	
	@Test
	public void testReadPricing_invalidVariablePrice() throws Exception
	{
		String testFile = getClass().getResource("invalidVariablePriceDataFile.json").getPath();
		try
		{
			jsonFile.readPricingData(testFile, null);
			fail("Should have failed to parse the invalid data");
		}
		catch (Exception e)
		{
			assertTrue(e.getMessage().contains("Cost data in json file did not match expectation for cost data format."));
		}
	}
	
	@Test
	public void testReadPricing_missingDataAndIncorrectOrderField() throws Exception
	{
		String testFile = getClass().getResource("missingDataFieldFile.json").getPath();
		try
		{
			jsonFile.readPricingData(testFile, null);
			fail("Should have failed to parse the invalid data");
		}
		catch (Exception e)
		{
			assertTrue(e.getMessage().contains("Cost data in JSON data file does not match expected format. Expected field"));
		}
	}
	
	@Test
	public void testReadPricing_missingDataField() throws Exception
	{
		String testFile = getClass().getResource("missingDataFieldFile2.json").getPath();
		try
		{
			jsonFile.readPricingData(testFile, null);
			fail("Should have failed to parse the invalid data");
		}
		catch (Exception e)
		{
			assertTrue(e.getMessage().contains("Found invalid token in the JSON data file. Expected to find more field in the JSON tuple"));
		}
	}
	
	/*
	 * Data file with Apple and Mango data info
	 */
	@Test
	public void testReadPricing_twoCommodityEntriesDataField() throws Exception
	{
		String testFile = getClass().getResource("twoCommodityEntriesDataFile.json").getPath();
		Map<String, List<CommoditySourceInfo>> data = jsonFile.readPricingData(testFile, null);
		assertNotNull(data);
		
		// Expect only one commodity type in this test file, which is MANGO.
		assertTrue(data.size() == 2);
		
		List<CommoditySourceInfo> commoditySourceList = data.get("MANGO");
		assertNotNull(commoditySourceList);
		assertEquals(2, commoditySourceList.size());
		
		CommoditySourceInfo sourceInfo = commoditySourceList.get(0);
		FlatFileFormatReaderTest.checkCommoditySourceInfo(sourceInfo, "MANGO", "MX", 32, 1.24);
		
		CommoditySourceInfo sourceInfo2 = commoditySourceList.get(1);
		FlatFileFormatReaderTest.checkCommoditySourceInfo(sourceInfo2, "MANGO", "BR", 20, 1.42);
		
		commoditySourceList = data.get("APPLE");
		assertNotNull(commoditySourceList);
		assertEquals(1, commoditySourceList.size());
		
		sourceInfo = commoditySourceList.get(0);
		FlatFileFormatReaderTest.checkCommoditySourceInfo(sourceInfo, "APPLE", "CB", 25, 1.30);
	}
	
	/*
	 * InputData and InputData2 file contents combined
	 */
	@Test
	public void testReadPricing_twoInputDataCombined() throws Exception
	{
		Map<String, List<CommoditySourceInfo>> data = jsonFile.readPricingDataFromThirdParty();
		assertNotNull(data);
		
		// Expect only one commodity type in this test file, which is MANGO.
		assertTrue(data.size() == 8);
		
		// Check for data from inputData
		List<CommoditySourceInfo> commoditySourceList = data.get("PLUM");
		assertNotNull(commoditySourceList);
		assertEquals(2, commoditySourceList.size());
		
		CommoditySourceInfo sourceInfo = commoditySourceList.get(0);
		FlatFileFormatReaderTest.checkCommoditySourceInfo(sourceInfo, "PLUM", "MX", 32, 1.24);
		
		CommoditySourceInfo sourceInfo2 = commoditySourceList.get(1);
		FlatFileFormatReaderTest.checkCommoditySourceInfo(sourceInfo2, "PLUM", "BR", 20, 1.42);
		
		commoditySourceList = data.get("PEACH");
		assertNotNull(commoditySourceList);
		assertEquals(1, commoditySourceList.size());
		
		sourceInfo = commoditySourceList.get(0);
		FlatFileFormatReaderTest.checkCommoditySourceInfo(sourceInfo, "PEACH", "CB", 25, 1.30);
		
		// Check for data from inputData2
		commoditySourceList = data.get("STRAWBERRY");
		assertNotNull(commoditySourceList);
		assertEquals(1, commoditySourceList.size());
		
		sourceInfo = commoditySourceList.get(0);
		FlatFileFormatReaderTest.checkCommoditySourceInfo(sourceInfo, "STRAWBERRY", "MX", 32, 1.24);
		
		commoditySourceList = data.get("RASBERRY");
		assertNotNull(commoditySourceList);
		assertEquals(2, commoditySourceList.size());
		
		sourceInfo = commoditySourceList.get(0);
		FlatFileFormatReaderTest.checkCommoditySourceInfo(sourceInfo, "RASBERRY", "JP", 25, 1.35);
		
		sourceInfo2 = commoditySourceList.get(1);
		FlatFileFormatReaderTest.checkCommoditySourceInfo(sourceInfo2, "RASBERRY", "KN", 20, 1.42);
	}
	
	@Test
	public void testReadPricing_invalidJsonDataFile() throws Exception
	{
		String testFile = getClass().getResource("badJsonFile.json").getPath();
		try
		{
			jsonFile.readPricingData(testFile, null);
			fail("Should have failed to parse the invalid data");
		}
		catch (Exception e)
		{
			assertEquals("The json input file isnt formatted as expected. Expected to find Start Array token", e.getMessage());
		}
	}

}
