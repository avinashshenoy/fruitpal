package com.fruitpal.thirdpartydata.test;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.util.List;

import org.junit.Test;

import com.fruitpal.core.Commodity;
import com.fruitpal.core.CommoditySourceInfo;

public class CommodityTest {

	@Test
	public void testMangoExample() throws Exception 
	{
		Commodity commodity = new Commodity("mango", 53, 405);
		List<CommoditySourceInfo> result = null;
		
		result = commodity.getCommodityInfoForTraderRequest();
		
		assertNotNull(result);
		assertEquals(2, result.size());
		
		CommoditySourceInfo s1 = result.get(0);
		CommoditySourceInfo s2 = result.get(1);
		
		checkCommodityValue(s1, "BR", "22060.10", "54.42", 405, 20, 53);
		checkCommodityValue(s2, "MX", "21999.20", "54.24", 405, 32, 53);
		
	}
	
	@Test
	public void testAppleExample() throws Exception 
	{
		Commodity commodity = new Commodity("apple", 53, 405);
		List<CommoditySourceInfo> result = null;
		
		result = commodity.getCommodityInfoForTraderRequest();
		
		assertNotNull(result);
		assertEquals(1, result.size());
		
		CommoditySourceInfo s1 = result.get(0);
		
		checkCommodityValue(s1, "CB", "22016.50", "54.30", 405, 25, 53);
	}
	
	@Test
	public void testBananaExample() throws Exception 
	{
		Commodity commodity = new Commodity("banana", 50, 405);
		List<CommoditySourceInfo> result = null;
		
		result = commodity.getCommodityInfoForTraderRequest();
		
		assertNotNull(result);
		assertEquals(2, result.size());
		
		CommoditySourceInfo s1 = result.get(0);
		CommoditySourceInfo s2 = result.get(1);
		
		checkCommodityValue(s1, "KN", "20845.10", "51.42", 405, 20, 50);
		checkCommodityValue(s2, "JP", "20821.75", "51.35", 405, 25, 50);
	}
	
	@Test
	public void testInvalidCommodityName() throws Exception
	{
		Commodity commodity = new Commodity("mango", 53, 405);
		
		try
		{
			commodity.getCommodityInfoForTraderRequest();
		}
		catch(Exception e)
		{
			assertEquals("We dont have any pricing information for the given commodity at the moment.", e.getMessage());
		}
		
	}

	private void checkCommodityValue(CommoditySourceInfo commodity, String expectedCountryCode, String expectedTotalCost, String expectedCostPerTon , double quantityInTons, double expectedPerTradeCost, double baseCostPerTonFromTrader) 
	{
		DecimalFormat formatter = new DecimalFormat("0.00");
		assertNotNull(commodity);
		assertEquals(expectedCountryCode, commodity.getCountryCode());
		
		assertEquals(expectedTotalCost, formatter.format(commodity.getTotalCost(baseCostPerTonFromTrader, quantityInTons)));
		assertEquals(expectedCostPerTon, formatter.format(commodity.getBasePerTonCost(baseCostPerTonFromTrader)));
		assertEquals(String.valueOf(formatter.format(expectedPerTradeCost)), formatter.format(commodity.getFixedCost()));
	}

}
