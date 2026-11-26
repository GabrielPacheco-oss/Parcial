
package Parcial.src.Persistencia;

import Parcial.src.Modelo.Material;
import Parcial.src.Modelo.Libro;
import Parcial.src.Modelo.Portatil;
import Parcial.src.Modelo.Kit;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MaterialMapper {

    public static Material reconstruir(ResultSet rs)
            throws SQLException {

        int id = rs.getInt("id_material");
        String codigo = rs.getString("codigo");
        String nombre = rs.getString("nombre");
        String tipo = rs.getString("tipo");

        switch (tipo) {

            case "LIBRO":
                return new Libro(
                    id,
                    codigo,
                    nombre
                );

            case "PORTATIL":
                return new Portatil(
                    id,
                    codigo,
                    nombre,
                    rs.getDouble("valor_comercial")
                );

            case "KIT":
                return new Kit(
                    id,
                    codigo,
                    nombre,
                    rs.getInt("numero_componentes")
                );

            default:
                throw new SQLException(
                    "Tipo de material desconocido: " + tipo
                );
        }
    }
}
