package com.concorrente.lab10.service;

import com.concorrente.lab10.dto.ProdutoDTO;
import com.concorrente.lab10.exception.EstoqueInsuficienteException;
import com.concorrente.lab10.exception.ProdutoNaoEncontradoException;
import com.concorrente.lab10.repository.ProdutoRepository;
import com.concorrente.lab10.exception.ProdutoJaExistenteException;
import java.util.List;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class ProdutoService {
    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Map<String, Object> cadastrarProduto(ProdutoDTO produtoDTO) {
        if (repository.existsById(produtoDTO.getId())) {
            throw new ProdutoJaExistenteException(produtoDTO.getId());
        }
        repository.save(produtoDTO);
        return Map.of(
            "message", "Produto cadastrado com sucesso.",
            "product", Map.of("id", produtoDTO.getId(), "name", produtoDTO.getName())
        );
    }

    public List<ProdutoDTO> consultarProdutos() {
        return repository.findAll();
    }

    public ProdutoDTO consultarProduto(String id) {
        ProdutoDTO produto = repository.findById(id);
        if (produto == null) throw new ProdutoNaoEncontradoException(id);
        return produto;
    }

    public Map<String, Object> atualizarEstoque(String id, ProdutoDTO dto) {
        ProdutoDTO produto = repository.findById(id);
        if (produto == null) throw new ProdutoNaoEncontradoException(id);
        repository.updateEstoque(id, dto.getQuantity());
        return Map.of("message", "Estoque atualizado.", "remainingStock", dto.getQuantity());
    }

    public void realizarBaixaEstoque(String id, int quantidade) {
        ProdutoDTO produto = repository.findById(id);
        if (produto == null) throw new ProdutoNaoEncontradoException(id);
        if (produto.getQuantity() < quantidade) {
            throw new EstoqueInsuficienteException(produto.getQuantity());
        }
        repository.reduzirEstoque(id, quantidade);
    }

    public Map<String, Integer> getVendasPorProduto() {
        return repository.getVendas();
    }
}
