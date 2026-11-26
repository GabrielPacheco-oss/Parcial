
package Parcial.src.Persistencia;

import Parcial.src.Modelo.Material;
import java.util.List;

public interface MaterialDAO {

    void guardar(Material material) throws Exception;

    List<Material> listar() throws Exception;

    void actualizar(Material material) throws Exception;

    void eliminar(int idMaterial) throws Exception;

}
