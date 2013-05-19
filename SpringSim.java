import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.UIManager;
import javax.swing.JSlider;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.JToolTip;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.KeyboardFocusManager;
import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Image;
import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import java.awt.Graphics; 
import java.awt.Graphics2D; 
import java.awt.image.BufferedImage;
import javax.swing.Timer;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JApplet;
import java.applet.Applet;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.lang.ClassNotFoundException;
import javax.swing.UnsupportedLookAndFeelException;
import java.io.Serializable;

public class SpringSim extends Applet 
{
    public void init() 
    {   
        try
        {
            EventQueue.invokeLater(new Runnable() 
                {
                    public void run()
                    {
                        JPanel panel = new SpringSimulation();
                        add(panel);

                    }
                }
            );
        }
        catch (Exception e)
        {
            System.err.println("The program did not create the GUI correctly");
        }
    }
}
class SpringSimulation  extends JPanel implements MouseListener, MouseMotionListener, KeyListener, ActionListener
{

    ArrayList nodes; 
    ArrayList springs;
    ArrayList masses;
    Node catched;

    private static Timer timer;

    private int _delay = 0; 
    private int _initFrameRate = 50; 
    private int _frameCount = 0;
    private int _frameRate = 0; 
    private int _displayFrameRate = 0; 
    private long startTime = System.currentTimeMillis(); 

    private static double prevX, prevY;
    private static double currX, currY;
    private static double sprLocX1,sprLocX2,sprLocY1, sprLocY2; 
    private static float mouseForceX, mouseForceY;
    private static double onFlyX, onFlyY;
    private static float dampener;

    private static int focusedNodeIndex;
    private static int tempFocusedNodeIndex;
    private static boolean noNodePressFocus;
    private static boolean noNodeReleaseFocus;   

    private static final int w = 640;
    private static final int h = 480;

    public SpringSimulation()
    {
        setPreferredSize(new Dimension(w, h));
        setBounds(0, 0, w, h);       
        setBackground(new Color(123, 72, 142) );
        setVisible(true);
        setOpaque(true); 
        setLayout(null); 
        setFocusable(true);       
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);

        catched = null; 

        nodes = new ArrayList(); 
        springs = new ArrayList();        
        masses = new ArrayList();

        addGrid(8, 200, 20, 15F, 0.2F);
        addString(15, 400, 20, 10F, 1.0F);

        _delay = 1000 / _initFrameRate; 
        timer = new Timer(_delay, this); 
        timer.start();    
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g); 
        Graphics2D g2 = (Graphics2D)g; 

        _frameCount++;
        if (System.currentTimeMillis() > startTime + 1000) 
        {
            startTime = System.currentTimeMillis();
            _displayFrameRate = _frameCount; 
            _frameCount = 0; 
        }

        g.setColor(Color.white);
        g.drawString("X:  "+(int)onFlyX+"   Y:  "+(int)onFlyY, 10, 30); 
        g.drawString("FPS: " + _displayFrameRate, 10, 60); 

        for(int i = 0; i < springs.size(); i++)
        {
            ((Spring)springs.get(i)).draw(g); 
        }
        for(int i = 0; i < nodes.size(); i++)
        {
            ((Node)nodes.get(i)).draw(g);
        }

    }

    public void actionPerformed(ActionEvent e)
    {
        try
        {

        }
        finally
        {
			reCalculateAll();
			repaint();
        }
    }

    public void reCalculateAll()
    {

        for(int i = 0; i < nodes.size(); i++)
        {
            Node node = (Node)nodes.get(i);

            node.resetFocused();
            node.force.y = 1.0F;
            node.force.x = 0.0F;

            node.checkMouseFocus(node);

            if ( node.checkFocused() == true)
            {
                focusedNodeIndex = i;
            }
        }

        Iterator it = springs.iterator();
        do
        {
            if(!it.hasNext())
            {
                break;
            }

            Spring s = (Spring)it.next();
            s.reCalculate(); 

            if(Math.abs(s.force) > 100F) 
            {
                s.a.renderIt = true;
                s.b.renderIt = true;
                it.remove(); 
            }

        } while(true);

        for(int i = 0; i < nodes.size(); i++)
        {

            Node node = (Node)nodes.get(i);

            node.resetFocused();

            if((double)node.mass < 0.10000000000000001D) 
            {
                node.velocity.x += node.force.x;
                node.velocity.y += node.force.y; 
            } else
            {
                node.velocity.x += (node.force.x / node.mass) * 0.95F; 
                node.velocity.y += (node.force.y / node.mass) * 0.95F; 
            }

            node.move(); 
            node.checkMouseFocus(node);

            if ( node.checkFocused() == true)
            {
                focusedNodeIndex = i;
            }

        }

    }

    void catchNode(int x, int y)
    {
        if(catched != null)
        {
            return;
        }

        for(int i = 0; i < nodes.size(); i++)
        {

            if((float)x >= ((Node)nodes.get(i)).position.x - 10F && (float)y >= ((Node)nodes.get(i)).position.y 
            - 10F && (float)x <= ((Node)nodes.get(i)).position.x + 10F && (float)y <= ((Node)nodes.get(i)).position.y + 10F)
            {
                catched = (Node)nodes.get(i);
                return;
            }
        }

        catched = null;

    }

    public void calcMousePressed(MouseEvent event)
    { 

        noNodePressFocus = checkNoFocus(nodes);

        switch (event.getButton())
        {

            case MouseEvent.BUTTON1:           
            prevX = event.getX();
            prevY = event.getY();

            if(catched == null)
            {
                catchNode(event.getX(), event.getY());
            }
            break;

            case MouseEvent.BUTTON2:
            sprLocX1 = event.getX();
            sprLocY1 = event.getY();
            tempFocusedNodeIndex = focusedNodeIndex;
            break;

            case MouseEvent.BUTTON3:
            spawnNode(event.getX(), event.getY());
            break;

        }

    }

    public void calcMouseDragged(MouseEvent event)
    { 

        if(catched != null)
        {

            catched.position.x = event.getX();
            catched.position.y = event.getY();

        }

        onFlyX = event.getX();
        onFlyY = event.getY();

    }

    public void calcMouseReleased(MouseEvent event)
    { 

        noNodeReleaseFocus = checkNoFocus(nodes);

        currX = event.getX();
        currY = event.getY();

        mouseForceX = (float)currX - (float)prevX;
        mouseForceY = (float)currY - (float)prevY;

        dampener = 0.5F;

        switch (event.getButton())
        {
            case MouseEvent.BUTTON1:

            if(catched == null)
            {
                return;

            } else
            {

                catched.velocity.x = mouseForceX*dampener;
                catched.velocity.y = mouseForceY*dampener;
                catched.force.y = 0.0F;
                catched.force.x = 9.4F;
                catched = null;               
            }              
            break;

            case MouseEvent.BUTTON2:

            sprLocX2 = event.getX();
            sprLocY2 = event.getY();     

            if ( (noNodePressFocus == false) && (noNodeReleaseFocus == true) )
            {
                spawnSpring( (Node)nodes.get(focusedNodeIndex) , sprLocX2, sprLocY2);
            }
            else if ( (noNodePressFocus == false) && (noNodeReleaseFocus == false) )
            {
                spawnSpring( (Node)nodes.get(tempFocusedNodeIndex) , (Node)nodes.get(focusedNodeIndex) );
            }
            else
            {
                spawnSpring(sprLocX1, sprLocX2, sprLocY1, sprLocY2);
            }

            break;
        }
    }	  
    public void calcMouseMoved(MouseEvent event)
    {
        onFlyX = event.getX();
        onFlyY = event.getY();
    }	

    public void calcKeyPressed(KeyEvent event)
    {
        switch(event.getKeyCode())
        {  
            case KeyEvent.VK_A:

            if (checkNoFocus(getNodeList()) == true)
            {
                return;
            }
            else
            {
                ((Node)nodes.get(focusedNodeIndex)).toggleAnchor();
            }

            break;

        }
    }

    public void calcKeyReleased(KeyEvent event)
    {
        switch(event.getKeyCode())
        {              
        }
    }

    public void calcKeyTyped(KeyEvent event)
    {
        switch(event.getKeyCode())
        {             
        }
    }

    class Node 
    {

        Vector2D velocity;
        Vector2D position;
        Vector2D force;
        Node catched;
        float mass;
        boolean anchor;
        boolean renderIt;
        boolean focused;
        boolean recentlyAnchored;

        boolean checkFocused()
        {
            return focused;
        }

        void resetFocused()
        {
            focused = false;
        }

        boolean checkRecentlyAnchored()
        {
            return recentlyAnchored;
        }

        void toggleAnchor()
        {
            if (anchor != true)
            {
                anchor = true;
            }
            else
            {
                anchor = false;
                recentlyAnchored = true;
            }
        }

        void draw(Graphics g)
        {	
            if(!renderIt) 
            {
                return;
            } 

            else
            {
                if (focused == true)
                {
                    g.setColor(Color.red);
                }
                else
                {
                    g.setColor(Color.white);    
                }

                if (catched != null)
                {
                    g.fillOval((int)onFlyX - 2, (int)onFlyY - 1 ,4, 4);
                }
                else
                {

                    g.fillOval((int)position.x - 2, (int)position.y - 1, 4, 4);
                }

                return;
            }

        }

        void addForceFrom(float force, Vector2D from)
        {  

            float distance = position.distance(from); 

            float dx = from.x - position.x; 
            float dy = from.y - position.y; 

            this.force.x += (-dx / distance) * force; 
            this.force.y += (-dy / distance) * force; 

        }

        float distanceNodes(Node a, Node b) //Distance measuring
        {
            return (float)Math.sqrt((a.position.x - b.position.x) * (a.position.x - b.position.x) 
                + (a.position.y - b.position.y) * (a.position.y - b.position.y));    
        }

        void checkMouseFocus(Node a)
        {

            if( (a.position.x <= onFlyX+4) && (a.position.x >= onFlyX-4) )
            {
                if( (a.position.y <= onFlyY+4) && (a.position.y >= onFlyY-4) ) 
                {
                    a.focused = true;  
                }
            }
            else
            {
                a.focused = false;
            }
        }

        void move()
        {

            if(anchor || this == catched) 
            {
                return;
            }

            checkRecentlyAnchored();

            if(recentlyAnchored == true)
            {     
                velocity.x *= 0.25F; 
                velocity.y *= 0.25F; 

                position.x += velocity.x; 
                position.y += velocity.y;

            }
            else
            {         
                velocity.x *= 0.95F; 
                velocity.y *= 0.95F; 

                position.x += velocity.x; 
                position.y += velocity.y;
            }

            if(position.x < 4.0F) 
            {
                position.x = 4.0F;
                velocity.x *= -0.6F;
            }

            if(position.y < 2.0F) 
            {
                position.y = 2.0F;
                velocity.y *= -0.6F;
            }

            if(position.x > SpringSimulation.w - 2.0F)
            {
                position.x = SpringSimulation.w - 2.0F;
                velocity.x *= -0.6F;
            }

            if(position.y > SpringSimulation.h - 10.0F)
            {
                position.y = SpringSimulation.h - 10.0F;
                velocity.y *= -0.9F;
            }

            recentlyAnchored = false;
        }

        Node(int x, int y, float mass)
        {		    
            velocity = new Vector2D();
            position = new Vector2D();
            force = new Vector2D();

            position.x = x;
            position.y = y;
            anchor = false;
            this.mass = mass;
            renderIt = true;
            recentlyAnchored = false;
            focused = false;
        }

        Node(float x, float y, float mass)
        {		    
            velocity = new Vector2D();
            position = new Vector2D();
            force = new Vector2D();

            position.x = x;
            position.y = y;
            anchor = false;
            this.mass = mass;
            renderIt = true;
            recentlyAnchored = false;
            focused = false;
        }

    }

    class Vector2D
    {
        float x;
        float y;    

        Vector2D()
        {
            x = 0.0F;
            y = 0.0F;		    		    
        }

        float distance(Vector2D other) 
        {
            return (float)Math.sqrt((x - other.x) * (x - other.x) 
                + (y - other.y) * (y - other.y));    
        }

    }

    class Spring
    {
        float sprConst;
        float size;
        float force;

        Node a;
        Node b;

        void draw(Graphics g)
        {  
            float r = Math.abs(force) / 100F;

            int red = (int)(255F * r); 
            red = red <= 255 ? red : 255;

            g.setColor(Color.decode((new StringBuilder()).append("0x")
                    .append(Integer.toHexString(red))
                    .append(Integer.toHexString(255 - (red * 8) / 10))
                    .append("00").toString()));

            g.drawLine((int)a.position.x, (int)a.position.y, (int)b.position.x, (int)b.position.y); 
        }

        void reCalculate()
        {		
            force = -sprConst * (a.position.distance(b.position) - size);
            a.addForceFrom(force, b.position);
            b.addForceFrom(force, a.position); 

        }

        Spring(float sprConst, Node a, Node b)
        {
            this.sprConst = sprConst; 
            this.a = a;
            this.b = b;		   
            size = (a.position).distance(b.position);		    		    
        }

    }	

    Node addNode(int x, int y, float mass) 
    {
        Node ret = new Node(x, y, mass);
        nodes.add(ret);
        return ret;
    }

    Node addNode(float x, float y, float mass) 
    {
        Node ret = new Node(x, y, mass);
        nodes.add(ret);
        return ret;
    }

    void addGrid(int elements, int x, int y, float elemSpace, float sprConst) 
    {

        Node grid[][] = new Node[elements][elements]; 

        for(int i = 0; i < elements; i++) 
        {
            for(int j = 0; j < elements; j++)
            {
                addNode((float)x + (float)i * elemSpace, (float)y + (float)j * elemSpace, 1.0F);

                grid[i][j] = (Node)nodes.get(nodes.size() - 1);

                if (j == 0)
                {
                    grid[i][j].anchor = true; 
                }

            }

        }

        for(int i = 1; i < elements - 1; i++)
        {
            for(int j = 1; j < elements - 1; j++)
            {
                addSpring(sprConst / (float)j, grid[i][j], grid[i - 1][j]);
                addSpring(sprConst / (float)j, grid[i][j], grid[i + 1][j]);
                addSpring(sprConst / (float)j, grid[i][j], grid[i][j - 1]);
                addSpring(sprConst / (float)j, grid[i][j], grid[i][j + 1]);
            }

        }

        for(int i = 0; i < elements - 1; i++)
        {
            sprConst = sprConst / elements;
        }

    }

    void addString(int elements, int x, int y, float elemSpace, float sprConst) 
    {

        Node string[] = new Node[elements];

        for(int i = 0; i < elements; i++) 
        {

            addNode((float)x , (float)y + elemSpace * i , sprConst);

            string[i] = (Node)nodes.get(nodes.size() - 1);

            if (i == 0)
            {
                string[i].anchor = true; 
            }

        }

        for(int i = 1; i < elements - 1; i++)
        {

            addSpring(sprConst , string[i], string[i-1]);
        }

    }

    void addSpring(float sprConst, Node a, Node b) 
    {

        springs.add(new Spring(sprConst, a, b)); 

    }

    public void spawnNode(float coordX, float coordY)
    {
        Node ret = new Node( (float)coordX , (float)coordY, 1.0F);
        nodes.add(ret);

    }

    public void spawnSpring(Node a, Node b)
    {
        springs.add(new Spring(0.3F, a, b));
    }

    public void spawnSpring(Node a, double coordX2, double coordY2)
    {
        Node custom1 = new Node( (float) coordX2, (float) coordY2, 1.0F);
        nodes.add(custom1);

        springs.add(new Spring(0.3F, a, custom1));
    }

    public void spawnSpring(double coordX1, double coordX2, double coordY1, double coordY2)
    {
        Node custom1 = new Node( (float) coordX1, (float) coordY1, 1.0F);
        Node custom2 = new Node( (float) coordX2, (float) coordY2, 1.0F);
        custom1.anchor = true;

        nodes.add(custom1);
        nodes.add(custom2);

        springs.add(new Spring(0.3F, custom1, custom2));

    }

    public ArrayList getNodeList()
    {
        return nodes;
    }

    boolean checkNoFocus( ArrayList nodes )
    {
        for(int i = 0; i < nodes.size(); i++)
        {
            Node check = (Node)nodes.get(i);

            if( check.focused == true)
            {
                return false;
            }
        }
        return true;
    }

    public void mousePressed(MouseEvent event)
    {
        calcMousePressed(event);
    }

    public void mouseReleased(MouseEvent event)
    {
        calcMouseReleased(event); 
    }

    public void mouseDragged(MouseEvent event)
    {
        calcMouseDragged(event); 
    }

    public void mouseMoved(MouseEvent event)
    {
        calcMouseMoved(event); 
    }

    public void mouseExited (MouseEvent event){} 

    public void mouseEntered (MouseEvent event){}

    public void mouseClicked (MouseEvent event){}

    public void keyPressed (KeyEvent event)
    {
        calcKeyPressed(event);
    }

    public void keyReleased (KeyEvent event)
    {
        calcKeyReleased(event);
    }

    public void keyTyped (KeyEvent event)
    {
        calcKeyTyped(event);
    }

}
