package org.knime.geo.difference;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Difference" Node.
 * 
 *
 * @author Forkan
 */
public class DifferenceNodeFactory 
        extends NodeFactory<DifferenceNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public DifferenceNodeModel createNodeModel() {
        return new DifferenceNodeModel();
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
    public NodeView<DifferenceNodeModel> createNodeView(final int viewIndex,
            final DifferenceNodeModel nodeModel) {
        return new DifferenceNodeView(nodeModel);
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
        return new DifferenceNodeDialog();
    }

}

