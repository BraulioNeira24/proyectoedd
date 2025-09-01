package ec.edu.espol;

import java.io.Serializable;

//Aristas
public class Vuelos implements Serializable{
    private Aeropuerto source; // vertice origen
    private Aeropuerto target; // vertice destino
    private int minuto; // minuto de vuelo
    private Double distancia; // distancia de la arista
    private double precio; // contenido de la arista
    private String aerolinea; // nombre aerolinea del vuelo

    // Constructor
    public Vuelos(Aeropuerto source, Aeropuerto target, int minuto, double precio, String aerolinea){
        this.source = source;
        this.target = target;
        this.minuto = minuto;
        this.distancia = distancia(source,target);
        this.precio = precio;
        this.aerolinea = aerolinea;
    }

    // Getters y Setters
    public Aeropuerto getSource() {
        return source;
    }

    public void setSource(Aeropuerto source) {
        this.source = source;
    }

    public Aeropuerto getTarget() {
        return target;
    }

    public void setTarget(Aeropuerto target) {
        this.target = target;
    }

    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }

    public Double getDistancia() {
        return distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getAerolinea() {
        return aerolinea;
    }

    public void setAerolinea(String aerolinea) {
        this.aerolinea = aerolinea;
    }

    public static double distancia(Aeropuerto source, Aeropuerto target){
        final int R = 6371; //Radio de la tierra

        double latRad1 = Math.toRadians(source.getLatitud());
        double latRad2 = Math.toRadians(target.getLatitud());
        double deltaLat = Math.toRadians(target.getLatitud() - source.getLatitud());
        double deltaLon = Math.toRadians(target.getLongitud() - source.getLongitud());

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
               Math.cos(latRad1) * Math.cos(latRad2) *
               Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Math.round(R * c);
    }

}