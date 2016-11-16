package org.knime.gdalutils;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.geotools.coverage.grid.Interpolator2D;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.Geometries;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.linemerge.LineMerger;

import org.knime.geoutils.*;

class OverlapInfo {
	
	public Geometry geometry;
	public int rank1;
	public int rank2;
	public int index1;
	public int index2;
	public int ovid;
	
	public OverlapInfo(){}
	
	public OverlapInfo(Geometry g, int r1, int r2, int i1, int i2){
		this.geometry = g;
		this.rank1 = r1;
		this.rank2 = r2;
		this.index1 = i1;
		this.index2 = i2;				
	}	
}

class OverlapPoint{	
	public int x;
	public int y;
	public int z;	
}

class SurveyLine{
	int index;
	LineString ls;
}

class TrippleOverlap{
	
	public Geometry geometry;
	public OverlapPoint p;			
}

class ClippedSurvey{
	public Geometry geometry;
	public int ovid;
}


public class TestClass {
	
	public static void main (String args[])
	{
		//String shpFile = "C:/Scratch/GA work/Simple Case Study/Source Data/coastal.shp";
		String shpFile = "C:/Scratch/gadata/mask/mergedsurveys.shp";
		
		SimpleFeatureCollection collection = 
        		ShapeFileFeatureExtractor.getShapeFeature(shpFile);
	
		SimpleFeatureType type = collection.getSchema();
			
		SimpleFeatureIterator iterator = collection.features();
		ArrayList<SimpleFeature> features = new ArrayList<SimpleFeature>();
		
		
		//--5. Survey polygons to lines
		ArrayList<SurveyLine> surveyLines = new ArrayList<SurveyLine>();		
		while (iterator.hasNext()) {
			SimpleFeature feature = iterator.next(); 
			features.add(feature);		
			MultiPolygon  mp = (MultiPolygon) feature.getDefaultGeometry();
			int n = mp.getNumGeometries();		
			if (n==1){
				Polygon poly = (Polygon) mp.getGeometryN(0);
				LineString linestring = poly.getExteriorRing();
				SurveyLine sLine = new SurveyLine();
				sLine.ls = linestring;
				sLine.index = (int) feature.getAttribute("Index");
				surveyLines.add(sLine);
			}
		}
		
												
		//--1. Overlap regions and --2. Overlap multipolygons to polygons
		
		/*
		SimpleFeatureType t1 = null;
		try{
			t1 = DataUtilities.createType("Ploygon",
	                "the_geom:Polygon:srid=4326," +
	                "Index1:Integer," +   
	                "Index2:Integer," +
	                "Rank1:Integer," +
	                "Rank2:Integer," +
	                "ovid:Integer"
	        );																					
		}
		catch (Exception e){
			e.printStackTrace();
		}		
		List<SimpleFeature> overlaps = new ArrayList<SimpleFeature>();
		*/
			
		ArrayList<OverlapInfo> overlaps = new ArrayList<OverlapInfo>();				
		int ovid = 1;
		
		for (int i = 0; i < collection.size(); i++ ){
			for (int j = i+1; j < collection.size(); j++ ){
				SimpleFeature feature1 = features.get(i);
				SimpleFeature feature2 = features.get(j);
				int k1 = (int) feature1.getAttribute("Index");
				int k2 = (int) feature2.getAttribute("Index");		
				Geometry geo1 = (Geometry)feature1.getDefaultGeometryProperty().getValue();
				Geometry geo2 = (Geometry)feature2.getDefaultGeometryProperty().getValue();				
				if (  (k1 < k2)  && (geo1.intersects(geo2)) && (!geo1.touches(geo2)) ){														
					Geometry geo = geo1.intersection(geo2);						
					Geometries geomType = Geometries.get(geo);					
					if (geomType == Geometries.POLYGON){														
						int r1 = Integer.parseInt(feature1.getAttribute("Rank").toString());
						int r2 = Integer.parseInt(feature2.getAttribute("Rank").toString());
						/*
						SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(t1);
						featureBuilder.add(geo);
						featureBuilder.add(k1);
						featureBuilder.add(k2);
						featureBuilder.add(r1);
						featureBuilder.add(r2);
						featureBuilder.add(ovid);
						SimpleFeature feature = featureBuilder.buildFeature(null);
						overlaps.add(feature);
						*/																													
						OverlapInfo oi = new OverlapInfo();
						oi.rank1 = r1;
						oi.rank2 = r2;
						oi.index1 = k1;
						oi.index2 = k2;
						oi.geometry = geo;
						oi.ovid = ovid;
						overlaps.add(oi);
						ovid++;
						//GeometryJSON json = new GeometryJSON();
						//System.out.println(json.toString(geo));						
					}										
				}				
			}					
		}
		
		/*
		FeatureCollection<SimpleFeatureType, SimpleFeature> ovFeatures = new ListFeatureCollection(t1,overlaps);
		File file = new File("C:/Scratch/gadata/overlapend/overlap.shp");
		WriteShapefile writer = new WriteShapefile(file);
		writer.writeFeatures(ovFeatures);
		*/
				
		//--3. Calculate triple overlaps and --4. Find tripleoverlap centroids
		
		ArrayList<TrippleOverlap> trippleOverlaps = new ArrayList<TrippleOverlap>();			
		
		for (int a = 0; a < overlaps.size(); a++ ){
			for (int b = a+1; b < overlaps.size(); b++ ){
				for (int c = b+1; c < overlaps.size(); c++ ){
					
					if ( (overlaps.get(a).index1 == overlaps.get(b).index1) 
							&& (overlaps.get(a).index2 == overlaps.get(c).index1) 
							&& (overlaps.get(b).index2 == overlaps.get(c).index2) 
							&& (overlaps.get(a).geometry.intersects(overlaps.get(b).geometry)) ){
						
						OverlapPoint op = new OverlapPoint();
						op.x = overlaps.get(a).ovid;
						op.y = overlaps.get(b).ovid;
						op.z = overlaps.get(c).ovid;						
							
						TrippleOverlap to = new TrippleOverlap();
						to.p = op;
						to.geometry = overlaps.get(a).geometry.intersection(overlaps.get(b).geometry);
						trippleOverlaps.add(to);						
					}										
				}
			}			
		}
		
		
		
		//--6. Clip surveylines to overlap areas 
		ArrayList<ClippedSurvey> clippedsurveys1 = new ArrayList<ClippedSurvey>();				
		for (int a=0; a < surveyLines.size(); a++ ){
			for (int b=0; b < overlaps.size(); b++ ){				
				if (surveyLines.get(a).index == overlaps.get(b).index2 ){
					Geometry g1 = overlaps.get(b).geometry.buffer(0.01);					
					if ( surveyLines.get(a).ls.intersects(g1)) {
						Geometry g2 = surveyLines.get(a).ls.intersection(g1);					
						LineMerger merger = new LineMerger();
						merger.add(g2);
						Collection lines = merger.getMergedLineStrings();
						Geometry g3 =  g2.getFactory().buildGeometry(lines);					
						ClippedSurvey cs = new ClippedSurvey();
						cs.geometry = g3;
						cs.ovid = overlaps.get(b).ovid;
						clippedsurveys1.add(cs);
					}
					
				}
			}
		}
		
		
		ArrayList<ClippedSurvey> clippedsurveys2 = new ArrayList<ClippedSurvey>();	
		for (int i = 0; i < clippedsurveys1.size(); i++ ){
			Geometry g = clippedsurveys1.get(i).geometry;
			Geometries geomType = Geometries.get(g);					
			if (geomType == Geometries.MULTILINESTRING){	
				MultiLineString ms = (MultiLineString)g;
				for (int j = 0; j < ms.getNumGeometries(); j++ ){
					ClippedSurvey cs = new ClippedSurvey();
					cs.geometry = ms.getGeometryN(j);
					cs.ovid = clippedsurveys1.get(i).ovid;
					clippedsurveys1.add(cs);
					
				}								
			}
			else{
				
				clippedsurveys2.add(clippedsurveys1.get(i));
			}
			
		}
		
		
		/*
		 if (g3 != null) {
						double lengthLine = g3.getLength();
						System.out.println(lengthLine);
						if ( lengthLine > longestLine ){
							longest = g3;
							pointOvid = overlaps.get(b).ovid;
							//cs.geometry = g3;
							//cs.ovid = overlaps.get(b).ovid;	
							//
						}
					}
		 */
		
			
		/*
		LineString ss = (LineString)longest;
		Point startPoint = ss.getStartPoint();
		Point endPoint = ss.getEndPoint();
		*/
		
		//--6.1. Find longest line in any remaining multilines		 			
				
		
		
		//System.out.println(overlaps.size());
		//System.out.println(surveyLines.size());
		//System.out.println(trippleOverlaps.size());
		System.out.println(clippedsurveys1.size());	
		
		/*
		String shpFile1 = "C:/Scratch/gadata/STAGE5/2overlap.shp";
		
		SimpleFeatureCollection collection1 = 
        		ShapeFileFeatureExtractor.getShapeFeature(shpFile1);
	
		System.out.println(collection1.size());
		*/
		
		
		
		
				
		//System.out.println(features.size());
					
		//System.out.println(count);
		//System.out.println(type.getCoordinateReferenceSystem().toWKT());
		
		/*
		String shpFile1 = "C:/Scratch/gadata/STAGE5/5surveylines.shp";
		
		SimpleFeatureCollection collection1 = 
        		ShapeFileFeatureExtractor.getShapeFeature(shpFile1);
		
		System.out.println(collection1.size());
	
		SimpleFeatureType type1 = collection1.getSchema();		
			
		SimpleFeatureIterator iterator1 = collection1.features();
		
		int k=0;
		try{
			PrintWriter writer = new PrintWriter("C:/Scratch/gadata/1.txt", "UTF-8");
			
			while ( iterator1.hasNext() ){
				SimpleFeature f = iterator1.next();
				Geometry g = (Geometry) f.getDefaultGeometryProperty().getValue();
				GeometryJSON json = new GeometryJSON();
				System.out.println(json.toString(g));
				k++;
				writer.println(json.toString(g));
			}
		}
		catch(Exception e)
		{
			
		}
		*/
		
		
		
		
	}
	

}
