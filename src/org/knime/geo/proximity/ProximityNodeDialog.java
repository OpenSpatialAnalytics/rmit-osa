package org.knime.geo.proximity;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.geo.mosaic.OutputType;
import org.knime.geo.rasterize.RasterizeNodeModel;
import org.knime.geo.resample.OutputFormat;

/**
 * <code>NodeDialog</code> for the "Proximity" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Forkan
 */
public class ProximityNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring Proximity node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected ProximityNodeDialog() {
          	
		DialogComponentStringSelection outputTypeSelectDialog = new DialogComponentStringSelection(
				new SettingsModelString(ProximityNodeModel.OT,OutputType.Byte.toString()),
				"Output Type",OutputTypes());
		
		DialogComponentStringSelection outputFormatSelectDialog = new DialogComponentStringSelection(
				new SettingsModelString(ProximityNodeModel.OF,OutputFormat.GTiff.toString()),
				"Output Format",OutputFormats());
		
		DialogComponentStringSelection distanceUnitSelectDialog = new DialogComponentStringSelection(
				new SettingsModelString(ProximityNodeModel.DT,DistanceUnit.GEO.toString()),
				"Distance Unit",DistanceUnits());
		
    	DialogComponentString nodata = new DialogComponentString(
    			new SettingsModelString(ProximityNodeModel.ND,""), "No Data Value");
    	      	
    	DialogComponentFileChooser outputPath = 
    			new DialogComponentFileChooser(new SettingsModelString(ProximityNodeModel.OUTPATH,""), 
    					ProximityNodeModel.OUTPATH, JFileChooser.SAVE_DIALOG, true);
    	
    	outputPath.setBorderTitle("Output Folder location");
    	
    	DialogComponentBoolean runCommandDialog = 
    			new DialogComponentBoolean ( new SettingsModelBoolean(ProximityNodeModel.RC,false), "Run commands");
    	
    	addDialogComponent(outputTypeSelectDialog);
    	addDialogComponent(outputFormatSelectDialog);
    	addDialogComponent(distanceUnitSelectDialog);
    	addDialogComponent(nodata);    	
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
    
    
    private String[] DistanceUnits()
    {
    	DistanceUnit[] methods = DistanceUnit.values();
		String[] names = new String[methods.length];
		
		for (int i = 0; i < methods.length; i++) {
	        names[i] = methods[i].toString();
	    }
		
		return names;
    }
    
    
}

