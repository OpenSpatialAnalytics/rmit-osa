package org.knime.geo.linepoint;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.knime.geoutils.Constants;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * This is the model implementation of LinePoints.
 * 
 *
 * @author Forkan
 */
public class LinePointsNodeModel extends NodeModel {
    
    /**
     * Constructor for the node model.
     */
    protected LinePointsNodeModel() {
    
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
    	
    	DataTableSpec outSpec = createSpec(inTable.getSpec(),geomIndex);
    	BufferedDataContainer container = exec.createDataContainer(outSpec);
    	
    	int index = 0;
    	for (DataRow row : inTable) {	    		
    		DataCell[] cells1 = new DataCell[outSpec.getNumColumns()];	  
    		DataCell[] cells2 = new DataCell[outSpec.getNumColumns()];
    		DataCell geometryCell = row.getCell(geomIndex);
    		if (geometryCell instanceof StringValue){
    			String geoJsonString = ((StringValue) geometryCell).getStringValue();
    			Geometry geo = new GeometryJSON().read(geoJsonString);
    			Geometries gtype = Geometries.get(geo);
    			Point p1 = null;
    			Point p2 = null;
    			if (gtype == Geometries.LINESTRING){
    				LineString ls = (LineString)geo;
    				p1 = ls.getStartPoint();
    				p2 = ls.getEndPoint();
    			}
    			else{
    				throw new Exception();
    			}

				GeometryJSON json = new GeometryJSON();
				String str = json.toString(p1);
				cells1[geomIndex] = new StringCell(str);
				cells1[geomIndex+1] = new IntCell(1);
				
				json = new GeometryJSON();
				str = json.toString(p2);
				cells2[geomIndex] = new StringCell(str);
				cells2[geomIndex+1] = new IntCell(0);
				for ( int col = 0; col < numberOfColumns; col++ ) {	
					if (col != geomIndex ) {
	    				cells1[col+1] = row.getCell(col);
	    				cells2[col+1] = row.getCell(col);
					}
	    		}
    			
    		}
    		container.addRowToTable(new DefaultRow(row.getKey()+"_1", cells1));
    		container.addRowToTable(new DefaultRow(row.getKey()+"_0", cells2));
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
    
    private static DataTableSpec createSpec(DataTableSpec inSpec, int geomIndex) throws InvalidSettingsException {
		
		List<DataColumnSpec> columns = new ArrayList<>();
		columns.add(inSpec.getColumnSpec(geomIndex));
		columns.add(new DataColumnSpecCreator("isStart", IntCell.TYPE).createSpec());

		int k = 0;
		for (DataColumnSpec column : inSpec) {
			if(k != geomIndex)
				columns.add(column);
			k++;
		}
		
		return new DataTableSpec(columns.toArray(new DataColumnSpec[0]));
	}

}

