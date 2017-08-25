/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Totoro
 */
public class ChatRoom extends javax.swing.JFrame {

    //private DefaultListModel<Account> modelAccount;
    private Client client = null;
    private static CustomizeAbstractListModel customizeListModel = null;
    private static boolean firstRun = true;
    private static Account accountSelected = null;
    private static CustomizeAbstractListChat customizeListChat = null;
    private static CustomizeAbstractListChat customizeListChatAPerson = null;
    private static HashMap<String, List<PackageChat>> hashMapChat = null;
    
    public Client getClient() {
        return client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
    
    public void initListChat() {
        if (customizeListChat == null) {
            customizeListChat = new CustomizeAbstractListChat(new ArrayList<PackageChat>());
            listAllText.setModel(customizeListChat);
            listAllText.setCellRenderer(new ChatItem(Client.account));
        }
    }

    /**
     * Creates new form ChatRoom
     */
    public ChatRoom(Client client) {
        initComponents();
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                txtSearch.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        customizeImageAvar(avartar, "../Images/unnamed.png");
        this.client = client;
        lbUsername.setText(client.getAccount().getFullName());
        System.out.println(client.getAccount().getFullName());
        
        hashMapChat = new HashMap<String, List<PackageChat>>();

        // khởi tạo list account
        try {
            client.startReciveFormServer();
        } catch (IOException ex) {
            Logger.getLogger(ChatRoom.class.getName()).log(Level.SEVERE, null, ex);
        }

        // khởi tạo list chat all
        initListChat();
    }
    
    private BufferedImage resizeImage(BufferedImage originalImage, int type) {
        BufferedImage resizedImage = new BufferedImage(avartar.getWidth(), avartar.getHeight(), type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, avartar.getWidth(), avartar.getHeight(), null);
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
    
    public static void setUsernameOnUi(String username) {
        lbUsername.setText(username);
    }

    // xem danh sách bạn bè
    public synchronized static void showListOfAccount(List<Account> listOfAccount) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (firstRun) {
                    customizeListModel = new CustomizeAbstractListModel(listOfAccount);
                    if (listAccount.isValid()) {
                        listAccount.setModel(customizeListModel);
                        listAccount.setCellRenderer(new AccountItem());
                        firstRun = false;
                    }
                } else if (listAccount.isValid()) {
                    int size = listOfAccount.size();
                    for (int i = 0; i < size; i++) {
                        if (customizeListModel.getDsAccount().contains(listOfAccount.get(i)) == false) {
                            customizeListModel.addAccount(listOfAccount.get(i));
                        }
                    }
                    
                    int size2 = customizeListModel.getDsAccount().size();
                    for (int j = 0; j < size2; j++) {
                        if (listOfAccount.contains(customizeListModel.getDsAccount().get(j)) == false) {
                            if (customizeListModel.getDsAccount().get(j).equals(accountSelected)) {
                                accountSelected = null;
                                panelHome.setVisible(true);
                                panelChat.setVisible(false);
                                panelChatAll.setVisible(false);
                            }
                            customizeListModel.deleteAccount(customizeListModel.getDsAccount().get(j));
                        }
                    }
                }

                // debug
                System.out.println("size of list account from server " + listOfAccount.size());
                if (listAccount.isValid()) {
                    for (int i = 0; i < listOfAccount.size(); i++) {
                        System.out.println(listOfAccount.get(i).getUserName());
                    }
                    System.out.println("----------------------------------");
                }
            }
        }).start();
    }
    
    public synchronized static void showListChatAll(PackageChat pckChat) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                customizeListChat.addToList(pckChat);
            }
        }).start();
    }
    
    public synchronized static void showListChatAPerson(PackageChat pckChat) {
        if (!hashMapChat.containsKey(pckChat.getAccount().getUserName())) {
            hashMapChat.put(pckChat.getAccount().getUserName(), new ArrayList<PackageChat>());
        }
        
        if(accountSelected.equals(pckChat.getAccount())){
            customizeListChatAPerson.addToList(pckChat);
        }else{
            hashMapChat.get(pckChat.getAccount().getUserName()).add(pckChat);
        }
        System.out.println("Client nhận dc " + pckChat);
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
        lbUsername = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        pannelContent = new javax.swing.JPanel();
        panelHeader = new javax.swing.JPanel();
        lbHome = new javax.swing.JLabel();
        lbChatAll = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        panelListAccount = new javax.swing.JScrollPane();
        listAccount = new javax.swing.JList<>();
        panlChatRoom = new javax.swing.JPanel();
        panelHome = new javax.swing.JPanel();
        panelChat = new javax.swing.JPanel();
        panelTitle = new javax.swing.JPanel();
        lbTitleContact = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        panelBody = new javax.swing.JPanel();
        panelText = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listText = new javax.swing.JList<>();
        panelMessage = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        txtMessage = new javax.swing.JTextField();
        panelChatAll = new javax.swing.JPanel();
        panelAllTitle = new javax.swing.JPanel();
        lbTitleContact1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        panelAllBody = new javax.swing.JPanel();
        panelAllText = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listAllText = new javax.swing.JList<>();
        paneAlllMessage = new javax.swing.JPanel();
        jSeparator4 = new javax.swing.JSeparator();
        txtAllMessage = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(850, 581));
        setMinimumSize(new java.awt.Dimension(850, 581));
        setPreferredSize(new java.awt.Dimension(850, 581));
        setResizable(false);

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

        lbUsername.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        lbUsername.setText("Trịnh Thanh Thuận");
        lbUsername.setToolTipText("");
        panelProfile.add(lbUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 184, -1));

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

        lbChatAll.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbChatAll.setText("ALL");
        lbChatAll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbChatAllMouseClicked(evt);
            }
        });

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
                .addComponent(lbChatAll)
                .addContainerGap(69, Short.MAX_VALUE))
        );
        panelHeaderLayout.setVerticalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lbChatAll)
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
            .addComponent(panelListAccount, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
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
        panlChatRoom.setMaximumSize(new java.awt.Dimension(570, 550));
        panlChatRoom.setMinimumSize(new java.awt.Dimension(570, 550));
        panlChatRoom.setPreferredSize(new java.awt.Dimension(570, 550));
        panlChatRoom.setLayout(new java.awt.CardLayout());

        panelHome.setBackground(new java.awt.Color(255, 255, 255));
        panelHome.setMaximumSize(new java.awt.Dimension(570, 561));
        panelHome.setMinimumSize(new java.awt.Dimension(570, 561));
        panelHome.setPreferredSize(new java.awt.Dimension(570, 561));

        javax.swing.GroupLayout panelHomeLayout = new javax.swing.GroupLayout(panelHome);
        panelHome.setLayout(panelHomeLayout);
        panelHomeLayout.setHorizontalGroup(
            panelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 570, Short.MAX_VALUE)
        );
        panelHomeLayout.setVerticalGroup(
            panelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 561, Short.MAX_VALUE)
        );

        panlChatRoom.add(panelHome, "card2");

        panelChat.setBackground(new java.awt.Color(247, 247, 247));
        panelChat.setAlignmentX(0.0F);
        panelChat.setAlignmentY(0.0F);
        panelChat.setMaximumSize(new java.awt.Dimension(570, 561));
        panelChat.setMinimumSize(new java.awt.Dimension(570, 561));
        panelChat.setPreferredSize(new java.awt.Dimension(570, 561));

        panelTitle.setBackground(new java.awt.Color(255, 255, 255));
        panelTitle.setAlignmentX(0.0F);
        panelTitle.setAlignmentY(0.0F);

        lbTitleContact.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        lbTitleContact.setText("conmeomunbencuaso");

        jSeparator1.setForeground(new java.awt.Color(204, 204, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-Talk Male-24.png"))); // NOI18N

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-Phone-24.png"))); // NOI18N

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-Video Message-24.png"))); // NOI18N

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-Christmas Star-24.png"))); // NOI18N

        javax.swing.GroupLayout panelTitleLayout = new javax.swing.GroupLayout(panelTitle);
        panelTitle.setLayout(panelTitleLayout);
        panelTitleLayout.setHorizontalGroup(
            panelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(panelTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbTitleContact)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap())
        );
        panelTitleLayout.setVerticalGroup(
            panelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lbTitleContact))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setAlignmentX(0.0F);
        panelBody.setAlignmentY(0.0F);
        panelBody.setMaximumSize(new java.awt.Dimension(570, 518));
        panelBody.setMinimumSize(new java.awt.Dimension(570, 518));
        panelBody.setPreferredSize(new java.awt.Dimension(570, 518));
        panelBody.setLayout(new java.awt.BorderLayout());

        panelText.setBackground(new java.awt.Color(255, 255, 255));
        panelText.setPreferredSize(new java.awt.Dimension(560, 458));

        jScrollPane1.setBorder(null);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(550, 450));
        jScrollPane1.setViewportView(listText);

        panelText.add(jScrollPane1);

        panelBody.add(panelText, java.awt.BorderLayout.CENTER);

        panelMessage.setBackground(new java.awt.Color(255, 255, 255));
        panelMessage.setPreferredSize(new java.awt.Dimension(570, 60));

        jSeparator3.setBackground(new java.awt.Color(0, 102, 204));
        jSeparator3.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator3.setPreferredSize(new java.awt.Dimension(550, 2));
        panelMessage.add(jSeparator3);

        txtMessage.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        txtMessage.setText("   Enter to talk...");
        txtMessage.setBorder(null);
        txtMessage.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        txtMessage.setPreferredSize(new java.awt.Dimension(550, 35));
        txtMessage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtMessageMouseClicked(evt);
            }
        });
        txtMessage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMessageKeyPressed(evt);
            }
        });
        panelMessage.add(txtMessage);

        panelBody.add(panelMessage, java.awt.BorderLayout.SOUTH);

        javax.swing.GroupLayout panelChatLayout = new javax.swing.GroupLayout(panelChat);
        panelChat.setLayout(panelChatLayout);
        panelChatLayout.setHorizontalGroup(
            panelChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelChatLayout.setVerticalGroup(
            panelChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChatLayout.createSequentialGroup()
                .addComponent(panelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(panelBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panlChatRoom.add(panelChat, "card3");

        panelChatAll.setBackground(new java.awt.Color(247, 247, 247));
        panelChatAll.setAlignmentX(0.0F);
        panelChatAll.setAlignmentY(0.0F);
        panelChatAll.setMaximumSize(new java.awt.Dimension(570, 561));
        panelChatAll.setMinimumSize(new java.awt.Dimension(570, 561));
        panelChatAll.setPreferredSize(new java.awt.Dimension(570, 561));

        panelAllTitle.setBackground(new java.awt.Color(255, 255, 255));
        panelAllTitle.setAlignmentX(0.0F);
        panelAllTitle.setAlignmentY(0.0F);

        lbTitleContact1.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        lbTitleContact1.setText("Chat with all people");

        jSeparator2.setForeground(new java.awt.Color(204, 204, 255));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-Talk Male-24.png"))); // NOI18N

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-Phone-24.png"))); // NOI18N

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-Video Message-24.png"))); // NOI18N

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-Christmas Star-24.png"))); // NOI18N

        javax.swing.GroupLayout panelAllTitleLayout = new javax.swing.GroupLayout(panelAllTitle);
        panelAllTitle.setLayout(panelAllTitleLayout);
        panelAllTitleLayout.setHorizontalGroup(
            panelAllTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2)
            .addGroup(panelAllTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbTitleContact1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addContainerGap())
        );
        panelAllTitleLayout.setVerticalGroup(
            panelAllTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAllTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelAllTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelAllTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lbTitleContact1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelAllBody.setBackground(new java.awt.Color(255, 255, 255));
        panelAllBody.setAlignmentX(0.0F);
        panelAllBody.setAlignmentY(0.0F);
        panelAllBody.setMinimumSize(new java.awt.Dimension(570, 508));
        panelAllBody.setPreferredSize(new java.awt.Dimension(570, 518));
        panelAllBody.setLayout(new java.awt.BorderLayout());

        panelAllText.setBackground(new java.awt.Color(255, 255, 255));
        panelAllText.setMaximumSize(new java.awt.Dimension(570, 458));
        panelAllText.setMinimumSize(new java.awt.Dimension(570, 458));
        panelAllText.setPreferredSize(new java.awt.Dimension(570, 458));

        jScrollPane2.setBorder(null);
        jScrollPane2.setPreferredSize(new java.awt.Dimension(550, 450));
        jScrollPane2.setRequestFocusEnabled(false);
        jScrollPane2.setViewportView(listAllText);

        panelAllText.add(jScrollPane2);

        panelAllBody.add(panelAllText, java.awt.BorderLayout.CENTER);

        paneAlllMessage.setBackground(new java.awt.Color(255, 255, 255));
        paneAlllMessage.setMaximumSize(new java.awt.Dimension(570, 60));
        paneAlllMessage.setMinimumSize(new java.awt.Dimension(570, 60));
        paneAlllMessage.setPreferredSize(new java.awt.Dimension(570, 60));

        jSeparator4.setBackground(new java.awt.Color(0, 102, 204));
        jSeparator4.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator4.setAlignmentY(0.0F);
        jSeparator4.setPreferredSize(new java.awt.Dimension(550, 2));
        paneAlllMessage.add(jSeparator4);

        txtAllMessage.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N
        txtAllMessage.setText("Enter to talk...");
        txtAllMessage.setBorder(null);
        txtAllMessage.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        txtAllMessage.setPreferredSize(new java.awt.Dimension(550, 40));
        txtAllMessage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAllMessageMouseClicked(evt);
            }
        });
        txtAllMessage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAllMessageKeyPressed(evt);
            }
        });
        paneAlllMessage.add(txtAllMessage);

        panelAllBody.add(paneAlllMessage, java.awt.BorderLayout.SOUTH);

        javax.swing.GroupLayout panelChatAllLayout = new javax.swing.GroupLayout(panelChatAll);
        panelChatAll.setLayout(panelChatAllLayout);
        panelChatAllLayout.setHorizontalGroup(
            panelChatAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelAllBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelAllTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelChatAllLayout.setVerticalGroup(
            panelChatAllLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelChatAllLayout.createSequentialGroup()
                .addComponent(panelAllTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(panelAllBody, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE))
        );

        panlChatRoom.add(panelChatAll, "card3");

        getContentPane().add(panlChatRoom, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void listAccountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listAccountMouseClicked
        synchronized (this) {
            if (listAccount.getSelectedValue() != null) {
                panelHome.setVisible(false);
                panelChat.setVisible(true);
                panelChatAll.setVisible(false);
                accountSelected = listAccount.getSelectedValue();
                lbTitleContact.setText(listAccount.getSelectedValue().getFullName());
                if (!hashMapChat.containsKey(accountSelected.getUserName())) {
                    hashMapChat.put(accountSelected.getUserName(), new ArrayList<PackageChat>());
                }
                if (customizeListChatAPerson == null) {
                    customizeListChatAPerson = new CustomizeAbstractListChat(hashMapChat.get(accountSelected.getUserName()));
                    listText.setModel(customizeListChatAPerson);
                    listText.setCellRenderer(new ChatItem(Client.account));
                }else{
                    customizeListChatAPerson.setDsChat(hashMapChat.get(accountSelected.getUserName()));
                }
            }
        }
    }//GEN-LAST:event_listAccountMouseClicked

    private void lbHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbHomeMouseClicked
        panelChat.setVisible(false);
        panelHome.setVisible(true);
        panelChatAll.setVisible(false);
    }//GEN-LAST:event_lbHomeMouseClicked

    private void txtSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSearchMouseClicked
        txtSearch.setText("");
    }//GEN-LAST:event_txtSearchMouseClicked

    private void txtMessageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMessageMouseClicked
        txtMessage.setText("");
    }//GEN-LAST:event_txtMessageMouseClicked

    private void txtMessageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMessageKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            synchronized (this) {
                if (accountSelected != null) {
                    Date date = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    int h = calendar.get(Calendar.HOUR_OF_DAY);
                    int m = calendar.get(Calendar.MINUTE);
                    PackageChat pckChat = new PackageChat(client.getAccount(), txtMessage.getText(), h, m);
                    
                    if (!hashMapChat.containsKey(accountSelected.getUserName())) {
                        hashMapChat.put(accountSelected.getUserName(), new ArrayList<PackageChat>());
                    }
                    //hashMapChat.get(accountSelected.getUserName()).add(pckChat);
                    customizeListChatAPerson.addToList(pckChat);
                    
                    // debug
                    System.out.println("size hashmap sau khi chat: " + hashMapChat.get(accountSelected.getUserName()).size());
                    System.out.println(pckChat);
                    
                    pckChat =new PackageChat(client.getAccount(), accountSelected.getUserName() + ";" + txtMessage.getText(), h, m);
                    client.chatWithAPerson(pckChat);
                }
            }
        }
    }//GEN-LAST:event_txtMessageKeyPressed

    private void txtAllMessageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAllMessageMouseClicked
        
    }//GEN-LAST:event_txtAllMessageMouseClicked

    private void txtAllMessageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAllMessageKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int h = calendar.get(Calendar.HOUR_OF_DAY);
            int m = calendar.get(Calendar.MINUTE);
            PackageChat pckChat = new PackageChat(client.getAccount(), txtAllMessage.getText(), h, m);
            synchronized (this) {
                customizeListChat.addToList(pckChat);
            }
            client.chatAll(pckChat);
        }
    }//GEN-LAST:event_txtAllMessageKeyPressed

    private void lbChatAllMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbChatAllMouseClicked
        panelChat.setVisible(false);
        panelHome.setVisible(false);
        panelChatAll.setVisible(true);
    }//GEN-LAST:event_lbChatAllMouseClicked

    /**
     * @param args the command line arguments
     */
    public void showchatRoom() {
        
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
    private javax.swing.JLabel avartar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel lbChatAll;
    private javax.swing.JLabel lbHome;
    private javax.swing.JLabel lbTitleContact;
    private javax.swing.JLabel lbTitleContact1;
    private static javax.swing.JLabel lbUsername;
    private static javax.swing.JList<Account> listAccount;
    private static javax.swing.JList<PackageChat> listAllText;
    private javax.swing.JList<PackageChat> listText;
    private javax.swing.JPanel paneAlllMessage;
    private javax.swing.JPanel panelAllBody;
    private javax.swing.JPanel panelAllText;
    private javax.swing.JPanel panelAllTitle;
    private javax.swing.JPanel panelBody;
    private static javax.swing.JPanel panelChat;
    private static javax.swing.JPanel panelChatAll;
    private javax.swing.JPanel panelHeader;
    private static javax.swing.JPanel panelHome;
    private javax.swing.JScrollPane panelListAccount;
    private javax.swing.JPanel panelMessage;
    private javax.swing.JPanel panelProfile;
    private javax.swing.JPanel panelSearch;
    private javax.swing.JPanel panelText;
    private javax.swing.JPanel panelTitle;
    private javax.swing.JPanel panelTop;
    private javax.swing.JPanel panlChatRoom;
    private javax.swing.JPanel pannelContent;
    private javax.swing.JTextField txtAllMessage;
    private javax.swing.JTextField txtMessage;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
