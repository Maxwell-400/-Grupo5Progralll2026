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

public class ArbolAVL {

    private NodoAVL raiz;
    public int rotaciones = 0;

    private int altura(NodoAVL n) {
        return (n == null) ? 0 : n.altura;
    }

    private int balance(NodoAVL n) {
        return (n == null) ? 0 : altura(n.izquierdo) - altura(n.derecho);
    }

    private NodoAVL rotarDerecha(NodoAVL y) {
        rotaciones++;

        NodoAVL x = y.izquierdo;
        NodoAVL T2 = x.derecho;

        x.derecho = y;
        y.izquierdo = T2;

        y.altura = Math.max(altura(y.izquierdo), altura(y.derecho)) + 1;
        x.altura = Math.max(altura(x.izquierdo), altura(x.derecho)) + 1;

        return x;
    }

    private NodoAVL rotarIzquierda(NodoAVL x) {
        rotaciones++;

        NodoAVL y = x.derecho;
        NodoAVL T2 = y.izquierdo;

        y.izquierdo = x;
        x.derecho = T2;

        x.altura = Math.max(altura(x.izquierdo), altura(x.derecho)) + 1;
        y.altura = Math.max(altura(y.izquierdo), altura(y.derecho)) + 1;

        return y;
    }

    public void insertar(Libro l) {
        raiz = insertar(raiz, l);
    }

    private NodoAVL insertar(NodoAVL nodo, Libro l) {

        if (nodo == null) {
            return new NodoAVL(l);
        }

        if (l.getCodigoLibro() < nodo.dato.getCodigoLibro()) {
            nodo.izquierdo = insertar(nodo.izquierdo, l);
        } else if (l.getCodigoLibro() > nodo.dato.getCodigoLibro()) {
            nodo.derecho = insertar(nodo.derecho, l);
        } else {
            return nodo;
        }

        nodo.altura = 1 + Math.max(altura(nodo.izquierdo), altura(nodo.derecho));

        int balance = balance(nodo);

        if (balance > 1 && l.getCodigoLibro() < nodo.izquierdo.dato.getCodigoLibro()) {
            return rotarDerecha(nodo);
        }

        if (balance < -1 && l.getCodigoLibro() > nodo.derecho.dato.getCodigoLibro()) {
            return rotarIzquierda(nodo);
        }

        if (balance > 1 && l.getCodigoLibro() > nodo.izquierdo.dato.getCodigoLibro()) {
            nodo.izquierdo = rotarIzquierda(nodo.izquierdo);
            return rotarDerecha(nodo);
        }

        if (balance < -1 && l.getCodigoLibro() < nodo.derecho.dato.getCodigoLibro()) {
            nodo.derecho = rotarDerecha(nodo.derecho);
            return rotarIzquierda(nodo);
        }

        return nodo;
    }

    public Libro buscar(int codigo) {
        NodoAVL resultado = buscar(raiz, codigo);
        return (resultado != null) ? resultado.dato : null;
    }

    private NodoAVL buscar(NodoAVL nodo, int codigo) {

        if (nodo == null) {
            return null;
        }

        if (codigo == nodo.dato.getCodigoLibro()) {
            return nodo;
        }

        if (codigo < nodo.dato.getCodigoLibro()) {
            return buscar(nodo.izquierdo, codigo);
        }

        return buscar(nodo.derecho, codigo);
    }

    public void eliminar(int codigo) {
        raiz = eliminar(raiz, codigo);
    }

    private NodoAVL eliminar(NodoAVL nodo, int codigo) {

        if (nodo == null) {
            return null;
        }

        if (codigo < nodo.dato.getCodigoLibro()) {
            nodo.izquierdo = eliminar(nodo.izquierdo, codigo);
        } else if (codigo > nodo.dato.getCodigoLibro()) {
            nodo.derecho = eliminar(nodo.derecho, codigo);
        } else {

            if (nodo.izquierdo == null || nodo.derecho == null) {
                NodoAVL temp = (nodo.izquierdo != null) ? nodo.izquierdo : nodo.derecho;

                if (temp == null) {
                    nodo = null;
                } else {
                    nodo = temp;
                }
            } else {

                NodoAVL temp = minimo(nodo.derecho);
                nodo.dato = temp.dato;
                nodo.derecho = eliminar(nodo.derecho, temp.dato.getCodigoLibro());
            }
        }

        if (nodo == null) {
            return null;
        }

        nodo.altura = 1 + Math.max(altura(nodo.izquierdo), altura(nodo.derecho));

        int balance = balance(nodo);

        if (balance > 1 && balance(nodo.izquierdo) >= 0) {
            return rotarDerecha(nodo);
        }

        if (balance > 1 && balance(nodo.izquierdo) < 0) {
            nodo.izquierdo = rotarIzquierda(nodo.izquierdo);
            return rotarDerecha(nodo);
        }

        if (balance < -1 && balance(nodo.derecho) <= 0) {
            return rotarIzquierda(nodo);
        }

        if (balance < -1 && balance(nodo.derecho) > 0) {
            nodo.derecho = rotarDerecha(nodo.derecho);
            return rotarIzquierda(nodo);
        }

        return nodo;
    }

    private NodoAVL minimo(NodoAVL nodo) {
        NodoAVL actual = nodo;
        while (actual.izquierdo != null) {
            actual = actual.izquierdo;
        }
        return actual;
    }

    public int getAltura() {
        return altura(raiz);
    }

    public int getRotaciones() {
        return rotaciones;
    }
}
