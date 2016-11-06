package org.knime.geo.buffer;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Buffer" Node.
 * 
 *
 * @author Forkan
 */
public class BufferNodeFactory 
        extends NodeFactory<BufferNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferNodeModel createNodeModel() {
        return new BufferNodeModel();
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
    public NodeView<BufferNodeModel> createNodeView(final int viewIndex,
            final BufferNodeModel nodeModel) {
        return new BufferNodeView(nodeModel);
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
        return new BufferNodeDialog();
    }

}

