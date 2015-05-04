/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entityBeans;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "food_des")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FoodDescription.findLikeLongDesc", query = "SELECT f FROM FoodDescription f WHERE f.longDesc LIKE :longDesc"),
    @NamedQuery(name = "FoodDescription.findAll", query = "SELECT f FROM FoodDescription f"),
    @NamedQuery(name = "FoodDescription.findByNDBNo", query = "SELECT f FROM FoodDescription f WHERE f.nDBNo = :nDBNo"),
    @NamedQuery(name = "FoodDescription.findByLongDesc", query = "SELECT f FROM FoodDescription f WHERE f.longDesc = :longDesc"),
    @NamedQuery(name = "FoodDescription.findByShrtDesc", query = "SELECT f FROM FoodDescription f WHERE f.shrtDesc = :shrtDesc")})
public class FoodDescription implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "NDB_No")
    private Integer nDBNo;
    @Size(max = 200)
    @Column(name = "Long_Desc")
    private String longDesc;
    @Size(max = 60)
    @Column(name = "Shrt_Desc")
    private String shrtDesc;
    @Size(max = 100)
    @Column(name = "ComName")
    private String comName;
    @Size(max = 65)
    @Column(name = "ManufacName")
    private String manufacName;
    @Size(max = 135)
    @Column(name = "Ref_Desc")
    private String refDesc;
    
    @JoinColumn(name = "foodgroupId", referencedColumnName = "foodgroupId")
    @ManyToOne
    private FoodGroup fdGrpCd;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "foodDescription")
    private FoodInfo foodInfo;

    public FoodDescription() {
    }

    public FoodDescription(Integer nDBNo) {
        this.nDBNo = nDBNo;
    }

    public Integer getNDBNo() {
        return nDBNo;
    }

    public void setNDBNo(Integer nDBNo) {
        this.nDBNo = nDBNo;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public String getShrtDesc() {
        return shrtDesc;
    }

    public void setShrtDesc(String shrtDesc) {
        this.shrtDesc = shrtDesc;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getManufacName() {
        return manufacName;
    }

    public void setManufacName(String manufacName) {
        this.manufacName = manufacName;
    }

    public String getRefDesc() {
        return refDesc;
    }

    public void setRefDesc(String refDesc) {
        this.refDesc = refDesc;
    }

    public FoodGroup getFoodGroup() {
        return fdGrpCd;
    }

    public void setFoodGroup(FoodGroup fdGrpCd) {
        this.fdGrpCd = fdGrpCd;
    }

    public FoodInfo getFoodInfo() {
        return foodInfo;
    }

    public void setFoodInfo(FoodInfo foodInfo) {
        this.foodInfo = foodInfo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nDBNo != null ? nDBNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FoodDescription)) {
            return false;
        }
        FoodDescription other = (FoodDescription) object;
        if (this.nDBNo != other.nDBNo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entityBeans.FoodDescription[ nDBNo=" + nDBNo + " ]";
    }
    
}
