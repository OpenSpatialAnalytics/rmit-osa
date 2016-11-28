package org.knime.geo.unary;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "UnaryUnion" Node.
 * 
 *
 * @author Forkan
 */
public class UnaryUnionNodeFactory 
        extends NodeFactory<UnaryUnionNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public UnaryUnionNodeModel createNodeModel() {
        return new UnaryUnionNodeModel();
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
    public NodeView<UnaryUnionNodeModel> createNodeView(final int viewIndex,
            final UnaryUnionNodeModel nodeModel) {
        return new UnaryUnionNodeView(nodeModel);
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
        return new UnaryUnionNodeDialog();
    }

}

