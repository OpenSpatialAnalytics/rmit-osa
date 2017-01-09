package org.knime.geo.clipsingle;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ClipARaster" Node.
 * 
 *
 * @author Forkan
 */
public class ClipARasterNodeFactory 
        extends NodeFactory<ClipARasterNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ClipARasterNodeModel createNodeModel() {
        return new ClipARasterNodeModel();
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
    public NodeView<ClipARasterNodeModel> createNodeView(final int viewIndex,
            final ClipARasterNodeModel nodeModel) {
        return new ClipARasterNodeView(nodeModel);
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
        return new ClipARasterNodeDialog();
    }

}

