package org.knime.geo.reader;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
//import org.geotools.swing.JMapFrame;
import org.geotools.swing.JMapPane;
import org.geotools.swing.action.InfoAction;
import org.geotools.swing.action.NoToolAction;
import org.geotools.swing.action.PanAction;
import org.geotools.swing.action.ResetAction;
import org.geotools.swing.action.ZoomInAction;
import org.geotools.swing.action.ZoomOutAction;
import org.geotools.swing.control.JMapStatusBar;
import org.knime.core.node.NodeView;
import org.knime.geoutils.StyleLab;
import net.miginfocom.swing.MigLayout;

/**
 * <code>NodeView</code> for the "ShapeFileReader" Node.
 * Read a shapefile
 *
 * @author Forkan
 */
public class ShapeFileReaderNodeView extends NodeView<ShapeFileReaderNodeModel> {
	
	
	 public static final String TOOLBAR_INFO_BUTTON_NAME = "ToolbarInfoButton";
	 public static final String TOOLBAR_PAN_BUTTON_NAME = "ToolbarPanButton";
	 public static final String TOOLBAR_POINTER_BUTTON_NAME = "ToolbarPointerButton";
	 public static final String TOOLBAR_RESET_BUTTON_NAME = "ToolbarResetButton";
	 public static final String TOOLBAR_ZOOMIN_BUTTON_NAME = "ToolbarZoomInButton";
	 public static final String TOOLBAR_ZOOMOUT_BUTTON_NAME = "ToolbarZoomOutButton";
	 
	
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
	        

	        // Now display the map
	        //JMapFrame.showMap(map)
	        
	        /*
	        JMapFrame jf = new JMapFrame(map);
	        jf.setTitle("Map View");
	        jf.setVisible(true);
	        jf.setAlwaysOnTop(true);
	        jf.setSize(640,480);	
	       
	        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	        jf.setLocation(dim.width/2-jf.getSize().width/2, dim.height/2-jf.getSize().height/2);
	        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        */
	        //map.dispose();
	        	        
	        //jf.showMap(map);
	   	    //setComponent( jf.getContentPane());
	        
	        JMapPane mapPane = new JMapPane(map);
	        mapPane.setBackground(Color.WHITE);
	        //mapPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	        
	        /*
	        mapPane.addFocusListener(new FocusAdapter() {

	            @Override
	            public void focusGained(FocusEvent e) {
	                mapPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	            }

	            @Override
	            public void focusLost(FocusEvent e) {
	                mapPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
	            }
	        });
	        
	        mapPane.addMouseListener(new MouseAdapter() {

	            @Override
	            public void mousePressed(MouseEvent e) {
	                mapPane.requestFocusInWindow();
	            }
	        });
	        */
	        
	        StringBuilder sb = new StringBuilder();
	        //sb.append("[]");
	        sb.append("[grow]");
	        //sb.append("[min!]"); 
	       
	        JPanel panel = new JPanel(new MigLayout("wrap 1, insets 0", "[grow]",  sb.toString()));
	        

	        /*
	        JToolBar toolBar = new JToolBar();
            toolBar.setOrientation(JToolBar.HORIZONTAL);
            toolBar.setFloatable(false);
            
            JButton btn;
            ButtonGroup cursorToolGrp = new ButtonGroup();
            
            btn = new JButton(new NoToolAction(mapPane));
            btn.setName(TOOLBAR_POINTER_BUTTON_NAME);
            toolBar.add(btn);
            cursorToolGrp.add(btn);
            
            btn = new JButton(new ZoomInAction(mapPane));
            btn.setName(TOOLBAR_ZOOMIN_BUTTON_NAME);
            toolBar.add(btn);
            cursorToolGrp.add(btn);

            btn = new JButton(new ZoomOutAction(mapPane));
            btn.setName(TOOLBAR_ZOOMOUT_BUTTON_NAME);
            toolBar.add(btn);
            cursorToolGrp.add(btn);

            toolBar.addSeparator();
            
            btn = new JButton(new PanAction(mapPane));
            btn.setName(TOOLBAR_PAN_BUTTON_NAME);
            toolBar.add(btn);
            cursorToolGrp.add(btn);

            toolBar.addSeparator();
            
            btn = new JButton(new InfoAction(mapPane));
            btn.setName(TOOLBAR_INFO_BUTTON_NAME);
            toolBar.add(btn);

            toolBar.addSeparator();
            
            btn = new JButton(new ResetAction(mapPane));
            btn.setName(TOOLBAR_RESET_BUTTON_NAME);
            toolBar.add(btn);
            */
	        
            //panel.add(toolBar, "grow");
            
	        /*
	        JMapFrame frame = new JMapFrame(map);
	        frame.enableToolBar(true);
	        JToolBar toolBar = frame.getToolBar();
	        setComponent(toolBar);
	        panel.add(toolBar, "grow");
	        */
	        panel.add(mapPane,"grow");
	        
	        /*
	        mapLayerTable = new MapLayerTable(mapPane);
            mapLayerTable.setPreferredSize(new Dimension(200, -1));
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
                    false, 
                    mapLayerTable, 
                    mapPane);
            panel.add(splitPane, "grow");
            */
	        
	        //panel.add(JMapStatusBar.createDefaultStatusBar(mapPane), "grow");
	        
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

