package br.com.relatepdf.gerador.controle;

import br.com.relatepdf.gerador.relatorios.RelatorioSimples;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class RelatorioControle {

    private final RelatorioSimples relatorioSimples;
    public RelatorioControle(RelatorioSimples relatorioSimples) {
        this.relatorioSimples = relatorioSimples;
    }

    @GetMapping("/pdf/generate")
    public void gerarPdf(HttpServletResponse response) {

        response.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=relatorio.pdf";
        response.setHeader(headerKey, headerValue);

        this.relatorioSimples.export(response);


    }

}
