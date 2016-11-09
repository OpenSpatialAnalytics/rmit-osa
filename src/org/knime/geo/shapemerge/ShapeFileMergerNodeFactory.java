package org.knime.geo.shapemerge;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ShapeFileMerger" Node.
 * 
 *
 * @author Forkan
 */
public class ShapeFileMergerNodeFactory 
        extends NodeFactory<ShapeFileMergerNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ShapeFileMergerNodeModel createNodeModel() {
        return new ShapeFileMergerNodeModel();
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
    public NodeView<ShapeFileMergerNodeModel> createNodeView(final int viewIndex,
            final ShapeFileMergerNodeModel nodeModel) {
        return new ShapeFileMergerNodeView(nodeModel);
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
        return new ShapeFileMergerNodeDialog();
    }

}

