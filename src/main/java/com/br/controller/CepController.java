package com.br.controller;

import com.br.entity.Cep;

import com.br.service.CepService;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.jws.WebParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


@RestController
@RequestMapping("/xml")

public class CepController {
private final CepService cepService;

    public CepController(CepService cepService) {
        this.cepService = cepService;
    }


    @GetMapping
    @Operation(description = "Endpoint para receber cep e retorna xml")
    public ResponseEntity<String> obterCepRetornarXml(@RequestParam String cep) {
        return ResponseEntity.ok(cepService.obterCepRetornandoXml(cep));
    }

    @GetMapping("/{cep}")
    @Operation(description = "Endpoint para recebe cep retorna json")
    public ResponseEntity<Cep> obterCepRetornarJson (@PathVariable String cep)  {
        return ResponseEntity.ok(cepService.obterCepRetornandoJson(cep));
    }

    @PostMapping(produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    @Operation(description = "Endpoint para receber xml e retorna xml")
    public String consultarCep(@RequestBody Cep request) {
        return cepService.consultarCep(request.getCep());
    }
}
