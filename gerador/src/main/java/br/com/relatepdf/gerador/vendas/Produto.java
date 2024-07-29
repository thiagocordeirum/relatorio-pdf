package br.com.relatepdf.gerador.vendas;


import lombok.Getter;

@Getter
public class Produto {
    private String nome;
    private int quantidade;
    private double valor;


    public Produto(String nome, int quantidade, double valor) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    public double calcularPreco(){
        return this.quantidade * this.valor;
    }

}

