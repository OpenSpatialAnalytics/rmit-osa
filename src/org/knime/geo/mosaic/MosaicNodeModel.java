package org.knime.geo.mosaic;

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
import org.knime.geoutils.Constants;


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
	static final String LOC_COLUMN = Utility.LOC_COLUMN;
	static final String RC = "run_command";
	//static final String SPILT = "split";
	static String NODATA = Utility.getNoDataValue();
	
	public final SettingsModelString outputType = new SettingsModelString(OT,"Float32");
    public final SettingsModelString outPath = new SettingsModelString(OUTPATH,"");
    public final SettingsModelString outputFormat = new SettingsModelString(OF,"GTiff");
    public final SettingsModelString location = new SettingsModelString(LOC_COLUMN,"Location");
   // public final SettingsModelString split =  new SettingsModelString(SPILT,"0");
    public final SettingsModelBoolean rc = new SettingsModelBoolean(RC,false);
    
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
		
		/*
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
		*/
		
		FileUtils.cleanDirectory(new File(outPath.getStringValue())); 
		
		List<String> inPathList = new ArrayList<String>();
		//List<String> splitSet = null;
		
		String mergedFile = "";
		DataRow r1 = inTable.iterator().next();		
		int rankIndex = inTable.getSpec().findColumnIndex(Constants.RANK);
		int locIndex = inTable.getSpec().findColumnIndex(Utility.LOC_COLUMN);		
		int numRows = (int) inTable.size();		
		
		if (rankIndex != -1){
			IntCell rankCell = (IntCell)r1.getCell(rankIndex);
    		int rank = rankCell.getIntValue();
    		Rank = rank;    		
    		mergedFile  = outPath.getStringValue() + "/" + rank + Utility.outputFormat;
    		RowIterator ri = inTable.iterator();
    		int k = 0;
    		String inPath = "";
    		while( ri.hasNext() ){
    			DataRow r = ri.next();
    			rankCell = (IntCell)r.getCell(rankIndex);
        		rank = rankCell.getIntValue();
        		int currentRank = rank;
        		while ( ri.hasNext() && currentRank == rank ){
        			StringCell inPathCell = (StringCell)r.getCell(locIndex);
        	    	inPath = inPathCell.getStringValue();        	    
        	    	inPathList.add(inPath);
        	    	r = ri.next(); 
        	    	rankCell = (IntCell)r.getCell(rankIndex);
            		rank = rankCell.getIntValue();
        		}
        		currentRank = rank;        		
        		String outFile = Utility.MergeRasters(inPathList, mergedFile, outputType.getStringValue(), NODATA, 
        				outputFormat.getStringValue(), rc.getBooleanValue() );        	
        		DataCell[] cells = new DataCell[outSpec.getNumColumns()];
        		cells[0] = new StringCell(outFile);
        		container.addRowToTable(new DefaultRow("Row"+k, cells));        		
        		inPathList = new ArrayList<String>();
        		StringCell inPathCell = (StringCell)r.getCell(locIndex);
    	    	inPath = inPathCell.getStringValue();    	    	
        		inPathList.add(inPath);        		
        		mergedFile  = outPath.getStringValue() + "/" + rank + Utility.outputFormat;
        		k++;
    		}
    		
		}
		else{
			StringCell inPathCell1 = (StringCell)r1.getCell(locIndex);
			String inPath1File = inPathCell1.getStringValue();
			inPath1File = inPath1File.replace("\\", "/");
			String inPaths[] = inPath1File.split("/");
			String parentFolder = inPaths[inPaths.length-2];
			mergedFile  = outPath.getStringValue() + parentFolder + Utility.outputFormat;
			
			RowIterator ri = inTable.iterator();
    		int k = 0;
    		String inPath = "";			
			while( ri.hasNext() ){
    			DataRow r = ri.next();    			
        		String currentFolder = parentFolder;
        		while ( ri.hasNext() && (parentFolder.compareTo(currentFolder)==0) ){
        			StringCell inPathCell = (StringCell)r.getCell(locIndex);
        	    	inPath = inPathCell.getStringValue();
        	    	inPathList.add(inPath);
        	    	String inPathsList[] = inPath.split("/");
        	    	parentFolder = inPathsList[inPathsList.length-2];
        	    	r = ri.next();        	    	        	    	
        		}
        		currentFolder = parentFolder;
        		String outFile = Utility.MergeRasters(inPathList, mergedFile, outputType.getStringValue(), 
        				NODATA, outputFormat.getStringValue(), rc.getBooleanValue());
        		DataCell[] cells = new DataCell[outSpec.getNumColumns()];
        		cells[0] = new StringCell(outFile);
        		container.addRowToTable(new DefaultRow("Row"+k, cells));
        		inPathList = new ArrayList<String>();
        		StringCell inPathCell = (StringCell)r.getCell(locIndex);
    	    	inPath = inPathCell.getStringValue();
        		inPathList.add(inPath);
        		mergedFile  = outPath.getStringValue() + "/" + parentFolder + Utility.outputFormat;
        		k++;
    		}						
		}
    			
		/*
    	
    	String outFile = "";
    	
		for (DataRow r : inTable) {
	    	StringCell inPathCell = (StringCell)r.getCell(locIndex);
	    	String inPath = inPathCell.getStringValue();
	    	inPathList.add(inPath);
		}
		*/
		
		//outFile = Utility.MergeRasters(inPathList, mergedFile, outputType.getStringValue(), NODATA, outputFormat.getStringValue());
		
		/*
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
		*/
				    	
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
        this.outputType.saveSettingsTo(settings);
        this.outPath.saveSettingsTo(settings);
        this.outputFormat.saveSettingsTo(settings);
        this.rc.saveSettingsTo(settings);
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
        this.rc.loadSettingsFrom(settings);
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
		columns.add(new DataColumnSpecCreator(LOC_COLUMN, StringCell.TYPE).createSpec());
		return new DataTableSpec(columns.toArray(new DataColumnSpec[0]));
	}
    
    protected String getMergedFileName()
    {
    	String mergedFile  = outPath.getStringValue() + "/" + Rank + Utility.outputFormat;
    	return mergedFile;
    }

}

