/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.rgm.adm.util;

/**
 *
 * @author jjolon
 */
public class signatureInfo {
    
     String signText;
    String graphicSignature;
    String keyFile;
    String keyPassword;
    String reason;
    String location;
    Integer llx;
    Integer lly;
    Integer urx;
    Integer ury;
    Integer signPage;
    String fieldName;
    String typeDocument;

    public String getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }
    byte[] document;

    public signatureInfo() {
    }

    public String getSignText() {
        return signText;
    }

    public void setSignText(String signText) {
        this.signText = signText;
    }

    public String getGraphicSignature() {
        return graphicSignature;
    }

    public void setGraphicSignature(String graphicSignature) {
        this.graphicSignature = graphicSignature;
    }

    public String getKeyFile() {
        return keyFile;
    }

    public void setKeyFile(String keyFile) {
        this.keyFile = keyFile;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getLlx() {
        return llx;
    }

    public void setLlx(Integer llx) {
        this.llx = llx;
    }

    public Integer getLly() {
        return lly;
    }

    public void setLly(Integer lly) {
        this.lly = lly;
    }

    public Integer getUrx() {
        return urx;
    }

    public void setUrx(Integer urx) {
        this.urx = urx;
    }

    public Integer getUry() {
        return ury;
    }

    public void setUry(Integer ury) {
        this.ury = ury;
    }

    public Integer getSignPage() {
        return signPage;
    }

    public void setSignPage(Integer signPage) {
        this.signPage = signPage;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public byte[] getDocument() {
        return document;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }
    
}
