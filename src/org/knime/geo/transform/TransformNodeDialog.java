package org.knime.geo.transform;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

/**
 * <code>NodeDialog</code> for the "Transform" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author 
 */
public class TransformNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the Transform node.
     */
    protected TransformNodeDialog() {

    }
}

