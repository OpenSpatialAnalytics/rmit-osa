package org.knime.geo.mosaic;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.geo.resample.OutputFormat;
import org.knime.geo.resample.ResampleNodeModel;

/**
 * <code>NodeDialog</code> for the "Mosaic" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author 
 */
public class MosaicNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the Mosaic node.
     */
    protected MosaicNodeDialog() {
    	
    	DialogComponentStringSelection outputTypeSelectDialog = new DialogComponentStringSelection(
    			new SettingsModelString(MosaicNodeModel.OT,OutputType.Float32.toString()),
    			"Output Type",OutputTypes());
    	
    	DialogComponentStringSelection outputFormatSelectDialog = new DialogComponentStringSelection(
    			new SettingsModelString(MosaicNodeModel.OF,OutputFormat.GTiff.toString()),
    			"Output Format",OutputFormats());
    	
    	DialogComponentFileChooser outputPath = 
    			new DialogComponentFileChooser(new SettingsModelString(MosaicNodeModel.OUTPATH,""), 
    					MosaicNodeModel.OUTPATH, JFileChooser.SAVE_DIALOG, true);
    	
    	outputPath.setBorderTitle("Distination Directory");
    	
    	
    	DialogComponentString mergeFileNameDialog = new DialogComponentString(
    			new SettingsModelString(MosaicNodeModel.MF,""), "Merged File Name");
 
    	
    	DialogComponentBoolean runCommandDialog = 
    			new DialogComponentBoolean ( new SettingsModelBoolean(MosaicNodeModel.RC,false), "Run commands");
    	
    	addDialogComponent(outputTypeSelectDialog);
    	addDialogComponent(outputFormatSelectDialog);
    	addDialogComponent(outputPath);
    	addDialogComponent(mergeFileNameDialog);
    	addDialogComponent(runCommandDialog);
    	
    }
    
    private String[] OutputTypes()
    {
    	OutputType[] methods = OutputType.values();
		String[] names = new String[methods.length];
		
		for (int i = 0; i < methods.length; i++) {
	        names[i] = methods[i].toString();
	    }
		
		return names;
    }
    
    private String[] OutputFormats()
    {
    	OutputFormat[] methods = OutputFormat.values();
		String[] names = new String[methods.length];
		
		for (int i = 0; i < methods.length; i++) {
	        names[i] = methods[i].toString();
	    }
		
		return names;
    }
}

