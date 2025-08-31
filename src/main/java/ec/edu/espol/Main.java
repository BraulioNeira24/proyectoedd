package ec.edu.espol;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import java.io.*;
import java.util.*;

public class Main extends Application {
    private final String ARCHIVO_DATOS = "datosAeropuertos.dat";
    private Random randomColor = new Random();
    private Map<Aeropuerto, Color> coloresAeropuertos = new HashMap<>();

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

        /*
         * AEROPUERTOS
         */
        //Comparator para comprar codigos de aeropuerto 
        Comparator<String> cmp = (a,b) -> a.compareToIgnoreCase(b);
        Red red = new Red(cmp);

        //Agrega algunos aeropuertos de ejemplo
        red.addAeropuerto(new Aeropuerto("PKX", "Daxing","Pekín","China",39.511944, 116.410556));
        red.addAeropuerto(new Aeropuerto("UIO","Quito", "Quito", "Ecuador", -0.141, -78.488));
        red.addAeropuerto(new Aeropuerto("MIA","Miami", "Miami", "EEUU", 25.7617, -80.1918));
        red.addAeropuerto(new Aeropuerto("MAD","Madrid", "Madrid", "España", 40.4168, -3.7038));
        red.addAeropuerto(new Aeropuerto("CDG","París", "París", "Francia", 49.0097, 2.5479));
        red.addAeropuerto(new Aeropuerto("NRT","Tokio", "Tokio", "Japón", 35.7767, 140.3186));
        red.addAeropuerto(new Aeropuerto("SYD","Sídney", "Sídney", "Australia", -33.8688, 151.2093));
        red.addAeropuerto(new Aeropuerto("AKL","Auckland", "Auckland", "Nueva Zelanda", -37.0083, 174.7947));
        red.addAeropuerto(new Aeropuerto("SN", "Santo Domingo", "Santo Domingo", "República Dominicana", 18.4861, -69.9312));
        red.addAeropuerto(new Aeropuerto("DXB", "Dubai International", "Dubái", "Emiratos Árabes Unidos", 25.2532, 55.3657));

        
        for(Aeropuerto a : red.getVertices()){
            // Asigna un color aleatorio si no tiene uno asignado
            Color colorAl = generarColorAleatorio();
            coloresAeropuertos.put(a,colorAl);
            double x = lonToX(a.getLongitud(), canvasWidth);
            double y = latToY(a.getLatitud(), canvasHeight);
            gc.setFill(colorAl); // Color del aeropuerto
            gc.fillOval(x - 5, y - 5, 10, 10); // Dibuja un circulo de radio 5
            gc.setFill(Color.BLACK);
            gc.fillText(a.getCodigo(), x + 8, y); // Escribe el nombre al lado del aeropuerto
        }

        /*
         * VUELOS
         */
        red.addVuelo("PKX", "DXB", 220, 200.15,"China Airlines");
        red.addVuelo("PKX", "CDG", 340, 230.23,"China Airlines");
        red.addVuelo("UIO", "MIA", 230, 600.30,"LATAM Airlines");
        red.addVuelo("NRT", "MAD" , 542, 438,"Avianca");
        red.addVuelo("UIO", "LIM", 70, 123.12,"LATAM Airlines");
        red.addVuelo("LIM", "MIA", 70, 230.30,"LATAM Airlines");
        red.addVuelo("MIA", "NRT", 340, 600.29,"American Airlines");
        red.addVuelo("SYD", "NRT", 320, 502.53,"Avianca");
        red.addVuelo("SN", "MAD", 130, 134.32,"Avianca");

        //Recorre los vuelos y dibuja las rutas
        for(Aeropuerto origen : red.getVertices()){ // Recorre los aeropuertos de la red
            for(Vuelos vuelo : origen.getAdyacentes()){ // Recorre los vuelos adyacentes de cada aeropuerto
                Aeropuerto destino = vuelo.getTarget(); // Obtiene el Aeropuerto de destino de ese vuelo
                // Calculan la posición (X, Y) del aeropuerto de origen en el canvas.
                double x1 = lonToX(origen.getLongitud(), canvasWidth);
                double y1 = latToY(origen.getLatitud(), canvasHeight);
                // Calculan la posición (X, Y) del aeropuerto de destino en el canvas.
                double x2 = lonToX(destino.getLongitud(), canvasWidth);
                double y2 = latToY(destino.getLatitud(), canvasHeight);
                // Dibuja la linea del vuelo 
                gc.setStroke(coloresAeropuertos.get(origen)); // Color de la línea del vuelo origen
                gc.setLineWidth(2); // Grosor linea
                gc.strokeLine(x1, y1, x2, y2); // Dibuja la linea que representa el vuelo
                // Dibuja la flecha en el extremo de la línea
                drawArrow(gc, x1, y1, x2, y2);
                //Calculo el punto medio 
                double midX = (x1 + x2) / 2;
                double midY = (y1 + y2) / 2;
                gc.setFill(Color.BLACK); //Color texto
                gc.fillText(vuelo.getDistancia() + " km", midX, midY - 5); // Escribe la distancia en el punto medio
            }
        }
        /*
         * ZOOM
        */








        

        /*
         * BOTONES
        */
        













    }

    // METODOS PARA CONVERTIR COORDENADAS
    private double lonToX(double lon,double canvasWidth){
        return ((lon+180)/360.0)*canvasWidth;  // Convierte longitud a coordenadas en X
    }
    private double latToY(double lat,double canvasHeight){
        return ((90 -lat)/180.0)* canvasHeight; // Convierte latitud a coordenadas en Y
    }
    
    //genera un color aleatorio
    private Color generarColorAleatorio() {
        return Color.color(randomColor.nextDouble(), randomColor.nextDouble(), randomColor.nextDouble());
    }

    
    // Dibuja una flecha entre dos puntos (Gc es el que me permite dibujar en canvas)
    private void drawArrow(GraphicsContext gc, double x1, double y1, double x2, double y2){
        double angle = Math.atan2(y2 - y1, x2 - x1); // Calcula el ángulo de la línea entre el punto de origen (x1, y1) y el destino (x2, y2)
        double arrowLength = 15; // Longitud de las líneas de la flecha
        double arrowAngle = Math.toRadians(20); //Define el ángulo de apertura de la flecha (20 grados convertidos a radianes)

        // Calculan la posición del primer lado de la flecha.
        double xArrow1 = x2 - arrowLength * Math.cos(angle - arrowAngle);
        double yArrow1 = y2 - arrowLength * Math.sin(angle - arrowAngle);

        //Calculan la posición del segundo lado de la flecha.
        double xArrow2 = x2 - arrowLength * Math.cos(angle + arrowAngle);
        double yArrow2 = y2 - arrowLength * Math.sin(angle + arrowAngle);

        //Dibuja el primer lado de la flecha
        gc.strokeLine(x2,y2,xArrow1,yArrow1);
        //Dibuja el segundo lado de la flecha
        gc.strokeLine(x2,y2,xArrow2,yArrow2);
    }
    

    public static void main(String[] args) {
        launch(args);
    }

    
}
