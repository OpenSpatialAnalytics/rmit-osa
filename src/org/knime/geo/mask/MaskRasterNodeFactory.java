package org.knime.geo.mask;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "MaskRaster" Node.
 * 
 *
 * @author Forkan
 */
public class MaskRasterNodeFactory 
        extends NodeFactory<MaskRasterNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public MaskRasterNodeModel createNodeModel() {
        return new MaskRasterNodeModel();
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
    public NodeView<MaskRasterNodeModel> createNodeView(final int viewIndex,
            final MaskRasterNodeModel nodeModel) {
        return new MaskRasterNodeView(nodeModel);
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
        return new MaskRasterNodeDialog();
    }

}

