package miumg.edu.gt.nodesheet.View;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import miumg.edu.gt.nodesheet.BD.Libro;
import miumg.edu.gt.nodesheet.BD.LibroServicio;
import miumg.edu.gt.nodesheet.Model.ArbolAVL;
import miumg.edu.gt.nodesheet.Model.ArbolB;

public class BibliotecaUI extends javax.swing.JFrame {

    LibroServicio servicio = new LibroServicio();
    ArbolAVL avl = new ArbolAVL();
    ArbolB arbolB = new ArbolB();

    public BibliotecaUI() {
        initComponents();
        cargarTabla();
        cargarEstructuras();
        eventos();
    }

    private void cargarEstructuras() {

        List<Libro> lista = servicio.obtenerLibros();

        avl = new ArbolAVL();
        arbolB = new ArbolB();

        long inicioAVL = System.nanoTime();

        for (Libro l : lista) {
            if (l.isActivo()) {
                avl.insertar(l);
            }
        }

        long finAVL = System.nanoTime();

        long inicioB = System.nanoTime();

        for (Libro l : lista) {
            if (l.isActivo()) {
                arbolB.insertar(l);
            }
        }

        long finB = System.nanoTime();

        System.out.println("\n====== RESULTADOS ======");

        System.out.println("AVL Tiempo: " + (finAVL - inicioAVL) + " ns");
        System.out.println("AVL Rotaciones: " + avl.getRotaciones());
        System.out.println("AVL Altura: " + avl.getAltura());

        System.out.println("----------------------------");

        System.out.println("Arbol B Tiempo: " + (finB - inicioB) + " ns");
        System.out.println("Arbol B Divisiones: " + arbolB.getDivisiones());
        System.out.println("Arbol B Altura: " + arbolB.getAltura());

        System.out.println("============================\n");
    }

    private void cargarTabla() {
        List<Libro> lista = servicio.obtenerLibros();

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.setRowCount(0);

        for (Libro l : lista) {
            if (l.isActivo()) {
                modelo.addRow(new Object[]{
                    l.getCodigoLibro(),
                    l.getIsbn(),
                    l.getTitulo(),
                    l.getAutor(),
                    l.getAnio(),
                    l.getCategoria()
                });
            }
        }
    }

    private void eventos() {

        btnBUSCAR.addActionListener(e -> {
            try {
                String texto = lblCODLIBRO.getText().trim();

                DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
                modelo.setRowCount(0);

                if (texto.isEmpty()) {
                    cargarTabla();

                    int ultimoCodigo = servicio.obtenerUltimoCodigo();

                    modelo.addRow(new Object[]{
                        ultimoCodigo + 1, "", "", "", "", ""
                    });

                } else {

                    int codigo;

                    try {
                        codigo = Integer.parseInt(texto);
                    } catch (NumberFormatException ex) {
                        System.out.println("Código inválido");
                        return;
                    }

                    Libro l = avl.buscar(codigo);

                    if (l != null) {
                        modelo.addRow(new Object[]{
                            l.getCodigoLibro(),
                            l.getIsbn(),
                            l.getTitulo(),
                            l.getAutor(),
                            l.getAnio(),
                            l.getCategoria()
                        });
                    } else {
                        System.out.println("Libro no encontrado");
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnINSERTAR.addActionListener(e -> {
            try {

                if (jTable1.isEditing()) {
                    jTable1.getCellEditor().stopCellEditing();
                }

                DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();

                int fila = jTable1.getSelectedRow();

                if (fila == -1) {
                    System.out.println("Seleccione la fila que desea insertar");
                    return;
                }

                String codigoStr = getValor(modelo, fila, 0);
                String isbn = getValor(modelo, fila, 1);
                String titulo = getValor(modelo, fila, 2);
                String autor = getValor(modelo, fila, 3);
                String anioStr = getValor(modelo, fila, 4);
                String categoria = getValor(modelo, fila, 5);

                if (codigoStr.isEmpty() || isbn.isEmpty() || titulo.isEmpty()
                        || autor.isEmpty() || anioStr.isEmpty() || categoria.isEmpty()) {
                    System.out.println("Todos los campos son obligatorios");
                    return;
                }

                int codigo = Integer.parseInt(codigoStr);
                int anio = Integer.parseInt(anioStr);

                if (anio < 1900 || anio > 2026) {
                    System.out.println("Año fuera de rango");
                    return;
                }

                if (servicio.buscarPorCodigo(codigo) != null) {
                    System.out.println("Ese código ya existe");
                    return;
                }

                Libro l = new Libro();
                l.setCodigoLibro(codigo);
                l.setIsbn(isbn);
                l.setTitulo(titulo);
                l.setAutor(autor);
                l.setAnio(anio);
                l.setCategoria(categoria);
                l.setActivo(true);

                avl.insertar(l);
                arbolB.insertar(l);
                servicio.insertar(l);

                System.out.println("Insertado correctamente");

                recargarTodo();

            } catch (NumberFormatException ex) {
                System.out.println("Error: código o año inválido");
            } catch (Exception ex) {
                System.out.println("Error inesperado");
                ex.printStackTrace();
            }
        });

        btnELIMINAR.addActionListener(e -> {

            String texto = lblCODLIBRO.getText().trim();

            if (texto.isEmpty()) {
                System.out.println("Ingrese un código");
                return;
            }

            try {
                int codigo = Integer.parseInt(texto);

                servicio.desactivar(codigo);
                avl.eliminar(codigo);
                arbolB.eliminar(codigo);

                recargarTodo();

            } catch (NumberFormatException ex) {
                System.out.println("Código inválido");
            }
        });

        btnMODIFICAR.addActionListener(e -> {
            try {
                String texto = lblCODLIBRO.getText().trim();

                if (texto.isEmpty()) {
                    System.out.println("Ingrese un código");
                    return;
                }

                int codigo;

                try {
                    codigo = Integer.parseInt(texto);
                } catch (NumberFormatException ex) {
                    System.out.println("Código inválido");
                    return;
                }

                Libro libro = servicio.buscarPorCodigo(codigo);

                if (libro == null || !libro.isActivo()) {
                    System.out.println("Libro no encontrado o eliminado");
                    return;
                }

                String isbn = javax.swing.JOptionPane.showInputDialog("ISBN:", libro.getIsbn());
                String titulo = javax.swing.JOptionPane.showInputDialog("Título:", libro.getTitulo());
                String autor = javax.swing.JOptionPane.showInputDialog("Autor:", libro.getAutor());
                String anioStr = javax.swing.JOptionPane.showInputDialog("Año:", libro.getAnio());
                String categoria = javax.swing.JOptionPane.showInputDialog("Categoría:", libro.getCategoria());

                if (isbn == null || titulo == null || autor == null || anioStr == null || categoria == null) {
                    System.out.println("Operación cancelada");
                    return;
                }

                isbn = isbn.trim();
                titulo = titulo.trim();
                autor = autor.trim();
                categoria = categoria.trim();
                anioStr = anioStr.trim();

                if (isbn.isEmpty() || titulo.isEmpty() || autor.isEmpty() || categoria.isEmpty()) {
                    System.out.println("Todos los campos son obligatorios");
                    return;
                }

                int anio;

                try {
                    anio = Integer.parseInt(anioStr);
                } catch (NumberFormatException ex) {
                    System.out.println("Año inválido");
                    return;
                }

                if (anio < 1900 || anio > 2026) {
                    System.out.println("Año fuera de rango");
                    return;
                }

                libro.setIsbn(isbn);
                libro.setTitulo(titulo);
                libro.setAutor(autor);
                libro.setAnio(anio);
                libro.setCategoria(categoria);

                servicio.actualizar(libro);

                recargarTodo();

            } catch (Exception ex) {
                System.out.println("Error inesperado al modificar");
                ex.printStackTrace();
            }
        });

    }

    private String getValor(DefaultTableModel modelo, int fila, int col) {
        Object val = modelo.getValueAt(fila, col);
        return (val == null) ? "" : val.toString().trim();
    }

    private void recargarTodo() {
        avl = new ArbolAVL();
        arbolB = new ArbolB();
        cargarEstructuras();
        cargarTabla();

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();

        int ultimoCodigo = servicio.obtenerUltimoCodigo();

        modelo.addRow(new Object[]{
            ultimoCodigo + 1, "", "", "", "", ""
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        lblCODLIBRO = new javax.swing.JTextField();
        btnBUSCAR = new javax.swing.JButton();
        btnELIMINAR = new javax.swing.JButton();
        btnINSERTAR = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnMODIFICAR = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel3.setText("CODIGO LIBRO");

        btnBUSCAR.setText("BUSCAR");

        btnELIMINAR.setText("ELIMINAR");

        btnINSERTAR.setText("INSERTAR");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "CODLIBRO", "ISBN", "TITULO", "AUTOR", "AÑO", "CATEGORIA"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        btnMODIFICAR.setText("MODIFICAR");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 765, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnELIMINAR, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnMODIFICAR, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(160, 160, 160)
                        .addComponent(btnINSERTAR, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCODLIBRO, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBUSCAR, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCODLIBRO, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBUSCAR, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnINSERTAR, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnELIMINAR, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMODIFICAR, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BibliotecaUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BibliotecaUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BibliotecaUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BibliotecaUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BibliotecaUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBUSCAR;
    private javax.swing.JButton btnELIMINAR;
    private javax.swing.JButton btnINSERTAR;
    private javax.swing.JButton btnMODIFICAR;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField lblCODLIBRO;
    // End of variables declaration//GEN-END:variables
}
