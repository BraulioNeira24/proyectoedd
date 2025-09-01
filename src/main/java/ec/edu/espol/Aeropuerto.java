package ec.edu.espol;

import java.io.Serializable;
import java.util.LinkedList;

public class Aeropuerto implements Serializable{
    //Atributos 
    private String codigo; // codigo del aeropuerto
    private String nombre; // nombre del aeropuerto
    private String ciudad; // ciudad del aeropuerto
    private String pais; // pais del aeropuerto
    private Double latitud; // Coordenadas del aeropuerto
    private Double longitud;
    private LinkedList<Vuelos> adyacentes; // lista de vuelos que salen del aeropuerto

    //Constructor
    public Aeropuerto(String codigo, String nombre, String ciudad, String pais, Double latitud, Double longitud){
        this.codigo = codigo; 
        this.nombre = nombre;
        this.ciudad = ciudad; 
        this.pais = pais; 
        this.latitud = latitud;
        this.longitud = longitud;
        this.adyacentes = new LinkedList<>();
    }

    // Getters y Setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public LinkedList<Vuelos> getAdyacentes() {
        return adyacentes;
    }

    public void setAdyacentes(LinkedList<Vuelos> adyacentes) {
        this.adyacentes = adyacentes;
    }

    // Equals 
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Aeropuerto other = (Aeropuerto) obj;
        return codigo != null && codigo.equals(other.codigo);
    }

    // HashCode
    public int hashCode(){
        return codigo != null ? codigo.hashCode() : 0;
    }
}
