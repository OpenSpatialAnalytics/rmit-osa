package org.knime.geo.visualizer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geojson.geom.GeometryJSON;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.StringValue;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.geoutils.WriteShapefile;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;

import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;


/**
 * This is the model implementation of GeoMapViewer.
 * 
 *
 * @author Forkan
 */
public class GeoMapViewerNodeModel extends NodeModel {
    
	 static final String CFG_SHP_FILE = "ShpFile";
	 public final SettingsModelString shpFile =
		        new SettingsModelString(CFG_SHP_FILE,"temp"+((int)Math.random()*1000)+".shp");

	
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(GeoMapViewerNodeModel.class);
        
    protected FeatureCollection<SimpleFeatureType, SimpleFeature> features;
    
    /**
     * Constructor for the node model.
     */
    protected GeoMapViewerNodeModel() {
    
        // TODO one incoming port and one outgoing port is assumed
        super(1,0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
    	
    	BufferedDataTable geometryTable = inData[0];

    	DataRow firstRow =  geometryTable.iterator().next();
    	DataCell firstCell = firstRow.getCell(0);
    	String jsonStr = ((StringValue) firstCell).getStringValue();
    	String geomType = getGeomType(jsonStr);
    	if (geomType.compareTo("Polygon") == 0)
    		geomType = "MultiPolygon";
    	
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
    		
    		features = new ListFeatureCollection(schemaDef, feats);
    		//FeatureCollection<SimpleFeatureType, SimpleFeature> features = new ListFeatureCollection(schemaDef, feats);
    		//File file = new File(fname);
			//WriteShapefile writer = new WriteShapefile(file);
			//writer.writeFeatures(features);
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
        // TODO Code executed on reset.
        // Models build during execute are cleared here.
        // Also data handled in load/saveInternals will be erased here.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
        
        // TODO: check if user settings are available, fit to the incoming
        // table structure, and the incoming types are feasible for the node
        // to execute. If the node can execute in its current state return
        // the spec of its output data table(s) (if you can, otherwise an array
        // with null elements), or throw an exception with a useful user message

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {

    	this.shpFile.saveSettingsTo(settings);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
    	this.shpFile.loadSettingsFrom(settings);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
        this.shpFile.validateSettings(settings);

    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        
        // TODO load internal data. 
        // Everything handed to output ports is loaded automatically (data
        // returned by the execute method, models loaded in loadModelContent,
        // and user settings set through loadSettingsFrom - is all taken care 
        // of). Load here only the other internals that need to be restored
        // (e.g. data used by the views).

    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
       
        // TODO save internal models. 
        // Everything written to output ports is saved automatically (data
        // returned by the execute method, models saved in the saveModelContent,
        // and user settings saved through saveSettingsTo - is all taken care 
        // of). Save here only the other internals that need to be preserved
        // (e.g. data used by the views).

    }
    
    private String getGeomType(String jsonStr)
    {
    	String[] type = jsonStr.split(":");
		String[] typeStr = type[1].split(",");
		String geomString = typeStr[0].substring(1, typeStr[0].length()-1);
    	return geomString;
    }

}

