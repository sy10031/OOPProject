
package tetris;

import tetris.Tetris; 

import java.awt.Color;
import java.awt.Point; 
import java.awt.Graphics; 

public class Mino 
{
    Color color; 
    Point[] pCoors; 
    
    int id; 
    
    public Mino (Mino mino, boolean reset)
    {
        this.id = mino.id; 
        this.color = mino.color; 
        
        if (reset)
            this.pCoors = getStartCoors();
        
        else
        {
            this.pCoors = new Point[4];
            for(int i=0; i<4; i++)
            {
                pCoors[i] = new Point(mino.pCoors[i].x, mino.pCoors[i].y);
            }
        }
    }
    
    public Mino( int id )
    {   
        this.id = id; 
        
        pCoors = getStartCoors();
    }
    
    public Point[] getStartCoors()
    {
        pCoors = new Point[4]; 
        
        switch (id) 
        {
            case 1:
            {
                color = Color.yellow;
                pCoors[0] = new Point(4, -1);
                pCoors[1] = new Point(5, -1);
                pCoors[2] = new Point(4, 0);
                pCoors[3] = new Point(5, 0);
                
                break;
            }
            case 2:
            {
                color = Color.cyan;
                pCoors[0] = new Point(3, 0);
                pCoors[1] = new Point(4, 0);
                pCoors[2] = new Point(5, 0);
                pCoors[3] = new Point(6, 0);
                
                break;
            }
            case 3:
            {
                color = Color.magenta;
                pCoors[0] = new Point(3, 0);
                pCoors[1] = new Point(4, 0);
                pCoors[2] = new Point(5, 0);
                pCoors[3] = new Point(4, -1);
                
                break;
            }
            case 4:
            {
                color = Color.red;
                pCoors[0] = new Point(3, -1);
                pCoors[1] = new Point(4, -1);
                pCoors[2] = new Point(4, 0);
                pCoors[3] = new Point(5, 0);
                
                break;
            }
            case 5:
            {
                color = Color.orange;
                pCoors[0] = new Point(3, 0);
                pCoors[1] = new Point(4, 0);
                pCoors[2] = new Point(5, 0);
                pCoors[3] = new Point(5, -1);
                
                break;
            }
            case 6:
            {
                color = Color.blue;
                
                pCoors[0] = new Point(3, 0);
                pCoors[1] = new Point(4, 0);
                pCoors[2] = new Point(5, 0);
                pCoors[3] = new Point(3, -1);
                
                break;
            }
            case 7:
            {
                color = Color.green;
                pCoors[0] = new Point(4, -1);
                pCoors[1] = new Point(5, -1);
                pCoors[2] = new Point(3, 0);
                pCoors[3] = new Point(4, 0);
                
                break;
            }
        }
        
        return pCoors; 
    }
    
    public void draw(Graphics g)
    {
        for(int i=0; i<4; i++)
        {
            g.setColor(color); 
            g.fillRect(Tetris.dim*pCoors[i].x, Tetris.dim*pCoors[i].y, 
                       Tetris.dim,             Tetris.dim);
            
            g.setColor(Color.white);
            g.drawRect(Tetris.dim*pCoors[i].x, Tetris.dim*pCoors[i].y, 
                       Tetris.dim-1,           Tetris.dim-1);  
            
            g.setColor(Color.black);
            g.drawRect(Tetris.dim*pCoors[i].x, Tetris.dim*pCoors[i].y, 
                       Tetris.dim,             Tetris.dim);

        }
    }
    
    public void drawInQueue(Graphics g, int width, int height)
    {
        int qDim   = Tetris.dim/6*5; 
        int xOffset = (width - (qDim*3))/2;
        int yOffset = (height - (qDim * 2))/2;
        
        switch(id)
        {
            case 1:
            {
                xOffset += -1*qDim/2; 
                break;
            }
            case 2: 
            {
                xOffset += -1*qDim/2; 
                yOffset += -1*qDim/2;  
            }
        }
        
        for(int i=0; i<4; i++)
        {
            g.setColor(color); 
            g.fillRect(qDim*(pCoors[i].x-3)+xOffset, qDim*(pCoors[i].y+1)+yOffset, 
                       qDim,   
                       qDim);
            
            g.setColor(Color.white);
            g.drawRect(qDim*(pCoors[i].x-3)+xOffset, qDim*(pCoors[i].y+1)+yOffset, 
                       qDim-1,   
                       qDim-1);
            
            g.setColor(Color.black);
            g.drawRect(qDim*(pCoors[i].x-3)+xOffset, qDim*(pCoors[i].y+1)+yOffset, 
                       qDim,                                 
                       qDim);  
        }
    }
    
    public void move(Point shift)
    {
         for(int i=0; i<4; i++)
         {
             pCoors[i].x+=shift.x; 
             pCoors[i].y+=shift.y; 
         }
    }
    
    public void rotate(int n)
    {   
        switch (id) 
        {
            case 1:
            {
                break;
            }

            case 2:
            {
                
                if (pCoors[0].y != pCoors[1].y)
                {
                    n *= -1;
                }
                pCoors[0].x += 1*n;
                pCoors[0].y +=-1*n;
                pCoors[1].x += 0*n;
                pCoors[1].y += 0*n;
                pCoors[2].x +=-1*n; 
                pCoors[2].y += 1*n;
                pCoors[3].x +=-2*n;
                pCoors[3].y += 2*n;
                
                break;
            }
            case 3:
            {
                
                //pCoors[0] = new Point(3, 1);
                //pCoors[1] = new Point(4, 1);
                //pCoors[2] = new Point(5, 1);
                //pCoors[3] = new Point(4, 0);
                
                if (pCoors[0].y == pCoors[1].y && pCoors[1].y == pCoors[2].y)
                {
                    if (pCoors[1].y > pCoors[3].y)
                    {
                        pCoors[0].x += 1*n;
                        pCoors[0].y +=-1*n;
                        pCoors[1].x += 0;
                        pCoors[1].y += 0;
                        pCoors[2].x +=-1*n; 
                        pCoors[2].y += 1*n;
                        pCoors[3].x += 1*n;
                        pCoors[3].y += 1*n;
                    }
                    else
                    {
                        pCoors[0].x +=-1*n;
                        pCoors[0].y += 1*n;
                        pCoors[1].x += 0;
                        pCoors[1].y += 0;
                        pCoors[2].x += 1*n; 
                        pCoors[2].y +=-1*n;
                        pCoors[3].x +=-1*n;
                        pCoors[3].y +=-1*n;
                    }
                }
                else
                {
                    if (pCoors[1].x < pCoors[3].x)
                    {
                        pCoors[0].x += 1*n;
                        pCoors[0].y += 1*n;
                        pCoors[1].x += 0;
                        pCoors[1].y += 0;
                        pCoors[2].x +=-1*n; 
                        pCoors[2].y +=-1*n;
                        pCoors[3].x +=-1*n;
                        pCoors[3].y += 1*n;
                    }
                    else
                    {
                        pCoors[0].x +=-1*n;
                        pCoors[0].y +=-1*n;
                        pCoors[1].x += 0;
                        pCoors[1].y += 0;
                        pCoors[2].x += 1*n; 
                        pCoors[2].y += 1*n;
                        pCoors[3].x += 1*n;
                        pCoors[3].y +=-1*n;
                    }
                }
                break;
            }
            case 4:
            {   
                if (pCoors[0].y != pCoors[1].y)
                {
                    n *= -1;
                }
                pCoors[0].x += 2*n;
                pCoors[0].y +=-1*n;
                pCoors[1].x += 1*n;
                pCoors[1].y += 0*n;
                pCoors[2].x += 0*n; 
                pCoors[2].y +=-1*n;
                pCoors[3].x +=-1*n;
                pCoors[3].y += 0*n;
                
                break;
            }
            case 5:
            {
                if (pCoors[0].y == pCoors[2].y && pCoors[1].y == pCoors[2].y)
                {
                    if (pCoors[1].y > pCoors[3].y)
                    {
                        pCoors[0].x += 1*n;
                        pCoors[0].y +=-1*n;
                        pCoors[1].x += 0;
                        pCoors[1].y += 0;
                        pCoors[2].x +=-1*n; 
                        pCoors[2].y += 1*n;
                        pCoors[3].x += 0;
                        pCoors[3].y += 2*n;
                    }
                    else
                    {
                        pCoors[0].x +=-1*n;
                        pCoors[0].y += 1*n;
                        pCoors[1].x += 0;
                        pCoors[1].y += 0;
                        pCoors[2].x += 1*n; 
                        pCoors[2].y +=-1*n;
                        pCoors[3].x += 0;
                        pCoors[3].y +=-2*n;
                    }
                }
                else if (pCoors[0].x == pCoors[2].x && pCoors[1].x == pCoors[2].x)
                {
                    if (pCoors[1].x < pCoors[3].x)
                    {
                        pCoors[0].x += 1*n;
                        pCoors[0].y += 1*n;
                        pCoors[1].x += 0;
                        pCoors[1].y += 0;
                        pCoors[2].x +=-1*n; 
                        pCoors[2].y +=-1*n;
                        pCoors[3].x +=-2*n;
                        pCoors[3].y += 0;
                    }
                    else
                    {
                        pCoors[0].x +=-1*n;
                        pCoors[0].y +=-1*n;
                        pCoors[1].x += 0;
                        pCoors[1].y += 0;
                        pCoors[2].x += 1*n; 
                        pCoors[2].y += 1*n;
                        pCoors[3].x += 2*n;
                        pCoors[3].y += 0;
                    }
                }
                break;
            }
            case 6:
            {           
                if (pCoors[0].y == pCoors[2].y && pCoors[1].y == pCoors[2].y)
                {
                    if (pCoors[1].y > pCoors[3].y)
                    {
                        pCoors[0].x += 1*n;
                        pCoors[0].y +=-1*n;
                        pCoors[1].x += 0;
                        pCoors[1].y += 0;
                        pCoors[2].x +=-1*n; 
                        pCoors[2].y += 1*n;
                        pCoors[3].x += 2*n;
                        pCoors[3].y += 0;
                    }
                    else
                    {
                        pCoors[0].x +=-1*n;
                        pCoors[0].y += 1*n;
                        pCoors[1].x += 0;
                        pCoors[1].y += 0;
                        pCoors[2].x += 1*n; 
                        pCoors[2].y +=-1*n;
                        pCoors[3].x +=-2*n;
                        pCoors[3].y += 0;
                    }
                }
                else if (pCoors[0].x == pCoors[2].x && pCoors[1].x == pCoors[2].x)
                {
                    if (pCoors[1].x < pCoors[3].x)
                    {
                        pCoors[0].x += 1*n;
                        pCoors[0].y += 1*n;
                        pCoors[1].x += 0;
                        pCoors[1].y += 0;
                        pCoors[2].x +=-1*n; 
                        pCoors[2].y +=-1*n;
                        pCoors[3].x += 0;
                        pCoors[3].y += 2*n;
                    }
                    else
                    {
                        pCoors[0].x +=-1*n;
                        pCoors[0].y +=-1*n;
                        pCoors[1].x += 0;
                        pCoors[1].y += 0;
                        pCoors[2].x += 1*n; 
                        pCoors[2].y += 1*n;
                        pCoors[3].x += 0;
                        pCoors[3].y +=-2*n;
                    }
                }
                break;
            }
            case 7:
            {                
                if (pCoors[0].y != pCoors[1].y)
                {
                    n *= -1;
                }
                pCoors[0].x += 1*n;
                pCoors[0].y += 0*n;
                pCoors[1].x += 0*n;
                pCoors[1].y += 1*n;
                pCoors[2].x += 1*n; 
                pCoors[2].y +=-2*n;
                pCoors[3].x += 0*n;
                pCoors[3].y +=-1*n;
                
                break;
            }
        }
    }
    
    public void reverseRotate()
    {
        for(int i=0; i<getReverseRotationIndexById(id); i++)
        {
            rotate(1);
        }
    }
    
    private int getReverseRotationIndexById(int id)
    {
        switch(id)
        {
            case 2: 
            case 4:
            case 7:
            {
                return 1; 
            }
            case 3:
            case 5:
            case 6:
            {
                return 3; 
            }
        }
        return 0;
    }
}