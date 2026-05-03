package miumg.edu.gt.nodesheet.BD;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author danie
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Libro.findAll", query = "SELECT l FROM Libro l"),
    @NamedQuery(name = "Libro.findByCodigoLibro", query = "SELECT l FROM Libro l WHERE l.codigoLibro = :codigoLibro"),
    @NamedQuery(name = "Libro.findByIsbn", query = "SELECT l FROM Libro l WHERE l.isbn = :isbn"),
    @NamedQuery(name = "Libro.findByTitulo", query = "SELECT l FROM Libro l WHERE l.titulo = :titulo"),
    @NamedQuery(name = "Libro.findByAutor", query = "SELECT l FROM Libro l WHERE l.autor = :autor"),
    @NamedQuery(name = "Libro.findByAnio", query = "SELECT l FROM Libro l WHERE l.anio = :anio"),
    @NamedQuery(name = "Libro.findByCategoria", query = "SELECT l FROM Libro l WHERE l.categoria = :categoria")})
public class Libro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    private Integer codigoLibro;
    @Basic(optional = false)
    private String isbn;
    @Basic(optional = false)
    private String titulo;
    @Basic(optional = false)
    private String autor;
    @Basic(optional = false)
    private int anio;
    @Basic(optional = false)
    private String categoria;
    @Basic(optional = false)
    private boolean activo;

    public Libro() {
    }

    public Libro(Integer codigoLibro) {
        this.codigoLibro = codigoLibro;
    }

    public Libro(Integer codigoLibro, String isbn, String titulo, String autor, int anio, String categoria) {
        this.codigoLibro = codigoLibro;
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
        this.categoria = categoria;
    }

    public Integer getCodigoLibro() {
        return codigoLibro;
    }

    public void setCodigoLibro(Integer codigoLibro) {
        this.codigoLibro = codigoLibro;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoLibro != null ? codigoLibro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
       
        if (!(object instanceof Libro)) {
            return false;
        }
        Libro other = (Libro) object;
        if ((this.codigoLibro == null && other.codigoLibro != null) || (this.codigoLibro != null && !this.codigoLibro.equals(other.codigoLibro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "miumg.edu.gt.nodesheet.BD.Libro[ codigoLibro=" + codigoLibro + " ]";
    }

}
