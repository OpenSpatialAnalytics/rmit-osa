package org.knime.geo.rasterize;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Rasterize" Node.
 * 
 *
 * @author Forkan
 */
public class RasterizeNodeFactory 
        extends NodeFactory<RasterizeNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public RasterizeNodeModel createNodeModel() {
        return new RasterizeNodeModel();
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
    public NodeView<RasterizeNodeModel> createNodeView(final int viewIndex,
            final RasterizeNodeModel nodeModel) {
        return new RasterizeNodeView(nodeModel);
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
        return new RasterizeNodeDialog();
    }

}

