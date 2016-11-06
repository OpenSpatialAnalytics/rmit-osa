package org.knime.geo.visualizer;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "GeoMapViewer" Node.
 * 
 *
 * @author Forkan
 */
public class GeoMapViewerNodeFactory 
        extends NodeFactory<GeoMapViewerNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public GeoMapViewerNodeModel createNodeModel() {
        return new GeoMapViewerNodeModel();
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
    public NodeView<GeoMapViewerNodeModel> createNodeView(final int viewIndex,
            final GeoMapViewerNodeModel nodeModel) {
        return new GeoMapViewerNodeView(nodeModel);
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
        return new GeoMapViewerNodeDialog();
    }

}

