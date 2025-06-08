package com.concorrente.lab10.service;

import com.concorrente.lab10.dto.ProdutoDTO;
import com.concorrente.lab10.exception.*;
import java.util.Map;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class VendaService {
    
    private final ProdutoService produtoService;

    public VendaService(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    public synchronized Map<String, Object> realizarCompra(String produtoId, int quantidade) {
        try {
            produtoService.realizarBaixaEstoque(produtoId, quantidade);
            ProdutoDTO produto = produtoService.consultarProduto(produtoId);

            return Map.of(
                    "message", "Compra realizada com sucesso.",
                    "product", Map.of(
                            "id", produto.getId(),
                            "name", produto.getName(),
                            "remainingStock", produto.getQuantity()
                    )
            );
        } catch (ProdutoNaoEncontradoException e) {
            throw e;
        } catch (EstoqueInsuficienteException e) {
            throw e;
        }
    }

    public synchronized Map<String, Object> gerarRelatorioDeVendas() {
        Map<String, Integer> vendasPorProduto = produtoService.getVendasPorProduto();
        List<Map<String, Object>> produtosVendidos = new ArrayList<>();
        int totalVendas = 0;

        for (Map.Entry<String, Integer> entry : vendasPorProduto.entrySet()) {
            String id = entry.getKey();
            int quantidadeVendida = entry.getValue();
            totalVendas += quantidadeVendida;

            ProdutoDTO produto = produtoService.consultarProduto(id);
            produtosVendidos.add(Map.of(
                    "id", id,
                    "name", produto.getName(),
                    "quantitySold", quantidadeVendida
            ));
        }

        return Map.of(
                "totalSales", totalVendas,
                "products", produtosVendidos
        );
    }
}