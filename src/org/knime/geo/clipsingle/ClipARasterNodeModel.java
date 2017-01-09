package org.knime.geo.clipsingle;

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
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.gdalutils.Utility;
import org.knime.geoutils.Constants;

/**
 * This is the model implementation of ClipARaster.
 * 
 *
 * @author Forkan
 */
public class ClipARasterNodeModel extends NodeModel {
	
	 static final String OR = "over_write";
	 static final String TAP = "tap";
	 static final String WO_NAME = "wo_name";
	 static final String WO_VALUE = "wo_value";
	 static final String XRES = "xres";
	 static final String YRES = "yres";
	 static final String CWHERE = "cwhere";
	 static final String INPATH = "inRasterLocation";
	 static final String OUTPATH = "outputpath";
	 static final String SRCPATH = "srcShapeFile";
	 
	
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
    
    /**
     * Constructor for the node model.
     */
    protected ClipARasterNodeModel() {
    
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
    	//FileUtils.cleanDirectory(new File(outpath.getStringValue()));     			
		String srcShapeFile = inputShpFile.getStringValue();
		int loc = inTable.getSpec().findColumnIndex(Utility.LOC_COLUMN);				
		DataTableSpec outSpec = createSpec(inTable.getSpec());
		BufferedDataContainer container = exec.createDataContainer(outSpec);
				
		int i = 0;
		long s = inTable.size();
		for (DataRow r : inTable){
			StringCell inPathCell = (StringCell)r.getCell(loc);
	    	String srcTifFile = inPathCell.getStringValue();
	    	srcTifFile = srcTifFile.replace("\\", "/");
	    	String[] filePaths = srcTifFile.split("/");
	    	String srcFileName = filePaths[filePaths.length-1];
	    	String actualFileName = srcFileName.substring(0, srcFileName.length()-4);
	    	String outTifFile =  outpath.getStringValue().replace("\\", "/") + "/" + actualFileName + "_clipped.tif";
	    	
	    	Utility.ClipRaster(srcShapeFile, srcTifFile, outTifFile, 
	    			overWrite.getBooleanValue(), tap.getBooleanValue(), 
	    			xRes.getStringValue(), yRes.getStringValue(),
	    			woName.getStringValue(),woValue.getStringValue(),cwhere.getStringValue());
	    		    			    	
	    	DataCell[] cells = new DataCell[outSpec.getNumColumns()];	    	
	    	cells[loc] = new StringCell(outpath.getStringValue());
	    	for ( int col = 0; col < inTable.getSpec().getNumColumns(); col++ ) {	
				if (col != loc) {
					cells[col] = r.getCell(col);
				}
    		}
	    	
	    	exec.checkCanceled();
	    	container.addRowToTable(new DefaultRow("Row"+i, cells));
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
         // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        // TODO: generated method stub
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

