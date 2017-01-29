package org.knime.geoutils;

import java.util.ArrayList;
import java.util.List;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.Geometries;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.node.NodeLogger;
import org.knime.geo.reader.ShapeFileReaderNodeModel;
import org.knime.core.data.def.*;
import org.knime.core.data.def.BooleanCell.BooleanCellFactory;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeType;


import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

public class ShapeToKnime {
	
	   private static final NodeLogger logger = NodeLogger
	            .getLogger(ShapeToKnime.class);
	
	public static DataTableSpec[] createSpec(SimpleFeatureCollection collection)
	{
		SimpleFeatureType type = collection.getSchema();
		List<DataColumnSpec> columns = new ArrayList<>();
		
		for (AttributeType t : type.getTypes()) {
			if (t == type.getGeometryDescriptor().getType()) {
				columns.add(new DataColumnSpecCreator(type.getGeometryDescriptor().getLocalName().toString(), 
						StringCell.TYPE).createSpec());
			}
			
			else{
			
				String name = t.getName().toString();
		
				if (t.getBinding() == Integer.class) {
					columns.add(new DataColumnSpecCreator(name, IntCell.TYPE).createSpec());
				}else if (t.getBinding() == Long.class) {
					columns.add(new DataColumnSpecCreator(name, LongCell.TYPE).createSpec());
				} else if (t.getBinding() == Double.class) {
					columns.add(new DataColumnSpecCreator(name, DoubleCell.TYPE).createSpec());
				} else if (t.getBinding() == Boolean.class) {
					columns.add(new DataColumnSpecCreator(name, BooleanCell.TYPE).createSpec());
				} else {
					columns.add(new DataColumnSpecCreator(name, StringCell.TYPE).createSpec());
				}
			}
		}
		
		
		return new DataTableSpec[] { new DataTableSpec(columns.toArray(new DataColumnSpec[0])) };
	}
	
	public static ArrayList<DataCell []> createCell(SimpleFeatureCollection collection)
	{
		SimpleFeatureIterator iterator = collection.features();
		int numOfColums = collection.getSchema().getTypes().size();
		
		ArrayList<DataCell []> cellList = new ArrayList<DataCell []>();
		
		while (iterator.hasNext()) {
			SimpleFeature feature = iterator.next();
			
			DataCell[] cells = new DataCell[numOfColums];
			int column = 0;
			
			for (Property p : feature.getProperties()) {
				
				Object value = p.getValue();
				String str = "";
				GeometryJSON json = new GeometryJSON(Constants.JsonPrecision);

				if (value == null) {
					cells[column] = DataType.getMissingCell();
				} else if (value instanceof Geometry) {
					Geometry geo = (Geometry)value;
					Geometries geomType = Geometries.get(geo);	    			
	    			if (geomType == Geometries.MULTIPOLYGON){	
	    				MultiPolygon  mp = (MultiPolygon)geo;
	    				if (mp.getNumGeometries() == 1){
	    					Polygon poly = (Polygon) mp.getGeometryN(0);
	    					str = json.toString(poly);
	    				}
	    				else{
	    					str = json.toString(mp);
	    				}
	    			}
	    			else{						    				
	    				str = json.toString((Geometry)value);
	    			}	    				
					cells[column] = new StringCell(str);
				} else if (value instanceof Integer) {
					cells[column] = new IntCell((Integer) p.getValue());
				} else if (value instanceof Long){
					cells[column] = new LongCell((Long) p.getValue());
				} else if (value instanceof Double) {
					cells[column] = new DoubleCell((Double) p.getValue());
				} else if (value instanceof Boolean) {
					cells[column] = BooleanCellFactory.create((Boolean) p.getValue());
				} else if (p.getValue().toString().isEmpty()) {
					cells[column] = DataType.getMissingCell();
				} else {
					cells[column] = new StringCell(p.getValue().toString());
				}
				column++;
			}	
			cellList.add(cells);
		}
		
		return cellList;
	}
	
	/*
	public static void main (String args[])
	{
		String elevband = "E:\\GA Project\\Simple Case Study\\Source Data\\reserves.shp";
		SimpleFeatureCollection collection = ShapeFileFeatureExtractor.getShapeFeature(elevband);
		
		DataTableSpec spec = createSpec(collection)[0];
		System.out.println("fdsfdsfs");
		System.out.println(spec.getColumnSpec(1).getType().toString());
		
	}
	*/

}
