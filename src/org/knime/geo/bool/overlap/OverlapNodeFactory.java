package org.knime.geo.bool.overlap;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Overlap" Node.
 * 
 *
 * @author Forkan
 */
public class OverlapNodeFactory 
        extends NodeFactory<OverlapNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public OverlapNodeModel createNodeModel() {
        return new OverlapNodeModel();
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
    public NodeView<OverlapNodeModel> createNodeView(final int viewIndex,
            final OverlapNodeModel nodeModel) {
        return new OverlapNodeView(nodeModel);
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
        return new OverlapNodeDialog();
    }

}

