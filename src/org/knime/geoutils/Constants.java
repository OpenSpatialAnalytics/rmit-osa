package org.knime.geoutils;

import org.geotools.geojson.geom.GeometryJSON;

import com.vividsolutions.jts.geom.Geometry;

public class Constants {
	
	public static String GEOM = "the_geom";
	public static String RANK = "Rank";
	public static String INDEX = "Index";
	public static String OVID = "ovid";
	
	public static boolean isGeometry(String str)
	{
		try {
			Geometry g = new GeometryJSON().read(str);
			if ( g instanceof Geometry )
				return true;
			else
				return false;
		}
		catch(Exception e)
		{
			return false;
		}
	}

}
