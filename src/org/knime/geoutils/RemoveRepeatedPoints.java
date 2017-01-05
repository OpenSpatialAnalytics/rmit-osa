package org.knime.geoutils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateArrays;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import java.util.ArrayList;


public class RemoveRepeatedPoints{

    private static final GeometryFactory FACTORY = new GeometryFactory();

    public RemoveRepeatedPoints() {        
    }
	
    public static Geometry removeRepeatedPoints(Geometry geometry) {
        return removeDuplicateCoordinates(geometry);
    }

    public static Geometry removeDuplicateCoordinates(Geometry geom) {
        if(geom ==null){
            return null;
        }
        else if (geom.isEmpty()) {
            return geom;
        } else if (geom instanceof Point || geom instanceof MultiPoint) {
            return geom;
        } else if (geom instanceof LineString) {
            return removeDuplicateCoordinates((LineString) geom);
        } else if (geom instanceof MultiLineString) {
            return removeDuplicateCoordinates((MultiLineString) geom);
        } else if (geom instanceof Polygon) {
            return removeDuplicateCoordinates((Polygon) geom);
        } else if (geom instanceof MultiPolygon) {
            return removeDuplicateCoordinates((MultiPolygon) geom);
        } else if (geom instanceof GeometryCollection) {
            return removeDuplicateCoordinates((GeometryCollection) geom);
        }
        return null;
    }

    public static LineString removeDuplicateCoordinates(LineString g) {
        Coordinate[] coords = CoordinateArrays.removeRepeatedPoints(g.getCoordinates());
        return FACTORY.createLineString(coords);
    }

    public static LinearRing removeDuplicateCoordinates(LinearRing g) {
        Coordinate[] coords = CoordinateArrays.removeRepeatedPoints(g.getCoordinates());
        return FACTORY.createLinearRing(coords);
    }

    public static MultiLineString removeDuplicateCoordinates(MultiLineString g) {
        ArrayList<LineString> lines = new ArrayList<LineString>();
        for (int i = 0; i < g.getNumGeometries(); i++) {
            LineString line = (LineString) g.getGeometryN(i);
            lines.add(removeDuplicateCoordinates(line));
        }
        return FACTORY.createMultiLineString(GeometryFactory.toLineStringArray(lines));
    }

    public static Polygon removeDuplicateCoordinates(Polygon poly) {
        Coordinate[] shellCoords = CoordinateArrays.removeRepeatedPoints(poly.getExteriorRing().getCoordinates());
        LinearRing shell = FACTORY.createLinearRing(shellCoords);
        ArrayList<LinearRing> holes = new ArrayList<LinearRing>();
        for (int i = 0; i < poly.getNumInteriorRing(); i++) {
            Coordinate[] holeCoords = CoordinateArrays.removeRepeatedPoints(poly.getInteriorRingN(i).getCoordinates());
            holes.add(FACTORY.createLinearRing(holeCoords));
        }
        return FACTORY.createPolygon(shell, GeometryFactory.toLinearRingArray(holes));
    }

    public static MultiPolygon removeDuplicateCoordinates(MultiPolygon g) {
        ArrayList<Polygon> polys = new ArrayList<Polygon>();
        for (int i = 0; i < g.getNumGeometries(); i++) {
            Polygon poly = (Polygon) g.getGeometryN(i);
            polys.add(removeDuplicateCoordinates(poly));
        }
        return FACTORY.createMultiPolygon(GeometryFactory.toPolygonArray(polys));
    }

    public static GeometryCollection removeDuplicateCoordinates(GeometryCollection g) {
        ArrayList<Geometry> geoms = new ArrayList<Geometry>();
        for (int i = 0; i < g.getNumGeometries(); i++) {
            Geometry geom = g.getGeometryN(i);
            geoms.add(removeDuplicateCoordinates(geom));
        }
        return FACTORY.createGeometryCollection(GeometryFactory.toGeometryArray(geoms));
    }
}
