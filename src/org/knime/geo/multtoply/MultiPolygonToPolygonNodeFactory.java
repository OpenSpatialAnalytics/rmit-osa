package org.knime.geo.multtoply;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "MultiPolygonToPolygon" Node.
 * 
 *
 * @author 
 */
public class MultiPolygonToPolygonNodeFactory 
        extends NodeFactory<MultiPolygonToPolygonNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public MultiPolygonToPolygonNodeModel createNodeModel() {
        return new MultiPolygonToPolygonNodeModel();
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
    public NodeView<MultiPolygonToPolygonNodeModel> createNodeView(final int viewIndex,
            final MultiPolygonToPolygonNodeModel nodeModel) {
        return new MultiPolygonToPolygonNodeView(nodeModel);
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
        return new MultiPolygonToPolygonNodeDialog();
    }

}

