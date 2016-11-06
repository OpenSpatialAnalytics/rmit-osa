package org.knime.geo.operators;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "GeometryOperation" Node.
 * 
 *
 * @author 
 */
public class GeometryOperationNodeFactory 
        extends NodeFactory<GeometryOperationNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public GeometryOperationNodeModel createNodeModel() {
        return new GeometryOperationNodeModel();
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
    public NodeView<GeometryOperationNodeModel> createNodeView(final int viewIndex,
            final GeometryOperationNodeModel nodeModel) {
        return new GeometryOperationNodeView(nodeModel);
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
        return new GeometryOperationNodeDialog();
    }

}

