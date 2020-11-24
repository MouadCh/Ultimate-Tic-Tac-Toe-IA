/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TicTacToe;

import static Users.User.playTime;
import Users.User1;
import Users.User2;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;

/*
    * @author MouadC
*/

public class TicTacFrame extends javax.swing.JFrame {
    
    private final static String GAMES_FILE = "Games.txt";
    private final static String LIST_USERS_FILENAME="saves/list_users.txt";
    private final static String USERS_DIRECTORY="saves/users/";
    
    public static boolean CAN_PLAY_GAME=true;  //Le jeu est en pause ou nn
    public static int GAME_DURATION=15;  //Duree du jeu
    public static int MACHINE_NIVEAU=1;  //Duree du jeu


    private TicTacPrincipalePanel pan;
    private User1 user1;
    private User2 user2;

    private Vector<Vector> games=new Vector();  //Contients toutes les parties sauvegardés
    private Vector<Vector> gameUndoVector;
    
//    private int playingMode;
//    private int secindPlayer;
    
    private Timer timerRandomeMove;
    private Timer timerMachineMove;
    
    private String curentUsername="";
    private String currentPassword="";
    

//    public Vector actifVector=new Vector();
    /**
     * Creates new form Main
     */
    public TicTacFrame() {
        initComponents();
        this.setTitle("Ultimate Tic Tac Toe jeu");
        this.setLocationRelativeTo(null);
        
        JLabel bg=new JLabel("");
        bg.setIcon(  new javax.swing.ImageIcon(  getClass().getResource("/Img/firstPage_tic-tac-toe.jpg") ) );
        bg.setSize(Home_Panel.getWidth(), Home_Panel.getHeight());
        Home_Panel.add(bg);
                
        InitFrame();
        
        getGamesFromeFile();
    }
    
    public void InitFrame(){
        InitializeComboBoxes();
        InitializemoveTimeProgressBar();
        InitializeFrameAtrributes();
        InitializeFrameComponents();
        InitializePanel();
        InitializeTimerMachine();
    }
    
    private void InitializeComboBoxes(){
        this.playersComboBox.removeAllItems();
        this.playersComboBox.addItem("Human vs Human");
        this.playersComboBox.addItem("Human vs Machine");
        
        this.SecondPlayerjComboBox.removeAllItems();
        this.SecondPlayerjComboBox.addItem("Parties");
        
        this.Machine_NiveaujComboBox.removeAllItems();
        this.Machine_NiveaujComboBox.addItem("Easy");
        this.Machine_NiveaujComboBox.addItem("Medium");
        this.Machine_NiveaujComboBox.addItem("Hard");
        Machine_NiveaujComboBox.setVisible(false);
        
        this.gameModesComboBox.removeAllItems();
        this.gameModesComboBox.addItem("3 successive victoires");
        this.gameModesComboBox.addItem("Premier Victoire");
        
        this.GamesToLoadComboBox1.removeAllItems();
        this.GamesToLoadComboBox1.addItem("Parties");
    }
    private void InitializemoveTimeProgressBar(){
        this.moveTimeProgressBar.setMaximum((int) GAME_DURATION);
        this.moveTimeProgressBar.setValue((int) GAME_DURATION);
    }
    private void InitializeFrameAtrributes(){
        user1=new User1("");
        user2=new User2("");    
            
        gameUndoVector=new Vector();
        
        timerRandomeMove = new Timer();
        timerMachineMove = new Timer();
        
//        this.playingMode=gameModesComboBox.getSelectedIndex();
    }
    private void InitializeFrameComponents(){
        timerInputValue.setValue(3);
    }
    private void InitializePanel(){
        pan=new TicTacPrincipalePanel(this,this.Panel.getWidth()-6,this.Panel.getHeight()-6);         
        this.Panel.setBackground(pan.getBackground());
        this.Panel.add(pan);
        DisableAllPanels();
    }
    private void InitializeTimerMachine(){
        timerMachineMove.cancel();
        timerMachineMove = new Timer();
        timerMachineMove.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                playMachineMove();
            }
        }, 0, 3000);
    }
    
    
    public void RestartTimer(){
            RefreshTimer();
            timerRandomeMove.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        if(TicTacFrame.CAN_PLAY_GAME)
                            countPlayTimeSeconds();
                    } catch (Exception ex) {}
                }
            }, 0,1000);
    }
    
    
    public void RefreshTimer(){
        playTime=0;
        timerRandomeMove.cancel();
        timerRandomeMove =new Timer();
    }
    public void countPlayTimeSeconds() throws Exception{
        if(playTime<TicTacFrame.GAME_DURATION){
            playTime++;
            moveTimeProgressBar().setValue((int) (TicTacFrame.GAME_DURATION-playTime));
//                    .setText("Time remains : "+(duree-playTime));
        }else{
           PlayRandomMove();
           playTime=0;
        }
    }
    public void PlayRandomMove(){
        try{
            int i = new Random().nextInt(9);
            int j = new Random().nextInt(9);
            if(pan.getPanelAt(i+1).getButtonAt(j+1).isEnabled())
                pan.getPanelAt(i+1).getButtonAt(j+1).setBackground(Color.cyan);
            else
                PlayRandomMove();
        }catch(StackOverflowError e){
        }
    }
    
    private void playMachineMove(){
        if(!User1.isPlay && !User2.isPlay){
            pan.PlayMachineMove();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
   
    
    public void addRowToTable(String col1,String col2,String col3,int col4){
        DefaultTableModel model = (DefaultTableModel) jTablle_historique.getModel();
        model.addRow(new Object[]{col1,col2,col3,col4});
    }
    
    private void DisableAllPanels(){
        this.pan.DesableAllPanels();
    }
    private void EnableAllPanels(){
        this.pan.EnableAllPanels();
    }
    private void RefreshAllPanels(){
        this.pan.RefreshAllPanels();
    }   
    
//    public void RestartTimer(){
//        setTimerRandomInitialisationOn();
//    }
        
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        Home_Panel = new javax.swing.JPanel();
        TTT_Panel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        regleButton1 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        loginPanel = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        currentUsernameJTextField = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        signUpUsernameJTextField = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        seConnecterButton = new javax.swing.JButton();
        SinscrireButton = new javax.swing.JButton();
        currentPasswordJPasswordField = new javax.swing.JPasswordField();
        signUpPasswordJPasswordField = new javax.swing.JPasswordField();
        jLabel21 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        Panel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        startGameButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        user1_NameTextField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        playersComboBox = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        gameModesComboBox = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        timerInputValue = new javax.swing.JSpinner();
        jLabel14 = new javax.swing.JLabel();
        Machine_NiveaujComboBox = new javax.swing.JComboBox<>();
        SecondPlayerjComboBox = new javax.swing.JComboBox<>();
        gameControlPanel = new javax.swing.JPanel();
        undoButton = new javax.swing.JButton();
        restartGameButton = new javax.swing.JButton();
        moveTimeProgressBar = new javax.swing.JProgressBar();
        hintButton = new javax.swing.JButton();
        pauseAndPlayToggleButton = new javax.swing.JToggleButton();
        SaveGameButton = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTablle_historique = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        GamesToLoadComboBox1 = new javax.swing.JComboBox<>();
        ApplyGamejButton = new javax.swing.JButton();
        ResumeGamejButton = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.darkGray, java.awt.Color.darkGray, java.awt.Color.darkGray, java.awt.Color.darkGray));
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        Home_Panel.setBackground(new java.awt.Color(192, 192, 192));

        TTT_Panel.setBackground(new java.awt.Color(10, 10,10 ,75));
        TTT_Panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Comic Sans MS", 2, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("The Ultimate Tic Tac Toe,");

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Yu Gothic Light", 2, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Vous maîtrisez déjà le jeu classique des X et O? Ultimate tic tac toe est une touche amusante et stratégique");

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Yu Gothic Light", 2, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("compétences contre l'ordinateur.");

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Yu Gothic Light", 2, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("du jeu que nous connaissons et aimons tous. Jouez avec vos amis ou perfectionnez vos ");

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Dialog", 3, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Qu'attendez-vous?");

        jButton3.setBackground(new java.awt.Color(237, 231, 220));
        jButton3.setText("Jouer Vs IA");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        regleButton1.setBackground(new java.awt.Color(237, 231, 220));
        regleButton1.setText("Consulter les règles");
        regleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regleButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout TTT_PanelLayout = new javax.swing.GroupLayout(TTT_Panel);
        TTT_Panel.setLayout(TTT_PanelLayout);
        TTT_PanelLayout.setHorizontalGroup(
            TTT_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TTT_PanelLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(TTT_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(TTT_PanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(regleButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3))
                    .addGroup(TTT_PanelLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(TTT_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))))
                .addContainerGap(117, Short.MAX_VALUE))
        );
        TTT_PanelLayout.setVerticalGroup(
            TTT_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TTT_PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addGap(15, 15, 15)
                .addGroup(TTT_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jButton3)
                    .addComponent(regleButton1))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout Home_PanelLayout = new javax.swing.GroupLayout(Home_Panel);
        Home_Panel.setLayout(Home_PanelLayout);
        Home_PanelLayout.setHorizontalGroup(
            Home_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Home_PanelLayout.createSequentialGroup()
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(Home_PanelLayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(TTT_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
        );
        Home_PanelLayout.setVerticalGroup(
            Home_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Home_PanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel22)
                .addGap(31, 31, 31)
                .addComponent(TTT_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(326, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Acceuil", Home_Panel);

        jPanel6.setBackground(new java.awt.Color(192, 192, 192));

        loginPanel.setBackground(new java.awt.Color(8, 49, 58,95));
        loginPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, null, java.awt.Color.gray, java.awt.Color.gray));

        jLabel15.setBackground(new java.awt.Color(255, 255, 255));
        jLabel15.setFont(new java.awt.Font("Dubai Light", 3, 24)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Se connecter");

        jLabel16.setBackground(new java.awt.Color(255, 255, 255));
        jLabel16.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Nom d'utilisateur :");

        currentUsernameJTextField.setBackground(new java.awt.Color(204, 204, 204));
        currentUsernameJTextField.setForeground(new java.awt.Color(0, 102, 102));
        currentUsernameJTextField.setCaretColor(new java.awt.Color(0, 102, 102));
        currentUsernameJTextField.setSelectedTextColor(new java.awt.Color(51, 0, 0));

        jLabel19.setBackground(new java.awt.Color(255, 255, 255));
        jLabel19.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Mot de passe :");

        jLabel17.setBackground(new java.awt.Color(255, 255, 255));
        jLabel17.setFont(new java.awt.Font("Dubai Light", 3, 27)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("S'inscrire");

        jLabel18.setBackground(new java.awt.Color(255, 255, 255));
        jLabel18.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Nom d'utilisateur :");

        signUpUsernameJTextField.setBackground(new java.awt.Color(204, 204, 204));
        signUpUsernameJTextField.setForeground(new java.awt.Color(0, 102, 102));
        signUpUsernameJTextField.setCaretColor(new java.awt.Color(0, 102, 102));
        signUpUsernameJTextField.setSelectedTextColor(new java.awt.Color(51, 0, 0));

        jLabel20.setBackground(new java.awt.Color(255, 255, 255));
        jLabel20.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Mot de passe :");

        jSeparator2.setForeground(new java.awt.Color(0, 51, 51));

        seConnecterButton.setBackground(new java.awt.Color(116, 183, 172));
        seConnecterButton.setText("Se connecter");
        seConnecterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seConnecterButtonActionPerformed(evt);
            }
        });

        SinscrireButton.setBackground(new java.awt.Color(116, 183, 172));
        SinscrireButton.setText("S'inscrire");
        SinscrireButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SinscrireButtonActionPerformed(evt);
            }
        });

        currentPasswordJPasswordField.setBackground(new java.awt.Color(204, 204, 204));
        currentPasswordJPasswordField.setSelectedTextColor(new java.awt.Color(51, 0, 0));

        signUpPasswordJPasswordField.setBackground(new java.awt.Color(204, 204, 204));
        signUpPasswordJPasswordField.setSelectedTextColor(new java.awt.Color(51, 0, 0));

        javax.swing.GroupLayout loginPanelLayout = new javax.swing.GroupLayout(loginPanel);
        loginPanel.setLayout(loginPanelLayout);
        loginPanelLayout.setHorizontalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginPanelLayout.createSequentialGroup()
                        .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel19)
                            .addComponent(seConnecterButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(loginPanelLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(currentUsernameJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                                    .addComponent(currentPasswordJPasswordField))))
                        .addGap(43, 43, 43))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginPanelLayout.createSequentialGroup()
                        .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel20)
                            .addComponent(SinscrireButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(loginPanelLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(signUpUsernameJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                                    .addComponent(signUpPasswordJPasswordField))))
                        .addGap(41, 41, 41))))
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 8, Short.MAX_VALUE))
        );
        loginPanelLayout.setVerticalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currentUsernameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(currentPasswordJPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(seConnecterButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(signUpUsernameJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(signUpPasswordJPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SinscrireButton)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/tic-tac-toe.jpg"))); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel21)
                .addGap(28, 28, 28)
                .addComponent(loginPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(loginPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Se connecter", jPanel6);

        jPanel1.setBackground(new java.awt.Color(192, 192, 192));

        Panel.setBackground(new java.awt.Color(255, 255, 255));
        Panel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray, java.awt.Color.lightGray, java.awt.Color.lightGray), "Le jeu n'a pas commencé"));
        Panel.setForeground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout PanelLayout = new javax.swing.GroupLayout(Panel);
        Panel.setLayout(PanelLayout);
        PanelLayout.setHorizontalGroup(
            PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        PanelLayout.setVerticalGroup(
            PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 499, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(8, 49, 58,85));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        jPanel3.setForeground(new java.awt.Color(51, 51, 51));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Dubai", 3, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Paramétres :");

        startGameButton.setText("Commencer la partie");
        startGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startGameButtonActionPerformed(evt);
            }
        });

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Le nom du 1er Joueur :");

        user1_NameTextField.setEnabled(false);
        user1_NameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                user1_NameTextFieldActionPerformed(evt);
            }
        });

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Le nom du 2ème Joueur :");

        playersComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        playersComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playersComboBoxActionPerformed(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Jouer avec :");

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Tic Tac Toe mode :");

        gameModesComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gameModesComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gameModesComboBoxActionPerformed(evt);
            }
        });

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Régler le minuteur à :");

        jLabel13.setBackground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("seconds");

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        jLabel14.setBackground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("(min 3seconds)");

        Machine_NiveaujComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        Machine_NiveaujComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Machine_NiveaujComboBoxActionPerformed(evt);
            }
        });

        SecondPlayerjComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel11)
                                            .addComponent(jLabel2))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(gameModesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(playersComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(Machine_NiveaujComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(timerInputValue, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel14))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(user1_NameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(SecondPlayerjComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jSeparator1)
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(startGameButton)
                .addGap(30, 30, 30))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playersComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(Machine_NiveaujComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gameModesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(timerInputValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(user1_NameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(SecondPlayerjComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(startGameButton)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        gameControlPanel.setBackground(new java.awt.Color(8, 49, 58,95));
        gameControlPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 6, 6));
        gameControlPanel.setForeground(java.awt.Color.lightGray);

        undoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/back.png"))); // NOI18N
        undoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoButtonActionPerformed(evt);
            }
        });

        restartGameButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/reset.png"))); // NOI18N
        restartGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restartGameButtonActionPerformed(evt);
            }
        });

        hintButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/hint.png"))); // NOI18N
        hintButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hintButtonActionPerformed(evt);
            }
        });

        pauseAndPlayToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pause.png"))); // NOI18N
        pauseAndPlayToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseAndPlayToggleButtonActionPerformed(evt);
            }
        });

        SaveGameButton.setText("save");
        SaveGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveGameButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout gameControlPanelLayout = new javax.swing.GroupLayout(gameControlPanel);
        gameControlPanel.setLayout(gameControlPanelLayout);
        gameControlPanelLayout.setHorizontalGroup(
            gameControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameControlPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(restartGameButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(undoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addComponent(pauseAndPlayToggleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(moveTimeProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(hintButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SaveGameButton)
                .addGap(41, 41, 41))
        );
        gameControlPanelLayout.setVerticalGroup(
            gameControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameControlPanelLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(gameControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(gameControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(hintButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(SaveGameButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(gameControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(undoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(pauseAndPlayToggleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(restartGameButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(moveTimeProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(8, 49, 58,85));

        jPanel4.setBackground(new java.awt.Color(228, 229, 232));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        jPanel4.setForeground(new java.awt.Color(153, 153, 153));

        jTablle_historique.setBackground(new java.awt.Color(255, 255, 255));
        jTablle_historique.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tour", "Joueur", "Position", "Time(s)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTablle_historique);
        if (jTablle_historique.getColumnModel().getColumnCount() > 0) {
            jTablle_historique.getColumnModel().getColumn(0).setResizable(false);
            jTablle_historique.getColumnModel().getColumn(1).setResizable(false);
            jTablle_historique.getColumnModel().getColumn(2).setResizable(false);
            jTablle_historique.getColumnModel().getColumn(3).setResizable(false);
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Historique du partie courant", jPanel4);

        jPanel7.setBackground(new java.awt.Color(10, 10,10 ,75));

        jLabel23.setBackground(new java.awt.Color(255, 255, 255));
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Vous pouver choisir une ancienne partie puis continuer !");

        jLabel24.setBackground(new java.awt.Color(255, 255, 255));
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Choisir votre partie :");

        GamesToLoadComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        GamesToLoadComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GamesToLoadComboBox1ActionPerformed(evt);
            }
        });

        ApplyGamejButton.setText("Apperçu");
        ApplyGamejButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ApplyGamejButtonActionPerformed(evt);
            }
        });

        ResumeGamejButton.setText("Continuer");
        ResumeGamejButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResumeGamejButtonActionPerformed(evt);
            }
        });

        jLabel25.setBackground(new java.awt.Color(255, 255, 255));
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Choisir une partie, puis cliquer sur \" Apperçu \", pour apperçue ");

        jLabel26.setBackground(new java.awt.Color(255, 255, 255));
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("la partie. \" Continuer \" pour jouer à cette partie.");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(GamesToLoadComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addComponent(ApplyGamejButton)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(ResumeGamejButton))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel25)
                                    .addComponent(jLabel26))))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel26)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addComponent(GamesToLoadComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ApplyGamejButton)
                    .addComponent(ResumeGamejButton))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Historique des parties", jPanel7);

        jLabel10.setBackground(new java.awt.Color(0, 0, 0));
        jLabel10.setFont(new java.awt.Font("Dubai", 3, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Historique");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTabbedPane2))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(gameControlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(gameControlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Lancer une jeu Vs IA", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jTabbedPane1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    private void startGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startGameButtonActionPerformed
        // TODO add your handling code here:
        pan.getAlgorithme().Tac();
        StartGame();
    }//GEN-LAST:event_startGameButtonActionPerformed
    
    public void StartGame(){
        CAN_PLAY_GAME=true;
        user1.setUsername(this.user1_NameTextField.getText());
        user2.setUsername((String) SecondPlayerjComboBox.getSelectedItem());
        
        if(!"".equals(user1.getUsername()) && !"".equals(user2.getUsername()) && ( (Integer) this.timerInputValue.getValue()).intValue()>2){
           
            GAME_DURATION=( (Integer) this.timerInputValue.getValue()).intValue();
            User1.isPlay=true;
            moveTimeProgressBar.setMaximum(( (Integer) this.timerInputValue.getValue()).intValue());
            
            this.RefreshAllPanels();
            this.EnableAllPanels();
            
            
            removeAllFromHistoriqueTable();
            RestartTimer();

            TicTacPrincipalePanel.ID_SAVE_GAME=new Random().nextInt();
//            RefreshpauseAndPlayToggleButton(); 
        } 
    }
    
    private void user1_NameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_user1_NameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_user1_NameTextFieldActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        this.jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        // TODO add your handling code here:
        getPreviousMove();
        RestartTimer();
    }//GEN-LAST:event_undoButtonActionPerformed

    private void hintButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hintButtonActionPerformed
        // TODO add your handling code here:
        PlayRandomMove();
//        for (int i = 0; i < 9; i++) {
//            this.pan.getPanelAt(i+1).getBoardToString();
//            System.out.println("");
//        }

//        this.pan.getPanelAt(1).getBoardToString();
//        this.pan.PlayBestMoveFromAll();

        
    }//GEN-LAST:event_hintButtonActionPerformed

    private void pauseAndPlayToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseAndPlayToggleButtonActionPerformed
        // TODO add your handling code here:
        PauseGameToggled();
    }//GEN-LAST:event_pauseAndPlayToggleButtonActionPerformed
    
    public void PauseGameToggled(){
        if(pauseAndPlayToggleButton.isSelected()){ 
        //Game in pause
            pauseAndPlayToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/play.png"))); // NOI18N
            addToGameVector();
            CAN_PLAY_GAME=false;
            pan.DesactifAllPanels();
        }else{ 
        //Game in play
            pauseAndPlayToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pause.png"))); // NOI18N            
            loadMoveAt(this.gameUndoVector.size()-1);
            CAN_PLAY_GAME=true;
//            getPreviousMove();
        }
    }
    
    private void playersComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playersComboBoxActionPerformed
        // TODO add your handling code here:
        int index=playersComboBox.getSelectedIndex();
        if (index == 0) {
//            user2_NameTextField.setText("");
//            user2_NameTextField.setEditable(true);
            AddComboBoxSecondPlayer(this.curentUsername);
            Machine_NiveaujComboBox.setVisible(false);
            this.gameModesComboBox.setEnabled(true);
        } else {
//            user2_NameTextField.setText("Machine");
//            user2_NameTextField.setEditable(false
            SecondPlayerjComboBox.removeAllItems();
            SecondPlayerjComboBox.addItem("Machine");
            Machine_NiveaujComboBox.setVisible(true);
            
            this.gameModesComboBox.setEnabled(false);
            this.gameModesComboBox.setSelectedIndex(1);
        }
        this.repaint();
    }//GEN-LAST:event_playersComboBoxActionPerformed

    private void restartGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restartGameButtonActionPerformed
        // TODO add your handling code here:
        RestartGame();
    }//GEN-LAST:event_restartGameButtonActionPerformed
    
    public void RestartGame(){
        if(this.gameUndoVector.size()>0){
            loadMoveAt(0);
            this.gameUndoVector.clear();
            RestartTimer();
        }
        pan.getAlgorithme().initiateBoard();
    }
    
    private void gameModesComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gameModesComboBoxActionPerformed
        // TODO add your handling code here:
//        this.playingMode=gameModesComboBox.getSelectedIndex();
        this.repaint();
    }//GEN-LAST:event_gameModesComboBoxActionPerformed

    private void seConnecterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seConnecterButtonActionPerformed
        // TODO add your handling code here:
        SeConnecte();
        
    }//GEN-LAST:event_seConnecterButtonActionPerformed
    
    public void SeConnecte(){
        //        InitializeFrameAtrributes();

        User1.isPlay=true;
        User2.isPlay=false;
        
        CAN_PLAY_GAME=false;

//        pauseAndPlayToggleButton.setSelected(false);
       
        GAME_DURATION=3;
        
        InitializemoveTimeProgressBar();
        InitializeComboBoxes();

        this.gameUndoVector.clear();
        
        RefreshAllPanels();
        DisableAllPanels();
        
        String currentUsername = this.currentUsernameJTextField.getText().toString();
        String currentPassword = this.currentPasswordJPasswordField.getText().toString();
        
        if(!"".equals(currentPassword) && !"".equals(currentUsername)){
            if(isUsernameExist(currentUsername,currentPassword)){
                
                setCurrentUserInformations(currentUsername,currentPassword);
                
                AddToLoadGamesComboBox(currentUsername);
                
                this.jTabbedPane1.setSelectedIndex(2);
            }else
                JOptionPane.showMessageDialog(this,"Utilisateur non trouvé !", "Warning", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void AddToLoadGamesComboBox(String currentUsername){
        GamesToLoadComboBox1.removeAllItems();
        getGamesFromeFile();
        System.out.println("games Size = "+games.size());
        for (int i = 0; i < games.size(); i++) {
            System.out.println("curentUsername = "+games.get(i).get(0)+" == "+curentUsername);
            System.out.println("Add = "+games.get(i).get(1));
            if(games.get(i).get(0).equals(curentUsername))
                this.GamesToLoadComboBox1.addItem(""+games.get(i).get(1));
        }
    }
    
    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        // TODO add your handling code here:
        if(this.jTabbedPane1.getSelectedIndex() == 2){
            
            if( !"".equals(this.curentUsername) ){
                user1_NameTextField.setText(curentUsername);
            }else{
                this.jTabbedPane1.setSelectedIndex(1);
                JOptionPane.showMessageDialog(this,"Avant de jouer, il faut se connecter !!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
            
        }
        
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void SinscrireButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SinscrireButtonActionPerformed
        // TODO add your handling code here:
        Sinscrire();        
    }//GEN-LAST:event_SinscrireButtonActionPerformed
    
    public void Sinscrire(){
        String signUpUsername = this.signUpUsernameJTextField.getText().toString();
        String signUpPassword = this.signUpPasswordJPasswordField.getText().toString();
        
        if(!"".equals(signUpUsername) && !"".equals(signUpPassword)){
            WriteToListUsersFile(signUpUsername, signUpPassword);
        }
    }
    
    private void ApplyGamejButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ApplyGamejButtonActionPerformed
        // TODO add your handling code here:
        ApplyLoadedGame();
        this.repaint();
    }//GEN-LAST:event_ApplyGamejButtonActionPerformed
    
    public void ApplyLoadedGame(){
        try {
            int idGame= Integer.valueOf( GamesToLoadComboBox1.getSelectedItem().toString() );
            CAN_PLAY_GAME=false;
            ReadFromUserFile(idGame);            
        } catch (Exception e) {
        }

    }
    
    private void SaveGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveGameButtonActionPerformed
        // TODO add your handling code here:
        Vector temp = new Vector();
        temp.add(curentUsername);
        temp.add(TicTacPrincipalePanel.ID_SAVE_GAME);
        games.add(temp);
               
        WriteToUserFile(curentUsername);
        Vector temp2 = new Vector();
        if( !( SecondPlayerjComboBox.getSelectedItem().toString().equals("Machine") ) ){
            temp2.add(SecondPlayerjComboBox.getSelectedItem().toString());
            temp2.add(TicTacPrincipalePanel.ID_SAVE_GAME);
            games.add(temp2);
            WriteToUserFile((String) SecondPlayerjComboBox.getSelectedItem());
        }
        AddGamesToFile();
        getGamesFromeFile();   
        AddToLoadGamesComboBox(curentUsername);
        
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {                
                if(pan.getPanelAt(i).getButtonAt(j).getPlayer() == 1)
                    pan.getPanelAt(i).getButtonAt(j).setText("X");
                else if(pan.getPanelAt(i).getButtonAt(j).getPlayer() == 2)
                    pan.getPanelAt(i).getButtonAt(j).setText("O");
            }
        }
    }//GEN-LAST:event_SaveGameButtonActionPerformed

    private void ResumeGamejButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResumeGamejButtonActionPerformed
        // TODO add your handling code here:
        CAN_PLAY_GAME=true;
//        ResumeTheGame();
    }//GEN-LAST:event_ResumeGamejButtonActionPerformed

    private void Machine_NiveaujComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Machine_NiveaujComboBoxActionPerformed
        // TODO add your handling code here:
        switch (Machine_NiveaujComboBox.getSelectedIndex()) {
            case 0:
                MACHINE_NIVEAU=1;
                this.timerInputValue.setValue(3);
                break;
            case 1:
                MACHINE_NIVEAU=3;
                this.timerInputValue.setValue(10);
                break;
            case 2:
                MACHINE_NIVEAU=6;
                this.timerInputValue.setValue(20);
               break;
        }
        this.repaint();
    }//GEN-LAST:event_Machine_NiveaujComboBoxActionPerformed

    private void GamesToLoadComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GamesToLoadComboBox1ActionPerformed
        // TODO add your handling code here:
//        ApplyLoadedGame();
    }//GEN-LAST:event_GamesToLoadComboBox1ActionPerformed

    private void regleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regleButton1ActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            String path = "C:\\Users\\Asus\\Documents\\NetBeansProjects\\TicTacToe\\";
            Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler "+path+"Ultimate TIC TAC TOE Rapport - CHOAUKI Mouad");
        } catch (IOException ex) {
            Logger.getLogger(TicTacFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_regleButton1ActionPerformed
    
    public void CreateListUsersFile(){
        try {
            File myFile = new File(LIST_USERS_FILENAME);
            if (myFile.createNewFile()) {
              System.out.println("File created: " + myFile.getName());
            } else {
              System.out.println("File already exists.");
            }
        } catch (IOException e) {
          System.out.println("An error occurred.");
          e.printStackTrace();
        }
    }
    
    public Vector<String> ReadFromListUsersFile(){
        Vector<String> listUsers = new Vector<>();
        
        try {
            File myObj = new File(LIST_USERS_FILENAME);
            Scanner myReader = new Scanner(myObj);
            
            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
              
              if(data.charAt(0) == 'U')
                listUsers.add(data.substring(1));
            }
            
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            CreateListUsersFile();
//            e.printStackTrace();
        }
        
        return listUsers;
    }
    
    public void WriteToListUsersFile(String username,String password){
        
        try {
            File myObj = new File(LIST_USERS_FILENAME);
            if (!myObj.exists()) {
                CreateListUsersFile();
                WriteToListUsersFile(username, password);
            } else {
                FileWriter myWriter = new FileWriter(LIST_USERS_FILENAME,true);
                
//                myWriter.
                
                myWriter.write("U"+username+"\n");
                myWriter.write("P"+password+"\n");
                myWriter.close();
                
                JOptionPane.showMessageDialog(this, "Nouveau utilisateur est ajouté !", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            JOptionPane.showMessageDialog(this, "Utilisateur déja existé !", "Error", JOptionPane.ERROR);
          e.printStackTrace();
        }
    }
    
    public boolean isUsernameExist(String currentUsername,String currentPassword){
        for (String user : ReadFromListUsersFile()) {
            if(user.equals(currentUsername))
                return true;
        }
        return false;
    }
    
    public void AddComboBoxSecondPlayer(String currentUsername){
        SecondPlayerjComboBox.removeAllItems();
        for (String user : ReadFromListUsersFile()) {
            if(!user.equals(currentUsername))
                SecondPlayerjComboBox.addItem(user);
        }
    }
    
        
    public void addToGameVector(){
        Vector game=new Vector<>();
        game.add(pan.addToPreviousMove());
        game.add(user1.isPlay);
        game.add(user2.isPlay);
        gameUndoVector.add(game);
    }
        
    public void getPreviousMove(){
        int size=gameUndoVector.size();
        if(size>0){
//            this.pan.getAlgorithme().initiateBoard();
            loadMoveAt(size-1);
            removeLastLineFromHistoriqueTable();
            gameUndoVector.remove(size-1);
            
//            this.pan.getAlgorithme().getPreviousMove();
        }
    }
    private void loadMoveAt(int k){
        Vector gameCourant=new Vector();
        
        //Récupuration de du game num k
        gameCourant=gameUndoVector.get(k);

        //Récupuration de toutes les panels !!
        //Couche Frame
        Vector gridsVect= (Vector) gameCourant.get(0);           
        user1.isPlay=(boolean) gameCourant.get(1);
        user2.isPlay=(boolean) gameCourant.get(2);
        
        //CouchePrincipalPanel
        Vector panelsVector=(Vector) gridsVect.get(0);
        int winnerPrincipal=(int) gridsVect.get(2);
//        boolean availablePrincipalPanel=(boolean) gridsVect.get(3);
        
        //Modification des données actuel du Panel
        this.pan.setPrincipalePanel(winnerPrincipal);
//        this.pan.setPrincipalePanel(winnerPrincipal,availablePrincipalPanel);
        
        //Récupuration des données de chaque panel
        for (int i = 0; i < 9; i++) {
            Vector panel = (Vector) panelsVector.elementAt(i);     
            
            //CouchePanel
            Vector buttonsVector = (Vector) panel.get(0);
            int winnerPanel = (int) panel.get(1);
            boolean availabePanel = (boolean) panel.get(2);
            
            //Modification des données actuel du Panel
            this.pan.getPanelAt(i+1).setPanel(winnerPanel,availabePanel);
            
            //Récupuration des données de chaque button
             for (int j = 0; j < 9; j++) {
                 Vector button =(Vector) buttonsVector.elementAt(j);
                 
                 //Couche Button
                 boolean actif = (boolean) button.get(0);
                 int player = (int) button.get(1);
                 boolean enabled = (boolean) button.get(2);
                 Color defaultColor = (Color) button.get(3);
                 
                 //Modification des données actuel de button
                 this.pan.getPanelAt(i+1).getButtonAt(j+1).setButton(enabled,actif,player,defaultColor);
             }
         }
    }
        
    private void removeLastLineFromHistoriqueTable() {
        try {
            DefaultTableModel model = (DefaultTableModel) jTablle_historique.getModel();
            if( 
                (model.getValueAt(model.getRowCount()-1, 0)) == (model.getValueAt(model.getRowCount()-2, 0))
                && (model.getValueAt(model.getRowCount()-1, 1)) == (model.getValueAt(model.getRowCount()-2, 1))
                ){
                
                model.removeRow(model.getRowCount()-1);
                
                if( 
                (model.getValueAt(model.getRowCount()-1, 0)) == (model.getValueAt(model.getRowCount()-2, 0))
                && (model.getValueAt(model.getRowCount()-1, 1)) == (model.getValueAt(model.getRowCount()-2, 1))
                )
                    model.removeRow(model.getRowCount()-1);
            }
            model.removeRow(model.getRowCount()-1);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        
    }
    
    private void removeAllFromHistoriqueTable() {
        DefaultTableModel model = (DefaultTableModel) jTablle_historique.getModel();
        for (int i = 0; i < jTablle_historique.getRowCount(); i++) {
            model.removeRow(i);
        }
    }
    
    public int getUser1GamingTime(){
        int timeValue=0;
        int size=( (DefaultTableModel) jTablle_historique.getModel()).getRowCount();
        if( size>0 ){
            for (int i = 0; i < size; i++) {
                String username=( (String) ( (DefaultTableModel) jTablle_historique.getModel()).getValueAt(i, 1));
                if(username.equals(user1.getUsername()))
                    timeValue +=( (Integer) ( (DefaultTableModel) jTablle_historique.getModel()).getValueAt(i, 3)).intValue();
            }
        }
        return timeValue;
    }
    public int getUser2GamingTime(){
        int timeValue=0;
        int size=( (DefaultTableModel) jTablle_historique.getModel()).getRowCount();
        if( size>0 ){
            for (int i = 0; i < size; i++) {
                String username=( (String) ( (DefaultTableModel) jTablle_historique.getModel()).getValueAt(i, 1));
                if(username.equals(user2.getUsername()) && ( (Integer) ( (DefaultTableModel) jTablle_historique.getModel()).getValueAt(i, 3)).intValue()< GAME_DURATION)
                    timeValue +=( (Integer) ( (DefaultTableModel) jTablle_historique.getModel()).getValueAt(i, 3)).intValue();
            }
        }
        return timeValue;
    }
    
    
    public void CreateGamesFile(){
        try {
            File myFile = new File(USERS_DIRECTORY+GAMES_FILE);
            myFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(TicTacFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void AddGamesToFile(){
        try{
          FileOutputStream sortie = new FileOutputStream(USERS_DIRECTORY+GAMES_FILE);
          ObjectOutputStream s = new ObjectOutputStream(sortie);
          s.writeObject(games);
          s.close();
          } catch (FileNotFoundException ex) {
              CreateGamesFile();
//              WriteToUserFile();
          } catch (IOException ex) {
          }
    }
    public void getGamesFromeFile(){
        Vector games = new Vector( );
        try{
            FileInputStream entree = new FileInputStream(USERS_DIRECTORY+GAMES_FILE);
            ObjectInputStream e = new ObjectInputStream(entree);

            games=(Vector) e.readObject();

            e.close();
          } catch (FileNotFoundException ex) {
              CreateGamesFile();
          } catch (IOException ex) {
          } catch (ClassNotFoundException ex) {
            Logger.getLogger(TicTacFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.games.removeAllElements();
        this.games.addAll(games);
    }
    public void WriteToUserFile(String curentUsername){
         try{
          FileOutputStream sortie = new FileOutputStream(new File(USERS_DIRECTORY+curentUsername+TicTacPrincipalePanel.ID_SAVE_GAME+".txt"));
          ObjectOutputStream s = new ObjectOutputStream(sortie);
          s.writeObject(getTheGame());
          s.close();
          } catch (FileNotFoundException ex) {
//              CreateUserFile(this.curentUsername);
//              WriteToUserFile();
          } catch (IOException ex) {
          }
    }
    public void ReadFromUserFile(int idGame){
        try {
            FileInputStream entree = new FileInputStream(USERS_DIRECTORY+this.curentUsername+idGame+".txt");
            System.out.println("ID = "+games.get(0));
            ObjectInputStream e = new ObjectInputStream(entree);

            Vector game=(Vector) e.readObject();
            setTheGame(game);
            
            e.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TicTacFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TicTacFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TicTacFrame.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    public void setCurrentUserInformations(String currentUsername,String currentPassword){
        this.curentUsername=currentUsername;
        this.currentPassword=currentPassword;
        AddComboBoxSecondPlayer(currentUsername);
    }
    
    
    public Vector getTheGame(){
        Vector game=new Vector<>();

        game.add(pan.addToPreviousMove());

        game.add(Users.User1.isPlay);
        game.add(Users.User2.isPlay);
//        game.add(Users.User1.games);
        
        game.add(gameModesComboBox.getSelectedIndex());
        game.add(playersComboBox.getSelectedIndex());
        game.add(GAME_DURATION);
        
        if(playersComboBox.getSelectedIndex() == 0)
//            game.add(user2_NameTextField.getText().toString());
            game.add( SecondPlayerjComboBox.getSelectedItem() );
        else
            game.add("Machine");
               
        return game;
    }
    
    
    public void setTheGame(Vector gameCourant){
        //Récupuration de toutes les panels !!
        //Couche Frame
        Vector gridsVect= (Vector) gameCourant.get(0);           
        user1.isPlay=(boolean) gameCourant.get(1);
        user2.isPlay=(boolean) gameCourant.get(2);
        
        //Ajouter l'ensemble des parties d'unn joueur
//        User1.games.clear();
//        User1.games.addAll((Vector) gameCourant.get(3) );
        
        //Re-initialisation des paramètres
        gameModesComboBox.setSelectedIndex((int) gameCourant.get(3));
        playersComboBox.setSelectedIndex((int) gameCourant.get(4));
        GAME_DURATION= (int) gameCourant.get(5);
        
        user1_NameTextField.setText(this.curentUsername);
        user1.setUsername(curentUsername);
        SecondPlayerjComboBox.setSelectedItem((String) gameCourant.get(6));
        user2.setUsername((String) gameCourant.get(6));
        
        ApplyGameChanges();
        
        //CouchePrincipalPanel
        Vector panelsVector=(Vector) gridsVect.get(0);
        int idGame=(int) gridsVect.get(1);
        int winnerPrincipal=(int) gridsVect.get(2);
//        boolean availablePrincipalPanel=(boolean) gridsVect.get(3);
        
        //Modification des données actuel du Panel
//        this.pan.setPrincipalePanel(idGame,winnerPrincipal,availablePrincipalPanel);
        this.pan.setPrincipalePanel(idGame,winnerPrincipal);
        
        //Récupuration des données de chaque panel
        for (int i = 0; i < 9; i++) {
            Vector panel = (Vector) panelsVector.elementAt(i);     
            
            //CouchePanel
            Vector buttonsVector = (Vector) panel.get(0);
            int winnerPanel = (int) panel.get(1);
            boolean availabePanel = (boolean) panel.get(2);
            
            //Modification des données actuel du Panel
            this.pan.getPanelAt(i+1).setPanel(winnerPanel,availabePanel);
            
            //Récupuration des données de chaque button
             for (int j = 0; j < 9; j++) {
                 Vector button =(Vector) buttonsVector.elementAt(j);
                 
                 //Couche Button
                 boolean actif = (boolean) button.get(0);
                 int player = (int) button.get(1);
                 boolean enabled = (boolean) button.get(2);
                 Color defaultColor = (Color) button.get(3);
                 
                 //Modification des données actuel de button
                 this.pan.getPanelAt(i+1).getButtonAt(j+1).setButton(enabled,actif,player,defaultColor);
             }
         }
    }
    
    public void ApplyGameChanges(){
            this.timerInputValue.setValue(GAME_DURATION);
            moveTimeProgressBar.setMaximum(GAME_DURATION);
            
            removeAllFromHistoriqueTable();
            RestartTimer();
    }
    
    
     public TicTacPrincipalePanel getPan() {
        return pan;
    }
    public void setPan(TicTacPrincipalePanel pan) {
        this.pan = pan;
    }

    public User1 getUser1() {
        return user1;
    }
    public void setUser1(User1 user1) {
        this.user1 = user1;
    }

    public User2 getUser2() {
        return user2;
    }
    public void setUser2(User2 user2) {
        this.user2 = user2;
    }

    public JPanel getPanel() {
        return Panel;
    }

    public void setPanel(JPanel Panel) {
        this.Panel = Panel;
    }
    
    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(TicTacFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(TicTacFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(TicTacFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(TicTacFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new TicTacFrame().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ApplyGamejButton;
    private javax.swing.JComboBox<String> GamesToLoadComboBox1;
    private javax.swing.JPanel Home_Panel;
    private javax.swing.JComboBox<String> Machine_NiveaujComboBox;
    private javax.swing.JPanel Panel;
    private javax.swing.JButton ResumeGamejButton;
    private javax.swing.JButton SaveGameButton;
    private javax.swing.JComboBox<String> SecondPlayerjComboBox;
    private javax.swing.JButton SinscrireButton;
    private javax.swing.JPanel TTT_Panel;
    private javax.swing.JPasswordField currentPasswordJPasswordField;
    private javax.swing.JTextField currentUsernameJTextField;
    private javax.swing.JPanel gameControlPanel;
    private javax.swing.JComboBox<String> gameModesComboBox;
    private javax.swing.JButton hintButton;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTablle_historique;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JProgressBar moveTimeProgressBar;
    private javax.swing.JToggleButton pauseAndPlayToggleButton;
    private javax.swing.JComboBox<String> playersComboBox;
    private javax.swing.JButton regleButton1;
    private javax.swing.JButton restartGameButton;
    private javax.swing.JButton seConnecterButton;
    private javax.swing.JPasswordField signUpPasswordJPasswordField;
    private javax.swing.JTextField signUpUsernameJTextField;
    private javax.swing.JButton startGameButton;
    private javax.swing.JSpinner timerInputValue;
    private javax.swing.JButton undoButton;
    private javax.swing.JTextField user1_NameTextField;
    // End of variables declaration//GEN-END:variables

    public JProgressBar moveTimeProgressBar(){
        return this.moveTimeProgressBar;
    }
    public int getMode(){
        return gameModesComboBox.getSelectedIndex();
    }
    public int getSecondPlayer(){
        return playersComboBox.getSelectedIndex();
    }
}
