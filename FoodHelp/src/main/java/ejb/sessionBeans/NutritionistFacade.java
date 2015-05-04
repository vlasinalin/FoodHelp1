/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionBeans;

import ejb.entityBeans.Nutritionist;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Alinv
 */
@Stateless
public class NutritionistFacade extends AbstractFacade<Nutritionist> {
    @PersistenceContext(unitName = "FoodHelpPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NutritionistFacade() {
        super(Nutritionist.class);
    }
    
}
