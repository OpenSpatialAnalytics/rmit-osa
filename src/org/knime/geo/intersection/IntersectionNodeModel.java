package org.knime.geo.intersection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.Geometries;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowIterator;
import org.knime.core.data.StringValue;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.IntCell;
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
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.geoutils.Constants;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * This is the model implementation of Intersection.
 * 
 *
 * @author 
 */
public class IntersectionNodeModel extends NodeModel {
	
	static final String IN = "intersects";
	static final String TO = "touches";
	
	  public final SettingsModelBoolean intersects = new SettingsModelBoolean(IN,true);
	  public final SettingsModelBoolean touches = new SettingsModelBoolean(TO,false);
    
    /**
     * Constructor for the node model.
     */
    protected IntersectionNodeModel() {
    
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
    	
    	DataTableSpec outSpec = createSpec(inTable1.getSpec(),inTable2.getDataTableSpec(),geomIndex);
    	BufferedDataContainer container = exec.createDataContainer(outSpec);
    	
    	boolean isIntersects = intersects.getBooleanValue();
    	boolean isTouches = touches.getBooleanValue();
    	    	    	
    	RowIterator ri1 = inTable1.iterator();
    	RowIterator ri2 = inTable2.iterator();
    	
    	int index = 0;
    	    	    	    	    	
    	try{    	
	    	for (int i = 0; i < inTable1.size(); i++ ) {
	    		
	    		DataRow r1 = ri1.next();
	    		DataRow r2 = ri2.next();	    		
	    			    			    		
	    		DataCell geometryCell1 = r1.getCell(geomIndex);
	    		DataCell geometryCell2 = r2.getCell(geomIndex);
	    		
	    		
	    		if ( (geometryCell1 instanceof StringValue) && (geometryCell2 instanceof StringValue) ){
	    			String geoJsonString1 = ((StringValue) geometryCell1).getStringValue();	    			
	    			Geometry geo1 = new GeometryJSON().read(geoJsonString1);
	    			String geoJsonString2 = ((StringValue) geometryCell2).getStringValue();	    			
	    			Geometry geo2 = new GeometryJSON().read(geoJsonString2);	    				    			
	    			if ( (geo1.intersects(geo2)) && (!geo1.touches(geo2)) ){
	    				Geometry geo = geo1.intersection(geo2);
	    				GeometryJSON json = new GeometryJSON();
    					String str = json.toString(geo);
    					
    					DataCell[] cells = new DataCell[outSpec.getNumColumns()];
    					
    					cells[geomIndex] = new StringCell(str);
    					
    					for ( int col = 0; col < inTable1.getSpec().getNumColumns(); col++ ) {	
    						if (col != geomIndex ) {
    		    				cells[col] = r1.getCell(col);
    						}
    		    		}
    					
    					for ( int col = 0; col < inTable2.getSpec().getNumColumns(); col++ ) {	
    						if (col != geomIndex ) {
    		    				cells[inTable1.getSpec().getNumColumns()-1+col] = r2.getCell(col);
    						}
    		    		}
    					cells[outSpec.getNumColumns()-1] = new IntCell(index+1);
    					
    					container.addRowToTable(new DefaultRow("Row"+index, cells));
    		    		exec.checkCanceled();
    					exec.setProgress((double) i / (double) inTable1.size());
    					index++;    					
	    			}
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
    	this.intersects.saveSettingsTo(settings);
    	this.touches.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	this.intersects.loadSettingsFrom(settings);
    	this.touches.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	this.intersects.validateSettings(settings);
    	this.touches.validateSettings(settings);
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
    
    private static DataTableSpec createSpec(DataTableSpec inSpec1, DataTableSpec inSpec2, int geomIndex) throws InvalidSettingsException {
		
		List<DataColumnSpec> columns = new ArrayList<>();

		int k = 0;
		for (DataColumnSpec column : inSpec1) {
			if (k == geomIndex){
				columns.add(column);
			}
			else{
				String name = column.getName();
				name = name + "_1";
				columns.add(new DataColumnSpecCreator(name, column.getType()).createSpec());
			}
			k++;
		}
		
		k = 0;
		for (DataColumnSpec column : inSpec2) {
			if ( k != geomIndex ) {				
				String name = column.getName();
				//name = name + "_2";
				columns.add(new DataColumnSpecCreator(name, column.getType()).createSpec());
			}
			k++;
		}
		
		columns.add(new DataColumnSpecCreator(Constants.OVID, IntCell.TYPE).createSpec());
		
		return new DataTableSpec(columns.toArray(new DataColumnSpec[0]));
	}

}

