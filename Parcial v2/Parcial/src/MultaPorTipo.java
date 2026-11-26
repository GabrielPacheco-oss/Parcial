package Parcial.src;

public class MultaPorTipo {

    private String tipoMaterial;
    private double totalMultas;

    public MultaPorTipo(String tipoMaterial, double totalMultas) {
        this.tipoMaterial = tipoMaterial;
        this.totalMultas = totalMultas;
    }

    public String getTipoMaterial() {
        return tipoMaterial;
    }

    public double getTotalMultas() {
        return totalMultas;
    }
}