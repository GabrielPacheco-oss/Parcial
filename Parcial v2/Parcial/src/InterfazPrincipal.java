package Parcial.src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import Parcial.src.Prestamo;
import Parcial.src.Modelo.*;
import Parcial.src.Persistencia.PrestamoDAO;
import Parcial.src.Persistencia.PrestamoDAOImpl;

public class InterfazPrincipal extends JFrame {

    private JTable tabla;
    private List<Prestamo> prestamosCargados = new java.util.ArrayList<>();
    private DefaultTableModel modeloTabla;
    private PrestamoDAO prestamoDAO;

    public InterfazPrincipal() {

        prestamoDAO = new PrestamoDAOImpl();

        setTitle("LabLibre - Gestión de préstamos");
        setSize(1100, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel(
                "REGISTRO DE PRÉSTAMOS - LABLIBRE",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        String[] columnas = {
            "ID", "Código", "Material", "Tipo",
            "Documento", "Fecha préstamo", "Fecha límite",
            "Fecha devolución", "Seguro", "Multa base", "Multa final"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        JButton btnActualizar = new JButton("Actualizar tabla");
        JButton btnRegistrar = new JButton("Registrar préstamo");

        btnRegistrar.addActionListener(e -> {
            new FormularioPrestamo(this).setVisible(true);
            cargarPrestamos();
        });

        btnActualizar.addActionListener(e -> cargarPrestamos());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnActualizar);
        JButton btnEditar = new JButton("Editar préstamo");

        btnEditar.addActionListener(e -> editarPrestamo());
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnEditar);

        add(panelBotones, BorderLayout.SOUTH);

        cargarPrestamos();
    }

    private void cargarPrestamos() {

        modeloTabla.setRowCount(0);

        try {

            prestamosCargados = prestamoDAO.listar();

            for (Prestamo p : prestamosCargados) {

                Material material = p.getMaterial();

                String tipo = material.getClass()
                        .getSimpleName().toUpperCase();

                Object[] fila = {
                    p.getIdPrestamo(),
                    material.getCodigo(),
                    material.getNombre(),
                    tipo,
                    p.getDocumento(),
                    p.getFechaPrestamo(),
                    p.getFechaLimite(),
                    p.getFechaDevolucion() == null
                            ? "Pendiente"
                            : p.getFechaDevolucion(),
                    p.getSeguro(),
                    p.getMultaBase(),
                    p.getMultaFinal()
                };

                modeloTabla.addRow(fila);
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible cargar los préstamos:\n" + e.getMessage(),
                    "Error de consulta",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    private void editarPrestamo() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un préstamo de la tabla."
            );
            return;
        }

        int indice = tabla.convertRowIndexToModel(fila);

        if (indice < 0 || indice >= prestamosCargados.size()) {
            return;
        }

        Prestamo seleccionado = prestamosCargados.get(indice);

        new FormularioEditarPrestamo(this, seleccionado).setVisible(true);

        cargarPrestamos();
    }
}
