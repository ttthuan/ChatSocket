/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import static mmt.ChatRoom.applyQualityRenderingHints;

public class ChatItem extends javax.swing.JPanel implements ListCellRenderer<PackageChat> {

    private Account account = null;
    private boolean isFile = false;

    public ChatItem(Account account) {
        initComponents();
        this.account = account;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        avatar = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        txtName = new javax.swing.JLabel();
        txtTime2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        txtMessage = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(640, 50));
        setMinimumSize(new java.awt.Dimension(640, 50));
        setPreferredSize(new java.awt.Dimension(640, 50));

        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(579, 50));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(50, 50));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(avatar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(avatar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.add(jPanel3, java.awt.BorderLayout.WEST);

        jPanel4.setOpaque(false);
        jPanel4.setPreferredSize(new java.awt.Dimension(50, 100));
        jPanel4.setLayout(new java.awt.BorderLayout());

        txtName.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        txtName.setText("Trịnh Thanh Thuận");
        txtName.setMaximumSize(new java.awt.Dimension(36, 15));
        txtName.setMinimumSize(new java.awt.Dimension(36, 15));
        txtName.setPreferredSize(new java.awt.Dimension(36, 15));
        jPanel4.add(txtName, java.awt.BorderLayout.PAGE_START);

        txtTime2.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        txtTime2.setText("6:27 PM");
        jPanel4.add(txtTime2, java.awt.BorderLayout.PAGE_END);

        jPanel5.setOpaque(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(589, 18));

        txtMessage.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtMessage.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txtMessage.setText("SMS ");
        txtMessage.setPreferredSize(new java.awt.Dimension(30, 20));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 527, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 639, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel avatar;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel txtMessage;
    private javax.swing.JLabel txtName;
    private javax.swing.JLabel txtTime2;
    // End of variables declaration//GEN-END:variables

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // debug
        System.out.println("paint");
        Font font = new Font("Segeo UI", Font.PLAIN, 14);
        FontMetrics met = g.getFontMetrics(font);
        int heightText = met.getHeight();
        int widthText = met.stringWidth(txtMessage.getText());
        int length = txtMessage.getText().length();

        Dimension arcs = new Dimension(15, 15);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if(txtMessage.getHorizontalAlignment() == JLabel.RIGHT){
            //Draws the rounded opaque panel with borders.
            graphics.setColor(new Color(199, 237, 252));
            graphics.fillRoundRect(width - widthText - 38, 33 - heightText, widthText + 15, heightText + 4, arcs.width, arcs.height);//paint background
            graphics.setColor(new Color(199, 237, 252));
            graphics.drawRoundRect(width - widthText - 38, 33 - heightText, widthText + 15, heightText + 4, arcs.width, arcs.height);//paint border
        }else{
            //Draws the rounded opaque panel with borders.
            graphics.setColor(new Color(240, 244, 248));
            graphics.fillRoundRect(75, 33 - heightText, widthText + 15, heightText + 4, arcs.width, arcs.height);//paint background
            graphics.setColor(new Color(240, 244, 248));
            graphics.drawRoundRect(75, 33 - heightText, widthText + 15, heightText + 4, arcs.width, arcs.height);//paint border
        }
        
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends PackageChat> list, PackageChat value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            synchronized (this) {

                // debug
                System.out.println("render");
                txtTime2.setText(value.getH() + ":" + value.getM());
                txtMessage.setText(value.getContent());
                // kiểm tra người nào đẩy lên
                if (value.getAccount().equals(account)) {
                    // chủ nhân đẩy lên
                    // lệch phải
                    
                    txtMessage.setHorizontalAlignment(JLabel.RIGHT);
                    txtTime2.setHorizontalAlignment(JLabel.RIGHT);
                    txtName.setText("");
                    avatar.setIcon(null);
                } else {
                    // lệch trái
                    txtMessage.setHorizontalAlignment(JLabel.LEFT);
                    txtTime2.setHorizontalAlignment(JLabel.LEFT);
                    txtName.setText(value.getAccount().getFullName());
                    customizeImageAvar(this.avatar, "../Images/unnamed.png");
                }

                txtMessage.setOpaque(false);
                avatar.setOpaque(false);
                txtName.setOpaque(false);
                txtTime2.setOpaque(false);
                this.setOpaque(true);
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
