/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import dataModel.Dish;
import dataModel.FoodRecommendation;
import dataModel.GaIndividual;
import ejb.entityBeans.FoodInfo;
import ejb.entityBeans.Person;
import ejb.sessionBeans.FoodInfoFacade;
import java.util.*;
import services.contracts.FoodRecommendationsService;

public class GeneticAlgorithmService implements FoodRecommendationsService {   

    private final int POPULATION_SIZE = 60;  
    private final int ITERATIONS = 15;        
    
    private final int INDIVIDUAL_FEAT_NR = 24;
    private final double ELITE_RATIO = 0.1;
    private final int FEATURE_MIN_QUANT = 10;
    
    private FoodInfoFacade foodInfoFacade;  
    private FoodInfo foodInfoRda;
    private Dish[] allFinas;
    private List<GaIndividual> population;
    private Random rand;
    private GaIndividual bestIndividual;
    
    private HillClimbingAlgorithmService hcas;
    
    private List<Integer> costs = new ArrayList<Integer>();
    
    private void initService(FoodInfoFacade fiFacade, Person person) {
        hcas = new HillClimbingAlgorithmService();
        bestIndividual = new GaIndividual();
        foodInfoFacade = fiFacade;
        foodInfoRda = person.getRdaFoodInfo();
        rand = new Random();
        initAllFoods(foodInfoFacade.findAllExcl(person.getExclFoodGroups()));
    }
    
    @Override
    public FoodRecommendation generateRecommendation(FoodInfoFacade fiFacade, Person person) {
        initService(fiFacade, person);
        
        Date before = new Date();
        
        buildInitialPopulation();
        runGeneticAlgorithm();

        List<Dish> solFoods = new ArrayList(bestIndividual.features);
        System.out.println("\n BestFoodsArr Error = " + HillClimbingAlgorithmService.getErrorValue(Dish.computeNutrTotal(solFoods)));
                    
        List<Dish> filteredFoods = Dish.filterFoodsByQuantity(solFoods, 5);
        FoodRecommendation.fillWater(filteredFoods, Dish.from(foodInfoFacade.getWater(), foodInfoRda));
        FoodRecommendation result = new FoodRecommendation();
        result.setFoodsList(filteredFoods);
        result.setNutrTotal(Dish.computeNutrTotal(filteredFoods));
        result.setPersonId(person.getUserAccountId());
        
        Date after = new Date();
        long milisec = after.getTime() - before.getTime();
        System.out.println("\n##########\n Exec Time Genetic Algorithm = " + milisec + "(ms)");
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

    private void buildInitialPopulation() {
        population = new ArrayList<GaIndividual>();
        for (int i=0; i<POPULATION_SIZE; i++) {
            population.add(selectRandomIndividual());
        }
    }
    
    private GaIndividual selectRandomIndividual() {
         GaIndividual individual = new GaIndividual();
         int allFoodsSize = allFinas.length;
         for (int i=0; i<INDIVIDUAL_FEAT_NR; i++) {
             individual.features.add(allFinas[rand.nextInt(allFoodsSize)]);
         }
         return individual;
    }
    
    private List<GaIndividual> getPopulationElite() {
        int eliteSize = (int)(POPULATION_SIZE*ELITE_RATIO);
        Collections.sort(population);
        return new ArrayList<GaIndividual>(population.subList(0, eliteSize));
    }
    
    // genetic algorithm
    private void runGeneticAlgorithm() {
        for (int i=0; i<ITERATIONS; i++) {
            computeFitness();
            System.out.println("\n Population " + i + " BestIndividualFitness = " + bestIndividual.fitness); 
            costs.add(bestIndividual.fitness);
            computeProbabilities();
            
            List<GaIndividual> nextPopulation = getPopulationElite();
            //List<GaIndividual> nextPopulation = new ArrayList<GaIndividual>();
            for (int j=nextPopulation.size(); j<POPULATION_SIZE; j++) {
                //System.out.println("\n child " + j);
                GaIndividual ri1 = getRandomIndividual();
                //printIndividual(ri1);
                GaIndividual ri2 = getRandomIndividual();
                //printIndividual(ri2);
                nextPopulation.add(reproduce(ri1, ri2));
            }      
            
            population = nextPopulation;
        }
        
        String output = new String();
        for (int x : costs) {
            output += x + "\t";
        }
        System.out.println(output);
    }

    private GaIndividual reproduce(GaIndividual ri1, GaIndividual ri2) {     
        GaIndividual children = new GaIndividual();
        children.features.addAll(ri1.getFeaturesHigherThan(FEATURE_MIN_QUANT));
        children.features.addAll(ri2.getFeaturesHigherThan(FEATURE_MIN_QUANT));
        //System.out.println(children.features.size());
        while (children.features.size() > INDIVIDUAL_FEAT_NR-2) {
            Dish fiToDelete = children.getRandomFeature(rand);
            children.features.remove(fiToDelete);
        }
        while (children.features.size() < INDIVIDUAL_FEAT_NR) {
            children.features.add(allFinas[rand.nextInt(allFinas.length)]);
        }
        return children;
    }
    
    private GaIndividual getRandomIndividual() {      
        int randInt = rand.nextInt(10000);
        int probSum = 0;
        for (int i=0; i<POPULATION_SIZE; i++) {
            probSum += (int)(population.get(i).probability * 10000.0);
            if (randInt < probSum) 
                return population.get(i);
        }
        return population.get(POPULATION_SIZE - 1);
    }
    
    private void computeProbabilities() {
        double maxFitness = 0;
        double[] fitnessArr = new double[POPULATION_SIZE];
        for (int i=0; i<POPULATION_SIZE; i++) {
            maxFitness = Math.max(maxFitness, population.get(i).fitness);
        }
        for (int i=0; i<POPULATION_SIZE; i++) {
           fitnessArr[i] = maxFitness / population.get(i).fitness;
        }
        double totalFitness = 0;
        for (int i=0; i<POPULATION_SIZE; i++) {
           totalFitness += fitnessArr[i];
        }
        for (int i=0; i<POPULATION_SIZE; i++) {
            population.get(i).probability = fitnessArr[i] / totalFitness;
        }
    }
    
    private void computeFitness() {
        for (int i=0; i<POPULATION_SIZE; i++) {
            GaIndividual individual = population.get(i);
            
            Dish[] currentFoods = individual.features.toArray(new Dish[individual.features.size()]);
            Dish[] candidateFoods = individual.features.toArray(new Dish[individual.features.size()]);
            
            hcas.computeQuantities(currentFoods, candidateFoods, 1);
            double[] nutrArray = Dish.computeNutrTotal(candidateFoods);
            individual.fitness = HillClimbingAlgorithmService.getErrorValue(nutrArray);

            if (individual.fitness < bestIndividual.fitness) {
                copyIndividual(individual, bestIndividual);
            }
        }
    }  
    
    private void copyIndividual(GaIndividual from, GaIndividual to) {
        copyDishesSets(from.features, to.features);
        to.fitness = from.fitness;
        to.probability = from.probability;
    }
    
    private void copyDishesSets(Set<Dish> from, Set<Dish> to) {
        to.clear();
        Dish[] dishes = new Dish[from.size()];
        for (int i=0; i<from.size(); i++) {
            dishes[i] = new Dish();
        }
        copyDishes(from.toArray(new Dish[from.size()]), dishes);
        to.addAll(Arrays.asList(dishes));
    }
    
        
    private void copyDishes(Dish[] from, Dish[] to) {
        for (int i=0; i<from.length; i++) {
            to[i].setFoodgroupDesc(from[i].getFoodgroupDesc());
            to[i].setLongDesc(from[i].getLongDesc());
            to[i].setNDBNo(from[i].getNDBNo());
            System.arraycopy(from[i].nutrients, 0, to[i].nutrients, 0, to[i].nutrients.length);
            to[i].quantity = from[i].quantity;
        }
    }
        
    private void printIndividual(List<Dish> individual) {
        System.out.println("--------individual--------");
        for(Dish fina : individual) {
            System.out.println(fina.getNDBNo() + " = " + fina.getLongDesc());
        }
    }
    
}
