package jsf;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;
import com.sun.jersey.api.json.JSONMarshaller;
import dataModel.Dish;
import dataModel.FoodRecommendation;
import ejb.entityBeans.FoodInfo;
import ejb.entityBeans.Meal;
import ejb.entityBeans.Person;
import ejb.entityBeans.UserAccount;
import ejb.sessionBeans.FoodInfoFacade;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import ejb.sessionBeans.MealFacade;
import ejb.sessionBeans.PersonFacade;
import ejb.sessionBeans.UserAccountFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import services.HillClimbingAlgorithmService;

@ManagedBean(name = "mealController")
@SessionScoped
public class MealController implements Serializable {

    @EJB
    private MealFacade mealFacade;
    @EJB
    private UserAccountFacade userAccountFacade;
    @EJB
    private PersonFacade personFacade;
    @EJB
    private FoodInfoFacade foodInfoFacade;
    
    private FoodInfo selectedFood;

    private List<FoodInfo> allFoods;
    private FoodRecommendation createdFoodRecomm;    
    private Person personFilter;
    
    private Meal selectedMeal;
    private List<Meal> meals;

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
    private List<Meal> filteredMeals;

    public MealController() {
        if (createdFoodRecomm == null) {
            initFoodRecomm();
        }
    }
    
    public void initFoodRecomm() {
        createdFoodRecomm = new FoodRecommendation();
    }
        
    public Meal getSelectedMeal() {
        return selectedMeal;
    }

    public void setSelectedMeal(Meal selectedMeal) {
        this.selectedMeal = selectedMeal;
    }

    public List<Meal> getFilteredMeals() {
        return filteredMeals;
    }

    public void setFilteredMeals(List<Meal> filteredMeals) {
        this.filteredMeals = filteredMeals;
    }
    
    public List<Meal> getMeals() {
        if (meals == null) {
            meals = mealFacade.findForPerson(getPersonFilter().getUserAccountId());
        }
        return meals;
    }

    public FoodRecommendation getCreatedFoodRecomm() {
        return createdFoodRecomm;
    }
    
    public String getCreatedFoodRecommJson() {
        try {
            JSONJAXBContext jc = new JSONJAXBContext(JSONConfiguration.mapped().build(), FoodRecommendation.class);
            JSONMarshaller marshaller = jc.createJSONMarshaller();
            StringWriter sw = new StringWriter();
            marshaller.marshallToJSON(getCreatedFoodRecomm(), sw);
            System.out.println("jsonObj=" + sw.toString());
            return sw.toString();
        }
        catch(Exception ex) {
            ex.printStackTrace(System.out);
        }
        return null;
    }

    public void setCreatedFoodRecomm(FoodRecommendation createdFoodRecomm) {
        this.createdFoodRecomm = createdFoodRecomm;
    }
    
    public FoodInfo getSelectedFood() {
        return selectedFood;
    }

    public void setSelectedFood(FoodInfo selectedFood) {
        this.selectedFood = selectedFood;
        if (selectedFood != null) {
            //System.out.println("selectedFood=" + selectedFood.getFoodDescription().getLongDesc());
            //System.out.println(getPersonFilter().getFullName());
            Dish fina = Dish.from(selectedFood, getPersonFilter().getRdaFoodInfo());
            if (!createdFoodRecomm.getFoodsList().contains(fina)) {
                createdFoodRecomm.getFoodsList().add(fina);
            }
        } 
    }
    
    public Person getPersonFilter() {
        if (personFilter == null) {
            HttpServletRequest req = JsfUtil.getRequest();
            if (req.isUserInRole("USER")) {
                String loggedUsername = req.getUserPrincipal().getName();
                UserAccount loggedUserAccount = userAccountFacade.findByUsername(loggedUsername);
                personFilter = personFacade.find(loggedUserAccount.getUserAccountId());
            }
            else {
                PersonController controller = JsfUtil.getControllerFromJSFContext(PersonController.class, "personController");
                personFilter = controller.getSelected();
            }
        }
        return personFilter;
    }

    public void setPersonFilter(Person personFilter) {
        this.personFilter = personFilter;
    }

    public void computeQuantities() {
        //System.out.println("computeQuantities");
        HillClimbingAlgorithmService hcas = new HillClimbingAlgorithmService();
        Dish[] currentDishes = createdFoodRecomm.getFoodsList().toArray(new Dish[createdFoodRecomm.getFoodsList().size()]);
        Dish[] updatedDishes = createdFoodRecomm.getFoodsList().toArray(new Dish[createdFoodRecomm.getFoodsList().size()]);
        hcas.computeQuantities(currentDishes, updatedDishes, 10);
        createdFoodRecomm.setNutrTotal(Dish.computeNutrTotal(updatedDishes));
        createdFoodRecomm.setPersonId(getPersonFilter().getUserAccountId());
    }
    
    public List<FoodInfo> completeFoods(String query) {  
        List<FoodInfo> suggestions = new ArrayList<FoodInfo>();
        int count = 10;
        for(FoodInfo fi : getAllFoods()) {  
            if (searchCriteria(query.toLowerCase(), fi.getFoodDescription().getLongDesc().toLowerCase(), 0)) {
                suggestions.add(fi);
                count --;
            }
            if (count == 0)
                break;
        }        
        return suggestions;  
    }
    
    private boolean searchCriteria(String query, String text, int level) {
        int notContains = 0;
        String words[] = query.split("\\s+");
        level = Math.min(level, words.length - 1);
        for (String word : words) {
            if (!text.contains(word)) {
                notContains++;
            }
            if (level < notContains) 
                return false;
        }
        return true;
    }
    
    public List<FoodInfo> getAllFoods() {
        if (allFoods == null) {
            allFoods = foodInfoFacade.findAll();
        }
        return allFoods;
    }

    public String prepareList() {
        meals = null;
        filteredMeals = null;
        return "List?faces-redirect=true";
    }

    public String prepareCreate() {
        selectedMeal = new Meal();
        return "Create?faces-redirect=true";
    }
    
    public String prepareCreateRecipe() {
        return "CreateRecipe?faces-redirect=true";
    }
    
    public String create() {
        try {
            selectedMeal.setPerson(getPersonFilter());
            mealFacade.create(selectedMeal);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("MealCreated"));
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        //selectedMeal = (Meal) getItems().getRowData();
        return "Edit?faces-redirect=true";
    }

    public String update() {
        try {
            mealFacade.edit(selectedMeal);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("MealUpdated"));
            return "View?faces-redirect=true";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        performDestroy();
        meals = null;
        filteredMeals = null;
        return "List?faces-redirect=true";
    }

    private void performDestroy() {
        try {
            mealFacade.remove(selectedMeal);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("MealDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(mealFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(mealFacade.findAll(), true);
    }

    @FacesConverter(forClass = Meal.class)
    public static class MealControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MealController controller = (MealController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mealController");
            return controller.mealFacade.find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Meal) {
                Meal o = (Meal) object;
                return getStringKey(o.getMealId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Meal.class.getName());
            }
        }
    }
}
