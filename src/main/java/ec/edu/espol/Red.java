package ec.edu.espol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/*
 * 
 */
//Aristas dirigidas?
public class Red implements Serializable{
    private LinkedList<Aeropuerto> vertices = new LinkedList<>();
    private transient Comparator cmp;


    public Red(Comparator cmp) {
        this.cmp = cmp;
    }

    

    public LinkedList<Aeropuerto> getVertices() {
        return vertices;
    }



    // Agregar un aeropuerto
    public boolean addAeropuerto(Aeropuerto nuevo){
        if (nuevo == null || findAeropuerto(nuevo.getCodigo()) != null){
            return false;
        }

        this.vertices.add(nuevo);
        
        return true;
    }

    
    public Aeropuerto findAeropuerto(String Codigo) {
        for (Aeropuerto v : vertices){
            String c = v.getCodigo();
            
            if (this.cmp.compare(c, Codigo) == 0){
                return v;
            }
        }
        return null;
    }

    // Eliminar Aeropuerto
    public boolean eliminarAeropuerto(String codigo){
        Aeropuerto aeropuertoxEliminar = findAeropuerto(codigo);
        if (aeropuertoxEliminar==null){
            return false; //no se puede eliminar un aeropuerto que no se encuentra
        }
        
        // Eliminar el aeropuerto de la lista de aeropuertos
        this.vertices.remove(aeropuertoxEliminar);

        // Eliminar los vuelos que tenían este aeropuerto como origen o destino
        for (Aeropuerto aeropuerto : vertices) {
            // Eliminar vuelos donde este aeropuerto sea el origen
            aeropuerto.getAdyacentes().removeIf(vuelo -> vuelo.getSource().equals(aeropuertoxEliminar));
            //se hace uso de una funcion lambda 
            // Eliminar vuelos donde este aeropuerto sea el destino
            aeropuerto.getAdyacentes().removeIf(vuelo -> vuelo.getTarget().equals(aeropuertoxEliminar));
        }
        
        return true;

    }
    
    
    // Conectar Aeropuertos "Crear Vuelo"
    public boolean addVuelo(String codigo1, String codigo2, int minuto, double precio, String aerolinea){
        if (codigo1==null || codigo2==null){
            return false;
        }
        
        Aeropuerto v1= findAeropuerto(codigo1);
        Aeropuerto v2= findAeropuerto(codigo2);
        
        if (v1==null || v2 == null){
            return false;
        }

        Vuelos newEdge = new Vuelos(v1, v2, minuto, precio, aerolinea);
        v1.getAdyacentes().add(newEdge);
        
        return true;
    } 
    
    // Eliminar Vuelo 
    public boolean deleteVuelo(String codigo1, String codigo2){
        Aeropuerto origen = findAeropuerto(codigo1);
        Aeropuerto destino = findAeropuerto(codigo2);
        // Existen los Aeropuertos?
        if(origen == null || destino == null){
            return false;
        }
        // Buscar y eliminar en la lista de vuelos 
        boolean eliminado = origen.getAdyacentes().removeIf(vuelo -> vuelo.getTarget().equals(destino));
        return eliminado;
    }

    
    // Encontrar ruta mas corta 
    public List<Vuelos> dijkstraVuelos(String codigoOrigen, String codigoDestino) {
        Map<Aeropuerto, Vuelos> predecesor = new HashMap<>();
        Map<Aeropuerto, Double> distancia = new HashMap<>();
        Set<Aeropuerto> visitados = new HashSet<>();
        PriorityQueue<Aeropuerto> cola = new PriorityQueue<>((a1, a2) -> Double.compare(distancia.get(a1), distancia.get(a2))); // Comparamos por distancia

        Aeropuerto origen = findAeropuerto(codigoOrigen);
        Aeropuerto destino = findAeropuerto(codigoDestino);
        if (origen == null || destino == null) 
            return new ArrayList<>();

        for (Aeropuerto a : vertices) {
            distancia.put(a, Double.POSITIVE_INFINITY);
        }
        distancia.put(origen, 0.0);
        cola.add(origen);

        while (!cola.isEmpty()) {
            Aeropuerto actual = cola.poll();
            // Si el aeropuerto actual es el destino, reconstruimos la ruta.
            if (actual.equals(destino)) {
                List<Vuelos> ruta = new ArrayList<>();
                Aeropuerto current = destino;
                while (predecesor.containsKey(current)) {
                    Vuelos v = predecesor.get(current);
                    ruta.add(0, v);  // Insertamos al inicio para que la ruta sea correcta
                    current = v.getSource();
                }
                if (ruta.isEmpty() || !ruta.get(0).getSource().equals(origen)) {
                    return new ArrayList<>(); // En el caso de que no haya ruta, devolvemos una lista vacía
                }
                return ruta;  // Si la ruta es encontrada entonces la retornamos.
            }

            // Si no es el destino entonces procesamos el aeropuerto actual.
            if (!visitados.contains(actual)) {
                visitados.add(actual);

                for (Vuelos v : actual.getAdyacentes()) {
                    Aeropuerto vecino = v.getTarget();
                    double peso = v.getDistancia();
                    if (!visitados.contains(vecino) && distancia.get(actual) + peso < distancia.get(vecino)) {
                        distancia.put(vecino, distancia.get(actual) + peso);
                        predecesor.put(vecino, v);
                        cola.add(vecino);
                    }
                }
            }
        }

        // Si no se encuentra ninguna ruta, devolvemos una lista vacía.
        return new ArrayList<>();
    }


    //Aeropuerto con más conexiones
    public Aeropuerto aeropuertoMasConectado() {
        Aeropuerto masConectado = null;
        int maxConexiones = 0;
        // Recorrer todos los aeropuertos
        for (Aeropuerto aeropuerto : vertices) {
            // Obtener el número de conexiones salientes del aeropuerto
            int conexiones = aeropuerto.getAdyacentes().size();
            // Comprobar si el número de conexiones es mayor que el mayor actual
                if (maxConexiones < conexiones) {
                    maxConexiones = conexiones;
                    masConectado = aeropuerto;
                }
            }
            return masConectado;
        }


    
    //  Los vuelos que salen del aeropuerto
    public int conexionesDeAeropuerto(String codigo) {
        Aeropuerto aeropuerto = findAeropuerto(codigo);
        if (aeropuerto == null) {
            return -1; // si no existe el aeropuerto, devuelve -1 
        }
        return aeropuerto.getAdyacentes().size();
    }
    
    //Aeropuerto con menor conexion
    public Aeropuerto aeropuertoMenosConectado() {
        Aeropuerto menosConectado = null;
        int maxConexiones = Integer.MAX_VALUE;
        // Recorrer todos los aeropuertos
        for (Aeropuerto aeropuerto : vertices) {
            // Obtener el número de conexiones salientes del aeropuerto
            int conexiones = aeropuerto.getAdyacentes().size();
            // Comprobar si el número de conexiones es menor que el mínimo actual
            if (conexiones < maxConexiones) {
                maxConexiones = conexiones;
                menosConectado = aeropuerto;
            }
        }
        return menosConectado;
    }

    // Busqueda de rutas alternativas 
    public List<List<Vuelos>> buscarRutasAlternativas(String origen, String destino){
        List<List<Vuelos>> rutas = new ArrayList<>();
        Aeropuerto aeropuertoOrigen = findAeropuerto(origen);
        Aeropuerto aeropuertoDestino = findAeropuerto(destino);
        // Comprobar si los aeropuertos existen 
        if(aeropuertoOrigen == null || aeropuertoDestino == null) {
            return rutas;
        }
        List<Vuelos> rutaActual = new ArrayList<>();
        List<Aeropuerto> visitados = new ArrayList<>();
        buscarRutasDFS(aeropuertoOrigen, aeropuertoDestino, rutaActual, rutas, visitados);
        return rutas;
    }
    // Metodo Auxiliar
    private void buscarRutasDFS(Aeropuerto actual, Aeropuerto destino, List<Vuelos> rutaActual, List<List<Vuelos>> rutas, List<Aeropuerto> visitados){
        // Si el actual es igual al destino
        if(actual.equals(destino)){
            rutas.add(new ArrayList<>(rutaActual));
            return;
        }
        visitados.add(actual);
        for(Vuelos vuelo : actual.getAdyacentes()){
            Aeropuerto siguiente = vuelo.getTarget();
            if(!visitados.contains(siguiente)){
                rutaActual.add(vuelo);
                buscarRutasDFS(siguiente, destino, rutaActual, rutas, visitados);
                rutaActual.remove(rutaActual.size() - 1); 
            }
        }
        visitados.remove(visitados.size() - 1);
    }
    
    //Buscar vuelos por Aerolinea 
    public List<Vuelos> buscarVuelosPorAerolinea(String aerolinea){
        List<Vuelos> vuelosEncontrados = new ArrayList<>();
        for(Aeropuerto aeropuerto : vertices){
            for(Vuelos vuelo : aeropuerto.getAdyacentes()){
                if(vuelo.getAerolinea() != null && vuelo.getAerolinea().equalsIgnoreCase(aerolinea)){
                    vuelosEncontrados.add(vuelo);
                }
            }
        }
        return vuelosEncontrados;
    }

    // Metodo que devuelve todas las rutas posibles como lista de códigos de aeropuerto
    public List<List<String>> buscarTodasLasRutas(String origen, String destino) {
        List<List<String>> rutas = new ArrayList<>();
        Aeropuerto aeropuertoOrigen = findAeropuerto(origen);
        Aeropuerto aeropuertoDestino = findAeropuerto(destino);
        if (aeropuertoOrigen == null || aeropuertoDestino == null) return rutas;
        List<String> rutaActual = new ArrayList<>();
        Set<Aeropuerto> visitados = new HashSet<>();
        dfsRutas(aeropuertoOrigen, aeropuertoDestino, rutaActual, rutas, visitados);
        return rutas;
    }

    // Método auxiliar para realizar DFS y encontrar todas las rutas
    private void dfsRutas(Aeropuerto actual, Aeropuerto destino, List<String> rutaActual, List<List<String>> rutas, Set<Aeropuerto> visitados) {
        rutaActual.add(actual.getCodigo());
        if (actual.equals(destino)) {
            rutas.add(new ArrayList<>(rutaActual));
        } else {
            visitados.add(actual);
            for (Vuelos vuelo : actual.getAdyacentes()) {
                Aeropuerto siguiente = vuelo.getTarget();
                if (!visitados.contains(siguiente)) {
                    dfsRutas(siguiente, destino, rutaActual, rutas, visitados);
                }
            }
            visitados.remove(actual);
        }
        rutaActual.remove(rutaActual.size() - 1);
    }

    // Método para calcular el costo total de una ruta
    // tipo puede ser "precio", "minuto" o "distancia"
    public double calcularCostoTotal(List<Vuelos> ruta, String tipo) {
        double total = 0;
        for (Vuelos v : ruta) {
            if ("precio".equalsIgnoreCase(tipo)) {
                total += v.getPrecio();
            } else if ("minuto".equalsIgnoreCase(tipo)) {
                total += v.getMinuto();
            } else if ("distancia".equalsIgnoreCase(tipo)) {
                total += v.getDistancia();
            }
        }
        return total;
    }
    // Método para encontrar vuelos directos entre dos aeropuertos
    public List<Vuelos> vuelosDirectos(String codigoOrigen, String codigoDestino) {
        Aeropuerto origen = findAeropuerto(codigoOrigen);
        Aeropuerto destino = findAeropuerto(codigoDestino);
        List<Vuelos> directos = new ArrayList<>();
        if (origen == null || destino == null) return directos;
        for (Vuelos v : origen.getAdyacentes()) {
            if (v.getTarget().equals(destino)) {
                directos.add(v);
            }
        }
        return directos;
    }
    // Método para encontrar todos los aeropuertos alcanzables desde un aeropuerto dado
    public Set<Aeropuerto> aeropuertosAlcanzables(String codigoOrigen) {
    Set<Aeropuerto> alcanzables = new HashSet<>();
    Aeropuerto origen = findAeropuerto(codigoOrigen);
    if (origen == null) return alcanzables;
    Queue<Aeropuerto> cola = new LinkedList<>();
    cola.add(origen);
    alcanzables.add(origen);
    while (!cola.isEmpty()) {
        Aeropuerto actual = cola.poll();
        for (Vuelos v : actual.getAdyacentes()) {
            Aeropuerto siguiente = v.getTarget();
            if (alcanzables.add(siguiente)) {
                cola.add(siguiente);
            }
        }
    }
    alcanzables.remove(origen); 
    return alcanzables;
}
    public void setComparator(Comparator cmp) {
    this.cmp = cmp;
}
}