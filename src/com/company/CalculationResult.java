package com.company;

/**
 * Created by andrey on 3/9/17.
 */
public class CalculationResult {
    public double falseAlarm;
    public double sumOfMistakes;
    public int intersectionPointIndex;

    public CalculationResult(double falseAlarm, double sumOfMistakes, double probabilityOfMissingDetection,int
            intersectionPointIndex) {
        this.falseAlarm = falseAlarm;
        this.sumOfMistakes = sumOfMistakes;
        this.probabilityOfMissingDetection = probabilityOfMissingDetection;
        this.intersectionPointIndex = intersectionPointIndex;
    }

    public double probabilityOfMissingDetection;
}
