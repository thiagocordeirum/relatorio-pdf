package br.com.relatepdf.gerador.vendas;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Component
public class Venda {
    private LocalDate dataVenda;
    private String nomeCliente;
    private List<Produto> produtosVendidos;

    public Venda(@Value("")String nomeCliente, List<Produto> arrayList) {
        this.dataVenda = LocalDate.now();
        //LocalDate dataAtual = LocalDate.now();
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        //this.dataVenda = dataAtual.format(formatter);
        this.nomeCliente = nomeCliente;
        this.produtosVendidos = arrayList;

    }

    public double calcularValorTotalCarrinho(){
        double total = 0;
        for (Produto produto : produtosVendidos) {
            total += produto.calcularPreco();
        }
        return total;
    }
    public void addProdutoAoCarrinho(Produto produto){
        this.produtosVendidos.add(produto);
    }

    public LocalDate getDataVenda() {
        return this.dataVenda;
    }

    public String getNomeCliente() {
        return this.nomeCliente;
    }

    public List<Produto> getProdutosVendidos() {
        return this.produtosVendidos;
    }

}