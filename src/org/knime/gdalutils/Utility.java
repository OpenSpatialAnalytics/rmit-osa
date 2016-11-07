package org.knime.gdalutils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.lang.StringUtils;


public class Utility {
	
	private final static String hdrFormat = "hdr.adf";
	private final static String metaDataFormat = "metadata.xml";
	public static String LOC_COLUMN = "Location";
	public static String RANK = "Rank";
	public static String outputFormat = ".tif";
	public static String shapeFormat = ".shp";
	
	/***
	 * return a list of zip files in directory
	 * @param dirPath - the directory containing zip files of surveys
	 * @return - List of zip files in the directory
	 */
	public static List<String> zipFiles(String dirPath)
	{
		dirPath = dirPath.replace("\\", "/");
		List<String> zipFiles = new ArrayList<String>();
		File dir = new File(dirPath);
		  for (File file : dir.listFiles()) {
		    if (file.getName().toLowerCase().endsWith((".zip"))) {
		    	zipFiles.add(dirPath + "/" + file.getName());
		    }
		  }
		  return zipFiles;
	}
	
	/***
	 * return all hdr.adf files inside a zip file
	 * @param zipFileName
	 * @return List of .hdr files with full path
	 */
	public static List<String> readHdrInZipFile(String zipFileName)
	{
		zipFileName = zipFileName.replace("\\", "/");
		List<String> hdrFiles = new ArrayList<String>();
		
		boolean isZip = false;
		if (zipFileName.toLowerCase().contains(".zip"))
    		isZip = true;
		
		if (isZip) {
			try{
				 ZipFile zipFile = new ZipFile(zipFileName);
				 Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
				 while (zipEntries.hasMoreElements()) {
						String name = ((ZipEntry) zipEntries.nextElement()).getName();
						if (name.endsWith(hdrFormat)) {
							name.replace("\\", "/");
							String hdrFile = zipFileName + "/" + name;
							hdrFiles.add(hdrFile);
						}
				 }
				 zipFile.close();
			}
			catch (Exception e){
				System.out.println("Error reading hdr files in zip file");
				e.printStackTrace();
			}
		}
		else{
			try{
				File folder = new File(zipFileName);				
				File[] listOfFiles = folder.listFiles();				
				for (int i = 0; i < listOfFiles.length; i++) {
					File subFolder = listOfFiles[i];
					if( subFolder.isDirectory() ) {
						for (File f : subFolder.listFiles()) {
							String name = f.getName();
							if (name.endsWith(hdrFormat)) {
								name.replace("\\", "/");
								String hdrFile = zipFileName + "/" + subFolder.getName() + "/"  + name;
								hdrFiles.add(hdrFile);
								break;
							}						      
						}
					}
				 }
				
			}
			catch (Exception e){
				System.out.println("Error reading hdr files in folder");
				e.printStackTrace();
			}
			
		}
		
		return hdrFiles;
	}
	
	/***
	 * return a rankedList of surveys
	 * @param zipFileList or folderList
	 * @return return the rank and corresponding zip file/folder location
	 */
	public static Map<Integer,String> RankZipFilesByTime(List<String> zipFileList)
	{
		Map<Date,String> myMap = new HashMap<Date,String>();
		List<String> metadataFiles = new ArrayList<String>();
		
		String  pathName = zipFileList.get(0);
		boolean isZip = false;
		if (pathName.toLowerCase().contains(".zip"))
    		isZip = true;
		
		if(isZip) {
		
			for (int i=0; i< zipFileList.size(); i++ ){
				String zipFileName = zipFileList.get(i);
				
				try{
					 ZipFile zipFile = new ZipFile(zipFileName);
					 Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
					 while (zipEntries.hasMoreElements()) {
						 	ZipEntry ze = (ZipEntry) zipEntries.nextElement();
							String name = ze.getName();
							if (name.endsWith(metaDataFormat)) {
								name.replace("\\", "/");
								//String metaDataXML = zipFileName + "/" + name;
								//metaDataList.add(metaDataXML);
								InputStreamReader zin =  new InputStreamReader(zipFile.getInputStream(ze));
								BufferedReader br = new BufferedReader(zin);
								String line;
								
					            while ((line = br.readLine()) != null) {
					            	if (line.startsWith("<metadata")){
					            		String dateStr = line.substring(
					            				line.indexOf("<CreaDate>")+("<CreaDate>").length(), 
					            				line.indexOf("</CreaDate>"));
					            		String timeStr = line.substring(
					            				line.indexOf("<CreaTime>")+("<CreaTime>").length(), 
					            				line.indexOf("</CreaTime>")-2);
					            		String dateTimeStr = dateStr + timeStr;
					            		
					            		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
					            		try
					                    {
					                        Date date = simpleDateFormat.parse(dateTimeStr);
					                        myMap.put(date, zipFileName);
					                    }
					                    catch (ParseException ex)
					                    {
					                        System.out.println("Exception "+ex);
					                    }
					            			
					            	}
					            }
					            br.close();
								
								break;
							}
					 }
					 zipFile.close();
				}
				catch (Exception e){
					System.out.println("Error reading " + zipFileName );
					e.printStackTrace();
				}
				
			}		
		}
		
		else{ // the files are inside a folder
			
			for (int i=0; i< zipFileList.size(); i++ ){				
				String zipFileName = zipFileList.get(i);
				
				try{
					File folder = new File(zipFileName);
					File[] listOfFiles = folder.listFiles();
					for (int j = 0; j < listOfFiles.length; j++) {
						  File subFolder = listOfFiles[i];
					      if ( subFolder.isDirectory() ) {
					    	  
					    	  for (File f : subFolder.listFiles()) {
						    	  String name = f.getName();
						    	  if (name.endsWith(metaDataFormat)) {
										name.replace("\\", "/");
										String metaDataFileName = zipFileName + "/" + subFolder.getName() + "/" + name;
										BufferedReader br = new BufferedReader( new FileReader(metaDataFileName));
										
										String line;
										
							            while ((line = br.readLine()) != null) {
							            	if (line.startsWith("<metadata")){
							            		String dateStr = line.substring(
							            				line.indexOf("<CreaDate>")+("<CreaDate>").length(), 
							            				line.indexOf("</CreaDate>"));
							            		String timeStr = line.substring(
							            				line.indexOf("<CreaTime>")+("<CreaTime>").length(), 
							            				line.indexOf("</CreaTime>")-2);
							            		String dateTimeStr = dateStr + timeStr;
							            		
							            		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
							            		try
							                    {
							                        Date date = simpleDateFormat.parse(dateTimeStr);
							                        myMap.put(date, zipFileName);
							                    }
							                    catch (ParseException ex)
							                    {
							                        System.out.println("Exception "+ex);
							                    }
							            			
							            	}
							            }
							            br.close();
							            break;
						    	  }
									
					    	  }
					      } 
					 }
					
				}
				catch (Exception e){
					System.out.println("Error reading hdr files in folder");
					e.printStackTrace();
				}
				
				
			}
			
		}
		
		Map<Date, String> treeMap = new TreeMap<Date, String>(myMap);
		Map<Integer,String> rankedList = new HashMap<Integer,String>();
		
		int i = 1;
		for (String value : treeMap.values() ) {
			Integer key = new Integer(i);
			rankedList.put(key, value);
			i++;
		}
		
		return rankedList;
	}
		
	
	/*
	public static void ReadXML(String xmlFilePath)
	{
		  try {
			  File file = new File(xmlFilePath);
			  DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
                      .newDocumentBuilder();
			  Document doc = dBuilder.parse(file);
			  doc.getDocumentElement().normalize();
			  System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			  NodeList nList = doc.getElementsByTagName("Esri");
			  Node nNode = nList.item(0);
			  Element eElement = (Element) nNode;
			  System.out.println(eElement.getElementsByTagName("CreaDate").item(0).getTextContent());

		  }
		  catch (Exception e) {
			  e.printStackTrace();
			  System.out.println(e.getMessage());
		  }
	}
	*/
	
	public static String ReSampleRaster(String inPath, String outDir, boolean overWrite, String resample, 
			String workingMemory, String outputFormat, String xRes, String yRes, boolean isZip)
	{
		inPath = inPath.replace("\\", "/");
		outDir = outDir.replace("\\", "/");
		
		List<String> commandList = new ArrayList<String>();
		commandList.add("gdalwarp");
		if (overWrite)
			commandList.add("-overwrite");
		commandList.add("-r");
		commandList.add(resample);
		commandList.add("-wm");
		commandList.add(workingMemory);
		commandList.add("-of");
		commandList.add(outputFormat);
		commandList.add("-tr");
		commandList.add(xRes);
		commandList.add(yRes);		
		commandList.add(BuildInputPath(inPath,isZip));
		
		String[] inPaths = inPath.split("/");
		String folderName = inPaths[inPaths.length-2];
		String outFileName = folderName + outputFormat;
		String createdFile = outDir+"/"+outFileName;
		commandList.add(pathBuilder(outDir+"/"+outFileName));
				
		String outputStr = executeCommand(commandList);
		
		String outputStringFile = outDir +"/resample_log.txt";
    	String outputCommandFile = outDir +"/commands.txt";
    	
    	String command = toCommand(commandList); 
    	writeOutputCommand(outputCommandFile, command);
    	writeOutputLog(outputStringFile, command, outputStr);
    	
		return createdFile;	
	}
	
	
	public static String MergeRasters(List<String> inList, String mergedFile, String outputType,
			String noDataValue, String outputFormat)
	{
		mergedFile = mergedFile.replace("\\", "/");
		String gdalPath = getGdalPath();
		
		List<String> commandList = new ArrayList<String>();
		if (gdalPath.length() != 0)
			commandList.add("python");
		commandList.add(pathBuilder(gdalPath+"gdal_merge.py"));
		commandList.add("-ot");
		commandList.add(outputType);
		commandList.add("-n");
		commandList.add(noDataValue);
		commandList.add("-a_nodata");
		commandList.add(noDataValue);
		commandList.add("-o");
		
		if(!mergedFile.endsWith(".tif"))
			mergedFile = mergedFile + ".tif";
		
		commandList.add(pathBuilder(mergedFile));
		commandList.add("-of");
		commandList.add(outputFormat);
		commandList.add("-co");
		commandList.add("SPARSE_OK=TRUE");
		
		for (int i = 0; i < inList.size(); i++ ){
			String inFile = inList.get(i).replace("\\", "/");
			commandList.add(pathBuilder(inFile));
		}
		
		
		String outputStr = executeCommand(commandList);
		
		String folderLoc = mergedFile.substring(0, mergedFile.lastIndexOf("/"));
		
		String outputCommandFile = folderLoc +"/merge.txt";
		String outputStringFile = folderLoc +"/merge_log.txt";
				
		String command = toCommand(commandList);
		writeOutputCommand(outputCommandFile,command);
		writeOutputLog(outputStringFile, command, outputStr);
		
		return mergedFile;		
	}
	
	
	public static String GetGdalCalc(List<String> sourceFiles, List<String> varNames, 
			String destFile,  String type, String expression)
	{
		
		destFile = destFile.replace("\\", "/");
		String gdalPath = getGdalPath();
		
		List<String> commandList = new ArrayList<String>();
		if (gdalPath.length() != 0)
			commandList.add("python");
		commandList.add(pathBuilder(gdalPath+"gdal_calc.py"));
				 
		for (int i=0; i< sourceFiles.size(); i++ ){			
			commandList.add("-"+varNames.get(i));		
			String sourceFile = sourceFiles.get(i);
			sourceFile = sourceFile.replace("\\", "/");
			commandList.add(pathBuilder(sourceFile));
		}
		
		if(!destFile.endsWith(outputFormat))
			destFile = destFile + outputFormat;
		
		commandList.add("--outfile="+pathBuilder(destFile));
		//commandList.add("--type=Byte");
		if ( type != null )
			commandList.add("--type="+type);
		
		commandList.add("--calc="+pathBuilder(expression));
		//commandList.add("--NoDataValue="+noDataVlue);
		commandList.add("--overwrite");	
		
		
	
		String outputStr = executeCommand(commandList);
				
		String outputCommandFile = destFile +"/calc.txt";
		String outputLogFile = destFile +"/calc_log.txt";
		
		
		String command = toCommand(commandList);
		writeOutputCommand(outputCommandFile,command);
		writeOutputLog(outputLogFile, command, outputStr);
		
		return destFile;				
	}
	
	
	public static String MaskRaster(String sourceFile, String outPath, String type, String noDataVlue)
	{
		
		outPath = outPath.replace("\\", "/");
		String gdalPath = getGdalPath();
		
		List<String> commandList = new ArrayList<String>();
		if (gdalPath.length() != 0)
			commandList.add("python");
		commandList.add(pathBuilder(gdalPath+"gdal_calc.py"));	
		
		commandList.add("-A");
		sourceFile = sourceFile.replace("\\", "/");
		commandList.add(pathBuilder(sourceFile));
		
		String[] inPaths = sourceFile.split("/");
		String inFileName = inPaths[inPaths.length-1];
    	String outFile = outPath + "/" + inFileName;       	    	
    	String rank = inFileName.substring(0, inFileName.indexOf(".tif"));
    	
    	String expression = "(A>-1000)*"+rank;				
		commandList.add("--outfile="+pathBuilder(outFile));		
		commandList.add("--type="+type);
		commandList.add("--calc="+pathBuilder(expression));
		commandList.add("--NoDataValue="+noDataVlue);
		commandList.add("--overwrite");	
		
		
		String outputStr = executeCommand(commandList);				
		String outputCommandFile = outPath +"/mask.txt";
		String outputLogFile = outPath +"/mask_log.txt";
		
		String destFile = outPath + "/" + rank + shapeFormat;
		
		
		String command = toCommand(commandList);
		writeOutputCommand(outputCommandFile,command);
		writeOutputLog(outputLogFile, command, outputStr);
		
		GetGdalPolygonize(outFile,destFile,"ESRI Shapefile");
		//outputStr = executeCommand(command);	
		//writeOutputCommand(outputStringFile,command,outputStr);
		
		return destFile;				
	}
	
	
	public static String GetGdalPolygonize(String sourceFile, String destFile, String format)
	{
		sourceFile = sourceFile.replace("\\", "/");
		destFile = destFile.replace("\\", "/");
		String gdalPath = getGdalPath();
		
		List<String> commandList = new ArrayList<String>();
		if (gdalPath.length() != 0)
			commandList.add("python");
		commandList.add(pathBuilder(gdalPath+"gdal_polygonize.py"));
		commandList.add(pathBuilder(sourceFile));
		commandList.add("-f");
		commandList.add(pathBuilder(format));
		
		if(!destFile.endsWith(".shp"))
			destFile = destFile + ".shp";
		
		commandList.add(pathBuilder(destFile));
		

		String outputStr = executeCommand(commandList);
		
		String folderLoc = destFile.substring(0, destFile.lastIndexOf("/"));
		String outputCommandFile = folderLoc +"/polygolize.txt";
		String outputLogFile = folderLoc +"/polygolize_log.txt";
		

		String command = toCommand(commandList);
		writeOutputCommand(outputCommandFile,command);
		writeOutputLog(outputLogFile, command, outputStr);
		
		return destFile;						
	}
	
	public static String ClipPolygonToRaster(String overlapShapeFile, String ovid, String srcTifFile, String destTifFile, 
			boolean overWrite)
	{
		
		overlapShapeFile = overlapShapeFile.replace("\\", "/");
		srcTifFile = srcTifFile.replace("\\", "/");
		destTifFile = destTifFile.replace("\\", "/");
		
		List<String> commandList = new ArrayList<String>();
		commandList.add("gdalwarp");
		if (overWrite)
			commandList.add("-overwrite");
		commandList.add("-cutline");
		commandList.add(pathBuilder(overlapShapeFile));
		commandList.add("-cwhere");
		commandList.add("'ovid="+pathBuilder(ovid)+"'");
		commandList.add("-crop_to_cutline");
		commandList.add(pathBuilder(srcTifFile));
		commandList.add(pathBuilder(destTifFile));
		
		System.out.println(toCommand(commandList));
			
		String command = toCommand(commandList);		
		String outputStr = executeCommand(commandList);
		    	    	
    	String folderLoc = overlapShapeFile.substring(0, overlapShapeFile.lastIndexOf("/"));
    	String outputCommandFile = folderLoc +"/ClipPolygonToRaster.txt";
    	String outputLogFile = folderLoc +"/ClipPolygonToRaster_log.txt";
    	    	
    	writeOutputCommand(outputCommandFile,toCommand(commandList));
    	writeOutputLog(outputLogFile, command, outputStr);
    	    	
		return destTifFile;	
		
	}
	
	public static String GetGdalInfo(String sourceFile)
	{
		sourceFile = sourceFile.replace("\\", "/");
		List<String> commandList = new ArrayList<String>();
		commandList.add("gdalInfo");
		commandList.add("-stats");
		commandList.add(pathBuilder(sourceFile));
		
		//String command = toCommand(commandList);;
		String outputStr = executeCommand(commandList);
		
		return outputStr;			
	}
	
	public static String getNoDataValue()
	{
		return "-340282346638529993179660072199368212480.000";
	}
	
	public static String BuildInputPath(String inPath, boolean isZip)
	{
		String zipCommand = "";
		if (isZip){
			if(inPath.startsWith("/"))
				zipCommand = "/vsizip" + inPath;
			else
				zipCommand = "/vsizip/" + inPath;
			return pathBuilder(zipCommand);
		}
		else{
			 return pathBuilder(inPath);
		}
		
	}
	
	
	private static String pathBuilder(String path)
	{
		return new String("\"" + path + "\"");
	}
	
	private static String getGdalPath()
	{
		/*
		String e = System.getenv("GDAL_DATA");
		e = e.replace("\\", "/");
		String gdalPath = e.substring(0, e.lastIndexOf("/"));
		return gdalPath;
		*/
		
		String os = System.getProperty("os.name");
		boolean isWindows = true;
		
		String token = "";
		if ( os.startsWith("Windows") ){
			token = ";";
		}
		else{
			token = ":";
			isWindows = false;
		}
		
		String pathVar = System.getenv("PATH");
		String[] varList = pathVar.split(token);
		String gdalPath = "";
		
		for (int i = 0; i < varList.length; i++ ){
			String varStr = varList[i].toLowerCase();
			if(varStr.contains("gdal")){
				gdalPath = varList[i];
				break;
			}
		}
		
		gdalPath = gdalPath.replace("\\", "/");
		if ( isWindows )
			return gdalPath + "/";
		else
			return "";
	}
	
	private static String toCommand(List<String> commandList)
	{
		String[] commands = new String[commandList.size()];
		commands = commandList.toArray(commands);
		return StringUtils.join(commands," ");	
	}
	
	private static String executeCommand(List<String> commandList) {
		
		String os = System.getProperty("os.name");
		String importPath = "export PATH=/Library/Frameworks/GDAL.framework/Programs:$PATH";
		
		File f;
		
		if ( os.startsWith("Windows") ){
			f = new File("C:/");
		}
		else{
			f = new File("/");
			try {				
				Process p1 = null;
				p1 = Runtime.getRuntime().exec(importPath);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		String[] commands = new String[commandList.size()];
		commands = commandList.toArray(commands);

		StringBuffer output = new StringBuffer();

		Process p = null;
		try {
			ProcessBuilder pb = new ProcessBuilder(commands);
			pb.directory(f);
			p = pb.start();
			//p = Runtime.getRuntime().exec(command,null,f);		
			int code = p.waitFor();
			if (code == 0){
				BufferedReader reader =
	                            new BufferedReader(new InputStreamReader(p.getInputStream()));
	
	                        String line = "";
				while ((line = reader.readLine())!= null) {
					output.append(line + "\n");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();
	}
	
	private static void writeOutputCommand(String fileName, String command)
	{
		BufferedWriter bw = null;
		
		String os = System.getProperty("os.name");
		String importPath = "export PATH=/Library/Frameworks/GDAL.framework/Programs:$PATH";
		
        try {          	
           bw = new BufferedWriter(new FileWriter(fileName, true));
           if ( !os.startsWith("Windows") ){
        	   BufferedReader br = new BufferedReader(new FileReader(fileName));
        	   String text = br.readLine();
        	   br.close();
        	   if ( text == null ) {        	   
        		   bw.write(importPath);
        	   	   bw.newLine();
        	   }        	  
   		   }           
           bw.write(command);
           bw.newLine();           
           bw.flush();
        }
        catch (IOException ioe) {
        	ioe.printStackTrace();
        } 
        finally {
	        if (bw != null) try {
	        	bw.close();
	        } 
	        catch (IOException ioe2) {}
	    } 		
	}
	
	private static void writeOutputLog(String fileName, String command, String outputStr)
	{
		BufferedWriter bw = null;
		 		
        try {          	
           bw = new BufferedWriter(new FileWriter(fileName, true));               
           bw.write("Command: " + command);
           bw.newLine();           
           bw.write(outputStr);
           bw.newLine();                      
           bw.flush();
        }
        catch (IOException ioe) {
        	ioe.printStackTrace();
        } 
        finally {
	        if (bw != null) try {
	        	bw.close();
	        } 
	        catch (IOException ioe2) {}
	    } 		
		
	}
	
	public static void main (String args[])
	{
		List<String> files = readHdrInZipFile("C:\\Scratch\\gadata\\LoganeCityCouncil");
		for (int i = 0; i < files.size(); i++ ){
			System.out.println(files.get(i));
		}
		
	}
		
			
	/*
	public static void main (String args[])
	{
		List<String> zipFileList =  zipFiles("E:\\GA Project\\data");
		Map<Integer,String> myMap = RankZipFilesByTime(zipFileList);
		System.out.println(myMap.size());
		
		for (Integer key: myMap.keySet() )
			System.out.println(key.toString());
		
		for (String value : myMap.values()){
			System.out.println(value);
		} */
		
		/*
		int number = 65;
		char c = (char)number;
		System.out.println(Character.toString(c));
		*/
		
		/*
		String s = GetGdalResampleCommand("E:/GA Project/data/LoganCityCouncil.zip/e479691901005/hdr.adf",
				"E:/GA Project/data/output",true,"average","500","GTiff","25","25",true);
		System.out.println(s);
		*/
		//String d = System.getenv("PATH");
		//System.out.println(getGdalPath());
		//System.out.println(System.getProperty("os.name"));
		
		
		/*
		String command = "gdalinfo -stats \"C:/Scratch/gadata/LoganeCityCouncilTifMerge/LoganeCityCouncil.tif\"";
		String e =  executeCommand(command);
		String e1 = e.substring(e.indexOf("NoData Value"), e.length()-1);
		String[] e2 = e1.split("\n");
		String[] e3 = e2[0].split("=");
		Double d = 0.0;
		
		try{
			
			d = Double.parseDouble(e3[1]);
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
		}
				
		System.out.println(d);
		*/
		//System.out.println(e);
		
	//}

}
