package com.cnsunrun.common.quest;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class SSLSocketFactoryEx extends SSLSocketFactory {
    SSLContext sslContext;

    public static void setSSLSocketFactory(AsyncHttpClient client, SyncHttpClient synClient) {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            client.setSSLSocketFactory(new SSLSocketFactoryEx(trustStore));
            synClient.setSSLSocketFactory(new SSLSocketFactoryEx(trustStore));
        } catch (KeyManagementException var3) {
            var3.printStackTrace();
        } catch (UnrecoverableKeyException var4) {
            var4.printStackTrace();
        } catch (NoSuchAlgorithmException var5) {
            var5.printStackTrace();
        } catch (KeyStoreException var6) {
            var6.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public SSLSocketFactoryEx() throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        this(KeyStore.getInstance(KeyStore.getDefaultType()));
    }

    public SSLSocketFactoryEx(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);
//        this.sslContext = SSLContext.getInstance("TLS");
        this.sslContext = SSLContext.getInstance("SSL");
        setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        TrustManager tm = new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        };
        this.sslContext.init((KeyManager[])null, new TrustManager[]{tm}, (SecureRandom)null);
    }

    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return this.sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    public Socket createSocket() throws IOException {
        return this.sslContext.getSocketFactory().createSocket();
    }
}
