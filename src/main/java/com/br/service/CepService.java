package com.br.service;

import com.br.entity.Cep;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


@Service
public class CepService {


    public String obterCepRetornandoXml(String cep) {
        try {
            String urlStr = "https://viacep.com.br/ws/" + cep + "/xml/";

            // Cria uma URL a partir da string da URL
            URL url = new URL(urlStr);

            // Abre uma conexão HTTP para a URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Lê o XML da resposta da requisição
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Retorna o XML como uma string
            return response.toString();
        } catch (Exception e) {
            // Trata exceções
            e.printStackTrace();
            return null;
        }
    }

    public Cep obterCepRetornandoJson(String cep) {
        // Inicializa a conexão como nula
        HttpURLConnection connection = null;
        try {
            // Constrói a URL para a consulta do CEP
            String urlStr = "https://viacep.com.br/ws/" + cep + "/xml/";
            URL url = new URL(urlStr);

            // Abre uma conexão HTTP com a URL
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Obtém o código de resposta da conexão
            int responseCode = connection.getResponseCode();

            // Verifica se a resposta da requisição foi bem-sucedida (código 200)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Se bem-sucedida, lê os dados da resposta
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    // Inicia o processo de desserialização XML com JAXB
                    JAXBContext context = JAXBContext.newInstance(Cep.class);
                    Unmarshaller unmarshaller = context.createUnmarshaller();

                    // Desserializa os dados XML em um objeto Cep e o retorna
                    return (Cep) unmarshaller.unmarshal(in);
                } catch (Exception e) {
                    // Em caso de erro durante a desserialização, imprime o rastreamento da pilha e retorna null
                    e.printStackTrace();
                    return null;
                }
            } else {
                // Se a resposta não for bem-sucedida, imprime uma mensagem de erro e retorna null
                System.out.println("Erro na operação: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            // Em caso de erro durante a conexão ou a requisição, imprime o rastreamento da pilha e retorna null
            e.printStackTrace();
            return null;
        } finally {
            // Finalmente, desconecta-se da conexão HTTP para liberar recursos
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public String consultarCep(String cep) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL("https://viacep.com.br/ws/" + cep + "/xml/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/xml");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();
    }
}


