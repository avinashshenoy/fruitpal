package com.fruitpal.core;

import java.util.List;

public class Commodity {
	String commodityName;
	double pricePerTon;
	double numberOfTons;
	
	public Commodity(String name, double price, double tons) {
		commodityName = name;
		pricePerTon = price;
		numberOfTons = tons;
	}
	
	public String getCommodityType()
	{
		return commodityName;
	}
	
	public List<CommoditySourceInfo> getCommodityInfoForTraderRequest()
	{
		return null;
	}

}
