package org.knime.geo.bool.covers;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Covers" Node.
 * 
 *
 * @author 
 */
public class CoversNodeFactory 
        extends NodeFactory<CoversNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public CoversNodeModel createNodeModel() {
        return new CoversNodeModel();
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
    public NodeView<CoversNodeModel> createNodeView(final int viewIndex,
            final CoversNodeModel nodeModel) {
        return new CoversNodeView(nodeModel);
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
        return new CoversNodeDialog();
    }

}

