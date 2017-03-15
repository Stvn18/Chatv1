package cliente;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;

/**
 *
 * @author Steven
 */
public class ThreadEnvia implements Runnable {

    private final ClienteChat principalChat;
    private final Socket socket;

    private ObjectOutputStream objectOutputStream;
    private String mensaje;

    public ThreadEnvia(Socket socket, final ClienteChat principalChat) {
        this.socket = socket;
        this.principalChat = principalChat;

        /**
         * Evento que ocurre al escribir en el campo de texto
         */
        principalChat.campoTexto.addActionListener(new ActionListener() {

            @Override()
            public void actionPerformed(ActionEvent event) {
                mensaje = event.getActionCommand();
                /**
                 * se envia el mensaje
                 */
                enviarDatos(mensaje);

                /**
                 * borra el texto del enterfield
                 */
                principalChat.campoTexto.setText("");
            }
        }
        );
    }

    /**
     * enviar objeto a cliente
     * @param mensaje 
     */ 
    private void enviarDatos(String mensaje) {
        try {
            objectOutputStream.writeObject("Cliente: " + mensaje);
            
            /**
             * flush salida a cliente
             */
            objectOutputStream.flush();
            
            principalChat.mostrarMensaje("Yo: " + mensaje);
        }
        catch (IOException ioException) {
            System.err.println(ioException.getMessage());
            principalChat.mostrarMensaje("Error escribiendo Mensaje");
        }

    }

    /**
     * manipula areaPantalla en el hilo despachador de eventos
     * @param mensaje 
     */
    public void mostrarMensaje(String mensaje) {
        principalChat.areaTexto.append(mensaje);
    }
    
    @Override()
    public void run() {
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.flush();
        } catch (SocketException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        } catch (NullPointerException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
