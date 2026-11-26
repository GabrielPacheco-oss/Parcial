
package Parcial.src.Modelo;

public class Portatil extends Material implements Asegurable {

    private double valorComercial;

    public Portatil(int idMaterial, String codigo,
                    String nombre, double valorComercial) {
        super(idMaterial, codigo, nombre);

        if (valorComercial <= 0) {
            throw new IllegalArgumentException(
                "El valor comercial debe ser mayor que cero"
            );
        }

        this.valorComercial = valorComercial;
    }

    public double getValorComercial() {
        return valorComercial;
    }

    @Override
    public double calcularMultaBase(
            int diasRetraso,
            int componentesFaltantes
    ) {
        return Math.max(0, diasRetraso) * 10000;
    }

    @Override
    public double calcularSeguro() {
        return valorComercial * 0.02;
    }
}
