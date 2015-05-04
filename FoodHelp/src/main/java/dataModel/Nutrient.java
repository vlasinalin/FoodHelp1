
package dataModel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "nutrient")
public class Nutrient {
    public String name;
    public Double value;
}
