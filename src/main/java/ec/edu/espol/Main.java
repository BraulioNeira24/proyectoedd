package ec.edu.espol;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TextInputDialog;
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
        /*  
         * Boton agregar aeropuerto
        */
        Button btnAgregarAeropuerto = new Button("Agregar Aeropuerto");
        btnAgregarAeropuerto.setLayoutX(20); //Posiciona horizontal en la ventana
        btnAgregarAeropuerto.setLayoutY(20); //Posicionavertical en la ventana
        root.getChildren().add(btnAgregarAeropuerto); // Añadir el botón a la ventana
        /*
         * Boton Reiniciar aeropuertos
        */
        Button btnReiniciar = new Button("Reiniciar Aeropuertos");
        btnReiniciar.setLayoutX(20); //Posiciona horizontal en la ventana
        btnReiniciar.setLayoutY(60); //Posiciona vertical en la ventana
        root.getChildren().add(btnReiniciar); // Añadir el botón a la ventana
        /*
         * Boton Eliminar Aeropuerto
        */
        Button btnEliminarAeropuerto = new Button("Eliminar Aeropuerto");
        btnEliminarAeropuerto.setLayoutX(20);
        btnEliminarAeropuerto.setLayoutY(100);
        root.getChildren().add(btnEliminarAeropuerto); // Añadir el botón a la ventana
        /*
         * Boton Crear Vuelo
         */
        Button btncrearVuelo = new Button("Crear Vuelo");
        btncrearVuelo.setLayoutX(20);
        btncrearVuelo.setLayoutY(140); // (elige una posición libre)
        root.getChildren().add(btncrearVuelo); // Añadir el botón a la ventana
        /*
         * Boton Ver Vuelos de Aeropuerto
         */
        Button btnVerVuelos = new Button("Ver vuelos de Aeropuerto");
        btnVerVuelos.setLayoutX(20);
        btnVerVuelos.setLayoutY(180);
        root.getChildren().add(btnVerVuelos); // Añadir el botón a la ventana
        /* 
        * Botón Buscar Ruta 
         */
        Button btnBuscarRuta = new Button("Buscar Ruta");
        btnBuscarRuta.setLayoutX(20); 
        btnBuscarRuta.setLayoutY(220);
        root.getChildren().add(btnBuscarRuta);
        /*
        * Botón Regresar (para volver al mapa completo después de buscar rutas)
        */
        Button btnRegresar = new Button("Regresar");
        btnRegresar.setLayoutX(20);
        btnRegresar.setLayoutY(260);
        btnRegresar.setVisible(false); 
        root.getChildren().add(btnRegresar);
        /*
        * Boton Eliminar Vuelo
        */
        Button btnEliminarVuelo = new Button("Eliminar Vuelo");
        btnEliminarVuelo.setLayoutX(20);
        btnEliminarVuelo.setLayoutY(300); // Ajusta la posición si es necesario
        root.getChildren().add(btnEliminarVuelo);

        //Dibujar la imagen en el canvas (sin escalado)
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(mapa, 0, 0, canvasWidth, canvasHeight);

        // Crear la escena y mostrarla
        Scene scene = new Scene(root, canvasWidth, canvasHeight); //Crea la escena principal con el tamaño deseado.
        stage.setTitle("Sistema de Gestión de Aeropuertos"); //Establece el título de la ventana.
        stage.setScene(scene); //Muestra la ventana principal.
        stage.show();

        /*
         * CREACION MAPA
         */
        //Comparator para comprar codigos de aeropuerto 
        Comparator<String> cmp = (a,b) -> a.compareToIgnoreCase(b);
        Red red = new Red(cmp);
        cargarDatosIniciales(red);
        coloresAeropuertos.clear();
        redibujarMapa(gc, mapa, canvasWidth, canvasHeight, red, coloresAeropuertos);
        /*
         * Boton Estadistica
        */
        Button btnEstadistica = new Button("Estadísticas");
        btnEstadistica.setLayoutX(20);
        btnEstadistica.setLayoutY(340);
        root.getChildren().add(btnEstadistica);
        btnEstadistica.setOnAction(e -> mostrarEstadisticasAeropuertos(red));
        /*
         * BOTONES
         *    ^_^
        */

    // Buscar Ruta
    // Si no encuentra ruta, sale un anuncio de que no existe esa ruta, pero si la encuentra, debe mostrar
    // solamente la ruta, osea que las demas conexiones de vuelos el color sea transparente y q a su a vez
    // aparezca un boton para regresar
        
        // Acción Buscar Ruta
        // Acción Buscar Rutas Alternativas
        btnBuscarRuta.setOnAction(e -> {
            TextInputDialog dOrigen = new TextInputDialog();
            dOrigen.setTitle("Buscar Todas Rutas Posibles");
            dOrigen.setHeaderText("Ingrese código del aeropuerto de origen:");
            Optional<String> rOrigen = dOrigen.showAndWait();
            if(!rOrigen.isPresent()) return;
            TextInputDialog dDestino = new TextInputDialog();
            dDestino.setTitle("Buscar Rutas Alternativas");
            dDestino.setHeaderText("Ingrese código del aeropuerto destino:");
            Optional<String> rDestino = dDestino.showAndWait();
            if(!rDestino.isPresent()) return;
            String codOrigen = rOrigen.get().trim().toUpperCase(); // Los Codgios cambiarlos a mayusculas porsiacaso
            String codDestino = rDestino.get().trim().toUpperCase();// Los Codigos cambiarlos los espacios y las mayusculas (destino)L
            List<List<Vuelos>> rutas = red.buscarRutasAlternativas(codOrigen, codDestino);
            if(rutas.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No Existe Ruta que cruze entre estos Aeropuertos", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            //Primero se Encuentra la Ruta más Corta
            List<Vuelos> rutaMasCorta = rutas.get(0);
            for(List<Vuelos> ruta : rutas){
                if(ruta.size() < rutaMasCorta.size()){
                    rutaMasCorta = ruta;
                }
            }
            // Eliminar y Redibujar Mapa
            gc.clearRect(0, 0, canvasWidth, canvasHeight);
            gc.drawImage(mapa, 0, 0, canvasWidth, canvasHeight);
            // Redibujar los Aeropuertos
            for(Aeropuerto a : red.getVertices()){
                double x = lonToX(a.getLongitud(), canvasWidth);
                double y = latToY(a.getLatitud(), canvasHeight);
                gc.setFill(Color.DARKGRAY);
                gc.fillOval(x-5, y-5, 10, 10);
                gc.setFill(Color.BLACK);
                gc.fillText(a.getCodigo(), x+8, y);
            }
            // Dibujar cada Ruta individualmente
            for(List<Vuelos> ruta : rutas){
                Color colorRuta = (ruta == rutaMasCorta) ? Color.GOLD : Color.RED;
                for(Vuelos vuelo: ruta){
                    Aeropuerto a1 = vuelo.getSource();
                    Aeropuerto a2 = vuelo.getTarget();
                    double x1 = lonToX(a1.getLongitud(), canvasWidth);
                    double y1 = latToY(a1.getLatitud(), canvasHeight);
                    double x2 = lonToX(a2.getLongitud(), canvasWidth);
                    double y2 = latToY(a2.getLatitud(), canvasHeight);
                    gc.setStroke(colorRuta);
                    gc.setLineWidth((ruta == rutaMasCorta) ? 4 : 2); // la ruta mas corta tiene mas grosor
                    gc.strokeLine(x1, y1, x2, y2);
                    drawArrow(gc, x1, y1, x2, y2);
                    // Texto explicativo de Distancia
                    double midX = (x1 + x2) / 2;
                    double midY = (y1 + y2) / 2;
                    gc.setFill(colorRuta.darker());
                    gc.fillText(vuelo.getDistancia()+ "km", midX, midY -5);
                }
            }
            btnRegresar.setVisible(true);
        });
        btnRegresar.setOnAction(e -> {
            redibujarMapa(gc, mapa, canvasWidth, canvasHeight, red, coloresAeropuertos);
            btnRegresar.setVisible(false);
        });



        // Agregar Aeropuerto
        btnAgregarAeropuerto.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog(); //crea la ventanita para pedir texto
            dialog.setTitle("Agregar Aeropuerto"); 
            dialog.setHeaderText("Ingrese el código del aeropuerto:");
            Optional<String> result = dialog.showAndWait(); //muestra la ventana y espera la respuesta
            if(result.isPresent()){ //verifica si el usuario escribió algo o canceló
                String codigo = result.get().trim().toUpperCase(); // limpia espacios y pone el código en mayúsculas
                if(red.findAeropuerto(codigo) != null){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Ya existe un aeropuerto con ese código.", ButtonType.OK);
                    alert.showAndWait(); // Muestra un mensaje de error si el aeropuerto ya existe
                    return; // Salimos del handler, no seguimos con el proceso
                }
                Alert info = new Alert(Alert.AlertType.INFORMATION, "Haz clic en el mapa para seleccionar la ubicacion del aeropuerto:", ButtonType.OK);
                info.showAndWait(); // Muestra un mensaje de información

                // Espera a que el usuario haga clic en el canvas
                canvas.setOnMouseClicked((MouseEvent ev) -> {
                    // Obtenemos las coordenadas del clic
                    double x = ev.getX(); 
                    double y = ev.getY();

                    //Convertir a coordenadas reales de la imagen
                    double escalaX = mapa.getWidth()/ canvas.getWidth();
                    double escalaY = mapa.getHeight()/ canvas.getHeight();

                    int imgX = (int)(x*escalaX);
                    int imgY = (int)(y*escalaY);

                    //Evitamos que exista un aeropuerto en el agua
                    PixelReader pixelReader = mapa.getPixelReader();
                    Color colorPixel = pixelReader.getColor(imgX, imgY);
                    if(colorPixel.getBlue() < 0.7 && colorPixel.getGreen() < 0.5 && colorPixel.getRed() < 0.4){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "No se puede colocar un aeropuerto en el agua.", ButtonType.OK);
                    alert.showAndWait();
                    return;
                    }
                    

                    
                        
                    // Convertimos x, y a la longitud y latitud usando las funciones auxiliares
                    double lon = (x / canvasWidth) * 360.0 - 180.0;
                    double lat = 90.0 - (y / canvasHeight) * 180.0;

                    TextInputDialog dNombre = new TextInputDialog();
                    dNombre.setTitle("Nombre");
                    dNombre.setHeaderText("Ingrese el nombre del aeropuerto:");
                    Optional<String> rNombre = dNombre.showAndWait();
                    if(!rNombre.isPresent()) return;

                    TextInputDialog dCiudad = new TextInputDialog();
                    dCiudad.setTitle("Ciudad");
                    dCiudad.setHeaderText("Ingrese la ciudad del aeropuerto:");
                    Optional<String> rCiudad = dCiudad.showAndWait();
                    if(!rCiudad.isPresent()) return;

                    TextInputDialog dPais = new TextInputDialog();
                    dPais.setTitle("País");
                    dPais.setHeaderText("Ingrese el país del aeropuerto:");
                    Optional<String> rPais = dPais.showAndWait();
                    if(!rPais.isPresent()) return;

                    // Si todos los datos son válidos, se crea el aeropuerto
                    String nombre = rNombre.get().trim();
                    String ciudad = rCiudad.get().trim();
                    String pais = rPais.get().trim();
                    Aeropuerto nuevoAeropuerto = new Aeropuerto(codigo, nombre, ciudad, pais, lat, lon);
                    red.addAeropuerto(nuevoAeropuerto);

                    // Dibuja el aeropuerto en el mapa
                    Color colorAl = generarColorAleatorio();
                    coloresAeropuertos.put(nuevoAeropuerto, colorAl);
                    gc.setFill(colorAl);
                    gc.fillOval(x - 5, y - 5, 10, 10);
                    gc.setFill(Color.BLACK);
                    gc.fillText(nuevoAeropuerto.getNombre(), x + 8, y);

                    // Restablece el evento de clic del mouse
                    canvas.setOnMouseClicked(null);
                });
            }
        });
        // Crear Vuelo
        btncrearVuelo.setOnAction(e -> {
            Alert info = new Alert(Alert.AlertType.INFORMATION,"Haga clic en el aeropuerto de ORIGEN", ButtonType.OK);
            info.showAndWait();
            final Aeropuerto[] origenSeleccionado = new Aeropuerto[1]; //se usan arreglos de tamaño 1 dentro el evento
            final Aeropuerto[] destinoSeleccionado = new Aeropuerto[1];
            
            canvas.setOnMouseClicked((MouseEvent ev)->{
                double x = ev.getX();
                double y = ev.getY();
                //Buscar el aeropuerto más cercano al clic
                Aeropuerto aeropuertoClic = null;
                double minDist = 10.0; // distancia mínima en píxeles para considerar que se hizo clic en un aeropuerto
                for(Aeropuerto a : red.getVertices()){
                    double ax = lonToX(a.getLongitud(), canvasWidth);
                    double ay = latToY(a.getLatitud(), canvasHeight);
                    double dist = Math.hypot(x - ax, y - ay);
                    if(dist <= minDist){
                        aeropuertoClic = a;
                        minDist = dist;
                    }
                }
                if(aeropuertoClic == null){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "No se hizo clic cerca de ningún aeropuerto.", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
                if (origenSeleccionado[0] == null){
                    origenSeleccionado[0] = aeropuertoClic;
                    Alert info2 = new Alert(Alert.AlertType.INFORMATION, "Origen seleccionado: " + aeropuertoClic.getCodigo() + ". Ahora haga clic en el aeropuerto DESTINO.", ButtonType.OK);
                    info2.showAndWait();
                } else if(destinoSeleccionado[0] == null){
                    destinoSeleccionado[0] = aeropuertoClic;
                    if(origenSeleccionado[0].equals(destinoSeleccionado[0])){
                        Alert alert = new Alert(Alert.AlertType.ERROR, "El aeropuerto de origen y destino no pueden ser el mismo.", ButtonType.OK);
                        alert.showAndWait();
                        origenSeleccionado[0] = null;
                        destinoSeleccionado[0] = null;
                        return;
                    }
                    
                    TextInputDialog dialogMinutos = new TextInputDialog();
                    dialogMinutos.setTitle("Crear Vuelo");
                    dialogMinutos.setHeaderText("Ingrese la duración del vuelo en minutos:");
                    Optional<String> resultMinutos = dialogMinutos.showAndWait();
                    if(!resultMinutos.isPresent()){
                        canvas.setOnMouseClicked(null);
                        return;                    
                    }
                    int minutos;
                    try{
                        minutos = Integer.parseInt(resultMinutos.get().trim());
                        if(minutos <= 0) throw new NumberFormatException();
                    } catch(NumberFormatException ex){
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Duración inválida. Debe ser un número entero positivo.", ButtonType.OK);
                        alert.showAndWait();
                        canvas.setOnMouseClicked(null);
                        return;
                    }
                    TextInputDialog dialogPrecio = new TextInputDialog();
                    dialogPrecio.setTitle("Crear Vuelo");
                    dialogPrecio.setHeaderText("Ingrese el precio del vuelo en dólares:");
                    Optional<String> resultPrecio = dialogPrecio.showAndWait();
                    if(!resultPrecio.isPresent()){
                        canvas.setOnMouseClicked(null);
                        return;                    
                    }
                    double precio;
                    try{
                        precio = Double.parseDouble(resultPrecio.get().trim());
                    } catch(NumberFormatException ex){
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Precio inválido. Debe ser un número.", ButtonType.OK);
                        alert.showAndWait();
                        canvas.setOnMouseClicked(null);
                        return;
                    }
                    TextInputDialog dialogAerolinea = new TextInputDialog();
                    dialogAerolinea.setTitle("Crear Vuelo");
                    dialogAerolinea.setHeaderText("Ingrese el nombre de la aerolínea:");
                    Optional<String> resultAerolinea = dialogAerolinea.showAndWait();
                    if(!resultAerolinea.isPresent()){
                        canvas.setOnMouseClicked(null);
                        return;                    
                    }
                    String aerolinea = resultAerolinea.get().trim();
                    boolean agregado = red.addVuelo(origenSeleccionado[0].getCodigo(), destinoSeleccionado[0].getCodigo(), minutos, precio, aerolinea);
                    if(agregado){
                        redibujarMapa(gc, mapa, canvasWidth, canvasHeight, red, coloresAeropuertos);
                        Alert exito = new Alert(Alert.AlertType.INFORMATION, "Vuelo creado exitosamente.", ButtonType.OK);
                        exito.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "No se pudo agregar el vuelo.", ButtonType.OK);
                        alert.showAndWait();
                    }
                    canvas.setOnMouseClicked(null);
                }
            });
        });

        btnEliminarVuelo.setOnAction(e -> {
            Alert info = new Alert(Alert.AlertType.INFORMATION, "Haga clic en el aeropuerto de ORIGEN", ButtonType.OK);
            info.showAndWait();
            final Aeropuerto[] origenSeleccionado = new Aeropuerto[1];
            final Aeropuerto[] destinoSeleccionado = new Aeropuerto[1];

            canvas.setOnMouseClicked((MouseEvent ev) -> {
                double x = ev.getX();
                double y = ev.getY();
                Aeropuerto aeropuertoClic = null;
                double minDist = 10.0;
                for (Aeropuerto a : red.getVertices()) {
                    double ax = lonToX(a.getLongitud(), canvasWidth);
                    double ay = latToY(a.getLatitud(), canvasHeight);
                    double dist = Math.hypot(x - ax, y - ay);
                    if (dist <= minDist) {
                        aeropuertoClic = a;
                        minDist = dist;
                    }
                }
                if (aeropuertoClic == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "No se hizo clic cerca de ningún aeropuerto.", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
                if (origenSeleccionado[0] == null) {
                    origenSeleccionado[0] = aeropuertoClic;
                    Alert info2 = new Alert(Alert.AlertType.INFORMATION, "Origen seleccionado: " + aeropuertoClic.getCodigo() + ". Ahora haga clic en el aeropuerto DESTINO.", ButtonType.OK);
                    info2.showAndWait();
                } else if (destinoSeleccionado[0] == null) {
                    destinoSeleccionado[0] = aeropuertoClic;
                    if (origenSeleccionado[0].equals(destinoSeleccionado[0])) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "El aeropuerto de origen y destino no pueden ser el mismo.", ButtonType.OK);
                        alert.showAndWait();
                        origenSeleccionado[0] = null;
                        destinoSeleccionado[0] = null;
                        return;
                    }
                    // Buscar y eliminar el vuelo
                    boolean eliminado = red.deleteVuelo(origenSeleccionado[0].getCodigo(), destinoSeleccionado[0].getCodigo());
                    if (eliminado) {
                        redibujarMapa(gc, mapa, canvasWidth, canvasHeight, red, coloresAeropuertos);
                        Alert exito = new Alert(Alert.AlertType.INFORMATION, "Vuelo eliminado exitosamente.", ButtonType.OK);
                        exito.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "No existe un vuelo entre esos dos aeropuertos.", ButtonType.OK);
                        alert.showAndWait();
                    }
                    canvas.setOnMouseClicked(null);
                }
            });
        });        

        // Reiniciar aeropuertos
        btnReiniciar.setOnAction(e ->{
            coloresAeropuertos.clear();
            cargarDatosIniciales(red);
            redibujarMapa(gc, mapa, canvasWidth, canvasHeight, red, coloresAeropuertos);
        });

        // Eliminar Aeropuerto
        btnEliminarAeropuerto.setOnAction(e ->{
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Eliminar Aeropuerto");
            dialog.setHeaderText("Ingrese el codigo del aeropuerto a eliminar:");
            Optional<String> result = dialog.showAndWait();
            if(result.isPresent()){
                String codigo = result.get().trim().toUpperCase();
                Aeropuerto aeropuertoEliminar = red.findAeropuerto(codigo);
                if(aeropuertoEliminar == null){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "No existe un aeropuerto con ese codigo", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
                red.eliminarAeropuerto(codigo);
                coloresAeropuertos.remove(aeropuertoEliminar);
                redibujarMapa(gc, mapa, canvasWidth, canvasHeight, red, coloresAeropuertos);
            }
        });

        // Ver vuelos Aeropuerto 
        btnVerVuelos.setOnAction(e ->{
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Ver vuelos de Aeropuerto");
            dialog.setHeaderText("Ingrese el codigo del aeropuerto:");
            Optional<String> result = dialog.showAndWait();
            if(result.isPresent()){
                String codigo = result.get().trim().toUpperCase();
                Aeropuerto aeropuerto = red.findAeropuerto(codigo);
                if(aeropuerto == null){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "No existe un aeropuerto con ese codigo", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
                List<Vuelos> vuelos = aeropuerto.getAdyacentes();
                if(vuelos.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "No hay vuelos disponibles desde este aeropuerto", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for(Vuelos v : vuelos){
                    sb.append("Destino: ").append(v.getTarget().getCodigo())
                        .append(" | Minutos: ").append(v.getMinuto())
                        .append(" | Distancia: ").append(v.getDistancia()).append(" km")
                        .append(" | Precio: $").append(v.getPrecio())
                        .append(" | Aerolínea: ").append(v.getAerolinea())
                        .append(System.lineSeparator());
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION, sb.toString(), ButtonType.OK);
                alert.setTitle("Vuelos desde " + aeropuerto.getCodigo());
                alert.setHeaderText("Vuelos desde " + aeropuerto.getNombre());
                alert.showAndWait();
            }
        });

        // Estadistica 
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
    
    //Metodo para redibujar el mapa compelto (Aeropuerto y vuelos)
    private void redibujarMapa(GraphicsContext gc, Image mapa, double canvasWidth,double canvasHeight, Red red, Map<Aeropuerto, Color> coloresAeropuerto){
        gc.clearRect(0, 0,canvasWidth, canvasHeight);
        gc.drawImage(mapa, 0, 0, canvasWidth, canvasHeight);
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
    }

    // Método para iniciar la aplicación
    private void cargarDatosIniciales(Red red){
        red.getVertices().clear();
        /*
         * AEROPUERTOS
        */
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

    }
    
    private void mostrarEstadisticasAeropuertos(Red red){
        List<Aeropuerto> aeropuertos = red.getVertices();
        if(aeropuertos.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No hay aeropuertos en la red.", ButtonType.OK);
            alert.setTitle("Estadistica de Vuelos");
            alert.setHeaderText("Estadistica de Vuelos");
            alert.showAndWait();
            return;
        }
        StringBuilder sb = new StringBuilder();
        int maxVuelos = -1;
        int minVuelos = Integer.MAX_VALUE;
        Aeropuerto aeropuertoMax = null;
        Aeropuerto aeropuertoMin = null;
        for(Aeropuerto a : aeropuertos){
            int numVuelos = a.getAdyacentes().size();
            sb.append("Aeropuerto ").append(a.getCodigo())
                .append(" (").append(a.getNombre()).append(") tiene ")
                .append(numVuelos).append(numVuelos == 1 ? " vuelo" : " vuelos").append(System.lineSeparator());
            if (numVuelos > maxVuelos) {
                maxVuelos = numVuelos;
                aeropuertoMax = a;
            }
            if (numVuelos < minVuelos) {
                minVuelos = numVuelos;
                aeropuertoMin = a;
            }
        }
        sb.append(System.lineSeparator());
        if (aeropuertoMax != null) {
            sb.append("Aeropuerto con MÁS vuelos: ")
                .append(aeropuertoMax.getCodigo()).append(" (").append(aeropuertoMax.getNombre()).append(") con ")
                .append(maxVuelos).append(maxVuelos == 1 ? " vuelo" : " vuelos").append(System.lineSeparator());
        }
        if (aeropuertoMin != null) {
            sb.append("Aeropuerto con MENOS vuelos: ")
                .append(aeropuertoMin.getCodigo()).append(" (").append(aeropuertoMin.getNombre()).append(") con ")
                .append(minVuelos).append(minVuelos == 1 ? " vuelo" : " vuelos");
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION, sb.toString(), ButtonType.OK);
        alert.setTitle("Estadísticas de Vuelos");
        alert.setHeaderText("Vuelos por Aeropuerto");
        alert.showAndWait();
    }
        

    
    public static void main(String[] args) {
        launch(args);
    }
}