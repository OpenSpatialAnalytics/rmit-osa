package org.knime.geo.snapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geotools.geojson.geom.GeometryJSON;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.DoubleValue;
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
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.gdalutils.Utility;
import org.knime.geoutils.Constants;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.precision.GeometryPrecisionReducer;

/**
 * This is the model implementation of SnapToGrid.
 * 
 *
 * @author 
 */
public class SnapToGridNodeModel extends NodeModel {
    
    /**
     * Constructor for the node model.
     */
	
	static final String SNAP_SIZE = "size";
	public final SettingsModelString snapSize = new SettingsModelString(SNAP_SIZE, "1");
	
    protected SnapToGridNodeModel() {
            
        super(1, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

    	BufferedDataTable dataTable = inData[0];    	
    	int geomIndex = dataTable.getSpec().findColumnIndex(Constants.GEOM);	 
    	int numberOfColumns = dataTable.getSpec().getNumColumns();
    	
    	DataTableSpec outSpec = createSpec(dataTable.getSpec());
    	BufferedDataContainer container = exec.createDataContainer(outSpec);
    	    	
    	double scaleFactor = 0.0;
    	try{
    		scaleFactor = Double.parseDouble(snapSize.getStringValue());    		
    	}
    	catch (NumberFormatException e)
    	{
    		throw new NumberFormatException("Span scale size must be a double value");
    	}
    	
    	PrecisionModel pm = new PrecisionModel(scaleFactor);
    	
    	int index = 0;
    	
    	try{
    		
	    	for (DataRow row : dataTable) {
	    	
	    		DataCell geometryCell = row.getCell(geomIndex);
	    	
	    		if (geometryCell instanceof StringValue){
	    			DataCell[] cells = new DataCell[outSpec.getNumColumns()];
	    			String geoJsonString = ((StringValue) geometryCell).getStringValue();
	    			Geometry geo = new GeometryJSON().read(geoJsonString);
	    			Geometry gp = GeometryPrecisionReducer.reduce(geo, pm);
	    			GeometryJSON json = new GeometryJSON(Constants.JsonPrecision);
					String str = json.toString(gp);
					cells[geomIndex] = new StringCell(str);
					for ( int col = 0; col < numberOfColumns; col++ ) {	
						if (col != geomIndex ) {
		    				cells[col] = row.getCell(col);
						}
		    		}
					container.addRowToTable(new DefaultRow(row.getKey(), cells));
		    		exec.checkCanceled();
					exec.setProgress((double) index / (double) dataTable.size());
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
    	
    	if (snapSize.getStringValue() == null )
    		throw new InvalidSettingsException( "Must provide a snap scale size");
    	    	
    	try{
    		double d = Double.parseDouble(snapSize.getStringValue());
    		
    	}
    	catch (NumberFormatException e)
    	{
    		throw new NumberFormatException("Snap scale size must be a double value");
    	}
    	
    	String columNames[] = inSpecs[0].getColumnNames();
    	if (!Arrays.asList(columNames).contains(Constants.GEOM)){
			throw new InvalidSettingsException( "Input table must contains a geometry column (the_geom)");
		}
    	    	
        return new DataTableSpec[]{null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	snapSize.saveSettingsTo(settings);
         // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	snapSize.loadSettingsFrom(settings);
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	snapSize.validateSettings(settings);
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
		return new DataTableSpec(columns.toArray(new DataColumnSpec[0]));
	}

}

