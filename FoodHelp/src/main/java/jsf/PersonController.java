package jsf;

import ejb.entityBeans.FoodInfo;
import dataModel.Dish;
import ejb.entityBeans.FoodGroup;
import ejb.entityBeans.Meal;
import ejb.entityBeans.Nutritionist;
import ejb.entityBeans.Person;
import ejb.entityBeans.UserAccount;
import ejb.sessionBeans.FoodInfoFacade;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import ejb.sessionBeans.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.DualListModel;

@ManagedBean(name = "personController")
@SessionScoped
public class PersonController implements Serializable {  
    @EJB
    private PersonFacade ejbFacade;
    @EJB
    private FoodGroupFacade foodGroupFacade;
    @EJB
    private MealFacade mealFacade;
    @EJB
    private UserAccountFacade userAccountFacade;
    @EJB
    private NutritionistFacade nutritionistFacade;
    
    private Person current;    

    private Nutritionist nutritionistFilter;
    private DataModel items = null;
    private PaginationHelper pagination;
    private DualListModel<FoodGroup> personFoodGroups;

    public DualListModel<FoodGroup> getPersonFoodGroups() {
        return personFoodGroups;
    }

    public void setPersonFoodGroups(DualListModel<FoodGroup> personFoodGroups) {
        this.personFoodGroups = personFoodGroups;
    }

    public PersonController() {

    }
    
    public Person getSelected() {
        if (current == null) {
            HttpServletRequest req = JsfUtil.getRequest();
            if (req.isUserInRole("USER")) {
                String loggedUsername = req.getUserPrincipal().getName();
                UserAccount loggedUserAccount = userAccountFacade.findByUsername(loggedUsername);
                setSelected(getFacade().find(loggedUserAccount.getUserAccountId()));
            }
        }
        return current;
    }
    
    public void setSelected(Person p) {
        current = p;
        if (current.getExclFoodGroups() == null) {
            List<FoodGroup> exfg = new ArrayList<FoodGroup>();
            current.setExclFoodGroups(exfg);
        }
        List<FoodGroup> groupsSource = foodGroupFacade.findAll();            
        List<FoodGroup> groupsTarget = new ArrayList(current.getExclFoodGroups());
        groupsSource.remove(groupsSource.size()-1);
        groupsSource.removeAll(groupsTarget);      
        personFoodGroups = new DualListModel<FoodGroup>(groupsSource, groupsTarget);  
    }

    private PersonFacade getFacade() {
        return ejbFacade;
    }

    public Nutritionist getNutritionistFilter() {
        if (nutritionistFilter == null) {
            HttpServletRequest req = JsfUtil.getRequest();
            if (req.isUserInRole("NUTRITIONIST")) {
                String loggedUsername = req.getUserPrincipal().getName();
                UserAccount loggedUserAccount = userAccountFacade.findByUsername(loggedUsername);
                nutritionistFilter = nutritionistFacade.find(loggedUserAccount.getUserAccountId());
            }
        }
        return nutritionistFilter;
    }
    
    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    if (getNutritionistFilter() == null) {
                        return new ListDataModel(getFacade().findRange(
                                new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));        
                    }
                    else {
                        return new ListDataModel(getFacade().findForNutritionist(
                                getNutritionistFilter().getUserAccountId(), 
                                new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()})); 
                    }
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List?faces-redirect=true";
    }

    public String prepareView() {
        //setSelected((Person) getItems().getRowData());
        return "View?faces-redirect=true";
    }
    
    public String prepareEdit() {
        //setSelected((Person) getItems().getRowData());
        return "Edit";
    }

    public String update() {
        try {
            current.setExclFoodGroups(personFoodGroups.getTarget());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PersonUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        setSelected((Person) getItems().getRowData());
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            userAccountFacade.remove(userAccountFacade.find(current.getUserAccountId()));
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PersonDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }
        
    public SelectItem[] getMealDatesSelectOne() {
        return JsfUtil.getSelectItems(mealFacade.getMealDates(current.getUserAccountId()), true);
    }

    @FacesConverter(forClass = Person.class)
    public static class PersonControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PersonController controller = (PersonController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "personController");
            return controller.ejbFacade.find(getKey(value));
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
            if (object instanceof Person) {
                Person o = (Person) object;
                return getStringKey(o.getUserAccountId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Person.class.getName());
            }
        }
    }
}
