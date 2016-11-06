package org.knime.geo.resample;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.geo.operators.GeometryOperationNodeModel;
import org.knime.geo.writer.ShapeFileWriterNodeModel;

/**
 * <code>NodeDialog</code> for the "Resample" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Forkan
 */
public class ResampleNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the Resample node.
     */
    protected ResampleNodeDialog() {
    	
    	DialogComponentStringSelection resampleMethodSelectDialog = new DialogComponentStringSelection(
    			new SettingsModelString(ResampleNodeModel.RM,ReSampleMethod.AVG.toString()),
    			"Resample Method",ResampleMethods());
    	
    	DialogComponentString workingMemory = new DialogComponentString(
    			new SettingsModelString(ResampleNodeModel.WM,"500"), "Working memoty");
    	
    	DialogComponentLabel resoluation = new DialogComponentLabel("Output File Resolution");
    	
    	
    	DialogComponentString xres = new DialogComponentString(
    			new SettingsModelString(ResampleNodeModel.XRES,"25"), "X resolution");
    	
    	DialogComponentString yres = new DialogComponentString(
    			new SettingsModelString(ResampleNodeModel.YRES,"25"), "Y resolution");
    	
    	
    	DialogComponentStringSelection outputFormatSelectDialog = new DialogComponentStringSelection(
    			new SettingsModelString(ResampleNodeModel.OF,OutputFormat.GTiff.toString()),
    			"Output Format",OutputFormats());
    	
    	/*
    	DialogComponentFileChooser inputPath = 
    			new DialogComponentFileChooser(new SettingsModelString(ResampleNodeModel.INPATH,""), 
    					ResampleNodeModel.INPATH, JFileChooser.OPEN_DIALOG,".zip");
    					*/
    	
    	DialogComponentBoolean overWriteSelection = 
    			new DialogComponentBoolean ( new SettingsModelBoolean(ResampleNodeModel.OR,true), "Overwrite");
    	

    	
    	DialogComponentFileChooser outputPath = 
    			new DialogComponentFileChooser(new SettingsModelString("output_path",""), 
    					ResampleNodeModel.OUTPATH, JFileChooser.SAVE_DIALOG, true);
    	
    	outputPath.setBorderTitle("Output location of resampled files");
    	
    	
    	addDialogComponent(resampleMethodSelectDialog);
    	addDialogComponent(workingMemory);
    	addDialogComponent(resoluation);
    	addDialogComponent(xres);
    	addDialogComponent(yres);
    	addDialogComponent(outputFormatSelectDialog);
    	addDialogComponent(overWriteSelection);
    	addDialogComponent(outputPath);

    }
    
    private String[] ResampleMethods()
    {
    	ReSampleMethod[] methods = ReSampleMethod.values();
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

