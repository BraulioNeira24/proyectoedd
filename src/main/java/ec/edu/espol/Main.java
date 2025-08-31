package ec.edu.espol;

import java.util.Comparator;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
// import javafx.scene.control.Alert;
// import javafx.scene.control.Button;
// import javafx.scene.control.ButtonType;
// import javafx.scene.control.TextField;
// import javafx.scene.control.Label;
// import javafx.scene.input.MouseEvent;
// import java.io.*;
// import java.util.*;

public class Main extends Application {
    private final String ARCHIVO_DATOS = "datosAeropuertos.dat";

    @Override
    public void start(Stage stage) {
        //Crear el Pane principal
        Pane root = new Pane(); //Crea un contenedor para los elementos gráficos.

        // Cargar la imagen del mapa 
        Image mapa = new Image(getClass().getResourceAsStream("/mapa.png"));
        double canvasWidth = 1280; // Obtener tamano de la imagen anchura
        double canvasHeight = 820; // Obtener tamano de la imagen altura
        
        // Crear el Canvas (por ejemplo, de 900x600 pixeles)
        Canvas canvas = new Canvas(canvasWidth, canvasHeight); //Crea el área de dibujo donde se mostrará el mapa y los elementos.
        root.getChildren().add(canvas); //Añade el canvas al contenedor.
        
        //Dibujar la imagen en el canvas (sin escalado)
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(mapa, 0, 0, canvasWidth, canvasHeight);

        // Crear la escena y mostrarla
        Scene scene = new Scene(root, canvasWidth, canvasHeight); //Crea la escena principal con el tamaño deseado.
        stage.setTitle("Sistema de Gestión de Aeropuertos"); //Establece el título de la ventana.
        stage.setScene(scene); //Muestra la ventana principal.
        stage.show();

        //Comparator para comprar codigos de aeropuerto 
        Comparator<String> cmp = (a,b) -> a.compareToIgnoreCase(b);
        Red red = new Red(cmp);

        //Agrega algunos aeropuertos de ejemplo
        red.addAeropuerto(new Aeropuerto("GYE","Guayaquil", "Guayaquil", "Ecuador", -2.157, -79.883));
        red.addAeropuerto(new Aeropuerto("UIO","Quito", "Quito", "Ecuador", -0.141, -78.488));
        red.addAeropuerto(new Aeropuerto("CUE","Cuenca", "Cuenca", "Ecuador", -2.900, -78.983));
        red.addAeropuerto(new Aeropuerto("MIA","Miami", "Miami", "EEUU", 25.7617, -80.1918));
        red.addAeropuerto(new Aeropuerto("LIM","Lima", "Lima", "Perú", -12.0464, -77.0428));
        red.addAeropuerto(new Aeropuerto("MAD","Madrid", "Madrid", "España", 40.4168, -3.7038));
        red.addAeropuerto(new Aeropuerto("CDG","París", "París", "Francia", 49.0097, 2.5479));
        red.addAeropuerto(new Aeropuerto("NRT","Tokio", "Tokio", "Japón", 35.7767, 140.3186));
        red.addAeropuerto(new Aeropuerto("SYD","Sídney", "Sídney", "Australia", -33.8688, 151.2093));
        red.addAeropuerto(new Aeropuerto("AKL","Auckland", "Auckland", "Nueva Zelanda", -37.0083, 174.7947));
    
        for(Aeropuerto a : red.getVertices()){
            double x = lonToX(a.getLongitud(), canvasWidth);
            double y = latToY(a.getLatitud(), canvasHeight);
            gc.setFill(Color.RED); // Color del aeropuerto
            gc.fillOval(x - 5, y - 5, 10, 10); // Dibuja un circulo de radio 5
            gc.setFill(Color.BLACK);
            gc.fillText(a.getCodigo(), x + 8, y); // Escribe el nombre al lado del aeropuerto
        }
    }

    private double lonToX(double lon,double canvasWidth){
        return ((lon+180)/360.0)*canvasWidth;  // Convierte longitud a coordenadas en X
    }
    private double latToY(double lat,double canvasHeight){
        return ((90 -lat)/180.0)* canvasHeight; // Convierte latitud a coordenadas en Y
    }

    public static void main(String[] args) {
        launch(args);
    }

    
}
