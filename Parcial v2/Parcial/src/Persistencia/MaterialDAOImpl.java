package Parcial.src.Persistencia; 
 
import Parcial.src.Modelo.Libro; 
import Parcial.src.Modelo.Kit; 
import Parcial.src.Modelo.Material; 
import Parcial.src.Modelo.Portatil; 
import java.sql.*;
import java.util.*;

public class MaterialDAOImpl implements MaterialDAO {

    @Override
    public void guardar(Material material) throws Exception {

        String sql = "INSERT INTO materiales "
                + "(codigo, nombre, tipo, valor_comercial, numero_componentes) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, material.getCodigo());
            ps.setString(2, material.getNombre());

            if (material instanceof Libro) {
                ps.setString(3, "LIBRO");
                ps.setNull(4, java.sql.Types.DECIMAL);
                ps.setNull(5, java.sql.Types.INTEGER);

            } else if (material instanceof Portatil) {
                Portatil p = (Portatil) material;
                ps.setString(3, "PORTATIL");
                ps.setDouble(4, p.getValorComercial());
                ps.setNull(5, java.sql.Types.INTEGER);

            } else if (material instanceof Kit) {
                Kit k = (Kit) material;
                ps.setString(3, "KIT");
                ps.setNull(4, java.sql.Types.DECIMAL);
                ps.setInt(5, k.getNumeroComponentes());

            } else {
                throw new IllegalArgumentException("Tipo de material no válido.");
            }

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al guardar material: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Material> listar() throws Exception {

        List<Material> materiales = new ArrayList<>();

        String sql = "SELECT * FROM materiales";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                materiales.add(MaterialMapper.reconstruir(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar materiales: " + e.getMessage());
            throw e;
        }

        return materiales;
    }

    @Override
    public void actualizar(Material material) throws Exception {

        String sql = "UPDATE materiales SET codigo=?, nombre=?, "
                + "valor_comercial=?, numero_componentes=? "
                + "WHERE id_material=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, material.getCodigo());
            ps.setString(2, material.getNombre());

            if (material instanceof Portatil) {
                ps.setDouble(3, ((Portatil) material).getValorComercial());
                ps.setNull(4, java.sql.Types.INTEGER);

            } else if (material instanceof Kit) {
                ps.setNull(3, java.sql.Types.DECIMAL);
                ps.setInt(4, ((Kit) material).getNumeroComponentes());

            } else {
                ps.setNull(3, java.sql.Types.DECIMAL);
                ps.setNull(4, java.sql.Types.INTEGER);
            }

            ps.setInt(5, material.getIdMaterial());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al actualizar material: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void eliminar(int idMaterial) throws Exception {

        String sql = "DELETE FROM materiales WHERE id_material=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idMaterial);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al eliminar material: " + e.getMessage());
            throw e;
        }
    }
}