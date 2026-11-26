
package Parcial.src.Modelo;

public class Kit extends Material implements Asegurable {

    private int numeroComponentes;

    public Kit(int idMaterial, String codigo,
               String nombre, int numeroComponentes) {
        super(idMaterial, codigo, nombre);

        if (numeroComponentes < 1 || numeroComponentes > 50) {
            throw new IllegalArgumentException(
                "El kit debe tener entre 1 y 50 componentes"
            );
        }

        this.numeroComponentes = numeroComponentes;
    }

    public int getNumeroComponentes() {
        return numeroComponentes;
    }

    @Override
    public double calcularMultaBase(
            int diasRetraso,
            int componentesFaltantes
    ) {
        if (componentesFaltantes < 0 ||
            componentesFaltantes > numeroComponentes) {
            throw new IllegalArgumentException(
                "Cantidad de componentes faltantes inválida"
            );
        }

        return Math.max(0, diasRetraso) * 3000
                + componentesFaltantes * 5000;
    }

    @Override
    public double calcularSeguro() {
        return 2000;
    }
}
