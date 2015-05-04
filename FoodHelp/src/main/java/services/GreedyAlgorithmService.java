/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import dataModel.Dish;
import dataModel.FoodRecommendation;
import ejb.entityBeans.FoodInfo;
import ejb.entityBeans.Person;
import ejb.sessionBeans.FoodInfoFacade;
import java.util.*;
import services.contracts.FoodRecommendationsService;

public class GreedyAlgorithmService implements FoodRecommendationsService {   
    
    private static final int HILL_CLIMB_ITERATIONS = 900;
    private static final int RECIPE_FOODS_NR = 24;
    
    private HillClimbingAlgorithmService hcas;
    private FoodInfoFacade foodInfoFacade;  
    private FoodInfo foodInfoRda;
    private int selectedFoodsNr = RECIPE_FOODS_NR;
    private Dish[] currentFoodsArr;
    private Dish[] tempFoodsArr;
    private Dish[] bestFoodsArr;
    private Dish[] allFinas;
    private Random rand;   
    
    private int solutionMinNutrIndex = -1;
    private int solutionMaxNutrIndex = -1;
    
    private List<Integer> costs = new ArrayList<Integer>();
    
    private void initService(FoodInfoFacade fiFacade, Person person) {
        hcas = new HillClimbingAlgorithmService();
        foodInfoFacade = fiFacade;
        foodInfoRda = person.getRdaFoodInfo();
        rand = new Random();
        initAllFoods(foodInfoFacade.findAllExcl(person.getExclFoodGroups()));
        currentFoodsArr = new Dish[selectedFoodsNr];
        tempFoodsArr = new Dish[selectedFoodsNr];
        bestFoodsArr = new Dish[selectedFoodsNr];
        
        for (int i=0; i<selectedFoodsNr; i++) {
            tempFoodsArr[i] = new Dish();
            bestFoodsArr[i] = new Dish();
            currentFoodsArr[i] = new Dish();
        }
    }

    @Override
    public FoodRecommendation generateRecommendation(FoodInfoFacade fiFacade, Person person) {
        initService(fiFacade, person);
        
        Date before = new Date();
        
        runGreedyAlgorithm();      
        
        System.out.println("\n BestFoodsArr Error = " + HillClimbingAlgorithmService.getErrorValue(Dish.computeNutrTotal(bestFoodsArr)));
        
        List<Dish> filteredFoods = Dish.filterFoodsByQuantity(Arrays.asList(bestFoodsArr), 5);
        FoodRecommendation.fillWater(filteredFoods, Dish.from(foodInfoFacade.getWater(), foodInfoRda));
        FoodRecommendation result = new FoodRecommendation();
        result.setFoodsList(filteredFoods);      
        result.setNutrTotal(Dish.computeNutrTotal(filteredFoods));
        result.setPersonId(person.getUserAccountId());

        Date after = new Date();
        long milisec = after.getTime() - before.getTime();
        System.out.println("\n##########\n Exec Time Greedy Algorithm = " + milisec + "(ms)");  
        System.out.println("\n Best Solution Error = " + HillClimbingAlgorithmService.getErrorValue(result.getNutrTotal()));
        return result;
    }

    private void initAllFoods(List<FoodInfo> allFoods) {
        int foodsSize = allFoods.size();
        allFinas = new Dish[foodsSize];
        for (int i=0; i<foodsSize; i++) {
            allFinas[i] = Dish.from(allFoods.get(i), foodInfoRda);
        }
    }

    private void runGreedyAlgorithm() {
        int minSolErr = Integer.MAX_VALUE;
        int solErr;
        int trials = HILL_CLIMB_ITERATIONS;
        
        selectedRandomFoods();
        copyDishes(currentFoodsArr, bestFoodsArr);
        
        while (trials > 0) {   
            trials--;
            
            copyDishes(currentFoodsArr, tempFoodsArr);
            hcas.computeQuantities(tempFoodsArr, currentFoodsArr, 1);
            double[] nutrArray = Dish.computeNutrTotal(currentFoodsArr);
            solErr = HillClimbingAlgorithmService.getErrorValue(nutrArray);

            if (minSolErr > solErr) {
                minSolErr = solErr;
                copyDishes(currentFoodsArr, bestFoodsArr);
                System.out.println("\n GREEDY Better sol error = " + solErr);
            }
            
            if (trials % 60 == 0) {
                costs.add(minSolErr);
            }
            
            adjustSelectedFoods();
        }      
        
        
        String output = new String();
        for (int x : costs) {
            output += x + "\t";
        }
        System.out.println(output);
    } 
    
    private void copyDishes(Dish[] from, Dish[] to) {
        for (int i=0; i<selectedFoodsNr; i++) {
            to[i].setFoodgroupDesc(from[i].getFoodgroupDesc());
            to[i].setLongDesc(from[i].getLongDesc());
            to[i].setNDBNo(from[i].getNDBNo());
            System.arraycopy(from[i].nutrients, 0, to[i].nutrients, 0, to[i].nutrients.length);
            to[i].quantity = from[i].quantity;
        }
    }
    
    private void adjustSelectedFoods() {
        solutionMinNutrIndex = getSolutionMinNutrIndex();
        solutionMaxNutrIndex = getSolutionMaxNutrIndex();
        int worstFoodIndex = getWorstFoodIndex();
        Dish worstFood = currentFoodsArr[worstFoodIndex];

        int zeroCount = 0;
        for (int i=0; i<selectedFoodsNr; i++) {
            if (currentFoodsArr[i].quantity == 0) {
                zeroCount++;
                currentFoodsArr[i] = selectCandidateFood(worstFood); 
                currentFoodsArr[i].quantity = 50 + 10 * rand.nextInt(15);
            }
        }
        //System.out.println("\n HC zeroCount = " + zeroCount);
        if (zeroCount > 5) {
            return;
        }          

        currentFoodsArr[worstFoodIndex] = selectCandidateFood(worstFood);
        currentFoodsArr[worstFoodIndex].quantity = 50 + 10 * rand.nextInt(15);
    }
    
    private int getWorstFoodIndex() {      
        int worstFoodIndex = -1;
        double worstFoodMaxNutrVal = 0.0;
        for (int i=0; i<selectedFoodsNr; i++) {
            if (getMaxNutrIndex(currentFoodsArr[i].nutrients) == solutionMaxNutrIndex) {
                if (currentFoodsArr[i].nutrients[solutionMaxNutrIndex] > worstFoodMaxNutrVal) {
                    worstFoodMaxNutrVal = currentFoodsArr[i].nutrients[solutionMaxNutrIndex];
                    worstFoodIndex = i;
                }
            }
        }
        if (worstFoodIndex < 0) {
            worstFoodMaxNutrVal = 0.0;
            for (int i=0; i<selectedFoodsNr; i++) {
                if (currentFoodsArr[i].nutrients[solutionMaxNutrIndex] > worstFoodMaxNutrVal) {
                    worstFoodMaxNutrVal = currentFoodsArr[i].nutrients[solutionMaxNutrIndex];
                    worstFoodIndex = i;
                }
            }
        }
        return worstFoodIndex;
    }
    
    private void selectedRandomFoods() {
        int foodsSize =  allFinas.length;
        for (int i=0; i<selectedFoodsNr; i++) {
            Dish fina = allFinas[rand.nextInt(foodsSize)];
            currentFoodsArr[i] = fina;
        }
    }
    
    private Dish selectCandidateFood(Dish worstFina) {
        int foodsSize =  allFinas.length;
        int trials = 10000;
        while(trials > 0) {
            Dish fina = allFinas[rand.nextInt(foodsSize)];
            if (fina.nutrients[solutionMinNutrIndex] >= worstFina.nutrients[solutionMinNutrIndex] 
                && fina.nutrients[solutionMaxNutrIndex] < worstFina.nutrients[solutionMaxNutrIndex]) {
                return fina;
            }
            trials--;
        }
        Dish fina = allFinas[rand.nextInt(foodsSize)];
        return fina;
    }
    
    private int getSolutionMaxNutrIndex() {
        double[] nutrSum = Dish.computeNutrTotal(currentFoodsArr);
        return getMaxNutrIndex(nutrSum);
    }
    
    private int getSolutionMinNutrIndex() {
        double[] nutrSum = Dish.computeNutrTotal(currentFoodsArr);
        return getMinNutrIndex(nutrSum);
    }
    
    private int getMinNutrIndex(double[] nutrArr) {
        double minNutrVal = Double.MAX_VALUE;
        int minNutrIndex = 0;
        for (int jj=1; jj<nutrArr.length-4; jj++) {
            if (nutrArr[jj] < minNutrVal) {
                minNutrVal = nutrArr[jj];
                minNutrIndex = jj;
            }
        }
        return minNutrIndex;
    }
    
    private int getMaxNutrIndex(double[] nutrArr) {
        double maxNutrVal = 0.0;
        int maxNutrIndex = 0;
        for (int jj=1; jj<nutrArr.length; jj++) {
            if (nutrArr[jj] > maxNutrVal) {
                maxNutrVal = nutrArr[jj];
                maxNutrIndex = jj;
            }
        }
        return maxNutrIndex;
    }
}
