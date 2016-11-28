package org.knime.geo.bool.touches;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Touches" Node.
 * 
 *
 * @author 
 */
public class TouchesNodeFactory 
        extends NodeFactory<TouchesNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public TouchesNodeModel createNodeModel() {
        return new TouchesNodeModel();
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
    public NodeView<TouchesNodeModel> createNodeView(final int viewIndex,
            final TouchesNodeModel nodeModel) {
        return new TouchesNodeView(nodeModel);
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
        return new TouchesNodeDialog();
    }

}

