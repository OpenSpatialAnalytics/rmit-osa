package org.knime.geo.visualizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.JMapPane;
import org.geotools.swing.action.ZoomInAction;
import org.geotools.swing.action.ZoomOutAction;
import org.knime.core.node.NodeView;
import org.knime.geoutils.StyleLab;
import org.knime.geoutils.WriteShapefile;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import net.miginfocom.swing.MigLayout;

/**
 * <code>NodeView</code> for the "GeoMapViewer" Node.
 * 
 *
 * @author Forkan
 */
public class GeoMapViewerNodeView extends NodeView<GeoMapViewerNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link GeoMapViewerNodeModel})
     */
    protected GeoMapViewerNodeView(final GeoMapViewerNodeModel nodeModel) {
        super(nodeModel);

        try {
        	/*
        	String currentDir = System.getProperty("user.dir");
        	currentDir.replaceAll("\\", "/");
        	File dir = new File(currentDir+"/temp");
        	if (!dir.exists())
        		dir.mkdir();
        		*/
        	
    		File file = new File(nodeModel.shpFile.getStringValue());
    		FeatureCollection<SimpleFeatureType, SimpleFeature> features = nodeModel.features;
    		//File file = new File(fname);
			WriteShapefile writer = new WriteShapefile(file);
			writer.writeFeatures(features);
			
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
	        
	        //JMapFrame.showMap(map);
	        
	        /*
	        JMapFrame jf = new JMapFrame(map);
	        jf.setTitle("Map View");
	        //jf.enableToolBar(true);
	        //jf.enableStatusBar(true);
	        jf.setSize(640,480);	
	        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	        jf.setLocation(dim.width/2-jf.getSize().width/2, dim.height/2-jf.getSize().height/2);
	        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        jf.setVisible(true);
	        jf.setAlwaysOnTop(true);
	        */
	        
	    
	        
	    	//file.delete();
	        //map.dispose();
	         
	        
	        /*
	        JMapPane mapPane = new JMapPane(map);
	        Envelope bounds = mapPane.getDisplayArea();
	        System.out.println( bounds.getDimension() );
	        System.out.println(bounds.getMinimum(0));
	        System.out.println(bounds.getMinimum(1));
	        System.out.println(bounds.getMaximum(0));
	        System.out.println(bounds.getMaximum(1));
		       */
	       
	        /*
	        mapPane.setRenderer( new StreamingRenderer() );
	        mapPane.setMapContent(map);
	        
	        CoordinateReferenceSystem crs = CRS.decode("EPSG:3111");
	        double minX = 0;
			double maxX = 600;
			double minY = 0;
			double maxY = 800;
			Envelope newBounds = new ReferencedEnvelope(minX, maxX, minY, maxY, crs);
			mapPane.setDisplayArea(newBounds);
			*/
			
			/*
			JToolBar toolBar = new JToolBar();
			toolBar.setOrientation(JToolBar.HORIZONTAL);
			toolBar.setFloatable(false);
			
			ButtonGroup cursorToolGrp = new ButtonGroup();
			  
			JButton zoomInBtn = new JButton(new ZoomInAction(mapPane));
			toolBar.add(zoomInBtn);
			cursorToolGrp.add(zoomInBtn);
			  
			JButton zoomOutBtn = new JButton(new ZoomOutAction(mapPane));
			toolBar.add(zoomOutBtn);
			cursorToolGrp.add(zoomOutBtn);
			*/
			
			//setComponent(mapPane);
			
			
			
	        	        
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

        // TODO retrieve the new model from your nodemodel and 
        // update the view.
        GeoMapViewerNodeModel nodeModel = 
            (GeoMapViewerNodeModel)getNodeModel();
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

