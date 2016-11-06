package org.knime.geo.listhdr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
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

import org.knime.gdalutils.*;

/**
 * This is the model implementation of ListHdr.
 * 
 *
 * @author Forkan
 */
public class ListHdrNodeModel extends NodeModel {
	
	
    
    /**
     * Constructor for the node model.
     */
    protected ListHdrNodeModel() {
           
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
    	int lastSize = 0;
    	
    	while(ri.hasNext()) {
	    	DataRow r = ri.next();
	    	IntCell rankCell = (IntCell)r.getCell(inTable.getSpec().findColumnIndex(Utility.RANK));
	    	int rank = rankCell.getIntValue();
	    	StringCell inPathCell = (StringCell)r.getCell(inTable.getSpec().findColumnIndex(Utility.LOC_COLUMN));
	    	String inPath = inPathCell.getStringValue();
	    	
	    	List<String> hdrFiles = Utility.readHdrInZipFile(inPath);
	    	int numFiles = hdrFiles.size();
	    	
			for (int i = 0; i < numFiles; i++ ){
				DataCell[] cells = new DataCell[outSpec.getNumColumns()];
				cells[0] = new IntCell(rank);
				cells[1] = new StringCell(hdrFiles.get(i));
				
				exec.checkCanceled();
				container.addRowToTable(new DefaultRow("Row"+(lastSize + i), cells));
				exec.setProgress((double) i / (double) numFiles );
			}
			lastSize = lastSize + numFiles;
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

