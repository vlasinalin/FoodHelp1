
package ejb.entityBeans;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.criteria.Fetch;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "abbrev")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FoodInfo.findAll", query = "SELECT f FROM FoodInfo f WHERE f.rda = 0"),
    @NamedQuery(name = "FoodInfo.findByNDBNo", query = "SELECT f FROM FoodInfo f WHERE f.nDBNo = :nDBNo"),
    @NamedQuery(name = "FoodInfo.findAllRda", query = "SELECT f FROM FoodInfo f WHERE f.rda = 1"),
})
public class FoodInfo implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "NDB_No")
    private Integer nDBNo;
    
    @Basic(optional = false)
    @Column(name = "RDA")
    private boolean rda;
    
    @Column(name = "Water_g")
    private Double waterg;
    @Column(name = "Energ_Kcal")
    private Integer energKcal;
    @Column(name = "Protein_g")
    private Double proteing;
    @Column(name = "Lipid_Tot_g")
    private Double lipidTotg;
    @Column(name = "Ash_g")
    private Double ashg;
    @Column(name = "Carbohydrt_g")
    private Double carbohydrtg;
    @Column(name = "Fiber_TD_g")
    private Double fiberTDg;
    @Column(name = "Sugar_Tot_g")
    private Double sugarTotg;
    @Column(name = "Calcium_mg")
    private Integer calciummg;
    @Column(name = "Iron_mg")
    private Double ironmg;
    @Column(name = "Magnesium_mg")
    private Double magnesiummg;
    @Column(name = "Phosphorus_mg")
    private Integer phosphorusmg;
    @Column(name = "Potassium_mg")
    private Integer potassiummg;
    @Column(name = "Sodium_mg")
    private Integer sodiummg;
    @Column(name = "Zinc_mg")
    private Double zincmg;
    @Column(name = "Copper_mg")
    private Double coppermg;
    @Column(name = "Manganese_mg")
    private Double manganesemg;
    @Column(name = "Selenium_ug")
    private Double seleniumug;
    @Column(name = "Vit_C_mg")
    private Double vitCmg;
    @Column(name = "Thiamin_mg")
    private Double thiaminmg;
    @Column(name = "Riboflavin_mg")
    private Double riboflavinmg;
    @Column(name = "Niacin_mg")
    private Double niacinmg;
    @Column(name = "Panto_Acid_mg")
    private Double pantoAcidmg;
    @Column(name = "Vit_B6_mg")
    private Double vitB6mg;
    @Column(name = "Folate_Tot_ug")
    private Double folateTotug;
    @Column(name = "Choline_Tot_mg")
    private Double cholineTotmg;
    @Column(name = "Vit_B12_ug")
    private Double vitB12ug;
    @Column(name = "Vit_A_IU")
    private Integer vitAIU;
    @Column(name = "Vit_E_mg")
    private Double vitEmg;
    @Column(name = "Vit_D_ug")
    private Double vitDug;
    @Column(name = "Vit_K_ug")
    private Double vitKug;
    @Column(name = "FA_Sat_g")
    private Double fASatg;
    @Column(name = "FA_Mono_g")
    private Double fAMonog;
    @Column(name = "FA_Poly_g")
    private Double fAPolyg;
    @Column(name = "Cholestrl_mg")
    private Integer cholestrlmg;
    
    @JoinColumn(name = "NDB_No", referencedColumnName = "NDB_No", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private FoodDescription foodDescription;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nDBNo", fetch = FetchType.LAZY)
    private Collection<Meal> mealCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rdaFoodInfo", fetch = FetchType.LAZY)
    private Collection<Person> rdaPersons;

    public Collection<Person> getRdaPersons() {
        return rdaPersons;
    }

    public void setRdaPersons(Collection<Person> rdaPersons) {
        this.rdaPersons = rdaPersons;
    }
    
    public FoodInfo() {
    }

    public FoodInfo(Integer nDBNo) {
        this.nDBNo = nDBNo;
    }

    public Integer getNDBNo() {
        return nDBNo;
    }

    public void setNDBNo(Integer nDBNo) {
        this.nDBNo = nDBNo;
    }

    public Double getWaterg() {
        return waterg;
    }

    public void setWaterg(Double waterg) {
        this.waterg = waterg;
    }

    public Integer getEnergKcal() {
        return energKcal;
    }

    public void setEnergKcal(Integer energKcal) {
        this.energKcal = energKcal;
    }

    public Double getProteing() {
        return proteing;
    }

    public void setProteing(Double proteing) {
        this.proteing = proteing;
    }

    public Double getLipidTotg() {
        return lipidTotg;
    }

    public void setLipidTotg(Double lipidTotg) {
        this.lipidTotg = lipidTotg;
    }

    public Double getAshg() {
        return ashg;
    }

    public void setAshg(Double ashg) {
        this.ashg = ashg;
    }

    public Double getCarbohydrtg() {
        return carbohydrtg;
    }

    public void setCarbohydrtg(Double carbohydrtg) {
        this.carbohydrtg = carbohydrtg;
    }

    public Double getFiberTDg() {
        return fiberTDg;
    }

    public void setFiberTDg(Double fiberTDg) {
        this.fiberTDg = fiberTDg;
    }

    public Double getSugarTotg() {
        return sugarTotg;
    }

    public void setSugarTotg(Double sugarTotg) {
        this.sugarTotg = sugarTotg;
    }

    public Integer getCalciummg() {
        return calciummg;
    }

    public void setCalciummg(Integer calciummg) {
        this.calciummg = calciummg;
    }

    public Double getIronmg() {
        return ironmg;
    }

    public void setIronmg(Double ironmg) {
        this.ironmg = ironmg;
    }

    public Double getMagnesiummg() {
        return magnesiummg;
    }

    public void setMagnesiummg(Double magnesiummg) {
        this.magnesiummg = magnesiummg;
    }

    public Integer getPhosphorusmg() {
        return phosphorusmg;
    }

    public void setPhosphorusmg(Integer phosphorusmg) {
        this.phosphorusmg = phosphorusmg;
    }

    public Integer getPotassiummg() {
        return potassiummg;
    }

    public void setPotassiummg(Integer potassiummg) {
        this.potassiummg = potassiummg;
    }

    public Integer getSodiummg() {
        return sodiummg;
    }

    public void setSodiummg(Integer sodiummg) {
        this.sodiummg = sodiummg;
    }

    public Double getZincmg() {
        return zincmg;
    }

    public void setZincmg(Double zincmg) {
        this.zincmg = zincmg;
    }

    public Double getCoppermg() {
        return coppermg;
    }

    public void setCoppermg(Double coppermg) {
        this.coppermg = coppermg;
    }

    public Double getManganesemg() {
        return manganesemg;
    }

    public void setManganesemg(Double manganesemg) {
        this.manganesemg = manganesemg;
    }

    public Double getSeleniumug() {
        return seleniumug;
    }

    public void setSeleniumug(Double seleniumug) {
        this.seleniumug = seleniumug;
    }

    public Double getVitCmg() {
        return vitCmg;
    }

    public void setVitCmg(Double vitCmg) {
        this.vitCmg = vitCmg;
    }

    public Double getThiaminmg() {
        return thiaminmg;
    }

    public void setThiaminmg(Double thiaminmg) {
        this.thiaminmg = thiaminmg;
    }

    public Double getRiboflavinmg() {
        return riboflavinmg;
    }

    public void setRiboflavinmg(Double riboflavinmg) {
        this.riboflavinmg = riboflavinmg;
    }

    public Double getNiacinmg() {
        return niacinmg;
    }

    public void setNiacinmg(Double niacinmg) {
        this.niacinmg = niacinmg;
    }

    public Double getPantoAcidmg() {
        return pantoAcidmg;
    }

    public void setPantoAcidmg(Double pantoAcidmg) {
        this.pantoAcidmg = pantoAcidmg;
    }

    public Double getVitB6mg() {
        return vitB6mg;
    }

    public void setVitB6mg(Double vitB6mg) {
        this.vitB6mg = vitB6mg;
    }

    public Double getFolateTotug() {
        return folateTotug;
    }

    public void setFolateTotug(Double folateTotug) {
        this.folateTotug = folateTotug;
    }

    public Double getCholineTotmg() {
        return cholineTotmg;
    }

    public void setCholineTotmg(Double cholineTotmg) {
        this.cholineTotmg = cholineTotmg;
    }

    public Double getVitB12ug() {
        return vitB12ug;
    }

    public void setVitB12ug(Double vitB12ug) {
        this.vitB12ug = vitB12ug;
    }

    public Integer getVitAIU() {
        return vitAIU;
    }

    public void setVitAIU(Integer vitAIU) {
        this.vitAIU = vitAIU;
    }

    public Double getVitEmg() {
        return vitEmg;
    }

    public void setVitEmg(Double vitEmg) {
        this.vitEmg = vitEmg;
    }

    public Double getVitDug() {
        return vitDug;
    }

    public void setVitDug(Double vitDug) {
        this.vitDug = vitDug;
    }

    public Double getVitKug() {
        return vitKug;
    }

    public void setVitKug(Double vitKug) {
        this.vitKug = vitKug;
    }

    public Double getFASatg() {
        return fASatg;
    }

    public void setFASatg(Double fASatg) {
        this.fASatg = fASatg;
    }

    public Double getFAMonog() {
        return fAMonog;
    }

    public void setFAMonog(Double fAMonog) {
        this.fAMonog = fAMonog;
    }

    public Double getFAPolyg() {
        return fAPolyg;
    }

    public void setFAPolyg(Double fAPolyg) {
        this.fAPolyg = fAPolyg;
    }

    public Integer getCholestrlmg() {
        return cholestrlmg;
    }

    public void setCholestrlmg(Integer cholestrlmg) {
        this.cholestrlmg = cholestrlmg;
    }

    public FoodDescription getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(FoodDescription foodDescription) {
        this.foodDescription = foodDescription;
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
        if (!(object instanceof FoodInfo)) {
            return false;
        }
        FoodInfo other = (FoodInfo) object;
        if (this.nDBNo != other.nDBNo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entityBeans.FoodInfo[ nDBNo=" + nDBNo + " ]";
    }
    
}
