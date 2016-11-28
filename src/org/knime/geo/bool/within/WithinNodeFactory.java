package org.knime.geo.bool.within;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Within" Node.
 * 
 *
 * @author Forkan
 */
public class WithinNodeFactory 
        extends NodeFactory<WithinNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public WithinNodeModel createNodeModel() {
        return new WithinNodeModel();
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
    public NodeView<WithinNodeModel> createNodeView(final int viewIndex,
            final WithinNodeModel nodeModel) {
        return new WithinNodeView(nodeModel);
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
        return new WithinNodeDialog();
    }

}

