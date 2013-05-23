package tomSprings;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
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