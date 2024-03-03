package com.br.controller;

import com.br.entity.Cep;

import com.br.service.CepService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/xml")
public class CepController {
private final CepService cepService;

    public CepController(CepService cepService) {
        this.cepService = cepService;
    }



    @GetMapping
    public ResponseEntity<String> obterCepRetornarXml(@RequestParam String cep) {
        return ResponseEntity.ok(cepService.obterCepRetornandoXml(cep));
    }

    @GetMapping("/{cep}")
    public ResponseEntity<Cep> obterCepRetornarJson (@PathVariable String cep)  {
        return ResponseEntity.ok(cepService.obterCepRetornandoJson(cep));
    }
}
