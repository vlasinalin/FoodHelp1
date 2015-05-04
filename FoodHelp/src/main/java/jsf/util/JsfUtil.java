package jsf.util;

import ejb.entityBeans.FoodGroup;
import ejb.entityBeans.FoodInfo;
import ejb.entityBeans.Nutritionist;
import ejb.entityBeans.Person;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class JsfUtil {

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }
    
    public static <T> T getControllerFromJSFContext(Class<T> controllerClass, String controller) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return (T) facesContext.getApplication().getELResolver()
                .getValue(facesContext.getELContext(), null, controller); 
    }
    
    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            String label = null;
            if (x instanceof FoodInfo )
                label = ((FoodInfo)x).getFoodDescription() != null ? ((FoodInfo)x).getFoodDescription().getShrtDesc() : "null";
            else if (x instanceof FoodGroup)
                label = ((FoodGroup)x).getFdGrpDesc();
            else if (x instanceof Person)
                label = ((Person)x).getFullName();
            else if (x instanceof Nutritionist)
                label = ((Nutritionist)x).getFullName();
            else if (x instanceof Date)
                label = new SimpleDateFormat("yyyy-MM-dd").format((Date)x);
            if (label == null)
                label = x.toString();
            items[i++] = new SelectItem(x, label);
        }
        return items;
    }

    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }
}