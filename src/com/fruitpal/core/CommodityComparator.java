package com.fruitpal.core;

import java.util.Comparator;

/**
 * Comparator class that compared two CommoditySourceInfo objects based on the
 * totalCost information. This comparator give a ascending order sort.
 * @author ashenoy
 *
 */
public class CommodityComparator implements Comparator<CommoditySourceInfo> {
	
	double m_baseCostPerTonFromTrader;
	double m_quantityInTons;
	
	public CommodityComparator(double costPerTon, double quantity) 
	{
		m_baseCostPerTonFromTrader = costPerTon;
		m_quantityInTons = quantity;
	}

	@Override
	public int compare(CommoditySourceInfo c1, CommoditySourceInfo c2) {
		double c1Cost = c1.getTotalCost(m_baseCostPerTonFromTrader, m_quantityInTons);
		double c2Cost = c2.getTotalCost(m_baseCostPerTonFromTrader, m_quantityInTons);
		
		if (c1Cost < c2Cost)
		{
			return -1;
		}
		else if (c1Cost == c2Cost)
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}

}
