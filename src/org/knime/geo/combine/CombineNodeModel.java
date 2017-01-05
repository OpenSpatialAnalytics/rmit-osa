package org.knime.geo.combine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geotools.geojson.geom.GeometryJSON;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.IntValue;
import org.knime.core.data.RowIterator;
import org.knime.core.data.StringValue;
import org.knime.core.data.collection.CollectionCellFactory;
import org.knime.core.data.collection.ListCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.LongCell;
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
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.knime.geoutils.Constants;

import com.ibm.icu.impl.Utility;
import com.sun.scenario.Settings;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.util.GeometryCombiner;

/**
 * This is the model implementation of Combine.
 * 
 *
 * @author Forkan
 */
public class CombineNodeModel extends NodeModel {
	
	static final String CN = "column_name";
	public final SettingsModelString columnNames = new SettingsModelString(CN,"");
    
    /**
     * Constructor for the node model.
     */
    protected CombineNodeModel() {
   
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
    	int combineIndex = -1;
    	boolean combinedByAll = false;
    	int strColSize = inTable.getSpec().getNumColumns()-1;
    	DataTableSpec outSpec;
    	GeometryFactory geometryFactory = new GeometryFactory();
    	
    	if(columnNames.getStringValue() == null)
    		combinedByAll = true;
    	
    	if (!combinedByAll)  {
    		combineIndex = inTable.getSpec().findColumnIndex(columnNames.getStringValue());
    		strColSize = inTable.getSpec().getNumColumns()-2;
    		outSpec = createSpec(inTable.getSpec(),columnNames.getStringValue());
    	}
    	else{
    		outSpec = createSpec(inTable.getSpec(),"All");
    	}
    	
    	//String[] attrValues = new String[strColSize];
    	List<List<StringCell>> attrValues = new ArrayList<List<StringCell>>(strColSize);
    	for(int i = 0; i < strColSize; i++)  {
    		attrValues.add(new ArrayList<StringCell>());
        }
    	
    	BufferedDataContainer container = exec.createDataContainer(outSpec);
    	List <Geometry> geometries = new ArrayList <Geometry>();
        	    	    	    	    	
    	try{    	
 
    		if (combinedByAll) {
    		
		    	for (DataRow r : inTable) {
		    				    		
		    		DataCell geometryCell = r.getCell(geomIndex);
		    		
		    		if ( (geometryCell instanceof StringValue) ){
		    			String geoJsonString = ((StringValue) geometryCell).getStringValue();	    			
		    			Geometry g = new GeometryJSON().read(geoJsonString);
		    			geometries.add(g);			  				    
		    		}
		    		
		    		int k = 0;
		    		for ( int col = 0; col < inTable.getSpec().getNumColumns(); col++ ) {
		    			if (col != geomIndex ){
		    				DataCell c = r.getCell(col);
		    				attrValues.get(k).add(new StringCell(c.toString()));
		    				k++;
		    			}
		    			
		    		}
		    	}
		    	//Geometry geo = geometryFactory.buildGeometry(geometries);
	    		//Geometry geo = GeometryCombiner.combine(geometries);
		    	GeometryCollection collect = new GeometryCollection((Geometry[]) geometries.toArray(new Geometry[0]),geometryFactory);
    			GeometryJSON json = new GeometryJSON(Constants.JsonPrecision);
				String str = json.toString(collect);
				DataCell[] cells = new DataCell[outSpec.getNumColumns()];
				cells[geomIndex] = new StringCell(str);
				int k = 0;
				for ( int col = 0; col < inTable.getSpec().getNumColumns(); col++ ) {
	    			if (col != geomIndex ){
	    				cells[col] =  CollectionCellFactory.createListCell(attrValues.get(k));
	    				k++;
	    			}
				}
				container.addRowToTable(new DefaultRow("Row0", cells));
	    		exec.checkCanceled();
				//exec.setProgress((double) i / (double) inTable.size());  			    				    				    			    			    			    			    		    							
	    	}
    		else{
    			List <String> colValues = new ArrayList<String>();
    			for (DataRow r : inTable) {
    				DataCell groupCell = r.getCell(combineIndex);
    				colValues.add(groupCell.toString());
    			}
    			Set uniqueValues = new HashSet(colValues); 
    			int numOfGroups = uniqueValues.size();
    			Object[] groupValues = uniqueValues.toArray();
    			
    			List<String> groupValuesStr = new ArrayList<String>(numOfGroups);
    			List<List<Geometry>> combinedGeometries = new ArrayList<List<Geometry>>(numOfGroups);
    			
    			List<List<List<StringCell>>> columnAppendList = new ArrayList<List<List<StringCell>>>(numOfGroups);
    			
    					
    			for (int i = 0; i < numOfGroups; i++ ){
    				combinedGeometries.add(new ArrayList<Geometry>());
    				groupValuesStr.add(groupValues[i].toString());
    				columnAppendList.add(new ArrayList<List<StringCell>>(strColSize));
    				columnAppendList.get(i).add(new ArrayList<StringCell>());
    			}
    			
    			
    			for (DataRow r : inTable) {
    				
    				DataCell geometryCell = r.getCell(geomIndex);
    				DataCell combineCell = r.getCell(combineIndex);
    				int grpIndex = groupValuesStr.indexOf(combineCell.toString());
    				
    				if ( (geometryCell instanceof StringValue) ){
		    			String geoJsonString = ((StringValue) geometryCell).getStringValue();	    			
		    			Geometry g = new GeometryJSON().read(geoJsonString);
		    			combinedGeometries.get(grpIndex).add(g);	  				    
		    		}
    				
    				
    				int k = 0;
		    		for ( int col = 0; col < inTable.getSpec().getNumColumns(); col++ ) {
		    			if ( (col != geomIndex) && (col != combineIndex) ){
		    				DataCell c = r.getCell(col);
		    				columnAppendList.get(grpIndex).get(k).add(new StringCell( c.toString()));
		    				k++;
		    			}
		    		}
    			}
    			
    			for (int i = 0; i < numOfGroups; i++ ){
    				//Geometry geo = GeometryCombiner.combine(combinedGeometries.get(i));
    				GeometryCollection collect = new GeometryCollection((Geometry[]) combinedGeometries.get(i).toArray(new Geometry[0]),geometryFactory);
    				//Geometry geo = geometryFactory.buildGeometry(combinedGeometries.get(i));
    				GeometryJSON json = new GeometryJSON(Constants.JsonPrecision);
    				String str = json.toString(collect);
    				DataCell[] cells = new DataCell[outSpec.getNumColumns()];
    				cells[geomIndex] = new StringCell(str);
    				
    				DataColumnSpec sp = inTable.getSpec().getColumnSpec(combineIndex);
    				if (sp.getType() == IntCell.TYPE)
    					cells[combineIndex] = new IntCell(Integer.parseInt(groupValuesStr.get(i)));
    				else if (sp.getType() == LongCell.TYPE)
    					cells[combineIndex] = new LongCell(Long.parseLong(groupValuesStr.get(i)));
    				else
    					cells[combineIndex] = new StringCell(groupValuesStr.get(i));
    				
    				
    				int k = 0;
    				for ( int col = 0; col < inTable.getSpec().getNumColumns(); col++ ) {
    	    			if ( col != geomIndex && col != combineIndex ){
    	    				cells[col] = CollectionCellFactory.createListCell(columnAppendList.get(i).get(k));
    	    				k++;
    	    			}
    				}
    				
    				container.addRowToTable(new DefaultRow("Row"+i, cells));
    	    		exec.checkCanceled();
    				
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
    	 
        return new DataTableSpec[]{null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        this.columnNames.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        this.columnNames.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        this.columnNames.validateSettings(settings);
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
    
    private static DataTableSpec createSpec(DataTableSpec inSpec, String combineCol) 
    		throws InvalidSettingsException {
		
		List<DataColumnSpec> columns = new ArrayList<>();

		for (DataColumnSpec column : inSpec) {
			if (column.getName().compareTo(combineCol)==0){
				columns.add(column);
			}
			else{
				if (column.getName().compareTo(Constants.GEOM)==0)
					columns.add(column);
				else
					columns.add(new DataColumnSpecCreator(column.getName(), ListCell.getCollectionType(StringCell.TYPE)).createSpec());
					//columns.add(new DataColumnSpecCreator(column.getName(), StringCell.TYPE).createSpec());
			}
			
		}
		
		return new DataTableSpec(columns.toArray(new DataColumnSpec[0]));
	}

}

