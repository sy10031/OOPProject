
package tetris;

import java.awt.Color;
import java.awt.Graphics; 
import java.awt.Dimension; 
import java.awt.BorderLayout;
import java.awt.Point; 
import java.awt.FlowLayout;
import java.awt.Font;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent; 

import javax.swing.JPanel; 
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory; 

import java.applet.AudioClip;
import java.applet.Applet;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Image; 

public class GamePanel extends JPanel implements Runnable
{   
    public Image backImage; 
    public ImageIcon backIcon;
    
    public enum Status { START, PLAYING, PAUSED, COUNTDOWN, GAMEOVER };
    
    public Status status; 

    public GameStats gameStats;
    
    public AudioClip tetrisTheme; 
    public AudioClip moveSound; 
    public AudioClip rotateSound; 
    public AudioClip hardDropSound; 
    public AudioClip countDownSound; 
    public AudioClip countOverSound; 
    
    private final int FPS    = 25; 
    private       int frames;
    
    public boolean musicOn;
    
    private int     minoDelay;
    private boolean minoDelaying; 
        
    private int countDown; 
    
    private MatrixPanel matrix; 
    private StatsPanel  stats; 
    private QueuePanel  queue; 
    
    private boolean[] keys; 
    
    private boolean hasHeld; 
    
    private MinoQueue minos;
    private Mino currentMino;
    
    private int linesLeft;
    
    private Ghost ghost; 
    
    private int lines, singles, doubles, triples, tetrises, minoCount;
    
    private int comboCount; 
       
    public GamePanel()
    {
        setPreferredSize(new Dimension(Tetris.width, Tetris.height));
        setSize(getPreferredSize());
        setLayout( new FlowLayout() );
        addKeyListener( new KeyHandler() );
        addComponentListener( new ComponentHandler() );
        setBackground(null);
        setFocusable(true);
        setOpaque(false);
        
        backImage = new ImageIcon(getClass().getResource("blue-background.jpg")).getImage();
        Image back = backImage.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);  
        backIcon = new ImageIcon(back);
        
        tetrisTheme   = Applet.newAudioClip(getClass().getResource("tetris-theme.aiff"));
        rotateSound   = Applet.newAudioClip(getClass().getResource("rotateSound.wav"));
        moveSound     = rotateSound; 
        hardDropSound = Applet.newAudioClip(getClass().getResource("hardDropSound.wav"));
        countDownSound= Applet.newAudioClip(getClass().getResource("countDownSound.wav"));
        countOverSound= Applet.newAudioClip(getClass().getResource("countDownSound.wav"));
        
        resetGame();
    }
    
    
    public void resetGame()
    {     
        if (Tetris.navigation.musicIsOn)
        {
            musicOn = true;
        }
        else
            musicOn = false; 
        
        lines = singles = doubles = triples = tetrises = minoCount = 0;
        
        comboCount = 0;
        countDown  = 3;
        minoDelay  = 0;
        minoDelaying = false;
        
        status = Status.START;
        
        frames = 0; 
        hasHeld = false;
        
        keys = new boolean[8]; 
        
        for(int i=0; i<keys.length; i++)
        {
            keys[i] = false;
        }

        minos = new MinoQueue(); 
        
        linesLeft = 1; 
 
        stats     = new StatsPanel (); 
        queue     = new QueuePanel (); 
        matrix    = new MatrixPanel();
        
        JPanel gameAndQueue = new JPanel();
        gameAndQueue.setLayout(new BorderLayout(0,0));
        gameAndQueue.setOpaque(false);
        
        JPanel game = new JPanel();
        game.setLayout(new BorderLayout(4,4));
        game.setBackground(null);
        game.setOpaque(false);
        game.setBorder( BorderFactory.createLoweredBevelBorder());
        game.add(matrix, BorderLayout.CENTER);
        
        JPanel pad = new JPanel(); 
        pad.setOpaque(false);
        pad.setBackground(null);
        pad.setPreferredSize(new Dimension(this.getWidth(), 50));
        
        add(pad);
        
        gameAndQueue.add(game, BorderLayout.CENTER);
        gameAndQueue.add(queue,BorderLayout.EAST);
        gameAndQueue.add(stats, BorderLayout.WEST);
       
        add(gameAndQueue);
    }
    
    
    @Override
    public void run()
    {
        while( status != Status.GAMEOVER )
        {
            if (status == Status.PLAYING)
            {
                frames++; 

                if ( frames % 2 == 0)
                {
                    stats.timer.update();
                    
                    if (frames % 4 == 0)
                    {
                        stats.repaint();
                    }
                }
                
                if (minoDelaying)
                {
                    minoDelay++;
                }
                
                if (minoDelay == 15)
                {
                    minoDelay = 0; 
                    minoDelaying = false;
                    matrix.minoToMatrixElement(currentMino);
                    setNextMino();
                }

                if (frames %30 == 0)
                {       
                    currentMino.move(new Point(0, 1));
                    
                    checkMoveAgainstMatrix(currentMino, new Point(0, 1));

                    getGhostCoors();

                    this.repaint();
                }
            }
            
            else if (status == Status.START)
            {
                
            }
            
            else if (status == Status.PAUSED)
            {
                
            }
            
            else if (status == Status.COUNTDOWN)
            {
                frames++; 
                
                if (frames % 40 == 0)
                {
                    countDownSound.play();
                    countDown--; 
                    repaint();
                    
                    if (countDown == 1)
                    {
                        if (musicOn)
                        {
                            tetrisTheme.loop();
                        }
                    }
                }
                
                if (countDown == 0)
                {      
                    countOverSound.play();
                    
                    status = Status.PLAYING;
                    
                    setNextMino();
                            
                    ghost = new Ghost(currentMino);
                    getGhostCoors();
                    
                    Tetris.navigation.pauseButton.setEnabled(true);              
                }
            }
            
            try
            {
                Thread.sleep(FPS);
            }
            catch(InterruptedException exception)
            {
                break;
            }
        }
    }
    
    
    public void setBackImage(Image image)
    {
        backImage = image;
        Image back = backImage.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);  
        backIcon = new ImageIcon(back);
        
        repaint();
    }
    
    
    public void getGhostCoors()
    {
        if (status != Status.PLAYING)
        {
            return; 
        }
        
        ghost  = new Ghost(currentMino);
        
        while(!checkGhostAgainstMatrix(ghost, new Point(0, 1)))
        {
            ghost.move(new Point(0, 1));
        }
    }
    
    
    public boolean checkGhostAgainstMatrix( Mino mino, Point shift)
    {
        for(int i=0; i<4; i++)
        {
            if (mino.pCoors[i].y == 19)
            {
                return true;
            }

            
            for(int j=0; j<matrix.elements.length; j++)
            {
                for(int k=0; k<matrix.elements[j].length; k++)
                {
                    if (matrix.elements[j][k] != null && mino.pCoors[i].x == j
                                                    && mino.pCoors[i].y == k-1)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    
    public boolean checkMinoAgainstMatrix(Mino mino)
    {
        for(int i=0; i<4; i++)
        {
            if (mino.pCoors[i].y >= 19)
            {
                return true;
            }

            for(int j=0; j<matrix.elements.length; j++)
            {
                for(int k=0; k<matrix.elements[j].length; k++)
                {
                    if (matrix.elements[j][k] != null && mino.pCoors[i].x == j
                                                    && mino.pCoors[i].y == k-1)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    
    public boolean minoOutOfBounds(Mino mino)
    {
        int count = 0;
        for(int i=0; i<4; i++)
        {
            if (mino.pCoors[i].x <= 9 && mino.pCoors[i].x >= 0)
            {
                count++;
                
                if (count == 4)
                {
                    return false; 
                }
            }    
        }
        return true;
    }
    
    
    public void checkMinoAgainstBorders(Mino mino)
    {
        if (!minoOutOfBounds(mino))
        {
            return; 
        }
        
        int x = 1;
        int y = 0;
        
        for(int i=0; i<4; i++)
        {
            if (mino.pCoors[i].x > 9)
            {
                x = -1; 
            }
        }
        
        Point shift = new Point(x, y);

        while (minoOutOfBounds(mino))
        {
            mino.move(shift);
        }
    }
    
    
    public boolean bottomEmpty(Mino mino)
    {
        for(int i=0; i<4; i++)
        {
            if (mino.pCoors[i].y == 19)
                return false;
            
            if (matrix.elements[mino.pCoors[i].x][mino.pCoors[i].y+1] == null)
            {
                return true;
            }
        }
        return false;
    }
    
    
    public void checkMinoAgainstMatrixAndCorrect(Mino mino)
    {
        if (!checkMinoAgainstMatrix(mino))
        {
            return; 
        }
        
        Point shift = new Point(0,-1);
        
        //int count = 0; 
        while (checkMinoAgainstMatrix(mino))
        {
            mino.move(shift);
        }
    }
    
    
    public void checkMoveAgainstMatrix( Mino mino, Point shift)
    {
        for(int i=0; i<4; i++)
        {
            if (mino.pCoors[i].y > 19)
            {
                currentMino.move(new Point(shift.x*-1, shift.y*-1));
                if (shift.x == 0)
                {
                    //matrix.minoToMatrixElement(mino);
                    //setNextMino();
                    minoDelaying = true;
                }
                return;
            }
            
            if (mino.pCoors[i].x > 9 || mino.pCoors[i].x < 0)
            {
                currentMino.move(new Point(shift.x*-1, shift.y*-1));
            }
            
            for(int j=0; j<matrix.elements.length; j++)
            {
                for(int k=0; k<matrix.elements[j].length; k++)
                {
                    if (matrix.elements[j][k] != null && mino.pCoors[i].x == j
                                                    && mino.pCoors[i].y == k)
                    {
                        currentMino.move(new Point(shift.x*-1, shift.y*-1)); 

                        
                    if (shift.x == 0)
                    {
                        //matrix.minoToMatrixElement(mino);
                        //setNextMino();
                        minoDelaying = true;
                    }
                        return;
                    }
                }
            }
        }
    }
    
    
    public void setNextMino()
    {
        minoCount++;
        minoDelay = 0;
        minoDelaying = false;
       
        matrix.checkLineClears();
        
        boolean gameOver = false;
        
        if (linesLeft <= 0)
        {
            gameOver = true;
        }

        if (gameOver)
        {
            gameOverMethod(true);
        }
 
        if (status == Status.PLAYING)
        {
            currentMino = new Mino(minos.newMino(), true);
            minos.shiftUp();
            queue.adjust();
            hasHeld = false;
        }
    }
    
    
    public void gameOverMethod(boolean wins)
    {
        for(int j=0; j<keys.length; j++)
        {
            keys[j] = false;
        }

        status    = Status.GAMEOVER;
        
        tetrisTheme.stop();

        gameStats = new GameStats( wins,stats.timer.minutes, 
                                        stats.timer.seconds, 
                                        stats.timer.hundredths,
                                        lines  , singles , doubles  ,
                                        triples, tetrises, minoCount);

        Tetris.navigation.pauseButton.setEnabled(false);
        
        JPanel pad = new JPanel(); 
        pad.setOpaque(false);
        pad.setBackground(null);
        pad.setPreferredSize(new Dimension(this.getWidth(), 50));
        
        this.removeAll();
        this.revalidate();
        this.add(pad);
        this.add(gameStats);
        this.repaint();
    }
    
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        g.drawImage(backIcon.getImage(), 0, 0, null);
    }
    
    
    public void keyUpdate()
    {
        if (keys[0])
        {
            minoDelay = 0;  
            minoDelaying = false;
            currentMino.rotate(1);
            
            while (( checkMinoAgainstMatrix(currentMino) ) || 
                   ( minoOutOfBounds(currentMino)) )
            {
                checkMinoAgainstMatrixAndCorrect(currentMino);

                checkMinoAgainstBorders(currentMino);
            }
            keys[0] = false;     
//            rotateSound.play();
        }
        
        if (keys[1])
        {
            currentMino.move(new Point(1, 0));
            checkMoveAgainstMatrix(currentMino, new Point(1, 0));
            minoDelaying = false;
            minoDelay = 0;
            keys[1] = false;
//            moveSound.play();
            
        }
        
        if (keys[2])
        {
            currentMino.move(new Point(0, 1));
            checkMoveAgainstMatrix(currentMino, new Point(0, 1));

        }
        
        if (keys[3])
        {
            currentMino.move(new Point(-1, 0));
            checkMoveAgainstMatrix(currentMino, new Point(-1, 0));
            minoDelaying = false;
            minoDelay = 0;
            keys[3] = false;
//            moveSound.play();
        }
        
        if (keys[4])
        {
            currentMino.pCoors = ghost.pCoors;
            matrix.minoToMatrixElement(currentMino);
            setNextMino();

            keys[4] = false;
//            hardDropSound.play();
        }
        
        if ( (keys[5]) && !hasHeld)
        {
            if (minos.heldMino != null)
            {
                Mino tempC = new Mino(minos.heldMino, true); 
                minos.heldMino = new Mino(currentMino, true); 
                currentMino = new Mino(tempC, true);
            }
            else
            {
                Mino tempC    = new Mino(currentMino, true); 
                minos.heldMino= new Mino(tempC, true); 
                
                currentMino   = new Mino(minos.newMino(), true);              
                
                minos.shiftUp();
                queue.adjust();
            }
            stats.hold.mino = minos.heldMino; 
            hasHeld = true;
        }
        
        if (keys[6])
        {
            if (status == Status.PAUSED)
            {
                Tetris.navigation.playButton.doClick();
            }
            
            else if (status == Status.PLAYING)
            {
                Tetris.navigation.pauseButton.doClick();
            }
            Tetris.navigation.repaint();
            repaint();
        }
        
        if (keys[7])
        {
            currentMino.reverseRotate();
            
            while (( checkMinoAgainstMatrix(currentMino) ) || 
                   ( minoOutOfBounds(currentMino)) )
            {
                checkMinoAgainstMatrixAndCorrect(currentMino);

                checkMinoAgainstBorders(currentMino);
            }
            keys[7] = false;
            minoDelaying = false;
            minoDelay = 0;  
        }
    }
    
    
    private class KeyHandler extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent event)
        {
            if (status == Status.PLAYING)
            {
                if (event.getKeyCode() == KeyEvent.VK_UP)
                {
                    keys[0] = true;
                }
                if (event.getKeyCode() == KeyEvent.VK_RIGHT)
                {
                    keys[1] = true;
                }
                if (event.getKeyCode() == KeyEvent.VK_DOWN)
                {
                    keys[2] = true;
                }
                if (event.getKeyCode() == KeyEvent.VK_LEFT)
                {
                    keys[3] = true;
                }
                if (event.getKeyCode() == KeyEvent.VK_SPACE)
                {
                    keys[4] = true;
                }
                if ((event.getKeyCode() == KeyEvent.VK_SHIFT) ||
                    (event.getKeyCode() == KeyEvent.VK_C))
                {
                    keys[5] = true;
                }
                if (event.getKeyCode() == KeyEvent.VK_Z)
                {
                    keys[7] = true;
                }
            }
                if (event.getKeyCode() == KeyEvent.VK_P)
                {
                    keys[6] = true;
                }
            keyUpdate(); 
            getGhostCoors();
            repaint();
        }
        
        
        @Override
        public void keyReleased(KeyEvent event)
        {
            if (status == Status.PLAYING)
            {
                if (event.getKeyCode() == KeyEvent.VK_UP)
                {
                    keys[0] = false;
                }
                if (event.getKeyCode() == KeyEvent.VK_RIGHT)
                {
                    keys[1] = false;
                }
                if (event.getKeyCode() == KeyEvent.VK_DOWN)
                {
                    keys[2] = false;
                }
                if (event.getKeyCode() == KeyEvent.VK_LEFT)
                {
                    keys[3] = false;
                }
                if (event.getKeyCode() == KeyEvent.VK_SPACE)
                {
                    keys[4] = false;
                }
                if ((event.getKeyCode() == KeyEvent.VK_SHIFT) ||
                    (event.getKeyCode() == KeyEvent.VK_C))
                {
                    keys[5] = false;
                }
                if (event.getKeyCode() == KeyEvent.VK_Z)
                {
                    keys[7] = false;
                }
            }
                if (event.getKeyCode() == KeyEvent.VK_P)
                {
                    keys[6] = false;
                }
        }
    }
    

    private class ComponentHandler extends ComponentAdapter
    {
        @Override
        public void componentHidden(ComponentEvent e) 
        {

        }

        @Override
        public void componentResized(ComponentEvent e) 
        {
            Image lake = backImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);  
            backIcon = new ImageIcon(lake);
        }

        @Override
        public void componentShown(ComponentEvent e) 
        {

        }
    }

    
    class MatrixPanel extends JPanel
    {
        MatrixElement[][] elements; 

        public MatrixPanel()
        {
            setSize(270, 540);
            setOpaque(false);
            
            elements = new MatrixElement[10][20]; 
            
            for(int i=0; i<10; i++)
            {
                for(int j=0; j<20; j++)
                {
                    elements[i][j] = null; 
                }
            }
        }
        
        
        public void minoToMatrixElement(Mino mino)
        {
            for(int i=0; i<4; i++)
            {
                if (mino.pCoors[i].y < 0)
                {
                    gameOverMethod(false);
                    return;
                }
                elements[mino.pCoors[i].x][mino.pCoors[i].y] = new MatrixElement(mino.color, mino.pCoors[i]);
            }
        }
        
        
        public void checkLineClears()
        {
            for(int i=0; i<20; i++)
            {
                int count = 0;
                for(int j=0; j<10; j++)
                {
                     if (elements[j][i] != null)
                     {
                         count++; 
                     }
                     if (count == elements.length)
                     {
                         System.out.println("Line clear at:"+i);
                         removeRow(i);
                         linesLeft--;
                         lines++;
                         comboCount++;
                         stats.updateLines();
                     }
                }
            }
            if (comboCount == 1)
            {
                singles++;
            }
            else if (comboCount == 2)
            {
                doubles++; 
            }
            else if (comboCount == 3)
            {
                triples++;
            }
            else if (comboCount == 4)
            {
                tetrises++;
            }
            comboCount = 0;
        }
        
        
        public void removeRow(int row)
        {
            for(int i=0; i<10; i++)
            {
                elements[i][row] = null; 
            }
            
            MatrixElement[][] temp = new MatrixElement[10][20];
            
            for(int i=0; i<20; i++)
            {
                for(int j=0; j<10; j++)
                {
                    if (elements[j][i] != null)
                    {
                        temp[j][i] = new MatrixElement(elements[j][i]);
                    }
                }
            }
            
            for(int i=row; i>=0; i--)
            {
                for(int j=0; j<10; j++)
                {
                    elements[j][i] = null; 
                
                    if (i != 0)
                    {
                        if (temp[j][i-1] != null)
                        {
                            elements[j][i] = new MatrixElement(temp[j][i-1].color, new Point(temp[j][i-1].coors.x, temp[j][i-1].coors.y+1));
                        }
                    }
                }
            }
        }

        
        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            drawBackground(g); 
            
            if (status == Status.PLAYING || status == Status.PAUSED)
            {

                for(int i=0; i<elements.length; i++)
                {
                    for(int j=0; j<elements[i].length; j++)
                    {
                        if (elements[i][j] != null)
                            elements[i][j].draw(g, Tetris.dim);
                    }
                }
                ghost.draw(g);
                currentMino.draw(g);
            }
            
            else if (status == Status.COUNTDOWN)
            {
                g.setColor(Color.white);
                g.setFont(new Font("Helvetica", Font.PLAIN, 36));
                g.drawString(""+countDown, getWidth()/2-9, getHeight()/2-36);
            }
        }
        

        public void drawBackground(Graphics g)
        {
        for(int i=0; i<elements.length; i++)
            {
                for(int j=0; j<elements[i].length; j++)
                {
                    if ((i+j) % 2 == 0)
                        g.setColor(new Color(40,40,40,128)); 
                    else
                        g.setColor(new Color(50,50,50,128));

                    g.fillRoundRect(i*Tetris.dim, j*Tetris.dim, Tetris.dim+1, Tetris.dim+1, 4, 4);
                }
            }
        }

        
        @Override
        public Dimension getPreferredSize()
        {
            return new Dimension(220, 440);
        }
    }

    
    class QueuePanel extends HUDPanel
    {
        MinoPanel[] minoPanels; 
        
        public QueuePanel()
        {
            setLayout(new BorderLayout());
            setOpaque(false);
            JPanel queue = new JPanel(); 
            queue.setLayout(new FlowLayout());
            queue.setOpaque(false);
        
            minoPanels = new MinoPanel[5]; 
            
            GameElementPanel queueL = new GameElementPanel(); 
            queueL.setPreferredSize(new Dimension(88, 28)); 
            JLabel queueLabel = new JLabel("NEXT");
            queueL.add(queueLabel); 
            
            queue.add(queueL);
            
            for(int i=0; i<5; i++)
            {
                minoPanels[i] = new MinoPanel(minos.minos.get(i)); 
                
                if (i == 0)
                {               
                    minoPanels[i].setPreferredSize(new Dimension(88, 88));
                    minoPanels[i].setBorder(BorderFactory.createLoweredBevelBorder());
                }
                
                if (i == 1)
                {               
                    minoPanels[i].setPreferredSize(new Dimension(79, 69));
                }
                queue.add(minoPanels[i]); 
            }
            
            add(queue, BorderLayout.CENTER);
        }
        
        
        public void adjust()
        {
            for(int i=0; i<5; i++)
            {
                minoPanels[i].mino = new Mino(minos.minos.get(i), true);
            }
        }
        
        
        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
        }
        
        
        @Override
        public Dimension getPreferredSize()
        {
            return new Dimension(110, 440);
        }
    }
    
    
    class MinoPanel extends JPanel
    {
        Mino mino; 
        
        public MinoPanel(Mino mino)
        {
            this.mino = mino;
            
            setBackground(null);
            setOpaque(false);
            setBorder(BorderFactory.createLineBorder(Color.darkGray));
            setPreferredSize(new Dimension(70, 60));
        }
        
        public MinoPanel()
        {
            setBackground   (null);
            setPreferredSize(new Dimension(70, 60));
        }
        
        
        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            
            g.setColor(new Color(128,128,128,128));
            g.fillRoundRect(0,0,this.getWidth(),this.getHeight(), 10, 10);
            
            if (mino != null)
                mino.drawInQueue(g, this.getWidth(), this.getHeight());
        }
    }
    
    
    class StatsPanel extends HUDPanel
    {
        MinoPanel hold; 
        JLabel    holdLabel;
        Timer timer; 
        JLabel timerLabel;
        JLabel linesLeftLabel;
 
        public StatsPanel()
        {
            setOpaque(false);
            
            timer = new Timer(); 
            
            GameElementPanel time = new GameElementPanel(); 
            time.setPreferredSize(new Dimension(100, 28)); 
            timerLabel = new JLabel("TIME");
            
            time.add(timerLabel); 
            
            GameElementPanel holdL = new GameElementPanel(); 
            holdL.setPreferredSize(new Dimension(88, 28)); 
            holdLabel = new JLabel("HOLD");
            holdL.add(holdLabel); 
            
            GameElementPanel linesL = new GameElementPanel(); 
            linesL.setPreferredSize(new Dimension(88, 28));
            JLabel lines = new JLabel("LINES");
            linesL.add(lines); 
            
            GameElementPanel linePanel = new GameElementPanel(); 
            linePanel.setPreferredSize(new Dimension(88, 40)); 
            linesLeftLabel = new JLabel(""+linesLeft); 
            linesLeftLabel.setFont(new Font("Helvetica", Font.PLAIN, 24));
            linePanel.add(linesLeftLabel); 
            
            hold = new MinoPanel();
            hold.setPreferredSize(new Dimension(88, 88));
            hold.setBorder(BorderFactory.createLineBorder(Color.darkGray));

            hold.setOpaque(false);

            add(holdL);
            add(hold);
            
            JPanel pad = new JPanel();
            pad.setPreferredSize(new Dimension(88, 30));
            pad.setOpaque(false);
            
            add(pad);
            
            add(time);
            add(timer); 
                  
            JPanel pad2 = new JPanel();
            pad2.setPreferredSize(new Dimension(88, 20));
            pad2.setOpaque(false);
            add(pad2);
            
            add(linesL);
            add(linePanel);
        }
        
        
        public void updateLines()
        {
            linesLeftLabel.setText(""+linesLeft); 
        }
        
        
        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
        }
    
        
        @Override
        public Dimension getPreferredSize()
        {
            return new Dimension(110, 440);
        }
    }
}