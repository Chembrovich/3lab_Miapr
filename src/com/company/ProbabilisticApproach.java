package com.company;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

// nextInt is normally exclusive of the top value,
// so add 1 to make it inclusive


/**
 * Created by evgeny on 3/3/17.
 */
public class ProbabilisticApproach {

    private int firstRandomValues [];
    private int secondRandomValues [];

    private double apriorityProbability [] = new double[2];

    final private int countOfImages = 10000;
    final private Domain domains [] =  {new Domain(-300,500), new Domain(300,800)};

    private double plotStep;
    //private int plotWidth;

    private double expectedValues[];
    private double meanSquareDeviation [];
    public Domain[] getDomain(){
        return domains;
    }

    private Point resultGaussDensityArray [][];

    public ProbabilisticApproach (double firstApriorityProbability,double secondAprioryProbability,double step){

        globalDomain = getGlobalDomain();
        this.apriorityProbability[0] = firstApriorityProbability;
        this.apriorityProbability[1] = secondAprioryProbability;
        firstRandomValues = new int [countOfImages];
        secondRandomValues = new int [countOfImages];

        for (int i=0; i<countOfImages; i++){
            firstRandomValues [i] = ThreadLocalRandom.current().nextInt(domains[0].min, domains[0].max);
            secondRandomValues[i] = ThreadLocalRandom.current().nextInt(domains[1].min, domains[1].max);
        }

        this.plotStep = step;
        //this.plotWidth = globalDomain.max - globalDomain.min;
    }

    private double calсExpectedValue(int [] numbers){
        double sum = 0;
        for(int i: numbers) {
            sum += i;
        }
        int size = numbers.length;
        return sum/size;
    }

    private double calcMeanSquareDeviation (int numbers[], double expectedValue){
        double sigma = 0;
        for (int i : numbers){
            sigma += Math.pow(i-expectedValue, 2);
        }
        return  Math.sqrt(sigma/numbers.length);
    }

    private double gaussianDistributionDensity(double x, double expectedValue, double meanSquareDeviation){
        double pow = Math.pow((x - expectedValue) / meanSquareDeviation, 2);
        double result = Math.exp(pow/(-2)) /
                (meanSquareDeviation * Math.sqrt(2 * Math.PI));
        return  result;
    }

    int intersectionPointIndex = 0;

    public Domain getGlobalDomain(){
        int min = domains[0].min;
        int max = domains[0].max;
        if (domains[0].min < domains[1].min)
            min = domains[0].min;
        else
            min = domains[1].min;

        if (domains[0].max > domains[1].max)
            max = domains[0].max;
        else
            max = domains[1].max;

        return new Domain (min , max);
    }

    private double calcFalseAlarmProbability(int intersectionPointIndex ){
        double sum1 = 0;
        double sum2 = 0;
        for (int i = 0; i<intersectionPointIndex; i++){
            sum1 += resultGaussDensityArray[1][i].getY();
        }

        for (int i = 0; i<intersectionPointIndex; i++){
            sum2 += resultGaussDensityArray[0][i].getY();
        }

        return  sum1 / sum2;
    }

    private double calcProbabilityOfMissingDetection(int intersectionPointIndex){

        double sum1 = 0;
        double sum2 = 0;
        for (int i = intersectionPointIndex; i<resultGaussDensityArray[0].length; i++){
            sum1 += resultGaussDensityArray[0][i].getY();
        }

        for (int i = intersectionPointIndex; i<resultGaussDensityArray[1].length; i++){
            sum2 += resultGaussDensityArray[1][i].getY();
        }

        return sum1 / sum2;
    }

    public int getIntersectionPointIndex(int plotFist, int plotSecond){

        double min = resultGaussDensityArray[plotFist][0].getY() - resultGaussDensityArray[plotSecond][0].getY();
        if(min<0)min *= -1;
        int minPoint = 0;
        double  dist = 0;
        for (int i=0; i<resultGaussDensityArray[plotFist].length;i++){
            dist =  resultGaussDensityArray[plotFist][i].getY() - resultGaussDensityArray[plotSecond][i].getY();
            if (dist<0)dist *=-1;
            double x = resultGaussDensityArray[plotFist][i].getX();
            if (dist<min) {
                min = dist;
                minPoint = i;
            }
        }
        return minPoint;
    }

    private Point [][] calcGaussPlotArray(double step){
        Point points[][] = new Point[2][];
        for(int i=0; i<2; i++)
            points[i]=getGaussPlotArray(i,step);
        return points;
    }

    private Domain globalDomain = null;
    private Point [] getGaussPlotArray(int number, double step){
        int count = (int)((globalDomain.max-globalDomain.min)/step);
        Point [] points = new Point[count];
        double x = globalDomain.min;
        int i = 0;
        while (i<count){
            double result = gaussianDistributionDensity(x,
                    expectedValues[number],
                    meanSquareDeviation[number]);
            result *= apriorityProbability[number];
            points[i] = new Point(x,result);
            x+=step;
            i+=1;
        }
        return  points;
    }

    public Point [][] getPlot(){
        return resultGaussDensityArray;
    }

    public CalculationResult  doCalculations (){
        this.expectedValues = new double[2];
        this.meanSquareDeviation = new double[2];
        expectedValues[0] = calсExpectedValue(firstRandomValues);
        expectedValues[1] = calсExpectedValue(secondRandomValues);
        meanSquareDeviation[0] = calcMeanSquareDeviation(firstRandomValues, expectedValues[0]);
        meanSquareDeviation[1] = calcMeanSquareDeviation(secondRandomValues, expectedValues[1]);

        resultGaussDensityArray = calcGaussPlotArray(plotStep);
        int intersectionPointIndex = getIntersectionPointIndex(0,1);
        double falseAlarm = calcFalseAlarmProbability(intersectionPointIndex);
        double probabilityOfMissingDetection =
                calcProbabilityOfMissingDetection(intersectionPointIndex);

        double sumOfMistakes =falseAlarm + probabilityOfMissingDetection;

        return new CalculationResult(falseAlarm,sumOfMistakes,probabilityOfMissingDetection,intersectionPointIndex);

    }
}
