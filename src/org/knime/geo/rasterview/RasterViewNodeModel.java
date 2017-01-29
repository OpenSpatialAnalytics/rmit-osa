package org.knime.geo.rasterview;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * This is the model implementation of RasterView.
 * 
 *
 * @author 
 */
public class RasterViewNodeModel extends NodeModel {
    
    /**
     * Constructor for the node model.
     */
	
	 public static final String RASTER = "raster_file";
	 public static final String SHP = "shape_file";
	 public final SettingsModelString rasterFile =  new SettingsModelString(RASTER,"");
	 public final SettingsModelString shpFile =  new SettingsModelString(SHP,"");
		       
	
    protected RasterViewNodeModel() {
    
        // TODO: Specify the amount of input and output ports needed.
        super(0, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

        // TODO: Return a BufferedDataTable for each output port 
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

    	if (rasterFile.getStringValue() == null) {
			throw new InvalidSettingsException("No raster file name specified");
		}
        return new DataTableSpec[]{null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        this.rasterFile.saveSettingsTo(settings);
        this.shpFile.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        	this.rasterFile.loadSettingsFrom(settings);
        	this.shpFile.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        this.rasterFile.validateSettings(settings);
        this.shpFile.validateSettings(settings);
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
    
    protected String getRasterFileName()
    {
    	
    	return this.rasterFile.getStringValue();
    }

    
    protected String getShapeFileName()
    {
    	
    	return this.shpFile.getStringValue();
    }

}

