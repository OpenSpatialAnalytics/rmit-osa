package org.knime.geo.polygontoline;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.Geometries;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.data.def.DefaultRow;
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
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

import org.knime.geoutils.Constants;

/**
 * This is the model implementation of PolygonToLine.
 * 
 *
 * @author Forkan
 */
public class PolygonToLineNodeModel extends NodeModel {
    
	
	 private static final NodeLogger logger = NodeLogger
	            .getLogger(PolygonToLineNodeModel.class);
	
	
    /**
     * Constructor for the node model.
     */
    protected PolygonToLineNodeModel() {
    
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
    	DataTableSpec outSpec = createSpec(inTable.getSpec());
    	BufferedDataContainer container = exec.createDataContainer(outSpec);
    	
    	int geomIndex = inTable.getSpec().findColumnIndex(Constants.GEOM);	
    	int numberOfColumns = inTable.getSpec().getNumColumns();
    	    	    	    	    	
    	try{
    		int index = 0;
	    	for (DataRow row : inTable) {	    		
	    		DataCell[] cells = new DataCell[outSpec.getNumColumns()];	    		    		
	    		DataCell geometryCell = row.getCell(geomIndex);
	    		
	    		//logger.info("geometryCell:" + geometryCell.toString());
	    		
	    		if (geometryCell instanceof StringValue){
	    			String geoJsonString = ((StringValue) geometryCell).getStringValue();
	    			Geometry geo = new GeometryJSON().read(geoJsonString);
	    			Geometries geomType = Geometries.get(geo);	    			
	    			if (geomType == Geometries.MULTIPOLYGON){	
	    				MultiPolygon  mp = (MultiPolygon)geo;
	    				LineString[] lineStrings = new LineString[mp.getNumGeometries()];
	    				for (int i = 0; i < mp.getNumGeometries(); i++ ){
	    					Polygon poly = (Polygon) mp.getGeometryN(i);
	    					LineString linestring = poly.getExteriorRing();
	    					lineStrings[i] = linestring;
	    				}		
	    				
	    				MultiLineString ms = new GeometryFactory().createMultiLineString(lineStrings);
	    				GeometryJSON json = new GeometryJSON(Constants.JsonPrecision);
    					String str = json.toString(ms);
    					cells[geomIndex] = new StringCell(str);
    					for ( int col = 0; col < numberOfColumns; col++ ) {	
    						if (col != geomIndex ) {
    		    				cells[col] = row.getCell(col);
    						}
    		    		}
	    			}
	    			else if(geomType == Geometries.POLYGON) {
	    				Polygon poly = (Polygon)geo;
	    				LineString linestring = poly.getExteriorRing();	
	    				GeometryJSON json = new GeometryJSON(Constants.JsonPrecision);
    					String str = json.toString(linestring);
    					cells[geomIndex] = new StringCell(str);
    					for ( int col = 0; col < numberOfColumns; col++ ) {	
    						if (col != geomIndex ) {
    		    				cells[col] = row.getCell(col);
    						}
    		    		}
	    			}
	    				
	    		}
	    			    			    			    			    			    		
	    		container.addRowToTable(new DefaultRow(row.getKey(), cells));
	    		exec.checkCanceled();
				exec.setProgress((double) index / (double) inTable.size());
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

    	String columNames[] = inSpecs[0].getColumnNames();
    	if (!Arrays.asList(columNames).contains(Constants.GEOM)){
			throw new InvalidSettingsException( "Input table must contain a geometry column (the_geom)");
		}
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

