package org.knime.geo.bool.coveredBy;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "CoveredBy" Node.
 * 
 *
 * @author 
 */
public class CoveredByNodeFactory 
        extends NodeFactory<CoveredByNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public CoveredByNodeModel createNodeModel() {
        return new CoveredByNodeModel();
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
    public NodeView<CoveredByNodeModel> createNodeView(final int viewIndex,
            final CoveredByNodeModel nodeModel) {
        return new CoveredByNodeView(nodeModel);
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
        return new CoveredByNodeDialog();
    }

}

