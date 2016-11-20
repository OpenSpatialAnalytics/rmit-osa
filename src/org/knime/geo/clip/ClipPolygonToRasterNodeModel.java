package org.knime.geo.clip;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
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
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.gdalutils.Utility;

/**
 * This is the model implementation of ClipPolygonToRaster.
 * 
 *
 * @author Forkan
 */
public class ClipPolygonToRasterNodeModel extends NodeModel {
    
    /**
     * Constructor for the node model.
     */
	
	static final String OR = "over_write";
	static final String TAP = "tap";
	static final String WO_NAME = "wo_name";
	static final String WO_VALUE = "wo_value";
	static final String XRES = "xres";
	static final String YRES = "yres";
	static final String CWHERE = "cwhere";
	 
	
	 public final SettingsModelBoolean overWrite = new SettingsModelBoolean(OR,true);
	 public final SettingsModelBoolean tap = new SettingsModelBoolean(TAP,false);
	 public final SettingsModelString xRes = new SettingsModelString(XRES,"");
	 public final SettingsModelString yRes = new SettingsModelString(YRES,"");
	 public final SettingsModelString woName = new SettingsModelString(WO_NAME,"");
	 public final SettingsModelString woValue = new SettingsModelString(WO_VALUE,"");
	 public final SettingsModelString cwhere = new SettingsModelString(CWHERE,"");
	
		
	//static final String OUTFILE = "output_file";
	//public final SettingsModelString outFile = new SettingsModelString(OUTFILE,"");
	

    protected ClipPolygonToRasterNodeModel() {
            
        super(2, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
    	
    	
    	BufferedDataTable inTable1 = inData[0];
    	BufferedDataTable inTable2 = inData[1];    	
    	DataTableSpec outSpec = createSpec();
		BufferedDataContainer container = exec.createDataContainer(outSpec);
		
		DataRow r1 = inTable1.iterator().next();
		StringCell shpPathCell = (StringCell)r1.getCell(inTable1.getSpec().findColumnIndex(Utility.LOC_COLUMN));
		String overlapShapeFile = shpPathCell.getStringValue();
				
		String ovid = "15";
		
		int i = 0;
		long s = inTable2.size();
		for (DataRow r : inTable2){
			StringCell inPathCell = (StringCell)r.getCell(inTable2.getSpec().findColumnIndex(Utility.LOC_COLUMN));
	    	String srcTifFile = inPathCell.getStringValue();
	    	StringCell outPathCell = (StringCell)r.getCell(1);
	    	String destFile = outPathCell.getStringValue();
	    	Utility.ClipRaster(overlapShapeFile, srcTifFile, destFile, 
	    			overWrite.getBooleanValue(), tap.getBooleanValue(), 
	    			xRes.getStringValue(), yRes.getStringValue(),
	    			woName.getStringValue(),woValue.getStringValue(),cwhere.getStringValue());
	    	
	    	DataCell[] cells = new DataCell[outSpec.getNumColumns()];
			cells[0] = new StringCell(destFile);
			container.addRowToTable(new DefaultRow("Row"+i, cells));
	    	
	    	exec.checkCanceled();
			exec.setProgress((double) i / (double) s);
			i++;
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
         //this.outFile.saveSettingsTo(settings);      
    	this.overWrite.saveSettingsTo(settings);
    	this.tap.saveSettingsTo(settings);
    	this.xRes.saveSettingsTo(settings);
    	this.yRes.saveSettingsTo(settings);
    	this.woName.saveSettingsTo(settings);
    	this.woValue.saveSettingsTo(settings);
    	this.cwhere.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        //this.outFile.loadSettingsFrom(settings);
    	this.overWrite.loadSettingsFrom(settings);
    	this.tap.loadSettingsFrom(settings);
    	this.xRes.loadSettingsFrom(settings);
    	this.yRes.loadSettingsFrom(settings);
    	this.woName.loadSettingsFrom(settings);
    	this.woValue.loadSettingsFrom(settings);
    	this.cwhere.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        //this.outFile.validateSettings(settings);
    	this.overWrite.validateSettings(settings);
    	this.tap.validateSettings(settings);
    	this.xRes.validateSettings(settings);
    	this.yRes.validateSettings(settings);
    	this.woName.validateSettings(settings);
    	this.woValue.validateSettings(settings);
    	this.cwhere.validateSettings(settings);
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
    
 private static DataTableSpec createSpec() throws InvalidSettingsException {
		
    	List<DataColumnSpec> columns = new ArrayList<>();
		columns.add(new DataColumnSpecCreator(Utility.LOC_COLUMN, StringCell.TYPE).createSpec());
		return new DataTableSpec(columns.toArray(new DataColumnSpec[0]));
	}

}

