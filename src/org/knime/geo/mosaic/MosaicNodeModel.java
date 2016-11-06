package org.knime.geo.mosaic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowIterator;
import org.knime.core.data.def.DefaultRow;
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


/**
 * This is the model implementation of Mosaic.
 * 
 *
 * @author 
 */
public class MosaicNodeModel extends NodeModel {
	
	static final String OT = "output_type";
	static final String OUTPATH = "output_path";
	static final String OF = "output_format";
	static final String RANK = Utility.RANK;
	static final String LOC_COLUMN = Utility.LOC_COLUMN;
	static final String SPILT = "split";
	static String NODATA = Utility.getNoDataValue();
	
	public final SettingsModelString outputType = new SettingsModelString(OT,"Float32");
    public final SettingsModelString outPath = new SettingsModelString(OUTPATH,"");
    public final SettingsModelString outputFormat = new SettingsModelString(OF,"GTiff");
    public final SettingsModelString location = new SettingsModelString(LOC_COLUMN,"Location");
    public final SettingsModelString split =  new SettingsModelString(SPILT,"0");
    
    public int Rank;
    
    /**
     * Constructor for the node model.
     */
    protected MosaicNodeModel() {
    
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
		
		int totalFiles = (int) inTable.size();
		int splitSize = 0;
		int numberOfSplit = 1;
		int m = 0;

		try{
			splitSize = Integer.parseInt(split.getStringValue());
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		
		if (splitSize != 0 ){
			m =  totalFiles % splitSize;
			numberOfSplit = (totalFiles/splitSize);
			if ( m != 0 )
				numberOfSplit = numberOfSplit + 1;
		}
		
		List<String> inPathList = new ArrayList<String>();
		List<String> splitSet = null;
		
		DataRow r1 = inTable.iterator().next();
		IntCell rankCell = (IntCell)r1.getCell(inTable.getSpec().findColumnIndex(RANK));
    	int rank = rankCell.getIntValue();
    	Rank = rank;
    	String mergedFile  = outPath.getStringValue() + "/" + rank + ".tif";
    	String outFile = "";
    	
		for (DataRow r : inTable) {
	    	StringCell inPathCell = (StringCell)r.getCell(inTable.getSpec().findColumnIndex(LOC_COLUMN));
	    	String inPath = inPathCell.getStringValue();
	    	inPathList.add(inPath);
		}
		
		if ( numberOfSplit != 1){
			List<String> splitList = new ArrayList<String>();
			for (int i=0; i < numberOfSplit; i++ ){
				if(i == numberOfSplit-1){
					if (m == 0)
						splitSet = new ArrayList<String>(inPathList.subList(i*splitSize, (i+1)*splitSize));
					else
						splitSet = new ArrayList<String>(inPathList.subList(i*splitSize, inPathList.size()));
				}
				else{
					splitSet = new ArrayList<String>(inPathList.subList(i*splitSize, (i+1)*splitSize));
				}
				
				String subMergedFile  = outPath.getStringValue() + "/" + rank + "_" + i + ".tif";
				String splitOutFile = Utility.MergeRasters(splitSet, subMergedFile, outputType.getStringValue(), NODATA, outputFormat.getStringValue());
				splitList.add(splitOutFile);
				
				exec.checkCanceled();
				exec.setProgress((double) i / (double) numberOfSplit);
			}
			outFile = Utility.MergeRasters(splitList, mergedFile, outputType.getStringValue(), NODATA, outputFormat.getStringValue());
		}
		else{
			outFile = Utility.MergeRasters(inPathList, mergedFile, outputType.getStringValue(), NODATA, outputFormat.getStringValue());
		}
		
		DataCell[] cells = new DataCell[outSpec.getNumColumns()];
		cells[0] = new StringCell(outFile);
		container.addRowToTable(new DefaultRow("Row0", cells));
	    	
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
        this.outputType.saveSettingsTo(settings);
        this.outPath.saveSettingsTo(settings);
        this.outputFormat.saveSettingsTo(settings);
        this.split.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	this.outputType.loadSettingsFrom(settings);
        this.outPath.loadSettingsFrom(settings);
        this.outputFormat.loadSettingsFrom(settings);
        this.split.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	this.outputType.validateSettings(settings);
        this.outPath.validateSettings(settings);
        this.outputFormat.validateSettings(settings);
        this.split.validateSettings(settings);
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
		columns.add(new DataColumnSpecCreator(LOC_COLUMN, StringCell.TYPE).createSpec());
		return new DataTableSpec(columns.toArray(new DataColumnSpec[0]));
	}
    
    protected String getMergedFileName()
    {
    	String mergedFile  = outPath.getStringValue() + "/" + Rank + ".tif";
    	return mergedFile;
    }

}

