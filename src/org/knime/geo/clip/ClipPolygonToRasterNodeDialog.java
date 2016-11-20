package org.knime.geo.clip;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.geo.mosaic.MosaicNodeModel;
import org.knime.geo.resample.ResampleNodeModel;

/**
 * <code>NodeDialog</code> for the "ClipPolygonToRaster" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Forkan
 */
public class ClipPolygonToRasterNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the ClipPolygonToRaster node.
     */
    protected ClipPolygonToRasterNodeDialog() {
    	
    	
    	DialogComponentBoolean tapSelection = 
    			new DialogComponentBoolean ( new SettingsModelBoolean(ClipPolygonToRasterNodeModel.TAP,false), "(Target Aligned Pixels");
    	
    	DialogComponentLabel wrapOption = new DialogComponentLabel("Wrap option");
    	
    	DialogComponentString woNameText = new DialogComponentString(
    			new SettingsModelString(ClipPolygonToRasterNodeModel.WO_NAME,""), "Name");
    	
    	DialogComponentString woValueText = new DialogComponentString(
    			new SettingsModelString(ClipPolygonToRasterNodeModel.WO_VALUE,""), "Value");
    
    	DialogComponentLabel resoluation = new DialogComponentLabel("Output File Resolution");
    	
    	DialogComponentString xres = new DialogComponentString(
    			new SettingsModelString(ClipPolygonToRasterNodeModel.XRES,""), "X resolution");
    	
    	DialogComponentString yres = new DialogComponentString(
    			new SettingsModelString(ClipPolygonToRasterNodeModel.YRES,""), "Y resolution");
    	
    	DialogComponentString cwhereText = new DialogComponentString(
    			new SettingsModelString(ClipPolygonToRasterNodeModel.CWHERE,""), "Attribute query for cutline");
    	
    	
    	DialogComponentBoolean overWriteSelection = 
    			new DialogComponentBoolean ( new SettingsModelBoolean(ClipPolygonToRasterNodeModel.OR,true), "Overwrite");
    	
    	
    	addDialogComponent(tapSelection);
    	addDialogComponent(wrapOption);
    	addDialogComponent(woNameText);
    	addDialogComponent(woValueText);
    	addDialogComponent(resoluation);
    	addDialogComponent(xres);
    	addDialogComponent(yres);
    	addDialogComponent(cwhereText);
    	addDialogComponent(overWriteSelection);
    	
    	/*
    	DialogComponentFileChooser outputPath = 
    			new DialogComponentFileChooser(new SettingsModelString(ClipPolygonToRasterNodeModel.OUTFILE,""), 
    					ClipPolygonToRasterNodeModel.OUTFILE, JFileChooser.OPEN_DIALOG);
    	
    	addDialogComponent(outputPath);
    	*/

    }
}

