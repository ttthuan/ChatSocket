/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.awt.AlphaComposite;
import java.awt.Component;
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


public class AccountItem extends javax.swing.JPanel implements ListCellRenderer<Account> {

    /**
     * Creates new form AccountItem
     */
    public AccountItem() {
        initComponents();
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Account> listAccount, Account account, int index, boolean isSelect, boolean bln) {
        if (account != null) {
            this.name.setText(account.getFullName());

            customizeImageAvar(this.avatar, "../Images/unnamed.png");
            try {
                btnOnline.setIcon(new ImageIcon(ImageIO.read(AccountItem.class.getResource("../Images/checkbox-circle.png"))));
            } catch (IOException ex) {
                Logger.getLogger(AccountItem.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (isSelect) {
                name.setBackground(listAccount.getSelectionBackground());
                avatar.setBackground(listAccount.getSelectionBackground());
                setBackground(listAccount.getSelectionBackground());
                itemAccount.setBackground(listAccount.getSelectionBackground());
            } else { // when don't select
                name.setBackground(listAccount.getBackground());
                avatar.setBackground(listAccount.getBackground());
                setBackground(listAccount.getBackground());
                itemAccount.setBackground(listAccount.getBackground());
            }
            
            name.setOpaque(false);
            avatar.setOpaque(false);
            btnOnline.setOpaque(false);
            this.setOpaque(true);
        }

        return this;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int type) {
        BufferedImage resizedImage = new BufferedImage(48, 48, type);
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

    public static void applyQualityRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        itemAccount = new javax.swing.JPanel();
        btnOnline = new javax.swing.JLabel();
        avatar = new javax.swing.JLabel();
        name = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        itemAccount.setBackground(new java.awt.Color(226, 238, 246));
        itemAccount.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnOnline.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/checkbox-circle.png"))); // NOI18N
        itemAccount.add(btnOnline, new org.netbeans.lib.awtextra.AbsoluteConstraints(43, 34, -1, -1));
        itemAccount.add(avatar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 60, 60));

        name.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        name.setText("Trinh Thanh Thuận");
        itemAccount.add(name, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, -1, -1));

        add(itemAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 60));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel avatar;
    private javax.swing.JLabel btnOnline;
    private javax.swing.JPanel itemAccount;
    private javax.swing.JLabel name;
    // End of variables declaration//GEN-END:variables

}
