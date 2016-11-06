package org.knime.geo.operators;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "GeometryOperation" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author 
 */
public class GeometryOperationNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the GeometryOperation node.
     */
    protected GeometryOperationNodeDialog() {
    	String operations[] = {"Union","Intersect","Difference"};
    	
    	DialogComponentStringSelection operatorSelectDialog = new DialogComponentStringSelection(
    			new SettingsModelString(GeometryOperationNodeModel.CFG_OPERATOR,GeometryOperationNodeModel.DEFAULT_SELECTION),
    			"Geometry Operation",operations);
    	addDialogComponent(operatorSelectDialog);
    }
}

