/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionBeans;

import ejb.entityBeans.FoodDescription;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author AlinV
 */
@Stateless
public class FoodDescriptionFacade extends AbstractFacade<FoodDescription> {
    @PersistenceContext(unitName = "FoodHelpPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FoodDescriptionFacade() {
        super(FoodDescription.class);
    }
    
    public List<FoodDescription> findRange(int[] range, String foodNameFilter) {
        javax.persistence.Query q = em.createNamedQuery("FoodDescription.findLikeLongDesc");
        q.setParameter("longDesc", "%" + foodNameFilter + "%");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
}
