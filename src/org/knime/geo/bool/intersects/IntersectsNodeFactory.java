package org.knime.geo.bool.intersects;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Intersects" Node.
 * 
 *
 * @author 
 */
public class IntersectsNodeFactory 
        extends NodeFactory<IntersectsNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public IntersectsNodeModel createNodeModel() {
        return new IntersectsNodeModel();
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
    public NodeView<IntersectsNodeModel> createNodeView(final int viewIndex,
            final IntersectsNodeModel nodeModel) {
        return new IntersectsNodeView(nodeModel);
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
        return new IntersectsNodeDialog();
    }

}

