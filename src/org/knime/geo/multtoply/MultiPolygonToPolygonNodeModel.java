package org.knime.geo.multtoply;

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
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.geoutils.Constants;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * This is the model implementation of MultiPolygonToPolygon.
 * 
 *
 * @author 
 */
public class MultiPolygonToPolygonNodeModel extends NodeModel {
    
    /**
     * Constructor for the node model.
     */
    protected MultiPolygonToPolygonNodeModel() {
           
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
	    		DataCell geometryCell = row.getCell(geomIndex);
	    		
	    		//logger.info("geometryCell:" + geometryCell.toString());
	    		
	    		if (geometryCell instanceof StringValue){
	    			String geoJsonString = ((StringValue) geometryCell).getStringValue();
	    			Geometry geo = new GeometryJSON().read(geoJsonString);
	    			Geometries geomType = Geometries.get(geo);	    			
	    			if (geomType == Geometries.MULTIPOLYGON){	
	    				MultiPolygon  mp = (MultiPolygon)geo;
	    				for (int i = 0; i < mp.getNumGeometries(); i++ ){
	    					Polygon poly = (Polygon) mp.getGeometryN(i);	    					
	    					GeometryJSON json = new GeometryJSON(Constants.JsonPrecision);
	    					String str = json.toString(poly);
	    					DataCell[] cells = new DataCell[outSpec.getNumColumns()];	
	    					cells[geomIndex] = new StringCell(str);
	    					for ( int col = 0; col < numberOfColumns; col++ ) {	
	    						if (col != geomIndex ) {
	    		    				cells[col] = row.getCell(col);
	    						}
	    		    		}
	    					container.addRowToTable(new DefaultRow("Row"+index, cells));
	    		    		exec.checkCanceled();
	    					exec.setProgress((double) index / (double) inTable.size());
	    					index++;	
	    				}		
	    			}
	    			else if(geomType == Geometries.POLYGON) {
	    				Polygon poly = (Polygon)geo;	    				
	    				GeometryJSON json = new GeometryJSON(Constants.JsonPrecision);
    					String str = json.toString(poly);
    					DataCell[] cells = new DataCell[outSpec.getNumColumns()];
    					cells[geomIndex] = new StringCell(str);
    					for ( int col = 0; col < numberOfColumns; col++ ) {	
    						if (col != geomIndex ) {
    		    				cells[col] = row.getCell(col);
    						}
    		    		}
    					container.addRowToTable(new DefaultRow("Row"+index, cells));
    		    		exec.checkCanceled();
    					exec.setProgress((double) index / (double) inTable.size());
    					index++;
	    			}
	    			else if (geomType == Geometries.MULTILINESTRING){	
	    				MultiLineString  ml = (MultiLineString)geo;
	    				for (int i = 0; i < ml.getNumGeometries(); i++ ){
	    					LineString line = (LineString) ml.getGeometryN(i);	    					
	    					GeometryJSON json = new GeometryJSON(Constants.JsonPrecision);
	    					String str = json.toString(line);
	    					DataCell[] cells = new DataCell[outSpec.getNumColumns()];	
	    					cells[geomIndex] = new StringCell(str);
	    					for ( int col = 0; col < numberOfColumns; col++ ) {	
	    						if (col != geomIndex ) {
	    		    				cells[col] = row.getCell(col);
	    						}
	    		    		}
	    					container.addRowToTable(new DefaultRow("Row"+index, cells));
	    		    		exec.checkCanceled();
	    					exec.setProgress((double) index / (double) inTable.size());
	    					index++;
	    				}		
	    			}
	    			else if(geomType == Geometries.LINESTRING) {
	    				LineString line = (LineString)geo;	    				
	    				GeometryJSON json = new GeometryJSON(Constants.JsonPrecision);
    					String str = json.toString(line);
    					DataCell[] cells = new DataCell[outSpec.getNumColumns()];
    					cells[geomIndex] = new StringCell(str);
    					for ( int col = 0; col < numberOfColumns; col++ ) {	
    						if (col != geomIndex ) {
    		    				cells[col] = row.getCell(col);
    						}
    		    		}
    					container.addRowToTable(new DefaultRow("Row"+index, cells));
    		    		exec.checkCanceled();
    					exec.setProgress((double) index / (double) inTable.size());
    					index++;
	    			}
	    			else if (geomType == Geometries.MULTIPOINT){	
	    				MultiPoint  mp = (MultiPoint)geo;
	    				for (int i = 0; i < mp.getNumGeometries(); i++ ){
	    					Point point = (Point) mp.getGeometryN(i);	    					
	    					GeometryJSON json = new GeometryJSON(Constants.JsonPrecision);
	    					String str = json.toString(point);
	    					DataCell[] cells = new DataCell[outSpec.getNumColumns()];
	    					cells[geomIndex] = new StringCell(str);
	    					for ( int col = 0; col < numberOfColumns; col++ ) {	
	    						if (col != geomIndex ) {
	    		    				cells[col] = row.getCell(col);
	    						}
	    		    		}
	    					container.addRowToTable(new DefaultRow("Row"+index, cells));
	    		    		exec.checkCanceled();
	    					exec.setProgress((double) index / (double) inTable.size());
	    					index++;
	    				}		
	    			}
	    			else if(geomType == Geometries.POINT) {
	    				Point point = (Point)geo;	    				
	    				GeometryJSON json = new GeometryJSON(Constants.JsonPrecision);
    					String str = json.toString(point);
    					DataCell[] cells = new DataCell[outSpec.getNumColumns()];
    					cells[geomIndex] = new StringCell(str);
    					for ( int col = 0; col < numberOfColumns; col++ ) {	
    						if (col != geomIndex ) {
    		    				cells[col] = row.getCell(col);
    						}
    		    		}
    					container.addRowToTable(new DefaultRow("Row"+index, cells));
    		    		exec.checkCanceled();
    					exec.setProgress((double) index / (double) inTable.size());
    					index++;
	    			}
	    			else if(geomType == Geometries.GEOMETRYCOLLECTION) {
	    				for (int i = 0; i < geo.getNumGeometries(); i++ ){	    					
	    					
	    					Geometry g = geo.getGeometryN(i);
	    					Geometries gtype = Geometries.get(g);
	    					if(gtype==Geometries.MULTIPOLYGON ||gtype==Geometries.MULTILINESTRING || gtype==Geometries.MULTIPOINT){
	    						for (int j = 0; j < geo.getNumGeometries(); j++ ){
	    							GeometryJSON json = new GeometryJSON(Constants.JsonPrecision);
	    	    					String str = json.toString(g.getGeometryN(i));
	    	    					DataCell[] cells = new DataCell[outSpec.getNumColumns()];
	    	    					cells[geomIndex] = new StringCell(str);
	    	    					for ( int col = 0; col < numberOfColumns; col++ ) {	
	    	    						if (col != geomIndex ) {
	    	    		    				cells[col] = row.getCell(col);
	    	    						}
	    	    		    		}
	    	    					container.addRowToTable(new DefaultRow("Row"+index, cells));
			    		    		exec.checkCanceled();
			    					exec.setProgress((double) index / (double) inTable.size());
			    					index++;
	    						}
	    					}
	    					else{
	    						GeometryJSON json = new GeometryJSON(Constants.JsonPrecision);
		    					String str = json.toString(g);
		    					DataCell[] cells = new DataCell[outSpec.getNumColumns()];
		    					cells[geomIndex] = new StringCell(str);
		    					for ( int col = 0; col < numberOfColumns; col++ ) {	
		    						if (col != geomIndex ) {
		    		    				cells[col] = row.getCell(col);
		    						}
		    		    		}
		    					container.addRowToTable(new DefaultRow("Row"+index, cells));
		    		    		exec.checkCanceled();
		    					exec.setProgress((double) index / (double) inTable.size());
		    					index++;
	    					}
	    	
	    				}
	    			
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

