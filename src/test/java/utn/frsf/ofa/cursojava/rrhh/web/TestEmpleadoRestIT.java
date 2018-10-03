/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utn.frsf.ofa.cursojava.rrhh.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Cristian
 */
public class TestEmpleadoRestIT {
    
    public TestEmpleadoRestIT() {
    }
    
    private String entidadToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
    
    @Test
    public void testCrearBuscarporNombre() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode empleadoJson = mapper.createObjectNode();
        empleadoJson.put("nombre", "prueba999");
        empleadoJson.put("correoElectronico", "prueba@mail.com");
        empleadoJson.put("cuil", "203040");
        //empleadoJson.put("fecha_ingreso", "2000-01-01");
        empleadoJson.put("hs_trabajadas","10");
        empleadoJson.put("costo_hora","100");
        empleadoJson.put("TIPO_EMPLEADO", "0");
        StringEntity postingString = new StringEntity(empleadoJson.toString());
        HttpPost httpPost = new HttpPost("http://localhost:8080/rrhh-web/api/empleado/");
        httpPost.setEntity(postingString);
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpClient cliente = HttpClients.createDefault();
        CloseableHttpResponse response1 = cliente.execute(httpPost);
        HttpEntity entity1 = response1.getEntity();
        assertEquals(200, response1.getStatusLine().getStatusCode());
        EntityUtils.consume(entity1);
        response1.close();
        
        HttpGet httpget = new HttpGet("http://localhost:8080/rrhh-web/api/empleado/?nombre=prueba999");
        CloseableHttpResponse respBuscarPorNombre = cliente.execute(httpget);
        HttpEntity clientePorNombre = respBuscarPorNombre.getEntity();
        String resultado = entidadToString(clientePorNombre.getContent());
        System.out.println("RESUKLTADO--------------: " + resultado);
        
        // transformar el JSON en un objeto mapper
        JsonNode clienteJsonRespuesta = mapper.readTree(resultado);
        System.out.println("RESPUESTA:" +  clienteJsonRespuesta.get(0).get("nombre").asText().toLowerCase());
        assertEquals(clienteJsonRespuesta.get(0).get("nombre").asText().toLowerCase(),"prueba999");
        EntityUtils.consume(respBuscarPorNombre.getEntity());
        response1.close();
    }
}
    

