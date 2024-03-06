package com.br.service;

import com.br.request.CepRequest;
import com.br.response.CepResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;
import jakarta.xml.bind.*;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Service
public class CepService {
    public String obterCepRetornandoXml(CepRequest cepRequest) {
        try {

            // Cria o cliente HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Cria a requisição HTTP
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://viacep.com.br/ws/" + cepRequest.getCep() + "/xml/"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JAXBContext jaxbContext = JAXBContext.newInstance(CepResponse.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            CepResponse cepResponse = (CepResponse) unmarshaller.unmarshal(new StringReader(response.body()));

            Marshaller marshaller = jaxbContext.createMarshaller();
            StringWriter writer = new StringWriter();
            marshaller.marshal(cepResponse, writer);

            // Retorna a representação XML do objeto CepResponse
            return writer.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Ocorreu um erro ao obter o CEP: " + e.getMessage();
        }
    }




    public CepResponse obterCepRetornandoJson(String cep) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://viacep.com.br/ws/" + cep + "/json/"))
                    .GET()
                    .build();

            // Envia a requisição e recebe a resposta
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            // Verifica se a resposta foi bem-sucedida (código 200)
            if (response.statusCode() == 200) {
                // Cria um Unmarshaller JAXB para desserializar o JSON
                ObjectMapper objectMapper = new ObjectMapper();

                // Desserializa o JSON diretamente da InputStream da resposta
                return objectMapper.readValue(response.body(), CepResponse.class);
            } else {
                System.err.println("Falha na requisição: código de status " + response.statusCode());
                // Retorna um CepResponse vazio ou com valores padrão em caso de falha
                return new CepResponse();
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Retorna um CepResponse vazio ou com valores padrão em caso de exceção
            return new CepResponse();
        }
    }
    public String consultarCepXMlReXml(CepRequest cep) {
        // StringBuilder para armazenar a resposta da requisição
        StringBuilder response = new StringBuilder();

        try {
            // Criação de um cliente HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Criação de uma requisição HTTP GET para o serviço ViaCEP, especificando o formato XML
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://viacep.com.br/ws/" + cep.getCep() + "/xml/"))
                    .GET()
                    .build();

            // Envio da requisição e obtenção da resposta
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Verifica se a resposta foi bem-sucedida (código 200)
            if (httpResponse.statusCode() != 200) {
                // Se não foi bem-sucedida, lança uma exceção indicando o erro
                throw new RuntimeException("Falha na requisição HTTP: " + httpResponse.statusCode());
            }

            // Se a resposta foi bem-sucedida, anexa o corpo da resposta ao StringBuilder
            response.append(httpResponse.body());
        } catch (Exception e) {
            // Se ocorrer uma exceção durante o processo, lança uma exceção indicando o erro
            throw new RuntimeException("Erro durante a consulta do CEP: " + e.getMessage(), e);
        }

        // Retorna a resposta como uma string
        return response.toString();
    }

    public String consultarCepXmlReJson(String cep) {
        // Criação de um cliente HTTP
        HttpClient client = HttpClient.newHttpClient();

        // Criação de uma requisição HTTP GET para o serviço ViaCEP, especificando o formato JSON
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://viacep.com.br/ws/" + cep + "/json/"))
                .GET()
                .build();

        try {
            // Envio da requisição e obtenção da resposta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Verifica se a resposta foi bem-sucedida (código 200)
            if (response.statusCode() == 200) {
                // Se for bem-sucedida, retorna o corpo da resposta (formato JSON)
                return response.body();
            } else {
                // Se não for bem-sucedida, imprime uma mensagem de erro e retorna null
                System.out.println("Erro na operação: " + response.statusCode());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            // Em caso de exceção durante o processo, imprime o rastreamento da pilha e retorna null
            e.printStackTrace();
            return "Ocorreu um erro ao obter o CEP: " + e.getMessage();
        }
    }









}


