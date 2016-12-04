package org.knime.geo.clip;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.knime.geoutils.Constants;

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
	 static final String INPATH = "inpath";
	 static final String OUTPATH = "outpath";
	 static final String SRCPATH = "srcPath";
	 
	
	 public final SettingsModelBoolean overWrite = new SettingsModelBoolean(OR,true);
	 public final SettingsModelBoolean tap = new SettingsModelBoolean(TAP,false);
	 public final SettingsModelString xRes = new SettingsModelString(XRES,"");
	 public final SettingsModelString yRes = new SettingsModelString(YRES,"");
	 public final SettingsModelString woName = new SettingsModelString(WO_NAME,"");
	 public final SettingsModelString woValue = new SettingsModelString(WO_VALUE,"");
	 public final SettingsModelString cwhere = new SettingsModelString(CWHERE,"");
	 public final SettingsModelString inputShpFile = new SettingsModelString(INPATH,"");
	 public final SettingsModelString outpath = new SettingsModelString(OUTPATH,"");
	 public final SettingsModelString srcPath = new SettingsModelString(SRCPATH,"");
	
		
	//static final String OUTFILE = "output_file";
	//public final SettingsModelString outFile = new SettingsModelString(OUTFILE,"");
	

    protected ClipPolygonToRasterNodeModel() {
            
        super(1, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
    	
    	BufferedDataTable inTable = inData[0];    	
    	DataTableSpec outSpec = createSpec();
		BufferedDataContainer container = exec.createDataContainer(outSpec);
		
		String overlapShapeFile = inputShpFile.getStringValue();
		int loc = inTable.getSpec().findColumnIndex(Utility.LOC_COLUMN);
		int numColumns = inTable.getSpec().getNumColumns();
		String columNames[] = inTable.getSpec().getColumnNames();
		boolean useOverlap = false;
		boolean useRank = false;
		int ovIndex = -1;
		
		if (Arrays.asList(columNames).contains(Constants.OVID)
				&& Arrays.asList(columNames).contains(Constants.RANK) ){
			useOverlap = true;
			ovIndex = inTable.getSpec().findColumnIndex(Constants.OVID);
		}
		
		if (!Arrays.asList(columNames).contains(Constants.OVID)
				&& Arrays.asList(columNames).contains(Constants.RANK) ){
			useRank = true;
		}
				
		int i = 0;
		long s = inTable.size();
		for (DataRow r : inTable){
			//StringCell inPathCell = (StringCell)r.getCell(loc);
	    	//String srcTifFile = inPathCell.getStringValue();
	    	if (useOverlap){
	    		int j = 0;
	    		int rankIndexs[] = new int[2];
	        	for (int col = 0; col < numColumns; col++) {
	        		if (columNames[col].contains(Constants.RANK) ){
	        			rankIndexs[j] = col;
	        			j++;
	        			if (j==2)
	        				break;
	        		}
	        	}
	        	
	        	String ovidStr = r.getCell(ovIndex).toString();
	        	String expr = Constants.OVID + "=" + ovidStr;
	        	
	        	String outFolder = outpath.getStringValue().replace("\\", "/");
	        	File directory = new File(outFolder+"/"+ovidStr);
	    		if (! directory.exists()){
	    			directory.mkdir();
	    		}
	    		
	    		String srcTifFile = srcPath.getStringValue().replace("\\", "/") + "/" + ovidStr + ".tif";
	    		String destFile1 = outFolder+"/"+ovidStr + "/" + r.getCell(rankIndexs[0]).toString()+"a.tif";
	    		String destFile2 = outFolder+"/"+ovidStr + "/" + r.getCell(rankIndexs[1]).toString()+"b.tif";
	    		
	    		Utility.ClipRaster(overlapShapeFile, srcTifFile, destFile1, 
		    			overWrite.getBooleanValue(), tap.getBooleanValue(), 
		    			xRes.getStringValue(), yRes.getStringValue(),
		    			woName.getStringValue(),woValue.getStringValue(),expr);
	    		
	    		Utility.ClipRaster(overlapShapeFile, srcTifFile, destFile2, 
		    			overWrite.getBooleanValue(), tap.getBooleanValue(), 
		    			xRes.getStringValue(), yRes.getStringValue(),
		    			woName.getStringValue(),woValue.getStringValue(),expr);
	    		
	    		DataCell[] cells1 = new DataCell[outSpec.getNumColumns()];
				cells1[0] = new StringCell(destFile1);
				container.addRowToTable(new DefaultRow("Row"+i, cells1));
				i++;
				
				DataCell[] cells2 = new DataCell[outSpec.getNumColumns()];
				cells2[0] = new StringCell(destFile2);
				container.addRowToTable(new DefaultRow("Row"+i, cells2));
				i++;
	    	}
	    	
	    	else if (useRank){
	    		int rnkIndex = inTable.getSpec().findColumnIndex(Constants.RANK);
	    		String rankStr = r.getCell(rnkIndex).toString();
	        	String expr = Constants.RANK + "=" + rankStr;
	        	
	        	String outFolder = outpath.getStringValue().replace("\\", "/");
	        	String destFile = outFolder+"/"+rankStr + ".tif";
	        	String srcTifFile = srcPath.getStringValue().replace("\\", "/") + "/" + rankStr + ".tif";
	        	
	        	Utility.ClipRaster(overlapShapeFile, srcTifFile, destFile, 
		    			overWrite.getBooleanValue(), tap.getBooleanValue(), 
		    			xRes.getStringValue(), yRes.getStringValue(),
		    			woName.getStringValue(),woValue.getStringValue(),expr);
	        	
	        	DataCell[] cells = new DataCell[outSpec.getNumColumns()];
				cells[0] = new StringCell(destFile);
				container.addRowToTable(new DefaultRow("Row"+i, cells));
				i++;
	    	}
	    	else{
	    		StringCell inPathCell = (StringCell)r.getCell(loc);
		    	String srcTifFile = inPathCell.getStringValue();
	    		String filenames[] = srcTifFile.split("/");
	    		String outFolder = outpath.getStringValue().replace("\\", "/");
	        	String destFile = outFolder+"/"+filenames[filenames.length-1];
	        	
		    	Utility.ClipRaster(overlapShapeFile, srcTifFile, destFile, 
		    			overWrite.getBooleanValue(), tap.getBooleanValue(), 
		    			xRes.getStringValue(), yRes.getStringValue(),
		    			woName.getStringValue(),woValue.getStringValue(),cwhere.getStringValue());
		    	
		    	DataCell[] cells = new DataCell[outSpec.getNumColumns()];
				cells[0] = new StringCell(destFile);
				container.addRowToTable(new DefaultRow("Row"+i, cells));   	
				i++;
	    	}
	    	
			exec.checkCanceled();
			exec.setProgress((double) i / (double) s);    	
	    	
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

    	if (inputShpFile.getStringValue() == null) {
			throw new InvalidSettingsException("Input shape file must be specified");
		}
    	
    	if (outpath.getStringValue() == null) {
			throw new InvalidSettingsException("Output path must be specified");
		}

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
    	this.inputShpFile.saveSettingsTo(settings);
    	this.srcPath.saveSettingsTo(settings);
    	this.outpath.saveSettingsTo(settings);
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
    	this.inputShpFile.loadSettingsFrom(settings);
    	this.srcPath.loadSettingsFrom(settings);
    	this.outpath.loadSettingsFrom(settings);
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
    	this.inputShpFile.validateSettings(settings);
    	this.srcPath.validateSettings(settings);
    	this.outpath.validateSettings(settings);
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

