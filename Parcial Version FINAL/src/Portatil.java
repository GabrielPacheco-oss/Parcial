public class Portatil extends Material implements Asegurable {
    private double valorComercial;
    public Portatil(int id, String codigo, String nombre, double valor) throws ValidacionException {
        super(id, codigo, nombre);
        setValorComercial(valor);
    }
    public double getValorComercial() { return valorComercial; }
    public void setValorComercial(double valor) throws ValidacionException {
        if (valor <= 0) throw new ValidacionException("El valor comercial del portátil debe ser mayor que cero.");
        valorComercial = valor;
    }
    @Override public String getTipo() { return "PORTATIL"; }
    @Override public int getPlazoMaximoDias() { return 3; }
    @Override public Double getValorComercialBD() { return valorComercial; }
    @Override public double calcularMultaBase(int dias, int faltantes) { return Math.max(0, dias) * 10000.0; }
    @Override public double calcularSeguro() { return valorComercial * 0.02; }
}
