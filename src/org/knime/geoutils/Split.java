package org.knime.geoutils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateArrays;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory; 
import com.vividsolutions.jts.geom.LineString; 
import com.vividsolutions.jts.geom.MultiLineString; 
import com.vividsolutions.jts.geom.MultiPolygon; 
import com.vividsolutions.jts.geom.Point; 
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.distance.DistanceOp;
import com.vividsolutions.jts.operation.distance.GeometryLocation; 
import com.vividsolutions.jts.operation.polygonize.Polygonizer; 
import com.vividsolutions.jts.operation.union.UnaryUnionOp;  
import java.util.ArrayList; 
import java.util.Collection; 
import java.util.Collections; 
import java.util.LinkedList; 
import java.util.List; 
 
/**
 * This function split a line by a line a line by a point a polygon by a line 
 * 
 * @author Erwan Bocher 
 */ 
public class Split { 
 
    private static final GeometryFactory FACTORY = new GeometryFactory(); 
    public static final double PRECISION = 10E-6; 
    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(); 
 
    public Split() { 
     
    } 
 
 
    /**
     * Split a geometry a according a geometry b. Supported operations are : 
     * split a line by a line a line by a point a polygon by a line. 
     * 
     * A default tolerance of 10E-6 is used to snap the cutter point. 
     * 
     * @param geomA 
     * @param geomB 
     * @return 
     * @throws SQLException 
     */ 
    public static Geometry split(Geometry geomA, Geometry geomB) throws Exception { 
        if(geomA == null||geomB == null){ 
            return null; 
        } 
        if (geomA instanceof Polygon) { 
            return splitPolygonWithLine((Polygon) geomA, (LineString) geomB); 
        }  
        else if(geomA instanceof MultiPolygon){ 
            return splitMultiPolygonWithLine((MultiPolygon)geomA, (LineString) geomB); 
        } 
        else if (geomA instanceof LineString) { 
            if (geomB instanceof LineString) { 
                return splitLineStringWithLine((LineString) geomA, (LineString) geomB); 
            } else if (geomB instanceof Point) { 
                return splitLineWithPoint((LineString) geomA, (Point) geomB, PRECISION); 
            } 
        } else if (geomA instanceof MultiLineString) { 
            if (geomB instanceof LineString) { 
                return splitMultiLineStringWithLine((MultiLineString) geomA, (LineString) geomB); 
            } else if (geomB instanceof Point) { 
                return splitMultiLineStringWithPoint((MultiLineString) geomA, (Point) geomB, PRECISION); 
            } 
        } 
        throw new Exception("Split a " + geomA.getGeometryType() + " by a " + geomB.getGeometryType() + " is not supported."); 
    } 
 
    /**
     * Split a geometry a according a geometry b using a snapping tolerance. 
     * 
     * This function support only the operations : 
     * 
     * - split a line or a multiline with a point. 
     * 
     * @param geomA the geometry to be splited 
     * @param geomB the geometry used to split 
     * @param tolerance a distance tolerance to snap the split geometry 
     * @return 
     * @throws java.sql.SQLException 
     */ 
    public static Geometry split(Geometry geomA, Geometry geomB, double tolerance) throws Exception { 
        if (geomA instanceof Polygon) { 
            throw new Exception("Split a Polygon by a line is not supported using a tolerance. \n" 
                    + "Please used ST_Split(geom1, geom2)"); 
        } else if (geomA instanceof LineString) { 
            if (geomB instanceof LineString) { 
                throw new Exception("Split a line by a line is not supported using a tolerance. \n" 
                        + "Please used ST_Split(geom1, geom2)"); 
            } else if (geomB instanceof Point) { 
                return splitLineWithPoint((LineString) geomA, (Point) geomB, tolerance); 
            } 
        } else if (geomA instanceof MultiLineString) { 
            if (geomB instanceof LineString) { 
                throw new Exception("Split a multiline by a line is not supported using a tolerance. \n" 
                        + "Please used ST_Split(geom1, geom2)"); 
            } else if (geomB instanceof Point) { 
                return splitMultiLineStringWithPoint((MultiLineString) geomA, (Point) geomB, tolerance); 
            } 
        } 
        throw new Exception("Split a " + geomA.getGeometryType() + " by a " + geomB.getGeometryType() + " is not supported."); 
    } 
 
    /**
     * Split a linestring with a point The point must be on the linestring 
     * 
     * @param line 
     * @param pointToSplit 
     * @return 
     */ 
    private static MultiLineString splitLineWithPoint(LineString line, Point pointToSplit, double tolerance) { 
        return FACTORY.createMultiLineString(splitLineStringWithPoint(line, pointToSplit, tolerance)); 
    } 
 
    /**
     * Splits a LineString using a Point, with a distance tolerance. 
     * 
     * @param line 
     * @param pointToSplit 
     * @param tolerance 
     * @return 
     */ 
    private static LineString[] splitLineStringWithPoint(LineString line, Point pointToSplit, double tolerance) { 
        Coordinate[] coords = line.getCoordinates(); 
        Coordinate firstCoord = coords[0]; 
        Coordinate lastCoord = coords[coords.length - 1]; 
        Coordinate coordToSplit = pointToSplit.getCoordinate(); 
        if ((coordToSplit.distance(firstCoord) <= PRECISION) || (coordToSplit.distance(lastCoord) <= PRECISION)) { 
            return new LineString[]{line}; 
        } else { 
            ArrayList<Coordinate> firstLine = new ArrayList<Coordinate>(); 
            firstLine.add(coords[0]); 
            ArrayList<Coordinate> secondLine = new ArrayList<Coordinate>(); 
            GeometryLocation geometryLocation = getVertexToSnap(line, pointToSplit, tolerance); 
            if (geometryLocation != null) { 
                int segmentIndex = geometryLocation.getSegmentIndex(); 
                Coordinate coord = geometryLocation.getCoordinate(); 
                int index = -1; 
                for (int i = 1; i < coords.length; i++) { 
                    index = i - 1; 
                    if (index < segmentIndex) { 
                        firstLine.add(coords[i]); 
                    } else if (index == segmentIndex) { 
                        coord.z = interpolate(coords[i - 1], coords[i], coord); 
                        firstLine.add(coord); 
                        secondLine.add(coord); 
                        if (!coord.equals2D(coords[i])) { 
                            secondLine.add(coords[i]); 
                        } 
                    } else { 
                        secondLine.add(coords[i]); 
                    } 
                } 
                LineString lineString1 = FACTORY.createLineString(firstLine.toArray(new Coordinate[firstLine.size()])); 
                LineString lineString2 = FACTORY.createLineString(secondLine.toArray(new Coordinate[secondLine.size()])); 
                return new LineString[]{lineString1, lineString2}; 
            } 
        } 
        return null; 
    } 
 
    /**
     * Splits a MultilineString using a point. 
     * 
     * @param multiLineString 
     * @param pointToSplit 
     * @param tolerance 
     * @return 
     */ 
    private static MultiLineString splitMultiLineStringWithPoint(MultiLineString multiLineString, Point pointToSplit, double tolerance) { 
        ArrayList<LineString> linestrings = new ArrayList<LineString>(); 
        boolean notChanged = true; 
        int nb = multiLineString.getNumGeometries(); 
        for (int i = 0; i < nb; i++) { 
            LineString subGeom = (LineString) multiLineString.getGeometryN(i); 
            LineString[] result = splitLineStringWithPoint(subGeom, pointToSplit, tolerance); 
            if (result != null) { 
                Collections.addAll(linestrings, result); 
                notChanged = false; 
            } else { 
                linestrings.add(subGeom); 
            } 
        } 
        if (!notChanged) { 
            return FACTORY.createMultiLineString(linestrings.toArray(new LineString[linestrings.size()])); 
        } 
        return null; 
    } 
 
    /**
     * Splits a Polygon with a LineString. 
     * 
     * @param polygon 
     * @param lineString 
     * @return 
     */ 
    private static Collection<Polygon> splitPolygonizer(Polygon polygon, LineString lineString) throws Exception { 
        LinkedList<LineString> result = new LinkedList<LineString>(); 
        createSegments(polygon.getExteriorRing(), result); 
        result.add(lineString); 
        int holes = polygon.getNumInteriorRing(); 
        for (int i = 0; i < holes; i++) { 
            createSegments(polygon.getInteriorRingN(i), result); 
        } 
        // Perform union of all extracted LineStrings (the edge-noding process)   
        UnaryUnionOp uOp = new UnaryUnionOp(result); 
        Geometry union = uOp.union(); 
 
        // Create polygons from unioned LineStrings   
        Polygonizer polygonizer = new Polygonizer(); 
        polygonizer.add(union); 
        Collection<Polygon> polygons = polygonizer.getPolygons(); 
 
        if (polygons.size() > 1) { 
            return polygons; 
        } 
        return null; 
    } 
 
    /**
     * Splits a Polygon using a LineString. 
     * 
     * @param polygon 
     * @param lineString 
     * @return 
     */ 
    private static Geometry splitPolygonWithLine(Polygon polygon, LineString lineString) throws Exception { 
        Collection<Polygon> pols = polygonWithLineSplitter(polygon, lineString); 
        if (pols != null) { 
            return FACTORY.buildGeometry(polygonWithLineSplitter(polygon, lineString)); 
        } 
        return null; 
    } 
 
    /**
     * Splits a Polygon using a LineString.  
     * 
     * @param polygon 
     * @param lineString 
     * @return 
     */ 
    private static Collection<Polygon> polygonWithLineSplitter(Polygon polygon, LineString lineString) throws Exception { 
        Collection<Polygon> polygons = splitPolygonizer(polygon, lineString); 
        if (polygons != null && polygons.size() > 1) { 
            List<Polygon> pols = new ArrayList<Polygon>(); 
            for (Polygon pol : polygons) { 
                if (polygon.contains(pol.getInteriorPoint())) { 
                    pols.add(pol); 
                } 
            } 
            return pols; 
        } 
        return null; 
    } 
 
    /**
     * Splits a MultiPolygon using a LineString. 
     * 
     * @param multiPolygon 
     * @param lineString 
     * @return 
     */ 
    private static Geometry splitMultiPolygonWithLine(MultiPolygon multiPolygon, LineString lineString) throws Exception { 
        ArrayList<Polygon> allPolygons = new ArrayList<Polygon>(); 
        for (int i = 0; i < multiPolygon.getNumGeometries(); i++) { 
            Collection<Polygon> polygons = splitPolygonizer((Polygon) multiPolygon.getGeometryN(i), lineString); 
            if (polygons != null) { 
                allPolygons.addAll(polygons); 
            } 
        } 
        if (!allPolygons.isEmpty()) { 
            return FACTORY.buildGeometry(allPolygons); 
        } 
        return null; 
    } 
 
    /**
     * Splits the specified lineString with another lineString. 
     * 
     * @param lineString 
     * @param lineString 
     * 
     */ 
    private static Geometry splitLineStringWithLine(LineString input, LineString cut) { 
        return input.difference(cut); 
    } 
 
    /**
     * Splits the specified MultiLineString with another lineString. 
     *  
     * @param MultiLineString 
     * @param lineString 
     * 
     */ 
    private static Geometry splitMultiLineStringWithLine(MultiLineString input, LineString cut) { 
        Geometry lines = input.difference(cut); 
        //Only to preserve SQL constrains 
        if (lines instanceof LineString) { 
            return FACTORY.createMultiLineString(new LineString[]{(LineString) lines.getGeometryN(0)}); 
        } 
        return lines; 
    } 
    
    private static GeometryLocation getVertexToSnap(Geometry g, Point p, double tolerance) { 
        DistanceOp distanceOp = new DistanceOp(g, p); 
        GeometryLocation snapedPoint = distanceOp.nearestLocations()[0]; 
        if (tolerance == 0 || snapedPoint.getCoordinate().distance(p.getCoordinate()) <= tolerance) { 
            return snapedPoint; 
        } 
        return null; 
 
    } 
    
    private static void createSegments(final Geometry geom, 
            final List<LineString> result) throws Exception { 
    		if (geom instanceof LineString) { 
    			createSegments((LineString) geom, result); 
    		} else if (geom instanceof Polygon) { 
    			createSegments((Polygon) geom, result); 
    		} else if (geom instanceof GeometryCollection) { 
    			createSegments((GeometryCollection) geom, result); 
    		} 
    } 

    private static void createSegments(final LineString geom, 
            final List<LineString> result) throws Exception { 
    	Coordinate[] coords = CoordinateArrays.removeRepeatedPoints(geom.getCoordinates()); 
    	for (int j = 0; j < coords.length - 1; j++) { 
    		LineString lineString = GEOMETRY_FACTORY.createLineString( 
    				new Coordinate[]{coords[j], coords[j + 1]}); 
    		result.add(lineString); 
    	} 
    } 

    private static void createSegments(final Polygon polygon, 
            final List<LineString> result) throws Exception { 
    	createSegments(polygon.getExteriorRing(), result); 
    	for (int i = 0; i < polygon.getNumInteriorRing(); i++) { 
    		createSegments(polygon.getInteriorRingN(i), result); 
    	} 
    } 

    private static void createSegments(final GeometryCollection geometryCollection, 
            final List<LineString> result) throws Exception { 
    	for (int i = 0; i < geometryCollection.getNumGeometries(); i++) { 
    		createSegments(geometryCollection.getGeometryN(i), result); 
    	} 
    } 
    
    public static double interpolate(Coordinate firstCoordinate, Coordinate lastCoordinate, Coordinate toBeInterpolated) { 
        if (Double.isNaN(firstCoordinate.z)) { 
                return Double.NaN; 
        } 
        if (Double.isNaN(lastCoordinate.z)) { 
                return Double.NaN; 
        } 
        return firstCoordinate.z + (lastCoordinate.z - firstCoordinate.z) * firstCoordinate.distance(toBeInterpolated) 
                / (firstCoordinate.distance(toBeInterpolated) + toBeInterpolated.distance(lastCoordinate)); 
    } 
    
}