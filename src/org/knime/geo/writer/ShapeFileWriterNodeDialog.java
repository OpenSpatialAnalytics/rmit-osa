package org.knime.geo.writer;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "ShapeFileWriter" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Forkan
 */
public class ShapeFileWriterNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the ShapeFileWriter node.
     */
    protected ShapeFileWriterNodeDialog() {
    	
    	DialogComponentString projDialog = new DialogComponentString(
    			new SettingsModelString(ShapeFileWriterNodeModel.PROJ,""), "Projection srid value");
    	
    	addDialogComponent(projDialog);
    	
    	DialogComponentFileChooser outPath = new DialogComponentFileChooser(
  		      new SettingsModelString(ShapeFileWriterNodeModel.CFG_LOC,""),
  		      ShapeFileWriterNodeModel.CFG_LOC,
  		      JFileChooser.SAVE_DIALOG,
  		     	".shp");
    	
    	outPath.setBorderTitle("Output Shapefile");
    	
    	addDialogComponent(outPath);
                      

    }
}

