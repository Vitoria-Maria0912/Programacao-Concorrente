package com.concorrente.lab10.exception;

public class ProdutoNaoEncontradoException extends RuntimeException {
    public ProdutoNaoEncontradoException(String id) {
        super("Produto não encontrado com ID: " + id);
    }
}