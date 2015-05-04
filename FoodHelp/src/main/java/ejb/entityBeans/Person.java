/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entityBeans;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "person")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p"),
    @NamedQuery(name = "Person.findByPersonId", query = "SELECT p FROM Person p WHERE p.userAccountId = :userAccountId"),
    @NamedQuery(name = "Person.findByNutritionistId", query = "SELECT p FROM Person p WHERE p.nutritionist.userAccountId = :nutritionistId"),
    @NamedQuery(name = "Person.findByFullName", query = "SELECT p FROM Person p WHERE p.fullName = :fullName")})
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "userAccountId")
    private Integer userAccountId;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "fullName")
    private String fullName;
    
    @Basic(optional = false)
    @Size(min = 1, max = 500)
    @Column(name = "description")
    private String description;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "age")
    private Integer age;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "gender")
    private Boolean gender;
    
    @JoinColumn(name = "userAccountId", referencedColumnName = "userAccountId", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private UserAccount userAccount;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
    private Collection<Meal> mealCollection;

    @JoinColumn(name = "rdaId", referencedColumnName = "NDB_No")
    @ManyToOne(optional = false)
    private FoodInfo rdaFoodInfo;
    
    @JoinColumn(name = "nutritionistId", referencedColumnName = "userAccountId")
    @ManyToOne(optional = false)
    private Nutritionist nutritionist;

    @ManyToMany
    @JoinTable(name="person_excluded_foodgroup",
        joinColumns=
            @JoinColumn(name="personId", referencedColumnName="userAccountId"),
        inverseJoinColumns=
            @JoinColumn(name="foodgroupId", referencedColumnName="foodgroupId")
        )
    
    private Collection<FoodGroup> exclFoodGroups;

    public Collection<FoodGroup> getExclFoodGroups() {
        return exclFoodGroups;
    }

    public void setExclFoodGroups(Collection<FoodGroup> exclFoodGroups) {
        this.exclFoodGroups = exclFoodGroups;
    }
    
    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public Nutritionist getNutritionist() {
        return nutritionist;
    }

    public void setNutritionist(Nutritionist nutritionist) {
        this.nutritionist = nutritionist;
    }

    public FoodInfo getRdaFoodInfo() {
        return rdaFoodInfo;
    }

    public void setRdaFoodInfo(FoodInfo rdaFoodInfo) {
        this.rdaFoodInfo = rdaFoodInfo;
    }
    
    public Person() {
    }

    public Person(Integer userAccountId) {
        this.userAccountId = userAccountId;
    }

    public Person(Integer userAccountId, String fullName) {
        this.userAccountId = userAccountId;
        this.fullName = fullName;
    }

    public Integer getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Integer userAccountId) {
        this.userAccountId = userAccountId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getGenderString() {
        return gender ? "Male" : "Female";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    @XmlTransient
    public Collection<Meal> getMealCollection() {
        return mealCollection;
    }

    public void setMealCollection(Collection<Meal> mealCollection) {
        this.mealCollection = mealCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userAccountId != null ? userAccountId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        if ((this.userAccountId == null && other.userAccountId != null) || (this.userAccountId != null && !this.userAccountId.equals(other.userAccountId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entityBeans.Person[ userAccountId=" + userAccountId + " ]";
    }
    
}
