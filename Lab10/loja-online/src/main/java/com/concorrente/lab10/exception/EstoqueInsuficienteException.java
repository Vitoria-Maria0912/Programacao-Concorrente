package com.concorrente.lab10.exception;

public class EstoqueInsuficienteException extends RuntimeException {

    public EstoqueInsuficienteException(int quantidadeDisponivel) {
        super("Estoque insuficiente. Quantidade disponível: " + quantidadeDisponivel);
    }
}