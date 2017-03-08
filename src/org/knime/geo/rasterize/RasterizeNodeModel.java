package org.knime.geo.rasterize;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
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
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.gdalutils.Utility;
import org.knime.geo.resample.DirectoryFormat;
import org.knime.geoutils.Constants;

/**
 * This is the model implementation of Rasterize.
 * 
 *
 * @author Forkan
 */
public class RasterizeNodeModel extends NodeModel {
	
	
	static final String XRES = "xres";
	static final String YRES = "yres";
	static final String OUTPATH = "output_file_path";
	static final String BURN = "burn_value";
	static final String ATTR = "attribute_name";
	static final String TAP = "tap";
	static final String ND = "a_nodata";
	static final String OT = "output_type"; 
	static final String OF = "output_format";
	static final String RC = "run_command";
	
    public final SettingsModelString xRes = new SettingsModelString(XRES,"");
    public final SettingsModelString yRes = new SettingsModelString(YRES,"");
    public final SettingsModelString outPath = new SettingsModelString(OUTPATH,"");
    public final SettingsModelString burn = new SettingsModelString(BURN,"");
    public final SettingsModelString attr = new SettingsModelString(ATTR,"");
    public final SettingsModelBoolean tap = new SettingsModelBoolean(TAP,false);
    public final SettingsModelString noDataValue = new SettingsModelString(ND,"");
    public final SettingsModelString outputType = new SettingsModelString(OT,"Byte");
    public final SettingsModelString outputFormat = new SettingsModelString(OF,"GTiff");
    public final SettingsModelBoolean rc = new SettingsModelBoolean(RC,false);
   
    
    /**
     * Constructor for the node model.
     */
    protected RasterizeNodeModel() {
    
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
		
		FileUtils.cleanDirectory(new File(outPath.getStringValue())); 
		
		int locIndex = inTable.getSpec().findColumnIndex(Utility.LOC_COLUMN);
		int index = 0;
		
		for (DataRow r : inTable ){
			
			StringCell inPathCell = (StringCell)r.getCell(locIndex);
	    	String inFile = inPathCell.getStringValue();
	    	
	    	String outFile = Utility.Rasterize(inFile, outPath.getStringValue(), 
	    			xRes.getStringValue(), yRes.getStringValue(), 
	    			burn.getStringValue(), attr.getStringValue(), 
	    			noDataValue.getStringValue(), outputType.getStringValue(), 
	    			outputFormat.getStringValue(), tap.getBooleanValue(), rc.getBooleanValue());
	    	
	    	DataCell[] cells = new DataCell[outSpec.getNumColumns()];
	    	cells[locIndex] = new StringCell(outFile);
	    	for ( int col = 0; col < inTable.getSpec().getNumColumns(); col++ ) {	
				if (col != locIndex) {
					cells[col] = r.getCell(col);
				}
    		}
	    	
	    	exec.checkCanceled();
			container.addRowToTable(new DefaultRow("Row"+index, cells));
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

    	if (outPath.getStringValue() == null) {
			throw new InvalidSettingsException("No output path specified");
		}
    	
    	if (xRes.getStringValue() != null) {
	    	try{
	    			double x = Double.parseDouble(xRes.getStringValue());
	    			double y = Double.parseDouble(yRes.getStringValue());
	    	}
	    	catch(NumberFormatException e)
	    	{
	    		throw new InvalidSettingsException("X and Y resolution must contain numerical value");
	    	}
    	}
    	
    	if (noDataValue.getJavaUnescapedStringValue() != null ){
    		
    		try{
    			double nv = Double.parseDouble(noDataValue.getStringValue());
	    	}
	    	catch(NumberFormatException e)
	    	{
	    		throw new InvalidSettingsException("No Data value must contain valid numerical value");
	    	}
    	}
    	
        return new DataTableSpec[]{null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	
         this.xRes.saveSettingsTo(settings);
         this.yRes.saveSettingsTo(settings);
         this.burn.saveSettingsTo(settings);
         this.attr.saveSettingsTo(settings);
         this.noDataValue.saveSettingsTo(settings);
         this.outPath.saveSettingsTo(settings);
         this.outputFormat.saveSettingsTo(settings);
         this.outputType.saveSettingsTo(settings);
         this.tap.saveSettingsTo(settings);
         this.rc.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	
    	 this.xRes.loadSettingsFrom(settings);
         this.yRes.loadSettingsFrom(settings);
         this.burn.loadSettingsFrom(settings);
         this.attr.loadSettingsFrom(settings);
         this.noDataValue.loadSettingsFrom(settings);
         this.outPath.loadSettingsFrom(settings);
         this.outputFormat.loadSettingsFrom(settings);
         this.outputType.loadSettingsFrom(settings);
         this.tap.loadSettingsFrom(settings);
         this.rc.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	
    	 this.xRes.validateSettings(settings);
         this.yRes.validateSettings(settings);
         this.burn.validateSettings(settings);
         this.attr.validateSettings(settings);
         this.noDataValue.validateSettings(settings);
         this.outPath.validateSettings(settings);
         this.outputFormat.validateSettings(settings);
         this.outputType.validateSettings(settings);
         this.tap.validateSettings(settings);
         this.rc.validateSettings(settings);
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

