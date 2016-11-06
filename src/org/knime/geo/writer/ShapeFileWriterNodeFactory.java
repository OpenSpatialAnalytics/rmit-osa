package org.knime.geo.writer;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ShapeFileWriter" Node.
 * 
 *
 * @author Forkan
 */
public class ShapeFileWriterNodeFactory 
        extends NodeFactory<ShapeFileWriterNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ShapeFileWriterNodeModel createNodeModel() {
        return new ShapeFileWriterNodeModel();
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
    public NodeView<ShapeFileWriterNodeModel> createNodeView(final int viewIndex,
            final ShapeFileWriterNodeModel nodeModel) {
        return new ShapeFileWriterNodeView(nodeModel);
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
        return new ShapeFileWriterNodeDialog();
    }

}

