package br.com.gestaopedidos.core.util;

import br.com.gestaopedidos.core.MensagemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Objects;

@Log4j2
@Component
@RequiredArgsConstructor
public class JsonUtil {

    private final ObjectMapper objectMapper;

    private final MensagemService mensagemService;

    /**
     * Exemplo:
     * <p>
     * Map<String, String> nodes = Map.of(
     * "mensagem", "mensagem",
     * "status", String.valueOf(HttpStatus.BAD_REQUEST.value())
     * );
     *
     * @param nodes chave/valor que representam os nodes de uma estrutura JSON.
     * @return retorna o map em formato json
     */
    public String converterParaJsonString(Map<String, String> nodes) {
        try {
            if (CollectionUtils.isEmpty(nodes)) {
                log.warn(mensagemService.getMensagem("jsonUtil.negocial.1", nodes));
                return "";
            }
            ObjectNode rootNode = objectMapper.createObjectNode();
            nodes.forEach(rootNode::put);
            return objectMapper.writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            log.log(Level.FATAL, mensagemService.getMensagem("jsonUtil.erro.1", e.getMessage()));
            throw new RuntimeException(mensagemService.getMensagem("jsonUtil.erro.1", e.getMessage()), e);
        }
    }

    /**
     * A partir de um ou mais nodes retorna seu valor em json string
     *
     * @param nodes a serem convertidos em json string
     * @return json string
     */
    public String converterParaJsonString(ObjectNode... nodes) {
        try {
            return objectMapper.writeValueAsString(nodes);
        } catch (JsonProcessingException e) {
            log.error(mensagemService.getMensagem("jsonUtil.erro.1", e.getMessage()));
            throw new RuntimeException(mensagemService.getMensagem("jsonUtil.erro.1", e.getMessage()), e);
        }
    }

    /**
     * Cria um array json node a partir do array passado
     *
     * @param nodeName json node name
     * @param itens    array a ser adicionado no node
     * @return ObjectNode com novo array no seu conteúdo
     */
    public ObjectNode criarArrayNode(String nodeName, Object[] itens) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.createObjectNode();

        ArrayNode arrayNode = objectMapper.valueToTree(itens);
        jsonNode.set(nodeName, arrayNode);

        return jsonNode;
    }

    /**
     * Cria um json node a partir do valor passado
     *
     * @param nodeName json node name
     * @param item     valor a ser adicionado no node
     * @return ObjectNode
     */
    public ObjectNode criarNode(String nodeName, String item) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put(nodeName, item);

        return jsonNode;
    }

    /**
     * Método que converte Objects... numa string json
     *
     * @param objects varargs
     * @return <p>json string</p> ou <p>null</p> em caso de erro.
     */
    public String objetosArrayParaJsonString(Object... objects) {
        if (Objects.isNull(objects)) {
            log.warn(mensagemService.getMensagem("jsonUtil.negocial.1", objects));
            return null;
        }
        try {
            ArrayNode arrayNode = objectMapper.createArrayNode();
            for (var object : objects) {
                arrayNode.add(objectMapper.valueToTree(object));
            }
            return objectMapper.writeValueAsString(arrayNode);
        } catch (Exception e) {
            log.error(mensagemService.getMensagem("jsonUtil.erro.1", e.getMessage()));
            return null;
        }
    }

    /**
     * Método que converte objeto único numa string json
     *
     * @param object
     * @return <p>json string</p> ou <p>null</p> em caso de erro.
     */
    public String objetoParaJsonString(Object object) {
        if (Objects.isNull(object)) {
            log.warn(mensagemService.getMensagem("jsonUtil.negocial.1", object));
            return null;
        }
        try {
            return objectMapper.writeValueAsString(objectMapper.valueToTree(object));
        } catch (Exception e) {
            log.error(mensagemService.getMensagem("jsonUtil.erro.1", e.getMessage()));
            return null;
        }
    }
}
