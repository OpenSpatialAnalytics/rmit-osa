package org.knime.geo.mosaic;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.geotools.swing.JMapFrame;
import org.knime.core.node.NodeView;
import org.knime.gdalutils.Utility;

/**
 * <code>NodeView</code> for the "Mosaic" Node.
 * 
 *
 * @author 
 */
public class MosaicNodeView extends NodeView<MosaicNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link MosaicNodeModel})
     */
    protected MosaicNodeView(final MosaicNodeModel nodeModel) {
        super(nodeModel);
        
        /*
        String fname= nodeModel.getMergedFileName();
        String output = Utility.GetGdalInfo(fname);
       
        JFrame jf = new JFrame();
        jf.setTitle("Gdal Info");
        jf.setVisible(true);
        jf.setAlwaysOnTop(true);
        jf.setSize(640,480);	
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jf.setLocation(dim.width/2-jf.getSize().width/2, dim.height/2-jf.getSize().height/2);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       
        JTextArea ta = new JTextArea(output);
        ta.setEditable(false);
        JScrollPane sp = new JScrollPane(ta);
        jf.add(sp);
        setComponent(jf);
        */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {
        // TODO: generated method stub
    }

}

