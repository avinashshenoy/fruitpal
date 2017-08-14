package com.fruitpal.thirdpartydata;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fruitpal.core.CommoditySourceInfo;

public abstract class ThirdPartyDataDigester {
	private static Logger m_logger = Logger.getLogger(ThirdPartyDataDigester.class.getName());
	public static final String flatFileType = "flatfile";
	public static final String jsonFileType = "jsonfile";
	
	public abstract Map<String, List<CommoditySourceInfo>> readPricingData(String dataFilePath, Map<String, List<CommoditySourceInfo>> commodityToSourceInfoMapper) throws Exception;
	
	/**
	 * Read pricing data from third party data prices located in the data folder relative to
	 * this class 
	 */
	public Map<String, List<CommoditySourceInfo>> readPricingDataFromThirdParty()
	{
		Map<String, List<CommoditySourceInfo>> commodityToSourceInfoMapper = new HashMap<String, List<CommoditySourceInfo>>();
		String dataFileDirectory = getClass().getResource("data").getPath();
		
		readPricingDataFromThirdParty(commodityToSourceInfoMapper, dataFileDirectory);
		
		return commodityToSourceInfoMapper;
	}
	
	/**
	 * Update collection of CommodityToSourceInfo mapper with new commodity data point represented
	 * by the tuple {type, countryCode, fixedCost, variableCost}
	 * @param commodityToSourceInfoMapper
	 * @param type
	 * @param countryCode
	 * @param fixedCost
	 * @param variableCost
	 */
	public void updateCommoditySourceInfoWithNewPricingData(Map<String, List<CommoditySourceInfo>> commodityToSourceInfoMapper, String type, String countryCode, double fixedCost,
			double variableCost) {
				CommoditySourceInfo commodityInfo = new CommoditySourceInfo(type, countryCode, fixedCost, variableCost);
				
				updateCommoditySourceInfoWithNewPricingData(commodityToSourceInfoMapper, commodityInfo);
			}

	public void updateCommoditySourceInfoWithNewPricingData(
			Map<String, List<CommoditySourceInfo>> commodityToSourceInfoMapper,
			CommoditySourceInfo commodityInfo) 
	{
		String type = commodityInfo.getCommodityName();
		
		List<CommoditySourceInfo> sourceInfoPerCommodityType = commodityToSourceInfoMapper.get(type);
		if (sourceInfoPerCommodityType == null)
		{
			// First entry in the listing, create a new listing
			sourceInfoPerCommodityType = new ArrayList<CommoditySourceInfo>();
		}
		
		// Add this entry to our per commodity list and update the tracker with the new list.
		sourceInfoPerCommodityType.add(commodityInfo);
		commodityToSourceInfoMapper.put(type, sourceInfoPerCommodityType);
	}

	/**
	 * https://stackoverflow.com/questions/14676407/list-all-files-in-the-folder-and-also-sub-folders/14676464#14676464 
	 */
	public static void listFileRecursively(String directoryPath, ArrayList<File> files) {
	    File directory = new File(directoryPath);
	
	    // get all the files from a directory
	    File[] fList = directory.listFiles();
	    for (File file : fList) {
	        if (file.isFile()) {
	            files.add(file);
	        } else if (file.isDirectory()) {
	        	listFileRecursively(file.getAbsolutePath(), files);
	        }
	    }
	}

	/**
	 * Depending on the type requested, this method returns an instance of either
	 * FlatFileFormatReader or JSONFileFormatReader. Defaults to FlatFileFormatReader
	 * if type specified is not one of the two expected formats.
	 * @param type
	 * @return
	 */
	public static ThirdPartyDataDigester getInstance(String type)
	{
		ThirdPartyDataDigester digester;
		switch(type)
		{
		case flatFileType:
			digester = new FlatFileFormatReader();
			break;
			
		case jsonFileType:
			digester = new JsonFileFormatReader();
			break;
			
		default :
			digester = new FlatFileFormatReader();
			break;
		}
		return digester;
	}
	
	public static Logger getLogger()
	{
		return m_logger;
	}

	/**
	 * Read pricing data from 1 or more pricing data files located in the dataFileDirectoryPath
	 * provided as parameter. If one or more data file contains data in unexpected formats, 
	 * the issue is logged and this function skips processing of that data file.
	 * 
	 * Expected data file format: 
	 * suffice ".json" =>  JSON data file
	 * All other suffice is considered to be flatfile format. 
	 * 
	 * Upon completion, data is available in the {@commodityToSourceInfoMapper} map, which is a
	 * key value mapping from Commodity Name to a collection of CommoditySourceInfo. 
	 * commodityToSourceInfoMapper will be empty if there was no thirdparty data successfully read.   
	 */
	public static void readPricingDataFromThirdParty(Map<String, List<CommoditySourceInfo>> commodityToSourceInfoMapper, String dataFileDirectoryPath) {
		if (new File(dataFileDirectoryPath).exists())
		{
			// Build a list of file name for that potentially contain the our data set.
			ArrayList<File> files = new ArrayList<File>();
			listFileRecursively(dataFileDirectoryPath, files);
			
			for (File file : files)
			{
				try
				{
					ThirdPartyDataDigester digester = null ;
					if (!(file.getName().endsWith(".json")))
					{
						getLogger().log(Level.INFO, "Found flat file: " + file.getName());
						digester = ThirdPartyDataDigester.getInstance(ThirdPartyDataDigester.flatFileType);
					}
					else
					{
						getLogger().log(Level.INFO, "Found json file: " + file.getName());
						// Its a JSON data file. Need the JSON data parser.
						digester = ThirdPartyDataDigester.getInstance(ThirdPartyDataDigester.jsonFileType);
					}
					digester.readPricingData(file.getPath(), commodityToSourceInfoMapper);
				}
				catch (Exception e )
				{
					getLogger().log(Level.SEVERE, "Ran into an error parsing data file: \"" + file.getName() + "\"" , e);
				}
			}
		}
	}

}
