package com.fruitpal.core;

import java.util.List;

/**
 * Main class that parses the command line arguments for the invocation
 * and calls out to business logic that deals with the caculating the pricing data.
 * @author ashenoy
 *
 */
public class Fruitpal {

	public static void main(String[] args) {
		
		if (args.length != 3)
		{
			System.out.println("Incorrect invocation. Expected usage: \"fruitpal COMMODITY_NAME PRICE_PER_TON NUMBER_OF_TONS\"");
			System.exit(0);
		}
		
		String commodityName = args[0].toUpperCase();
		double pricePerTon = 0.0;
		double numberOfTons = 0.0;
		try
		{
			pricePerTon = Double.parseDouble(args[1]);
		}
		catch (NumberFormatException e)
		{
			System.out.println("Unexpected value for pricePerTon. Expected numerical value");
		}
		
		try
		{
			numberOfTons = Double.parseDouble(args[2]);
		}
		catch (NumberFormatException e)
		{
			System.out.println("Unexpected value for number of tons. Expected numerical value");
		}
		
		Commodity commodity = new Commodity(commodityName, pricePerTon, numberOfTons);
		List<CommoditySourceInfo> result = null;
		
		try
		{
			result = commodity.getCommodityInfoForTraderRequest();
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			System.exit(0);
		}
		
		for (CommoditySourceInfo perCountryPricing : result)
		{
			System.out.println(perCountryPricing.getTotalCostFormatedOutput(pricePerTon, numberOfTons));
		}
	}

}
