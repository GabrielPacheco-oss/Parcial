import java.time.LocalDate;
public class PrestamoVencido {
    private final String codigo,nombre,documento;
    private final LocalDate fechaLimite;
    private final int diasRetraso;
    public PrestamoVencido(String c,String n,String d,LocalDate f,int dias){codigo=c;nombre=n;documento=d;fechaLimite=f;diasRetraso=dias;}
    public String getCodigoMaterial(){return codigo;}
    public String getNombreMaterial(){return nombre;}
    public String getDocumento(){return documento;}
    public LocalDate getFechaLimite(){return fechaLimite;}
    public int getDiasRetraso(){return diasRetraso;}
}
