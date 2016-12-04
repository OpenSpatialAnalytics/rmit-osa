package org.knime.geo.shpmerge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
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
import org.knime.gdalutils.Utility;
import org.knime.geoutils.ShapeFileFeatureExtractor;
import org.knime.geoutils.WriteShapefile;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.operation.overlay.snap.GeometrySnapper;
import com.vividsolutions.jts.precision.GeometryPrecisionReducer;

/**
 * This is the model implementation of ShapeMerge.
 * 
 *
 * @author 
 */
public class ShapeMergeNodeModel extends NodeModel {
    
    /**
     * Constructor for the node model.
     */
    protected ShapeMergeNodeModel() {
    
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
		
		
		List<String> shapeFiles = new ArrayList<String>();
		for (DataRow r : inTable) {
	    	StringCell inPathCell = (StringCell)r.getCell(inTable.getSpec().findColumnIndex(Utility.LOC_COLUMN));
	    	String inPath = inPathCell.getStringValue();
	    	shapeFiles.add(inPath);	    		    		    	
		}
		
		String shpFile = Utility.MergeShapeFiles(shapeFiles);
    	DataCell[] cells = new DataCell[outSpec.getNumColumns()];
		cells[0] = new StringCell(shpFile);
		container.addRowToTable(new DefaultRow("Row0", cells));	
		
		
		SimpleFeatureCollection collection = 
	        		ShapeFileFeatureExtractor.getShapeFeature(shpFile);
		
		SimpleFeatureType type = collection.getSchema();
		
		SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
		typeBuilder.init(type);		
		typeBuilder.setName("newfeatureType");		
		typeBuilder.add("Index", Integer.class);
		SimpleFeatureType newFeatureType = typeBuilder.buildFeatureType();				
		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(newFeatureType);
						
		SimpleFeatureIterator iterator = collection.features();
		int i = 0;
		List<SimpleFeature> feats = new ArrayList<SimpleFeature>();
		
		while (iterator.hasNext()){
			SimpleFeature feature = iterator.next();
			Geometry geo = (Geometry)feature.getDefaultGeometryProperty().getValue();
			PrecisionModel pm = new PrecisionModel(1.0);
			geo = GeometryPrecisionReducer.reduce(geo, pm);
			featureBuilder.add(geo);
			featureBuilder.add(feature.getAttribute(Utility.RANK));
			featureBuilder.add(i+1);		
			feature = featureBuilder.buildFeature(null);
			feats.add(feature);
			i++;
		}
		
		String fname = shpFile.substring(0,shpFile.lastIndexOf("/")) + "/mergedsurveys.shp";
		FeatureCollection<SimpleFeatureType, SimpleFeature> features = new ListFeatureCollection(newFeatureType, feats);
		File file = new File(fname);
		WriteShapefile writer = new WriteShapefile(file);
		writer.writeFeatures(features);
		
		
		//exec.checkCanceled();
		//exec.setProgress((double) index / (double) inTable.size());				
					
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
    	if (!Arrays.asList(columNames).contains(Utility.LOC_COLUMN)){
			throw new InvalidSettingsException( "Input table must contain Location column");
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

