package org.knime.geo.overlay;

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
import org.knime.core.data.RowKey;
import org.knime.core.data.StringValue;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.geoutils.Constants;
import org.knime.geoutils.ShapeFileOperations;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;


/**
 * This is the model implementation of Overlay.
 * 
 *
 * @author Forkan
 */
public class OverlayNodeModel extends NodeModel {
    
	
   
    /**
     * Constructor for the node model.
     */
    protected OverlayNodeModel() {
    
        // TODO one incoming port and one outgoing port is assumed
        super(2, 1);
      
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
    	
    	BufferedDataTable geometryTable1 = inData[0];
    	BufferedDataTable geometryTable2 = inData[1];
    	DataTableSpec outSpec = createSpec(geometryTable1.getSpec(), geometryTable2.getSpec());
    	BufferedDataContainer container = exec.createDataContainer(outSpec);
    	
    	Geometry [] table1 = ShapeFileOperations.allocateGeomList(geometryTable1.size());
    	Geometry [] table2 = ShapeFileOperations.allocateGeomList(geometryTable2.size());
    	
    	try{
    		int i = 0;
    		for (DataRow row : geometryTable1) {
    			
	    		DataCell geometryCell = row.getCell(0);
	    		
	    		if (geometryCell instanceof StringValue){
	    			String geoJsonString = ((StringValue) geometryCell).getStringValue();
	    			Geometry geo = new GeometryJSON().read(geoJsonString);
	    			table1[i] = geo;
	    			i++;
	    		}
    		}
    		
    		i = 0;
    		for (DataRow row : geometryTable2) {
    			
	    		DataCell geometryCell = row.getCell(0);
	    		
	    		if (geometryCell instanceof StringValue){
	    			String geoJsonString = ((StringValue) geometryCell).getStringValue();
	    			Geometry geo = new GeometryJSON().read(geoJsonString);
	    			table2[i] = geo;
	    			i++;
	    		}
    		}
    	
    	}
		catch (Exception e)
    	{
    		e.printStackTrace();
    		
    	}
    	
    	
    	GeometryCollection gc1 = ShapeFileOperations.toCollection(table1);
    	GeometryCollection gc2 = ShapeFileOperations.toCollection(table2);
    	GeometryCollection gc = ShapeFileOperations.intersection(gc1,gc2);
    	Geometry[] results = new Geometry[gc.getNumGeometries()];
		for (int i = 0; i < gc.getNumGeometries(); i++) {
			results[i]= gc.getGeometryN(i);
	    }
    	//Geometry g1 = gc1.buffer(0);
		//Geometry g2 = gc2.buffer(0);
    	//Geometry[] results = new Geometry[1];
    	
    	for (int i=0; i < results.length; i++  ) {
    	
    		DataCell[] cells = new DataCell[outSpec.getNumColumns()];
    		
    		GeometryJSON json = new GeometryJSON(Constants.JsonPrecision);
			String str = json.toString(results[i]);
			cells[0] = new StringCell(str);
			String key = "Row"+(i+1);
			
			container.addRowToTable(new DefaultRow(new RowKey(key), cells));
    		exec.checkCanceled();
			exec.setProgress((double) i / (double) results.length);
    	}
    	
    	container.close();
    	return new BufferedDataTable[] { container.getTable() };
       
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // TODO Code executed on reset.
        // Models build during execute are cleared here.
        // Also data handled in load/saveInternals will be erased here.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
        
        // TODO: check if user settings are available, fit to the incoming
        // table structure, and the incoming types are feasible for the node
        // to execute. If the node can execute in its current state return
        // the spec of its output data table(s) (if you can, otherwise an array
        // with null elements), or throw an exception with a useful user message

        return new DataTableSpec[]{null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {

     

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
      

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
      

    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        
    
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
       
     

    }
    
 private static DataTableSpec createSpec(DataTableSpec inSpec1, DataTableSpec inSpec2) throws InvalidSettingsException {
		
		List<DataColumnSpec> columns = new ArrayList<>();
		DataColumnSpec column = inSpec1.getColumnSpec(0);
		columns.add(column);
		
		/*
		for (DataColumnSpec column : inSpec1) {
			columns.add(column);
		}
		
		int i = 0;
		for (DataColumnSpec column : inSpec2) {
			if ( i != 0 )
				columns.add(column);
			i++;
		}
		*/
		
		return new DataTableSpec(columns.toArray(new DataColumnSpec[0]));
	}

}

