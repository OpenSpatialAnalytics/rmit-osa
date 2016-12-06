package org.knime.geo.calc;

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
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.gdalutils.Utility;


/**
 * This is the model implementation of GdalCalc.
 * 
 *
 * @author Forkan
 */
public class GdalCalcNodeModel extends NodeModel {
    
    /**
     * Constructor for the node model.
     */
	
	 static final String OUTPATH = "output_path";
	 static final String EXPR = "expression";
	
	 public final SettingsModelString outPath = new SettingsModelString(OUTPATH,"");
	 public final SettingsModelString expr = new SettingsModelString(EXPR,"");
	
	
    protected GdalCalcNodeModel() {
            
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
		int numColumns = inTable.getSpec().getNumColumns();
		
    	
		int i = 0;	
	
		for (DataRow r : inTable){
			List<String> sourceFiles = new ArrayList<String>(); 
			List<String> varNames = new ArrayList<String>();
			String rank = "";
			
			for ( int col = 0; col < numColumns; col++ ) {
				StringCell inPathCell = (StringCell) r.getCell(col);
				String srcTifFile = inPathCell.getStringValue();
		    	sourceFiles.add(srcTifFile);
		    	if (col==0){
		    		srcTifFile = srcTifFile.replace("\\", "/");
		    		String [] ss = srcTifFile.split("/");
		    		String fname = ss[ss.length-1];
		    		rank = fname.replaceAll("\\D+","");	  
		    	}
		    	char c = (char) (65+col);
		    	varNames.add(Character.toString (c));
			}
			String outFile = outPath.getStringValue().replace("\\", "/") + "/" + rank + ".tif";
			Utility.GetGdalCalc(sourceFiles, varNames, outFile ,null, expr.getStringValue());
			
			DataCell[] cells = new DataCell[outSpec.getNumColumns()];
			cells[0] = new StringCell(outFile);
			container.addRowToTable(new DefaultRow("Row"+i, cells));	
			exec.checkCanceled();
			exec.setProgress((double) i / (double) inTable.size());  
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
    	this.outPath.saveSettingsTo(settings);
    	this.expr.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        	this.outPath.loadSettingsFrom(settings);
        	this.expr.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    		this.outPath.validateSettings(settings);
    		this.expr.validateSettings(settings);
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

