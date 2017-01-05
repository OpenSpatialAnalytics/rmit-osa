package org.knime.geo.split;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.geojson.geom.GeometryJSON;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowIterator;
import org.knime.core.data.StringValue;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.geoutils.Constants;
import org.knime.geoutils.Split;
import com.vividsolutions.jts.geom.Geometry;

/**
 * This is the model implementation of Split.
 * 
 *
 * @author Forkan
 */
public class SplitNodeModel extends NodeModel {
    
    /**
     * Constructor for the node model.
     */
    protected SplitNodeModel() {
    
        // TODO: Specify the amount of input and output ports needed.
        super(1, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

    	BufferedDataTable inTable = inData[0];
    	String columNames[] = inTable.getSpec().getColumnNames();
    	int numColumns = inTable.getSpec().getNumColumns();
    	int geomIndexs[] = new int[2];
    	
    	int j = 0;
    	for (int i = 0; i < numColumns; i++) {
    		if (columNames[i].contains(Constants.GEOM) ){
    			geomIndexs[j] = i;
    			j++;
    			if (j==2)
    				break;
    		}
    	}
    	
    	DataTableSpec outSpec = createSpec(inTable.getSpec(), geomIndexs);
    	BufferedDataContainer container = exec.createDataContainer(outSpec);
    	
    
    	RowIterator ri = inTable.iterator();
  
    	int index = 0;
    	    	    	    	    	
    	try{    	
	    	for (int i = 0; i < inTable.size(); i++ ) {
	    		
	    		DataRow r = ri.next();		
	    		
	    		DataCell geometryCell1 = r.getCell(geomIndexs[0]);
	    		DataCell geometryCell2 = r.getCell(geomIndexs[1]);
	    		
	    		if ( (geometryCell1 instanceof StringValue) && (geometryCell2 instanceof StringValue) ){
	    			String geoJsonString1 = ((StringValue) geometryCell1).getStringValue();	    			
	    			Geometry geo1 = new GeometryJSON().read(geoJsonString1);
	    			String geoJsonString2 = ((StringValue) geometryCell2).getStringValue();	    			
	    			Geometry geo2 = new GeometryJSON().read(geoJsonString2);	    				    			
    				Geometry geo = Split.split(geo1, geo2);
    				GeometryJSON json = new GeometryJSON(Constants.JsonPrecision);
					String str = json.toString(geo);
					
					DataCell[] cells = new DataCell[outSpec.getNumColumns()];
					
					cells[geomIndexs[0]] = new StringCell(str);
					
					int k = 0;
					for ( int col = 0; col < numColumns; col++ ) {	
						if (col == geomIndexs[0]) {
		    				k++;
						}else if(col == geomIndexs[1]){
							
						}else{
							cells[k] = r.getCell(col);
		    				k++;
						}
		    		}
					//cells[outSpec.getNumColumns()-1] = new IntCell(index+1);
					
					container.addRowToTable(new DefaultRow("Row"+index, cells));
		    		exec.checkCanceled();
					exec.setProgress((double) i / (double) inTable.size());
					index++;    					
	    		}
	    	}
	    				    				    				    			    			    			    			    		    						
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		
    	}
    	container.close();
    	return new BufferedDataTable[] { container.getTable() };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {

        // TODO: generated method stub
        return new DataTableSpec[]{null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
         // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        // TODO: generated method stub
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // TODO: generated method stub
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // TODO: generated method stub
    }
    
    private static DataTableSpec createSpec(DataTableSpec inSpec, int geomIndexs[]) throws InvalidSettingsException {
		
		List<DataColumnSpec> columns = new ArrayList<>();

		int k = 0;
		for (DataColumnSpec column : inSpec) {
			if (k != geomIndexs[1]){
				columns.add(column);
			}
			k++;
		}
		return new DataTableSpec(columns.toArray(new DataColumnSpec[0]));
	}


}

