package org.knime.geo.crossjoin;

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
import org.knime.core.data.StringValue;
import org.knime.core.data.def.DefaultRow;
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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 * This is the model implementation of CrossJoiner.
 * 
 *
 * @author Forkan
 */
public class CrossJoinerNodeModel extends NodeModel {
    
    /**
     * Constructor for the node model.
     */
    protected CrossJoinerNodeModel() {
    
        // TODO: Specify the amount of input and output ports needed.
        super(4, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
    	
    	DataTableSpec outSpec = createSpec(inData);
    	BufferedDataContainer container = exec.createDataContainer(outSpec);
    	
    	if (inData.length < 2 ){
    		throw new Exception("At least need 2 tables");
    	}
    	else if (inData.length == 2){
    		for (int i = 0; i < inData[0].size(); i++ ){
    			for (int j = i+1; j < inData[1].size(); j++ ){
    				DataCell[] cells = new DataCell[outSpec.getNumColumns()];
    			}
    		}
    			
    		
    	}

		//container.addRowToTable(new DefaultRow(row.getKey(), cells));
		exec.checkCanceled();
		//exec.setProgress((double) index / (double) inTable.size());
		//index++;
    	
    	
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
    
    private static DataTableSpec createSpec(BufferedDataTable[] inData) throws InvalidSettingsException {
		
		List<DataColumnSpec> columns = new ArrayList<>();
		
		for (int i=0; i < inData.length; i++ ){
			DataTableSpec inSpec = inData[i].getSpec();
			for (DataColumnSpec column : inSpec) {
				if (i != 0) {
					String name = column.getName();
					name = name + "_" + i;
					columns.add(new DataColumnSpecCreator(name, column.getType()).createSpec());
				}
				else{
					columns.add(column);
				}
			}
		}

		
		return new DataTableSpec(columns.toArray(new DataColumnSpec[0]));
	}

}

