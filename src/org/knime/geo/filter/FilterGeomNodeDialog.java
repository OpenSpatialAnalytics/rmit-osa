package org.knime.geo.filter;

import org.geotools.geometry.jts.Geometries;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.geo.resample.ReSampleMethod;
import org.knime.geo.resample.ResampleNodeModel;

/**
 * <code>NodeDialog</code> for the "FilterGeom" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author 
 */
public class FilterGeomNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the FilterGeom node.
     */
    protected FilterGeomNodeDialog() {
    	
    	DialogComponentStringSelection gemetryTypeSelectDialog = new DialogComponentStringSelection(
    			new SettingsModelString(FilterGeomNodeModel.GT,Geometries.POLYGON.toString()),
    			"Geometry Type",GeometryTypes());
    	
    	addDialogComponent(gemetryTypeSelectDialog);

    }
    
    private String[] GeometryTypes()
    {
    	Geometries[] types = Geometries.values();
		String[] names = new String[types.length];
		
		for (int i = 0; i < types.length; i++) {
	        names[i] = types[i].toString();
	    }
		
		return names;
    }

}

