package servidor;

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

    private final ServidorChat servidorChat;
    private final Socket socket;
    private ObjectOutputStream objectOutputStream;
    private String mensaje;
    
    /**
     * 
     * @param socket
     * @param servidorChat 
     */
    public ThreadEnvia(Socket socket, final ServidorChat servidorChat) {
        this.socket = socket;
        this.servidorChat = servidorChat;

        /**
         * Evento que ocurre al escribir en el areaTexto
         */
        servidorChat.campoTexto.addActionListener(new ActionListener() {
            
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
                servidorChat.campoTexto.setText("");
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
            objectOutputStream.writeObject("Servidor: " + mensaje);
            
            /**
             * flush salida a cliente
             */
            objectOutputStream.flush();
            servidorChat.mostrarMensaje("Yo: " + mensaje);
        }
        catch (IOException ioException) {
            System.err.println(ioException.getMessage());
            servidorChat.mostrarMensaje("Error escribiendo Mensaje");
        }

    }

    /**
     * manipula areaPantalla en el hilo despachador de eventos
     * @param mensaje 
     */
    public void mostrarMensaje(String mensaje) {
        servidorChat.areaTexto.append(mensaje);
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
