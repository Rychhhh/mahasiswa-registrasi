import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Mahasiswa {
    private JPanel Main;
    private JTextField txtName;
    private JButton saveButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JScrollPane table_mahasiswa;
    private JLabel Nama;

    private JTable table1;
    private JTextField txtEmail;
    private JTextField txtNo_telp;
    private JTextField txtNim;
    private JTextField txtid;
    private JTextField txtNims;


    public static void main(String[] args) {
        JFrame frame = new JFrame("Mahasiswa");
        frame.setContentPane(new Mahasiswa().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    Connection con;
    PreparedStatement pst;

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/coba_java_sql", "root", "");
            System.out.println("Success");
        }catch (ClassNotFoundException ex) {
                ex.printStackTrace();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    void table_load()
    {
        try
        {
            pst = con.prepareStatement("select * from mahasiswa");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    public Mahasiswa() {
        connect();
        table_load();


        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nim, nama, email, no_hp;
                    nim = txtNim.getText();
                    nama = txtName.getText();
                    email = txtEmail.getText();
                    no_hp = txtNo_telp.getText();

                    try {
                        pst = con.prepareStatement("insert into mahasiswa(nim,nama,email,no_telepon) values (?,?,?,?)");
                        pst.setString(1, nim);
                        pst.setString(2, nama);
                        pst.setString(3, email);
                        pst.setString(4, no_hp);
                        pst.execute();
                        JOptionPane.showMessageDialog(null, "Data Telah Ditambahkan");
                        table_load();
                        txtNim.setText("");
                        txtName.setText("");
                        txtEmail.setText("");
                        txtNo_telp.setText("");
                        txtName.requestFocus();
                    }catch (SQLException ex) {
                            ex.printStackTrace();
                    }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nim, nama, email, no_telp;

                nim = txtNim.getText();
                nama = txtName.getText();
                email = txtEmail.getText();
                no_telp = txtNo_telp.getText();

                try {
                    pst = con.prepareStatement("update mahasiswa set nama = ?,email = ?,no_telepon = ? where nim = ?");
                    pst.setString(1, nama);
                    pst.setString(2, email);
                    pst.setString(3, no_telp);
                    pst.setString(4, nim);

                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Data Berhasil DiUbah");
                    table_load();
                    txtNim.setText("");
                    txtName.setText("");
                    txtEmail.setText("");
                    txtNo_telp.setText("");
                    txtNim.requestFocus();
                } catch( SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nim;
                nim = txtNim.getText();

                try {
                    pst = con.prepareStatement("DELETE FROM mahasiswa WHERE nim = ?");

                    pst.setString(1, nim);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Data Berhasil DiHapus");
                    table_load();
                    txtNim.setText("");
                    txtName.setText("");
                    txtEmail.setText("");
                    txtNo_telp.setText("");
                    txtNim.requestFocus();
                }catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });


        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    String nim;
                    nim = txtid.getText();

                    pst = con.prepareStatement("select nama,email,no_telepon from mahasiswa where nim = ?");
                    pst.setString(1, nim);
                    ResultSet rs = pst.executeQuery();

                    if(rs.next() == true)
                        {
                            String nama = rs.getString(1);
                            String email = rs.getString(2);
                            String no_telepon = rs.getString(3);

                            txtNim.setText(nim);
                            txtName.setText(nama);
                            txtEmail.setText(email);
                            txtNo_telp.setText(no_telepon);

                        }
                    else
                        {
                            txtName.setText("");
                            txtEmail.setText("");
                            txtNo_telp.setText("");
                            JOptionPane.showMessageDialog(null,"Mahasiswa Tidak Ditemukan");

                        }
                }
                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }

        });
    }
}
