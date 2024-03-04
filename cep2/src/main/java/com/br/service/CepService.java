package com.br.service;

import com.br.entity.Cep;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;


@Service
public class CepService {
    public String obterCepRetornandoXml(String cep) {
        try {
            // Cria o cliente HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Cria a requisição HTTP
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://viacep.com.br/ws/" + cep + "/xml/"))
                    .GET()
                    .build();

            // Envia a requisição e recebe a resposta
            HttpResponse<InputStream> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            // Verifica se a resposta foi bem-sucedida (código 200)
            if (httpResponse.statusCode() == 200) {
                // Lê o XML da resposta da requisição
                BufferedReader in = new BufferedReader(new InputStreamReader(httpResponse.body()));
                StringBuilder responseBody = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    responseBody.append(inputLine);
                }
                in.close(); // Fechar o BufferedReader após o uso
                return responseBody.toString();
            } else {
                throw new RuntimeException("Falha na requisição: código de status " + httpResponse.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public Cep obterCepRetornandoJson(String cep) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://viacep.com.br/ws/" + cep + "/xml/"))
                    .GET()
                    .build();

            // Envia a requisição e recebe a resposta
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            // Verifica se a resposta foi bem-sucedida (código 200)
            if (response.statusCode() == 200) {
                // Cria um Unmarshaller JAXB para desserializar o XML
                JAXBContext jaxbContext = JAXBContext.newInstance(Cep.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

                // Desserializa o XML diretamente da InputStream da resposta

                return (Cep) unmarshaller.unmarshal(response.body());
            } else {
                System.err.println("Falha na requisição: código de status " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String consultarCepXMlReXml(String cep) {
        // StringBuilder para armazenar a resposta da requisição
        StringBuilder response = new StringBuilder();

        try {
            // Criação de um cliente HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Criação de uma requisição HTTP GET para o serviço ViaCEP, especificando o formato XML
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://viacep.com.br/ws/" + cep + "/xml/"))
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
            return null;
        }
    }

}


