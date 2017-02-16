package org.knime.geo.combine;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataValue;
import org.knime.core.data.StringValue;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnNameSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.util.ColumnFilter;
import org.knime.core.node.util.ColumnSelectionComboxBox;
import org.knime.geo.resample.ReSampleMethod;
import org.knime.geo.resample.ResampleNodeModel;
import org.knime.geoutils.Constants;

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
	@SuppressWarnings("unchecked")
    protected CombineNodeDialog() {
    	
    	//ColumnSelectionComboxBox combo = new ColumnSelectionComboxBox("Group By");
    	//addDialogComponent(combo);
    	
    	/*
    	DialogComponentStringSelection columnNameSelectDialog = new DialogComponentStringSelection(
    			new SettingsModelString(CombineNodeModel.CN,CombineNodeModel.columnNameList[0]),
    			"Combine By",ColumNameMethod());
    			*/
    	
    	DialogComponentColumnNameSelection columnSelect = 
    			new DialogComponentColumnNameSelection(new SettingsModelString(CombineNodeModel.CN,""),
    					"Combine By",0,false,true, filterColumn);
    	addDialogComponent(columnSelect);
    	
    }
	
	ColumnFilter filterColumn = new ColumnFilter() {
        @Override
        public boolean includeColumn(DataColumnSpec dataColumnSpec) {
            return !(dataColumnSpec.getName().compareTo(Constants.GEOM) == 0);
        }

		@Override
		public String allFilteredMsg() {
			// TODO Auto-generated method stub
			return "No column other than Geometry column is available";
		}
	};
    
	/*
    private String[] ColumNameMethod()
    {
    	String[] names = new String[CombineNodeModel.numColumns];
    	
		for (int i = 0; i < CombineNodeModel.columnNameList.length; i++) {
	        names[i] = CombineNodeModel.columnNameList[i];
	    }
		
		return names;
    }
    */
}

