package org.knime.geo.reomovehole;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "RemoveHoles" Node.
 * 
 *
 * @author 
 */
public class RemoveHolesNodeFactory 
        extends NodeFactory<RemoveHolesNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public RemoveHolesNodeModel createNodeModel() {
        return new RemoveHolesNodeModel();
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
    public NodeView<RemoveHolesNodeModel> createNodeView(final int viewIndex,
            final RemoveHolesNodeModel nodeModel) {
        return new RemoveHolesNodeView(nodeModel);
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
        return new RemoveHolesNodeDialog();
    }

}

