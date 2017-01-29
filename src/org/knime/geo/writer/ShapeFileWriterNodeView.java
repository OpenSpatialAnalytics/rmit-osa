package org.knime.geo.writer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.JMapPane;
import org.knime.core.node.NodeView;
import org.knime.geoutils.StyleLab;
import org.knime.geoutils.WriteShapefile;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import net.miginfocom.swing.MigLayout;

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
	        
	        JMapPane mapPane = new JMapPane(map);
	        mapPane.setBackground(Color.WHITE);
	        StringBuilder sb = new StringBuilder();
	        sb.append("[grow]");
	        JPanel panel = new JPanel(new MigLayout("wrap 1, insets 0", "[grow]",  sb.toString()));
	        panel.add(mapPane,"grow");
	        panel.setPreferredSize(new Dimension(640, 480));
	        panel.setMaximumSize(panel.getPreferredSize()); 
	        panel.setMinimumSize(panel.getPreferredSize());
	        setComponent(panel);
	        
	    	
	        	        
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

