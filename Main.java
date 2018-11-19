package edu.gvsu.cs351.conversion;
import com.sun.xml.internal.fastinfoset.util.StringArray;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.Key;
import java.util.ArrayList;


public class Main implements ActionListener {
    private JButton go,search,connect;
    private JLabel enter,Keyword, server, port, hostname, username, speed;
    private JTextField key,ip, portdigit, user, host, command;
    private JList<DefaultListModel> type;
    private DefaultListModel speeds;
    private JFrame frame;
    private JPanel  TopPanel, MiddlePanel, BottomPanel, contentPane;
    private JTextArea controlpanel;
    private JScrollPane scroll, tab;
    private DefaultTableModel storage;
    private JTable database;
    private int counter;
    public HostClient hostClient ;
    private Boolean test;

    public void setGUI() {

               test = false;
                hostClient = new HostClient();
                counter = 0;
                frame = new JFrame("GV-NAPSTER Host");

                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame.addWindowListener(new java.awt.event.WindowAdapter(){
                    @Override
                            public void windowClosing(java.awt.event.WindowEvent e) {

                        if(test){hostClient.closeserver();}

                        e.getWindow().dispose();
                    }
        });
                //Bottom pannel attachments
                go = new JButton("Go");
                go.addActionListener(this::actionPerformed);
                enter = new JLabel("Enter Command:");
                command = new JTextField();
                controlpanel = new JTextArea();
                controlpanel.setEditable(false);
                scroll = new JScrollPane(controlpanel);
                scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                //Middle Pannel Attachments
                search = new JButton(("Search"));
                search.addActionListener(this::actionPerformed);
                Keyword = new JLabel("Keyword:");
                key = new JTextField();
                String[] columnNames = {"Speed" , "HostName", "FileName"};
                Object [] [] temp = new Object[][]{};
                storage = new DefaultTableModel(temp,columnNames);
                database = new JTable(storage);
                Object[] datas = new Object[]{"875", "875","875"};
                storage.addRow(datas);
                tab = new JScrollPane(database);
                //Top Pannel Attachments
                connect = new JButton("Connect");
                connect.addActionListener(this::actionPerformed);
                server = new JLabel("Server Hostname:");
                port = new JLabel("Port:");
                username = new JLabel("Username:");
                hostname = new JLabel("Hostname:");
                speed = new JLabel("Speed:");
                ip = new JTextField();
                portdigit = new JTextField();
                user = new JTextField();
                host = new JTextField();
                speeds = new DefaultListModel();
                speeds.addElement("Ethernet");
                speeds.addElement("Modem");
                speeds.addElement("TC 1");
                speeds.addElement("TC 2");
                type = new JList(speeds);
                //Setting up pannels
                contentPane = new JPanel(new GridLayout(3,1));
                TopPanel = new JPanel();
                TopPanel.setLayout(new GridBagLayout());
                MiddlePanel = new JPanel();
                MiddlePanel.setLayout((new GridBagLayout()));
                BottomPanel = new JPanel();
                BottomPanel.setLayout(new GridBagLayout());
                String toptitle = "Connection";
                String middletitle = "Search";
                String bottomtitle = "FTP";
                Border topborder = BorderFactory.createTitledBorder(toptitle);
                Border middleborder = BorderFactory.createTitledBorder(middletitle);
                Border bottomborder = BorderFactory.createTitledBorder(bottomtitle);
                TopPanel.setBorder(topborder);
                MiddlePanel.setBorder(middleborder);
                BottomPanel.setBorder(bottomborder);
                GridBagConstraints c = new GridBagConstraints();
                //Bottom Pannel set up
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 0.5;
                c.gridx= 0;
                c.gridy = 0;
                BottomPanel.add(enter,c);
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 4;
                c.gridx= 1;
                c.gridy = 0;
                BottomPanel.add(command,c);
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 0.5;
                c.gridx= 2;
                c.gridy = 0;
                BottomPanel.add(go,c);
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = .5;
                c.weighty = 1;
                c.gridx= 0;
                c.gridwidth=3;
                c.gridy = 1;
                c.ipady = 160;
                BottomPanel.add(controlpanel,c);
                c.weightx = .5 ;
                //Middle Pannel set up
                c.ipady = 0;
                c.weighty = 0;
                c.gridwidth = 1;
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 0.5;
                c.gridx= 0;
                c.gridy = 0;
                MiddlePanel.add(Keyword,c);
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 1;
                c.gridx= 1;
                c.gridy = 0;
                MiddlePanel.add(key,c);
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 0.5;
                c.gridx= 2;
                c.gridy = 0;
                MiddlePanel.add(search,c);
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 0.5;
                c.gridx= 0;
                c.gridwidth = 3;
                c.ipady =160;
                c.gridy = 1;
                MiddlePanel.add(tab,c);
                c.ipady = 0;
                c.weighty = 0;
                c.gridwidth = 1;
                //Top Pannel set up
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 0.5;
                c.gridx= 0;
                c.gridy = 0;
                TopPanel.add(server,c);
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 1;
                c.gridx= 1;
                c.gridy = 0;
                TopPanel.add(ip,c);
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 0.25;
                c.gridx= 2;
                c.gridy = 0;
                TopPanel.add(port,c);
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 0.25;
                c.gridx= 3;
                c.gridy = 0;
                TopPanel.add(portdigit,c);
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 1;
                c.gridx= 4;
                c.gridy = 0;
                TopPanel.add(connect,c);
               //line 2 of top panel
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 0.5;
                c.gridx= 0;
                c.gridy = 1;
                TopPanel.add(username,c);
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = .5;
                c.gridx= 1;
                c.gridy = 1;
                TopPanel.add(user,c);
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 0.5;
                c.gridx= 2;
                c.gridy = 1;
                TopPanel.add(hostname,c);
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 0.5;
                c.gridx= 3;
                c.gridy = 1;
                TopPanel.add(host,c);
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = .25;
                c.gridx= 4;
                c.gridy = 1;
                TopPanel.add(speed,c);
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = .5;
                c.gridx= 5;
                c.gridy = 1;
                TopPanel.add(type,c);









                contentPane.add(TopPanel);
                contentPane.add(MiddlePanel);
                contentPane.add(BottomPanel);
                frame.setContentPane(contentPane);
                frame.setSize(1000,800);
                frame.setVisible(true);


            }
    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()== connect)
        {
            String ips = ip.getText();
            String ports = portdigit.getText();
            String usernames = user.getText();
            String hostnames= host.getText();
            String speed = speeds.getElementAt(type.getSelectedIndex()).toString();
            hostClient.createa(ips,ports,usernames,hostnames,speed);
            test = true;
        }
        else if(e.getSource()== go){


            controlpanel.append(">> " + command.getText() + "\n");

            String temp = hostClient.commands(command.getText());
            controlpanel.append(temp+ " \n");
            command.setText("");
            frame.invalidate();
            frame.validate();
            frame.repaint();
        }
        else if(e.getSource()== search){
            String searchwrod = key.getText();
            int x = storage.getRowCount();
            System.out.println(x);
            for( int y = 0; y < x; y++){
                storage.removeRow(y);
            }
            command.setText("");
            frame.invalidate();
            frame.validate();
            frame.repaint();

        }


    }
    public static void main(String[] args){
        Main e = new Main();

        e.setGUI();
    }





    }



