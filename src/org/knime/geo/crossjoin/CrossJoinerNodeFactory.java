package org.knime.geo.crossjoin;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "CrossJoiner" Node.
 * 
 *
 * @author Forkan
 */
public class CrossJoinerNodeFactory 
        extends NodeFactory<CrossJoinerNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public CrossJoinerNodeModel createNodeModel() {
        return new CrossJoinerNodeModel();
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
    public NodeView<CrossJoinerNodeModel> createNodeView(final int viewIndex,
            final CrossJoinerNodeModel nodeModel) {
        return new CrossJoinerNodeView(nodeModel);
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
        return new CrossJoinerNodeDialog();
    }

}

