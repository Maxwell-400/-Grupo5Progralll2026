package miumg.edu.gt.nodesheet.Model;

public class Celda {

    public int fila;
    public int columna;
    public String valor;
    public String formula;

    public Celda derecha;
    public Celda abajo;

    public Celda(int fila, int columna, String texto) {
        this.fila = fila;
        this.columna = columna;
        this.formula = texto;
        this.valor = texto;
        this.derecha = null;
        this.abajo = null;
    }
}
