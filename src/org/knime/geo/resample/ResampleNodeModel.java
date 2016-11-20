package org.knime.geo.resample;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.StringValue;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowIterator;
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

import org.knime.gdalutils.*;



/**
 * This is the model implementation of Resample.
 * 
 *
 * @author Forkan
 */
public class ResampleNodeModel extends NodeModel {
    
		static final String OR = "over_write";
		static final String RM = "resample_method";
		static final String WM = "working_memory";
		static final String XRES = "xres";
		static final String YRES = "yres";
		static final String OUTPATH = "output_path";
		static final String OF = "output_format";
		static final String TAP = "tap";
		static final String S_SRS = "src_srs";
		static final String T_SRS = "t_srs";
	 	
	    public final SettingsModelString resampleMethod = new SettingsModelString(RM,"average");
	    public final SettingsModelString workingMemory = new SettingsModelString(WM,"500");
	    public final SettingsModelString xRes = new SettingsModelString(XRES,"25");
	    public final SettingsModelString yRes = new SettingsModelString(YRES,"25");
	    public final SettingsModelBoolean overWrite = new SettingsModelBoolean(OR,true);
	    public final SettingsModelString outPath = new SettingsModelString(OUTPATH,"");
	    public final SettingsModelString outputFormat = new SettingsModelString(OF,"GTiff");
	    public final SettingsModelString location = new SettingsModelString(Utility.LOC_COLUMN,"Location");
	    
	    public final SettingsModelBoolean tap = new SettingsModelBoolean(TAP,false);
	    public final SettingsModelString s_srs = new SettingsModelString(S_SRS,"");
	    public final SettingsModelString t_srs = new SettingsModelString(T_SRS,"");
	
	
	/**
     * Constructor for the node model.
     */
    protected ResampleNodeModel() {
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
    	
    	RowIterator ri = inTable.iterator();
    	long numFiles = inTable.size();
    	int index = 0;
    	
    	while (ri.hasNext()){
    	
	    	DataRow r = ri.next();
	    	StringCell inPathCell = (StringCell)r.getCell(inTable.getSpec().findColumnIndex(Utility.LOC_COLUMN));
	    	String inPath = inPathCell.getStringValue();
	    	
	    	boolean isZip = false;
	    	
	    	if (inPath.toLowerCase().contains(".zip"))
	    		isZip = true;
	    	
			String outFile = Utility.ReSampleRaster(inPath, outPath.getStringValue(), 
					overWrite.getBooleanValue(), tap.getBooleanValue(),
					resampleMethod.getStringValue(), workingMemory.getStringValue(), 
					outputFormat.getStringValue(),s_srs.getStringValue(),
					t_srs.getStringValue(), xRes.getStringValue(), yRes.getStringValue(), isZip);
			
			DataCell[] cells = new DataCell[outSpec.getNumColumns()];
			int rankIndex = inTable.getSpec().findColumnIndex(Utility.RANK);
			int locIndex = inTable.getSpec().findColumnIndex(Utility.LOC_COLUMN);
			//System.out.println("My Rank.........................." + rankIndex);
			if (rankIndex != -1)
				cells[rankIndex] = r.getCell(rankIndex);
			cells[locIndex] = new StringCell(outFile);
			
	    	exec.checkCanceled();
			container.addRowToTable(new DefaultRow("Row"+index, cells));
			exec.setProgress((double) index / (double) numFiles);
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
    
        return new DataTableSpec[]{null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
       
    	this.resampleMethod.saveSettingsTo(settings);
    	this.workingMemory.saveSettingsTo(settings);
    	this.overWrite.saveSettingsTo(settings);
    	this.xRes.saveSettingsTo(settings);
    	this.yRes.saveSettingsTo(settings);
    	this.outputFormat.saveSettingsTo(settings);
    	this.outPath.saveSettingsTo(settings);
    	this.s_srs.saveSettingsTo(settings);
    	this.t_srs.saveSettingsTo(settings);
    	this.tap.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {

    	this.resampleMethod.loadSettingsFrom(settings);
    	this.workingMemory.loadSettingsFrom(settings);
    	this.overWrite.loadSettingsFrom(settings);
    	this.xRes.loadSettingsFrom(settings);
    	this.yRes.loadSettingsFrom(settings);
    	this.outputFormat.loadSettingsFrom(settings);
    	this.outPath.loadSettingsFrom(settings);
    	this.s_srs.loadSettingsFrom(settings);
    	this.tap.loadSettingsFrom(settings);
    	this.t_srs.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
       
    	this.resampleMethod.validateSettings(settings);
    	this.workingMemory.validateSettings(settings);
    	this.overWrite.validateSettings(settings);
    	this.xRes.validateSettings(settings);
    	this.yRes.validateSettings(settings);
    	this.outputFormat.validateSettings(settings);
    	this.outPath.validateSettings(settings);
    	this.tap.validateSettings(settings);
    	this.s_srs.validateSettings(settings);
    	this.t_srs.validateSettings(settings);
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
			if((column.getName().compareTo(Utility.LOC_COLUMN)==0) || (column.getName().compareTo(Utility.RANK)==0))
				columns.add(column);			
		}
		return new DataTableSpec(columns.toArray(new DataColumnSpec[0]));
	}
    
}

