package org.knime.geo.bool.disjoint;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Disjoint" Node.
 * 
 *
 * @author 
 */
public class DisjointNodeFactory 
        extends NodeFactory<DisjointNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public DisjointNodeModel createNodeModel() {
        return new DisjointNodeModel();
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
    public NodeView<DisjointNodeModel> createNodeView(final int viewIndex,
            final DisjointNodeModel nodeModel) {
        return new DisjointNodeView(nodeModel);
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
        return new DisjointNodeDialog();
    }

}

