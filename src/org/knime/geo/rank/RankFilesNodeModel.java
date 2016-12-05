package org.knime.geo.rank;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.knime.gdalutils.Utility;
import org.knime.geoutils.Constants;

/**
 * This is the model implementation of RankFiles.
 * 
 *
 * @author 
 */
public class RankFilesNodeModel extends NodeModel {
    	
    /**
     * Constructor for the node model.
     */
    protected RankFilesNodeModel() {
    
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
    	List<String> locationList = new ArrayList<String>();
    	DataTableSpec outSpec = createSpec();
		BufferedDataContainer container = exec.createDataContainer(outSpec);		
		int locIndex = inTable.getSpec().findColumnIndex(Utility.LOC_COLUMN);
		
		RowIterator ri = inTable.iterator();
    	
    	while(ri.hasNext()) {
    		DataRow r = ri.next();
	    	StringCell fileCell = (StringCell)r.getCell(locIndex);
	    	locationList.add(fileCell.toString());
    	}
    	
    	int numFiles = locationList.size();
    	Map<Integer,String> myMap = Utility.RankZipFilesByTime(locationList);
    	Iterator<Integer> keySet = myMap.keySet().iterator();
    	int i = 0;
      	
    	for (String value : myMap.values()){
    		DataCell[] cells = new DataCell[outSpec.getNumColumns()];
			cells[0] = new IntCell(	keySet.next().intValue() );
			cells[1] = new StringCell(value);
    		
    		exec.checkCanceled();
			container.addRowToTable(new DefaultRow("Row"+i, cells));
			exec.setProgress((double) i / (double) numFiles );
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
    
    private static DataTableSpec createSpec() throws InvalidSettingsException {
		
		List<DataColumnSpec> columns = new ArrayList<>();
		
		columns.add(new DataColumnSpecCreator(Constants.RANK, IntCell.TYPE).createSpec());
		columns.add(new DataColumnSpecCreator(Utility.LOC_COLUMN, StringCell.TYPE).createSpec());

		return new DataTableSpec(columns.toArray(new DataColumnSpec[0]));
	}

}

