package org.knime.geo.clip;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.geo.mosaic.MosaicNodeModel;

/**
 * <code>NodeDialog</code> for the "ClipPolygonToRaster" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Forkan
 */
public class ClipPolygonToRasterNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the ClipPolygonToRaster node.
     */
    protected ClipPolygonToRasterNodeDialog() {
    	
    	/*
    	DialogComponentFileChooser outputPath = 
    			new DialogComponentFileChooser(new SettingsModelString(ClipPolygonToRasterNodeModel.OUTFILE,""), 
    					ClipPolygonToRasterNodeModel.OUTFILE, JFileChooser.OPEN_DIALOG);
    	
    	addDialogComponent(outputPath);
    	*/

    }
}

