package miumg.edu.gt.nodesheet.Model;

import javax.swing.JTable;

public class HojaCalculo {

    private Celda inicio;

    public void insertar(int fila, int columna, String texto) {

        if (texto == null || texto.isEmpty()) {
            eliminar(fila, columna);
            return;
        }

        Celda existente = obtener(fila, columna);

        if (existente != null) {
            existente.formula = texto;
            existente.valor = texto;
            return;
        }

        Celda nueva = new Celda(fila, columna, texto);

        if (inicio == null) {
            inicio = nueva;
            return;
        }

        if (fila < inicio.fila || (fila == inicio.fila && columna < inicio.columna)) {
            nueva.derecha = inicio;
            inicio = nueva;
            enlazarVertical(nueva);
            return;
        }

        Celda actual = inicio;
        Celda anterior = null;

        while (actual != null && (actual.fila < fila || (actual.fila == fila && actual.columna < columna))) {
            anterior = actual;
            actual = actual.derecha;
        }

        nueva.derecha = actual;

        if (anterior != null) {
            anterior.derecha = nueva;
        }

        enlazarVertical(nueva);
    }

    private void enlazarVertical(Celda nueva) {
        Celda actual = inicio;

        while (actual != null) {
            if (actual.columna == nueva.columna && actual != nueva) {

                if (actual.fila < nueva.fila) {

                    Celda temp = actual;

                    while (temp.abajo != null && temp.abajo.fila < nueva.fila) {
                        temp = temp.abajo;
                    }

                    nueva.abajo = temp.abajo;
                    temp.abajo = nueva;
                    return;

                } else {

                    nueva.abajo = actual;
                    reemplazarArriba(actual, nueva);
                    return;
                }
            }

            actual = actual.derecha;
        }
    }

    private void reemplazarArriba(Celda actual, Celda nueva) {
        Celda temp = inicio;

        while (temp != null) {
            if (temp.abajo == actual) {
                temp.abajo = nueva;
                nueva.abajo = actual;
                return;
            }
            temp = temp.derecha;
        }
    }

    public void eliminar(int fila, int columna) {

        Celda actual = inicio;
        Celda anterior = null;

        while (actual != null) {

            if (actual.fila == fila && actual.columna == columna) {

                if (anterior == null) {
                    inicio = actual.derecha;
                } else {
                    anterior.derecha = actual.derecha;
                }

                eliminarVertical(actual);
                return;
            }

            anterior = actual;
            actual = actual.derecha;
        }
    }

    private void eliminarVertical(Celda objetivo) {

        Celda actual = inicio;

        while (actual != null) {

            Celda temp = actual;

            while (temp != null) {

                if (temp.abajo == objetivo) {
                    temp.abajo = objetivo.abajo;
                    return;
                }

                temp = temp.abajo;
            }

            actual = actual.derecha;
        }
    }

    public Celda obtener(int fila, int columna) {

        Celda actual = inicio;

        while (actual != null) {

            if (actual.fila == fila && actual.columna == columna) {
                return actual;
            }

            actual = actual.derecha;
        }

        return null;
    }

    public int calcular(String formula) {

        formula = formula.substring(1);

        if (formula.startsWith("SUMAR(") && formula.endsWith(")")) {
            String rango = formula.substring(6, formula.length() - 1);
            return sumarRango(rango);
        }

        if (formula.startsWith("MULT(") && formula.endsWith(")")) {
            String rango = formula.substring(5, formula.length() - 1);
            return multiplicarRango(rango);
        }

        if (formula.contains("+")) {
            String[] partes = formula.split("\\+");
            int suma = 0;
            for (String p : partes) {
                suma += obtenerValorCelda(p.trim());
            }
            return suma;
        }

        if (formula.contains("-")) {
            String[] partes = formula.split("\\-");
            int resultado = obtenerValorCelda(partes[0].trim());
            for (int i = 1; i < partes.length; i++) {
                resultado -= obtenerValorCelda(partes[i].trim());
            }
            return resultado;
        }

        if (formula.contains("*")) {
            String[] partes = formula.split("\\*");
            int resultado = 1;
            for (String p : partes) {
                resultado *= obtenerValorCelda(p.trim());
            }
            return resultado;
        }

        if (formula.contains("/")) {
            String[] partes = formula.split("\\/");
            int resultado = obtenerValorCelda(partes[0].trim());
            for (int i = 1; i < partes.length; i++) {
                int val = obtenerValorCelda(partes[i].trim());
                if (val != 0) {
                    resultado /= val;
                }
            }
            return resultado;
        }

        return 0;
    }

    private int sumarRango(String rango) {

        String[] partes = rango.split(":");

        int[] inicio = convertirRef(partes[0]);
        int[] fin = convertirRef(partes[1]);

        int suma = 0;

        for (int f = inicio[0]; f <= fin[0]; f++) {
            for (int c = inicio[1]; c <= fin[1]; c++) {

                Celda celda = obtener(f, c);

                if (celda != null) {
                    try {
                        if (celda.formula.startsWith("=")) {
                            suma += calcular(celda.formula);
                        } else {
                            suma += Integer.parseInt(celda.formula);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }

        return suma;
    }

    private int multiplicarRango(String rango) {

        String[] partes = rango.split(":");

        int[] inicio = convertirRef(partes[0]);
        int[] fin = convertirRef(partes[1]);

        int resultado = 1;

        for (int f = inicio[0]; f <= fin[0]; f++) {
            for (int c = inicio[1]; c <= fin[1]; c++) {

                Celda celda = obtener(f, c);

                if (celda != null) {
                    try {
                        if (celda.formula.startsWith("=")) {
                            resultado *= calcular(celda.formula);
                        } else {
                            resultado *= Integer.parseInt(celda.formula);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }

        return resultado;
    }

    private int[] convertirRef(String ref) {
        int col = ref.charAt(0) - 'A' + 1;
        int fila = Integer.parseInt(ref.substring(1)) - 1;
        return new int[]{fila, col};
    }

    private int obtenerValorCelda(String ref) {

        ref = ref.trim();

        try {
            return Integer.parseInt(ref);
        } catch (NumberFormatException e) {

        }

        int[] pos = convertirRef(ref);

        Celda c = obtener(pos[0], pos[1]);

        if (c == null) {
            return 0;
        }

        try {
            if (c.formula.startsWith("=")) {
                return calcular(c.formula);
            }
            return Integer.parseInt(c.formula);
        } catch (Exception e) {
            return 0;
        }
    }

    public void recalcularTodo(JTable tabla) {

        Celda actual = inicio;

        while (actual != null) {

            try {

                if (actual.formula.startsWith("=")) {
                    int resultado = calcular(actual.formula);
                    actual.valor = String.valueOf(resultado);
                    tabla.setValueAt(resultado, actual.fila, actual.columna);
                } else {
                    actual.valor = actual.formula;
                    tabla.setValueAt(actual.valor, actual.fila, actual.columna);
                }

            } catch (Exception e) {
                tabla.setValueAt("ERROR", actual.fila, actual.columna);
            }

            actual = actual.derecha;
        }
    }
}
