import java.sql.*;
public class MaterialMapper {
    public static Material reconstruir(ResultSet r) throws SQLException {
        int id=r.getInt("id_material"); String codigo=r.getString("codigo");
        String nombre=r.getString("nombre"); String tipo=r.getString("tipo");
        try {
            switch(tipo) {
                case "LIBRO": return new Libro(id,codigo,nombre);
                case "PORTATIL": return new Portatil(id,codigo,nombre,r.getDouble("valor_comercial"));
                case "KIT": return new Kit(id,codigo,nombre,r.getInt("numero_componentes"));
                default: throw new SQLException("Tipo de material desconocido: "+tipo);
            }
        } catch(ValidacionException e){throw new SQLException("Datos de material inválidos en la base de datos.",e);}
    }
}
