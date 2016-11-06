package org.knime.geo.listhdr;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ListHdr" Node.
 * 
 *
 * @author Forkan
 */
public class ListHdrNodeFactory 
        extends NodeFactory<ListHdrNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ListHdrNodeModel createNodeModel() {
        return new ListHdrNodeModel();
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
    public NodeView<ListHdrNodeModel> createNodeView(final int viewIndex,
            final ListHdrNodeModel nodeModel) {
        return new ListHdrNodeView(nodeModel);
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
        return new ListHdrNodeDialog();
    }

}

