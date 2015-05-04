/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entityBeans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "meal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Meal.findMealDatesByPersonId", query = "SELECT distinct m.mealDate FROM Meal m WHERE m.person.userAccountId = :personId ORDER BY m.mealDate DESC"),
    @NamedQuery(name = "Meal.findByPersonId", query = "SELECT m FROM Meal m WHERE m.person.userAccountId = :personId ORDER BY m.mealDate DESC"),
    @NamedQuery(name = "Meal.findByDateAndPersonId", query = "SELECT m FROM Meal m WHERE m.person.userAccountId = :personId AND m.mealDate = :mealDate"),
    @NamedQuery(name = "Meal.findByMealDate", query = "SELECT m FROM Meal m WHERE m.mealDate = :mealDate")})
public class Meal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mealId")
    private Integer mealId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mealQuantity")
    private float mealQuantity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mealDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mealDate;
    @JoinColumn(name = "NDB_No", referencedColumnName = "NDB_No")
    @ManyToOne(optional = false)
    private FoodInfo nDBNo;
    @JoinColumn(name = "personId", referencedColumnName = "userAccountId")
    @ManyToOne(optional = false)
    private Person person;

    public Meal() {
    }

    public Meal(Integer mealId) {
        this.mealId = mealId;
    }

    public Meal(Integer mealId, float mealQuantity, Date mealDate) {
        this.mealId = mealId;
        this.mealQuantity = mealQuantity;
        this.mealDate = mealDate;
    }

    public Integer getMealId() {
        return mealId;
    }

    public void setMealId(Integer mealId) {
        this.mealId = mealId;
    }

    public float getMealQuantity() {
        return mealQuantity;
    }

    public void setMealQuantity(float mealQuantity) {
        this.mealQuantity = mealQuantity;
    }

    public Date getMealDate() {
        if (mealDate == null) {
            mealDate = Calendar.getInstance().getTime();
        }
        return mealDate;
    }

    public void setMealDate(Date mealDate) {
        this.mealDate = mealDate;
    }

    public FoodInfo getFoodInfo() {
        return nDBNo;
    }

    public void setFoodInfo(FoodInfo nDBNo) {
        this.nDBNo = nDBNo;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mealId != null ? mealId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Meal)) {
            return false;
        }
        Meal other = (Meal) object;
        if ((this.mealId == null && other.mealId != null) || (this.mealId != null && !this.mealId.equals(other.mealId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entityBeans.Meal[ mealId=" + mealId + " ]";
    }
    
}
