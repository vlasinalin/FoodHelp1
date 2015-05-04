
package dataModel;

import ejb.entityBeans.FoodInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import common.*;
import ejb.sessionBeans.FoodInfoFacade;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "dish")
public class Dish implements Serializable {

    public int quantity = 100; // grams
    private String longDesc;
    private String foodgroupDesc;

    public String getFoodgroupDesc() {
        return foodgroupDesc;
    }

    public void setFoodgroupDesc(String foodgroupDesc) {
        this.foodgroupDesc = foodgroupDesc;
    }
    private Integer nDBNo;
    
    @XmlTransient
    public double[] nutrients;
    
    public Dish() {
        nutrients = new double[Constants.nutrientNames.length];
        for (int i=0; i<nutrients.length; i++) {
            nutrients[i] = 0.0;
        }
    }
    
    public Dish(Dish fina) {
        quantity = fina.quantity;
        longDesc = fina.longDesc;
        foodgroupDesc = fina.foodgroupDesc;
        nutrients = new double[Constants.nutrientNames.length];
        for (int i=0; i<nutrients.length; i++) {
            nutrients[i] = fina.nutrients[i];
        }
    }
    
    public double[] getNutrients() {
        return nutrients;
    }
    
    public Integer getNDBNo() {
        return nDBNo;
    }
    
    public void setNDBNo(Integer nDBNo) {
        this.nDBNo = nDBNo;
    }
    
    public int getDishQuantity() {
        return this.quantity;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String shrtDesc) {
        this.longDesc = shrtDesc;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nDBNo != null ? nDBNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dish)) {
            return false;
        }
        Dish other = (Dish) object;
        if (this.nDBNo != other.nDBNo) {
            return false;
        }
        return true;
    }
    
    public static Dish from(FoodInfo fina, FoodInfo foodRDA) {
        Dish fiNutrArr = new Dish();   
        if (fina.getFoodDescription() != null) {
            fiNutrArr.setLongDesc(fina.getFoodDescription().getLongDesc());
            if (fina.getFoodDescription().getFoodGroup() != null) {
                fiNutrArr.setFoodgroupDesc(fina.getFoodDescription().getFoodGroup().getFdGrpDesc());
            }
        }
        fiNutrArr.setNDBNo(fina.getNDBNo());
        fiNutrArr.nutrients[0] = fina.getWaterg() / (double)(foodRDA.getWaterg()); 
        fiNutrArr.nutrients[1] = fina.getCarbohydrtg() / (double)(foodRDA.getCarbohydrtg()); 
        fiNutrArr.nutrients[2] = fina.getCalciummg() / (double)(foodRDA.getCalciummg());
        fiNutrArr.nutrients[3] = fina.getCoppermg() / (double)(foodRDA.getCoppermg());
        fiNutrArr.nutrients[4] = fina.getFiberTDg() / (double)(foodRDA.getFiberTDg()); 
        fiNutrArr.nutrients[5] = fina.getFolateTotug() / (double)(foodRDA.getFolateTotug()); 
        fiNutrArr.nutrients[6] = fina.getIronmg() / (double)(foodRDA.getIronmg()); 
        fiNutrArr.nutrients[7] = fina.getMagnesiummg() / (double)(foodRDA.getMagnesiummg()); 
        fiNutrArr.nutrients[8] = fina.getManganesemg() / (double)(foodRDA.getManganesemg());
        fiNutrArr.nutrients[9] = fina.getNiacinmg() / (double)(foodRDA.getNiacinmg()); 
        fiNutrArr.nutrients[10] = fina.getPantoAcidmg() / (double)(foodRDA.getPantoAcidmg()); 
        fiNutrArr.nutrients[11] = fina.getPhosphorusmg() / (double)(foodRDA.getPhosphorusmg()); 
        fiNutrArr.nutrients[12] = fina.getPotassiummg() / (double)(foodRDA.getPotassiummg()); 
        fiNutrArr.nutrients[13] = fina.getProteing() / (double)(foodRDA.getProteing());
        fiNutrArr.nutrients[14] = fina.getRiboflavinmg() / (double)(foodRDA.getRiboflavinmg()); 
        fiNutrArr.nutrients[15] = fina.getSeleniumug() / (double)(foodRDA.getSeleniumug()); 
        fiNutrArr.nutrients[16] = fina.getSodiummg() / (double)(foodRDA.getSodiummg()); 
        fiNutrArr.nutrients[17] = fina.getThiaminmg() / (double)(foodRDA.getThiaminmg()); 
        fiNutrArr.nutrients[18] = fina.getVitB12ug() / (double)(foodRDA.getVitB12ug());
        fiNutrArr.nutrients[19] = fina.getVitB6mg() / (double)(foodRDA.getVitB6mg()); 
        fiNutrArr.nutrients[20] = fina.getVitCmg() / (double)(foodRDA.getVitCmg()); 
        fiNutrArr.nutrients[21] = fina.getVitDug() / (double)(foodRDA.getVitDug()); 
        fiNutrArr.nutrients[22] = fina.getVitEmg() / (double)(foodRDA.getVitEmg()); 
        fiNutrArr.nutrients[23] = fina.getVitKug() / (double)(foodRDA.getVitKug()); 
        fiNutrArr.nutrients[24] = fina.getZincmg() / (double)(foodRDA.getZincmg());
        fiNutrArr.nutrients[25] = fina.getEnergKcal() / (double)(foodRDA.getEnergKcal());          
        fiNutrArr.nutrients[26] = fina.getLipidTotg() / (double)(foodRDA.getLipidTotg()); 
        fiNutrArr.nutrients[27] = fina.getSugarTotg() / (double)(foodRDA.getSugarTotg());
        fiNutrArr.nutrients[28] = fina.getCholestrlmg() / (double)(foodRDA.getCholestrlmg());  
        return fiNutrArr;
    }
    
    public static Dish from(FoodInfo fi) {
        Dish fiNutrArr = new Dish();   
        if (fi.getFoodDescription() != null) {
            fiNutrArr.setLongDesc(fi.getFoodDescription().getLongDesc());
            if (fi.getFoodDescription().getFoodGroup() != null) {
                fiNutrArr.setFoodgroupDesc(fi.getFoodDescription().getFoodGroup().getFdGrpDesc());
            }
        }
        fiNutrArr.setNDBNo(fi.getNDBNo());
        fiNutrArr.nutrients[0] = fi.getWaterg();
        fiNutrArr.nutrients[1] = fi.getCarbohydrtg(); 
        fiNutrArr.nutrients[2] = fi.getCalciummg();
        fiNutrArr.nutrients[3] = fi.getCoppermg();
        fiNutrArr.nutrients[4] = fi.getFiberTDg(); 
        fiNutrArr.nutrients[5] = fi.getFolateTotug(); 
        fiNutrArr.nutrients[6] = fi.getIronmg(); 
        fiNutrArr.nutrients[7] = fi.getMagnesiummg(); 
        fiNutrArr.nutrients[8] = fi.getManganesemg();
        fiNutrArr.nutrients[9] = fi.getNiacinmg(); 
        fiNutrArr.nutrients[10] = fi.getPantoAcidmg(); 
        fiNutrArr.nutrients[11] = fi.getPhosphorusmg(); 
        fiNutrArr.nutrients[12] = fi.getPotassiummg(); 
        fiNutrArr.nutrients[13] = fi.getProteing();
        fiNutrArr.nutrients[14] = fi.getRiboflavinmg(); 
        fiNutrArr.nutrients[15] = fi.getSeleniumug(); 
        fiNutrArr.nutrients[16] = fi.getSodiummg(); 
        fiNutrArr.nutrients[17] = fi.getThiaminmg(); 
        fiNutrArr.nutrients[18] = fi.getVitB12ug();
        fiNutrArr.nutrients[19] = fi.getVitB6mg(); 
        fiNutrArr.nutrients[20] = fi.getVitCmg(); 
        fiNutrArr.nutrients[21] = fi.getVitDug(); 
        fiNutrArr.nutrients[22] = fi.getVitEmg(); 
        fiNutrArr.nutrients[23] = fi.getVitKug(); 
        fiNutrArr.nutrients[24] = fi.getZincmg();
        fiNutrArr.nutrients[25] = fi.getEnergKcal();          
        fiNutrArr.nutrients[26] = fi.getLipidTotg(); 
        fiNutrArr.nutrients[27] = fi.getSugarTotg();
        fiNutrArr.nutrients[28] = fi.getCholestrlmg();  
        return fiNutrArr;
    }
    
    public static double[] computeNutrTotal(List<Dish> foodsList) {
        double[] nutrTotal = new double[Constants.nutrientNames.length];
        for (int i=0; i<foodsList.size(); i++) {
            Dish food = foodsList.get(i);
            for (int j=0; j<nutrTotal.length; j++) {
                nutrTotal[j] += food.nutrients[j] * food.quantity;
            }
        }  
        return nutrTotal;
    }
    
    public static double[] computeNutrTotal(Dish[] foods) {
        double[] nutrTotal = new double[Constants.nutrientNames.length];
        for (int i=0; i<foods.length; i++) {
            for (int j=0; j<nutrTotal.length; j++) {
                nutrTotal[j] += foods[i].nutrients[j] * foods[i].quantity;
            }
        }  
        return nutrTotal;
    }
    
    public static double[] computeNutrAverage(List<Dish> foodsList, int daysNr) {
        double[] nutrTotal = new double[Constants.nutrientNames.length];
        for (int i=0; i<foodsList.size(); i++) {
            Dish food = foodsList.get(i);
            for (int j=0; j<nutrTotal.length; j++) {
                nutrTotal[j] += food.nutrients[j] * food.quantity;
            }
        }
        for (int j=0; j<nutrTotal.length; j++) {
            nutrTotal[j] /= daysNr;
        }
        return nutrTotal;
    }
    
    public static List<Dish> filterFoodsByQuantity(List<Dish> foodsList, int minQuantity) {
        List<Dish> foods = new ArrayList<Dish>();
        for (Dish fi : foodsList) {
            if (fi.quantity >= minQuantity) {
                foods.add(fi);
            }
        }
        return foods;
    }
}
