package org.knime.geoutils;

import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.jts.FactoryFinder;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

public class ShapeFileOperations {
	
	
	public static Geometry[] allocateGeomList(long listSize)
	{
		Geometry[] geometries = new Geometry[(int) listSize];
		return geometries;
	}
	
	public static GeometryCollection toCollection(Geometry[] geometries)
	{
		@SuppressWarnings("deprecation")
		GeometryFactory factory = FactoryFinder.getGeometryFactory( null );
		return new GeometryCollection(geometries, factory);
	}
	
	public static GeometryCollection intersect(GeometryCollection gc1, GeometryCollection gc2) {
        List<Geometry> ret = new ArrayList<Geometry>();
        int size1=gc1.getNumGeometries();
        int size2=gc2.getNumGeometries();
        for (int i = 0; i < size1; i++) {
        	Geometry g1 = (Geometry)gc1.getGeometryN(i);
        	for ( int j = 0; j < size2; j++ ){
        		Geometry g2 = (Geometry)gc2.getGeometryN(j);
        		Geometry g = g1.intersection(g2);
        		if(! g.isEmpty())
                  ret.add(g);
        	}
        }
        return gc1.getFactory().createGeometryCollection(GeometryFactory.toGeometryArray(ret));
    }
	
	public static GeometryCollection intersection(GeometryCollection gc1, GeometryCollection gc2) {
        List<Geometry> ret = new ArrayList<Geometry>();
        int size=gc1.getNumGeometries();
        for (int i = 0; i < size; i++) {
            Geometry g1 = (Geometry)gc1.getGeometryN(i);
            List<Geometry> partial = intersection(gc2, g1);
            ret.addAll(partial);
        }

        return gc1.getFactory().createGeometryCollection(GeometryFactory.toGeometryArray(ret));
    }
 
	 private static List<Geometry> intersection(GeometryCollection gc, Geometry g) 
	 {
	        List<Geometry> ret = new ArrayList<Geometry>();
	        final int size=gc.getNumGeometries();
	        for (int i = 0; i < size; i++) {
	            Geometry g1 = (Geometry)gc.getGeometryN(i);
	            collect(g1.intersection(g), ret);
	        }
	        return ret;
	 }
  
	 private static void collect(Geometry g, List<Geometry> collector) {
	        if(g instanceof GeometryCollection) {
	            GeometryCollection gc = (GeometryCollection)g;
	            for (int i = 0; i < gc.getNumGeometries(); i++) {
	                Geometry loop = gc.getGeometryN(i);
	                if(! loop.isEmpty())
	                    collector.add(loop);
	            }
	        } else {
	            if(! g.isEmpty())
	                collector.add(g);
	        }
	    }

}
