package com.company;


import com.DrawFrame.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Main {

    private static Point gaussResult [][] = null;
    private static PlotDrawer plotDrawer = new PlotDrawer();

    private static CalculationResult calculationResult = null;

    private static void drawResult (Graphics g){
        int space = 20;
        int beginX = 100;
        int beginY = 20;
        g.drawString("Probability Of MissingDetection: " + String.format("%.4f",calculationResult.probabilityOfMissingDetection)
        ,beginX,beginY+space*1);
        g.drawString("Probability Of false alarm: " + String.format("%.4f",calculationResult.falseAlarm)
                ,beginX,beginY+space*2);
        g.drawString("Total error: " + String.format("%.4f",calculationResult.sumOfMistakes)
                ,beginX,beginY+space*3);

    }

    static Drawable drawable = new Drawable() {
        @Override
        public void draw(Graphics g) {
            plotDrawer.drawPlot(g);
            drawResult(g);
        }
    };

    public static void main(String[] args) {
        MainForm mainForm = new MainForm("Lab 3");
        mainForm.setDrawable(drawable);


        ProbabilisticApproach probabilisticApproach = new ProbabilisticApproach(0.7,0.3,0.5);
        calculationResult =  probabilisticApproach.doCalculations();
        gaussResult = probabilisticApproach.getPlot();
        for (Point [] plot : gaussResult){
            plotDrawer.addPlot(plot);
        }
        plotDrawer.drawVerticalLine(gaussResult[0][calculationResult.intersectionPointIndex]);
        mainForm.setVisible(true);
        mainForm.repaint();
    }
}
