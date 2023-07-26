package mx.gob.se.rug.masiva.to;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
	    "bienEspecial"
	})
@XmlRootElement(name = "bienes-especiales")
public class BienesEspeciales {

	@XmlElement(name = "bien-especial", required = true)
    protected List<BienEspecial> bienEspecial;

	public List<BienEspecial> getBienEspecial() {
		if (bienEspecial == null) {
			bienEspecial = new ArrayList<BienEspecial>();
        }
		return bienEspecial;
	}

	public void setBienEspecial(List<BienEspecial> bienEspecial) {
		this.bienEspecial = bienEspecial;
	}
		
}
