/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package miumg.edu.gt.nodesheet.Model;

import miumg.edu.gt.nodesheet.BD.Libro;

public class NodoB {

    public int n;
    public Libro[] claves;
    public NodoB[] hijos;
    public boolean hoja;

    public NodoB(boolean hoja) {
        this.hoja = hoja;
        this.claves = new Libro[4];
        this.hijos = new NodoB[5];
        this.n = 0;
    }
}
