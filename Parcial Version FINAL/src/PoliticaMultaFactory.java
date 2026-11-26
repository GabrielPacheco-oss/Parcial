public class PoliticaMultaFactory {
    public static PoliticaMulta obtener(String nombre) {
        if ("DOCENTE".equals(nombre)) return new MultaDocente();
        if ("SEMANA_EXAMENES".equals(nombre)) return new MultaSemanaExamenes();
        return new MultaNormal();
    }
}
