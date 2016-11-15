package org.knime.geo.shpmerge;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ShapeMerge" Node.
 * 
 *
 * @author 
 */
public class ShapeMergeNodeFactory 
        extends NodeFactory<ShapeMergeNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ShapeMergeNodeModel createNodeModel() {
        return new ShapeMergeNodeModel();
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
    public NodeView<ShapeMergeNodeModel> createNodeView(final int viewIndex,
            final ShapeMergeNodeModel nodeModel) {
        return new ShapeMergeNodeView(nodeModel);
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
        return new ShapeMergeNodeDialog();
    }

}

