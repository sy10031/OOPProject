/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris; 

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout; 
import javax.swing.ImageIcon;
import javax.swing.JButton; 
import javax.swing.JPanel; 
import java.awt.Image;
import java.awt.Color; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;

public class NavigationBar extends JMenuBar 
{
    boolean musicIsOn; 
//    boolean fxIsOn; 
    
    JMenuBar playPause; 
    JMenuBar otherButtons; 
    
    JButton pauseButton; 
    JButton playButton; 
    JButton highScoresButton; 
//    JButton changeBackground; 
    
    Image soundOn; 
    Image soundOff; 
//    Image backgroundChange; 
    JLabel soundIcon;
    JButton soundToggle; 
    
    JLabel playLabel; 
    JLabel pauseLabel; 
    JLabel highScoresLabel; 
//    JLabel backgroundLabel;
    
    public NavigationBar()
    {
//        musicIsOn = false; 
////        fxIsOn    = false; 
//        
        ButtonHandler handler = new ButtonHandler(); 
        
        setBackground(Color.BLUE);
        setLayout(new BorderLayout());
        
        playPause = new JMenuBar(); 
        playPause.setLayout(new FlowLayout()); 
        playPause.setBackground(Color.blue);
        
        otherButtons = new JMenuBar();
        otherButtons.setLayout(new FlowLayout());
        otherButtons.setBackground(Color.blue);
     
        highScoresLabel = new JLabel("High Scores"); 
        
        Image img =  new ImageIcon(getClass().getResource("high-scores.png")).getImage();
        Image resized = img.getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);  
        
        highScoresLabel.setIcon(new ImageIcon(resized));

        highScoresButton = new JButton();
        highScoresButton.add(highScoresLabel);
        
        highScoresButton.addActionListener(handler);
        
        otherButtons.add(highScoresButton, BorderLayout.CENTER);
        
        
        playLabel = new JLabel("PLAY"); 

        
        playButton = new JButton();
        playButton.add(playLabel); 
        playButton.setBackground(Color.GREEN);
        
        playButton.addActionListener(handler); 
                
        pauseLabel = new JLabel("PAUSE"); 

        pauseButton = new JButton();
        pauseButton.add(pauseLabel); 
        pauseButton.setBackground(Color.ORANGE);
        
        pauseButton.addActionListener(handler); 
        
        playPause.add(playButton); 
        playPause.add(pauseButton); 

        soundOn =  new ImageIcon(getClass().getResource("sound-on.png")).getImage();
        soundOn = soundOn.getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
        
        soundOff =  new ImageIcon(getClass().getResource("sound-off.png")).getImage();
        soundOff = soundOff.getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
        
        soundIcon = new JLabel(); 
        soundIcon.setIcon(new ImageIcon(soundOff));
        
        JMenuBar othersPanel = new JMenuBar();
        othersPanel.setLayout(new FlowLayout()); 
        othersPanel.setBackground(null);
        
        soundToggle = new JButton(); 
        soundToggle.add(soundIcon);
        soundToggle.addActionListener(handler);

        add(playPause     , BorderLayout.WEST); 
        add(othersPanel  , BorderLayout.EAST);

        pauseButton.setEnabled(false);
        
        setButtonsOff();
    }

    private class ItemHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            
        }
    }
    
    private class ButtonHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            if (event.getSource() == playButton)
            {
                if (Tetris.app.status == GamePanel.Status.START)
                {
                    Tetris.app.countDownSound.play();
                    
                    Tetris.app.status = GamePanel.Status.COUNTDOWN;
                    pauseButton.setEnabled(false);
                    
                }
                
                else if (Tetris.app.status == GamePanel.Status.PAUSED)
                {
                    Tetris.app.status = GamePanel.Status.PLAYING;
                    pauseButton.setEnabled(true);
                    
                    if (Tetris.app.musicOn)
                    {
                        Tetris.app.tetrisTheme.loop();
                    }
                }
                playButton.setEnabled(false);
                Tetris.app.repaint();
                
            }
            
            if (event.getSource() == pauseButton)
            {
                Tetris.app.status = GamePanel.Status.PAUSED;
                playButton.setEnabled(true);
                pauseButton.setEnabled(false);
                
                Tetris.app.tetrisTheme.stop();
            }
            
            if (event.getSource() == soundToggle)
            {

            }

            if (event.getSource() == highScoresButton)
            {
                
            }   
            repaint();
            setButtonsOff();
        }
    }
    
    public void setButtonsOff()
    {
        playButton.setFocusable(false);
        pauseButton.setFocusable(false);
        highScoresButton.setFocusable(false);
        soundToggle.setFocusable(false);
    }
}