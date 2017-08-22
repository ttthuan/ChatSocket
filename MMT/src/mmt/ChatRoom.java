/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author Totoro
 */
public class ChatRoom extends javax.swing.JFrame {

    private DefaultListModel<Account> modelAccount;
    private Client client = null;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
       
    /**
     * Creates new form ChatRoom
     */
    public ChatRoom() {
        initComponents();
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                txtSearch.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        customizeImageAvar(avartar, "../Images/unnamed.png");
        showListOfAccount();
        
        txtChat.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    chat(txtChat.getText());
                }
            }
        });

    }

    private BufferedImage resizeImage(BufferedImage originalImage, int type) {
        BufferedImage resizedImage = new BufferedImage(avartar.getWidth(), avartar.getHeight(), type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, avartar.getWidth(), avartar.getHeight(), null);
        System.out.println(" " + avartar.getWidth() + " " + avartar.getHeight());
        g.dispose();

        return resizedImage;
    }

    public void customizeImageAvar(JLabel label, String path) {
        try {
            BufferedImage master = ImageIO.read(ChatRoom.class.getResource(path));
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
            Logger.getLogger(ChatRoom.class.getName()).log(Level.SEVERE, null, ex);
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

    // xem danh sách bạn bè
    public void showListOfAccount() {
        modelAccount = new DefaultListModel<>();
        modelAccount.addElement(new Account("1", "A", "Trương Tuấn Kiệt", "cpp"));
        modelAccount.addElement(new Account("2", "B", "Đỗ Minh Thiện", "java"));
        modelAccount.addElement(new Account("3", "C", "Trịnh Thanh Thuận", "cs"));
        modelAccount.addElement(new Account("4", "D", "Nguyễn Đức Đông", "ios"));
        modelAccount.addElement(new Account("5", "E", "Lâm Thiên Hưng", "wp"));
        modelAccount.addElement(new Account("6", "F", "Nguyễn Công Thành", "android"));
        listAccount.setModel(modelAccount);
        listAccount.setCellRenderer(new AccountItem());
    }

    public void chat(String sms) {
        JOptionPane.showMessageDialog(null, sms);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        panelTop = new javax.swing.JPanel();
        panelSearch = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        panelProfile = new javax.swing.JPanel();
        avartar = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        pannelContent = new javax.swing.JPanel();
        panelHeader = new javax.swing.JPanel();
        lbHome = new javax.swing.JLabel();
        lbContact = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        panelListAccount = new javax.swing.JScrollPane();
        listAccount = new javax.swing.JList<>();
        panlChatRoom = new javax.swing.JPanel();
        panelHome = new javax.swing.JPanel();
        panelChat = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lbTitleContact = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listChat = new javax.swing.JList<>();
        jSeparator3 = new javax.swing.JSeparator();
        txtChat = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(842, 551));

        jPanel2.setBackground(new java.awt.Color(240, 244, 248));
        jPanel2.setAlignmentX(0.0F);
        jPanel2.setAlignmentY(0.0F);
        jPanel2.setPreferredSize(new java.awt.Dimension(270, 480));

        panelTop.setBackground(new java.awt.Color(240, 244, 248));
        panelTop.setAlignmentX(0.0F);
        panelTop.setAlignmentY(0.0F);
        panelTop.setLayout(new java.awt.BorderLayout());

        panelSearch.setBackground(new java.awt.Color(240, 244, 248));
        panelSearch.setPreferredSize(new java.awt.Dimension(270, 25));

        txtSearch.setBackground(new java.awt.Color(240, 244, 248));
        txtSearch.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        txtSearch.setText("Search Friends");
        txtSearch.setAlignmentX(0.0F);
        txtSearch.setAlignmentY(0.0F);
        txtSearch.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 172, 193)));
        txtSearch.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        txtSearch.setPreferredSize(new java.awt.Dimension(80, 25));
        txtSearch.setRequestFocusEnabled(false);
        txtSearch.setSelectionColor(new java.awt.Color(240, 244, 248));
        txtSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSearchMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelSearchLayout = new javax.swing.GroupLayout(panelSearch);
        panelSearch.setLayout(panelSearchLayout);
        panelSearchLayout.setHorizontalGroup(
            panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSearchLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        panelSearchLayout.setVerticalGroup(
            panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSearchLayout.createSequentialGroup()
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelTop.add(panelSearch, java.awt.BorderLayout.SOUTH);

        panelProfile.setBackground(new java.awt.Color(240, 244, 248));
        panelProfile.setPreferredSize(new java.awt.Dimension(270, 60));
        panelProfile.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        avartar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/unnamed.png"))); // NOI18N
        avartar.setAlignmentY(0.0F);
        avartar.setMaximumSize(new java.awt.Dimension(48, 48));
        avartar.setMinimumSize(new java.awt.Dimension(48, 48));
        avartar.setName(""); // NOI18N
        avartar.setPreferredSize(new java.awt.Dimension(48, 48));
        panelProfile.add(avartar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 60, 60));

        jLabel20.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        jLabel20.setText("Trịnh Thanh Thuận");
        jLabel20.setToolTipText("");
        panelProfile.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 184, -1));

        jLabel21.setFont(new java.awt.Font("Noto Sans", 2, 14)); // NOI18N
        jLabel21.setText("Một mùa hè nóng nực...");
        jLabel21.setToolTipText("");
        panelProfile.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 184, -1));

        panelTop.add(panelProfile, java.awt.BorderLayout.CENTER);

        pannelContent.setBackground(new java.awt.Color(240, 244, 248));
        pannelContent.setAlignmentX(0.0F);
        pannelContent.setAlignmentY(0.0F);

        panelHeader.setBackground(new java.awt.Color(240, 244, 248));

        lbHome.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbHome.setText("HOME");
        lbHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbHomeMouseClicked(evt);
            }
        });

        lbContact.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbContact.setText("CONTACT");

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.setMaximumSize(new java.awt.Dimension(1, 39));
        jPanel4.setMinimumSize(new java.awt.Dimension(1, 39));
        jPanel4.setPreferredSize(new java.awt.Dimension(1, 39));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelHeaderLayout = new javax.swing.GroupLayout(panelHeader);
        panelHeader.setLayout(panelHeaderLayout);
        panelHeaderLayout.setHorizontalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(lbHome)
                .addGap(20, 20, 20)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbContact)
                .addContainerGap(69, Short.MAX_VALUE))
        );
        panelHeaderLayout.setVerticalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lbContact)
                        .addComponent(lbHome)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelListAccount.setBorder(null);
        panelListAccount.setAlignmentX(0.0F);
        panelListAccount.setAlignmentY(0.0F);

        listAccount.setBackground(new java.awt.Color(240, 244, 248));
        listAccount.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listAccount.setAlignmentX(0.0F);
        listAccount.setAlignmentY(0.0F);
        listAccount.setSelectionBackground(new java.awt.Color(187, 222, 251));
        listAccount.setSelectionForeground(new java.awt.Color(0, 0, 0));
        listAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listAccountMouseClicked(evt);
            }
        });
        panelListAccount.setViewportView(listAccount);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelListAccount)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelListAccount, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pannelContentLayout = new javax.swing.GroupLayout(pannelContent);
        pannelContent.setLayout(pannelContentLayout);
        pannelContentLayout.setHorizontalGroup(
            pannelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pannelContentLayout.createSequentialGroup()
                .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pannelContentLayout.setVerticalGroup(
            pannelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pannelContentLayout.createSequentialGroup()
                .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pannelContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(panelTop, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pannelContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.WEST);

        panlChatRoom.setBackground(new java.awt.Color(255, 255, 255));
        panlChatRoom.setAlignmentX(0.0F);
        panlChatRoom.setLayout(new java.awt.CardLayout());

        panelHome.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelHomeLayout = new javax.swing.GroupLayout(panelHome);
        panelHome.setLayout(panelHomeLayout);
        panelHomeLayout.setHorizontalGroup(
            panelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 654, Short.MAX_VALUE)
        );
        panelHomeLayout.setVerticalGroup(
            panelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 562, Short.MAX_VALUE)
        );

        panlChatRoom.add(panelHome, "card2");

        panelChat.setBackground(new java.awt.Color(247, 247, 247));
        panelChat.setAlignmentX(0.0F);
        panelChat.setAlignmentY(0.0F);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setAlignmentX(0.0F);
        jPanel1.setAlignmentY(0.0F);

        lbTitleContact.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        lbTitleContact.setText("conmeomunbencuaso");

        jSeparator1.setForeground(new java.awt.Color(204, 204, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-Talk Male-24.png"))); // NOI18N

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-Phone-24.png"))); // NOI18N

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-Video Message-24.png"))); // NOI18N

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-Christmas Star-24.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbTitleContact)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 358, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lbTitleContact))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setAlignmentX(0.0F);
        jPanel3.setAlignmentY(0.0F);

        jScrollPane1.setBorder(null);

        jScrollPane1.setViewportView(listChat);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
        );

        jSeparator3.setBackground(new java.awt.Color(204, 204, 255));
        jSeparator3.setForeground(new java.awt.Color(255, 255, 255));

        txtChat.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        txtChat.setText("   Enter to talk...");
        txtChat.setBorder(null);
        txtChat.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        txtChat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtChatMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator3)
                    .addComponent(txtChat))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtChat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelChatLayout = new javax.swing.GroupLayout(panelChat);
        panelChat.setLayout(panelChatLayout);
        panelChatLayout.setHorizontalGroup(
            panelChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelChatLayout.setVerticalGroup(
            panelChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChatLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panlChatRoom.add(panelChat, "card3");

        getContentPane().add(panlChatRoom, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void listAccountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listAccountMouseClicked
        // TODO add your handling code here:
        panelHome.hide();
        panelChat.show();
        lbTitleContact.setText(listAccount.getSelectedValue().getFullName());
    }//GEN-LAST:event_listAccountMouseClicked

    private void lbHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbHomeMouseClicked
        // TODO add your handling code here:
        panelChat.hide();
        panelHome.show();
    }//GEN-LAST:event_lbHomeMouseClicked

    private void txtSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSearchMouseClicked
        // TODO add your handling code here:
        txtSearch.setText("");
    }//GEN-LAST:event_txtSearchMouseClicked

    private void txtChatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtChatMouseClicked
        // TODO add your handling code here:
        txtChat.setText("");
    }//GEN-LAST:event_txtChatMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
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
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChatRoom().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel avartar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lbContact;
    private javax.swing.JLabel lbHome;
    private javax.swing.JLabel lbTitleContact;
    private javax.swing.JList<Account> listAccount;
    private javax.swing.JList<String> listChat;
    private javax.swing.JPanel panelChat;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelHome;
    private javax.swing.JScrollPane panelListAccount;
    private javax.swing.JPanel panelProfile;
    private javax.swing.JPanel panelSearch;
    private javax.swing.JPanel panelTop;
    private javax.swing.JPanel panlChatRoom;
    private javax.swing.JPanel pannelContent;
    private javax.swing.JTextField txtChat;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
