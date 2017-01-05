package org.knime.geo.removepoints;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "RemoveRepPoints" Node.
 * 
 *
 * @author 
 */
public class RemoveRepPointsNodeFactory 
        extends NodeFactory<RemoveRepPointsNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public RemoveRepPointsNodeModel createNodeModel() {
        return new RemoveRepPointsNodeModel();
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
    public NodeView<RemoveRepPointsNodeModel> createNodeView(final int viewIndex,
            final RemoveRepPointsNodeModel nodeModel) {
        return new RemoveRepPointsNodeView(nodeModel);
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
        return new RemoveRepPointsNodeDialog();
    }

}

