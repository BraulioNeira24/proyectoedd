package ec.edu.espol;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
// import javafx.scene.control.Alert;
// import javafx.scene.control.Button;
// import javafx.scene.control.ButtonType;
// import javafx.scene.control.TextField;
// import javafx.scene.control.Label;
// 

// import javafx.scene.paint.Color;
// import javafx.scene.input.MouseEvent;


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

    
    }




    public static void main(String[] args) {
        launch(args);
    }

    
}
