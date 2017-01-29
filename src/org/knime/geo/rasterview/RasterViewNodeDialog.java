package org.knime.geo.rasterview;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.geo.reader.ShapeFileReaderNodeModel;

/**
 * <code>NodeDialog</code> for the "RasterView" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author 
 */
public class RasterViewNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the RasterView node.
     */
    protected RasterViewNodeDialog() {
    	
    	addDialogComponent(new DialogComponentFileChooser(
    		      new SettingsModelString(RasterViewNodeModel.RASTER,""),
    		      RasterViewNodeModel.RASTER,
    		      JFileChooser.OPEN_DIALOG,
    		     	".tif"));
    	
    	addDialogComponent(new DialogComponentFileChooser(
  		      new SettingsModelString(RasterViewNodeModel.SHP,""),
  		      RasterViewNodeModel.SHP,
  		      JFileChooser.OPEN_DIALOG,
  		     	".shp"));

    }
}

