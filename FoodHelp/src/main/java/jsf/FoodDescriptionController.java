package jsf;

import ejb.entityBeans.FoodDescription;
import ejb.entityBeans.FoodInfo;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import ejb.sessionBeans.FoodDescriptionFacade;

import java.io.Serializable;
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

@ManagedBean(name = "foodDescriptionController")
@SessionScoped
public class FoodDescriptionController implements Serializable {
    @EJB
    private ejb.sessionBeans.FoodDescriptionFacade ejbFacade;
    @EJB
    private ejb.sessionBeans.FoodInfoFacade foodInfoFacade;
    
    private FoodDescription current;
    //private DataModel items = null;  
    //private PaginationHelper pagination;
    //private int selectedItemIndex;
    //private String foodSearchString;
    
    private List<FoodDescription> foods;
    private List<FoodDescription> filteredFoods;

    public List<FoodDescription> getFoods() {
        if (foods == null) {
            foods = getFacade().findAll();     
        }
        return foods;
    }

    public void setFoods(List<FoodDescription> foods) {
        this.foods = foods;
    }

    public List<FoodDescription> getFilteredFoods() {
        return filteredFoods;
    }

    public void setFilteredFoods(List<FoodDescription> filteredFoods) {
        this.filteredFoods = filteredFoods;
    }
    
    public FoodDescriptionController() {
    }
    
    public FoodDescription getSelected() {
        if (current == null) {
            current = new FoodDescription();
            FoodInfo fi = new FoodInfo();
            current.setFoodInfo(fi);
        }
        return current;
    }
    
    public void setSelected(FoodDescription fd) {
        current = fd;
    }

    private FoodDescriptionFacade getFacade() {
        return ejbFacade;
    }

    public String prepareList() {
        recreateModel();
        return "List?faces-redirect=true";
    }

    public String prepareView() {
        //current = (FoodDescription) getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View?faces-redirect=true";
    }

    public String prepareCreate() {
        current = null;
        getSelected();
        return "Create?faces-redirect=true";
    }
    
    public String create() {
        try {     
            FoodInfo fi = current.getFoodInfo();
            current.setFoodInfo(null);
            ejbFacade.create(current);   
            fi.setNDBNo(current.getNDBNo());
            fi.setFoodDescription(current); 
            foodInfoFacade.create(fi);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("FoodDescriptionCreated"));
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        //current = (FoodDescription) getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit?faces-redirect=true";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("FoodDescriptionUpdated"));
            return "View?faces-redirect=true";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        //current = (FoodDescription) getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreateModel();
        return "List?faces-redirect=true";
    }

    private void performDestroy() {
        try {
            foodInfoFacade.remove(foodInfoFacade.find(current.getNDBNo()));
            ejbFacade.remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("FoodDescriptionDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void recreateModel() {
        foods = null;
        filteredFoods = null;
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(forClass = FoodDescription.class)
    public static class FoodDescriptionControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FoodDescriptionController controller = (FoodDescriptionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "foodDescriptionController");
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
            if (object instanceof FoodDescription) {
                FoodDescription o = (FoodDescription) object;
                return getStringKey(o.getNDBNo());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + FoodDescription.class.getName());
            }
        }
    }
}
