/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services.contracts;

import dataModel.FoodRecommendation;
import ejb.entityBeans.Person;
import ejb.sessionBeans.FoodInfoFacade;

/**
 *
 * @author Alinv
 */
public interface FoodRecommendationsService {
    
    public FoodRecommendation generateRecommendation(FoodInfoFacade fiFacade, Person person);   

}
