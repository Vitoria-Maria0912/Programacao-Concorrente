package com.concorrente.lab10.controller;

import com.concorrente.lab10.dto.ProdutoDTO;
import com.concorrente.lab10.service.ProdutoService;

import java.util.List;
import java.util.Map;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarProdutos() {
        return ResponseEntity.ok(produtoService.consultarProdutos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> consultarProduto(@PathVariable String id) {
        ProdutoDTO produto = produtoService.consultarProduto(id);
        return ResponseEntity.ok(produto);
    }

    @PostMapping
    public ResponseEntity<?> cadastrarProduto(@RequestBody ProdutoDTO produtoDTO) {
        Map<String, Object> resposta = produtoService.cadastrarProduto(produtoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<?> atualizarEstoque(@PathVariable String id, @RequestBody ProdutoDTO produtoDTO) {
        Map<String, Object> resposta = produtoService.atualizarEstoque(id, produtoDTO);
        return ResponseEntity.ok(resposta);
    }
}
