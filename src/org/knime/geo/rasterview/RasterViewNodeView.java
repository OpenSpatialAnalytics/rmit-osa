package org.knime.geo.rasterview;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.knime.core.node.NodeView;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.Parameter;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.StyleLayer;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.JMapPane;
import org.geotools.swing.action.SafeAction;
import org.geotools.swing.data.JParameterListWizard;
import org.geotools.swing.wizard.JWizard;
import org.geotools.util.KVP;
import org.opengis.filter.FilterFactory2;
import org.opengis.style.ContrastMethod;

import net.miginfocom.swing.MigLayout;

/**
 * <code>NodeView</code> for the "RasterView" Node.
 * 
 *
 * @author 
 */
public class RasterViewNodeView extends NodeView<RasterViewNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link RasterViewNodeModel})
     */
	
	private StyleFactory sf = CommonFactoryFinder.getStyleFactory();
    private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    private GridCoverage2DReader reader;
	
    protected RasterViewNodeView(final RasterViewNodeModel nodeModel) {
        super(nodeModel);
       
        String rasterFileName = nodeModel.getRasterFileName();
        String shpFileName =  nodeModel.getShapeFileName();
        
        try{
        	File rasterFile = new File(rasterFileName);
        	File shpFile = null;
        	if(!shpFileName.isEmpty()){
        		shpFile = new File(shpFileName);
        	}
        	
        	AbstractGridFormat format = GridFormatFinder.findFormat( rasterFile );        
        	reader = format.getReader(rasterFile);
        	
        	Style rasterStyle = createGreyscaleStyle(1);
        	
        	MapContent map = new MapContent();
        	map.setTitle("Raster File");
        	Layer rasterLayer = new GridReaderLayer(reader, rasterStyle);
            map.addLayer(rasterLayer);
            
            if (shpFile != null){
            	FileDataStore dataStore = FileDataStoreFinder.getDataStore(shpFile);
                SimpleFeatureSource shapefileSource = dataStore
                        .getFeatureSource();
            	Style shpStyle = SLD.createPolygonStyle(Color.RED, null, 0.0f);
            	 Layer shpLayer = new FeatureLayer(shapefileSource, shpStyle);
                 map.addLayer(shpLayer);
            }
            
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
        catch (Exception e)
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
    
    /*
    private Style createGreyscaleStyle() {
        GridCoverage2D cov = null;
        try {
            cov = reader.read(null);
        } catch (IOException giveUp) {
            throw new RuntimeException(giveUp);
        }
        int numBands = cov.getNumSampleDimensions();
        Integer[] bandNumbers = new Integer[numBands];
        for (int i = 0; i < numBands; i++) { bandNumbers[i] = i+1; }
        Object selection = JOptionPane.showInputDialog(
                frame,
                "Band to use for greyscale display",
                "Select an image band",
                JOptionPane.QUESTION_MESSAGE,
                null,
                bandNumbers,
                1);
        if (selection != null) {
            int band = ((Number)selection).intValue();
            return createGreyscaleStyle(band);
        }
        return null;
    }
    */
    
    private Style createGreyscaleStyle(int band) {
        ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NORMALIZE);
        SelectedChannelType sct = sf.createSelectedChannelType(String.valueOf(band), ce);

        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
        ChannelSelection sel = sf.channelSelection(sct);
        sym.setChannelSelection(sel);

        return SLD.wrapSymbolizers(sym);
    }
    
    private Style createRGBStyle() {
        GridCoverage2D cov = null;
        try {
            cov = reader.read(null);
        } catch (IOException giveUp) {
            throw new RuntimeException(giveUp);
        }
        // We need at least three bands to create an RGB style
        int numBands = cov.getNumSampleDimensions();
        if (numBands < 3) {
            return null;
        }
        // Get the names of the bands
        String[] sampleDimensionNames = new String[numBands];
        for (int i = 0; i < numBands; i++) {
            GridSampleDimension dim = cov.getSampleDimension(i);
            sampleDimensionNames[i] = dim.getDescription().toString();
        }
        final int RED = 0, GREEN = 1, BLUE = 2;
        int[] channelNum = { -1, -1, -1 };
        // We examine the band names looking for "red...", "green...", "blue...".
        // Note that the channel numbers we record are indexed from 1, not 0.
        for (int i = 0; i < numBands; i++) {
            String name = sampleDimensionNames[i].toLowerCase();
            if (name != null) {
                if (name.matches("red.*")) {
                    channelNum[RED] = i + 1;
                } else if (name.matches("green.*")) {
                    channelNum[GREEN] = i + 1;
                } else if (name.matches("blue.*")) {
                    channelNum[BLUE] = i + 1;
                }
            }
        }
        // If we didn't find named bands "red...", "green...", "blue..."
        // we fall back to using the first three bands in order
        if (channelNum[RED] < 0 || channelNum[GREEN] < 0 || channelNum[BLUE] < 0) {
            channelNum[RED] = 1;
            channelNum[GREEN] = 2;
            channelNum[BLUE] = 3;
        }
        // Now we create a RasterSymbolizer using the selected channels
        SelectedChannelType[] sct = new SelectedChannelType[cov.getNumSampleDimensions()];
        ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NORMALIZE);
        for (int i = 0; i < 3; i++) {
            sct[i] = sf.createSelectedChannelType(String.valueOf(channelNum[i]), ce);
        }
        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
        ChannelSelection sel = sf.channelSelection(sct[RED], sct[GREEN], sct[BLUE]);
        sym.setChannelSelection(sel);

        return SLD.wrapSymbolizers(sym);
    }

    
   

}

