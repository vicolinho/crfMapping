package de.uni_leipzig.imise.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import de.uni_leipzig.imise.data.CRFVersion;
import de.uni_leipzig.imise.data.Item;
import de.uni_leipzig.imise.data.constants.ItemConstants;



public class VersionReader {
	
	private static Logger log = Logger.getLogger(VersionReader.class.getName());

	private static final String CRF ="CRF";
	
	private static final String GROUPS ="Groups";
	
	private static final String ITEMS ="Items";
	
	private static final String SECTIONS ="Sections";
	
	private static final String RULES ="Rules";
	public CRFVersion readVersion(String fileName) throws IOException{
		File f = new File (fileName);
		int verNr = this.extractVersion(f.getName());
		FileInputStream fis = new FileInputStream(f);
		HSSFWorkbook versionBook= new HSSFWorkbook(fis,false); 
		HSSFSheet versionSheet = versionBook.getSheet(CRF);
		HSSFSheet itemSheet = versionBook.getSheet(ITEMS);
		HSSFSheet sectionSheet = versionBook.getSheet(SECTIONS);
		HSSFSheet groupSheet = versionBook.getSheet(GROUPS);
		CRFVersion version = new CRFVersion();
		version.setVersion(verNr);
		String name = f.getName().substring(0,f.getName().indexOf(verNr+""));
		version.setName(name);
		version.setItems(this.extractItems(itemSheet));
		return version;
	}
	
	private int extractVersion(String fileName) {
		Pattern p = Pattern.compile("[1-9][0-9]{0,2}(?=.{0,5}\\.xls)");
		Matcher m = p.matcher(fileName);
		if (m.find()){
			String v = m.group();
			return Integer.parseInt(v);
		}
		return -1;
	}

	private HashMap<String,Item> extractItems(HSSFSheet itemSheet){
		
		boolean isEmpty = false;
		HashMap<String,Item> itemMap = new HashMap<String,Item>();
		HashMap<Integer, String> dict = new HashMap<Integer,String>();
		HashMap<String,Integer> revDict = new HashMap<String,Integer>();
		int rowNr =0;
		Iterator <Cell> cellIter;
		
		Map<String,Response> resMap = new HashMap<String,Response>();
		while (!isEmpty){
			HSSFRow row = itemSheet.getRow(rowNr);
			Item i = null;
			if (row !=null){
				if (rowNr ==0){
					cellIter = row.cellIterator();
					while (cellIter.hasNext()){
						Cell c = cellIter.next();
						dict.put(c.getColumnIndex(), c.getStringCellValue());
						revDict.put(c.getStringCellValue(), c.getColumnIndex());
					}
				}else {
					i = new Item();
					i.setItemLabel(row.getCell(revDict.get(ItemConstants.ITEM_NAME)).getStringCellValue());
					for (Entry<String,Integer> e :revDict.entrySet()){	
						Cell c = row.getCell(e.getValue());	
						String colName = e.getKey();
						 if (i!=null){
							Object value ="";
							if(c!=null)
							switch (c.getCellType()){
								case Cell.CELL_TYPE_STRING:
									value = c.getStringCellValue();
									break;
								case Cell.CELL_TYPE_NUMERIC:
									value = c.getNumericCellValue()+"";
									break;
								case Cell.CELL_TYPE_BLANK:
									value = "";
									break;
							}
							
							if (!colName.equals(ItemConstants.RESPONSE_LABEL)&&
									!colName.equals(ItemConstants.RESPONSE_OPTIONS_TEXT)&&
									!colName.equals(ItemConstants.RESPONSE_VALUES_OR_CALCULATIONS)&&
									!colName.equals(ItemConstants.ITEM_NAME)){
								i.addProperty(colName,value);
								
							}else if (colName.equals(ItemConstants.RESPONSE_LABEL)){
								String currentCL = (String) value;
								if (!resMap.containsKey(currentCL)){
									Response r = new Response();
									Cell resC =  row.getCell(
											revDict.get(ItemConstants.RESPONSE_OPTIONS_TEXT));
									r.resOptText =(resC==null)?"":resC.getStringCellValue();
									resC = row.getCell(
											revDict.get(ItemConstants.RESPONSE_VALUES_OR_CALCULATIONS));
									r.valCal = (resC==null)?"":resC.getStringCellValue();
									resMap.put(currentCL, r);
									i.addProperty(ItemConstants.RESPONSE_LABEL, currentCL);
									i.addProperty(ItemConstants.RESPONSE_OPTIONS_TEXT, r.resOptText);
									i.addProperty(ItemConstants.RESPONSE_VALUES_OR_CALCULATIONS,r.valCal);
								}else {
									Response r = resMap.get(currentCL);
									i.addProperty(ItemConstants.RESPONSE_LABEL, currentCL);
									i.addProperty(ItemConstants.RESPONSE_OPTIONS_TEXT, r.resOptText);
									i.addProperty(ItemConstants.RESPONSE_VALUES_OR_CALCULATIONS,r.valCal);
								}
							}	
						}//is no empty row
					} //iterate over cells 	
				} //no header 
			}else {
				isEmpty=true;
			}
			if (i!=null){
				i.setPosition(rowNr);
				itemMap.put(i.getItemLabel(), i);
			}
			rowNr++;
		}
		return itemMap;
	}
	
	
	
	public static void main(String[] args){
		VersionReader reader =new VersionReader();
		for (String f :args){
			
			try {
				CRFVersion version = reader.readVersion(f);
				log.info ("#items:"+version.getItems().size());
				int count= 0;
				ArrayList<Item> list = new ArrayList<Item>();
				Collections.addAll(list, version.getItems().values().toArray(new Item[]{}));
				Collections.sort(list);
				for (Item i :list){
					System.out.println(i);
					
				}
				log.info("number of items with full properties:"+count);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class Response {
		String resOptText;
		String valCal; 
	}
}
