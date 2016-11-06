package org.knime.geo.calc;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentMultiLineString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.geo.mosaic.MosaicNodeModel;

/**
 * <code>NodeDialog</code> for the "GdalCalc" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Forkan
 */
public class GdalCalcNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the GdalCalc node.
     */
    protected GdalCalcNodeDialog() {
    	
    	DialogComponentFileChooser outputPathDialogue = 
    			new DialogComponentFileChooser(new SettingsModelString (GdalCalcNodeModel.OUTPATH,""), 
    				GdalCalcNodeModel.OUTPATH, JFileChooser.SAVE_DIALOG,true);
    	
    	outputPathDialogue.setBorderTitle("Locatin of calculated raster");
    	
    	
    	DialogComponentMultiLineString expressaionDialogue = 
    			new DialogComponentMultiLineString (new SettingsModelString (GdalCalcNodeModel.EXPR,""),"Logical expression");
    	
    	addDialogComponent(outputPathDialogue);
    	addDialogComponent(expressaionDialogue);
    	
    }
}

