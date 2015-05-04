package jsf;

import common.AlgorithmSettings;
import ejb.entityBeans.FoodInfo;
import dataModel.Dish;
import dataModel.SearchAlgorithm;
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

@ManagedBean(name = "adminController")
@SessionScoped
public class AdminController implements Serializable {
    @EJB
    AlgorithmSettings algorithmSettings;
    
    private SearchAlgorithm searchAlgorithm;

    public SearchAlgorithm getSearchAlgorithm() {
        if (searchAlgorithm == null)
            searchAlgorithm = algorithmSettings.getSearchAlgorithm();
        return searchAlgorithm;
    }

    public void setSearchAlgorithm(SearchAlgorithm searchAlgorithm) {
        //System.out.println("set sa " + searchAlgorithm);
        this.searchAlgorithm = searchAlgorithm;
    }
    
    public AdminController() {

    }
    
    public String saveSettings() {
        try {
            //System.out.println("save sa " + searchAlgorithm);
            algorithmSettings.setSearchAlgorithm(searchAlgorithm);
            JsfUtil.addSuccessMessage("Algorithm settings successfully saved");
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Error occured!");
            return null;
        }
    }
    
}
