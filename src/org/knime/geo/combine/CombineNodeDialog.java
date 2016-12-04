package org.knime.geo.combine;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.util.ColumnSelectionComboxBox;
import org.knime.geo.resample.ReSampleMethod;
import org.knime.geo.resample.ResampleNodeModel;

/**
 * <code>NodeDialog</code> for the "Combine" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Forkan
 */
public class CombineNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the Combine node.
     */
    protected CombineNodeDialog() {
    	
    	//ColumnSelectionComboxBox combo = new ColumnSelectionComboxBox("Group By");
    	//addDialogComponent(combo);
    	
    	DialogComponentStringSelection columnNameSelectDialog = new DialogComponentStringSelection(
    			new SettingsModelString(CombineNodeModel.CN,CombineNodeModel.columnNameList.get(0)),
    			"Combine By",ColumNameMethod());
    	
    	addDialogComponent(columnNameSelectDialog);
   
    }
    
    private String[] ColumNameMethod()
    {
    	String[] names = new String[CombineNodeModel.numColumns];
    	
		for (int i = 0; i < CombineNodeModel.columnNameList.size(); i++) {
	        names[i] = CombineNodeModel.columnNameList.get(i);
	    }
		
		return names;
    }
}

