package com.beserra.teste.main;

import java.net.URI;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;

public class ServicoRest  {

	/*Para teste*/
	public static void main(String[] args) {
	}
	
	public String executeGet(String dados) {

		try {
			JsonObject obj = strToObject(dados);
			WebResource wr = getWebResource(obj, "GET");
			ClientResponse response = wr.get(ClientResponse.class);
			return tratarRetorno(obj, response, "GET");
		} catch(Exception e) {
			System.out.println("ERRO AO EXECUTAR GET");
			e.printStackTrace();
			return "";
		}
		
	}
	
	public String executePut(String dados) {
		try {
			JsonObject obj = strToObject(dados);
			WebResource wr = getWebResource(obj, "PUT");
			
			String contentType = obj.get("contentType").getAsString();
			String dadosServico = obj.get("dados").getAsString();
			
			Builder builder = wr.type(contentType);
			 
			ClientResponse response = null;
			
			if(dadosServico == null)
				 response = builder.put(ClientResponse.class);
			else
			 response = builder.put(ClientResponse.class, dadosServico);
			 
			return tratarRetorno(obj, response, "PUT");
		} catch(Exception e) {
			System.out.println("ERRO AO EXECUTAR PUT");
			e.printStackTrace();
			return "";
		}
	}
	
	public String executePost(String dados) {
		try {
			String sessionId = "dzojzdd30ol0mhcx5n13r3p1";
			String userAuth = "A4475B32C986FB34E73E8F97DED34AA5866F53D5C59BF204A32183BE02AD25BD525A02912F67DFB09190FE2DEE35BACAD6A2CF71BABB5B41050BBAFA735E428ABC9A2305F19AFEAB49F30917"
					+ "9D585D6A46D8A764569E3EA7589B85CD074402A054B9E36D8B06B87B08B0157EED18E1EF47EA28B653EC09A680D2ED504EEFE344E046CB943C18C74D269822527D59F58F";
			JsonObject obj = strToObject(dados);
			WebResource wr = getWebResource(obj, "POST");

			String contentType = obj.get("contentType").getAsString();
			String dadosServico = obj.get("dados").getAsString();

			Builder builder = wr.type(contentType).cookie(new Cookie("SessionID", sessionId))
					.cookie(new Cookie("UserAuth", userAuth)).header("SessionID", sessionId).header("userAuth", userAuth);
			wr.setProperty("SessionID", sessionId);
			wr.setProperty("UserAuth", userAuth);


			ClientResponse response = null;

			if (dadosServico == null)
				response = builder.post(ClientResponse.class);
			else
				response = builder.post(ClientResponse.class, dadosServico);
			 
			return tratarRetorno(obj, response, "POST");
		} catch(Exception e) {
			System.out.println("ERRO AO EXECUTAR POST");
			e.printStackTrace();
			return "";
		}
	}
	
	private String converter(ClientResponse response, String dataType) {
		String retorno = response.getEntity(String.class);
		
		if (dataType == null || dataType.isEmpty())
			return retorno;
		
		MediaType dataTypeService = response.getType();
		
		if (isJson(dataTypeService.toString())) {
			Gson gson = new Gson();
			JsonElement element = gson.fromJson(retorno, JsonElement.class);
			if(element instanceof JsonObject) {
				JsonObject obj = (JsonObject) element;
				if(dataType.equals(MediaType.APPLICATION_XML))
					retorno = "";
			} else if(element instanceof JsonArray) {
				JsonArray array = (JsonArray) element;
				if(dataType.equals(MediaType.APPLICATION_XML)) {
					JsonObject obj = new JsonObject();
					obj.add("root", array);
					retorno = "";
				}
			}
		}
		return retorno;
	}
	
	private boolean isJson(String mediaType) {
		return mediaType.equals(MediaType.APPLICATION_JSON) ||
			   mediaType.equals(MediaType.APPLICATION_JSON_TYPE) ||
			   mediaType.split(";")[0].equals(MediaType.APPLICATION_JSON);
	}
	
	private JsonObject strToObject(String str) {
		Gson gson = new Gson();
		return gson.fromJson(str, JsonObject.class);
	}
	
	private WebResource getWebResource(JsonObject obj, String metodo) {
		WebResource wr = null;
		String url = obj.get("url").getAsString();
		boolean ssl = obj.get("ssl").getAsBoolean();
		boolean proxy = obj.get("proxy").getAsBoolean();
		if(ssl)
			wr = getWebResourceSecurity(obj, url, proxy, metodo);
		else {
			System.out.println("Executando servico REST metodo=" + metodo + ";url=" + url);
			Client c = Client.create();
			wr = c.resource(url);
		}
		return wr;
	}
	
	private WebResource getWebResourceSecurity(JsonObject obj, String url, boolean proxy, String metodo) {
		System.out.println("Executando servico REST Security metodo=" + metodo + ";url=" + url);
		ConnectionFactory cf;
		if(proxy)
			cf = new ConnectionFactory("proxy.datacoper.com.br", 3128);
		else
			cf = new ConnectionFactory();
		URLConnectionClientHandler cc  = new URLConnectionClientHandler(cf);
		Client c = new Client(cc);
		return c.resource(url);
	}
	
	private String tratarRetorno(JsonObject obj, ClientResponse response, String metodo) {
		String dataType = obj.get("dataType").getAsString();;
		String retorno = converter(response, dataType);
		
		/*
		Boolean removeCharEsp = obj.get("removeCharEsp").getAsBoolean();
		if(removeCharEsp)
			retorno = Utils.substituir(retorno);
		*/
			
		System.out.println("Retorno Execucao servico REST metodo=" + metodo + ";url="
				+ obj.get("url").getAsString());
		System.out.println(retorno);
		return retorno;
	}
	
	
	
	
	
	
	
	
	
	
	
	public String executeTeste(String auth, String req) {
		JsonObject authObj = strToObject(auth);
		JsonObject reqObj = strToObject(req);
		ConnectionFactory cf = new ConnectionFactory();
		URLConnectionClientHandler cc  = new URLConnectionClientHandler(cf);
		Client c = new Client(cc);
		String contentType = authObj.get("contentType").getAsString();
		String dados = reqObj.get("dados").getAsString();
		WebResource wr = c.resource(authObj.get("url").getAsString());
		Builder b = wr.type(contentType);
		ClientResponse cr = b.post(ClientResponse.class, " ");
		
		try {
			String url = reqObj.get("url").getAsString();
			System.out.println(url);
			System.out.println(wr.getURI());
			wr = wr.uri(new URI(url));
			System.out.println(wr.getURI());
		} catch (Exception e) {
			System.out.println("Erro ao definir url: " + reqObj.get("url").getAsString());
			e.printStackTrace();
		}
		System.out.println("Properties");
		System.out.println(wr);
		b = wr.type(contentType);
		ClientResponse response = b.post(ClientResponse.class, dados);
	
		 
		return tratarRetorno(reqObj, response, "POST");
	}
}
