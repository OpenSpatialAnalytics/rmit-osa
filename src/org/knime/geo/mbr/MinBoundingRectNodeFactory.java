package org.knime.geo.mbr;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "MinBoundingRect" Node.
 * 
 *
 * @author 
 */
public class MinBoundingRectNodeFactory 
        extends NodeFactory<MinBoundingRectNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public MinBoundingRectNodeModel createNodeModel() {
        return new MinBoundingRectNodeModel();
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
    public NodeView<MinBoundingRectNodeModel> createNodeView(final int viewIndex,
            final MinBoundingRectNodeModel nodeModel) {
        return new MinBoundingRectNodeView(nodeModel);
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
        return new MinBoundingRectNodeDialog();
    }

}

