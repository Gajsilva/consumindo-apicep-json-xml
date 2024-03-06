package com.br.response;

import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.*;
import lombok.Data;

@Data
@XmlRootElement(name ="xmlcep")
@XmlAccessorType(XmlAccessType.FIELD)
public class CepResponse {

    @XmlElement
    private String cep;
    @XmlElement
        private String localidade;
    @XmlElement
        private String uf;

}
