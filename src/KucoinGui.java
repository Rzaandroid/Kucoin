import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class KucoinGui {
    JFrame f;
    static JTable j;
    static Vector cols;
    static DefaultTableModel model;

    List<RowSorter.SortKey> sortKeys;
    JScrollPane sp;

    public KucoinGui()
    {
        String[] columnNames = {"Time", "Symbol", "First Price", "PLUS PERCENTAGE", "PRICE", "DIFFECENCE", "PERCENT"};
        cols = new Vector(List.of(columnNames));
        f = new JFrame();
        j = new JTable();

        f.setTitle("JTable Example");
        model = (DefaultTableModel) j.getModel();
        model.setColumnIdentifiers(columnNames);
        j = new JTable(model);
        j.setBounds(30, 40, 200, 300);
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(j.getModel());
        j.setRowSorter(sorter);

        sortKeys = new ArrayList<>(0);
        sortKeys.add(new RowSorter.SortKey(6, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);

        sp = new JScrollPane(j);
        f.add(sp);
        f.setSize(500, 200);
        f.setVisible(true);
    }

    public static void main(String[] args) {

        new KucoinGui();


        try {
            Kucoin ku = new Kucoin();
            //ku.login("https://api.kucoin.com/api/v1/accounts/617da39ff17a750001694f33");
            //fh = new FileHandler("~/kucoin.log");
            //logger.addHandler(fh);
            //SimpleFormatter formatter = new SimpleFormatter();
            //fh.setFormatter(formatter);

            //logger.info("My first log");
            ku.runonce(ku.geturl("https://api.kucoin.com/api/v1/market/allTickers"));
            //ku.sendtelegram();
            while (true) {

                ArrayList a = ku.run(ku.geturl("https://api.kucoin.com/api/v1/market/allTickers"));
                //j = new JTable(v, cols);
                //DefaultTableModel model = (DefaultTableModel) j.getModel();
                for(int i=0;i<a.size();i++) {
                    for(int x=0;x<j.getRowCount();x++) {
                        if (((Object[]) a.get(i))[1].equals(j.getValueAt(x, 1)))
                            model.removeRow(x);
                    }
                    model.addRow((Object[]) a.get(i));
                }
                model.fireTableDataChanged();
                System.gc();
            }
        } catch (Exception e) {
                System.out.println(LocalTime.now());
                System.out.println(e);
        }
    }
}
