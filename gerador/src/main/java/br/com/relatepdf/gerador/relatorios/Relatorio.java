package br.com.relatepdf.gerador.relatorios;

public interface Relatorio {
    public void gerarCabecalho();
    public void gerarCorpo();
    public void gerarRodape();
    public void imprimir();

}
