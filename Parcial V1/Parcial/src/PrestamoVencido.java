
package Parcial.src;

public class PrestamoVencido {

    private String codigoMaterial;
    private String nombreMaterial;
    private String documento;
    private java.time.LocalDate fechaLimite;
    private int diasRetraso;

    public PrestamoVencido(String codigoMaterial, String nombreMaterial,
            String documento, java.time.LocalDate fechaLimite,
            int diasRetraso) {

        this.codigoMaterial = codigoMaterial;
        this.nombreMaterial = nombreMaterial;
        this.documento = documento;
        this.fechaLimite = fechaLimite;
        this.diasRetraso = diasRetraso;
    }

    public String getCodigoMaterial() { return codigoMaterial; }
    public String getNombreMaterial() { return nombreMaterial; }
    public String getDocumento() { return documento; }
    public java.time.LocalDate getFechaLimite() { return fechaLimite; }
    public int getDiasRetraso() { return diasRetraso; }
}