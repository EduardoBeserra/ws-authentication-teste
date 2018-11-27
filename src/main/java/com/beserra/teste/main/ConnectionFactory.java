package com.beserra.teste.main;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.sun.jersey.client.urlconnection.HttpURLConnectionFactory;

public class ConnectionFactory implements HttpURLConnectionFactory {

    private Proxy proxy;
    private SSLContext sslContext;
    private boolean haProxy;

    public ConnectionFactory() {
    }

    public ConnectionFactory(String proxyHost, Integer proxyPort) {
    	this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
    	this.haProxy = true;
    }


    public HttpURLConnection getHttpURLConnection(URL url) throws IOException {
    	HttpURLConnection con;
    	if(haProxy)
    		con = (HttpURLConnection) url.openConnection(proxy);
    	else
    		con = (HttpURLConnection) url.openConnection();
    	
        if (con instanceof HttpsURLConnection) {
        	HttpsURLConnection httpsCon;
        	if(haProxy)
        		httpsCon = (HttpsURLConnection) url.openConnection(proxy);
        	else
        		httpsCon = (HttpsURLConnection) url.openConnection();
        	
            httpsCon.setHostnameVerifier(getHostnameVerifier());
            httpsCon.setSSLSocketFactory(getSslContext().getSocketFactory());
            return httpsCon;
        } else {
            return con;
        }

    }

    public SSLContext getSslContext() {
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{new SecureTrustManager()}, new SecureRandom());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sslContext;
    }

    private HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
            public boolean verify(String hostname,
                    javax.net.ssl.SSLSession sslSession) {
                return true;
            }
        };
    }

}