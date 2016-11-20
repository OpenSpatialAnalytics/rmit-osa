package org.knime.geo.split;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Split" Node.
 * 
 *
 * @author Forkan
 */
public class SplitNodeFactory 
        extends NodeFactory<SplitNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public SplitNodeModel createNodeModel() {
        return new SplitNodeModel();
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
    public NodeView<SplitNodeModel> createNodeView(final int viewIndex,
            final SplitNodeModel nodeModel) {
        return new SplitNodeView(nodeModel);
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
        return new SplitNodeDialog();
    }

}

