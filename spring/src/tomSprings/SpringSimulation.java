package tomSprings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.Timer;

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
    public static double onFlyX, onFlyY;
    private static float dampener;

    private static int focusedNodeIndex;
    private static int tempFocusedNodeIndex;
    private static boolean noNodePressFocus;
    private static boolean noNodeReleaseFocus;   

    public static final int w = 640;
    public static final int h = 480;

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

