package org.knime.geo.operators;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.geotools.geojson.geom.GeometryJSON;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.MissingCell;
import org.knime.core.data.RowKey;
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
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.geoutils.Constants;
import org.knime.geoutils.ShapeFileOperations;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

/**
 * This is the model implementation of GeometryOperation.
 * 
 *
 * @author 
 */
public class GeometryOperationNodeModel extends NodeModel {
	
	protected static final String CFG_OPERATOR = "operator";
	protected static final String DEFAULT_SELECTION = "Union";
	
	private SettingsModelString operator;

    
    /**
     * Constructor for the node model.
     */
    protected GeometryOperationNodeModel() {
    
        super(2, 1);
        operator = new SettingsModelString(CFG_OPERATOR, DEFAULT_SELECTION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
    	
    	BufferedDataTable geometryTable1 = inData[0];
    	BufferedDataTable geometryTable2 = inData[1];
    	String operation = operator.getStringValue();
    	int geomIndex = geometryTable1.getSpec().findColumnIndex(Constants.GEOM);
    	
    	//DataTableSpec outSpec = createSpec(geometryTable1.getSpec());
    	DataTableSpec outSpec = createSpec(geometryTable1.getSpec(), 
    			geometryTable2.getSpec(),geomIndex, operation);
    	BufferedDataContainer container = exec.createDataContainer(outSpec);
    	
    	try{
    		int k = 0;
    		for (DataRow r1 : geometryTable1){
    			
    			DataCell geometryCell1 = r1.getCell(geomIndex);
    			String geoJsonString1 = ((StringValue) geometryCell1).getStringValue();
    			Geometry geo1 = new GeometryJSON().read(geoJsonString1);
    			
    			for (DataRow r2 : geometryTable2){
    				DataCell geometryCell2 = r2.getCell(geomIndex);
    				String geoJsonString2 = ((StringValue) geometryCell2).getStringValue();
        			Geometry geo2= new GeometryJSON().read(geoJsonString2);
        			
        			DataCell[] cells = new DataCell[outSpec.getNumColumns()];
        			
        			switch (operation)
        	    	{
        	    		case "Intersection":
        	    			Geometry geo = geo1.intersection(geo2);
        	    			if(!geo.isEmpty()){
        	    				GeometryJSON json = new GeometryJSON();
            					String str = json.toString(geo);
            					cells[geomIndex] = new StringCell(str);
            					for ( int col = 0; col < geometryTable1.getSpec().getNumColumns(); col++ ) {	
            						if (col != geomIndex ) {
            		    				cells[col] = r1.getCell(col);
            						}
            		    		}
            					
            					for ( int col = 0; col < geometryTable2.getSpec().getNumColumns(); col++ ) {	
            						if (col != geomIndex ) {
            		    				cells[geometryTable1.getSpec().getNumColumns()-1+col] = r2.getCell(col);
            						}
            		    		}
            					container.addRowToTable(new DefaultRow("Row"+k, cells));
            		    		exec.checkCanceled();
            					exec.setProgress((double) k / ((double) geometryTable1.size()+geometryTable2.size()));
            					k++;    					
        	    			}
        	    			break;
        	    			
        	    		case "Union":
        	    			geo = geo1.union(geo2);
        	    			if (geo.equals(geo1)){
        	    				cells[geomIndex] = new StringCell(geoJsonString1);
        	    				for ( int col = 0; col < geometryTable1.getSpec().getNumColumns(); col++ ) {	
            						if (col != geomIndex ) {
            		    				cells[col] = r1.getCell(col);
            						}
            		    		}
        	    				for ( int col = 0; col < geometryTable2.getSpec().getNumColumns(); col++ ) {	
            						if (col != geomIndex ) {
            		    				cells[geometryTable1.getSpec().getNumColumns()-1+col] = new MissingCell(null);
            						}
            		    		}
        	    			}
        	    			else if (geo.equals(geo2)) {
        	    				cells[geomIndex] = new StringCell(geoJsonString2);
        	    				for ( int col = 0; col < geometryTable1.getSpec().getNumColumns(); col++ ) {	
            						if (col != geomIndex ) {
            		    				cells[col] = new MissingCell(null);
            						}
            		    		}
        	    				for ( int col = 0; col < geometryTable2.getSpec().getNumColumns(); col++ ) {	
            						if (col != geomIndex ) {
            		    				cells[geometryTable1.getSpec().getNumColumns()-1+col] = r2.getCell(col);
            						}
            		    		}
        	    			}
        	    			else{
        	    				GeometryJSON json = new GeometryJSON();
            					String str = json.toString(geo);
            					cells[geomIndex] = new StringCell(str);
            					for ( int col = 0; col < geometryTable1.getSpec().getNumColumns(); col++ ) {	
            						if (col != geomIndex ) {
            		    				cells[col] = r1.getCell(col);
            						}
            		    		}
            					
            					for ( int col = 0; col < geometryTable2.getSpec().getNumColumns(); col++ ) {	
            						if (col != geomIndex ) {
            		    				cells[geometryTable1.getSpec().getNumColumns()-1+col] = r2.getCell(col);
            						}
            		    		}
        	    			}
        	    			container.addRowToTable(new DefaultRow("Row"+k, cells));
        		    		exec.checkCanceled();
        					exec.setProgress((double) k / ((double) geometryTable1.size()+geometryTable2.size()));
        					k++; 
        	    			break;
        	    			
        	    		case "Difference":
        	    			geo = geo1.difference(geo2);
        	    			if(!geo.isEmpty()){
        	    				GeometryJSON json = new GeometryJSON();
            					String str = json.toString(geo);
            					cells[geomIndex] = new StringCell(str);
            					for ( int col = 0; col < geometryTable1.getSpec().getNumColumns(); col++ ) {	
            						if (col != geomIndex ) {
            		    				cells[col] = r1.getCell(col);
            						}
            		    		}
            					container.addRowToTable(new DefaultRow("Row"+k, cells));
            		    		exec.checkCanceled();
            					exec.setProgress((double) k / ((double) geometryTable1.size()+geometryTable2.size()));
            					k++;    					
        	    			}
        	    			break;
        	    	}
        			
    			}
    			
    		}
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		
    	}
    	
    	/*
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
    	Geometry g1 = gc1.buffer(0);
		Geometry g2 = gc2.buffer(0);
    	Geometry[] results = new Geometry[1];;
    	
    	switch (operation)
    	{
    		case "Intersect":
    			
    			GeometryCollection gc = ShapeFileOperations.intersection(gc1,gc2);
    			results = new Geometry[gc.getNumGeometries()];
    			for (int i = 0; i < gc.getNumGeometries(); i++) {
    				results[i]= gc.getGeometryN(i);
    		    }
    		  
    			results = new Geometry[1];
    			results[0] = g1.intersection(g2);
    			break;
    			
    		case "Union":
    			results = new Geometry[1];
    			results[0] = g1.union(g2);
    			break;
    			
    		case "Difference":
    			results = new Geometry[1];
    			results[0] = g1.difference(g2);
    			break;
    	}
    	
    	
    	for (int i=0; i < results.length; i++  ) {
    	
    		DataCell[] cells = new DataCell[outSpec.getNumColumns()];
    		
    		GeometryJSON json = new GeometryJSON();
			String str = json.toString(results[i]);
			cells[0] = new StringCell(str);
			String key = "Row"+(i+1);
			
			container.addRowToTable(new DefaultRow(new RowKey(key), cells));
    		exec.checkCanceled();
			//exec.setProgress((double) index / (double) geometryTable.size());
    	}
    	*/
    	
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

    	return new DataTableSpec[] { createSpec(inSpecs[0], inSpecs[1], 0, operator.getStringValue() ) };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
         operator.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	  operator.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
       operator.validateSettings(settings);
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
    
    /*
    private static DataTableSpec createSpec(DataTableSpec inSpec) throws InvalidSettingsException {
		
		List<DataColumnSpec> columns = new ArrayList<>();
		
		DataColumnSpec column = inSpec.getColumnSpec(0);
		columns.add(column);
		return new DataTableSpec(columns.toArray(new DataColumnSpec[0]));
	}
	*/
    
    
    private static DataTableSpec createSpec(DataTableSpec inSpec1, 
    		DataTableSpec inSpec2, int geomIndex, String operation) throws InvalidSettingsException {
		
		List<DataColumnSpec> columns = new ArrayList<DataColumnSpec>();
		List<String> columnNames = new ArrayList<String>();

		int k = 0;
		for (DataColumnSpec column : inSpec1) {
			columns.add(column);
			columnNames.add(column.getName());
		}
		
		if ( operation.compareTo("Difference") != 0){
			k = 0;
			for (DataColumnSpec column : inSpec2) {
				if ( k != geomIndex ) {				
					String name = column.getName();
					if ( ArrayUtils.contains(columnNames.toArray(), name))
						columns.add(new DataColumnSpecCreator(name+"_1", column.getType()).createSpec());
					else {
						columns.add(column);
						columnNames.add(column.getName());
					}
				}
				k++;
			}
		}
		return new DataTableSpec(columns.toArray(new DataColumnSpec[0]));
	}
	

}

