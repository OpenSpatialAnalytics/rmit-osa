package org.knime.geo.snapper;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.geo.buffer.BufferNodeModel;

/**
 * <code>NodeDialog</code> for the "SnapToGrid" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author 
 */
public class SnapToGridNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the SnapToGrid node.
     */
    protected SnapToGridNodeDialog() {
    	
    	DialogComponentString snapSizeDialog = new DialogComponentString(
    			new SettingsModelString(SnapToGridNodeModel.SNAP_SIZE,"1"), "Snap scale size");
    	
    	addDialogComponent(snapSizeDialog);

    }
}

