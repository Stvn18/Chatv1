package cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;

/**Clase que se encarga de correr los threads de enviar y recibir texto
 * y de crear la interfaz grafica.
 * 
 * @author Steven
 */
public class ClienteChat extends JFrame{
    
    /**
     * Para mostrar mensajes de los usuarios
     */
    public JTextField campoTexto;
    
    /**
     * Para ingresar mensaje a enviar
     */
    public JTextArea areaTexto;
    
    /**
     * Socket para conectarse con el cliente
     */
    private static Socket socket;
    
    /**
     * ip a la cual se conecta
     */
    private static final String IP = "127.0.0.1";
    
    public ClienteChat(){
        
        /**
         * Establece titulo al Frame
         */
        super("Cliente");
 
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
        
        /**
         * Crear displayArea
         */
        areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        add(new JScrollPane(areaTexto), BorderLayout.CENTER);
        
        /**
         * Pone de color cyan al displayArea
         */
        areaTexto.setBackground(Color.WHITE);
        
        /**
         * pinta azul la letra en el displayArea
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
        
        JMenuBar barra = new JMenuBar(); //Crea la barra de menus
        setJMenuBar(barra); //Agrega barra de menus a la aplicacion
        barra.add(menuArchivo); //agrega menuArchivo a la barra de menus
        
        //Accion que se realiza cuando se presiona el submenu Salir
        salir.addActionListener(new ActionListener() { //clase interna anonima
                public void actionPerformed(ActionEvent e) {
                    System.exit(0); //Sale de la aplicacion
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
     * 
     * @param args 
     */
    public static void main(String[] args) {
        ClienteChat main = new ClienteChat(); //Instanciacion de la clase Principalchat
        main.setLocationRelativeTo(null);   //Centrar el JFrame
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //habilita cerrar la ventana
        ExecutorService executor = Executors.newCachedThreadPool(); //Para correr los threads
 
        try {
            main.mostrarMensaje("Buscando Servidor ...");
            
            /**
             * comunicarme con el servidor
             */
            socket = new Socket(InetAddress.getByName(IP), 11111);
            
            main.mostrarMensaje("Conectado a :" + socket.getInetAddress().getHostName());
    
            main.habilitarTexto(true); //habilita el texto
            
            /**
             * Ejecucion de los Threads
             */
            executor.execute(new ThreadRecibe(socket, main));
            executor.execute(new ThreadEnvia(socket, main)); 
            
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        executor.shutdown();
    }
}
