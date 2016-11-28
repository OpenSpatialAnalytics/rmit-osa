package org.knime.geo.intersection;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.geo.resample.ResampleNodeModel;

/**
 * <code>NodeDialog</code> for the "Intersection" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author 
 */
public class IntersectionNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the Intersection node.
     */
    protected IntersectionNodeDialog() {
    	

    }
}

