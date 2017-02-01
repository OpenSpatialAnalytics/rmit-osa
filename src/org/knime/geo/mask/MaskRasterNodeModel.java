package org.knime.geo.mask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
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
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.gdalutils.Utility;

/**
 * This is the model implementation of MaskRaster.
 * 
 *
 * @author Forkan
 */
public class MaskRasterNodeModel extends NodeModel {
    
	static final String MT = "mask_type";
	static final String OUTPATH = "output_path";	
	static String NODATA = Utility.getNoDataValue();
	
	public final SettingsModelString maskType = new SettingsModelString(MT,"Byte");
    public final SettingsModelString outPath = new SettingsModelString(OUTPATH,"");    
    
	
	
    /**
     * Constructor for the node model.
     */
    protected MaskRasterNodeModel() {
           
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
		
		int index = 0;
		for (DataRow r : inTable) {
	    	StringCell inPathCell = (StringCell)r.getCell(inTable.getSpec().findColumnIndex(Utility.LOC_COLUMN));
	    	String inPath = inPathCell.getStringValue();
	    	String shpFile = Utility.MaskRaster(inPath,  outPath.getStringValue(),maskType.getStringValue(),NODATA);	    	
	    	DataCell[] cells = new DataCell[outSpec.getNumColumns()];
			cells[0] = new StringCell(shpFile);
			container.addRowToTable(new DefaultRow("Row"+index, cells));			
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

    	if (outPath.getStringValue() == null) {
			throw new InvalidSettingsException("No output path specified");
		}
    	
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
        this.maskType.saveSettingsTo(settings);
        this.outPath.saveSettingsTo(settings);       
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	this.maskType.loadSettingsFrom(settings);
    	this.outPath.loadSettingsFrom(settings);    	
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        
    	this.maskType.validateSettings(settings);
    	this.outPath.validateSettings(settings);    	
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
		columns.add(new DataColumnSpecCreator(Utility.LOC_COLUMN, StringCell.TYPE).createSpec());
		return new DataTableSpec(columns.toArray(new DataColumnSpec[0]));		
	}
    

}

