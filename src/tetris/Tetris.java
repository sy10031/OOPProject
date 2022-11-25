/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import java.awt.BorderLayout;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.Color; 
import javax.swing.JFrame;
import java.awt.Image; 
import java.awt.Toolkit;
import java.awt.Dimension; 
import javax.swing.JPanel;
import java.awt.Graphics; 

public class Tetris extends JFrame
{
    public Image lake; 
    
    public static int width = 1000; 
    public static int height= 655; 
    public static int dim = 440/20;
    
    public static NavigationBar navigation;
    public static GamePanel app; 
    
    public static ExecutorService executor;
    
    public Tetris()
    {
        setLayout( new BorderLayout(0,0) );
        setSize(width, height);
   //     setBackground(Color.gray); 
        
        navigation= new NavigationBar(); 
        app       = new GamePanel(); 
        
        JPanel pad = new JPanel(); 
        pad.setPreferredSize(new Dimension(100, 100));
        pad.setSize(100, 100);
        pad.setOpaque(false); 
        
        add( app       , BorderLayout.CENTER);
        
        add( navigation, BorderLayout.NORTH );
        
        setVisible(true);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        start();
    }
    
    public static void start()
    { 
        executor = Executors.newCachedThreadPool();
        
        executor.execute( app );
    }
    
    public static void stop()
    {
        executor.shutdown();
    }
    
    public static void restart()
    {
        navigation.playButton.setEnabled(true);
        
        app.resetGame();
         
        executor = Executors.newCachedThreadPool();
        
        executor.execute(app);
        
        app.repaint();
        
        app.revalidate();
    }
}