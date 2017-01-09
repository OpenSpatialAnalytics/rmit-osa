package org.knime.geo.proximity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
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
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.gdalutils.Utility;
import org.knime.geoutils.Constants;


/**
 * This is the model implementation of Proximity.
 * 
 *
 * @author Forkan
 */
public class ProximityNodeModel extends NodeModel {
    
	
	static final String OUTPATH = "output_file_path";	
	static final String ND = "nodata_value";
	static final String OT = "output_type"; 
	static final String OF = "output_format";
	static final String DT = "distance_unit";
	static final String RC = "run_command";
	
   
    public final SettingsModelString outPath = new SettingsModelString(OUTPATH,"");    
    public final SettingsModelString noDataValue = new SettingsModelString(ND,"");
    public final SettingsModelString outputType = new SettingsModelString(OT,"Byte");
    public final SettingsModelString outputFormat = new SettingsModelString(OF,"GTiff");
    public final SettingsModelString distanceUnit = new SettingsModelString(DT,"GEO");
    public final SettingsModelBoolean rc = new SettingsModelBoolean(RC,false);
    
    /**
     * Constructor for the node model.
     */
    protected ProximityNodeModel() {
    
        // TODO one incoming port and one outgoing port is assumed
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
		int locIndex = inTable.getSpec().findColumnIndex(Utility.LOC_COLUMN);
		
		FileUtils.cleanDirectory(new File(outPath.getStringValue())); 
		int index = 0;
		
		for (DataRow r : inTable ){
			
			StringCell inPathCell = (StringCell)r.getCell(locIndex);
	    	String inFile = inPathCell.getStringValue();
	    	
	    	String outFile = Utility.Proximity(inFile, outPath.getStringValue(), 
	    			noDataValue.getStringValue(), outputType.getStringValue(), 
	    			outputFormat.getStringValue(), distanceUnit.getStringValue(), 
	    			rc.getBooleanValue());
	    	
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
        
    	if (outPath.getStringValue() == null) {
			throw new InvalidSettingsException("No output path specified");
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
    	
    	this.outPath.saveSettingsTo(settings);
    	this.outputFormat.saveSettingsTo(settings);
    	this.outputType.saveSettingsTo(settings);
    	this.distanceUnit.saveSettingsTo(settings);
    	this.noDataValue.saveSettingsTo(settings);    	
    	this.rc.saveSettingsTo(settings);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
            
    	this.outPath.loadSettingsFrom(settings);
    	this.outputFormat.loadSettingsFrom(settings);
    	this.outputType.loadSettingsFrom(settings);
    	this.distanceUnit.loadSettingsFrom(settings);
    	this.noDataValue.loadSettingsFrom(settings);    	
    	this.rc.loadSettingsFrom(settings);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
     
    	this.outPath.validateSettings(settings);
    	this.outputFormat.validateSettings(settings);
    	this.outputType.validateSettings(settings);
    	this.distanceUnit.validateSettings(settings);
    	this.noDataValue.validateSettings(settings);    	
    	this.rc.validateSettings(settings);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        
    

    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
       

    }
    
    private static DataTableSpec createSpec(DataTableSpec inSpec) throws InvalidSettingsException {
		
		List<DataColumnSpec> columns = new ArrayList<>();

		for (DataColumnSpec column : inSpec) {
				columns.add(column);			
		}
		return new DataTableSpec(columns.toArray(new DataColumnSpec[0]));
	}

}

