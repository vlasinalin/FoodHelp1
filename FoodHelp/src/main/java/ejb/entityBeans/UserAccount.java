/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entityBeans;

import dataModel.Group;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author AlinV
 */
@Entity
@Table(name = "userAccount")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserAccount.findAll", query = "SELECT ua FROM UserAccount ua"),
    @NamedQuery(name = "UserAccount.findByUsername", query = "SELECT ua FROM UserAccount ua WHERE ua.username = :username")})
public class UserAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "userAccountId")
    private Integer userAccountId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "username")
    private String username;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "password")
    private String password;   
    
    @Size(min = 1, max = 45)
    @Column(name = "email")
    private String email;

    //@OneToOne(cascade = CascadeType.ALL, mappedBy = "userAccount")
    //private Person person;
    
    //@OneToOne(cascade = CascadeType.ALL, mappedBy = "userAccount")
    //private Nutritionist nutritionist;
    
    //@OneToOne(cascade = CascadeType.ALL, mappedBy = "userAccount")
    //private UserGroup userGroup;

    @ElementCollection(targetClass = Group.class)
    @CollectionTable(name = "usergroup", 
                    joinColumns       = @JoinColumn(name = "username", nullable=false), 
                    uniqueConstraints = { @UniqueConstraint(columnNames={"username","groupname"}) } )
    @Enumerated(EnumType.STRING)
    @Column(name="groupname", length=45, nullable=false)
    private List<Group> groups;

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public UserAccount() {
    }

    public UserAccount(Integer userAccountId) {
        this.userAccountId = userAccountId;
    }

    public UserAccount(Integer userAccountId, String username, String password) {
        this.userAccountId = userAccountId;
        this.username = username;
        this.password = password;
    }

    public Integer getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Integer userAccountId) {
        this.userAccountId = userAccountId;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        if (!(object instanceof UserAccount)) {
            return false;
        }
        UserAccount other = (UserAccount) object;
        if ((this.userAccountId == null && other.userAccountId != null) || (this.userAccountId != null && !this.userAccountId.equals(other.userAccountId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entityBeans.UserAccount[ userAccountId=" + userAccountId + " ]";
    }
    
}
