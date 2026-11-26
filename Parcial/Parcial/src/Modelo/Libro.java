
package Parcial.src.Modelo;

public class Libro extends Material {

    public Libro(int idMaterial, String codigo, String nombre) {
        super(idMaterial, codigo, nombre);
    }

    @Override
    public double calcularMultaBase(
            int diasRetraso,
            int componentesFaltantes
    ) {
        return Math.max(0, diasRetraso) * 1000;
    }
}
