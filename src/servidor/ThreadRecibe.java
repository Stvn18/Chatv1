package servidor;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * 
 * @author Steven
 */
public class ThreadRecibe implements Runnable {

    private final ServidorChat principalChat;
    private final Socket socket;
    private String mensaje;
    private ObjectInputStream objectInputStream;

    /**
     * Inicializar chatServer y configurar GUI
     * @param socket
     * @param principalChat 
     */
    public ThreadRecibe(Socket socket, ServidorChat principalChat) {
        this.socket = socket;
        this.principalChat = principalChat;
    }

    public void mostrarMensaje(String mensaje) {
        principalChat.areaTexto.append(mensaje);
    }
    
    @Override()
    public void run() {
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        
        /**
         * procesa los mensajes enviados desde el cliente
         */
        
        do {
            try {
                
                /**
                 * lee el mensaje 
                 */
                mensaje = (String) objectInputStream.readObject();
                
                /**
                 * validamos si lo que trae es una operacion matematica
                 */
                if (mensaje.contains("+") || mensaje.contains("-") || mensaje.contains("*") || mensaje.contains("/")) {
                    
                    String[] _mensaje = mensaje.split(":");
                    
                    ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

                    ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("js");
                    Object calcular = scriptEngine.eval(_mensaje[1]);
                    
                    String nuevoMensaje = "Cliente: " + _mensaje[1] + " = " + calcular;
                    
                    principalChat.mostrarMensaje(nuevoMensaje);
                    
                } else {
                    
                    principalChat.mostrarMensaje(mensaje);
                    
                }
                
            } catch (SocketException ex) {
                System.err.println(ex.getMessage());
            } catch (EOFException eofException) {
                System.err.println(eofException.getMessage());
                principalChat.mostrarMensaje("Fin de la conexion");
                break;
            }catch (IOException ex) {
                System.err.println(ex.getMessage());
            } catch (ClassNotFoundException classNotFoundException) {
                System.err.println(classNotFoundException.getMessage());
                principalChat.mostrarMensaje("Objeto desconocido");
            } catch (ScriptException ex) {
                System.err.println(ex.getMessage());
            }             

        } while (!mensaje.equals("Servidor>>> TERMINATE")); //Ejecuta hasta que el server escriba TERMINATE

        try {
            /**
             * cierra input Stream
             */
            objectInputStream.close();
            
            /**
             * ciera Socket
             */
            socket.close();
        }catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        }

        principalChat.mostrarMensaje("Fin de la conexion");
        System.exit(0);
    }
}
