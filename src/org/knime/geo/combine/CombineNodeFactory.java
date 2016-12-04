package org.knime.geo.combine;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Combine" Node.
 * 
 *
 * @author Forkan
 */
public class CombineNodeFactory 
        extends NodeFactory<CombineNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public CombineNodeModel createNodeModel() {
        return new CombineNodeModel();
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
    public NodeView<CombineNodeModel> createNodeView(final int viewIndex,
            final CombineNodeModel nodeModel) {
        return new CombineNodeView(nodeModel);
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
        return new CombineNodeDialog();
    }

}

