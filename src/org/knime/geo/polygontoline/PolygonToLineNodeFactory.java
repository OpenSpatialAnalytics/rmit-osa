package org.knime.geo.polygontoline;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "PolygonToLine" Node.
 * 
 *
 * @author Forkan
 */
public class PolygonToLineNodeFactory 
        extends NodeFactory<PolygonToLineNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public PolygonToLineNodeModel createNodeModel() {
        return new PolygonToLineNodeModel();
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
    public NodeView<PolygonToLineNodeModel> createNodeView(final int viewIndex,
            final PolygonToLineNodeModel nodeModel) {
        return new PolygonToLineNodeView(nodeModel);
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
        return new PolygonToLineNodeDialog();
    }

}

