package org.knime.geo.linemerge;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "LineMerger" Node.
 * 
 *
 * @author Forkan
 */
public class LineMergerNodeFactory 
        extends NodeFactory<LineMergerNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public LineMergerNodeModel createNodeModel() {
        return new LineMergerNodeModel();
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
    public NodeView<LineMergerNodeModel> createNodeView(final int viewIndex,
            final LineMergerNodeModel nodeModel) {
        return new LineMergerNodeView(nodeModel);
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
        return new LineMergerNodeDialog();
    }

}

