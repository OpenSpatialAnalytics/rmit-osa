package org.knime.geo.shpmerge;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "ShapeMerge" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author 
 */
public class ShapeMergeNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the ShapeMerge node.
     */
    protected ShapeMergeNodeDialog() {
    	
    	
    	DialogComponentFileChooser outPath = new DialogComponentFileChooser(
    		      new SettingsModelString(ShapeMergeNodeModel.FILE_LOC,""),
    	  		    ShapeMergeNodeModel.FILE_LOC,
    	  		      JFileChooser.SAVE_DIALOG,
    	  		     	".shp");
    	
    	outPath.setBorderTitle("Output Shapefile");
    	
    	addDialogComponent(outPath);
    }
}

