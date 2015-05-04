/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package restservice;

import common.Constants;
import dataModel.Dish;
import dataModel.FoodRecommendation;
import dataModel.Nutrient;
import ejb.entityBeans.Person;
import ejb.entityBeans.UserAccount;
import ejb.entityBeans.UserGroup;
import ejb.sessionBeans.AbstractFacade;
import services.AnalysisService;
import ejb.sessionBeans.FoodInfoFacade;
import services.GreedyAlgorithmService;
import ejb.sessionBeans.MealFacade;
import ejb.sessionBeans.PersonFacade;
import ejb.sessionBeans.UserAccountFacade;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import jsf.MealController;
import jsf.util.JsfUtil;
//import org.json.JSONObject;
//import org.json.XML;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
import common.AlgorithmSettings;
import dataModel.SearchAlgorithm;
import services.GeneticAlgorithmService;
import services.contracts.FoodRecommendationsService;

@Stateless
@Path("foodRecommendation")
public class FoodRecommendationFacadeREST {
    @EJB
    private MealFacade mealFacade;
    @EJB
    private PersonFacade personFacade;
    @EJB
    private AnalysisService analysisFacade;
    @EJB
    private FoodInfoFacade foodInfoFacade;
    @EJB
    private AlgorithmSettings algorithmSettings;
    
    public FoodRecommendationFacadeREST() {
        
    }

    @GET
    @Path("getFoodAvgArray/{personId}")
    @Produces({"application/xml", "application/json"})
    public List<FoodRecommendation> getFoodAvgArray(@PathParam("personId") Integer personId) {
        Person person = personFacade.getPerson(personId);
        if (person == null) {
            return null;
        }
        List<FoodRecommendation> foodAvgList = new ArrayList<FoodRecommendation>();
        List<Date> mealDates = mealFacade.getMealDates(personId);
        for (Date mealDate : mealDates) {
            FoodRecommendation fr = analysisFacade.getNormalizedFoodInfoAverage(person, mealDate);
            foodAvgList.add(fr);
        }
        FoodRecommendation fr = analysisFacade.getNormalizedFoodInfoAverage(person, null);
        foodAvgList.add(fr);
        return foodAvgList;
    }
    
    @GET
    @Path("getFoodRecommendation/{personId}")
    @Produces({"application/xml", "application/json"})
    public FoodRecommendation getFoodRecommendation(@PathParam("personId") Integer personId) {
        Person person = personFacade.getPerson(personId);
        if (person == null) {
            return null;
        }
        System.out.println(algorithmSettings.getSearchAlgorithm());
        FoodRecommendationsService frService = algorithmSettings.getServiceInstance();      
        return frService.generateRecommendation(foodInfoFacade, person);
    }

    @POST
    @Path("saveFoodRecommendation")
    @Consumes({"application/xml", "application/json"})
    public void saveFoodRecommendation(FoodRecommendation foodRecommendation) {
        System.out.println("FoodsRecom foods size=" + foodRecommendation.getFoodsList().size());
        System.out.println("FoodsList date=" + foodRecommendation.getRecommDate());
        System.out.println("FoodsList person=" + foodRecommendation.getPersonId());
        mealFacade.insertMeals(foodRecommendation.getFoodsList(), 
               foodRecommendation.getRecommDate(), foodRecommendation.getPersonId());
    }

    @GET
    @Path("getFoodNutrNames")
    @Produces({"application/xml", "application/json"})
    public List<Nutrient> getFoodNutrNames() {
        List<Nutrient> nutrients = new ArrayList<Nutrient>();
        for (String nutrName : Constants.nutrientNames) {
            Nutrient nutr = new Nutrient();
            nutr.name = nutrName;
            nutrients.add(nutr);
        }
        return nutrients;
    }
    
    @GET
    @Path("getRDA/{personId}")
    @Produces({"application/xml", "application/json"})
    public FoodRecommendation getRDA(@PathParam("personId") Integer personId) {
        Person person = personFacade.getPerson(personId);
        if (person == null) {
            return null;
        }
        Dish fina = Dish.from(person.getRdaFoodInfo());
        FoodRecommendation fr = new FoodRecommendation();
        fr.setNutrTotal(fina.nutrients);
        return fr;
    }

}
