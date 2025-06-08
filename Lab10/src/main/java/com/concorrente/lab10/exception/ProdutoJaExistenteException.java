package com.concorrente.lab10.exception;


public class ProdutoJaExistenteException extends RuntimeException {
        public ProdutoJaExistenteException(String id) {
            super("Produto com ID " + id + " já existente.");
        }
}
