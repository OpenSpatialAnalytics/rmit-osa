package org.knime.gdalutils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.knime.geo.resample.DirectoryFormat;
import org.knime.geoutils.Constants;


public class Utility {
	
	private final static String hdrFormat = "hdr.adf";
	private final static String metaDataFormat = "metadata.xml";
	public static String LOC_COLUMN = "Location";
	public static String outputFormat = ".tif";
	public static String shapeFormat = ".shp";
	public static String mergedFileName = "merged.shp";
	
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
	 * return all hdr.adf files inside a zip file or a folder
	 * @param zipFileName
	 * @return List of .hdr files with full path
	 */
	public static List<String> readHdrFiles(String location)
	{
		location = location.replace("\\", "/");
		List<String> hdrFiles = new ArrayList<String>();
		
		boolean isZip = false;
		if (location.toLowerCase().contains(".zip"))
    		isZip = true;
		
		if (isZip) {
			try{
				 ZipFile zipFile = new ZipFile(location);
				 Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
				 while (zipEntries.hasMoreElements()) {
						String name = ((ZipEntry) zipEntries.nextElement()).getName();
						if (name.endsWith(hdrFormat)) {
							name = name.replace("\\", "/");
							String hdrFile = location + "/" + name;
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
				File folder = new File(location);				
				File[] listOfFiles = folder.listFiles(new FileFilter() {
				    @Override
				    public boolean accept(File f) {
				        return f.isDirectory();
				    }
				});											
				
				for (int i = 0; i < listOfFiles.length; i++) {
					File subFolder = listOfFiles[i];
					File[] adfFiles = subFolder.listFiles(new FileFilter() {
					    @Override
					    public boolean accept(File f) {
					        return f.getName().endsWith(hdrFormat);
					    }
					});		
					
					if( adfFiles.length > 0 ) {					
						String hdrFile = adfFiles[0].getAbsolutePath();																						
						hdrFile = hdrFile.replace("\\", "/");							
						hdrFiles.add(hdrFile);
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
								name = name.replace("\\", "/");
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
					File[] listOfFiles = folder.listFiles(new FileFilter() {
					    @Override
					    public boolean accept(File f) {
					        return f.isDirectory();
					    }
					});									
						  
					File[] metaDataFiles = listOfFiles[0].listFiles(new FileFilter() {
					    public boolean accept(File f) {
					        return  f.getName().endsWith(metaDataFormat);
					    }
					});	
													
					String metaDataFileName = metaDataFiles[0].getAbsolutePath();
					metaDataFileName.replace("\\", "/");
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
	
	public static String ReSampleRaster(String inPath, String outDir, String directoryFormat,
			String selectedColumn, String columnValueName, String columnValueNo,
			boolean overWrite, boolean tap,
			String resample, String workingMemory, String oFormat, String s_srs, String t_srs,
			String xRes, String yRes, boolean isRun, boolean isZip)
	{
		inPath = inPath.replace("\\", "/");
		outDir = outDir.replace("\\", "/");
		
		List<String> commandList = new ArrayList<String>();
		commandList.add("gdalwarp");
		if (tap)
			commandList.add("-tap");
		if (overWrite)
			commandList.add("-overwrite");
		if(!s_srs.isEmpty()){
			commandList.add("-s_srs");
			commandList.add(s_srs);
		}
		if(!t_srs.isEmpty()){
			commandList.add("-t_srs");
			commandList.add(t_srs);
		}
		commandList.add("-r");
		commandList.add(resample);
		commandList.add("-wm");
		commandList.add(workingMemory);
		commandList.add("-of");
		commandList.add(oFormat);
		commandList.add("-tr");
		commandList.add(xRes);
		commandList.add(yRes);		
		commandList.add(BuildInputPath(inPath,isZip));
		
		String outputSubFolder = "";
		String outFileName = "";
		String[] inPaths = inPath.split("/");
		
		if (directoryFormat.compareTo(DirectoryFormat.MainDir.toString())==0){
			
			outputSubFolder = outDir;
			outFileName = inPaths[inPaths.length-1];  //take the source file name from input path
		}
		else if (directoryFormat.compareTo(DirectoryFormat.SubDir.toString())==0){
			/*
			String folderName = inPaths[inPaths.length-2];			
			String parentFolder = inPaths[inPaths.length-3];
			if ( parentFolder.contains(".zip") ){
				parentFolder = parentFolder.substring(0, parentFolder.length()-5);			
			}
			outputSubFolder = outDir+"/"+parentFolder;
			outFileName = folderName + outputFormat;
			*/
			String parentFolder  = outDir+"/"+selectedColumn;  //column name as subdirectory
			File directory = new File(parentFolder);
			if (! directory.exists()){
				directory.mkdir();
			}
			outputSubFolder = parentFolder + "/" + columnValueName;
			outFileName = columnValueNo + outputFormat;
			
		}
	
		File directory = new File(outputSubFolder);
		if (! directory.exists()){
			directory.mkdir();
		}
				
		String createdFile = outputSubFolder+"/"+outFileName;
		commandList.add(pathBuilder(createdFile));
				
		
		String outputStr = "";
		
		if (isRun)
			outputStr = executeCommand(commandList);
		else
			outputStr = "commands";
				
		String outputStringFile = outputSubFolder +"/resample_log.txt";
    	String outputCommandFile = outputSubFolder +"/commands.txt";
    	
    	String command = toCommand(commandList); 
    	writeOutputCommand(outputCommandFile, command);
    	writeOutputLog(outputStringFile, command, outputStr);
    	
		return createdFile;	
	}
	
	
	public static String MergeRasters(List<String> inList, String inPath, String mergedFile, 
			String outputType, String noDataValue, String oFormat, boolean isRun)
	{
		mergedFile = mergedFile.replace("\\", "/");
		String gdalPath = getGdalPath();
		
		List<String> commandList = new ArrayList<String>();
		List<String> commandList1 = new ArrayList<String>();

		if (gdalPath.length() != 0)
			commandList.add("python");
		commandList.add(pathBuilder(gdalPath+"gdal_merge.py"));
		commandList.add("-ot");
		commandList.add(outputType);
		//commandList.add("-n");
		//commandList.add(noDataValue);
		commandList.add("-a_nodata");
		commandList.add(noDataValue);
		commandList.add("-o");
		
		if(!mergedFile.endsWith(".tif"))
			mergedFile = mergedFile + ".tif";
		
		commandList.add(pathBuilder(mergedFile));
		commandList.add("-of");
		commandList.add(oFormat);
		commandList.add("-co");
		commandList.add("SPARSE_OK=TRUE");
		
		
		/*
		String inSourcePath = inList.get(0);
		inSourcePath = inSourcePath.replace("\\", "/");
		String inPath = inSourcePath.substring(0,inSourcePath.lastIndexOf("/"));
		*/
		
		boolean isLargeResamples = false;
		
		if ( inList.size() > 1500 )
			isLargeResamples = true;
		
		if(!isLargeResamples){
			for (int i = 0; i < inList.size(); i++ ){
				//String inFile = inList.get(i).replace("\\", "/");
				//String[] inPaths = inFile.split("/");
				//String inSourceFile = inPaths[inPaths.length-1];
				//commandList.add(inSourceFile);
				commandList.add(inList.get(i));
			}
		}
		else{
			commandList1.addAll(commandList.subList(0, commandList.size()));
			
			File folder = new File(inPath+"temp");
			if (! folder.exists()){
				folder.mkdir();
			}
			
			for (int i = 0; i < inList.size(); i++ ){
				//String inFile = inList.get(i).replace("\\", "/");
				String inFile = inList.get(i);
				File oldFile = new File(inPath+"/"+inFile);
				String fName = inFile.substring(0,inFile.indexOf(outputFormat));
				File newFile = new File(folder+"/"+fName);
				try{
					//oldFile.renameTo(newfile);					
					FileUtils.copyFile(oldFile, newFile); 
					commandList.add(fName);
					commandList1.add(inFile);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
		String outputStr = "";
		String executionPath = inPath;
		if (isLargeResamples)
			executionPath = inPath+"temp";
		
		if (isRun)
			outputStr = executeMergeCommand(commandList,executionPath);
		else
			outputStr = "commands";
				
		String folderLoc = mergedFile.substring(0, mergedFile.lastIndexOf("/"));
		
		String outputCommandFile = folderLoc +"/merge.txt";
		String outputStringFile = folderLoc +"/merge_log.txt";
				
		
		String command = toCommand(commandList);
		if(isLargeResamples)
			command = toCommand(commandList1);
		
		writeOutputCommand(outputCommandFile,command);
		writeOutputLog(outputStringFile, command, outputStr);
		
		if(isLargeResamples){
			try{
				FileUtils.cleanDirectory(new File(inPath+"temp"));
				FileUtils.deleteDirectory(new File(inPath+"temp"));
			}
			catch (Exception e)
			{
				
			}
			
		}		
		
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
		
		String folderLoc = destFile.substring(0, destFile.lastIndexOf("/"));
		
		String outputStr = executeCommand(commandList);
				
		String outputCommandFile = folderLoc +"/calc.txt";
		String outputLogFile = folderLoc +"/calc_log.txt";
		
		
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
		commandList.add("fieldname");
		commandList.add(Constants.RANK);
		

		String outputStr = executeCommand(commandList);
		
		String folderLoc = destFile.substring(0, destFile.lastIndexOf("/"));
		String outputCommandFile = folderLoc +"/polygolize.txt";
		String outputLogFile = folderLoc +"/polygolize_log.txt";
		

		String command = toCommand(commandList);
		writeOutputCommand(outputCommandFile,command);
		writeOutputLog(outputLogFile, command, outputStr);
		
		return destFile;						
	}
	
	public static String ClipRaster(String srcClipFile, String srcTifFile, String destTifFile, 
			boolean overWrite, boolean tap, String xRes, String yRes, String nData, String woName, String woValue,
			String cWhere)
	{
		
		srcClipFile = srcClipFile.replace("\\", "/");
		srcTifFile = srcTifFile.replace("\\", "/");
		destTifFile = destTifFile.replace("\\", "/");
		
		List<String> commandList = new ArrayList<String>();
		commandList.add("gdalwarp");
		if(!woName.isEmpty() && !woValue.isEmpty()){
			commandList.add("-wo");
			commandList.add(pathBuilder(woName+"="+woValue));
		}
		if (tap)
			commandList.add("-tap");
		if (overWrite)
			commandList.add("-overwrite");
		if( !xRes.isEmpty() && !yRes.isEmpty() ){
			commandList.add("-tr");
			commandList.add(xRes);
			commandList.add(yRes);
		}
		if (!nData.isEmpty()) {
			commandList.add("-dstnodata");
			commandList.add(nData);
		}
		commandList.add("-cutline");
		commandList.add(pathBuilder(srcClipFile));
		if (!cWhere.isEmpty()) {
			commandList.add("-cwhere");
			String[] cutlineFeatures = cWhere.split("=");
			String name = cutlineFeatures[0].trim();
			int value = Integer.parseInt(cutlineFeatures[1].trim());
			commandList.add("\""+name+" = "+value+"\"");
		}
		commandList.add("-crop_to_cutline");
		commandList.add(pathBuilder(srcTifFile));
		commandList.add(pathBuilder(destTifFile));
		
		String command = toCommand(commandList);		
		String outputStr = executeCommand(commandList);
		    	    	
    	String folderLoc = destTifFile.substring(0, destTifFile.lastIndexOf("/"));
    	
    	String outputCommandFile = folderLoc +"/ClipRaster.txt";
    	String outputLogFile = folderLoc +"/ClipRaster_log.txt";
    	    	
    	writeOutputCommand(outputCommandFile,toCommand(commandList));
    	writeOutputLog(outputLogFile, command, outputStr);
    	    	
		return destTifFile;	
		
	}
	
	public static String Rasterize(String srcShpFile, String outFileLoc,  String xRes, String yRes,
			String burn, String attr, String noDataValue, String outputType, String oFormat,  
			boolean tap, boolean isRun)
	{
		
		srcShpFile = srcShpFile.replace("\\", "/");
		outFileLoc = outFileLoc.replace("\\", "/");
		String[] inPaths = srcShpFile.split("/");
		String inFileName = inPaths[inPaths.length-1];
		//String[] file = inFileName.split(".");
		String fileName = inFileName.substring(0, inFileName.length()-4) + outputFormat;
		String outFile = outFileLoc + "/" + fileName;
		
		List<String> commandList = new ArrayList<String>();
		commandList.add("gdal_rasterize");
		if (!burn.isEmpty()) {
			commandList.add("-a");
			commandList.add(attr);
		}
		if (!attr.isEmpty()) {
			commandList.add("-burn");
			commandList.add(burn);
		}
		commandList.add("-of");
		commandList.add(oFormat);
		commandList.add("-a_nodata");
		commandList.add(noDataValue);
		commandList.add("-tr");
		commandList.add(xRes);
		commandList.add(yRes);
		if (tap)
			commandList.add("-tap");
		commandList.add("-ot");
		commandList.add(outputType);
		commandList.add(pathBuilder(srcShpFile));
		commandList.add(pathBuilder(outFile));
		
		String outputStr = "";
		
		if (isRun)
			outputStr = executeCommand(commandList);
		else
			outputStr = "commands";
				
		String outputStringFile = outFileLoc +"/rasterize_log.txt";
    	String outputCommandFile = outFileLoc +"/commands.txt";
    	
    	String command = toCommand(commandList); 
    	writeOutputCommand(outputCommandFile, command);
    	writeOutputLog(outputStringFile, command, outputStr);
    	
		return outFile;	
		
	}
	
	
	public static String Proximity(String srcRaster, String outFileLoc, 
			String noDataValue, String outputType, String oFormat, String distUnit, 
			boolean isRun)
	{
		srcRaster = srcRaster.replace("\\", "/");
		outFileLoc = outFileLoc.replace("\\", "/");
		String[] inPaths = srcRaster.split("/");
		String inFileName = inPaths[inPaths.length-1];
		//String[] file = inFileName.split(".");
		String fileName = inFileName.substring(0, inFileName.length()-4) + "_proximity" + outputFormat;
		String outFile = outFileLoc + "/" + fileName;
		String gdalPath = getGdalPath();
		
		List<String> commandList = new ArrayList<String>();
		if (gdalPath.length() != 0)
			commandList.add("python");
		commandList.add(pathBuilder(gdalPath+"gdal_proximity.py"));	
		commandList.add(pathBuilder(srcRaster));
		commandList.add(pathBuilder(outFile));
		commandList.add("-of");
		commandList.add(oFormat);
		commandList.add("-ot");
		commandList.add(outputType);
		commandList.add("-distunits");
		commandList.add(distUnit);
		commandList.add("-nodata");
		commandList.add(noDataValue);
	
		String outputStr = "";
		
		if (isRun)
			outputStr = executeCommand(commandList);
		else
			outputStr = "commands";
				
		String outputStringFile = outFileLoc +"/proximity_log.txt";
    	String outputCommandFile = outFileLoc +"/commands.txt";
    	
    	String command = toCommand(commandList); 
    	writeOutputCommand(outputCommandFile, command);
    	writeOutputLog(outputStringFile, command, outputStr);
    	
		return outFile;	
		
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
	
	private static String executeMergeCommand(List<String> commandList, String location) {
		
		String os = System.getProperty("os.name");
		String importPath = "export PATH=/Library/Frameworks/GDAL.framework/Programs:$PATH";
		
		if ( !os.startsWith("Windows") ){
			try {				
				Process p1 = null;
				p1 = Runtime.getRuntime().exec(importPath);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		File f = new File(location);
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
	
	
	public static String MergeShapeFiles(List<String> shapeFiles)
	{
		String inSourcePath = shapeFiles.get(0);
		inSourcePath = inSourcePath.replace("\\", "/");
		String inPath = inSourcePath.substring(0,inSourcePath.lastIndexOf("/"));	
		String os = System.getProperty("os.name");
		
					
		if ( os.startsWith("Windows") ){						 						
			for (int i = 0; i < shapeFiles.size(); i++ ) {
				String inFile = shapeFiles.get(i).replace("\\", "/");
				String[] inPaths = inFile.split("/");
				String inSourceFile = inPaths[inPaths.length-1];
				String command = "ogr2ogr -f \"ESRI Shapefile\" ";
						
				if ( i == 0 )
					 command = command + mergedFileName + " " + inSourceFile;
				else
					command = command + "-update -append " + mergedFileName + " " + inSourceFile + " -nln Merged";
								
				Process p;
				try {
					p = Runtime.getRuntime().exec(command,null,new File(inPath));
					p.waitFor();
					//output += "Merged file " + inSourceFile + " \n";					
				} catch (Exception e) {
					e.printStackTrace();
				}												
			}								 
		}
		else{
			String importPath = "export PATH=/Library/Frameworks/GDAL.framework/Programs:$PATH";
			try {				
				Process p1 = null;
				p1 = Runtime.getRuntime().exec(importPath);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			String command = "for f in *.shp; do ogr2ogr -update -append " + mergedFileName + " $f -f \"ESRI Shapefile\"; done;";
			try {
				Process p = Runtime.getRuntime().exec(command,null,new File(inPath));
				p.waitFor();
				//output += "Merged file\n";					
			} catch (Exception e) {
				e.printStackTrace();
			}					
		}
		
		return inPath+"/"+mergedFileName;
					
	}	
	
	/*
	public static void main (String args[]) throws IOException
	{
		//File folder = new File("C:/Scratch/gadata/Resample/BrisbaneCityCouncil2009");
		//String newPath = "C:/Scratch/gadata/Resample/6";
		//File[] listOfFiles = folder.listFiles();
		File folder = new File("C:/Scratch/gadata/Resample/6");
		File[] listOfFiles = folder.listFiles();
		List <String> fList = new ArrayList<String>();
		
		for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		    	  //File oldFile = new File("C:/Scratch/gadata/Resample/BrisbaneCityCouncil2009/" + listOfFiles[i].getName());
		    	  //File newFille = new File(newPath+"/"+i+".tif");
		    	  fList.add(listOfFiles[i].getName());
		    	  //FileUtils.copyFile(oldFile, newFille);
		      }
		}
		
		MergeRasters(fList, "C:/Scratch/gadata/Resample/6", "C:/Scratch/gadata/temp/a.tif" , "Float32", "-340282346638529993179660072199368212480.000", "GTiff", true);
	}
	*/
	
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
