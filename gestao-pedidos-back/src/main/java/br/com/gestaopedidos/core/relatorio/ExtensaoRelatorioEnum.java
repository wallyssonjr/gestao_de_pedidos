package br.com.gestaopedidos.core.relatorio;

import lombok.Getter;
import org.springframework.http.MediaType;

@Getter
public enum ExtensaoRelatorioEnum {
    PDF(".pdf", MediaType.APPLICATION_PDF);

    private final String nomeExtensao;
    private final MediaType mediaType;

    ExtensaoRelatorioEnum(String nome, MediaType mediaType) {
        this.nomeExtensao = nome;
        this.mediaType = mediaType;
    }
}
