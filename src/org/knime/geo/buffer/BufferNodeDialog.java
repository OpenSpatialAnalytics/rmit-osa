package org.knime.geo.buffer;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "Buffer" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Forkan
 */
public class BufferNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the Buffer node.
     */
    protected BufferNodeDialog() {
    	    	    	
    	DialogComponentString bufferDistanceDialog = new DialogComponentString(
    			new SettingsModelString(BufferNodeModel.DISTANCE,"0.0"), "Buffer Distance");
    	
    	addDialogComponent(bufferDistanceDialog);

    }
}

