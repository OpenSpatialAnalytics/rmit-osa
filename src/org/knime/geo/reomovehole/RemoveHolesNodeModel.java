package org.knime.geo.reomovehole;

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
import org.knime.geoutils.Constants;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * This is the model implementation of RemoveHoles.
 * 
 *
 * @author 
 */
public class RemoveHolesNodeModel extends NodeModel {
    
    /**
     * Constructor for the node model.
     */
    protected RemoveHolesNodeModel() {
    
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
    	int numberOfColumns = inTable.getSpec().getNumColumns();
    	
    	DataTableSpec outSpec = createSpec(inTable.getSpec());
    	BufferedDataContainer container = exec.createDataContainer(outSpec);
    	
    	int index = 0;
    	for (DataRow row : inTable) {	    		
    		DataCell[] cells = new DataCell[outSpec.getNumColumns()];	    		   		
    		DataCell geometryCell = row.getCell(geomIndex);
    		if (geometryCell instanceof StringValue){
    			String geoJsonString = ((StringValue) geometryCell).getStringValue();
    			Geometry geo = new GeometryJSON().read(geoJsonString);
    			Geometries geomType = Geometries.get(geo);	    			
    			if (geomType == Geometries.MULTIPOLYGON){	
    				MultiPolygon  mp = (MultiPolygon)geo;
    				Polygon [] polygons = new Polygon[mp.getNumGeometries()];
    				for (int i = 0; i < mp.getNumGeometries(); i++ ){
    					Polygon poly = (Polygon) mp.getGeometryN(i);
    					if ( poly.getNumInteriorRing() > 0){
    						Coordinate[] coordinates = poly.getExteriorRing().getCoordinates();
    						LinearRing linearRing = new GeometryFactory().createLinearRing(coordinates);
    						polygons[i]= new GeometryFactory().createPolygon(linearRing);
    					}
    					else {
    						polygons[i] = poly;
    		            }
    				}
    				
    				MultiPolygon newMP = new GeometryFactory().createMultiPolygon(polygons);
    				GeometryJSON json = new GeometryJSON(Constants.JsonPrecision);
					String str = json.toString(newMP);
					cells[geomIndex] = new StringCell(str);
    			}
    			
    			else if (geomType == Geometries.POLYGON){
    				Polygon poly = (Polygon) geo;
    				if ( poly.getNumInteriorRing() > 0){
						Coordinate[] coordinates = poly.getExteriorRing().getCoordinates();
						LinearRing linearRing = new GeometryFactory().createLinearRing(coordinates);
						poly = new GeometryFactory().createPolygon(linearRing);
					}
    				GeometryJSON json = new GeometryJSON(Constants.JsonPrecision);
					String str = json.toString(poly);
					cells[geomIndex] = new StringCell(str);
    			}
    			else{
    				GeometryJSON json = new GeometryJSON(Constants.JsonPrecision);
					String str = json.toString(geo);
					cells[geomIndex] = new StringCell(str);
    			}
    			
				for ( int col = 0; col < numberOfColumns; col++ ) {	
					if ( col != geomIndex ) {
						cells[col] = row.getCell(col);
					}
	    		}
    			
    		}
    		container.addRowToTable(new DefaultRow("Row"+index, cells));
    		exec.checkCanceled();
			exec.setProgress((double) index / (double) inTable.size());
			index++;
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

