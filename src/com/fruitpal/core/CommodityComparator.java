package com.fruitpal.core;

import java.util.Comparator;

public class CommodityComparator implements Comparator<CommoditySourceInfo> {

	@Override
	public int compare(CommoditySourceInfo c1, CommoditySourceInfo c2) {
		if (c1.getTotalCost() < c2.getTotalCost())
		{
			return -1;
		}
		else if (c1.getTotalCost() == c2.getTotalCost())
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}

}
