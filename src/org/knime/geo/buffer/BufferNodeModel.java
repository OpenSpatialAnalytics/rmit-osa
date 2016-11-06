package org.knime.geo.buffer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.geojson.geom.GeometryJSON;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowIterator;
import org.knime.core.data.RowKey;
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
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import com.vividsolutions.jts.geom.Geometry;

/**
 * This is the model implementation of Buffer.
 * 
 *
 * @author Forkan
 */
public class BufferNodeModel extends NodeModel {
	
	 private static final NodeLogger logger = NodeLogger
	            .getLogger(BufferNodeModel.class);
	
	protected static final String CFG_GEOMETRY_COLUMN = "the_geom";
	private static final int GEOMETRY_COLUMN_INDEX = 0;
	
	
	private SettingsModelString geomColumn;
    
    /**
     * Constructor for the node model.
     */
    protected BufferNodeModel() {
        super(2, 1);
        geomColumn = new SettingsModelString(CFG_GEOMETRY_COLUMN, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

    	BufferedDataTable geometryTable = inData[0];
    	BufferedDataTable bufferValueTable = inData[1];
    	DataTableSpec outSpec = createSpec(geometryTable.getSpec());
    	BufferedDataContainer container = exec.createDataContainer(outSpec);
    	
    	
    	RowIterator ri = bufferValueTable.iterator();
    	DoubleCell d = (DoubleCell) ri.next().getCell(0);
    	double distance = d.getDoubleValue();
    	
    	//logger.info("Read distance value for buffer: " + distance);
    	
    	
    	int index = 0;
    	//ri = geometryTable.iterator();
    	
    	try{
    		
	    	for (DataRow row : geometryTable) {
	    		
	    		DataCell[] cells = new DataCell[outSpec.getNumColumns()];
	    		DataCell geometryCell = row.getCell(GEOMETRY_COLUMN_INDEX);
	    		
	    		//logger.info("geometryCell:" + geometryCell.toString());
	    		
	    		if (geometryCell instanceof StringValue){
	    			String geoJsonString = ((StringValue) geometryCell).getStringValue();
	    			Geometry geo = new GeometryJSON().read(geoJsonString);
	    			Geometry gf = geo.buffer(distance);
	    			GeometryJSON json = new GeometryJSON();
					String str = json.toString(gf);
	    			cells[0] = new StringCell(str);
	    		}
	    		
	    		for ( int col = 1; col < geometryTable.getSpec().getNumColumns(); col++ ) {
	    			
	    			cells[col] = row.getCell(col);
	    			
	    		}
	    		
	    		
	    		/*
	    		for (String column : geometryTable.getSpec().getColumnNames()) {
	    			
	    			if ( column.compareTo(geomColumn.getStringValue()) != 0 )
	    			{
	    				cells[outSpec.findColumnIndex(column)] = row
							.getCell(geometryTable.getSpec().findColumnIndex(column));
	    			}
				}*/
	    		
	    		container.addRowToTable(new DefaultRow(row.getKey(), cells));
	    		exec.checkCanceled();
				exec.setProgress((double) index / (double) geometryTable.size());
				index++;
	    		
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

    	return new DataTableSpec[] { createSpec(inSpecs[0]) };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	geomColumn.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	geomColumn.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        geomColumn.validateSettings(settings);
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
		return new DataTableSpec(columns.toArray(new DataColumnSpec[0]));
	}

}

