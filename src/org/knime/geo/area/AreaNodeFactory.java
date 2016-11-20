package org.knime.geo.area;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Area" Node.
 * 
 *
 * @author Forkan
 */
public class AreaNodeFactory 
        extends NodeFactory<AreaNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public AreaNodeModel createNodeModel() {
        return new AreaNodeModel();
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
    public NodeView<AreaNodeModel> createNodeView(final int viewIndex,
            final AreaNodeModel nodeModel) {
        return new AreaNodeView(nodeModel);
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
        return new AreaNodeDialog();
    }

}

