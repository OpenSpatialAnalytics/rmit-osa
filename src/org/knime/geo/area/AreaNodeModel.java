package org.knime.geo.area;

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
import org.knime.core.data.def.DoubleCell;
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

/**
 * This is the model implementation of Area.
 * 
 *
 * @author Forkan
 */
public class AreaNodeModel extends NodeModel {
    
    /**
     * Constructor for the node model.
     */
    protected AreaNodeModel() {
    
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
    	int geomIndex = inTable.getSpec().findColumnIndex(Constants.GEOM);	 
    	int numberOfColumns = inTable.getSpec().getNumColumns();
    	
    	DataTableSpec outSpec = createSpec(inTable.getSpec());
    	BufferedDataContainer container = exec.createDataContainer(outSpec);
    	
    	int index = 0;
    	for (DataRow row : inTable) {	    			    		   		
    		DataCell geometryCell = row.getCell(geomIndex);
    		
    		if (geometryCell instanceof StringValue){
    			String geoJsonString = ((StringValue) geometryCell).getStringValue();
    			Geometry geo = new GeometryJSON().read(geoJsonString);
    			double length = geo.getArea();   	
    			DataCell[] cells = new DataCell[outSpec.getNumColumns()];	
				cells[outSpec.getNumColumns()-1] = new DoubleCell(length);
				for ( int col = 0; col < numberOfColumns; col++ ) {	
	    			cells[col] = row.getCell(col);
	    		}
				container.addRowToTable(new DefaultRow(row.getKey(), cells));
	    		exec.checkCanceled();
				exec.setProgress((double) index / (double) inTable.size());
				index++;
    		}	
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
    
    private static DataTableSpec createSpec(DataTableSpec inSpec) throws InvalidSettingsException {
		
		List<DataColumnSpec> columns = new ArrayList<>();
		
		for (DataColumnSpec column : inSpec) {
			columns.add(column);
		}
		columns.add(new DataColumnSpecCreator("area", DoubleCell.TYPE).createSpec());
		
		return new DataTableSpec(columns.toArray(new DataColumnSpec[0]));
	}

}

