package org.knime.geo.mask;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.geo.mosaic.MosaicNodeModel;
import org.knime.geo.mosaic.OutputType;
import org.knime.geo.resample.OutputFormat;

/**
 * <code>NodeDialog</code> for the "MaskRaster" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Forkan
 */
public class MaskRasterNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the MaskRaster node.
     */
    protected MaskRasterNodeDialog() {
    	
    	DialogComponentStringSelection maskTypeSelectDialog = new DialogComponentStringSelection(
    			new SettingsModelString(MaskRasterNodeModel.MT,MaskType.Byte.toString()),
    			"Type",MaskTypes());
    	    	
    	
    	DialogComponentFileChooser outputPathDialog = 
    			new DialogComponentFileChooser(new SettingsModelString(MaskRasterNodeModel.OUTPATH,""), 
    					MaskRasterNodeModel.OUTPATH, JFileChooser.SAVE_DIALOG, true);
    	
    	outputPathDialog.setBorderTitle("Destination Directory");
    	
    	/*
    	DialogComponentString expreDialog = new DialogComponentString(
    			new SettingsModelString(MaskRasterNodeModel.EXPR,"A"), "Calculation Expression");
    			*/
    	
    	addDialogComponent(maskTypeSelectDialog);
    	//addDialogComponent(expreDialog);
    	addDialogComponent(outputPathDialog);

    }
    
    
    private String[] MaskTypes()
    {
    	MaskType[] methods = MaskType.values();
		String[] names = new String[methods.length];
		
		for (int i = 0; i < methods.length; i++) {
	        names[i] = methods[i].toString();
	    }
		
		return names;
    }
}

