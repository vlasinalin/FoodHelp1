/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "foodRecommendation")
public class FoodRecommendation {
    
    private List<Dish> foodsList = new ArrayList<Dish>();
    private Integer personId;
    private Date recommDate = new Date();
    private double[] nutrTotal;

    public double[] getNutrTotal() {
        return nutrTotal;
    }

    public void setNutrTotal(double[] nutrTotal) {
        this.nutrTotal = nutrTotal;
    }


    public List<Dish> getFoodsList() {
        return foodsList;
    }

    public void setFoodsList(List<Dish> foodsList) {
        this.foodsList = foodsList;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public Date getRecommDate() {
        return recommDate;
    }

    public void setRecommDate(Date recommDate) {
        this.recommDate = recommDate;
    }
    
    public FoodRecommendation() {}
    
    public static void fillWater(List<Dish> solFoods, Dish finaWater) {
        double nutrTotal[] = Dish.computeNutrTotal(solFoods);     
        finaWater.quantity = (int)((100.0 - nutrTotal[0])/finaWater.nutrients[0]);
        if (solFoods.contains(finaWater)) {
            int waterIndex = solFoods.indexOf(finaWater);
            finaWater.quantity = (finaWater.quantity + solFoods.get(waterIndex).quantity);
            solFoods.remove(waterIndex);
        }
        solFoods.add(finaWater);
    }
           
}
