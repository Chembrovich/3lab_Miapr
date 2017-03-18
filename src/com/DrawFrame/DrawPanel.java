package com.DrawFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Created by evgeny on 1/29/17.
 */
class DrawPanel extends JPanel {

    public void setDrawableObj(Drawable drawableObj) {
        this.drawableObj = drawableObj;
    }

    private Drawable drawableObj;

    private void printShapes(Graphics g)
    {
        if(drawableObj!=null)
            drawableObj.draw((g));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        printShapes(g);
    }
}
