
package tetris; 

import java.awt.*;
import static java.awt.Color.cyan;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

    
public class Timer extends JPanel
{
    int minutes; 
    int seconds; 
    int hundredths; 

    String min; 
    String sec; 
    String hund; 

    String time; 


    public Timer()
    {
        setLayout(new FlowLayout());
        setSize(getPreferredSize());
        setBorder(BorderFactory.createLineBorder(Color.darkGray));
        setOpaque(false);

        min = sec = hund = "00";
        minutes = seconds = hundredths = 0;
        time = min+":"+sec+":"+hund; 
    }


    public void update()
    {
        hundredths+=5;

        if (hundredths >= 100)
        {
            hundredths = 0;
            seconds++;
        }
        if (seconds >= 60)
        {
            seconds = 0;
            minutes++; 
        }

        if (hundredths < 10)
        {
            hund = "0"+hundredths; 
        }
        else hund = ""+hundredths; 

        if (seconds < 10)
        {
            sec = "0"+seconds; 
        }
        else sec = ""+seconds; 

        if (minutes < 10)
        {
            min = "0"+minutes; 
        }
        else min = ""+minutes; 

        time = new String(min+":"+sec+":"+hund); 
    }


    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        g.setColor(cyan);
        g.fillRoundRect(0,0,this.getWidth(),this.getHeight(), 10, 10);
   
        g.setColor(Color.black);
        g.setFont(new Font("Helvetica", Font.PLAIN, 22));
        g.drawString(time, 5,35);
        
    }


    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(100,50); 
    }


    @Override
    public String toString()
    {
        return new String(min+":"+sec+":"+hund);
    }
}
    