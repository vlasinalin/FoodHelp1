/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataModel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Alinv
 */
public class GaIndividual implements Comparable {
    public Set<Dish> features;
    public int fitness;
    public double probability = 0.0;
    
    public GaIndividual(Collection features, int fitness) {
        this.features = new HashSet<Dish>(features);
        this.fitness = fitness;
    }
    
    public GaIndividual(Collection features) {
        this.features = new HashSet<Dish>(features);
        this.fitness = Integer.MAX_VALUE;
    }
    
    public GaIndividual() {
        this.features = new HashSet<Dish>();
        this.fitness = Integer.MAX_VALUE;
    }
    
    public Dish getRandomFeature(Random rand) {
        int randSetIndex = rand.nextInt(features.size());
        int index = 0;
        for (Dish fina : features) {
            if (index == randSetIndex) {
                return fina;
            }
            index++;
        }
        return null;
    }
    
    public Set<Dish> getFeaturesHigherThan(double minQuantity) {
        Set<Dish> result = new HashSet<Dish>();
        for (Dish feat : features) {
            if (feat.quantity > minQuantity)
                result.add(feat);
        }
        return result;
    }


    @Override
    public int compareTo(Object o) {
        GaIndividual other = (GaIndividual)o;
        return Double.compare(this.fitness, other.fitness);
    }
   
}
