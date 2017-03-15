package servidor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**Clase que se encarga de correr los threads de enviar y recibir texto
 * y de crear la interfaz grafica.
 * 
 * @author Steven
 */
public class ServidorChat extends JFrame{
    
    /**
     * Para mostrar mensajes de los usuarios
     */
    public JTextField campoTexto;
    
    /**
     * Para ingresar mensaje a enviar
     */
    public JTextArea areaTexto;
    private static ServerSocket servidor;
    
    /**
     * Socket para conectarse con el cliente
     */
    private static Socket conexion;
    
    /**
     * ip a la cual se conecta
     */
    private static final String IP = "127.0.0.1";
    
    public static ServidorChat main; 
    
    public ServidorChat(){
        
        /**
         * Establece titulo al Frame
         */
        super("Servidor");
        
        /**
         * crea el campo para texto
         */
        campoTexto = new JTextField();
        
        /**
         * No permite que sea editable el campo de texto
         */
        campoTexto.setEditable(false); 
        
        /**
         * Coloca el campo de texto en la parte superior
         */
        add(campoTexto, BorderLayout.NORTH);
        
        areaTexto = new JTextArea();
        
        /**
         * Crear displayArea
         */
        areaTexto.setEditable(false);
        add(new JScrollPane(areaTexto), BorderLayout.CENTER);
        
        /**
         * Pone de color cyan al areaTexto
         */
        areaTexto.setBackground(Color.WHITE);
        
        /**
         * pinta azul la letra en el areaTexto
         */
        areaTexto.setForeground(Color.BLACK);
        
        /**
         * pinta toja la letra del mensaje a enviar
         */
        campoTexto.setForeground(Color.BLACK);
        
        
        /**
         * Crea menu Archivo y submenu Salir, ademas agrega el submenu al menu
         */
        JMenu menuArchivo = new JMenu("Archivo"); 
        JMenuItem salir = new JMenuItem("Salir");
        
        /**
         * Agrega el submenu Salir al menu menuArchivo
         */
        menuArchivo.add(salir);
        
        /**
         * Crea la barra de menus
         */
        JMenuBar barra = new JMenuBar();
        
        /**
         * Agrega barra de menus a la aplicacion
         */
        setJMenuBar(barra);
        
        /**
         * agrega menuArchivo a la barra de menus
         */
        barra.add(menuArchivo);
        
        /**
         * Accion que se realiza cuando se presiona el submenu Salir
         */
        salir.addActionListener(new ActionListener() {
            
                /**
                 * clase interna anonima
                 * @param e 
                 */
                public void actionPerformed(ActionEvent e) {
                    
                    /**
                     * Sale de la aplicacion
                     */
                    System.exit(0);
                }
        });
        
        setSize(450, 320); //Establecer tamano a ventana
        setVisible(true); //Pone visible la ventana
    }
    
    //Para mostrar texto en displayArea
    public void mostrarMensaje(String mensaje) {
        areaTexto.append(mensaje + "\n");
    } 
    public void habilitarTexto(boolean editable) {
        campoTexto.setEditable(editable);
    }
 
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ServidorChat main = new ServidorChat(); //Instanciacion de la clase Principalchat
        main.setLocationRelativeTo(null);   //Centrar el JFrame
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //habilita cerrar la ventana
        ExecutorService executor = Executors.newCachedThreadPool(); //Para correr los threads
 
        try {
            
            servidor = new ServerSocket(11111, 100); 
            main.mostrarMensaje("Esperando Cliente ...");

            /**
             * Bucle infinito para esperar conexiones de los clientes
             */
            while (true){
                try {
                    /**
                     * Permite al servidor aceptar conexiones
                     */
                    conexion = servidor.accept();        
                    
                    main.mostrarMensaje("Conectado a : " + conexion.getInetAddress().getHostName());

                    main.habilitarTexto(true); //permite escribir texto para enviar

                    /**
                     * Ejecucion de los threads
                     */
                    executor.execute(new ThreadRecibe(conexion, main)); //client
                    executor.execute(new ThreadEnvia(conexion, main));
                } catch (IOException ex) {
                    Logger.getLogger(ServidorChat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServidorChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        executor.shutdown();
    }
}
