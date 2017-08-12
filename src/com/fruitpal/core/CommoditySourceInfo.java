package com.fruitpal.core;

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
	
	public double getTotalCost()
	{
		//TODO
		return 0;
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
