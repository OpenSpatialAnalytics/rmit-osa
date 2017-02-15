package org.knime.geo.reader;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "ShapeFileReader" Node.
 * Read a shapefile
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Forkan
 */
public class ShapeFileReaderNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring ShapeFileReader node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected ShapeFileReaderNodeDialog() {
        super();
        
        DialogComponentFileChooser inputPath = new DialogComponentFileChooser(
    		      new SettingsModelString(ShapeFileReaderNodeModel.CFG_SHP_FILE,""),
      		      ShapeFileReaderNodeModel.CFG_SHP_FILE,
      		      JFileChooser.OPEN_DIALOG,
      		     	".shp");
        
        inputPath.setBorderTitle("Source Shapefile");
        
        addDialogComponent(inputPath);
                    
    }
}

