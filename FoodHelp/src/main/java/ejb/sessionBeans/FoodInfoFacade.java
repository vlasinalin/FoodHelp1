/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionBeans;

import common.*;
import ejb.entityBeans.FoodInfo;
import dataModel.Dish;
import ejb.entityBeans.FoodGroup;
import ejb.entityBeans.Meal;
import ejb.entityBeans.Person;
import java.util.Date;
import java.util.List;
import java.util.*;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author AlinV
 */
@Stateless
public class FoodInfoFacade extends AbstractFacade<FoodInfo> {
    
    private static String sqlSelect = "select f from abbrev f where f.NDB_No not in ( " +
	"select exf.NDB_No " +
	"from abbrev exf " +
		"inner join food_des fd on fd.NDB_No = exf.NDB_No " +
		"inner join fd_group fg on fg.foodgroupId = fd.FdGrp_Cd " +
		"inner join person_excluded_foodgroup pef on pef.foodgroupId = fg.foodgroupId " +
	"where pef.personId = :personId) ";
    
    @PersistenceContext(unitName = "FoodHelpPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FoodInfoFacade() {
        super(FoodInfo.class);
    }

    public List<FoodInfo> findAllRda() {
        javax.persistence.Query q = em.createNamedQuery("FoodInfo.findAllRda");
        return q.getResultList();
    }
    
    public List<FoodInfo> findAll(int maxResults) {
        javax.persistence.Query q = em.createNamedQuery("FoodInfo.findAll");
        q.setMaxResults(maxResults);
        return q.getResultList();
    }
    
    public List<FoodInfo> findAll() {
        Date before = new Date();

        javax.persistence.Query q = em.createNamedQuery("FoodInfo.findAll");
        List<FoodInfo> fiList = q.getResultList();
                     
        Date after = new Date();
        long milisec = after.getTime() - before.getTime();
        System.out.println("\n##########\n Exec Time FoodInfoFacade.findAll = " + milisec + "(ms)"); 
        System.out.println("Foods count = " + fiList.size());
        
        return fiList;
    }
    
    public List<FoodInfo> findAllExcl(Collection<FoodGroup> exclFoodgroups) {
        List<FoodInfo> allFoods = findAll();
        List<FoodInfo> retVal = new ArrayList<FoodInfo>();
        for (FoodInfo fi : allFoods) {
            FoodGroup fg = fi.getFoodDescription().getFoodGroup();
            if (!exclFoodgroups.contains(fg)) {
                retVal.add(fi);
            }
        }
        return retVal;
    }
    
    public FoodInfo createFoodInfo()
    {
        FoodInfo dest = new FoodInfo();
        double initValDouble = 0.0;
        int initValInt = 0;
        dest.setAshg(initValDouble);
        dest.setCalciummg(initValInt);
        dest.setCarbohydrtg(initValDouble);
        dest.setCholestrlmg(initValInt);
        dest.setCholineTotmg(initValDouble);
        dest.setCoppermg(initValDouble);
        dest.setEnergKcal(initValInt);
        dest.setFAMonog(initValDouble);
        dest.setFAPolyg(initValDouble);
        dest.setFASatg(initValDouble);
        dest.setFiberTDg(initValDouble);
        dest.setFolateTotug(initValDouble);
        dest.setIronmg(initValDouble);
        dest.setLipidTotg(initValDouble);
        dest.setMagnesiummg(initValDouble);
        dest.setManganesemg(initValDouble);
        dest.setNiacinmg(initValDouble);
        dest.setPantoAcidmg(initValDouble);
        dest.setPhosphorusmg(initValInt);
        dest.setPotassiummg(initValInt);
        dest.setProteing(initValDouble);
        dest.setRiboflavinmg(initValDouble);
        dest.setSeleniumug(initValDouble);
        dest.setSodiummg(initValInt);
        dest.setSugarTotg(initValDouble);
        dest.setThiaminmg(initValDouble);
        dest.setVitAIU(initValInt);
        dest.setVitB12ug(initValDouble);
        dest.setVitB6mg(initValDouble);
        dest.setVitCmg(initValDouble);
        dest.setVitDug(initValDouble);
        dest.setVitEmg(initValDouble);
        dest.setVitKug(initValDouble);
        dest.setWaterg(initValDouble);
        dest.setZincmg(initValDouble);
        
        return dest;
    }
    
    public FoodInfo sumFoodInfo(FoodInfo dest, FoodInfo source, float mealQuantity) {
        float nutrientQuantity = 100;
        float ratio = mealQuantity / nutrientQuantity;
        dest.setAshg(dest.getAshg() + source.getAshg() * ratio);
        dest.setCalciummg(dest.getCalciummg() + (int)(source.getCalciummg() * ratio));
        dest.setCarbohydrtg(dest.getCarbohydrtg() + source.getCarbohydrtg() * ratio);
        dest.setCholestrlmg(dest.getCholestrlmg() + (int)(source.getCholestrlmg() * ratio));
        dest.setCholineTotmg(dest.getCholineTotmg() + source.getCholineTotmg() * ratio);
        dest.setCoppermg(dest.getCoppermg() + source.getCoppermg() * ratio);
        dest.setEnergKcal(dest.getEnergKcal() + (int)(source.getEnergKcal() * ratio));
        dest.setFAMonog(dest.getFAMonog() + source.getFAMonog() * ratio);
        dest.setFAPolyg(dest.getFAPolyg() + source.getFAPolyg() * ratio);
        dest.setFASatg(dest.getFASatg() + source.getFASatg() * ratio);
        dest.setFiberTDg(dest.getFiberTDg() + source.getFiberTDg() * ratio);
        dest.setFolateTotug(dest.getFolateTotug() + source.getFolateTotug() * ratio);
        dest.setIronmg(dest.getIronmg() + source.getIronmg() * ratio);
        dest.setLipidTotg(dest.getLipidTotg() + source.getLipidTotg() * ratio);
        dest.setMagnesiummg(dest.getMagnesiummg() + source.getMagnesiummg() * ratio);
        dest.setManganesemg(dest.getManganesemg() + source.getManganesemg() * ratio);
        dest.setNiacinmg(dest.getNiacinmg() + source.getNiacinmg() * ratio);
        dest.setPantoAcidmg(dest.getPantoAcidmg() + source.getPantoAcidmg() * ratio);
        dest.setPhosphorusmg(dest.getPhosphorusmg() + (int)(source.getPhosphorusmg() * ratio));
        dest.setPotassiummg(dest.getPotassiummg() + (int)(source.getPotassiummg() * ratio));
        dest.setProteing(dest.getProteing() + source.getProteing() * ratio);
        dest.setRiboflavinmg(dest.getRiboflavinmg() + source.getRiboflavinmg() * ratio);
        dest.setSeleniumug(dest.getSeleniumug() + source.getSeleniumug() * ratio);
        dest.setSodiummg(dest.getSodiummg() + (int)(source.getSodiummg() * ratio));
        dest.setSugarTotg(dest.getSugarTotg() + source.getSugarTotg() * ratio);
        dest.setThiaminmg(dest.getThiaminmg() + source.getThiaminmg() * ratio);
        dest.setVitAIU(dest.getVitAIU() + (int)(source.getVitAIU() * ratio));
        dest.setVitB12ug(dest.getVitB12ug() + source.getVitB12ug() * ratio);
        dest.setVitB6mg(dest.getVitB6mg() + source.getVitB6mg() * ratio);
        dest.setVitCmg(dest.getVitCmg() + source.getVitCmg() * ratio);
        dest.setVitDug(dest.getVitDug() + source.getVitDug() * ratio);
        dest.setVitEmg(dest.getVitEmg() + source.getVitEmg() * ratio);
        dest.setVitKug(dest.getVitKug() + source.getVitKug() * ratio);
        dest.setWaterg(dest.getWaterg() + source.getWaterg() * ratio);
        dest.setZincmg(dest.getZincmg() + source.getZincmg() * ratio);

        return dest;
    }
    
    public FoodInfo divideFoodInfo(FoodInfo dest, int divisor) {
        dest.setAshg(dest.getAshg() / divisor);
        dest.setCalciummg(dest.getCalciummg() / divisor);
        dest.setCarbohydrtg(dest.getCarbohydrtg() / divisor);
        dest.setCholestrlmg(dest.getCholestrlmg() / divisor);
        dest.setCholineTotmg(dest.getCholineTotmg() / divisor);
        dest.setCoppermg(dest.getCoppermg() / divisor);
        dest.setEnergKcal(dest.getEnergKcal() / divisor);
        dest.setFAMonog(dest.getFAMonog() / divisor);
        dest.setFAPolyg(dest.getFAPolyg() / divisor);
        dest.setFASatg(dest.getFASatg() / divisor);
        dest.setFiberTDg(dest.getFiberTDg() / divisor);
        dest.setFolateTotug(dest.getFolateTotug() / divisor);
        dest.setIronmg(dest.getIronmg() / divisor);
        dest.setLipidTotg(dest.getLipidTotg() / divisor);
        dest.setMagnesiummg(dest.getMagnesiummg() / divisor);
        dest.setManganesemg(dest.getManganesemg() / divisor);
        dest.setNiacinmg(dest.getNiacinmg() / divisor);
        dest.setPantoAcidmg(dest.getPantoAcidmg() / divisor);
        dest.setPhosphorusmg(dest.getPhosphorusmg() / divisor);
        dest.setPotassiummg(dest.getPotassiummg() / divisor);
        dest.setProteing(dest.getProteing() / divisor);
        dest.setRiboflavinmg(dest.getRiboflavinmg() / divisor);
        dest.setSeleniumug(dest.getSeleniumug() / divisor);
        dest.setSodiummg(dest.getSodiummg() / divisor);
        dest.setSugarTotg(dest.getSugarTotg() / divisor);
        dest.setThiaminmg(dest.getThiaminmg() / divisor);
        dest.setVitAIU(dest.getVitAIU() / divisor);
        dest.setVitB12ug(dest.getVitB12ug() / divisor);
        dest.setVitB6mg(dest.getVitB6mg() / divisor);
        dest.setVitCmg(dest.getVitCmg() / divisor);
        dest.setVitDug(dest.getVitDug() / divisor);
        dest.setVitEmg(dest.getVitEmg() / divisor);
        dest.setVitKug(dest.getVitKug() / divisor);
        dest.setWaterg(dest.getWaterg() / divisor);
        dest.setZincmg(dest.getZincmg() / divisor);

        return dest;
    }
    
    public FoodInfo getDefaultRDA() {
        return find(Constants.DefaultRdaDbId);
    }
    
    public FoodInfo getWater() {
        return this.find(114385);
    }
}
