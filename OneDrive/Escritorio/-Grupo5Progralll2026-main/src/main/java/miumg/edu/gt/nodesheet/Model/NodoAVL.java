/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package miumg.edu.gt.nodesheet.Model;

/**
 *
 * @author danie
 */
import miumg.edu.gt.nodesheet.BD.Libro;

public class NodoAVL {

    public Libro dato;
    public NodoAVL izquierdo;
    public NodoAVL derecho;
    public int altura;

    public NodoAVL(Libro dato) {
        this.dato = dato;
        this.altura = 1;
    }
}