package Parcial.src;
import java.time.LocalDate;
import Parcial.src.Modelo.Material;

public class Prestamo {

    private int idPrestamo;
    private Material material;
    private String documento;
    private LocalDate fechaPrestamo;
    private LocalDate fechaLimite;
    private LocalDate fechaDevolucion;
    private Integer componentesFaltantes;
    private String politicaMulta;
    private double seguro;
    private double multaBase;
    private double multaFinal;

    public Prestamo(int idPrestamo, Material material, String documento,
            LocalDate fechaPrestamo, LocalDate fechaLimite,
            LocalDate fechaDevolucion, Integer componentesFaltantes,
            String politicaMulta, double seguro,
            double multaBase, double multaFinal) {

        this.idPrestamo = idPrestamo;
        this.material = material;
        this.documento = documento;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaLimite = fechaLimite;
        this.fechaDevolucion = fechaDevolucion;
        this.componentesFaltantes = componentesFaltantes;
        this.politicaMulta = politicaMulta;
        this.seguro = seguro;
        this.multaBase = multaBase;
        this.multaFinal = multaFinal;
    }

    public int getIdPrestamo() { return idPrestamo; }
    public Material getMaterial() { return material; }
    public String getDocumento() { return documento; }
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public LocalDate getFechaLimite() { return fechaLimite; }
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public Integer getComponentesFaltantes() { return componentesFaltantes; }
    public String getPoliticaMulta() { return politicaMulta; }
    public double getSeguro() { return seguro; }
    public double getMultaBase() { return multaBase; }
    public double getMultaFinal() { return multaFinal; }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }
}