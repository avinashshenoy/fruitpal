package com.fruitpal.thirdpartydata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fruitpal.core.CommoditySourceInfo;

/**
 * Reader for 3rd party data received in flat file format.
 * @author ashenoy
 *
 */
public class FlatFileFormatReader extends ThirdPartyDataDigester 
{
	private static Logger m_FlatFilelogger = Logger.getLogger(FlatFileFormatReader.class.getName());
	
	public FlatFileFormatReader() 
	{
		super();
	}

	@Override
	public Map<String, List<CommoditySourceInfo>> readPricingData(String dataFilePath, Map<String, List<CommoditySourceInfo>> commodityToSourceInfoMapper) throws Exception 
	{
		if (commodityToSourceInfoMapper == null)
		{
			commodityToSourceInfoMapper = new HashMap<String, List<CommoditySourceInfo>>();
		}
		
		FileReader fileReader;
		BufferedReader bufferedReader;
		
		File inputFile = new File(dataFilePath);
		
		try
		{
			fileReader = new FileReader(inputFile);
		}
		catch (FileNotFoundException e)
		{
			throw new Exception("Invalid path for data file, please verify data file path is correct", e);
		}
		
		bufferedReader = new BufferedReader(fileReader);
		
		try
		{
			String currentLine;
			while ((currentLine = bufferedReader.readLine()) != null)
			{
				m_FlatFilelogger.log(Level.FINE, "Processing row: " + currentLine);
				
				String[] commodityInfoInputRow = currentLine.split("\\s");
				if (commodityInfoInputRow.length != 4)
				{
					throw new Exception("Expected to find 4 data point in the flat file format but received : \"" + currentLine + "\"");
				}
				
				String type =  commodityInfoInputRow[0].toUpperCase();
				String countryCode = commodityInfoInputRow[1].toUpperCase();
				double fixedCost;
				double variableCost;
				
				try
				{
					fixedCost = Double.parseDouble(commodityInfoInputRow[2]);
					variableCost = Double.parseDouble(commodityInfoInputRow[3]);
				}
				catch(NumberFormatException e)
				{
					throw new Exception("Cost data in flat file did not match expectation for cost data format.", e);
				}
				
				// we have all needed data field, so lets create the CommoditySourceInfo data point.
				updateCommoditySourceInfoWithNewPricingData(commodityToSourceInfoMapper, 
															type, 
															countryCode, 
															fixedCost,
															variableCost);
			}
		}
		finally
		{
			bufferedReader.close();
		}
		
		return commodityToSourceInfoMapper;
	}

}
