
package tetris;

import javax.swing.JPanel; 
import javax.swing.JButton; 
import javax.swing.JLabel; 
import javax.swing.JTextField;
import java.awt.BorderLayout; 
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener;

public class GameStats extends JPanel
{
    JLabel title; 
    JPanel stats; 
    ScoreTablePanel[] scores; 
    JPanel scoreSubmit; 
    JButton cancelButton; 
    JButton submitButton; 
    JButton playAgainButton;
    JTextField nameField; 

    String time; 
    
    int lines; 
    int singles; 
    int doubles; 
    int triples; 
    
    int tetrises;
    int tetrominos; 
    float linesPerMinute; 
    float minosPerMinute;
    
    public GameStats(boolean win, int min , int sec  , int milli , int line , 
                     int single , int doub, int trip , int tetris, int tetros)
    {   
        String hundredth; 
        String second; 
        String minute; 

        if (milli < 10)
        {
            hundredth = "0"+milli; 
        }
        else hundredth = ""+milli; 

        if (sec < 10)
        {
            second = "0"+sec; 
        }
        else second = ""+sec; 

        if (min < 10)
        {
            minute = "0"+min; 
        }
        else minute = ""+min; 

        time = new String(minute+":"+second+":"+hundredth); 

        lines   = line; 
        singles = single;
        doubles = doub;
        triples = trip;
        tetrises= tetris;
        
        tetrominos     = tetros;
        
        linesPerMinute = lines      / (min+((float)((float)sec+((float)milli/1000))/60));
        minosPerMinute = tetrominos / (min+((float)((float)sec+((float)milli/1000))/60));
        
        setSize(450, 350);
        setPreferredSize(new Dimension(500, 350));
        setLayout(new FlowLayout());
        setOpaque(false);
        
        title = new JLabel("TIME: "+time);
        
        if (win == false)
        {
            title = new JLabel("GAME OVER");
        }
        
        title.setForeground(Color.white);
        title.setFont(new Font("Helvetica", Font.BOLD, 28));
        
        stats = new JPanel(); 
        stats.setLayout(new FlowLayout());
        stats.setOpaque(false);
        stats.setPreferredSize(new Dimension(480, 140));
        
        scores = new ScoreTablePanel[8];

        scores[0] = new ScoreTablePanel(new Color(128,128,128,148));
        scores[1] = new ScoreTablePanel(new Color(200,200,200,148));
        scores[3] = new ScoreTablePanel(new Color(128,128,128,148));
        scores[2] = new ScoreTablePanel(new Color(200,200,200,148));
        scores[4] = new ScoreTablePanel(new Color(128,128,128,148));
        scores[5] = new ScoreTablePanel(new Color(200,200,200,148));
        scores[7] = new ScoreTablePanel(new Color(128,128,128,148));
        scores[6] = new ScoreTablePanel(new Color(200,200,200,148));
        
        scores[0].setLabel("Lines: ");
        scores[0].setValue(""+lines);
        
        scores[1].setLabel("Lines Per Minute: ");
        scores[1].setValue(""+linesPerMinute);
        
        scores[2].setLabel("Minos Locked Down: ");
        scores[2].setValue(""+tetrominos);
       
        scores[3].setLabel("Minos Per Minute: ");
        scores[3].setValue(""+minosPerMinute);
       
        scores[4].setLabel("Singles: ");
        scores[4].setValue(""+singles);
        
        scores[5].setLabel("Doubles: ");
        scores[5].setValue(""+doubles);
        
        scores[6].setLabel("Triples: ");
        scores[6].setValue(""+triples);
        
        scores[7].setLabel("Tetrises: ");
        scores[7].setValue(""+tetrises);
        
        for(int i=0; i<scores.length; i++)
        {
            stats.add(scores[i]);
        }

        JPanel textPanel = new JPanel(); 
        textPanel.setLayout(new FlowLayout());
        textPanel.setOpaque(false);
        
        scoreSubmit = new JPanel(); 
        scoreSubmit.setOpaque(false);
        scoreSubmit.setLayout(new FlowLayout()); 
        
        nameField = new JTextField("Enter your name...", 20);
        
        ButtonHandler handler = new ButtonHandler();
        
        cancelButton = new JButton("Cancel"); 
        submitButton = new JButton("Submit Score");
        
        cancelButton.addActionListener(handler);
        submitButton.addActionListener(handler); 
        

            playAgainButton = new JButton("PLAY AGAIN");
            playAgainButton.addActionListener(handler); 
            scoreSubmit.add(playAgainButton) ;
        
        add(title);
        add(stats); 
        
//        if (win == true)
//        {
//            add(textPanel);
//        }
//        
        add(scoreSubmit); 
        
        setVisible(true);
        
        nameField.setFocusable(true);
    }
    
    class ScoreTablePanel extends JPanel
    {
        Color color; 
        JLabel label; 
        JLabel value; 
        
        public ScoreTablePanel(Color clr)
        {
            setLayout(new BorderLayout(5, 5));
            setPreferredSize(new Dimension(230, 30));

            color = clr; 
        }
        
        public void setLabel(String label)
        {
            this.add(new JLabel("   "+label), BorderLayout.WEST);
        }
        
        public void setValue(String value)
        {
            this.add(new JLabel(value+"   "), BorderLayout.EAST);
        }
        
        @Override
        public void paintComponent(Graphics g)
        {
            g.setColor(color);
            g.fillRoundRect(0,0,this.getWidth(),this.getHeight(),4,4);
        }
    }
    
    private class ButtonHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            Tetris.app.removeAll(); 
            Tetris.stop(); 
            Tetris.restart();
        }
    }
}