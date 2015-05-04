package jsf;

import ejb.entityBeans.FoodGroup;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import ejb.sessionBeans.FoodGroupFacade;

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
import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

@ManagedBean(name = "foodGroupController")
@SessionScoped
public class FoodGroupController implements Serializable {

    private FoodGroup current;
    private DataModel items = null;
    @EJB
    private ejb.sessionBeans.FoodGroupFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public FoodGroupController() {
    }

    public FoodGroup getSelected() {
        if (current == null) {
            current = new FoodGroup();
            selectedItemIndex = -1;
        }
        return current;
    }

    private FoodGroupFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(30) {
                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareCreate() {
        current = new FoodGroup();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("FoodGroupCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (FoodGroup) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("FoodGroupUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (FoodGroup) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("FoodGroupDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
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

    /*public SelectItem[] getItemsAvailableSelectMany(boolean excludeRecommGroup) {
        List<FoodGroup> groups = ejbFacade.findAll();      
        if (excludeRecommGroup) {
            groups.remove(groups.size()-1);
        }
        for (FoodGroup fg : groups) {
            fg.setExclPersons(null);
        }
        return JsfUtil.getSelectItems(groups, false);
    }*/

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(value = "pickListFoodGroupConverter")
    public static class PickListFoodGroupConverter implements Converter {
        @Override
        public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
            Object ret = null;
            if (arg1 instanceof PickList) {
                Object dualList = ((PickList) arg1).getValue();
                DualListModel dl = (DualListModel) dualList;
                for (Object o : dl.getSource()) {
                    String id = "" + ((FoodGroup) o).getFoodgroupId();
                    if (arg2.equals(id)) {
                        ret = o;
                        break;
                    }
                }
                if (ret == null)
                    for (Object o : dl.getTarget()) {
                        String id = "" + ((FoodGroup) o).getFoodgroupId();
                        if (arg2.equals(id)) {
                            ret = o;
                            break;
                        }
                    }
            }
            return ret;
        }

        @Override
        public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {          
            String str = "";
            if (arg2 instanceof FoodGroup) {
                str = "" + ((FoodGroup) arg2).getFoodgroupId();
            }
            return str;
        }
    }
    
    @FacesConverter(forClass = FoodGroup.class)
    public static class FoodGroupControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FoodGroupController controller = (FoodGroupController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "foodGroupController");
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
            if (object instanceof FoodGroup) {
                FoodGroup o = (FoodGroup) object;
                return getStringKey(o.getFoodgroupId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + FoodGroup.class.getName());
            }
        }
    }
}
