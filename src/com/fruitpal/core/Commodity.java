package com.fruitpal.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fruitpal.thirdpartydata.ThirdPartyDataDigester;

public class Commodity {
	String commodityName;
	double pricePerTon;
	double numberOfTons;
	
	public Commodity(String name, double price, double tons) {
		commodityName = name.toUpperCase();
		pricePerTon = price;
		numberOfTons = tons;
	}
	
	public String getCommodityType()
	{
		return commodityName;
	}
	
	/**
	 * @return Return list of CommoditySourceInfo for the requested parameter from the trader
	 * @throws Exception if we dont have any current pricing for request criteria by the trader. 
	 */
	public List<CommoditySourceInfo> getCommodityInfoForTraderRequest() throws Exception
	{
		// Do the data loads to pull in the pricing from third party data source. Assumed to be in 
		// file system at relative path "../thirdpartydata/data". How many every data files there are,
		// it gets read and then returns a accumulation of pricing data per commodity type/name.
		Map<String, List<CommoditySourceInfo>> commodityToSourceInfoMapper = new HashMap<String, List<CommoditySourceInfo>>();
		String dataFileDirectory = getClass().getResource("../thirdpartydata/data").getPath();
		
		ThirdPartyDataDigester.readPricingDataFromThirdParty(commodityToSourceInfoMapper, dataFileDirectory);
		
		// Get the pricing data for the commodity that the trader requested.
		List<CommoditySourceInfo> pricingInfoForRequestType = commodityToSourceInfoMapper.get(commodityName);
		
		if (pricingInfoForRequestType == null)
		{
			throw new Exception("We dont have any pricing information for the given commodity at the moment.");
		}
		
		CommodityComparator comparator = new CommodityComparator(pricePerTon, numberOfTons); 
		Collections.sort(pricingInfoForRequestType, Collections.reverseOrder(comparator));
		
		return pricingInfoForRequestType;
	}

}
