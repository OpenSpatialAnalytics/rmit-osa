package org.knime.geo.resample;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "Resample" Node.
 * 
 *
 * @author Forkan
 */
public class ResampleNodeFactory 
        extends NodeFactory<ResampleNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ResampleNodeModel createNodeModel() {
        return new ResampleNodeModel();
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
    public NodeView<ResampleNodeModel> createNodeView(final int viewIndex,
            final ResampleNodeModel nodeModel) {
        return new ResampleNodeView(nodeModel);
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
        return new ResampleNodeDialog();
    }

}

