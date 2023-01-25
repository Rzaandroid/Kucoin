import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.net.URI;

public class KucoinGui {
    JFrame f;
    JTable j;
    Vector cols;
    DefaultTableModel model;
    List<RowSorter.SortKey> sortKeys;
    JScrollPane sp;
    String s;
    Desktop desktop;
    String[] columnNames;
    Process process;
    TableRowSorter<TableModel> sorter;
    static ArrayList a;
    static JSONArray candles;

    public KucoinGui()
    {
        a=null;
        columnNames = new String[]{"Time", "Symbol", "First Price", "PLUS PERCENTAGE", "PRICE", "DIFFECENCE", "PERCENT"};
        cols = new Vector(List.of(columnNames));
        f = new JFrame();
        j = new JTable();

        f.setTitle("JTable Example");
        model = (DefaultTableModel) j.getModel();
        model.setColumnIdentifiers(columnNames);
        j = new JTable(model);
        j.setBounds(30, 40, 200, 300);
        sorter = new TableRowSorter<TableModel>(j.getModel());
        j.setRowSorter(sorter);

        sortKeys = new ArrayList<>(0);
        sortKeys.add(new RowSorter.SortKey(6, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);

        j.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent event) {
                        int viewRow = j.getSelectedRow();
                        if (viewRow < 0) {

                        } else {
                            s = "https://www.kucoin.com/trade/" + j.getValueAt(j.getSelectedRow(),1);
                            try {
                                process = Runtime.getRuntime().exec("firefox "+URI.create(s));
                            } catch (IOException e) {
                                System.out.println(LocalTime.now());
                                System.out.println(e);
                            }
                        }
                    }
                }
        );

        sp = new JScrollPane(j);
        f.add(sp);
        f.setSize(500, 200);
        //f.setVisible(true);
    }

    public static void main(String[] args)
    {
        System.out.println("Started at: "+LocalTime.now());
        KucoinGui kg = new KucoinGui();

        try
        {
            Kucoin ku = new Kucoin();
            //ku.login("https://api.kucoin.com/api/v1/accounts/617da39ff17a750001694f33");
            //fh = new FileHandler("~/kucoin.log");
            //logger.addHandler(fh);
            //SimpleFormatter formatter = new SimpleFormatter();
            //fh.setFormatter(formatter);

            //logger.info("My first log");
            ku.runonce(ku.geturl("https://api.kucoin.com/api/v1/market/allTickers"));
            //ku.sendtelegram();
            while (true)
            {
                a = ku.run(ku.geturl("https://api.kucoin.com/api/v1/market/allTickers"));
                //System.out.println(candles.getString(0).toString());

                /*
                for(int t=0;t<model.getRowCount();t++)
                {
                     model.removeRow(t);
                }

                for(int i=0;i<a.size();i++) {
                    model.addRow((Object[]) a.get(i));
                }

 */
/*
                for(int i=0;i<a.size();i++) {
                    for(int x=0;x<kg.j.getRowCount();x++) {
                        if (((String)((Object[]) a.get(i))[1]).equals((String)kg.j.getValueAt(x, 1))) {
                            //j.setValueAt(j.getValueAt(x, 0), x, 0);
                            //j.setValueAt(j.getValueAt(x, 1), x, 1);
                            //j.setValueAt(j.getValueAt(x, 2), x, 2);
                            //j.setValueAt(j.getValueAt(x, 3), x, 3);
                            //j.setValueAt(j.getValueAt(x, 4), x, 4);
                            //j.setValueAt(j.getValueAt(x, 5), x, 5);
                            //j.setValueAt(Double.valueOf((Double) j.getValueAt(x, 6)), x, 6);
                            kg.model.removeRow(x);
                        }
                    }
                    kg.model.addRow((Object[]) a.get(i));
                }
                */
                //kg.j.setModel(kg.model);
                //kg.model.fireTableDataChanged();
                System.gc();
            }
        } catch (Exception e) {
                System.out.println(LocalTime.now());
                System.out.println(e);
        }
    }
}
