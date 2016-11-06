package org.knime.geo.rank;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "RankFiles" Node.
 * 
 *
 * @author 
 */
public class RankFilesNodeFactory 
        extends NodeFactory<RankFilesNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public RankFilesNodeModel createNodeModel() {
        return new RankFilesNodeModel();
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
    public NodeView<RankFilesNodeModel> createNodeView(final int viewIndex,
            final RankFilesNodeModel nodeModel) {
        return new RankFilesNodeView(nodeModel);
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
        return new RankFilesNodeDialog();
    }

}

