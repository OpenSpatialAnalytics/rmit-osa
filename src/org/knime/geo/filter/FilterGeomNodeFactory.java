package org.knime.geo.filter;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "FilterGeom" Node.
 * 
 *
 * @author 
 */
public class FilterGeomNodeFactory 
        extends NodeFactory<FilterGeomNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public FilterGeomNodeModel createNodeModel() {
        return new FilterGeomNodeModel();
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
    public NodeView<FilterGeomNodeModel> createNodeView(final int viewIndex,
            final FilterGeomNodeModel nodeModel) {
        return new FilterGeomNodeView(nodeModel);
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
        return new FilterGeomNodeDialog();
    }

}

