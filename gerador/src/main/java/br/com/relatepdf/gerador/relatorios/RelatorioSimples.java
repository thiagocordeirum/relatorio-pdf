package br.com.relatepdf.gerador.relatorios;

import br.com.relatepdf.gerador.vendas.Produto;
import br.com.relatepdf.gerador.vendas.Venda;
import com.lowagie.text.*;
import com.lowagie.text.Font;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;


import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class RelatorioSimples implements Relatorio { ;
    private Venda venda;
    private Document documentoPDF;
    private String caminhoRelatorio = "RelatorioSimples.pdf";


    public RelatorioSimples() {
        Scanner scanner = new Scanner(System.in);
        this.venda = entradaDados(scanner);
        this.documentoPDF = new Document();
    }

    private Venda entradaDados(Scanner scanner) {
        List<Produto> produtos = new ArrayList<>();
        System.out.print("Entre com o nome do cliente: ");
        String nomeCliente = scanner.nextLine();

        System.out.print("Quantos produtos diferentes você deseja adicionar? ");
        int numProdutos = scanner.nextInt();
        scanner.nextLine(); // Consumir a nova linha

        for (int i = 0; i < numProdutos; i++) {
            System.out.print("Nome do produto: ");
            String nomeProduto = scanner.nextLine();
            System.out.print("Quantidade: ");
            int quantidade = scanner.nextInt();
            System.out.print("Preço: ");
            double preco = scanner.nextDouble();
            scanner.nextLine(); // Consumir a nova linha

            produtos.add(new Produto(nomeProduto, quantidade, preco));
        }

        Venda venda = new Venda(nomeCliente, produtos);
        return venda;
    }

    public void export(HttpServletResponse response){
        try{
            PdfWriter.getInstance(this.documentoPDF, response.getOutputStream());
            this.documentoPDF.open();

            gerarCabecalho();
            gerarCorpo();
            gerarRodape();

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"relatorioSimples.pdf\"");

            imprimir();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void gerarCabecalho() {
        this.adicionarParagrafoTitulo();
        this.pularLinha();
        this.adicionarDadosCliente();
        this.pularLinha();
        this.adicionarQuebraDeSessao();
    }

    @Override
    public void gerarCorpo() {
        this.adicionarParagrafoItensVendidosTitulo();
        PdfPTable tableProdutos = this.criarTabelaComCabecalho();
        this.adicionarProdutosATabela(tableProdutos);
        this.documentoPDF.add(tableProdutos);
        this.pularLinha();
        this.adicionarTotalDaVenda();
    }

    @Override
    public void gerarRodape() {
        this.adicionarQuebraDeSessao();
        this.pularLinha();
        this.adicionarRodaPe();
    }

    @Override
    public void imprimir() {
        if (this.documentoPDF != null && this.documentoPDF.isOpen()) {
            documentoPDF.close();
        }
    }

    private void adicionarPaginacao() {
        HeaderFooter paginacao = new HeaderFooter(new Phrase("Pág.", new Font(Font.BOLD)), true);
        paginacao.setAlignment(Element.ALIGN_RIGHT);
        paginacao.setBorder(Rectangle.NO_BORDER);
        documentoPDF.setHeader(paginacao);
    }

    private void adicionarDadosCliente() {
        Chunk chunkDataCliente = new Chunk();
        chunkDataCliente.append("Cliente: " + this.venda.getNomeCliente());
        chunkDataCliente.append(this.criarDataFormatada());

        Paragraph paragrafoDataCliente = new Paragraph();
        paragrafoDataCliente.add(chunkDataCliente);
        this.documentoPDF.add(paragrafoDataCliente);
    }

    private void adicionarQuebraDeSessao() {
        Paragraph paragrafoSessao = new Paragraph("__________________________________________________________");
        paragrafoSessao.setAlignment(Element.ALIGN_CENTER);
        this.documentoPDF.add(paragrafoSessao);
    }

    private void adicionarParagrafoTitulo() {
        Paragraph paragrafoTitulo = new Paragraph();
        paragrafoTitulo.setAlignment(Element.ALIGN_CENTER);
        Chunk cTitulo = new Chunk("RELATÓRIO DE VENDAS");
        cTitulo.setFont(new Font(Font.COURIER, 24));
        cTitulo.setBackground(Color.lightGray, 2, 2, 2, 2);
        paragrafoTitulo.add(cTitulo);
        documentoPDF.add(paragrafoTitulo);
    }



    private void pularLinha() {
        this.documentoPDF.add(new Paragraph(" "));
    }

    private String criarDataFormatada() {
        StringBuilder dataVenda = new StringBuilder();
        dataVenda.append(" - Data da venda: ");
        dataVenda.append(this.venda.getDataVenda().getDayOfMonth());
        dataVenda.append("/");
        dataVenda.append(this.venda.getDataVenda().getMonthValue());
        dataVenda.append("/");
        dataVenda.append(this.venda.getDataVenda().getYear());
        return dataVenda.toString();
    }

    private PdfPTable criarTabelaComCabecalho() {
        // tabela com 4 colunas
        PdfPTable tableProdutos = new PdfPTable(4);
        tableProdutos.setWidthPercentage(98);
        tableProdutos.setWidths(new float[] { 2f, 1f, 1f, 1f });

        PdfPCell celulaTitulo = new PdfPCell(new Phrase("PRODUTO"));
        celulaTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        celulaTitulo.setBackgroundColor(Color.LIGHT_GRAY);
        tableProdutos.addCell(celulaTitulo);

        celulaTitulo = new PdfPCell(new Phrase("QUANTIDADE"));
        celulaTitulo.setBackgroundColor(Color.LIGHT_GRAY);
        celulaTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableProdutos.addCell(celulaTitulo);

        celulaTitulo = new PdfPCell(new Phrase("VALOR UNI."));
        celulaTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        celulaTitulo.setBackgroundColor(Color.LIGHT_GRAY);
        tableProdutos.addCell(celulaTitulo);

        celulaTitulo = new PdfPCell(new Phrase("VALOR TOTAL"));
        celulaTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        celulaTitulo.setBackgroundColor(Color.LIGHT_GRAY);
        tableProdutos.addCell(celulaTitulo);

        return tableProdutos;
    }

    private void adicionarProdutosATabela(PdfPTable tableProdutos) {
        int contador = 1;
        for (Produto produto : this.venda.getProdutosVendidos()) {

            PdfPCell celulaNome = new PdfPCell(new Phrase(produto.getNome()));
            PdfPCell celulaQuantidade = new PdfPCell(new Phrase(String.valueOf(produto.getQuantidade())));
            celulaQuantidade.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell celulaValor = new PdfPCell(new Phrase("R$ " + String.valueOf(produto.getValor())));
            celulaValor.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell celulaTotalUnit = new PdfPCell(new Phrase("R$ " + String.valueOf(produto.calcularPreco())));
            celulaTotalUnit.setHorizontalAlignment(Element.ALIGN_CENTER);

            if (contador % 2 == 0) {
                celulaNome.setBackgroundColor(Color.LIGHT_GRAY);
                celulaQuantidade.setBackgroundColor(Color.LIGHT_GRAY);
                celulaValor.setBackgroundColor(Color.LIGHT_GRAY);
                celulaTotalUnit.setBackgroundColor(Color.LIGHT_GRAY);
            }

            tableProdutos.addCell(celulaNome);
            tableProdutos.addCell(celulaQuantidade);
            tableProdutos.addCell(celulaValor);
            tableProdutos.addCell(celulaTotalUnit);

            contador++;
        }
    }

    private void adicionarTotalDaVenda() {

        Paragraph pTotal = new Paragraph();
        pTotal.setAlignment(Element.ALIGN_RIGHT);
        pTotal.add(new Chunk("Total da venda: R$ " + this.venda.calcularValorTotalCarrinho(),
                new Font(Font.TIMES_ROMAN, 20)));
        this.documentoPDF.add(pTotal);
    }

    private void adicionarParagrafoItensVendidosTitulo() {
        Paragraph pItensVendidos = new Paragraph();
        pItensVendidos.setAlignment(Element.ALIGN_CENTER);
        pItensVendidos.add(new Chunk("ITENS VENDIDOS ", new Font(Font.TIMES_ROMAN, 16)));
        documentoPDF.add(new Paragraph(pItensVendidos));
        documentoPDF.add(new Paragraph(" "));
    }

    private void adicionarRodaPe() {
        Paragraph pRodape = new Paragraph();
        pRodape.setAlignment(Element.ALIGN_CENTER);
        pRodape.add(new Chunk("https://github.com/thiagocordeirum", new Font(Font.TIMES_ROMAN, 14)));
        this.documentoPDF.add(pRodape);
    }
}
