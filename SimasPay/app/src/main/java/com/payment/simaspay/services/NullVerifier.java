package com.payment.simaspay.services;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
public class NullVerifier implements HostnameVerifier {
    @Override
    public final boolean verify(final String hostname,
                                final SSLSession sslSession) {
       return true;
    }

 }