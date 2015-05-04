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

public class BruteForceAlgorithmService implements FoodRecommendationsService {   
    
    private static final int ITERATIONS = 900;
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
        
        runBruteForceAlgorithm();      
        
        System.out.println("\n BestFoodsArr Error = " + HillClimbingAlgorithmService.getErrorValue(Dish.computeNutrTotal(bestFoodsArr)));
        
        List<Dish> filteredFoods = Dish.filterFoodsByQuantity(Arrays.asList(bestFoodsArr), 2);
        FoodRecommendation.fillWater(filteredFoods, Dish.from(foodInfoFacade.getWater(), foodInfoRda));
        FoodRecommendation result = new FoodRecommendation();
        result.setFoodsList(filteredFoods);      
        result.setNutrTotal(Dish.computeNutrTotal(filteredFoods));
        result.setPersonId(person.getUserAccountId());

        Date after = new Date();
        long milisec = after.getTime() - before.getTime();
        System.out.println("\n##########\n Exec Time BruteForce Algorithm = " + milisec + "(ms)");  
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

    private void runBruteForceAlgorithm() {
        int minSolErr = Integer.MAX_VALUE;
        int solErr;
        int trials = ITERATIONS;
        
        selectedRandomFoods();
        copyDishes(currentFoodsArr, bestFoodsArr);
        
        while (trials > 0)
        {   
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
            
            selectedRandomFoods();
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
    
    private void selectedRandomFoods() {
        int foodsSize =  allFinas.length;
        for (int i=0; i<selectedFoodsNr; i++) {
            Dish fina = allFinas[rand.nextInt(foodsSize)];
            currentFoodsArr[i] = fina;
        }
    }
    
}
