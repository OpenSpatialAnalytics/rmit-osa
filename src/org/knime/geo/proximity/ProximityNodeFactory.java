package org.knime.geo.proximity;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Proximity" Node.
 * 
 *
 * @author Forkan
 */
public class ProximityNodeFactory 
        extends NodeFactory<ProximityNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ProximityNodeModel createNodeModel() {
        return new ProximityNodeModel();
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
    public NodeView<ProximityNodeModel> createNodeView(final int viewIndex,
            final ProximityNodeModel nodeModel) {
        return new ProximityNodeView(nodeModel);
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
        return new ProximityNodeDialog();
    }

}

