package org.knime.geo.transform;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowIterator;
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
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Geometry;

/**
 * This is the model implementation of Transform.
 * 
 *
 * @author 
 */
public class TransformNodeModel extends NodeModel {
    
    /**
     * Constructor for the node model.
     */
    protected TransformNodeModel() {
    
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
    	
    	DataTableSpec outSpec = createSpec(inTable.getSpec());
    	BufferedDataContainer container = exec.createDataContainer(outSpec);
    	
    	RowIterator ri = inTable.iterator();
    	
    	CoordinateReferenceSystem srcCRS = CRS.decode("EPSG:3111");
    	CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326");
    	MathTransform transform = CRS.findMathTransform(srcCRS, targetCRS, true);
    	
        	    	    	    	    	
    	try{    	
	    	for (int i = 0; i < inTable.size(); i++ ) {
	    		
	    		DataRow r = ri.next();				    		
	    		DataCell geometryCell = r.getCell(geomIndex);
	    		
	    		
	    		if ( (geometryCell instanceof StringValue) ){
	    			String geoJsonString = ((StringValue) geometryCell).getStringValue();	    			
	    			Geometry g = new GeometryJSON().read(geoJsonString);
	    			  				    			
	    			Geometry geo = JTS.transform(g, transform);
	    			GeometryJSON json = new GeometryJSON();
    				String str = json.toString(geo);
    					
    				DataCell[] cells = new DataCell[outSpec.getNumColumns()];
    				cells[geomIndex] = new StringCell(str);
    					
					for ( int col = 0; col < inTable.getSpec().getNumColumns(); col++ ) {	
						if (col != geomIndex) {
							cells[col] = r.getCell(col);
						}
		    		}
					
					container.addRowToTable(new DefaultRow("Row"+i, cells));
		    		exec.checkCanceled();
					exec.setProgress((double) i / (double) inTable.size());  					
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
    
    private static DataTableSpec createSpec(DataTableSpec inSpec) throws InvalidSettingsException {
		
		List<DataColumnSpec> columns = new ArrayList<>();

		for (DataColumnSpec column : inSpec) {
			columns.add(column);
		}
		
		return new DataTableSpec(columns.toArray(new DataColumnSpec[0]));
	}

}
