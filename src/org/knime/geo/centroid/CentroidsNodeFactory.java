package org.knime.geo.centroid;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Centroids" Node.
 * 
 *
 * @author Forkan
 */
public class CentroidsNodeFactory 
        extends NodeFactory<CentroidsNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public CentroidsNodeModel createNodeModel() {
        return new CentroidsNodeModel();
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
    public NodeView<CentroidsNodeModel> createNodeView(final int viewIndex,
            final CentroidsNodeModel nodeModel) {
        return new CentroidsNodeView(nodeModel);
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
        return new CentroidsNodeDialog();
    }

}

