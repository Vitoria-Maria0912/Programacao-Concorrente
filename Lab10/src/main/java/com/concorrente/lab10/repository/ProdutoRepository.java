package com.concorrente.lab10.repository;

import com.concorrente.lab10.dto.ProdutoDTO;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ProdutoRepository {
    private final ConcurrentMap<String, ProdutoDTO> produtos = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, AtomicInteger> vendasPorProduto = new ConcurrentHashMap<>();

    public boolean existsById(String id) {
        return produtos.containsKey(id);
    }

    public void save(ProdutoDTO produtoDTO) {
        produtos.put(produtoDTO.getId(), produtoDTO);
        vendasPorProduto.put(produtoDTO.getId(), new AtomicInteger(0));
    }

    public ProdutoDTO findById(String id) {
        return produtos.get(id);
    }

    public List<ProdutoDTO> findAll() {
        return new ArrayList<>(produtos.values());
    }

    public void updateEstoque(String id, int novaQuantidade) {
        ProdutoDTO produto = produtos.get(id);
        if (produto != null) {
            produto.setQuantity(novaQuantidade);
        }
    }

    public void reduzirEstoque(String id, int quantidade) {
        ProdutoDTO produto = produtos.get(id);
        if (produto != null) {
            produto.setQuantity(produto.getQuantity() - quantidade);
            vendasPorProduto.get(id).addAndGet(quantidade);
        }
    }

    public Map<String, Integer> getVendas() {
        Map<String, Integer> vendas = new HashMap<>();
        vendasPorProduto.forEach((id, q) -> vendas.put(id, q.get()));
        return vendas;
    }
}
