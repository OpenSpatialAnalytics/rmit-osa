package org.knime.geo.resample;

import javax.swing.JFileChooser;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.util.ColumnFilter;
import org.knime.gdalutils.Utility;
import org.knime.geo.combine.CombineNodeModel;
import org.knime.geo.operators.GeometryOperationNodeModel;
import org.knime.geo.writer.ShapeFileWriterNodeModel;
import org.knime.geoutils.Constants;

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
    			new SettingsModelString(ResampleNodeModel.WM,"500"), "Working memory");
    	
    	DialogComponentString sSRSDialog = new DialogComponentString(
    			new SettingsModelString(ResampleNodeModel.S_SRS,""), "Source Spatial Reference Set");
    	
    	DialogComponentString tSRSDialog = new DialogComponentString(
    			new SettingsModelString(ResampleNodeModel.T_SRS,""), "Target Spatial Reference Set");
    	
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
      	DialogComponentBoolean tapSelection = 
    			new DialogComponentBoolean ( new SettingsModelBoolean(ResampleNodeModel.TAP,false), "Target Aligned Pixels");
    	
    	DialogComponentBoolean overWriteSelection = 
    			new DialogComponentBoolean ( new SettingsModelBoolean(ResampleNodeModel.OR,true), "Overwrite");
    	
    	
    	DialogComponentFileChooser outputPath = 
    			new DialogComponentFileChooser(new SettingsModelString("output_path",""), 
    					ResampleNodeModel.OUTPATH, JFileChooser.SAVE_DIALOG, true);
    	
    	outputPath.setBorderTitle("Output Folder Location");
    	

    	DialogComponentStringSelection directoryFormatSelectDialog = new DialogComponentStringSelection(
    			new SettingsModelString(ResampleNodeModel.DF,DirectoryFormat.MainDir.toString()),
    			"File Naming",DirectoryFOrmats());
    	
    	DialogComponentColumnNameSelection columnSelect = 
    			new DialogComponentColumnNameSelection(new SettingsModelString(ResampleNodeModel.CN,""),
    					"Select Column",0,false,true, filterColumn);
    	
    	DialogComponentBoolean runCommandDialog = 
    			new DialogComponentBoolean ( new SettingsModelBoolean(ResampleNodeModel.RC,false), "Run commands");
    	
    	addDialogComponent(resampleMethodSelectDialog);
    	addDialogComponent(workingMemory);
    	addDialogComponent(sSRSDialog);
    	addDialogComponent(tSRSDialog);
    	addDialogComponent(resoluation);
    	addDialogComponent(xres);
    	addDialogComponent(yres);
    	addDialogComponent(outputFormatSelectDialog);
    	addDialogComponent(tapSelection);
    	addDialogComponent(overWriteSelection);
    	addDialogComponent(outputPath);
    	addDialogComponent(directoryFormatSelectDialog);
    	addDialogComponent(columnSelect);
    	addDialogComponent(runCommandDialog);

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
    
    private String[] DirectoryFOrmats()
    {
    	DirectoryFormat[] methods = DirectoryFormat.values();
		String[] names = new String[methods.length];
		
		for (int i = 0; i < methods.length; i++) {
	        names[i] = methods[i].toString();
	    }
		
		return names;
    }
    
    ColumnFilter filterColumn = new ColumnFilter() {
        @Override
        public boolean includeColumn(DataColumnSpec dataColumnSpec) {
            return !(dataColumnSpec.getName().compareTo(Utility.LOC_COLUMN) == 0);
        }

		@Override
		public String allFilteredMsg() {
			// TODO Auto-generated method stub
			return "No column other than Location column is available";
		}
	};
    
}

