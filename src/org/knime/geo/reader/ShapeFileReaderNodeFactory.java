package org.knime.geo.reader;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ShapeFileReader" Node.
 * Read a shapefile
 *
 * @author Forkan
 */
public class ShapeFileReaderNodeFactory 
        extends NodeFactory<ShapeFileReaderNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ShapeFileReaderNodeModel createNodeModel() {
        return new ShapeFileReaderNodeModel();
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
    public NodeView<ShapeFileReaderNodeModel> createNodeView(final int viewIndex,
            final ShapeFileReaderNodeModel nodeModel) {
        return new ShapeFileReaderNodeView(nodeModel);
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
        return new ShapeFileReaderNodeDialog();
    }

}

