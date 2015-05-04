package jsf;

import common.*;
import dataModel.Group;
import ejb.entityBeans.FoodInfo;
import ejb.entityBeans.Nutritionist;
import ejb.entityBeans.Person;
import ejb.entityBeans.UserAccount;
import ejb.entityBeans.UserGroup;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import ejb.sessionBeans.UserAccountFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;

@ManagedBean(name = "userAccountController")
@SessionScoped
public class UserAccountController implements Serializable {
    
    private UserAccount current;
    private Group userGroup;
    private String userFullName;
    private String personDescription;
    private Boolean personGender;
    private Integer personAge;

    public String getPersonDescription() {
        return personDescription;
    }

    public void setPersonDescription(String personDescription) {
        this.personDescription = personDescription;
    }

    public Boolean getPersonGender() {
        return personGender;
    }

    public void setPersonGender(Boolean personGender) {
        this.personGender = personGender;
    }

    public Integer getPersonAge() {
        return personAge;
    }

    public void setPersonAge(Integer personAge) {
        this.personAge = personAge;
    }

    public Group getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(Group userGroup) {
        this.userGroup = userGroup;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    @EJB
    private ejb.sessionBeans.UserAccountFacade ejbFacade;
    @EJB
    private ejb.sessionBeans.NutritionistFacade nutritionistFacade;
    @EJB
    private ejb.sessionBeans.PersonFacade personFacade;
    @EJB
    private ejb.sessionBeans.UserGroupFacade userGroupFacade;

    public UserAccountController() {
    }

    public UserAccount getSelected() {
        if (current == null) {
            current = new UserAccount();
        }
        return current;
    }
    
    private UserAccountFacade getFacade() {
        return ejbFacade;
    }

    public HttpServletRequest getRequest() {
        return (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }
    
    public String login() {
        HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if(req.getUserPrincipal() == null){
            try {
                req.login(current.getUsername(), current.getPassword());
                current = getFacade().findByUsername(current.getUsername());
                req.getServletContext().log("FoodHelp: successfully logged in " + current.getUsername());
            } 
            catch (ServletException e) {
                e.printStackTrace();
                JsfUtil.addErrorMessage("Wrong username or password");
                current = null;
                return null;
            }
        }
        else {
            req.getServletContext().log("Skip logged because already logged in: "+ current.getUsername());
        }
        if (req.isUserInRole(Group.ADMINISTRATOR.toString())) {
            return "ViewAdmin";      
        }
        else if (req.isUserInRole(Group.NUTRITIONIST.toString())) {
            return "ViewNutritionist";      
        }
        else if (req.isUserInRole(Group.USER.toString())) {
            return "ViewPerson";      
        }
        return "/index?faces-redirect=true";
    }
    
    public String logout() {   
        HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        try {
            req.logout();
            req.getSession().invalidate();
            current = null;
            JsfUtil.addSuccessMessage("Logout successfull");
        } catch (ServletException e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("Logout error occured");
        }
        return "/index?faces-redirect=true";
    }

    public String register() {
        HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (current.getPassword().length() < 4) {
            JsfUtil.addErrorMessage("Password must have at least 4 characters");
            current = null;
            return null;
        }
        String unencPassword = current.getPassword();
        saveUser(current);      
        current.setPassword(unencPassword);
        return login();     
    }
    
    private void saveUser(UserAccount userAccount) {
        userAccount.setPassword(DigestUtils.sha512Hex(userAccount.getPassword()));
        Group group = Group.values()[userGroup.ordinal()];
        getFacade().create(userAccount);
        switch (group)
        {
            case NUTRITIONIST:
                Nutritionist nutritionist = new Nutritionist(userAccount.getUserAccountId());
                nutritionist.setFullName(userFullName);
                nutritionistFacade.create(nutritionist);
                break;                   
            case USER:
                Person person = new Person(userAccount.getUserAccountId());
                person.setFullName(userFullName);
                person.setAge(personAge);
                person.setDescription(personDescription);
                person.setGender(personGender);
                person.setRdaFoodInfo(new FoodInfo(Constants.DefaultRdaDbId));
                person.setNutritionist(nutritionistFacade.find(Constants.DefaultNutritionistDbId));
                personFacade.create(person);
                break;
        }
        UserGroup userGroup = new UserGroup(userAccount.getUsername(), group.toString());
        userGroup.setUserAccount(userAccount);
        userGroupFacade.create(userGroup); 
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }
    
    public SelectItem[] getItemsForSelectGroup() {
        List<Group> userGroups = new ArrayList<Group>();
        userGroups.add(Group.NUTRITIONIST);
        userGroups.add(Group.USER);
        return JsfUtil.getSelectItems(userGroups, true);
    }

    @FacesConverter(forClass = UserAccount.class)
    public static class UserAccountControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserAccountController controller = (UserAccountController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userAccountController");
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
            if (object instanceof UserAccount) {
                UserAccount o = (UserAccount) object;
                return getStringKey(o.getUserAccountId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + UserAccount.class.getName());
            }
        }
    }
}
