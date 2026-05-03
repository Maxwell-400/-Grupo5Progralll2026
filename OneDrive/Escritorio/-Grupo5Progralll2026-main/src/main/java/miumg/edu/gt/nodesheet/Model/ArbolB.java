package miumg.edu.gt.nodesheet.Model;

import miumg.edu.gt.nodesheet.BD.Libro;

public class ArbolB {

    private NodoB raiz;
    private final int M = 5;
    public int divisiones = 0;

    public void insertar(Libro k) {

        if (raiz == null) {
            raiz = new NodoB(true);
            raiz.claves[0] = k;
            raiz.n = 1;
            return;
        }

        if (raiz.n == M - 1) {
            NodoB nueva = new NodoB(false);
            nueva.hijos[0] = raiz;

            dividir(nueva, 0, raiz);

            int i = 0;
            if (k.getCodigoLibro() > nueva.claves[0].getCodigoLibro()) {
                i++;
            }

            insertarNoLleno(nueva.hijos[i], k);
            raiz = nueva;
        } else {
            insertarNoLleno(raiz, k);
        }
    }

    private void insertarNoLleno(NodoB nodo, Libro k) {

        int i = nodo.n - 1;

        if (nodo.hoja) {
            while (i >= 0 && k.getCodigoLibro() < nodo.claves[i].getCodigoLibro()) {
                nodo.claves[i + 1] = nodo.claves[i];
                i--;
            }
            nodo.claves[i + 1] = k;
            nodo.n++;
        } else {

            while (i >= 0 && k.getCodigoLibro() < nodo.claves[i].getCodigoLibro()) {
                i--;
            }
            i++;

            if (nodo.hijos[i].n == M - 1) {
                dividir(nodo, i, nodo.hijos[i]);

                if (k.getCodigoLibro() > nodo.claves[i].getCodigoLibro()) {
                    i++;
                }
            }
            insertarNoLleno(nodo.hijos[i], k);
        }
    }

    private void dividir(NodoB padre, int i, NodoB lleno) {

        divisiones++;

        int t = 2;

        NodoB nuevo = new NodoB(lleno.hoja);
        nuevo.n = t - 1;

        for (int j = 0; j < t - 1; j++) {
            nuevo.claves[j] = lleno.claves[j + t];
        }

        if (!lleno.hoja) {
            for (int j = 0; j < t; j++) {
                nuevo.hijos[j] = lleno.hijos[j + t];
            }
        }

        lleno.n = t - 1;

        for (int j = padre.n; j >= i + 1; j--) {
            padre.hijos[j + 1] = padre.hijos[j];
        }

        padre.hijos[i + 1] = nuevo;

        for (int j = padre.n - 1; j >= i; j--) {
            padre.claves[j + 1] = padre.claves[j];
        }

        padre.claves[i] = lleno.claves[t - 1];
        padre.n++;
    }

    public Libro buscar(int codigo) {
        return buscar(raiz, codigo);
    }

    private Libro buscar(NodoB nodo, int codigo) {

        if (nodo == null) {
            return null;
        }

        int i = 0;

        while (i < nodo.n && codigo > nodo.claves[i].getCodigoLibro()) {
            i++;
        }

        if (i < nodo.n && codigo == nodo.claves[i].getCodigoLibro()) {
            return nodo.claves[i];
        }

        if (nodo.hoja) {
            return null;
        }

        return buscar(nodo.hijos[i], codigo);
    }

    public void eliminar(int codigo) {
        eliminar(raiz, codigo);
    }

    private void eliminar(NodoB nodo, int codigo) {

        if (nodo == null) {
            return;
        }

        int i = 0;

        while (i < nodo.n && codigo > nodo.claves[i].getCodigoLibro()) {
            i++;
        }

        if (i < nodo.n && codigo == nodo.claves[i].getCodigoLibro()) {

            if (nodo.hoja) {
                for (int j = i; j < nodo.n - 1; j++) {
                    nodo.claves[j] = nodo.claves[j + 1];
                }
                nodo.n--;
            } else {
                nodo.claves[i] = nodo.hijos[i].claves[nodo.hijos[i].n - 1];
                eliminar(nodo.hijos[i], nodo.claves[i].getCodigoLibro());
            }

        } else {
            if (!nodo.hoja) {
                eliminar(nodo.hijos[i], codigo);
            }
        }
    }

    public int getAltura() {
        int altura = 0;
        NodoB actual = raiz;

        while (actual != null) {
            altura++;
            if (actual.hoja) {
                break;
            }
            actual = actual.hijos[0];
        }

        return altura;
    }

    public int getDivisiones() {
        return divisiones;
    }
}
