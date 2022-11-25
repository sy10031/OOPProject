
package tetris;

import java.util.ArrayList;
import java.util.Random;

public class MinoQueue
{
    Mino heldMino; 
    ArrayList<Mino> minos; 
    Random rand; 
    
    public MinoQueue()
    {
        rand = new Random();
        
        heldMino = null; 
        
        minos = new ArrayList<Mino>(); 
        
        for( int i=0; i<5; i++ )
        {
            minos.add( new Mino( rand.nextInt(7)+1 ));
        }
    }
    
    public void shiftUp()
    {
        minos.add(new Mino( rand.nextInt(7)+1 ));
        minos.remove(0);  
    }
    
    public Mino newMino()
    {
        return minos.get(0);
    }
    
    public void setHeld( Mino held )
    {
        heldMino = held; 
    }
    
    public Mino getHeld()
    {
        return heldMino; 
    }
}