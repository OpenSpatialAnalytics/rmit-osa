package org.knime.geo.bool.equals;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Equals" Node.
 * 
 *
 * @author 
 */
public class EqualsNodeFactory 
        extends NodeFactory<EqualsNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public EqualsNodeModel createNodeModel() {
        return new EqualsNodeModel();
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
    public NodeView<EqualsNodeModel> createNodeView(final int viewIndex,
            final EqualsNodeModel nodeModel) {
        return new EqualsNodeView(nodeModel);
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
        return new EqualsNodeDialog();
    }

}

