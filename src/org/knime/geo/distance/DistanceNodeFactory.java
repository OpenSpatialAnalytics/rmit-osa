package org.knime.geo.distance;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Distance" Node.
 * 
 *
 * @author Forkan
 */
public class DistanceNodeFactory 
        extends NodeFactory<DistanceNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public DistanceNodeModel createNodeModel() {
        return new DistanceNodeModel();
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
    public NodeView<DistanceNodeModel> createNodeView(final int viewIndex,
            final DistanceNodeModel nodeModel) {
        return new DistanceNodeView(nodeModel);
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
        return new DistanceNodeDialog();
    }

}

