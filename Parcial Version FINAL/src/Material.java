public abstract class Material {
    private int idMaterial;
    private String codigo;
    private String nombre;

    public Material(int idMaterial, String codigo, String nombre) {
        this.idMaterial = idMaterial;
        setCodigo(codigo);
        setNombre(nombre);
    }
    public int getIdMaterial() { return idMaterial; }
    public void setIdMaterial(int id) { if (id < 0) throw new IllegalArgumentException("ID inválido"); this.idMaterial = id; }
    public String getCodigo() { return codigo; }
    public final void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) throw new IllegalArgumentException("El código es obligatorio");
        this.codigo = codigo.trim();
    }
    public String getNombre() { return nombre; }
    public final void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) throw new IllegalArgumentException("El nombre es obligatorio");
        this.nombre = nombre.trim();
    }
    public abstract String getTipo();
    public abstract int getPlazoMaximoDias();
    public void validarComponentesFaltantes(int faltantes) throws DevolucionInvalidaException {
        if (faltantes != 0) throw new DevolucionInvalidaException("Este material no admite componentes faltantes.");
    }
    public abstract double calcularMultaBase(int diasRetraso, int componentesFaltantes);
    public double calcularSeguro() { return 0; }
    public Double getValorComercialBD() { return null; }
    public Integer getNumeroComponentesBD() { return null; }
}
