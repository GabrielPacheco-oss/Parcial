public class Kit extends Material implements Asegurable {
    private int numeroComponentes;
    public Kit(int id, String codigo, String nombre, int componentes) throws ValidacionException {
        super(id, codigo, nombre);
        setNumeroComponentes(componentes);
    }
    public int getNumeroComponentes() { return numeroComponentes; }
    public void setNumeroComponentes(int n) throws ValidacionException {
        if (n < 1 || n > 50) throw new ValidacionException("El kit debe tener entre 1 y 50 componentes.");
        numeroComponentes = n;
    }
    @Override public String getTipo() { return "KIT"; }
    @Override public int getPlazoMaximoDias() { return 7; }
    @Override public void validarComponentesFaltantes(int faltantes) throws DevolucionInvalidaException {
        if (faltantes < 0 || faltantes > numeroComponentes) throw new DevolucionInvalidaException("La cantidad de componentes faltantes no es válida.");
    }
    @Override public Integer getNumeroComponentesBD() { return numeroComponentes; }
    @Override public double calcularMultaBase(int dias, int faltantes) throws IllegalArgumentException {
        if (faltantes < 0 || faltantes > numeroComponentes) throw new IllegalArgumentException("Componentes faltantes fuera del rango del kit.");
        return Math.max(0, dias) * 3000.0 + faltantes * 5000.0;
    }
    @Override public double calcularSeguro() { return 2000.0; }
}
