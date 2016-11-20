package org.knime.geo.linepoint;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "LinePoints" Node.
 * 
 *
 * @author Forkan
 */
public class LinePointsNodeFactory 
        extends NodeFactory<LinePointsNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public LinePointsNodeModel createNodeModel() {
        return new LinePointsNodeModel();
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
    public NodeView<LinePointsNodeModel> createNodeView(final int viewIndex,
            final LinePointsNodeModel nodeModel) {
        return new LinePointsNodeView(nodeModel);
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
        return new LinePointsNodeDialog();
    }

}

