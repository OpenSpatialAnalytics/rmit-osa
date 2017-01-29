package org.knime.geo.rasterview;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "RasterView" Node.
 * 
 *
 * @author 
 */
public class RasterViewNodeFactory 
        extends NodeFactory<RasterViewNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public RasterViewNodeModel createNodeModel() {
        return new RasterViewNodeModel();
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
    public NodeView<RasterViewNodeModel> createNodeView(final int viewIndex,
            final RasterViewNodeModel nodeModel) {
        return new RasterViewNodeView(nodeModel);
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
        return new RasterViewNodeDialog();
    }

}

