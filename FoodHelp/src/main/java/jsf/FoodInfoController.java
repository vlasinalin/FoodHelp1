package jsf;

import common.Constants;
import ejb.entityBeans.FoodInfo;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import ejb.sessionBeans.FoodInfoFacade;

import java.io.Serializable;
import java.util.Arrays;
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

@ManagedBean(name = "foodInfoController")
@SessionScoped
public class FoodInfoController implements Serializable {

    @EJB
    private ejb.sessionBeans.FoodInfoFacade ejbFacade;
    private String foodSearchString;

    public String getFoodSearchString() {
        return foodSearchString;
    }

    public void setFoodSearchString(String foodSearchString) {
        this.foodSearchString = foodSearchString;
    }

    public FoodInfoController() {
    }
    
    public String applyFilter() {
        return "Create";
    }
    
    public String[] getNutrientNames() {
        return Constants.nutrientNames;
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findRange(new int[]{0, 100}), true);
    }
    
    public SelectItem[] getItemsRdaSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAllRda(), true);
    }
    
    public SelectItem[] getNutrientsSelectOne() {
        int length = Constants.nutrientNames.length;
        SelectItem[] items = new SelectItem[length];
        for (int i=0; i<length; i++) {
            items[i] = new SelectItem(i, Constants.nutrientNames[i]);
        }
        return items;
    }

    @FacesConverter(forClass = FoodInfo.class)
    public static class FoodInfoControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FoodInfoController controller = (FoodInfoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "foodInfoController");
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
            if (object instanceof FoodInfo) {
                FoodInfo o = (FoodInfo) object;
                return getStringKey(o.getNDBNo());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + FoodInfo.class.getName());
            }
        }
    }
}
