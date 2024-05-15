package OPC;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;

public class opcua {

    private static OpcUaClient client;

    public static void connect(String endpointUrl) throws Exception {
        // Cria um novo cliente OPC UA
        client = OpcUaClient.create(endpointUrl);

        // Conecta ao servidor OPC UA
        client.connect().get();

    }

    public static void disconnect() throws Exception {
        // Desconecta do servidor OPC UA
        client.disconnect().get();
    }

    public static Variant read(String nodeIdIdentifier) throws Exception {
        // Define o nó que você deseja ler
        NodeId nodeId = new NodeId(4, nodeIdIdentifier);

        // Lê o valor atual do nó
        DataValue dataValue = client.readValue(0, TimestampsToReturn.Both, nodeId).get();

        // Imprime o valor atual do nó-
        //  System.out.println("Valor atual da variante: " + dataValue.getValue());

        return dataValue.getValue();
    }

    public static void write(String nodeIdIdentifier, Variant newValue) throws Exception {
        // Define o nó que você deseja alterar
        NodeId nodeId = new NodeId(4, nodeIdIdentifier);

        // Cria um novo valor para escrever no nó
        DataValue dataValue = new DataValue(newValue);

        // Escreve o novo valor no nó
        StatusCode statusCode = client.writeValue(nodeId, dataValue).get();

        // Verifica se a escrita foi bem sucedida
        if (statusCode.isGood()) {
          System.out.println("Valor alterado com sucesso.");
        } else {
            System.err.println("Falha ao alterar o valor. StatusCode = " + statusCode);
        }

        // Lê o valor atual do nó
       DataValue currentValue = client.readValue(0, TimestampsToReturn.Both, nodeId).get();

        //   Imprime o valor alterado do nó
       System.out.println("Valor alterado para: " + currentValue.getValue());
        //  }

    }
}
