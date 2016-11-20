package org.knime.geo.length;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Length" Node.
 * 
 *
 * @author Forkan
 */
public class LengthNodeFactory 
        extends NodeFactory<LengthNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public LengthNodeModel createNodeModel() {
        return new LengthNodeModel();
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
    public NodeView<LengthNodeModel> createNodeView(final int viewIndex,
            final LengthNodeModel nodeModel) {
        return new LengthNodeView(nodeModel);
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
        return new LengthNodeDialog();
    }

}

