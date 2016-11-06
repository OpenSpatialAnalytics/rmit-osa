package org.knime.geo.mosaic;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Mosaic" Node.
 * 
 *
 * @author 
 */
public class MosaicNodeFactory 
        extends NodeFactory<MosaicNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public MosaicNodeModel createNodeModel() {
        return new MosaicNodeModel();
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
    public NodeView<MosaicNodeModel> createNodeView(final int viewIndex,
            final MosaicNodeModel nodeModel) {
        return new MosaicNodeView(nodeModel);
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
        return new MosaicNodeDialog();
    }

}

