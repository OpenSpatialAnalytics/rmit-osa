package org.knime.geo.bool.contains;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Contains" Node.
 * 
 *
 * @author 
 */
public class ContainsNodeFactory 
        extends NodeFactory<ContainsNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ContainsNodeModel createNodeModel() {
        return new ContainsNodeModel();
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
    public NodeView<ContainsNodeModel> createNodeView(final int viewIndex,
            final ContainsNodeModel nodeModel) {
        return new ContainsNodeView(nodeModel);
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
        return new ContainsNodeDialog();
    }

}

