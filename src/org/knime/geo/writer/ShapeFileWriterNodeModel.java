package org.knime.geo.writer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geojson.geom.GeometryJSON;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.geoutils.ShapeFileOperations;
import org.knime.geoutils.WriteShapefile;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;

import com.vividsolutions.jts.geom.Geometry;

/**
 * This is the model implementation of ShapeFileWriter.
 * 
 *
 * @author Forkan
 */
public class ShapeFileWriterNodeModel extends NodeModel {
	
	  static final String CFG_LOC = "FilePath";
	    public final SettingsModelString shpFileLoc =
		        new SettingsModelString(CFG_LOC,"");

    
    /**
     * Constructor for the node model.
     */
    protected ShapeFileWriterNodeModel() {
    
        super(1, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
    	
    	String fname=shpFileLoc.getStringValue().concat(".shp");
    	BufferedDataTable geometryTable = inData[0];
    	
    	DataRow firstRow =  geometryTable.iterator().next();
    	DataCell firstCell = firstRow.getCell(0);
    	String jsonStr = ((StringValue) firstCell).getStringValue();
    	String geomType = getGeomType(jsonStr);
    	
    	SimpleFeatureType schemaDef = 
				DataUtilities.createType(geomType, "the_geom:"+geomType+":srid=4326,name:String,timestamp:java.util.Date");
		
    	List<SimpleFeature> feats = new ArrayList<SimpleFeature>();
    	
    	try{

    		for (DataRow row : geometryTable) {
    			
	    		DataCell geometryCell = row.getCell(0);
	    		
	    		if (geometryCell instanceof StringValue){
	    			String geoJsonString = ((StringValue) geometryCell).getStringValue();
	    			Geometry geo = new GeometryJSON().read(geoJsonString);
	    			SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(schemaDef);
					featureBuilder.add(geo);
					SimpleFeature feature = featureBuilder.buildFeature(null);
					feats.add(feature);
	    			
	    		}
    		}
    		
    		FeatureCollection<SimpleFeatureType, SimpleFeature> features = new ListFeatureCollection(schemaDef, feats);
			File file = new File(fname);
			WriteShapefile writer = new WriteShapefile(file);
			writer.writeFeatures(features);
    		
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
		}
    	
    	
        return new BufferedDataTable[]{};
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
    	shpFileLoc.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	shpFileLoc.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        shpFileLoc.validateSettings(settings);
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
    
    
    private String getGeomType(String jsonStr)
    {
    	String[] type = jsonStr.split(":");
		String[] typeStr = type[1].split(",");
		String geomString = typeStr[0].substring(1, typeStr[0].length()-1);
    	return geomString;
    }

}

