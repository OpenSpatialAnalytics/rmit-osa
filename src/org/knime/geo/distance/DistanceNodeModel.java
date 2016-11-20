package org.knime.geo.distance;

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
 * This is the model implementation of Distance.
 * 
 *
 * @author Forkan
 */
public class DistanceNodeModel extends NodeModel {
    
    /**
     * Constructor for the node model.
     */
    protected DistanceNodeModel() {
    
        // TODO: Specify the amount of input and output ports needed.
        super(2, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

    	BufferedDataTable inTable1 = inData[0];    	
    	BufferedDataTable inTable2 = inData[1]; 
    	int geomIndex = inTable1.getSpec().findColumnIndex(Constants.GEOM);	 
  
    	DataTableSpec outSpec = createSpec();
    	BufferedDataContainer container = exec.createDataContainer(outSpec);
    	
    	RowIterator ri1 = inTable1.iterator();
    	RowIterator ri2 = inTable2.iterator();
    	
    	int index = 0;
    	for (int i = 0; i < inTable1.size(); i++ ) {
    		
    		DataRow r1 = ri1.next();
    		DataRow r2 = ri2.next();	 
    		
    		DataCell[] cells = new DataCell[outSpec.getNumColumns()];	    		   		
    		DataCell geometryCell1 = r1.getCell(geomIndex);
    		DataCell geometryCell2 = r2.getCell(geomIndex);
    		
    		if ( (geometryCell1 instanceof StringValue) && (geometryCell2 instanceof StringValue) ){
    			String geoJsonString1 = ((StringValue) geometryCell1).getStringValue();	    			
    			Geometry geo1 = new GeometryJSON().read(geoJsonString1);
    			String geoJsonString2 = ((StringValue) geometryCell2).getStringValue();	    			
    			Geometry geo2 = new GeometryJSON().read(geoJsonString2);
    			double distance = geo1.distance(geo2);			
				cells[geomIndex] = new DoubleCell(distance);	
				container.addRowToTable(new DefaultRow(r1.getKey(), cells));
	    		exec.checkCanceled();
				exec.setProgress((double) index / (double) inTable1.size());
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
    
    private static DataTableSpec createSpec() throws InvalidSettingsException {
		
		List<DataColumnSpec> columns = new ArrayList<>();
		columns.add(new DataColumnSpecCreator("Distance", DoubleCell.TYPE).createSpec());
		return new DataTableSpec(columns.toArray(new DataColumnSpec[0]));
	}

}

