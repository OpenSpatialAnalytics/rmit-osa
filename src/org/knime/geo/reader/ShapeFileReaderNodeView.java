package org.knime.geo.reader;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.MapContext;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.SLD;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.JMapPane;
import org.geotools.swing.styling.JSimpleStyleDialog;
import org.geotools.swt.control.ExceptionMonitor;
import org.knime.core.node.NodeView;
import org.knime.geoutils.StyleLab;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;

import org.opengis.geometry.Envelope;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * <code>NodeView</code> for the "ShapeFileReader" Node.
 * Read a shapefile
 *
 * @author Forkan
 */
public class ShapeFileReaderNodeView extends NodeView<ShapeFileReaderNodeModel> {
	
	
    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link ShapeFileReaderNodeModel})
     */
    @SuppressWarnings("deprecation")
    protected ShapeFileReaderNodeView(final ShapeFileReaderNodeModel nodeModel) {
        super(nodeModel);
        //this.closeView();
        //this.getComponent().setVisible(false);
     
        String fname= nodeModel.getShapeFileName();
        try {
    		
    		File file = new File(fname);
			
			Map<String, URL> map1 = new HashMap<String, URL>();      
			map1.put("url", file.toURI().toURL());
		
			FileDataStore store = FileDataStoreFinder.getDataStore(file);
	        @SuppressWarnings("rawtypes")
			FeatureSource featureSource = store.getFeatureSource();

	        // Create a map context and add our shapefile to it
	       
	        MapContent map = new MapContent();
	        //map.setTitle("StyleLab");

	        // Create a basic Style to render the features
	        //Style style = createStyle(file, featureSource);

	        // Add the features and the associated Style object to
	        // the MapContext as a new MapLayer
	        //map.addLayer(featureSource, style);
			
			//Style style = SLD.createSimpleStyle(featureSource.getSchema());
	        Style style = StyleLab.createStyle2(featureSource);
	        Layer layer = new FeatureLayer(featureSource, style);
	        map.addLayer(layer);
	        
	        /*
	        JPanel jp = new JPanel();
	        jp.setPreferredSize(new Dimension(600, 600));
	        JButton zoomInButton = new JButton("Zoom In");
	        jp.add(zoomInButton);
	        setComponent(jp);
	        */

	        // Now display the map
	        //JMapFrame.showMap(map)
	        
	        
	        JMapFrame jf = new JMapFrame(map);
	        jf.setTitle("Map View");
	        jf.setVisible(true);
	        jf.setAlwaysOnTop(true);
	        jf.setSize(640,480);	
	        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	        jf.setLocation(dim.width/2-jf.getSize().width/2, dim.height/2-jf.getSize().height/2);
	        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        //map.dispose();
	        	        
	        //jf.showMap(map);
	   	    //setComponent( jf.getContentPane());
	   	     
	
	       
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        
   
   
        //window.setVisible(true);
        // TODO instantiate the components of the view here.

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {

        // TODO retrieve the new model from your nodemodel and 
        // update the view.
        ShapeFileReaderNodeModel nodeModel = 
            (ShapeFileReaderNodeModel)getNodeModel();
        assert nodeModel != null;
        
        // be aware of a possibly not executed nodeModel! The data you retrieve
        // from your nodemodel could be null, emtpy, or invalid in any kind.
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
    
        // TODO things to do when closing the view
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {

        // TODO things to do when opening the view
    }

}

