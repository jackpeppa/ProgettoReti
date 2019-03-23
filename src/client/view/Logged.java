/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import java.awt.Color;
import java.awt.event.ActionListener;

/**
 *
 * @author Giacomo
 */
public class Logged extends javax.swing.JFrame {

    /**
     * Creates new form Logged
     */
    public Logged(ActionListener al) {
        initComponents();
        this.getContentPane().setBackground(new Color(196, 196, 196));
        this.logoutButton.addActionListener(al);
        this.createButton.addActionListener(al);
        this.shareButton.addActionListener(al);
        this.editButton.addActionListener(al);
        this.showButton.addActionListener(al);
    }
    
    public void resetUI()
    {
        createNameField.setText("");
        createNumField.setText("");
        createLabel.setText("");
        
        shareNameField.setText("");
        shareUsernameField.setText("");
        shareLabel.setText("");
        
        showNameField.setText("");
        showNumField.setText("");
        showLabel.setText("");
        
        editNameField.setText("");
        editNumField.setText("");
        editLabel.setText("");
        
    }
    
    public void setDocTextArea(String par)
    {
        this.docTextArea.setText(par);
    }
    
    public String getCreateNameField()
    {
        return createNameField.getText();
    }
    
    public String getCreateNumField()
    {
        return createNumField.getText();
    }
    
    public void setCreateLabel(String text, Color c)
    {
        createLabel.setForeground(c);
        createLabel.setText(text);
    }
    
    public String getShareNameField()
    {
        return shareNameField.getText();
    }
    
    public String getShareUsernameField()
    {
        return shareUsernameField.getText();
    }
    
    public void setShareLabel(String text, Color c)
    {
        shareLabel.setForeground(c);
        shareLabel.setText(text);
    }
    
    public String getShowNameField()
    {
        return showNameField.getText();
    }
    
    public String getShowNumField()
    {
        return showNumField.getText();
    }
    
    public void setShowLabel(String text, Color c)
    {
        showLabel.setForeground(c);
        showLabel.setText(text);
    }
    
    public String getEditNameField()
    {
        return editNameField.getText();
    }
    
    public String getEditNumField()
    {
        return editNumField.getText();
    }
    
    public void setEditLabel(String text, Color c)
    {
        editLabel.setForeground(c);
        editLabel.setText(text);
    }
    
    public void appendShowTextArea(String text)
    {
        showTextArea.append(text);
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        logoutButton = new javax.swing.JButton();
        createButton = new javax.swing.JButton();
        shareButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        docTextArea = new javax.swing.JTextArea();
        createNameField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        createNumField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        shareNameField = new javax.swing.JTextField();
        shareUsernameField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        showNameField = new javax.swing.JTextField();
        showNumField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        showButton = new javax.swing.JButton();
        editNameField = new javax.swing.JTextField();
        editNumField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        createLabel = new javax.swing.JLabel();
        shareLabel = new javax.swing.JLabel();
        showLabel = new javax.swing.JLabel();
        editLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        showTextArea = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        logoutButton.setText("LOGOUT");
        logoutButton.setActionCommand("logout");

        createButton.setText("CREATE");
        createButton.setActionCommand("create");

        shareButton.setText("SHARE");
        shareButton.setActionCommand("share");

        editButton.setText("EDIT");
        editButton.setActionCommand("edit");

        docTextArea.setEditable(false);
        docTextArea.setColumns(20);
        docTextArea.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        docTextArea.setRows(5);
        jScrollPane2.setViewportView(docTextArea);

        createNameField.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        jLabel1.setText("Doc Name");

        createNumField.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        jLabel2.setText("Num Sections");

        shareNameField.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        shareUsernameField.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        jLabel3.setText("Doc Name");

        jLabel4.setText("Username");

        showNameField.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        showNumField.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        jLabel5.setText("Doc Name");

        jLabel6.setText("Section");

        showButton.setText("SHOW");
        showButton.setActionCommand("show");

        editNameField.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        editNumField.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        jLabel7.setText("Doc Name");

        jLabel8.setText("Section");

        createLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        createLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        shareLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        shareLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        showLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        showLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        editLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        editLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        showTextArea.setEditable(false);
        showTextArea.setColumns(20);
        showTextArea.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        showTextArea.setRows(5);
        jScrollPane3.setViewportView(showTextArea);

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel9.setText("I TUOI DOCUMENTI : ");

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel10.setText("SEZIONI IN EDITING :");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(shareButton, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(shareLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(showButton, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(showLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(editLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(showNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel5))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(showNumField, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(createButton, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(createLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(createNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel1))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(createNumField, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel2)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(editNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel7))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel8)
                                            .addComponent(editNumField, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(shareNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4)
                                            .addComponent(shareUsernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(93, 93, 93))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(logoutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel9)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(createNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(createNumField, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(createButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(createLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(shareNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(shareUsernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(shareButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(shareLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(showNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(showNumField, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(showButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(showLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(editNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(editNumField, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(editLabel))
                        .addGap(43, 43, 43)
                        .addComponent(logoutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton createButton;
    private javax.swing.JLabel createLabel;
    private javax.swing.JTextField createNameField;
    private javax.swing.JTextField createNumField;
    private javax.swing.JTextArea docTextArea;
    private javax.swing.JButton editButton;
    private javax.swing.JLabel editLabel;
    private javax.swing.JTextField editNameField;
    private javax.swing.JTextField editNumField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton logoutButton;
    private javax.swing.JButton shareButton;
    private javax.swing.JLabel shareLabel;
    private javax.swing.JTextField shareNameField;
    private javax.swing.JTextField shareUsernameField;
    private javax.swing.JButton showButton;
    private javax.swing.JLabel showLabel;
    private javax.swing.JTextField showNameField;
    private javax.swing.JTextField showNumField;
    private javax.swing.JTextArea showTextArea;
    // End of variables declaration//GEN-END:variables
}
