public class Libro extends Material {
    public Libro(int id, String codigo, String nombre) { super(id, codigo, nombre); }
    @Override public String getTipo() { return "LIBRO"; }
    @Override public int getPlazoMaximoDias() { return 15; }
    @Override public double calcularMultaBase(int dias, int faltantes) { return Math.max(0, dias) * 1000.0; }
}
