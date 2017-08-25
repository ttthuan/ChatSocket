/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import static mmt.AccountItem.applyQualityRenderingHints;

/**
 *
 * @author Totoro
 */
public class ChatItem extends javax.swing.JPanel implements ListCellRenderer<PackageChat> {

    private Account account = null;

    public ChatItem(Account account) {
        initComponents();
        this.account = account;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        avatar = new javax.swing.JLabel();
        txtMessage = new javax.swing.JLabel();
        txtName = new javax.swing.JLabel();
        txtTime1 = new javax.swing.JLabel();
        txtTime2 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(550, 50));
        setMinimumSize(new java.awt.Dimension(550, 50));
        setPreferredSize(new java.awt.Dimension(550, 50));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(550, 50));
        jPanel1.setMinimumSize(new java.awt.Dimension(550, 50));
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(550, 50));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(avatar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 32, 32));

        txtMessage.setFont(new java.awt.Font("Noto Sans", 0, 12)); // NOI18N
        txtMessage.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txtMessage.setText("SMS ");
        jPanel1.add(txtMessage, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 15, 480, 17));

        txtName.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtName.setText("6:27 PM");
        txtName.setMaximumSize(new java.awt.Dimension(36, 15));
        txtName.setMinimumSize(new java.awt.Dimension(36, 15));
        txtName.setPreferredSize(new java.awt.Dimension(36, 15));
        jPanel1.add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 3, 200, 13));

        txtTime1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtTime1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtTime1.setText("6:27");
        jPanel1.add(txtTime1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 35, 40, 10));

        txtTime2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtTime2.setText("6:27 PM");
        jPanel1.add(txtTime2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 35, -1, 10));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel avatar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel txtMessage;
    private javax.swing.JLabel txtName;
    private javax.swing.JLabel txtTime1;
    private javax.swing.JLabel txtTime2;
    // End of variables declaration//GEN-END:variables

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // debug
        System.out.println("paint");
        
        Dimension arcs = new Dimension(40, 40);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Draws the rounded opaque panel with borders.
        graphics.setColor(new Color(192,202,233));
        graphics.fillRoundRect(0, 0, width - 1, height - 3, arcs.width, arcs.height);//paint background
        graphics.setColor(new Color(192,202,233));
        graphics.drawRoundRect(0, 0, width - 1, height - 3, arcs.width, arcs.height);//paint border
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends PackageChat> list, PackageChat value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            synchronized (this) {
                
                // debug
                System.out.println("render");
                
                // kiểm tra người nào đẩy lên
                if (value.getAccount().equals(account)) {
                    // chủ nhân đẩy lên
                    // lệch phải
                    txtMessage.setText(value.getContent());
                    txtMessage.setHorizontalAlignment(JLabel.RIGHT);
                    txtTime1.setText(value.getH() + ":" + value.getM());
                    txtName.setText("");
                    avatar.setIcon(null);
                    txtTime2.setText("");
                } else {
                    // lệch trái
                    txtMessage.setHorizontalAlignment(JLabel.LEFT);
                    txtMessage.setText(value.getContent());
                    txtName.setText(value.getAccount().getFullName());
                    txtTime2.setText(value.getH() + ":" + value.getM());
                    txtTime1.setText("");
                    customizeImageAvar(this.avatar, "../Images/unnamed.png");
                }
                
                txtMessage.setOpaque(false);
                avatar.setOpaque(false);
                txtTime1.setOpaque(false);
                txtName.setOpaque(false);
                //this.setOpaque(true);
            }

        }
        return this;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int type) {
        BufferedImage resizedImage = new BufferedImage(32, 32, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, this.avatar.getWidth(), this.avatar.getHeight(), null);
        g.dispose();

        return resizedImage;
    }

    public void customizeImageAvar(JLabel label, String path) {
        try {
            BufferedImage master = ImageIO.read(AccountItem.class.getResource(path));
            int type = master.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : master.getType();
            master = resizeImage(master, type);

            int diameter = Math.min(master.getWidth(), master.getHeight());
            BufferedImage mask = new BufferedImage(master.getWidth(), master.getHeight(), BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = mask.createGraphics();
            applyQualityRenderingHints(g2d);
            g2d.fillOval(0, 0, diameter - 1, diameter - 1);
            g2d.dispose();

            BufferedImage masked = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
            g2d = masked.createGraphics();
            applyQualityRenderingHints(g2d);
            int x = (diameter - master.getWidth()) / 2;
            int y = (diameter - master.getHeight()) / 2;
            g2d.drawImage(master, x, y, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
            g2d.drawImage(mask, 0, 0, null);
            g2d.dispose();

            ImageIcon imgaIcon = new ImageIcon(masked);
            label.setIcon(imgaIcon);
        } catch (IOException ex) {
            Logger.getLogger(AccountItem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
