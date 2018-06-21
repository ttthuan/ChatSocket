/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ChangePassword extends javax.swing.JFrame {

    private Client client = null;
    private int xx = 0;
    private int xy = 0;

    public ChangePassword(Client client) {
        initComponents();
        this.client = client;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtPasswordOld = new javax.swing.JPasswordField();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        txtPasswordNew = new javax.swing.JPasswordField();
        jSeparator9 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        txtRenewPassowrd = new javax.swing.JPasswordField();
        jSeparator10 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        btnOke = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnCancel = new javax.swing.JLabel();
        txtThongBao = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(36, 47, 65));
        jPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel1MouseDragged(evt);
            }
        });
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel1MousePressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 204, 204));
        jLabel6.setText("PASSWORD OLD");

        txtPasswordOld.setBackground(new java.awt.Color(36, 47, 65));
        txtPasswordOld.setForeground(new java.awt.Color(255, 255, 255));
        txtPasswordOld.setText("password");
        txtPasswordOld.setBorder(null);
        txtPasswordOld.setCaretColor(new java.awt.Color(204, 204, 204));
        txtPasswordOld.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPasswordOldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPasswordOldFocusLost(evt);
            }
        });
        txtPasswordOld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPasswordOldActionPerformed(evt);
            }
        });

        jSeparator8.setForeground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(204, 204, 204));
        jLabel7.setText("PASSWORD NEW");
        jLabel7.setToolTipText("");

        txtPasswordNew.setBackground(new java.awt.Color(36, 47, 65));
        txtPasswordNew.setForeground(new java.awt.Color(255, 255, 255));
        txtPasswordNew.setText("password");
        txtPasswordNew.setBorder(null);
        txtPasswordNew.setCaretColor(new java.awt.Color(204, 204, 204));
        txtPasswordNew.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPasswordNewFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPasswordNewFocusLost(evt);
            }
        });
        txtPasswordNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPasswordNewActionPerformed(evt);
            }
        });

        jSeparator9.setForeground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(204, 204, 204));
        jLabel8.setText("RE-ENTER NEW PASSWORD");

        txtRenewPassowrd.setBackground(new java.awt.Color(36, 47, 65));
        txtRenewPassowrd.setForeground(new java.awt.Color(255, 255, 255));
        txtRenewPassowrd.setText("password");
        txtRenewPassowrd.setBorder(null);
        txtRenewPassowrd.setCaretColor(new java.awt.Color(204, 204, 204));
        txtRenewPassowrd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtRenewPassowrdFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRenewPassowrdFocusLost(evt);
            }
        });
        txtRenewPassowrd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRenewPassowrdActionPerformed(evt);
            }
        });

        jSeparator10.setForeground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(120, 144, 156));
        jPanel3.setPreferredSize(new java.awt.Dimension(48, 48));

        btnOke.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnOke.setForeground(new java.awt.Color(255, 255, 255));
        btnOke.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnOke.setText("Oke");
        btnOke.setFocusable(false);
        btnOke.setPreferredSize(new java.awt.Dimension(48, 48));
        btnOke.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOkeMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnOke, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnOke, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4.setBackground(new java.awt.Color(120, 144, 156));
        jPanel4.setPreferredSize(new java.awt.Dimension(48, 48));

        btnCancel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnCancel.setText("Cancel");
        btnCancel.setFocusable(false);
        btnCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnCancel, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnCancel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        txtThongBao.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtThongBao.setForeground(new java.awt.Color(255, 255, 255));
        txtThongBao.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtThongBao, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(txtRenewPassowrd, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(txtPasswordNew, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(txtPasswordOld, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(103, 103, 103)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(49, 49, 49))))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtThongBao)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(11, 11, 11)
                .addComponent(txtPasswordOld, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addGap(11, 11, 11)
                .addComponent(txtPasswordNew, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addGap(11, 11, 11)
                .addComponent(txtRenewPassowrd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtPasswordOldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordOldFocusGained
        if (txtPasswordOld.getText().equals("password")) {
            txtPasswordOld.setText("");
        }
    }//GEN-LAST:event_txtPasswordOldFocusGained

    private void txtPasswordOldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordOldFocusLost
        if (txtPasswordOld.getText().isEmpty()) {
            txtPasswordOld.setText("password");
        }
    }//GEN-LAST:event_txtPasswordOldFocusLost

    private void txtPasswordOldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordOldActionPerformed
//        String username = txtSignInUsername.getText();
//        String password = txtSignInPassword.getText();
//
//        signIn(username, password);
    }//GEN-LAST:event_txtPasswordOldActionPerformed

    private void txtPasswordNewFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordNewFocusGained
        if (txtPasswordNew.getText().equals("password")) {
            txtPasswordNew.setText("");
        }
    }//GEN-LAST:event_txtPasswordNewFocusGained

    private void txtPasswordNewFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordNewFocusLost
        if (txtPasswordNew.getText().isEmpty()) {
            txtPasswordNew.setText("password");
        }
    }//GEN-LAST:event_txtPasswordNewFocusLost

    private void txtPasswordNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordNewActionPerformed
//        String username = txtSignInUsername.getText();
//        String password = txtSignInPassword.getText();
//
//        signIn(username, password);
    }//GEN-LAST:event_txtPasswordNewActionPerformed

    private void txtRenewPassowrdFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRenewPassowrdFocusGained
        if (txtRenewPassowrd.getText().equals("password")) {
            txtRenewPassowrd.setText("");
        }
    }//GEN-LAST:event_txtRenewPassowrdFocusGained

    private void txtRenewPassowrdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRenewPassowrdFocusLost
        if (txtRenewPassowrd.getText().isEmpty()) {
            txtRenewPassowrd.setText("password");
        }
    }//GEN-LAST:event_txtRenewPassowrdFocusLost

    private void txtRenewPassowrdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRenewPassowrdActionPerformed
//        String username = txtSignInUsername.getText();
//        String password = txtSignInPassword.getText();
//
//        signIn(username, password);
    }//GEN-LAST:event_txtRenewPassowrdActionPerformed

    private void btnOkeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOkeMouseClicked
        String passwordold = txtPasswordOld.getText();
        String passwordnew = txtPasswordNew.getText();
        String passrenew = txtRenewPassowrd.getText();

        if (passwordold.isEmpty()) {
            JOptionPane.showMessageDialog(this, "PasswordOld không được trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else if (passwordnew.isEmpty()) {
            // thông báo không được rỗng
            JOptionPane.showMessageDialog(this, "PasswordNew không được trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else if (passrenew.isEmpty()) {
            // thông báo không được rỗng
            JOptionPane.showMessageDialog(this, "PasswordReNew không được trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else if (passwordnew.equals(passrenew) == false) {
            // thông báo pass word renew gõ không đúng
            JOptionPane.showMessageDialog(this, "PasswordReNew nhập lại không đúng", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            client.changePassword(passwordold, passrenew);
        }
    }//GEN-LAST:event_btnOkeMouseClicked

    public static void showStatus(String content) {
        String[] contents = content.split(";");
        //[0]Successfull;[1]password[0] Fail
        if (contents[0].equals("Successfull")) {
            Client.account.setPassword(contents[0]);
            txtThongBao.setText("Đổi mật khẩu thành công");
        } else {
            txtThongBao.setText("Thay đổi mật khẩu không hợp lệ");
            //cảnh báo tới client không hợp lệ JOptionPane
            //JOptionPane.showMessageDialog(this, "PasswordOld nhập không đúng", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        this.dispose();
    }//GEN-LAST:event_btnCancelMouseClicked

    private void jPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xx, y - xy);
    }//GEN-LAST:event_jPanel1MouseDragged

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MousePressed
        xx = evt.getX();
        xy = evt.getY();
    }//GEN-LAST:event_jPanel1MousePressed

    public void showChangePass() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnCancel;
    private javax.swing.JLabel btnOke;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JPasswordField txtPasswordNew;
    private javax.swing.JPasswordField txtPasswordOld;
    private javax.swing.JPasswordField txtRenewPassowrd;
    private static javax.swing.JLabel txtThongBao;
    // End of variables declaration//GEN-END:variables
}
