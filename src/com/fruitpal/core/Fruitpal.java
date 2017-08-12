package com.fruitpal.core;

public class Fruitpal {

	public static void main(String[] args) {
		
		if (args.length != 3)
		{
			System.out.println("Incorrect invocation. Expected usage: \"Fruitpal COMMODITY_NAME PRICE_PER_TON NUMBER_OF_TONS\"");
			System.exit(0);
		}
		
		String commodityName = args[0];
		double pricePerTon;
		double numberOfTons;
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
		
		

	}

}
