package com.concorrente.lab10.controller;

import com.concorrente.lab10.service.VendaService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }
    
    @PostMapping("/purchase")
    public ResponseEntity<?> realizarCompra(@RequestBody Map<String, Object> request) {
        String id = (String) request.get("id");
        int quantity = (int) request.get("quantity");
        Map<String, Object> resposta = vendaService.realizarCompra(id, quantity);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/sales/report")
    public ResponseEntity<?> gerarRelatorio() {
        Map<String, Object> relatorio = vendaService.gerarRelatorioDeVendas();
        return ResponseEntity.ok(relatorio);
    }
}

    
