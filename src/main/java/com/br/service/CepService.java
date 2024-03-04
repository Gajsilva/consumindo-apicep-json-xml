package com.br.service;

import com.br.entity.Cep;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


@Service
public class CepService {


    /**
     * Obtém os dados do CEP no formato XML a partir de um serviço web.
     *
     * @param cep O CEP a ser consultado.
     * @return Uma string contendo o XML com os dados do CEP, ou null em caso de erro.
     */
    public String obterCepRetornandoXml(String cep) {
        try {
            // Constrói a URL para consulta do CEP
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
            // Trata exceções, caso ocorram
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


    /**
     * Consulta um serviço web para obter os dados de um CEP no formato XML.
     *
     * @param cep O CEP a ser consultado.
     * @return Uma string contendo os dados do CEP no formato XML, ou uma string vazia em caso de erro.
     */
    public String consultarCep(String cep) {
        StringBuilder response = new StringBuilder();
        try {
            // Constrói a URL para a consulta do CEP
            URL url = new URL("https://viacep.com.br/ws/" + cep + "/xml/");

            // Abre uma conexão HTTP com a URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/xml");

            // Verifica se a resposta da requisição foi bem-sucedida (código 200)
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            // Lê os dados da resposta e os armazena em uma string
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }

            // Desconecta-se da conexão HTTP
            conn.disconnect();
        } catch (Exception e) {
            // Em caso de erro durante a conexão ou a requisição, imprime o rastreamento da pilha
            // e retorna uma string vazia ou incompleta, dependendo do ponto em que ocorreu o problema.
            e.printStackTrace();
        }
        // Retorna a string contendo os dados do CEP no formato XML, ou uma string vazia em caso de erro.
        return response.toString();
    }



    /**
     * Consulta um serviço web para obter os dados de um CEP no formato JSON.
     *
     * @param cep O CEP a ser consultado.
     * @return Uma string contendo os dados do CEP no formato JSON, ou null em caso de erro.
     */
    public String consultarCepReJson(String cep) {
        HttpURLConnection connection = null;
        try {
            // Constrói a URL para a consulta do CEP
            String urlStr = "https://viacep.com.br/ws/" + cep + "/json/";
            URL url = new URL(urlStr);

            // Abre uma conexão HTTP com a URL
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Obtém o código de resposta da conexão
            int responseCode = connection.getResponseCode();

            // Verifica se a resposta da requisição foi bem-sucedida (código 200)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    // Lê os dados da resposta e os armazena em uma string
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }

                    // Retorna a resposta como um JSON
                    return response.toString();
                } catch (IOException e) {
                    // Em caso de erro durante a leitura da resposta, imprime o rastreamento da pilha e retorna null
                    e.printStackTrace();
                    return null;
                }
            } else {
                // Se a resposta não for bem-sucedida, imprime uma mensagem de erro e retorna null
                System.out.println("Erro na operação: " + responseCode);
                return null;
            }
        } catch (IOException e) {
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

}


