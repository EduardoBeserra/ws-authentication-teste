package com.beserra.teste.main;

import java.io.File;
import java.io.IOException;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Teste {

	public static void main(String[] args) throws Exception {
		new Teste().execute3();
	}
	
	private void execute1() throws IOException {
		JsonObject objReq = createJsonLogin();
		new ServicoRest().executePost(objReq.toString());
	}
	
	private void execute2() throws IOException {
		JsonObject objReq = createJsonIncluirReceituario();
		new ServicoRest().executePost(objReq.toString());
	}
	
	private void execute3() throws IOException {
		JsonObject objAuth = createJsonLogin();
		JsonObject objReq = createJsonIncluirReceituario();
		System.out.println(new ServicoRest().executeTeste(objAuth.toString(), objReq.toString()));
	}
	
	private JsonObject createJsonValidarUsuario() {
		JsonObject objReq = new JsonObject();
		objReq.addProperty("url",
				"https://svchomologacao.sigen.cidasc.sc.gov.br/Acesso/ValidarExisteUsuario"
				+ "?nmLogin=08601914942&dsSenha=26338551&tipousuario=RT");
		objReq.addProperty("contentType", "application/json");
		objReq.addProperty("dataType", "application/json");
		objReq.addProperty("ssl", true);
		objReq.addProperty("removeCharEsp", true);
		objReq.addProperty("proxy", false);
		objReq.addProperty("dados", " ");
		return objReq;
	}
	
	private JsonObject createJsonIncluirReceituario() {
		JsonObject objReq = new JsonObject();
		objReq.addProperty("url",
				"https://svchomologacao.sigen.cidasc.sc.gov.br/Receituario/Incluir");
		//objReq.addProperty("url2", "https://svchomologacao.sigen.cidasc.sc.gov.br/Receituario/Incluir");
		objReq.addProperty("contentType", "application/json");
		objReq.addProperty("dataType", "application/json");
		objReq.addProperty("ssl", true);
		objReq.addProperty("removeCharEsp", true);
		objReq.addProperty("proxy", false);
		objReq.addProperty("dados", " ");
		return objReq;
	}
	
	private JsonObject createJsonLogin() {
		JsonObject objReq = new JsonObject();
		objReq.addProperty("url",
				"https://svchomologacao.sigen.cidasc.sc.gov.br/Acesso/Login?authToken=MDg2MDE5MTQ5NDI6MjYzMzg1NTE=");
		objReq.addProperty("contentType", "application/json");
		objReq.addProperty("dataType", "application/json");
		objReq.addProperty("ssl", true);
		objReq.addProperty("removeCharEsp", true);
		objReq.addProperty("proxy", false);
		objReq.addProperty("dados", " ");
		return objReq;
	}
	
	private JsonObject fromFileLogin() {
		File f = new File("C:\\Users\\Beserra\\Desktop\\req.json");
		String strReq = "";
		try {
			strReq = Files.toString(f, Charsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Gson gson = new Gson();
		JsonObject objReq = gson.fromJson(strReq, JsonObject.class);
		return objReq;
	}
}
