package org.knime.geo.calc;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "GdalCalc" Node.
 * 
 *
 * @author Forkan
 */
public class GdalCalcNodeFactory 
        extends NodeFactory<GdalCalcNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public GdalCalcNodeModel createNodeModel() {
        return new GdalCalcNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<GdalCalcNodeModel> createNodeView(final int viewIndex,
            final GdalCalcNodeModel nodeModel) {
        return new GdalCalcNodeView(nodeModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
        return new GdalCalcNodeDialog();
    }

}

