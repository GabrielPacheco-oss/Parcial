
package Parcial.src.Modelo;

public abstract class Material {

    private int idMaterial;
    private String codigo;
    private String nombre;

    public Material(int idMaterial, String codigo, String nombre) {
        this.idMaterial = idMaterial;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public int getIdMaterial() {
        return idMaterial;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public abstract double calcularMultaBase(
            int diasRetraso,
            int componentesFaltantes
    );
}
