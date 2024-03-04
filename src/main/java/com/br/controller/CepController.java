package com.br.controller;

import com.br.entity.Cep;

import com.br.service.CepService;



import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    /**
     * Endpoint para receber um CEP e retornar o resultado em formato XML.
     *
     * @param cep O CEP a ser consultado.
     * @return Uma resposta HTTP contendo o XML resultante da consulta.
     */
    @GetMapping("/xml")
    @Operation(summary = "Obter CEP em formato XML", description = "Endpoint para receber um CEP e retornar o resultado em formato XML")
    public ResponseEntity<String> obterCepRetornarXml(@RequestParam String cep) {
        return ResponseEntity.ok(cepService.obterCepRetornandoXml(cep));
    }

    /**
     * Endpoint para receber um CEP e retornar o resultado em formato JSON.
     *
     * @param cep O CEP a ser consultado.
     * @return Uma resposta HTTP contendo o objeto Cep em formato JSON.
     */
    @GetMapping("/json/{cep}")
    @Operation(summary = "Obter CEP em formato JSON", description = "Endpoint para receber um CEP e retornar o resultado em formato JSON")
    public ResponseEntity<Cep> obterCepRetornarJson(@PathVariable String cep) {
        return ResponseEntity.ok(cepService.obterCepRetornandoJson(cep));
    }

    /**
     * Endpoint para receber um XML contendo dados de CEP e retornar o resultado em formato XML.
     *
     * @param request O XML contendo os dados do CEP a ser consultado.
     * @return Uma string contendo o XML resultante da consulta.
     */
    @PostMapping(value = "/consulta-xml", produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    @Operation(summary = "Consultar CEP em formato XML", description = "Endpoint para receber um XML contendo dados de CEP e retornar o resultado em formato XML")
    public String consultarCep(@RequestBody Cep request) {
        return cepService.consultarCep(request.getCep());
    }

    /**
     * Endpoint para receber um XML contendo dados de CEP e retornar o resultado em formato JSON.
     *
     * @param request O XML contendo os dados do CEP a ser consultado.
     * @return Uma string contendo o JSON resultante da consulta.
     */
    @PostMapping(value = "/consulta-json", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    @Operation(summary = "Consultar CEP em formato JSON", description = "Endpoint para receber um XML contendo dados de CEP e retornar o resultado em formato JSON")
    public String consultarCepReJson(@RequestBody Cep request) {
        return cepService.consultarCepReJson(request.getCep());
    }
}
