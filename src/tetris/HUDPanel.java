
package tetris;

import javax.swing.JPanel; 
import java.awt.FlowLayout;
import java.awt.Color; 
import java.awt.Dimension;

public class HUDPanel extends JPanel
{
    public HUDPanel()
    {
        setLayout( new FlowLayout() );
               
        setOpaque(false);
    }
    
    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension (175, 540);
    }
}
