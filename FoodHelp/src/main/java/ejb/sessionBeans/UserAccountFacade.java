/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionBeans;

import ejb.entityBeans.FoodInfo;
import ejb.entityBeans.Nutritionist;
import ejb.entityBeans.Person;
import ejb.entityBeans.UserAccount;
import ejb.entityBeans.UserGroup;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
/**
 *
 * @author Alinv
 */
@Stateless
public class UserAccountFacade extends AbstractFacade<UserAccount> {
    @PersistenceContext(unitName = "FoodHelpPU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserAccountFacade() {
        super(UserAccount.class);
    }
    
    public UserAccount findByUsername(String username) {
        javax.persistence.Query q = em.createNamedQuery("UserAccount.findByUsername");
        q.setParameter("username", username);
        q.setMaxResults(1);
        Object result = q.getSingleResult();
        if ((result != null) && (result instanceof UserAccount)) {
            return (UserAccount)result;
        }
        return null;
    }
    
}
