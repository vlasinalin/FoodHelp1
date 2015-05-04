/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import dataModel.Dish;
import dataModel.FoodRecommendation;
import ejb.entityBeans.Meal;
import ejb.entityBeans.Person;
import ejb.entityBeans.FoodInfo;
import ejb.sessionBeans.FoodInfoFacade;
import ejb.sessionBeans.MealFacade;
import java.util.Date;
import java.util.List;
import java.util.*;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class AnalysisService {
    @EJB
    private FoodInfoFacade foodInfoFacade;
    @EJB
    private MealFacade mealFacade;
    
    private Date mealDate;
    private Person person;
            
    public FoodRecommendation getNormalizedFoodInfoAverage(Person person, Date mealDate) {
        FoodRecommendation fr = new FoodRecommendation();
        this.mealDate = mealDate;
        this.person = person;
        List<Meal> personMeals = getPersonMeals();
        if (personMeals == null || personMeals.isEmpty()) {
            return fr;
        }
        fr.setRecommDate(mealDate);
        fr.setNutrTotal(getNutrAverage(personMeals));
        return fr;
    }
    
    private List<Meal> getPersonMeals() {
        if (mealDate == null) {
            return mealFacade.findForPerson(person.getUserAccountId());
        }
        else {
            return mealFacade.findForPersonAndDate(person.getUserAccountId(), mealDate);
        }
    }
    
    private double[] getNutrAverage(List<Meal> personMeals) {
        List<Dish> finas = new ArrayList<Dish>();
        List<Date> dates = new ArrayList<Date>();
        for(Meal meal : personMeals) {
            FoodInfo food = foodInfoFacade.find(meal.getFoodInfo().getNDBNo());
            Dish fina = Dish.from(food, person.getRdaFoodInfo());
            fina.quantity = (int)meal.getMealQuantity();
            finas.add(fina);
            
            if (!dates.contains(meal.getMealDate())) {
                dates.add(meal.getMealDate());
            }
        }
        int tracedDaysNr = dates.size();
        return Dish.computeNutrAverage(finas, tracedDaysNr);
    }
    
}
