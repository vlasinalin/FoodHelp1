/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import dataModel.Dish;
import java.util.Random;

/**
 *
 * @author Alinv
 */
public class HillClimbingAlgorithmService {

    private static final int FOOD_MAX_QUANTITY = 250;
    private static final int STEP_QUANTITY = 5;
    
    private Dish[] currentFinas;
    private Dish[] bestFinas;
    private Random rand;

    public void computeQuantities(Dish[] currentFinas, Dish[] bestFinas, int hillClimbTrials) {
        this.currentFinas = currentFinas;
        this.bestFinas = bestFinas;
        this.rand = new Random();
        hillClimb(hillClimbTrials);
        for (int i=0; i<bestFinas.length; i++) 
            bestFinas[i].quantity = this.bestFinas[i].quantity;
    }

    private void hillClimb(int trials) {
        int bestSolErr, solErr, curMinErr, curErrUp, curErrDown;
        bestSolErr = Integer.MAX_VALUE;
        solErr = Integer.MAX_VALUE;

        int foodsSize = currentFinas.length;
        
        while (trials > 0) {
            curMinErr = Integer.MAX_VALUE;
            
            do {
                solErr = curMinErr;     
                curMinErr = Integer.MAX_VALUE;
                int bestIndex = -1;
                int bestStep = 0;
                for (int i=0; i<foodsSize; i++) {
                    curErrUp = Integer.MAX_VALUE;
                    curErrDown = Integer.MAX_VALUE;
                    int stepVal = Math.min(STEP_QUANTITY, currentFinas[i].quantity);
                    //stepVal = Math.max(1, stepVal);
                    if (currentFinas[i].quantity <= (FOOD_MAX_QUANTITY - STEP_QUANTITY)) {
                        currentFinas[i].quantity += stepVal;
                        curErrUp = getErrorValue(Dish.computeNutrTotal(currentFinas));
                        currentFinas[i].quantity -= stepVal;
                    }
                    if (currentFinas[i].quantity > 0) {                        
                        currentFinas[i].quantity -= stepVal;
                        curErrDown = getErrorValue(Dish.computeNutrTotal(currentFinas));
                        currentFinas[i].quantity += stepVal;
                    }
                    if (curMinErr > curErrUp) {
                        curMinErr = curErrUp;
                        bestIndex = i;
                        bestStep = stepVal;
                    }
                    if (curMinErr > curErrDown) {
                        curMinErr = curErrDown;
                        bestIndex = i;
                        bestStep = -stepVal;
                    }              
                }
                currentFinas[bestIndex].quantity += bestStep;
            } while(solErr > curMinErr);
            
            trials--;
            if (bestSolErr > solErr) {
                bestSolErr = solErr;
                saveBestFoods();
            }
            if (trials > 0) {
                for (int i=0; i<foodsSize; i++) {
                    currentFinas[i].quantity = rand.nextInt(125);
                }
            }
        }
    }

    private void saveBestFoods() {
        for(int i=0; i<currentFinas.length; i++) {
            bestFinas[i].quantity = currentFinas[i].quantity;
        }
    }

    static int getErrorValue(double[] nutrSum) {
        int err = 0;
        for (int jj=1; jj<nutrSum.length-4; jj++) {
            double dif = Math.abs(110 - nutrSum[jj]);
            //if (logScale) {
                dif = dif * (1 + dif);
            //}              
            err += dif;                         
        }
        for (int jj=nutrSum.length-4; jj<nutrSum.length; jj++) {
            double dif = nutrSum[jj] - 100;
            if (dif > 0) {
                //if (logScale) {
                    dif = dif * (1 + dif);
                //}              
                err += dif;
            }
        }
        return err;
    }
 
}
