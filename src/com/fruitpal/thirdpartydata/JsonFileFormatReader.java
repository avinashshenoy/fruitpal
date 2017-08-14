package com.fruitpal.thirdpartydata;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fruitpal.core.CommoditySourceInfo;

public class JsonFileFormatReader extends ThirdPartyDataDigester {
	
	public static final String COUNTRY = "COUNTRY";
	public static final String COMMODITY = "COMMODITY";
	public static final String FIXED_OVERHEAD = "FIXED_OVERHEAD";
	public static final String VARIABLE_OVERHEAD = "VARIABLE_OVERHEAD";
	public static final String[] FIELDS = new String[]{COUNTRY, COMMODITY, FIXED_OVERHEAD, VARIABLE_OVERHEAD};
	
	ObjectMapper mapper;
	
	public JsonFileFormatReader() {
		mapper = new ObjectMapper();
	}

	@Override
	public Map<String, List<CommoditySourceInfo>> readPricingData(String dataFilePath,
			Map<String, List<CommoditySourceInfo>> commodityToSourceInfoMapper) throws Exception 
	{
		
		// Initialize it if its not already setup
		if (commodityToSourceInfoMapper == null)
		{
			commodityToSourceInfoMapper = new HashMap<String, List<CommoditySourceInfo>>();
		}

		JsonFactory factory = mapper.getFactory();
		File jsonFile = new File(dataFilePath);
		JsonParser parser = factory.createParser(jsonFile);
		
		JsonToken startArrayToken = parser.nextToken();
		
		if (JsonToken.START_ARRAY.equals(startArrayToken))
		{
			// found the start of the JSON Array. Lets read all the individual array
			// entries and extract the pricing data records.
			JsonToken token;
			while( (token = parser.nextToken()) != JsonToken.END_ARRAY )
			{
				//JsonToken token = parser.nextToken();
				if (JsonToken.START_OBJECT.equals(token))
				{
					// Start of a new pricing entry.
					JsonToken fieldNameToken ;
					int fieldCounter = 0;
					CommoditySourceInfo dataPoint = new CommoditySourceInfo();
					
					while ((fieldNameToken = parser.nextToken()) != null &&
							fieldCounter < FIELDS.length) // no more token left
					{
						if (JsonToken.FIELD_NAME.equals(fieldNameToken))
						{
							// Got a field name. Now Verify that it is the field we expect in
							// our JSON data file.
							// Could be COUNTRY, COMMODITY, FIXED_OVERHEAD, VARIABE_OVERHEAD
							String dataField = parser.getCurrentName(); 
							verifyField(FIELDS[fieldCounter], dataField);
							parser.nextToken();
							fillCommoditySourceStructure(dataPoint, dataField, parser);
							fieldCounter++;
						}
						else 
						{
							throw new Exception("Found invalid token in the JSON data file.");
						}
					}
					
					if (fieldCounter != FIELDS.length)
					{
						throw new Exception("We did seem to get all the field that we expected to be present in our data row.");
					}
					
					updateCommoditySourceInfoWithNewPricingData(commodityToSourceInfoMapper, dataPoint);
					// move on to next data row.
					
				}
				else if(JsonToken.END_OBJECT.equals(token))
				{
					// Move on to the next data row.
				}
			}// end while not json array end token
			
			// At this point we have parsed all the individual pricing data rows in the json file.
		}

		return commodityToSourceInfoMapper;
	}
	
	/**
	 * Verifies that we did parse the field that we expected. Does a compared of 
	 * expected field vs what we received. If it doesnt match, then it throws an Exception.
	 * @param expected
	 * @param parsedField
	 */
	private void verifyField(String expected, String parsedField) throws Exception
	{
		if (!expected.equalsIgnoreCase(parsedField))
		{
			throw new Exception("Cost data in JSON data file does not match expected format. Expected field: \"" + expected + " \" but received \"" + parsedField + "\"");
		}
		
	}
	
	private void fillCommoditySourceStructure(CommoditySourceInfo dataStruct, String fieldName, JsonParser parser) throws Exception
	{
		if (COUNTRY.equalsIgnoreCase(fieldName))
		{
			// parser a string
			String countryValue = parser.getValueAsString();
			if (countryValue.isEmpty())
			{
				throw new Exception("Cost data in json file did not match expectation for cost data format. Country value invalid");
			}
			dataStruct.setCountryCode(countryValue);
		}
		else if(COMMODITY.equalsIgnoreCase(fieldName))
		{
			String commodityNameValue = parser.getValueAsString().toUpperCase();
			if (commodityNameValue.isEmpty())
			{
				throw new Exception("Cost data in json file did not match expectation for cost data format. Commodity Name value invalid");
			}
			dataStruct.setCommodityName(commodityNameValue);
		}
		else if(FIXED_OVERHEAD.equalsIgnoreCase(fieldName))
		{
			double fixedOverhead = parser.getValueAsDouble();
			if (fixedOverhead == 0.0)
			{
				// should be getting  zero fixed value. something failed in parsing.
				throw new Exception("Cost data in json file did not match expectation for cost data format. Fixed overhead value invalid");
			}
			dataStruct.setFixedCost(fixedOverhead);
		}
		else if(VARIABLE_OVERHEAD.equalsIgnoreCase(fieldName))
		{
			double variableOverhead = parser.getValueAsDouble();
			if (variableOverhead == 0.0)
			{
				// should be getting  zero fixed value. something failed in parsing.
				throw new Exception("Cost data in json file did not match expectation for cost data format. Variable overhead value invalid");
			}
			dataStruct.setVariableCost(variableOverhead);
		}
		else
		{
			throw new Exception("Unexpected field in the JSON data file. Got: \"" + fieldName + "\"");
		}
		
	}

}
