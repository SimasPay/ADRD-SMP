package com.payment.simaspay.PojoClasses;

import org.json.JSONArray;

/**
 * Created by Nagendra P on 3/15/2016.
 */
public class Payments {

    String productCategory;

    JSONArray jsonArray,productsJsonArray;

    public String getIsPLNPrepaid() {
        return isPLNPrepaid;
    }

    public void setIsPLNPrepaid(String isPLNPrepaid) {
        this.isPLNPrepaid = isPLNPrepaid;
    }

    String isPLNPrepaid;

    String Nominaltype;

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

    String providerName,productName,productCode,paymentMode,invoiceType,DenomValues;

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
