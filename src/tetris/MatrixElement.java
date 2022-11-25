
package tetris;

import java.awt.Color;
import java.awt.Point; 
import java.awt.Graphics; 

public class MatrixElement 
{
    Color color; 
    Point coors; 
    
    public MatrixElement(Color clr, Point coors)
    {
        this.color = clr  ;
        this.coors = coors; 
    }
    public MatrixElement(MatrixElement elem)
    {
        this.color = new Color(elem.color.getRGB());
        this.coors = new Point(elem.coors.x, elem.coors.y);
    }
    
    public void draw(Graphics g, int dim)
    {
        g.setColor(new Color(120,120,120));
        g.setColor(color);
        g.fillRect(coors.x*dim+1, coors.y*dim+1, dim-1, dim-1);
        g.setColor(Color.white);
        g.drawRect(coors.x*dim+1, coors.y*dim+1, dim-2, dim-2);
        g.setColor(Color.black);
        g.drawRect(coors.x*dim+1, coors.y*dim+1, dim-1, dim-1);
    }
}
