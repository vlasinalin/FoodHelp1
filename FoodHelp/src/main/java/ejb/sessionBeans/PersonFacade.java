/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionBeans;

import ejb.entityBeans.Person;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author AlinV
 */
@Stateless
public class PersonFacade extends AbstractFacade<Person> {
    @PersistenceContext(unitName = "FoodHelpPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PersonFacade() {
        super(Person.class);
    }
    
    public List<Person> findForNutritionist(int nutritionistId, int range[]) {       
        javax.persistence.Query q = em.createNamedQuery("Person.findByNutritionistId");
        q.setParameter("nutritionistId", nutritionistId);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
    public Person getPerson(Integer personId) {
        if (personId == null || personId <= 0) {
            return null;
        }
        Person person = find(personId);
        return person;
    }
}
