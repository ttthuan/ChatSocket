/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmt;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
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
import javax.swing.JFileChooser;
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
    private JFileChooser fileChooser = new JFileChooser();

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

        panelListAccount.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        jScrollAll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        jScrollSimple.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

        lbUsername.setText(client.getAccount().getFullName());
        System.out.println(client.getAccount().getFullName());

        customizeListChat = null;
        customizeListChatAPerson = null;
        hashMapChat = new HashMap<String, List<PackageChat>>();
        fileChooser = new JFileChooser();

        firstRun = true;

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
                int lastIndex = listAllText.getModel().getSize() - 1;
                if (lastIndex >= 0) {
                    listAllText.ensureIndexIsVisible(lastIndex);
                }
            }
        }).start();
    }

    public synchronized static void showListChatAPerson(PackageChat pckChat) {
        if (!hashMapChat.containsKey(pckChat.getAccount().getUserName())) {
            hashMapChat.put(pckChat.getAccount().getUserName(), new ArrayList<PackageChat>());
        }

        if (accountSelected != null && accountSelected.equals(pckChat.getAccount())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    customizeListChatAPerson.addToList(pckChat);
                    int lastIndex = listText.getModel().getSize() - 1;
                    if (lastIndex >= 0) {
                        listText.ensureIndexIsVisible(lastIndex);
                    }
                }
            }).start();

        } else {
            hashMapChat.get(pckChat.getAccount().getUserName()).add(pckChat);
        }
        System.out.println("Client nhận dc " + pckChat);
    }

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
        btnLogout = new javax.swing.JLabel();
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
        imageChatSingle = new javax.swing.JLabel();
        btnPhone = new javax.swing.JLabel();
        btnVideo = new javax.swing.JLabel();
        panelBody = new javax.swing.JPanel();
        panelText = new javax.swing.JPanel();
        jScrollSimple = new javax.swing.JScrollPane();
        listText = new javax.swing.JList<>();
        panelMessage = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        txtMessage = new javax.swing.JTextField();
        btnSendfile = new javax.swing.JLabel();
        btnSendText = new javax.swing.JLabel();
        panelChatAll = new javax.swing.JPanel();
        panelAllTitle = new javax.swing.JPanel();
        lbTitleContact1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jlbAvartarAll = new javax.swing.JLabel();
        btnPhoneAll = new javax.swing.JLabel();
        btnVideoAll = new javax.swing.JLabel();
        panelAllBody = new javax.swing.JPanel();
        panelAllText = new javax.swing.JPanel();
        jScrollAll = new javax.swing.JScrollPane();
        listAllText = new javax.swing.JList<>();
        paneAlllMessage = new javax.swing.JPanel();
        jSeparator4 = new javax.swing.JSeparator();
        txtAllMessage = new javax.swing.JTextField();
        btnSendFileAll = new javax.swing.JLabel();
        btnSendTextAll = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1020, 640));
        setMinimumSize(new java.awt.Dimension(1020, 640));
        setName("Skype"); // NOI18N
        setPreferredSize(new java.awt.Dimension(1020, 640));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(245, 249, 252));
        jPanel2.setAlignmentX(0.0F);
        jPanel2.setAlignmentY(0.0F);
        jPanel2.setPreferredSize(new java.awt.Dimension(320, 480));

        panelTop.setBackground(new java.awt.Color(240, 244, 248));
        panelTop.setAlignmentX(0.0F);
        panelTop.setAlignmentY(0.0F);
        panelTop.setLayout(new java.awt.BorderLayout());

        panelSearch.setBackground(new java.awt.Color(245, 249, 252));
        panelSearch.setPreferredSize(new java.awt.Dimension(270, 25));

        txtSearch.setBackground(new java.awt.Color(245, 249, 252));
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
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );
        panelSearchLayout.setVerticalGroup(
            panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSearchLayout.createSequentialGroup()
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelTop.add(panelSearch, java.awt.BorderLayout.SOUTH);

        panelProfile.setBackground(new java.awt.Color(245, 249, 252));
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
        lbUsername.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbUsernameMouseClicked(evt);
            }
        });
        panelProfile.add(lbUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 184, -1));

        jLabel21.setFont(new java.awt.Font("Noto Sans", 2, 14)); // NOI18N
        jLabel21.setText("App chat");
        jLabel21.setToolTipText("");
        panelProfile.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 184, -1));

        btnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logout.png"))); // NOI18N
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogoutMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogoutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogoutMouseExited(evt);
            }
        });
        panelProfile.add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, -1, -1));

        panelTop.add(panelProfile, java.awt.BorderLayout.CENTER);

        pannelContent.setBackground(new java.awt.Color(245, 249, 252));
        pannelContent.setAlignmentX(0.0F);
        pannelContent.setAlignmentY(0.0F);

        panelHeader.setBackground(new java.awt.Color(245, 249, 252));

        lbHome.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbHome.setText("HOME");
        lbHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbHomeMouseClicked(evt);
            }
        });

        lbChatAll.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbChatAll.setText("GROUP");
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

        listAccount.setBackground(new java.awt.Color(245, 249, 252));
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
            .addComponent(panelListAccount, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelListAccount, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pannelContentLayout = new javax.swing.GroupLayout(pannelContent);
        pannelContent.setLayout(pannelContentLayout);
        pannelContentLayout.setHorizontalGroup(
            pannelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pannelContentLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        panlChatRoom.setMaximumSize(new java.awt.Dimension(700, 600));
        panlChatRoom.setMinimumSize(new java.awt.Dimension(700, 600));
        panlChatRoom.setPreferredSize(new java.awt.Dimension(700, 600));
        panlChatRoom.setLayout(new java.awt.CardLayout());

        panelHome.setBackground(new java.awt.Color(255, 255, 255));
        panelHome.setMaximumSize(new java.awt.Dimension(570, 561));
        panelHome.setMinimumSize(new java.awt.Dimension(570, 561));
        panelHome.setPreferredSize(new java.awt.Dimension(570, 561));

        javax.swing.GroupLayout panelHomeLayout = new javax.swing.GroupLayout(panelHome);
        panelHome.setLayout(panelHomeLayout);
        panelHomeLayout.setHorizontalGroup(
            panelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 694, Short.MAX_VALUE)
        );
        panelHomeLayout.setVerticalGroup(
            panelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        panlChatRoom.add(panelHome, "card2");

        panelChat.setBackground(new java.awt.Color(247, 247, 247));
        panelChat.setAlignmentX(0.0F);
        panelChat.setAlignmentY(0.0F);
        panelChat.setMaximumSize(new java.awt.Dimension(700, 600));
        panelChat.setMinimumSize(new java.awt.Dimension(700, 600));
        panelChat.setPreferredSize(new java.awt.Dimension(700, 600));
        panelChat.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        panelTitle.setBackground(new java.awt.Color(255, 255, 255));
        panelTitle.setAlignmentX(0.0F);
        panelTitle.setAlignmentY(0.0F);
        panelTitle.setMaximumSize(new java.awt.Dimension(700, 80));
        panelTitle.setMinimumSize(new java.awt.Dimension(700, 80));
        panelTitle.setPreferredSize(new java.awt.Dimension(700, 80));

        lbTitleContact.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbTitleContact.setText("Conmeomunbencuaso");

        jSeparator1.setForeground(new java.awt.Color(204, 204, 255));

        imageChatSingle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/account-all.png"))); // NOI18N
        imageChatSingle.setPreferredSize(new java.awt.Dimension(64, 64));

        btnPhone.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/phone.png"))); // NOI18N
        btnPhone.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPhoneMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPhoneMouseExited(evt);
            }
        });

        btnVideo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/video.png"))); // NOI18N
        btnVideo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnVideoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnVideoMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panelTitleLayout = new javax.swing.GroupLayout(panelTitle);
        panelTitle.setLayout(panelTitleLayout);
        panelTitleLayout.setHorizontalGroup(
            panelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(panelTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imageChatSingle, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbTitleContact)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 347, Short.MAX_VALUE)
                .addComponent(btnPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnVideo)
                .addGap(19, 19, 19))
        );
        panelTitleLayout.setVerticalGroup(
            panelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTitleLayout.createSequentialGroup()
                .addGroup(panelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTitleLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(imageChatSingle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(panelTitleLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(panelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnVideo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnPhone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbTitleContact))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelChat.add(panelTitle);

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setAlignmentX(0.0F);
        panelBody.setAlignmentY(0.0F);
        panelBody.setMaximumSize(new java.awt.Dimension(570, 518));
        panelBody.setMinimumSize(new java.awt.Dimension(570, 518));
        panelBody.setPreferredSize(new java.awt.Dimension(700, 520));
        panelBody.setLayout(new java.awt.BorderLayout());

        panelText.setBackground(new java.awt.Color(255, 255, 255));
        panelText.setPreferredSize(new java.awt.Dimension(560, 458));

        jScrollSimple.setBorder(null);
        jScrollSimple.setMaximumSize(new java.awt.Dimension(640, 460));
        jScrollSimple.setMinimumSize(new java.awt.Dimension(640, 460));
        jScrollSimple.setPreferredSize(new java.awt.Dimension(640, 460));

        listText.setDoubleBuffered(true);
        listText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listTextMouseClicked(evt);
            }
        });
        jScrollSimple.setViewportView(listText);

        panelText.add(jScrollSimple);

        panelBody.add(panelText, java.awt.BorderLayout.CENTER);

        panelMessage.setBackground(new java.awt.Color(255, 255, 255));
        panelMessage.setMaximumSize(new java.awt.Dimension(570, 60));
        panelMessage.setMinimumSize(new java.awt.Dimension(570, 60));
        panelMessage.setPreferredSize(new java.awt.Dimension(570, 60));
        panelMessage.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));

        jSeparator3.setBackground(new java.awt.Color(0, 102, 204));
        jSeparator3.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator3.setPreferredSize(new java.awt.Dimension(640, 2));
        panelMessage.add(jSeparator3);

        txtMessage.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtMessage.setText("Type a message");
        txtMessage.setBorder(null);
        txtMessage.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        txtMessage.setPreferredSize(new java.awt.Dimension(570, 30));
        txtMessage.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMessageFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMessageFocusLost(evt);
            }
        });
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

        btnSendfile.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        btnSendfile.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnSendfile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/file-send.png"))); // NOI18N
        btnSendfile.setAlignmentY(0.0F);
        btnSendfile.setIconTextGap(0);
        btnSendfile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSendfileMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSendfileMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSendfileMouseExited(evt);
            }
        });
        panelMessage.add(btnSendfile);

        btnSendText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/send.png"))); // NOI18N
        btnSendText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSendTextMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSendTextMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSendTextMouseExited(evt);
            }
        });
        panelMessage.add(btnSendText);

        panelBody.add(panelMessage, java.awt.BorderLayout.SOUTH);

        panelChat.add(panelBody);

        panlChatRoom.add(panelChat, "card3");

        panelChatAll.setBackground(new java.awt.Color(247, 247, 247));
        panelChatAll.setAlignmentX(0.0F);
        panelChatAll.setAlignmentY(0.0F);
        panelChatAll.setMaximumSize(new java.awt.Dimension(700, 600));
        panelChatAll.setMinimumSize(new java.awt.Dimension(700, 600));
        panelChatAll.setPreferredSize(new java.awt.Dimension(700, 600));
        panelChatAll.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        panelAllTitle.setBackground(new java.awt.Color(255, 255, 255));
        panelAllTitle.setAlignmentX(0.0F);
        panelAllTitle.setAlignmentY(0.0F);
        panelAllTitle.setMaximumSize(new java.awt.Dimension(700, 80));
        panelAllTitle.setMinimumSize(new java.awt.Dimension(700, 80));
        panelAllTitle.setPreferredSize(new java.awt.Dimension(700, 80));

        lbTitleContact1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbTitleContact1.setText("People");

        jSeparator2.setForeground(new java.awt.Color(204, 204, 255));

        jlbAvartarAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/account-all.png"))); // NOI18N

        btnPhoneAll.setBackground(new java.awt.Color(255, 255, 255));
        btnPhoneAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/phone.png"))); // NOI18N
        btnPhoneAll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPhoneAllMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPhoneAllMouseExited(evt);
            }
        });

        btnVideoAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/video.png"))); // NOI18N
        btnVideoAll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnVideoAllMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnVideoAllMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panelAllTitleLayout = new javax.swing.GroupLayout(panelAllTitle);
        panelAllTitle.setLayout(panelAllTitleLayout);
        panelAllTitleLayout.setHorizontalGroup(
            panelAllTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2)
            .addGroup(panelAllTitleLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jlbAvartarAll, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbTitleContact1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 479, Short.MAX_VALUE)
                .addComponent(btnPhoneAll)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnVideoAll)
                .addGap(13, 13, 13))
        );
        panelAllTitleLayout.setVerticalGroup(
            panelAllTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAllTitleLayout.createSequentialGroup()
                .addGroup(panelAllTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAllTitleLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(panelAllTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPhoneAll)
                            .addComponent(lbTitleContact1)
                            .addComponent(btnVideoAll))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAllTitleLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jlbAvartarAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelChatAll.add(panelAllTitle);

        panelAllBody.setBackground(new java.awt.Color(255, 255, 255));
        panelAllBody.setAlignmentX(0.0F);
        panelAllBody.setAlignmentY(0.0F);
        panelAllBody.setMaximumSize(new java.awt.Dimension(700, 520));
        panelAllBody.setMinimumSize(new java.awt.Dimension(700, 520));
        panelAllBody.setName(""); // NOI18N
        panelAllBody.setPreferredSize(new java.awt.Dimension(700, 520));
        panelAllBody.setLayout(new java.awt.BorderLayout());

        panelAllText.setBackground(new java.awt.Color(255, 255, 255));
        panelAllText.setAlignmentX(0.0F);
        panelAllText.setAlignmentY(0.0F);
        panelAllText.setMaximumSize(new java.awt.Dimension(570, 460));
        panelAllText.setMinimumSize(new java.awt.Dimension(570, 460));
        panelAllText.setPreferredSize(new java.awt.Dimension(570, 460));
        panelAllText.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 0));

        jScrollAll.setBorder(null);
        jScrollAll.setMaximumSize(new java.awt.Dimension(640, 460));
        jScrollAll.setMinimumSize(new java.awt.Dimension(640, 460));
        jScrollAll.setPreferredSize(new java.awt.Dimension(640, 460));

        listAllText.setDoubleBuffered(true);
        jScrollAll.setViewportView(listAllText);

        panelAllText.add(jScrollAll);

        panelAllBody.add(panelAllText, java.awt.BorderLayout.CENTER);

        paneAlllMessage.setBackground(new java.awt.Color(255, 255, 255));
        paneAlllMessage.setMaximumSize(new java.awt.Dimension(700, 60));
        paneAlllMessage.setMinimumSize(new java.awt.Dimension(700, 60));
        paneAlllMessage.setPreferredSize(new java.awt.Dimension(700, 60));
        paneAlllMessage.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));

        jSeparator4.setBackground(new java.awt.Color(0, 102, 204));
        jSeparator4.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator4.setAlignmentY(0.0F);
        jSeparator4.setPreferredSize(new java.awt.Dimension(640, 2));
        paneAlllMessage.add(jSeparator4);

        txtAllMessage.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtAllMessage.setText("Type a message");
        txtAllMessage.setBorder(null);
        txtAllMessage.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        txtAllMessage.setPreferredSize(new java.awt.Dimension(570, 30));
        txtAllMessage.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAllMessageFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAllMessageFocusLost(evt);
            }
        });
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

        btnSendFileAll.setBackground(new java.awt.Color(255, 255, 255));
        btnSendFileAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/file-send.png"))); // NOI18N
        btnSendFileAll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSendFileAllMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSendFileAllMouseExited(evt);
            }
        });
        paneAlllMessage.add(btnSendFileAll);

        btnSendTextAll.setBackground(new java.awt.Color(255, 255, 255));
        btnSendTextAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/send.png"))); // NOI18N
        btnSendTextAll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSendTextAllMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSendTextAllMouseExited(evt);
            }
        });
        paneAlllMessage.add(btnSendTextAll);

        panelAllBody.add(paneAlllMessage, java.awt.BorderLayout.SOUTH);

        panelChatAll.add(panelAllBody);

        panlChatRoom.add(panelChatAll, "card3");

        getContentPane().add(panlChatRoom, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void listAccountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listAccountMouseClicked
        if (firstRun) {
            return;
        }
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
            } else {
                customizeListChatAPerson.setDsChat(hashMapChat.get(accountSelected.getUserName()));
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

    public void sendSingleMessage() {
        if (!txtMessage.getText().isEmpty()) {
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

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        customizeListChatAPerson.addToList(pckChat);
                    }
                }).start();

                // debug
                System.out.println("size hashmap sau khi chat: " + hashMapChat.get(accountSelected.getUserName()).size());
                System.out.println(pckChat);

                PackageChat pckChatNew = new PackageChat(client.getAccount(), accountSelected.getUserName() + ";" + txtMessage.getText(), h, m);
                client.chatWithAPerson(pckChatNew);
                txtMessage.setText("");

                int lastIndex = listText.getModel().getSize() - 1;
                if (lastIndex >= 0) {
                    listText.ensureIndexIsVisible(lastIndex);
                }
            }
        }
    }

    private void txtMessageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMessageKeyPressed
        synchronized (this) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                sendSingleMessage();
            }
        }
    }//GEN-LAST:event_txtMessageKeyPressed

    private void txtAllMessageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAllMessageMouseClicked
        if (txtAllMessage.getText().equalsIgnoreCase("Type a message")) {
            txtAllMessage.setText("");
        }
    }//GEN-LAST:event_txtAllMessageMouseClicked

    private void txtAllMessageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAllMessageKeyPressed
        synchronized (this) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                if (!txtAllMessage.getText().isEmpty()) {
                    Date date = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    int h = calendar.get(Calendar.HOUR_OF_DAY);
                    int m = calendar.get(Calendar.MINUTE);
                    PackageChat pckChat = new PackageChat(client.getAccount(), txtAllMessage.getText(), h, m);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            customizeListChat.addToList(pckChat);
                        }
                    }).start();

                    client.chatAll(pckChat);
                    txtAllMessage.setText("");
                    int lastIndex = listAllText.getModel().getSize() - 1;
                    if (lastIndex >= 0) {
                        listAllText.ensureIndexIsVisible(lastIndex);
                    }
                }
            }
        }
    }//GEN-LAST:event_txtAllMessageKeyPressed

    private void lbChatAllMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbChatAllMouseClicked
        panelChat.setVisible(false);
        panelHome.setVisible(false);
        panelChatAll.setVisible(true);
    }//GEN-LAST:event_lbChatAllMouseClicked

    private void btnSendfileMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSendfileMouseEntered
        setIcon(btnSendfile, "../Images/file-send-hover.png");
    }//GEN-LAST:event_btnSendfileMouseEntered

    private void btnSendfileMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSendfileMouseExited
        setIcon(btnSendfile, "../Images/file-send.png");
    }//GEN-LAST:event_btnSendfileMouseExited

    private void btnSendfileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSendfileMouseClicked
        synchronized (this) {
            int returnVal = fileChooser.showOpenDialog(ChatRoom.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                //debug
                System.out.println(file.getName());
                String name = file.getName();
                String pathFile = file.getPath();

                if (accountSelected != null) {
                    Date date = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    int h = calendar.get(Calendar.HOUR_OF_DAY);
                    int m = calendar.get(Calendar.MINUTE);
                    PackageChat pckChat = new PackageChat(client.getAccount(), name, h, m);
                    pckChat.setIsFile(true);
                    if (!hashMapChat.containsKey(accountSelected.getUserName())) {
                        hashMapChat.put(accountSelected.getUserName(), new ArrayList<PackageChat>());
                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            customizeListChatAPerson.addToList(pckChat);
                        }
                    }).start();
                }

                // xử lý send file
                // [0] username send, [1] full name, [2] user name receive, [3] file name
                client.notificationSendFile(client.getAccount().getUserName() + ";" + client.getAccount().getFullName() + ";" + accountSelected.getUserName() + ";" + name, pathFile);
            }
        }
    }//GEN-LAST:event_btnSendfileMouseClicked

    private void listTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listTextMouseClicked
        int index = listText.getSelectedIndex();
        if (customizeListChatAPerson.getSize() != 0 && index < customizeListChatAPerson.getSize()) {
            if (customizeListChatAPerson.getElementAt(index).isIsFile()) {
                File check = new File(customizeListChatAPerson.getElementAt(index).getContent());
                if (check.isFile()) {
                    int returnVal = fileChooser.showSaveDialog(ChatRoom.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        File fileOld = new File(customizeListChatAPerson.getElementAt(index).getContent());
                        String[] names = fileOld.getName().split(".");
                        fileOld.renameTo(file);
                    }
                }
            }
        }
    }//GEN-LAST:event_listTextMouseClicked

    private void btnPhoneAllMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPhoneAllMouseEntered
        setIcon(btnPhoneAll, "../Images/phone-hover.png");
    }//GEN-LAST:event_btnPhoneAllMouseEntered

    public void setIcon(JLabel label, String path) {
        try {
            BufferedImage bufferImage = ImageIO.read(ChatRoom.class.getResource(path));
            ImageIcon imgaIcon = new ImageIcon(bufferImage);
            label.setIcon(imgaIcon);
        } catch (IOException ex) {
            Logger.getLogger(ChatRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void btnPhoneAllMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPhoneAllMouseExited
        setIcon(btnPhoneAll, "../Images/phone.png");
    }//GEN-LAST:event_btnPhoneAllMouseExited

    private void btnVideoAllMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVideoAllMouseEntered
        setIcon(btnVideoAll, "../Images/video-hover.png");
    }//GEN-LAST:event_btnVideoAllMouseEntered

    private void btnVideoAllMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVideoAllMouseExited
        setIcon(btnVideoAll, "../Images/video.png");
    }//GEN-LAST:event_btnVideoAllMouseExited

    private void btnPhoneMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPhoneMouseEntered
        setIcon(btnPhone, "../Images/phone-hover.png");
    }//GEN-LAST:event_btnPhoneMouseEntered

    private void btnPhoneMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPhoneMouseExited
        setIcon(btnPhone, "../Images/phone.png");
    }//GEN-LAST:event_btnPhoneMouseExited

    private void btnVideoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVideoMouseEntered
        setIcon(btnVideo, "../Images/video-hover.png");
    }//GEN-LAST:event_btnVideoMouseEntered

    private void btnVideoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVideoMouseExited
        setIcon(btnVideo, "../Images/video.png");
    }//GEN-LAST:event_btnVideoMouseExited

    private void btnSendTextMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSendTextMouseEntered
        setIcon(btnSendText, "../Images/send-hover.png");
    }//GEN-LAST:event_btnSendTextMouseEntered

    private void btnSendTextMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSendTextMouseExited
        setIcon(btnSendText, "../Images/send.png");
    }//GEN-LAST:event_btnSendTextMouseExited

    private void btnSendFileAllMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSendFileAllMouseEntered
        setIcon(btnSendFileAll, "../Images/file-send-hover.png");
    }//GEN-LAST:event_btnSendFileAllMouseEntered

    private void btnSendFileAllMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSendFileAllMouseExited
        setIcon(btnSendFileAll, "../Images/file-send.png");
    }//GEN-LAST:event_btnSendFileAllMouseExited

    private void btnSendTextAllMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSendTextAllMouseEntered
        setIcon(btnSendTextAll, "../Images/send-hover.png");
    }//GEN-LAST:event_btnSendTextAllMouseEntered

    private void btnSendTextAllMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSendTextAllMouseExited
        setIcon(btnSendTextAll, "../Images/send.png");
    }//GEN-LAST:event_btnSendTextAllMouseExited

    private void txtAllMessageFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAllMessageFocusLost
        if (txtAllMessage.getText().isEmpty()) {
            txtAllMessage.setText("Type a message");
        }
    }//GEN-LAST:event_txtAllMessageFocusLost

    private void txtAllMessageFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAllMessageFocusGained
        if (txtAllMessage.getText().equalsIgnoreCase("Type a message")) {
            txtAllMessage.setText("");
        }
    }//GEN-LAST:event_txtAllMessageFocusGained

    private void txtMessageFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMessageFocusLost
        if (txtMessage.getText().isEmpty()) {
            txtMessage.setText("Type a message");
        }
    }//GEN-LAST:event_txtMessageFocusLost

    private void txtMessageFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMessageFocusGained
        if (txtMessage.getText().equalsIgnoreCase("Type a message")) {
            txtMessage.setText("");
        }
    }//GEN-LAST:event_txtMessageFocusGained

    private void btnLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseClicked
        client.logout();
    }//GEN-LAST:event_btnLogoutMouseClicked

    private void btnLogoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseEntered
        setIcon(btnLogout, "../Images/logout-hover.png");
    }//GEN-LAST:event_btnLogoutMouseEntered

    private void btnLogoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseExited
        setIcon(btnLogout, "../Images/logout.png");
    }//GEN-LAST:event_btnLogoutMouseExited

    private void lbUsernameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbUsernameMouseClicked
        if (evt.getClickCount() == 2) {
            // double mouse click
            ChangePassword changePass = new ChangePassword(client);
            changePass.showChangePass();
        }
    }//GEN-LAST:event_lbUsernameMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        client.logout();
    }//GEN-LAST:event_formWindowClosing

    private void btnSendTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSendTextMouseClicked
        synchronized (this) {
            sendSingleMessage();
        }
    }//GEN-LAST:event_btnSendTextMouseClicked

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

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel avartar;
    private javax.swing.JLabel btnLogout;
    private javax.swing.JLabel btnPhone;
    private javax.swing.JLabel btnPhoneAll;
    private javax.swing.JLabel btnSendFileAll;
    private javax.swing.JLabel btnSendText;
    private javax.swing.JLabel btnSendTextAll;
    private javax.swing.JLabel btnSendfile;
    private javax.swing.JLabel btnVideo;
    private javax.swing.JLabel btnVideoAll;
    private javax.swing.JLabel imageChatSingle;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollAll;
    private javax.swing.JScrollPane jScrollSimple;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel jlbAvartarAll;
    private javax.swing.JLabel lbChatAll;
    private javax.swing.JLabel lbHome;
    private javax.swing.JLabel lbTitleContact;
    private javax.swing.JLabel lbTitleContact1;
    private static javax.swing.JLabel lbUsername;
    private static javax.swing.JList<Account> listAccount;
    private static javax.swing.JList<PackageChat> listAllText;
    private static javax.swing.JList<PackageChat> listText;
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
