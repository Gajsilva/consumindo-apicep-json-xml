package com.br.controller;

import com.br.request.CepRequest;
import com.br.response.CepResponse;

import com.br.service.CepService;



import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/cep")
@Tag(name = "CEP", description = "Operações relacionadas a consultas de CEP")
public class CepController {

    private final CepService cepService;

    public CepController(CepService cepService) {
        this.cepService = cepService;
    }

    @PostMapping(value = "/xml", produces = MediaType.APPLICATION_XML_VALUE,consumes =MediaType.APPLICATION_JSON_VALUE )
    @Operation(summary = "Obter CEP em formato Rest", description = "Endpoint para receber um CEP e retornar o " +
            "resultado em formato XML")
    public ResponseEntity<String> obterCepRetornarXml(@RequestBody CepRequest cepRequest) {
        return ResponseEntity.ok(cepService.obterCepRetornandoXml(cepRequest));
    }

    /**
     * Endpoint para receber um CEP e retornar o resultado em formato JSON.
     *
     * @param cep O CEP a ser consultado.
     * @return Uma resposta HTTP contendo o objeto Cep em formato JSON.
     */
    @GetMapping("/{cep}")
    @Operation(summary = "Obter CEP em formato JSON", description = "Endpoint para receber um CEP e retornar o resultado em formato JSON")
    public ResponseEntity<CepResponse> obterCepRetornarJson(@PathVariable String cep) {
        return ResponseEntity.ok(cepService.obterCepRetornandoJson(cep));
    }

    /**
     * Endpoint para receber um XML contendo dados de CEP e retornar o resultado em formato XML.
     *
     * @param request O XML contendo os dados do CEP a ser consultado.
     * @return Uma string contendo o XML resultante da consulta.
     */
    @PostMapping(value = "/consulta-xml", produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    @Operation(summary = "Consultar CEP em formato XML 22121212", description = "Endpoint para receber um XML " +
            "contendo dados de CEP e retornar o resultado em formato XML")
    public String consultarCep(@RequestBody CepRequest request) {
        return cepService.consultarCepXMlReXml(request);
    }

    /**
     * Endpoint para receber um XML contendo dados de CEP e retornar o resultado em formato JSON.
     *
     * @param request O XML contendo os dados do CEP a ser consultado.
     * @return Uma string contendo o JSON resultante da consulta.
     */
    @PostMapping(value = "/consulta-json", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    @Operation(summary = "Consultar CEP em formato JSON", description = "Endpoint para receber um XML contendo dados de CEP e retornar o resultado em formato JSON")
    public String consultarCepReJson(@RequestBody CepResponse request) {
        return cepService.consultarCepXmlReJson(request.getCep());
    }
}
