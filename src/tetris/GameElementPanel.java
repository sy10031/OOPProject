/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import javax.swing.JPanel; 
import java.awt.Color; 
import java.awt.Graphics; 
import javax.swing.BorderFactory;

public class GameElementPanel extends JPanel
{
    public GameElementPanel()
    {
        setBackground(null);
        setOpaque(false);
        setBorder(BorderFactory.createLineBorder(Color.yellow));
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        g.setColor(Color.yellow);
        g.fillRoundRect(0,0,this.getWidth(),this.getHeight(), 10, 10);
    }
}
