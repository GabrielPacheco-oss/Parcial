import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class InterfazPrincipal extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private List<Prestamo> cargados;
    private final PrestamoDAO dao=new PrestamoDAOImpl();

    public InterfazPrincipal(){
        setTitle("LabLibre - Gestión de préstamos");setSize(1250,600);setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);setLayout(new BorderLayout(8,8));
        JLabel titulo=new JLabel("LABLIBRE - GESTIÓN DE PRÉSTAMOS",SwingConstants.CENTER);
        titulo.setFont(new Font("Arial",Font.BOLD,20));add(titulo,BorderLayout.NORTH);
        String[] cols={"ID","Código","Material","Tipo","Documento","Préstamo","Límite","Devolución","Seguro","Multa base","Multa final","Política","Pagada"};
        modelo=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        tabla=new JTable(modelo);tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);add(new JScrollPane(tabla),BorderLayout.CENTER);
        JButton actualizar=new JButton("Actualizar"),registrar=new JButton("Registrar préstamo"),
            editar=new JButton("Editar / devolver"),eliminar=new JButton("Eliminar"),
            vencidos=new JButton("Préstamos vencidos"),multas=new JButton("Multas cobradas");
        JPanel botones=new JPanel(new FlowLayout());
        botones.add(actualizar);botones.add(registrar);botones.add(editar);botones.add(eliminar);botones.add(vencidos);botones.add(multas);
        add(botones,BorderLayout.SOUTH);
        actualizar.addActionListener(e->cargar());
        registrar.addActionListener(e->{new FormularioPrestamo(this).setVisible(true);cargar();});
        editar.addActionListener(e->editar());
        eliminar.addActionListener(e->eliminar());
        vencidos.addActionListener(e->mostrarVencidos());
        multas.addActionListener(e->mostrarMultas());
        cargar();
    }
    private void cargar(){
        modelo.setRowCount(0);
        try{
            cargados=dao.listar();
            for(Prestamo p:cargados){
                Material m=p.getMaterial();
                modelo.addRow(new Object[]{p.getIdPrestamo(),m.getCodigo(),m.getNombre(),m.getTipo(),
                    p.getDocumento(),p.getFechaPrestamo(),p.getFechaLimite(),
                    p.getFechaDevolucion()==null?"Pendiente":p.getFechaDevolucion(),
                    p.getSeguro(),p.getMultaBase(),p.getMultaFinal(),p.getPoliticaMulta(),
                    p.isMultaPagada()?"Sí":"No"});
            }
        }catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Error de base de datos",JOptionPane.ERROR_MESSAGE);}
    }
    private Prestamo seleccionado(){
        int fila=tabla.getSelectedRow();
        if(fila<0){JOptionPane.showMessageDialog(this,"Selecciona un préstamo.");return null;}
        int i=tabla.convertRowIndexToModel(fila);
        return i>=0&&i<cargados.size()?cargados.get(i):null;
    }
    private void editar(){
        Prestamo p=seleccionado();if(p==null)return;
        new FormularioEditarPrestamo(this,p).setVisible(true);cargar();
    }
    private void eliminar(){
        Prestamo p=seleccionado();if(p==null)return;
        int r=JOptionPane.showConfirmDialog(this,"¿Eliminar el préstamo "+p.getIdPrestamo()+"?","Confirmar",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if(r==JOptionPane.YES_OPTION){try{dao.eliminar(p.getIdPrestamo());cargar();}catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}}
    }
    private void mostrarVencidos(){
        String fecha=JOptionPane.showInputDialog(this,"Fecha de consulta (AAAA-MM-DD):",LocalDate.now().toString());
        if(fecha==null)return;
        try{
            List<PrestamoVencido> lista=dao.consultarVencidos(LocalDate.parse(fecha));
            String[] c={"Código","Material","Documento","Fecha límite","Días retraso"};
            DefaultTableModel m=new DefaultTableModel(c,0);
            for(PrestamoVencido v:lista)m.addRow(new Object[]{v.getCodigoMaterial(),v.getNombreMaterial(),v.getDocumento(),v.getFechaLimite(),v.getDiasRetraso()});
            JTable t=new JTable(m);JOptionPane.showMessageDialog(this,new JScrollPane(t),"Préstamos vencidos",JOptionPane.INFORMATION_MESSAGE);
        }catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}
    }
    private void mostrarMultas(){
        try{
            List<MultaPorTipo> lista=dao.consultarMultasPorTipo();
            StringBuilder s=new StringBuilder("TOTAL DE MULTAS COBRADAS POR TIPO\n\n");
            for(MultaPorTipo m:lista)s.append(m.getTipoMaterial()).append(": $").append(String.format("%.0f",m.getTotalMultas())).append("\n");
            if(lista.isEmpty())s.append("No hay multas registradas como pagadas.");
            JOptionPane.showMessageDialog(this,s.toString(),"Multas cobradas",JOptionPane.INFORMATION_MESSAGE);
        }catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);}
    }
}
