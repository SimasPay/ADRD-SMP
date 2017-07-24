package com.payment.simaspay.PojoClasses;

import org.json.JSONArray;

public class Payments {

    private String productCategory;

    private JSONArray jsonArray,productsJsonArray;

    public String getIsPLNPrepaid() {
        return isPLNPrepaid;
    }

    public void setIsPLNPrepaid(String isPLNPrepaid) {
        this.isPLNPrepaid = isPLNPrepaid;
    }

    private String isPLNPrepaid;

    private String Nominaltype;

    private boolean ismdn;

    public String getNominaltype() {
        return Nominaltype;
    }

    public void setNominaltype(String nominaltype) {
        Nominaltype = nominaltype;
    }

    public JSONArray getProductsJsonArray() {
        return productsJsonArray;
    }

    public void setProductsJsonArray(JSONArray productsJsonArray) {
        this.productsJsonArray = productsJsonArray;
    }

    private String providerName,productName,productCode,paymentMode,invoiceType,DenomValues,errormessage1,errormessage;

    private int maxLength, minLength;

    public String getErrormessage1() {
        return errormessage1;
    }

    public void setErrormessage1(String errormessage1) {
        this.errormessage1 = errormessage1;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public String getDenomValues() {
        return DenomValues;
    }

    public void setDenomValues(String denomValues) {
        DenomValues = denomValues;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public boolean getIsMDN() {
        return ismdn;
    }

    public void setIsMDN(boolean ismdn) {
        this.ismdn = ismdn;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
}
