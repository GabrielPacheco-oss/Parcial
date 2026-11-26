import java.util.List;
public interface MaterialDAO {
    void guardar(Material material) throws Exception;
    List<Material> listar() throws Exception;
    Material buscarPorCodigo(String codigo) throws Exception;
    void actualizar(Material material) throws Exception;
    void eliminar(int idMaterial) throws Exception;
}
