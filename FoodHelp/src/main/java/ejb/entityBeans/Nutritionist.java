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
@Table(name = "nutritionist")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Nutritionist.findAll", query = "SELECT n FROM Nutritionist n")})
public class Nutritionist implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "userAccountId")
    private Integer userAccountId;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "fullName")
    private String fullName;
    
    @JoinColumn(name = "userAccountId", referencedColumnName = "userAccountId", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private UserAccount userAccount;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nutritionist")
    private Collection<Person> persons;

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public Collection<Person> getPersons() {
        return persons;
    }

    public void setPersons(Collection<Person> persons) {
        this.persons = persons;
    }
    
    public Nutritionist() {
    }

    public Nutritionist(Integer userAccountId) {
        this.userAccountId = userAccountId;
    }

    public Nutritionist(Integer userAccountId, String fullName) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userAccountId != null ? userAccountId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Nutritionist)) {
            return false;
        }
        Nutritionist other = (Nutritionist) object;
        if ((this.userAccountId == null && other.userAccountId != null) || (this.userAccountId != null && !this.userAccountId.equals(other.userAccountId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entityBeans.Nutritionist[ userAccountId=" + userAccountId + " ]";
    }
    
}
