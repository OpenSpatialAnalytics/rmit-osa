package org.knime.geo.unary;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.FactoryFinder;
import org.geotools.geometry.jts.Geometries;
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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.union.UnaryUnionOp;

/**
 * This is the model implementation of UnaryUnion.
 * 
 *
 * @author Forkan
 */
public class UnaryUnionNodeModel extends NodeModel {
    
    /**
     * Constructor for the node model.
     */
    protected UnaryUnionNodeModel() {
    
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
    	    	        	    	    	    	    	
    	try{    	
	    	for (int i = 0; i < inTable.size(); i++ ) {
	    		
	    		DataRow r = ri.next();				    		
	    		DataCell geometryCell = r.getCell(geomIndex);
	    		
	    		
	    		if ( (geometryCell instanceof StringValue) ){
	    			String geoJsonString = ((StringValue) geometryCell).getStringValue();	    			
	    			Geometry g = new GeometryJSON().read(geoJsonString);
	    			Geometries geomType = Geometries.get(g);
	    				    			
	    			Geometry geo = null;
	    			if (geomType == Geometries.GEOMETRYCOLLECTION){
	    				List <Geometry> geometries = new LinkedList<Geometry>();
	    				for (int j = 0; j < g.getNumGeometries(); j++ ){	
	    					Geometry g1 = g.getGeometryN(j);
	    					geometries.add(g1);
	    				}
	    				UnaryUnionOp uOp = new UnaryUnionOp(geometries); 
	    			    geo = uOp.union(); 	    					    				
	    			}
	    			else {
	    				List <Geometry> geometries = new ArrayList<Geometry>();
	    				for (int j = 0; j < g.getNumGeometries(); j++ ){	
	    					Geometry g1 = g.getGeometryN(j);
	    					geometries.add(g1);
	    				}
	    				geo = UnaryUnionOp.union(geometries);
	    			}
	    			
	    			/*
	    			geomType = Geometries.get(geo);
	    			if (geomType == Geometries.MULTIPOLYGON){
	    				Polygon[] polygons = new Polygon[geo.getNumGeometries()];
	    				for (int j = 0; j < geo.getNumGeometries(); j++ ){
	    					polygons[j] = (Polygon) geo.getGeometryN(j);	    					
	    				}	    				
	    				GeometryFactory factory = FactoryFinder.getGeometryFactory( null );
	    				geo = new MultiPolygon(polygons, factory);
	    			}
	    			*/
	    					    				    			    				    			  				    			
	    			//Geometry geo = g.union();
	    			//Geometry geo = UnaryUnionOp.union(geometries);
	    			//GeometryFactory factory = FactoryFinder.getGeometryFactory( null );	
	    			//GeometryCollection geometryCollection =
	    			          //(GeometryCollection) factory.buildGeometry( geometries );	    			
	    			//Geometry geo = geometryCollection.union();
	    			
	    			GeometryJSON json = new GeometryJSON(Constants.JsonPrecision);
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

