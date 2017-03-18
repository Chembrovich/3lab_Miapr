package com.DrawFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.Panel;
import java.awt.event.*;

/**
 * Created by evgeny on 1/29/17.
 */
public class MainForm extends JFrame {

    private JPanel rootPanel;
    private JTextField numberOfImages;
    private JTextField countOfClasses;
    private JButton start;
    private JPanel menuPanel;
    private JPanel graphWrapper;

    private Drawable drawable;
    private DrawPanel panel;

    public void addMouseListener(MouseListener listener)
    {
       graphWrapper.addMouseListener(listener);
    }

    public MainForm (String name)
    {
        super(name);
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        InitPaintPanel();
        this.setBounds(100,100,500,300);

    }

    public void onStartButton(ActionListener listener)
    {
        start.addActionListener(listener);
    }

    public String getCountOfClasses()
    {
        return countOfClasses.getText();
    }

    public String getCountOfImages()
    {
        return numberOfImages.getText();
    }


    public void setDrawable(Drawable drawable){
        this.drawable = drawable;
        panel.setDrawableObj(drawable);
    }

    public void addMouseWheelListenerToDrawPanel(MouseMotionListener l){
       // panel.addMouseWheelListener(l);
        panel.addMouseMotionListener(l);
    }


    void InitPaintPanel() {
        //paintPanel = new DrawPanel();
        panel = new DrawPanel();
        panel.setBackground(Color.white);
        panel.setBackground(Color.white);
        graphWrapper.setLayout(new BorderLayout());
        graphWrapper.add(panel,BorderLayout.CENTER);
    }

}
