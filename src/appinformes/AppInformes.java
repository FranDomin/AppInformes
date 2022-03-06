
package appinformes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/*
 * @author Francisco Dominguez Ruiz
 */
public class AppInformes extends Application 
{
    
    public static Connection conexion = null;

    
    @Override
    public void start(Stage primaryStage) 
    {
        conectaBD();
        
        TextField tituloIntro = new TextField("1");
        
        MenuBar barraMenu = new MenuBar();
        Menu menuInformes = new Menu("Informes");
        MenuItem facturas = new MenuItem("Facturas");
        MenuItem ventas = new MenuItem("Ventas");
        MenuItem cliente = new MenuItem("Facturas/Cliente");
        MenuItem facturas2 = new MenuItem("Facturas (Subinformes)");
        
        barraMenu.getMenus().add(menuInformes);
        menuInformes.getItems().add(facturas);
        menuInformes.getItems().add(ventas);
        menuInformes.getItems().add(cliente);
        menuInformes.getItems().add(facturas2);
        
        facturas.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                generaInforme(tituloIntro, 1);
                System.out.println("Generando informe");
            }
        });
        
        facturas2.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                generaInforme(tituloIntro, 4);
                System.out.println("Generando informe");
            }
        });
        
        ventas.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                generaInforme(tituloIntro, 2);
                System.out.println("Generando informe");
            }
        });
        
        cliente.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                generaInforme(tituloIntro, 3);
                System.out.println("Generando informe");
            }
        });
        
        VBox root = new VBox(barraMenu);
        root.getChildren().add(tituloIntro);
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("AppInformes");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void conectaBD()
    {
        //Establecemos conexión con la BD
        String baseDatos = "jdbc:hsqldb:hsql://localhost:9001/test";
        String usuario = "sa";
        String clave = "";
        
        try
        {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
            conexion = DriverManager.getConnection(baseDatos,usuario,clave);
        }
        catch (ClassNotFoundException cnfe)
        {
            System.err.println("Fallo al cargar JDBC");
            System.exit(1);
        }
        catch (SQLException sqle)
        {
            System.err.println("No se pudo conectar a BD");
            System.exit(1);
        }
        catch (java.lang.InstantiationException sqlex)
        {
            System.err.println("Imposible Conectar");
            System.exit(1);
        }
        catch (Exception ex)
        {
            System.err.println("Imposible Conectar");
            System.exit(1);
        }
    }
    
    public void generaInforme(TextField tintro, int num) 
    { 
        try 
        {
            if(num == 1)
            {
                JasperReport jr =(JasperReport)JRLoader.loadObject(AppInformes.class.getResource("/informes/Facturas.jasper"));
                //Map de parámetros
                Map parametros = new HashMap();

                JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr,parametros, conexion);
                JasperViewer.viewReport(jp);
            }
            else if(num == 2)
            {
                JasperReport jr =(JasperReport)JRLoader.loadObject(AppInformes.class.getResource("/informes/VentasTotales.jasper"));
                //Map de parámetros
                Map parametros = new HashMap();

                JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr,parametros, conexion);
                JasperViewer.viewReport(jp);
            }
            else if(num == 3)
            {
                JasperReport jr =(JasperReport)JRLoader.loadObject(AppInformes.class.getResource("/informes/FacturasCliente.jasper"));
                //Map de parámetros
                Map parametros = new HashMap();
                int nproducto = Integer.valueOf(tintro.getText());
                parametros.put("ParamProducto", nproducto);

                JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr,parametros, conexion);
                JasperViewer.viewReport(jp);
            }
            else
            {
                JasperReport jr =(JasperReport)JRLoader.loadObject(AppInformes.class.getResource("/informes/Facturas2.jasper"));
                JasperReport jsr =(JasperReport)JRLoader.loadObject(AppInformes.class.getResource("/informes/Facturas2Sub.jasper"));
                //Map de parámetros
                Map parametros = new HashMap();
                parametros.put("SubInformeFacturas", jsr);

                JasperPrint jp = (JasperPrint) JasperFillManager.fillReport(jr,parametros, conexion);
                JasperViewer.viewReport(jp, false);
            }
        } 
        catch (JRException ex) 
        {
            System.out.println("Error al recuperar el jasper");
            JOptionPane.showMessageDialog(null, ex);
        }
    }


    public static void main(String[] args) 
    {
        launch(args);
    }
    
}
