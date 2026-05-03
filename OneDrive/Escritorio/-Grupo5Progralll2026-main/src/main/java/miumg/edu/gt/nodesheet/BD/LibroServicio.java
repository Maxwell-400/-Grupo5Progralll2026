/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package miumg.edu.gt.nodesheet.BD;

import javax.persistence.*;
import java.util.List;

/**
 *
 * @author danie
 */
public class LibroServicio {

    private EntityManagerFactory emf;

    public LibroServicio() {
        emf = Persistence.createEntityManagerFactory("miumg.edu.gt_NodeSheet_jar_1.0-SNAPSHOTPU");
    }

    public List<Libro> obtenerLibros() {
        EntityManager em = emf.createEntityManager();

        List<Libro> lista = em.createNamedQuery("Libro.findAll", Libro.class).getResultList();

        em.close();
        return lista;
    }

    public void insertar(Libro libro) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(libro);
        em.getTransaction().commit();
        em.close();
    }

    public Libro buscarPorCodigo(int codigo) {
        EntityManager em = emf.createEntityManager();
        Libro libro = em.find(Libro.class, codigo);
        em.close();
        return libro;
    }

    public void desactivar(int codigo) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Libro libro = em.find(Libro.class, codigo);
        if (libro != null) {
            libro.setActivo(false);
            em.merge(libro);
        }

        em.getTransaction().commit();
        em.close();
    }

    public int generarNuevoCodigo() {
        EntityManager em = emf.createEntityManager();

        Integer max = (Integer) em.createQuery(
                "SELECT MAX(l.codigoLibro) FROM Libro l"
        ).getSingleResult();

        em.close();

        return (max == null) ? 1 : max + 1;
    }

    public void actualizar(Libro libro) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            em.merge(libro);

            em.getTransaction().commit();

            System.out.println("Actualizado correctamente: " + libro.getCodigoLibro());

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            System.out.println("Error al actualizar (BD): " + e.getMessage());

        } finally {
            em.close();
        }
    }

    public void modificar(Libro libro) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            em.merge(libro);

            em.getTransaction().commit();

            System.out.println("Libro modificado correctamente");

        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public int obtenerUltimoCodigo() {
        EntityManager em = emf.createEntityManager();

        try {
            Integer max = (Integer) em.createQuery(
                    "SELECT MAX(l.codigoLibro) FROM Libro l"
            ).getSingleResult();

            return (max == null) ? 0 : max;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            em.close(); 
        }
    }

}
