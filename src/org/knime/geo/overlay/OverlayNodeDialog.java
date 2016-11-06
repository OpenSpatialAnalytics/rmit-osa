package org.knime.geo.overlay;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.geo.operators.GeometryOperationNodeModel;

/**
 * <code>NodeDialog</code> for the "Overlay" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Forkan
 */
public class OverlayNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring Overlay node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected OverlayNodeDialog() {
        super();
    }
}

