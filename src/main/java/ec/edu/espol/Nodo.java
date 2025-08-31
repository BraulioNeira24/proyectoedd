package ec.edu.espol;

class Nodo<V> {
    String codigo;   // aquí guardamos el código del aeropuerto
    int dist;   // la distancia acumulada desde el origen

    public Nodo(String codigo, int dist) {
        this.codigo = codigo;
        this.dist = dist;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }
    


}