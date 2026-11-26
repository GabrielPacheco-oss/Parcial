import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Prestamo {
    private int idPrestamo;
    private Material material;
    private String documento;
    private LocalDate fechaPrestamo, fechaLimite, fechaDevolucion;
    private Integer componentesFaltantes;
    private String politicaMulta;
    private double seguro, multaBase, multaFinal;
    private boolean multaPagada;

    public Prestamo(int id, Material material, String documento, LocalDate inicio, LocalDate limite,
                    LocalDate devolucion, Integer faltantes, String politica, double seguro,
                    double base, double total, boolean pagada) throws DevolucionInvalidaException {
        this.idPrestamo=id;
        this.material=material;
        setDocumento(documento);
        this.fechaPrestamo=inicio;
        setFechaLimite(limite);
        setFechaDevolucion(devolucion);
        setComponentesFaltantes(faltantes);
        setPoliticaMulta(politica);
        if (seguro < 0 || base < 0 || total < 0) throw new IllegalArgumentException("Los valores monetarios no pueden ser negativos.");
        this.seguro=seguro; this.multaBase=base; this.multaFinal=total; this.multaPagada=pagada;
    }
    public int getIdPrestamo(){return idPrestamo;}
    public Material getMaterial(){return material;}
    public String getDocumento(){return documento;}
    public LocalDate getFechaPrestamo(){return fechaPrestamo;}
    public LocalDate getFechaLimite(){return fechaLimite;}
    public LocalDate getFechaDevolucion(){return fechaDevolucion;}
    public Integer getComponentesFaltantes(){return componentesFaltantes;}
    public String getPoliticaMulta(){return politicaMulta;}
    public double getSeguro(){return seguro;}
    public double getMultaBase(){return multaBase;}
    public double getMultaFinal(){return multaFinal;}
    public boolean isMultaPagada(){return multaPagada;}

    public void setDocumento(String d) {
        if(d==null || d.trim().isEmpty()) throw new IllegalArgumentException("El documento es obligatorio.");
        documento=d.trim();
    }
    public void setFechaLimite(LocalDate f) throws DevolucionInvalidaException {
        if(f==null || fechaPrestamo==null || !f.isAfter(fechaPrestamo)) throw new DevolucionInvalidaException("La fecha límite debe ser posterior a la fecha de préstamo.");
        long dias=ChronoUnit.DAYS.between(fechaPrestamo,f);
        int max=material.getPlazoMaximoDias();
        if(dias>max) throw new DevolucionInvalidaException("El plazo máximo para este material es de "+max+" días.");
        fechaLimite=f;
    }
    public void setFechaDevolucion(LocalDate f) throws DevolucionInvalidaException {
        if(f!=null && (fechaPrestamo==null || f.isBefore(fechaPrestamo))) throw new DevolucionInvalidaException("La devolución no puede ser anterior al préstamo.");
        fechaDevolucion=f;
    }
    public void setComponentesFaltantes(Integer n) throws DevolucionInvalidaException {
        if(n!=null) {
            material.validarComponentesFaltantes(n);
        }
        componentesFaltantes=n;
    }
    public void setPoliticaMulta(String p) {
        if(!"NORMAL".equals(p) && !"DOCENTE".equals(p) && !"SEMANA_EXAMENES".equals(p)) throw new IllegalArgumentException("Política de multa inválida.");
        politicaMulta=p;
    }
    public void setMultaPagada(boolean v){multaPagada=v;}
    public void calcularMulta(PoliticaMulta politica) {
        if(fechaDevolucion==null) { multaBase=0; multaFinal=0; multaPagada=false; return; }
        int retraso=(int)Math.max(0,ChronoUnit.DAYS.between(fechaLimite,fechaDevolucion));
        int faltantes=componentesFaltantes==null?0:componentesFaltantes;
        multaBase=material.calcularMultaBase(retraso,faltantes);
        multaFinal=politica.calcular(multaBase);
        politicaMulta=politica.getNombre();
        if(multaFinal==0) multaPagada=true;
    }
}
