package org.knime.geo.overlay;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Overlay" Node.
 * 
 *
 * @author Forkan
 */
public class OverlayNodeFactory 
        extends NodeFactory<OverlayNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public OverlayNodeModel createNodeModel() {
        return new OverlayNodeModel();
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
    public NodeView<OverlayNodeModel> createNodeView(final int viewIndex,
            final OverlayNodeModel nodeModel) {
        return new OverlayNodeView(nodeModel);
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
        return new OverlayNodeDialog();
    }

}

