/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entityBeans;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "fd_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FoodGroup.findAll", 
        query = "SELECT f FROM FoodGroup f"),
    @NamedQuery(name = "FoodGroup.findByFoodgroupId", 
        query = "SELECT f FROM FoodGroup f WHERE f.foodgroupId = :foodgroupId"),
    @NamedQuery(name = "FoodGroup.findByFdGrpDesc", 
        query = "SELECT f FROM FoodGroup f WHERE f.fdGrpDesc = :fdGrpDesc")})
public class FoodGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "foodgroupId")
    private Integer foodgroupId;
    @Size(max = 60)
    @Column(name = "FdGrp_Desc")
    private String fdGrpDesc;
    @OneToMany(mappedBy = "fdGrpCd")
    private Collection<FoodDescription> foodDescriptionCollection;

    @ManyToMany(mappedBy="exclFoodGroups")
    private Collection<Person> exclPersons;

    public Collection<Person> getExclPersons() {
        return exclPersons;
    }

    public void setExclPersons(Collection<Person> exclPersons) {
        this.exclPersons = exclPersons;
    }
    
    public FoodGroup() {
    }

    public FoodGroup(Integer foodgroupId) {
        this.foodgroupId = foodgroupId;
    }

    public Integer getFoodgroupId() {
        return foodgroupId;
    }

    public void setFoodgroupId(Integer foodgroupId) {
        this.foodgroupId = foodgroupId;
    }

    public String getFdGrpDesc() {
        return fdGrpDesc;
    }

    public void setFdGrpDesc(String fdGrpDesc) {
        this.fdGrpDesc = fdGrpDesc;
    }

    @XmlTransient
    public Collection<FoodDescription> getFoodDescriptionCollection() {
        return foodDescriptionCollection;
    }

    public void setFoodDescriptionCollection(Collection<FoodDescription> foodDescriptionCollection) {
        this.foodDescriptionCollection = foodDescriptionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (foodgroupId != null ? foodgroupId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FoodGroup)) {
            return false;
        }
        FoodGroup other = (FoodGroup) object;
        if ((this.foodgroupId == null && other.foodgroupId != null) || (this.foodgroupId != null && !this.foodgroupId.equals(other.foodgroupId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entityBeans.FoodGroup[ foodgroupId=" + foodgroupId + " ]";
    }
    
}
