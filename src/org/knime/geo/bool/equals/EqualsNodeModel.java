package org.knime.geo.bool.equals;

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
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.DefaultRow;
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
 * This is the model implementation of Equals.
 * 
 *
 * @author 
 */
public class EqualsNodeModel extends NodeModel {
    
    /**
     * Constructor for the node model.
     */
    protected EqualsNodeModel() {
    
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
    	
    	DataTableSpec outSpec = createSpec(inTable.getSpec());
    	BufferedDataContainer container = exec.createDataContainer(outSpec);
    		    	    	    	    	
    	try{    	
    		int index = 0;
	    	for (DataRow row : inTable ) {
	    		
	    		DataCell geometryCell1 = row.getCell(geomIndexs[0]);
	    		DataCell geometryCell2 = row.getCell(geomIndexs[1]);
	    		
	    		
	    		if ( (geometryCell1 instanceof StringValue) && (geometryCell2 instanceof StringValue) ){
	    			String geoJsonString1 = ((StringValue) geometryCell1).getStringValue();	    			
	    			Geometry geo1 = new GeometryJSON().read(geoJsonString1);
	    			String geoJsonString2 = ((StringValue) geometryCell2).getStringValue();	    			
	    			Geometry geo2 = new GeometryJSON().read(geoJsonString2);
	    			
	    			boolean b = geo1.equals(geo2);
	    		
    				DataCell[] cells = new DataCell[outSpec.getNumColumns()];
    				cells[outSpec.getNumColumns()-1] = BooleanCell.BooleanCellFactory.create(b);
    					
					for ( int col = 0; col < inTable.getSpec().getNumColumns(); col++ ) {	
						cells[col] = row.getCell(col);
		    		}
					
					container.addRowToTable(new DefaultRow("Row"+index, cells));
		    		exec.checkCanceled();
					exec.setProgress((double) index / (double) inTable.size());  
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

    	int j = 0;
    	int numColumns = inSpecs[0].getNumColumns();
    	String columNames[] = inSpecs[0].getColumnNames();
    	for (int i = 0; i < numColumns; i++) {
    		if (columNames[i].contains(Constants.GEOM) ){
    			j++;
    			if (j==2)
    				break;
    		}
    	}
    	
    	if ( j < 2 )
    		throw new InvalidSettingsException( "Input table must contain 2 geometry columns "
                    + "and those column headers must start with the_geom");
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
		
		columns.add(new DataColumnSpecCreator("equals", BooleanCell.TYPE).createSpec());
		
		return new DataTableSpec(columns.toArray(new DataColumnSpec[0]));
	}

}

