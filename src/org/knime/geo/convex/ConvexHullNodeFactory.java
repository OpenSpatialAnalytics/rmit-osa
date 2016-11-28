package org.knime.geo.convex;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ConvexHull" Node.
 * 
 *
 * @author 
 */
public class ConvexHullNodeFactory 
        extends NodeFactory<ConvexHullNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ConvexHullNodeModel createNodeModel() {
        return new ConvexHullNodeModel();
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
    public NodeView<ConvexHullNodeModel> createNodeView(final int viewIndex,
            final ConvexHullNodeModel nodeModel) {
        return new ConvexHullNodeView(nodeModel);
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
        return new ConvexHullNodeDialog();
    }

}

