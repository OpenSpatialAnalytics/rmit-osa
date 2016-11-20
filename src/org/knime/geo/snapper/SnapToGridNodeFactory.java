package org.knime.geo.snapper;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "SnapToGrid" Node.
 * 
 *
 * @author 
 */
public class SnapToGridNodeFactory 
        extends NodeFactory<SnapToGridNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public SnapToGridNodeModel createNodeModel() {
        return new SnapToGridNodeModel();
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
    public NodeView<SnapToGridNodeModel> createNodeView(final int viewIndex,
            final SnapToGridNodeModel nodeModel) {
        return new SnapToGridNodeView(nodeModel);
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
        return new SnapToGridNodeDialog();
    }

}

