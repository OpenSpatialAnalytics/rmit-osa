package org.knime.geo.clipsingle;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.geo.clip.ClipPolygonToRasterNodeModel;

/**
 * <code>NodeDialog</code> for the "ClipARaster" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Forkan
 */
public class ClipARasterNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the ClipARaster node.
     */
    protected ClipARasterNodeDialog() {
    	
    	DialogComponentBoolean tapSelection = 
    			new DialogComponentBoolean ( new SettingsModelBoolean(ClipARasterNodeModel.TAP,false), "(Target Aligned Pixels)");
    	
    	DialogComponentLabel wrapOption = new DialogComponentLabel("Wrap option");
    	
    	DialogComponentString woNameText = new DialogComponentString(
    			new SettingsModelString(ClipARasterNodeModel.WO_NAME,""), "Name");
    	
    	DialogComponentString woValueText = new DialogComponentString(
    			new SettingsModelString(ClipARasterNodeModel.WO_VALUE,""), "Value");
    
    	//DialogComponentLabel resoluation = new DialogComponentLabel("Output File Resolution");
    	
    	DialogComponentString xres = new DialogComponentString(
    			new SettingsModelString(ClipARasterNodeModel.XRES,""), "X resolution");
    	
    	DialogComponentString yres = new DialogComponentString(
    			new SettingsModelString(ClipARasterNodeModel.YRES,""), "Y resolution");
    	
    	DialogComponentString cwhereText = new DialogComponentString(
    			new SettingsModelString(ClipARasterNodeModel.CWHERE,""), "Attribute query for cutline");
    	
    	DialogComponentBoolean overWriteSelection = 
    			new DialogComponentBoolean ( new SettingsModelBoolean(ClipARasterNodeModel.OR,true), "Overwrite");
    	
    	DialogComponentFileChooser shpFileSelect = new DialogComponentFileChooser(
    		      new SettingsModelString(ClipARasterNodeModel.INPATH,""),
    		      ClipARasterNodeModel.INPATH,
      		      JFileChooser.OPEN_DIALOG,
      		     	".shp");
    	
    	shpFileSelect.setBorderTitle("Clip shape file location");
    	
    	DialogComponentFileChooser inputPath = 
    			new DialogComponentFileChooser(new SettingsModelString(ClipARasterNodeModel.SRCPATH,""), 
    					ClipARasterNodeModel.SRCPATH, JFileChooser.OPEN_DIALOG, true);
    	
    	inputPath.setBorderTitle("Input files location");
    	
    	DialogComponentFileChooser outputPath = 
    			new DialogComponentFileChooser(new SettingsModelString(ClipARasterNodeModel.OUTPATH,""), 
    					ClipARasterNodeModel.OUTPATH, JFileChooser.SAVE_DIALOG, true);
    	
    	outputPath.setBorderTitle("Output files location");
    	
    	
    	
    	addDialogComponent(tapSelection);
    	addDialogComponent(wrapOption);
    	addDialogComponent(woNameText);
    	addDialogComponent(woValueText);    	
    	addDialogComponent(xres);
    	addDialogComponent(yres);
    	addDialogComponent(cwhereText);
    	addDialogComponent(overWriteSelection);
    	addDialogComponent(shpFileSelect);
    	addDialogComponent(inputPath);
    	addDialogComponent(outputPath);

    }
}

