package org.knime.geo.bool.crosses;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Crosses" Node.
 * 
 *
 * @author 
 */
public class CrossesNodeFactory 
        extends NodeFactory<CrossesNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public CrossesNodeModel createNodeModel() {
        return new CrossesNodeModel();
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
    public NodeView<CrossesNodeModel> createNodeView(final int viewIndex,
            final CrossesNodeModel nodeModel) {
        return new CrossesNodeView(nodeModel);
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
        return new CrossesNodeDialog();
    }

}

