package org.knime.geoutils;

import java.awt.Color;
import java.io.File;

import org.geotools.data.FeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.styling.JSimpleStyleDialog;
import org.geotools.swt.control.ExceptionMonitor;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

public class StyleLab {
	
	static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
    static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);
    
    public static Style createStyle(File file, FeatureSource featureSource) {
        File sld = toSLDFile(file);
        if (sld != null) {
            return createFromSLD(sld);
        }

        SimpleFeatureType schema = (SimpleFeatureType)featureSource.getSchema();
        return JSimpleStyleDialog.showDialog(null, schema);
    }
 
    public static File toSLDFile(File file)  {
        String path = file.getAbsolutePath();
        String base = path.substring(0,path.length()-4);
        String newPath = base + ".sld";
        File sld = new File( newPath );
        if( sld.exists() ){
            return sld;
        }
        newPath = base + ".SLD";
        sld = new File( newPath );
        if( sld.exists() ){
            return sld;
        }
        return null;
    }
 
    public static Style createFromSLD(File sld) {
        try {
            SLDParser stylereader = new SLDParser(styleFactory, sld.toURI().toURL());
            Style[] style = stylereader.readXML();
            return style[0];
            
        } catch (Exception e) {
            ExceptionMonitor.show(null, e, "Problem creating style");
        }
        return null;
    }
 	
    public static Style createStyle2(FeatureSource featureSource) {
        SimpleFeatureType schema = (SimpleFeatureType)featureSource.getSchema();
        Class geomType = schema.getGeometryDescriptor().getType().getBinding();

        if (Polygon.class.isAssignableFrom(geomType)
                || MultiPolygon.class.isAssignableFrom(geomType)) {
            return createPolygonStyle();

        } else if (LineString.class.isAssignableFrom(geomType)
                || MultiLineString.class.isAssignableFrom(geomType)) {
            return createLineStyle();

        } else {
            return createPointStyle();
        }
    }
 	
    public static Style createPolygonStyle() {

         // create a partially opaque outline stroke
         Stroke stroke = styleFactory.createStroke(
                 filterFactory.literal(Color.BLUE),
                 filterFactory.literal(1),
                 filterFactory.literal(0.5));

         // create a partial opaque fill
         Fill fill = styleFactory.createFill(
                 filterFactory.literal(Color.CYAN),
                 filterFactory.literal(0.5));

         /*
          * Setting the geometryPropertyName arg to null signals that we want to
          * draw the default geomettry of features
          */
         PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);

         Rule rule = styleFactory.createRule();
         rule.symbolizers().add(sym);
         FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
         Style style = styleFactory.createStyle();
         style.featureTypeStyles().add(fts);

         return style;
     }
 	 
    public static Style createLineStyle() {
         Stroke stroke = styleFactory.createStroke(
                 filterFactory.literal(Color.BLUE),
                 filterFactory.literal(1));

         /*
          * Setting the geometryPropertyName arg to null signals that we want to
          * draw the default geomettry of features
          */
         LineSymbolizer sym = styleFactory.createLineSymbolizer(stroke, null);

         Rule rule = styleFactory.createRule();
         rule.symbolizers().add(sym);
         FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
         Style style = styleFactory.createStyle();
         style.featureTypeStyles().add(fts);

         return style;
     }
 	 
    public static Style createPointStyle() {
 	        Graphic gr = styleFactory.createDefaultGraphic();

 	        Mark mark = styleFactory.getCircleMark();

 	        mark.setStroke(styleFactory.createStroke(
 	                filterFactory.literal(Color.BLUE), filterFactory.literal(1)));

 	        mark.setFill(styleFactory.createFill(filterFactory.literal(Color.CYAN)));

 	        gr.graphicalSymbols().clear();
 	        gr.graphicalSymbols().add(mark);
 	        gr.setSize(filterFactory.literal(5));

 	        /*
 	         * Setting the geometryPropertyName arg to null signals that we want to
 	         * draw the default geomettry of features
 	         */
 	        PointSymbolizer sym = styleFactory.createPointSymbolizer(gr, null);

 	        Rule rule = styleFactory.createRule();
 	        rule.symbolizers().add(sym);
 	        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
 	        Style style = styleFactory.createStyle();
 	        style.featureTypeStyles().add(fts);

 	        return style;
 	    }

}
