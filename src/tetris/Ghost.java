/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import java.awt.Color;
import java.awt.Graphics; 

public class Ghost extends Mino
{
    public Ghost(Mino mino)
    {
        super(mino, false);
        color = new Color (mino.color.getRed(), mino.color.getGreen(), mino.color.getBlue(),100);
    }
    
    @Override
    public void draw(Graphics g)
    {
        for(int i=0; i<4; i++)
        {
            g.setColor(color);
            g.fillRect(Tetris.dim*pCoors[i].x+1, Tetris.dim*pCoors[i].y+1, Tetris.dim-2, Tetris.dim-2);  
            g.setColor(Color.black);
            g.drawRect(Tetris.dim*pCoors[i].x+1, Tetris.dim*pCoors[i].y+1, Tetris.dim-3, Tetris.dim-3);  
        }
    }
}
