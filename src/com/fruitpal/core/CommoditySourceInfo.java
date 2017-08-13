package com.fruitpal.core;

import java.text.DecimalFormat;

public class CommoditySourceInfo {
	
	public String m_commodityName;
	public String m_countryCode;
	public double m_fixedCost;  // Assuming USD
	public double m_variableCost; // // Assuming USD
	
	public enum Currency
	{
		USD ("$", 1.0);
		
		private final String symbol;
		private final double conversionRate;
		
		Currency(String symbol, double conversion)
		{
			this.symbol = symbol;
			this.conversionRate = conversion;
		}
		
		public String getCurrencySymbol()
		{
			return this.symbol;
		}
		
		public double getConversionRate()
		{
			return this.conversionRate;
		}
	}
	
	public CommoditySourceInfo() {
	}
	
	public CommoditySourceInfo(String name, String code, double fixedCost, double variableCost)
	{
		m_commodityName = name;
		m_countryCode = code;
		m_fixedCost = fixedCost;
		m_variableCost = variableCost;
	}
	
	public String getCommodityName() {
		return m_commodityName;
	}
	
	public void setCommodityName(String m_commodityName) {
		this.m_commodityName = m_commodityName;
	}
	
	public String getCountryCode() {
		return m_countryCode;
	}
	
	public void setCountryCode(String m_countryCode) {
		this.m_countryCode = m_countryCode;
	}
	
	public double getFixedCost() {
		return m_fixedCost;
	}
	
	public void setFixedCost(double m_fixedCost) {
		this.m_fixedCost = m_fixedCost;
	}
	
	public double getVariableCost() {
		return m_variableCost;
	}
	
	public void setVariableCost(double m_variableCost) {
		this.m_variableCost = m_variableCost;
	}
	
	/*
	 * The cost is not being formatted for how many decimal places it need to hold. I would think
	 * formatting would be the responsibility of the caller as each caller may have different requirement for 
	 * how many decimal places to hold.
	 */
	public double getTotalCost(double baseCostPerTonFromTrader, double quantityInTons)
	{
		double totalPerTonCost = getBasePerTonCost(baseCostPerTonFromTrader);
		double costForRequestedTon = getCostForRequestedTon(quantityInTons, totalPerTonCost);
		
		double totalCost = costForRequestedTon + m_fixedCost;
		return totalCost;
	}

	public double getCostForRequestedTon(double quantityInTons, double totalPerTonCost) 
	{
		return totalPerTonCost * quantityInTons;
	}

	public double getBasePerTonCost(double baseCostPerTonFromTrader) 
	{
		return baseCostPerTonFromTrader + m_variableCost;
	}
	
	/*
	 * Unfortunately need to be kept in sync with the code in getTotalCost(..)
	 */
	public String getTotalCostFormatedOutput(double baseCostPerTonFromTrader, double quantityInTons)
	{
		double totalPerTonCost = getBasePerTonCost(baseCostPerTonFromTrader);
		double costForRequestedTon = getCostForRequestedTon(quantityInTons, totalPerTonCost);
		
		double totalCost = costForRequestedTon + m_fixedCost;
		
		DecimalFormat decimalFormatter = new DecimalFormat("0.00"); 
		DecimalFormat quantiyFormatter = new DecimalFormat("#.##");
		StringBuffer buffer = new StringBuffer();
		buffer.append("< ").append(m_countryCode).append(" ");
		buffer.append(decimalFormatter.format(totalCost)).append(" | (");
		buffer.append(decimalFormatter.format(totalPerTonCost)).append("*").append(quantiyFormatter.format(quantityInTons)).append(")+");
		buffer.append(decimalFormatter.format(m_fixedCost));
		
		return buffer.toString();
	}
	
	
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Commodity Name: ").append(m_commodityName).append(" ");
		buffer.append("Country Code: ").append(m_countryCode).append(" ");
		buffer.append("Fixed cost: ").append(m_fixedCost).append(" ");
		buffer.append("Variable Cost: ").append(m_variableCost).append(" ");
		return buffer.toString();
	}
}
