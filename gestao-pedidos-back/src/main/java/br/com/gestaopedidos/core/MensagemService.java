package br.com.gestaopedidos.core;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MensagemService {

    private final MessageSource mensagens;

    private static final Locale DEFAULT_LOCALE = new Locale("pt", "BR");

    public String getMensagem(String chave, Object... parametros) {
        return mensagens.getMessage(chave, parametros, DEFAULT_LOCALE);
    }

}
