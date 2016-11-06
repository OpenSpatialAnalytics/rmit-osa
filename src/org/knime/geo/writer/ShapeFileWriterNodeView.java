package org.knime.geo.writer;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.knime.core.node.NodeView;
import org.knime.geoutils.StyleLab;
import org.knime.geoutils.WriteShapefile;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * <code>NodeView</code> for the "ShapeFileWriter" Node.
 * 
 *
 * @author Forkan
 */
public class ShapeFileWriterNodeView extends NodeView<ShapeFileWriterNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link ShapeFileWriterNodeModel})
     */
    protected ShapeFileWriterNodeView(final ShapeFileWriterNodeModel nodeModel) {
        super(nodeModel);
        String fname= nodeModel.shpFileLoc.getStringValue().concat(".shp");
        
        try {
    		
    		File file = new File(fname);
    		
			Map<String, URL> map1 = new HashMap<String, URL>();      
			map1.put("url", file.toURI().toURL());
		
			FileDataStore store = FileDataStoreFinder.getDataStore(file);
		
	        @SuppressWarnings("rawtypes")
			FeatureSource featureSource = store.getFeatureSource();

	        MapContent map = new MapContent();
	        
	        Style style = StyleLab.createStyle2(featureSource);
	        Layer layer = new FeatureLayer(featureSource, style);
	        map.addLayer(layer);
	        
	        JMapFrame jf = new JMapFrame(map);
	        jf.setTitle("Map View");
	        jf.setVisible(true);
	        jf.setAlwaysOnTop(true);
	        jf.setSize(640,480);	
	        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize(); 
	        jf.setLocation(dim.width/2-jf.getSize().width/2, dim.height/2-jf.getSize().height/2);
	        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    	
	        	        
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {
        // TODO: generated method stub
    }

}

