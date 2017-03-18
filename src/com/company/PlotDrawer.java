package com.company;

import com.sun.javafx.binding.StringFormatter;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by evgeny on 3/7/17.
 */
public class PlotDrawer {

    private boolean drawGreed = true;
    private DomainDouble domainX;
    private DomainDouble domainY;
    private ArrayList<Point[]> plots;
    private double offsetX = 0;
    private double offsetY;

    private ArrayList<Point> verticalLines = new ArrayList<>();


    private DomainDouble calcDomainY (Point points  []){
        double min = 0;
        double max = 0;
        for (Point p : points){
            if (p.y < min) min = p.y;
            if (p.y > max) max = p.y;
        }
        return  new DomainDouble(min,max);
    }

    private DomainDouble calcDomainX(Point points[]){
        double min = points[0].getX();
        double max = points[0].getX();
        for (Point p : points){
            if (p.x < min) min = p.x;
            if (p.x > max) max = p.x;
        }
        return  new DomainDouble(min,max);
    }

    private void expandGlobalDomain(Point [] points){
        DomainDouble pointsDomainX = calcDomainX(points);
        DomainDouble pointsDomainY = calcDomainY(points);
        this.domainX = this.domainX.getPairDomain(pointsDomainX);
        this.domainY = this.domainY.getPairDomain(pointsDomainY);
    }

    public void addPlot(Point points[]){
        plots.add(points);
        expandGlobalDomain(points);
    }

    public PlotDrawer(){
        plots = new ArrayList<Point[]>();
        domainX = new DomainDouble();
        domainY = new DomainDouble();
    }

    private int calcX(double x, double offset, double scaleX){
        return (int)(x*scaleX + scaleX*offset);
    }
    private Double calcXDouble(double x, double offset, double scaleX){
        return (x*scaleX + scaleX*offset);
    }

    private int calcY(double y, double offset, double scaleY){
        y *=-1;
        y *=scaleY;
        y += offset;
        return (int)y;
    }

    private void drawPlotCoordinateLines(Graphics g, double count , double scaleX, double scaleY, double offsetX){

        double step = (domainX.max - domainX.min) / count;

        double x = domainX.min;

        for (int i=0; i<count; i++){
            int cx = calcX(x,offsetX,scaleX);
            g.drawLine(cx,0,cx,(int)g.getClipBounds().getHeight());
            String str = String.format("%.6f",(x));
            g.drawString(str,cx,(int)g.getClipBounds().getHeight()-10);
            x += step;
        }

        double y = domainY.min;

         step = (domainY.max - domainY.min) / count;
        for (int i=0; i<count; i++){
            //int cx = (x,offsetX,scaleX);
            int xy = calcY(y,0,scaleY);

            g.drawLine(3,xy,3,(int)g.getClipBounds().getWidth());
            String str = String.format("%.6f",(y));
            g.drawString(str,xy,(int)g.getClipBounds().getWidth());
            y += step;
        }





        //;
        //double end = 1000;



      /*  int height = (int)g.getClipBounds().getHeight();
        int width = (int)g.getClipBounds().getWidth();
        g.setColor(Color.cyan);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        g.drawLine(calcX(0,offsetX,scaleX),0,calcX(0,offsetX,scaleX),height);
        g.drawLine(3,height-3,width,height-3);
        g2.setStroke(new BasicStroke(1));
        g.setColor(Color.black);

        double y = 0;

        int step = (int)(height/count);
        int i = 0;
        //vertical lines
        while (i<=count){
            int reflectedY = calcY(y,height,1);
            g.drawLine(3,reflectedY,width,reflectedY);
            g.drawString(String.format("%.6f",y/scaleY),3,reflectedY);
            y+=step;
            i++;
        }

        //horisontal lines
        i=0;

        double sizeX = domainX.max - domainX.min;
        step = (int)(sizeX / count);
        double x = domainX.min;
        while (x<=domainX.max){
            g.drawLine(calcX(x,0,scaleX),3,calcX(x,0,scaleX),height);
            g.drawString(String.format("%.6f",calcXDouble(x,offsetX,scaleX)),calcX(x,0,scaleX),height-5);
            x+=step;
            i++;
        }
        //System.out.println(i);
        g.drawString(String.format("%.6f",calcXDouble(0,0,scaleX)),calcX(0,offsetX,scaleX),height-5);
*/
    }

    public void drawVerticalLine(Point point){
        verticalLines.add(point);
    }

    public void drawPlot(Graphics g){

        double width = g.getClipBounds().getWidth();
        double height = g.getClipBounds().getHeight();

        double sizeX = domainX.max - domainX.min;
        double sizeY = domainY.max - domainY.min;
        double scaleX = width / sizeX;
        double scaleY = height / sizeY;
        offsetX = -domainX.min;
        if(drawGreed)
            drawPlotCoordinateLines(g,5,scaleX,scaleY,offsetX);

        //int i = 300;
        for(Point [] result : plots) {
            for (Point p : result) {

               g.drawOval(calcX(p.getX(),offsetX,scaleX), calcY(p.y,height,scaleY), 3, 3);

            }
            g.setColor(Color.BLUE);
        }
        g.setColor(Color.black);

        g.setColor(Color.GREEN);
        for (Point point : verticalLines){
            g.drawLine(calcX(point.getX(),offsetX,scaleX), calcY(0,height,scaleY),
                    calcX(point.getX(),offsetX,scaleX), calcY(height,height,scaleY));
        }
        g.setColor(Color.black);
    }
}
