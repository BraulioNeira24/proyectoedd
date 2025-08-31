package ec.edu.espol;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {
    private double lastX = -1, lastY = -1;

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();

        // Lienzo para probar dibujo con el mouse
        Canvas canvas = new Canvas(800, 500);
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setFill(Color.WHITE);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g.setStroke(Color.DODGERBLUE);
        g.setLineWidth(2);

        // Controles básicos
        Label lbl = new Label("Nombre:");
        TextField txt = new TextField();
        Button btnSaludar = new Button("Saludar");
        Button btnLimpiar = new Button("Limpiar");
        Button btnImagen = new Button("Imagen");

        // Posicionamiento simple (rápido para “prueba”)
        lbl.setLayoutX(10);   lbl.setLayoutY(510);
        txt.setLayoutX(70);   txt.setLayoutY(505); txt.setPrefWidth(220);
        btnSaludar.setLayoutX(300); btnSaludar.setLayoutY(505);
        btnLimpiar.setLayoutX(380); btnLimpiar.setLayoutY(505);
        btnImagen.setLayoutX(460);  btnImagen.setLayoutY(505);

        // Dibujo con el mouse
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> { lastX = e.getX(); lastY = e.getY(); });
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (lastX >= 0) g.strokeLine(lastX, lastY, e.getX(), e.getY());
            lastX = e.getX(); lastY = e.getY();
        });
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> { lastX = lastY = -1; });

        // Acciones
        btnSaludar.setOnAction(ev -> {
            String name = txt.getText();
            String msg = (name == null || name.isBlank()) ? "¡Hola, mundo!" : "¡Hola, " + name.trim() + "!";
            new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
        });

        btnLimpiar.setOnAction(ev -> {
            var confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Limpiar el lienzo?", ButtonType.OK, ButtonType.CANCEL);
            Optional<ButtonType> res = confirm.showAndWait();
            if (res.isPresent() && res.get() == ButtonType.OK) {
                g.setFill(Color.WHITE);
                g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                g.setFill(Color.BLACK);
                g.setStroke(Color.DODGERBLUE);
            }
        });

        btnImagen.setOnAction(ev -> {
            try {
                Image img = new Image("https://upload.wikimedia.org/wikipedia/commons/4/42/JavaFX_Logo.png", 160, 0, true, true);
                g.drawImage(img, 10, 10);
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "No se pudo cargar la imagen.").showAndWait();
            }
        });

        // Ajuste de tamaño al redimensionar
        root.widthProperty().addListener((o, a, w) -> canvas.setWidth(w.doubleValue()));
        root.heightProperty().addListener((o, a, h) -> canvas.setHeight(Math.max(h.doubleValue() - 60, 100)));

        root.getChildren().addAll(canvas, lbl, txt, btnSaludar, btnLimpiar, btnImagen);

        stage.setScene(new Scene(root, 920, 560, Color.WHITE));
        stage.setTitle("Prueba JavaFX");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
