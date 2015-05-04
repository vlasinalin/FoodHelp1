/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionBeans;

import dataModel.Dish;
import ejb.entityBeans.FoodInfo;
import ejb.entityBeans.Meal;
import ejb.entityBeans.Person;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author AlinV
 */
@Stateless
public class MealFacade extends AbstractFacade<Meal> {
    @PersistenceContext(unitName = "FoodHelpPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MealFacade() {
        super(Meal.class);
    }
    
        
    public List<Meal> findForPerson(int personId) {       
        javax.persistence.Query q = em.createNamedQuery("Meal.findByPersonId");
        q.setParameter("personId", personId);
        return q.getResultList();
    }
    
    public List<Meal> findForPerson(int personId, int range[]) {       
        javax.persistence.Query q = em.createNamedQuery("Meal.findByPersonId");
        q.setParameter("personId", personId);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
    public List<Meal> findForPersonAndDate(int personId, Date mealDate) {       
        javax.persistence.Query q = em.createNamedQuery("Meal.findByDateAndPersonId");
        q.setParameter("personId", personId);
        q.setParameter("mealDate", mealDate);
        return q.getResultList();
    }
          
    public List<Date> getMealDates(int personId) {       
        javax.persistence.Query q = em.createNamedQuery("Meal.findMealDatesByPersonId");
        q.setParameter("personId", personId);
        return q.getResultList();
    }
    
    public void insertMeals(List<Dish> foods, Date date, int personId) {
        for (Dish fi : foods) {
            Meal meal = new Meal();          
            meal.setMealQuantity((float)fi.quantity);
            meal.setMealDate(date);
            meal.setPerson(new Person(personId));
            meal.setFoodInfo(new FoodInfo(fi.getNDBNo()));
            super.create(meal);  
        }
    }
}
