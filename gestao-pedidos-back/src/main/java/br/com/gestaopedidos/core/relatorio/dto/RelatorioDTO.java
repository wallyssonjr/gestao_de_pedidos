package br.com.gestaopedidos.core.relatorio.dto;

import br.com.gestaopedidos.core.relatorio.ExtensaoRelatorioEnum;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Data
@Builder
public class RelatorioDTO {

    private HttpHeaders httpHeader;
    private byte[] relatorioPDF;
    private String nomeRelatorio;
    private ExtensaoRelatorioEnum extensao;

    public HttpHeaders getHttpHeader() {
        httpHeader = new HttpHeaders();

        httpHeader.setContentType(getExtensao().getMediaType());
        httpHeader.setContentDispositionFormData("attachment", getNomeRelatorio());
        httpHeader.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return httpHeader;
    }

    public String getNomeRelatorio() {
        if (!StringUtils.hasLength(nomeRelatorio)) {
            nomeRelatorio = "relatorio";
        }
        return nomeRelatorio + getExtensao().getNomeExtensao();
    }

    public ExtensaoRelatorioEnum getExtensao() {
        if (Objects.isNull(extensao)) {
            return ExtensaoRelatorioEnum.PDF;
        }
        return extensao;
    }
}
