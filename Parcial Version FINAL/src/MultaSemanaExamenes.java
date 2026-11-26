public class MultaSemanaExamenes implements PoliticaMulta {
    public double calcular(double base){return Math.min(base*2.0,100000.0);}
    public String getNombre(){return "SEMANA_EXAMENES";}
}
