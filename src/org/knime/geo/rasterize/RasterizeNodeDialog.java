package org.knime.geo.rasterize;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.geo.mosaic.OutputType;
import org.knime.geo.resample.OutputFormat;


/**
 * <code>NodeDialog</code> for the "Rasterize" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Forkan
 */
public class RasterizeNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the Rasterize node.
     */
    protected RasterizeNodeDialog() {
    	
    	DialogComponentLabel resoluation = new DialogComponentLabel("Output File Resolution");
    	
    	DialogComponentString xres = new DialogComponentString(
    			new SettingsModelString(RasterizeNodeModel.XRES,""), "X resolution");
    	
    	DialogComponentString yres = new DialogComponentString(
    			new SettingsModelString(RasterizeNodeModel.YRES,""), "Y resolution");
    	
    	DialogComponentString attr = new DialogComponentString(
    			new SettingsModelString(RasterizeNodeModel.ATTR,""), "Attribute Name");
    	
    	DialogComponentString burn = new DialogComponentString(
    			new SettingsModelString(RasterizeNodeModel.BURN,""), "Burn Value");
    	
    	DialogComponentString nodata = new DialogComponentString(
    			new SettingsModelString(RasterizeNodeModel.ND,""), "No Data Value");
    	
    	DialogComponentStringSelection outputTypeSelectDialog = new DialogComponentStringSelection(
    			new SettingsModelString(RasterizeNodeModel.OT,OutputType.Byte.toString()),
    			"Output Type",OutputTypes());
    	
    	DialogComponentStringSelection outputFormatSelectDialog = new DialogComponentStringSelection(
    			new SettingsModelString(RasterizeNodeModel.OF,OutputFormat.GTiff.toString()),
    			"Output Format",OutputFormats());
      	DialogComponentBoolean tapSelection = 
    			new DialogComponentBoolean ( new SettingsModelBoolean(RasterizeNodeModel.TAP,false), "Target Aligned Pixels");
    	DialogComponentFileChooser outputPath = 
    			new DialogComponentFileChooser(new SettingsModelString(RasterizeNodeModel.OUTPATH,""), 
    					RasterizeNodeModel.OUTPATH, JFileChooser.SAVE_DIALOG, true);
    	
    	outputPath.setBorderTitle("Output Folder location");
    	
    	DialogComponentBoolean runCommandDialog = 
    			new DialogComponentBoolean ( new SettingsModelBoolean(RasterizeNodeModel.RC,false), "Run commands");
    	
    	addDialogComponent(resoluation);
    	addDialogComponent(xres);
    	addDialogComponent(yres);
    	addDialogComponent(attr);
    	addDialogComponent(burn);
    	addDialogComponent(nodata);
    	addDialogComponent(tapSelection);
    	addDialogComponent(outputTypeSelectDialog);
    	addDialogComponent(outputFormatSelectDialog);
    	addDialogComponent(outputPath);
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

