package mx.gob.se.firma;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

public class ClientWebServiceRestful {
    
    public static String callRestfulWebService(URL url, String content) throws Exception {
        return callRestfulWebService(url, content, null, 0);
    }

    public static String callRestfulWebService(URL url, String content, String proxy, int port) throws Exception {
        Proxy proxyObject = null;
        if (proxy != null && port > 0) {
            InetSocketAddress proxyAddress = new InetSocketAddress(proxy, port);
            proxyObject = new Proxy(Proxy.Type.HTTP, proxyAddress);
        }
        System.out.println("construyeConexionPost");
        // CREA CONEXION POST
        URLConnection urlc = construyeConexionPost(proxyObject, url);

        System.out.println("adicionaQueryAConexionPost");
        // ADDICIONA QUERY A CONEXION POST
        adicionaQueryAConexionPost(urlc, content);

        // INVOCA SERVICIO
        System.out.println("invocaServicioPost");
        return invocaServicioPost(urlc);
    }

    // CREA CONEXION POST
    private static URLConnection construyeConexionPost(Proxy proxyObject, URL url) throws Exception {
        URLConnection urlc = null;
        try {
            if (proxyObject == null) {
                urlc = url.openConnection();
            } else {
                urlc = url.openConnection(proxyObject);
            }
            urlc.setDoOutput(true);
            urlc.setAllowUserInteraction(false);
        } catch (IOException ex) {
            throw new Exception("No fue posible conectarse al servicio: " + url, ex);
        }
        return urlc;
    }

    // ADDICIONA QUERY A CONEXION POST
    private static void adicionaQueryAConexionPost(URLConnection urlc, String content) throws Exception {
        PrintStream ps;
        System.out.println("adicionaQueryAConexionPost--- content: "+content);
        try {
            if (content != null) {
                ps = new PrintStream(urlc.getOutputStream());
                ps.print(content);
                ps.close();
            }
        } catch (IOException ex) {
            throw new Exception("Error adicionar el contenido ["+content+"]: " + ex.getMessage() , ex);
        }
    }
    // INVOCA SERVICIO GET

    public static String invocaServicioGet(URL urlWebservice) throws Exception {
        String response = "";
        BufferedReader in = null;
        InputStreamReader inputStream = null;
        try {
            in = new BufferedReader(new InputStreamReader(urlWebservice.openStream()));
            inputStream = new InputStreamReader(urlWebservice.openStream(), "UTF-8");
            BufferedReader br = new BufferedReader(inputStream);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                if (sb.length() > 0) {
                    sb.append("\n");
                }
                sb.append(line);
            }
            response = new String(sb.toString().getBytes("ISO-8859-1"), "ISO-8859-1");
            return response;
        } catch (IOException ex) {
            throw new Exception("Error al invocar el servicio: " + urlWebservice, ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                throw new Exception("Error no determinado: " + ex.getMessage(), ex);
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
                throw new Exception("Error no determinado: " + ex.getMessage(), ex);
            }
        }
    }

    // INVOCA SERVICIO POST
    private static String invocaServicioPost(URLConnection urlc) throws Exception {
        System.out.println("invocaServicioPost--- Inicio");
        String response = "";
        BufferedReader br = null;
        InputStream inputStream = null;
        try {
            System.out.println("invocaServicioPost--- inputStream");
            inputStream = urlc.getInputStream();
            System.out.println("invocaServicioPost--- inputStreamReader");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            br = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                if (sb.length() > 0) {
                    sb.append("\n");
                }
                sb.append(line);
            }
            System.out.println("invocaServicioPost--- response");
            response = new String(sb.toString().getBytes("ISO-8859-1"), "ISO-8859-1");
            return response;
        } catch (Exception ex) {
            throw new Exception("Error al enviar o recibir la informacion al servicio: " + ex.getMessage(), ex);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                throw new Exception("Error no determinado: "  + ex.getMessage(), ex);
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
                throw new Exception("Error no determinado: "  + ex.getMessage(), ex);
            }
            System.out.println("invocaServicioPost--- fin");
        }
    }
}
