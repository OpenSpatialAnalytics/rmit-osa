package org.knime.geoutils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.cql2.CQL;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;


public class ShapeFileFeatureExtractor {
	
	/***
	 * 
	 * @param path of shapefile
	 * @return return the whole shape file as SimpleCollection
	 */
	public static SimpleFeatureCollection getShapeFeature(String fileName)
	{
		SimpleFeatureCollection collection;
		
		try {
    		
    		File file = new File(fileName);
			
			Map<String, URL> map = new HashMap<String, URL>();      
			map.put("url", file.toURI().toURL());
	
			DataStore dataStore = DataStoreFinder.getDataStore(map);
			String typeName = dataStore.getTypeNames()[0];
			
			SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
			collection = featureSource.getFeatures();
			
			dataStore.dispose();
			
			return collection;
			
    	}catch (Exception e) {
			e.printStackTrace();
			return null;
		} 	
	}
	
	public static SimpleFeatureCollection getGeometryFeature(String fileName) 
	{
		SimpleFeatureCollection collection;
		
		try {
    		
    		File file = new File(fileName);
			
			Map<String, URL> map = new HashMap<String, URL>();      
			map.put("url", file.toURI().toURL());
	
			DataStore dataStore = DataStoreFinder.getDataStore(map);
			String typeName = dataStore.getTypeNames()[0];
			
			SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
			FeatureType schema = featureSource.getSchema();
	        String name = schema.getGeometryDescriptor().getLocalName();
	        Query query = new Query(typeName, Filter.INCLUDE, new String[] { name });
			collection = featureSource.getFeatures(query);
			
			dataStore.dispose();
			
			return collection;
			
    	}catch (Exception e) {
			e.printStackTrace();
			return null;
		} 	
	}
	
	public static SimpleFeatureCollection getGeometryFeature(String fileName, String filterQuery)
	{
		SimpleFeatureCollection collection;
		
		try {
    		
    		File file = new File(fileName);
			
			Map<String, URL> map = new HashMap<String, URL>();      
			map.put("url", file.toURI().toURL());
	
			DataStore dataStore = DataStoreFinder.getDataStore(map);
			String typeName = dataStore.getTypeNames()[0];
			
			SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
			FeatureType schema = featureSource.getSchema();
	        String name = schema.getGeometryDescriptor().getLocalName();
	        
	        Filter filter = CQL.toFilter(filterQuery);
	        
	        Query query = new Query(typeName, filter, new String[] { name });
			collection = featureSource.getFeatures(query);
			
			dataStore.dispose();
			
			return collection;
			
    	}catch (Exception e) {
			e.printStackTrace();
			return null;
		} 	
	}
	
	
	/*
	public static void main (String args[])
	{
		
		String elevband = "E:\\GA Project\\Simple Case Study\\Source Data\\reserves.shp";
		SimpleFeatureCollection collection1 = getShapeFeature(elevband);
		SimpleFeatureType type = collection1.getSchema();
		
		for (AttributeType t : type.getTypes()) {
			if (t == type.getGeometryDescriptor().getType()) {
				System.out.println(type.getGeometryDescriptor().getLocalName().toString());
			}
			
			else{
			
				String name = t.getName().toString();
	
				if (t.getBinding() == Integer.class) {
					System.out.println("Integer: " + name);
				} else if (t.getBinding() == Double.class) {
					System.out.println("Double: " + name);
				} else if (t.getBinding() == Boolean.class) {
					System.out.println("Boolean: " + name);
				} else {
					System.out.println("String: " + name);
				}
			}
		}
		System.out.println(type.getTypes().size());
	}*/

}
