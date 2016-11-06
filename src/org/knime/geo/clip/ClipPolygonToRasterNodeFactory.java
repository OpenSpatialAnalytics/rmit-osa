package org.knime.geo.clip;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ClipPolygonToRaster" Node.
 * 
 *
 * @author Forkan
 */
public class ClipPolygonToRasterNodeFactory 
        extends NodeFactory<ClipPolygonToRasterNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ClipPolygonToRasterNodeModel createNodeModel() {
        return new ClipPolygonToRasterNodeModel();
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
    public NodeView<ClipPolygonToRasterNodeModel> createNodeView(final int viewIndex,
            final ClipPolygonToRasterNodeModel nodeModel) {
        return new ClipPolygonToRasterNodeView(nodeModel);
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
        return new ClipPolygonToRasterNodeDialog();
    }

}

