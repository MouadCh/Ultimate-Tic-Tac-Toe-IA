/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TicTacToe;

import Algorithms.IA_Algorithme;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author MouadC
 */
public class TicTacPrincipalePanel extends javax.swing.JPanel implements Serializable{
    
    private TicTacFrame ticTacFrame;
    private HashMap<Integer,TicTacPanel> grids=new HashMap<>();
    private IA_Algorithme algorithme;
    
    public static int ID_SAVE_GAME=new Random().nextInt();
//    public static int currentWorkingPanel=0;
    
    private int winnerPrincipal;
    
    
    /**
     * Creates new form PrincipalePanel
     */
    public TicTacPrincipalePanel(TicTacFrame ticTacFrame,int width,int height) {
        initComponents();
        this.ticTacFrame=ticTacFrame;
        this.setBounds(3,3,width, height);
        
        this.algorithme = new IA_Algorithme();
        initPanel();
    }
    
    public void initPanel(){
        for (int i = 0; i <= 9; i++) {
            grids.put(i, new TicTacPanel(ticTacFrame,i));
            grids.get(i).setBounds(3,2,this.getWidth()/4+18,this.getHeight()/4+12);
        }
        AddPanels(grids);
    }
    
    public void AddPanels(HashMap<Integer,TicTacPanel> gridsMap){
        this.jPanel1.add(gridsMap.get(1));
        this.jPanel2.add(gridsMap.get(2));
        this.jPanel3.add(gridsMap.get(3));
        this.jPanel4.add(gridsMap.get(4));
        this.jPanel5.add(gridsMap.get(5));
        this.jPanel6.add(gridsMap.get(6));
        this.jPanel7.add(gridsMap.get(7));
        this.jPanel8.add(gridsMap.get(8));
        this.jPanel9.add(gridsMap.get(9));
    }
    
    public void setGridsAvailability(){
        for (int i = 1; i < 10; i++) {
            ((TicTacPanel) this.grids.get(i)).setAvailability();
        }
    }
    
    public void checkGridsWinner(){
        for (int i = 1; i < 10; i++) {
            ((TicTacPanel) this.grids.get(i)).checkWinner();
        }
        checkWinner();
    }
    
    public boolean isSameWinner(int i,int j){
        return ( ( TicTacPanel )grids.get(i)).getWinner() == ( ( TicTacPanel )grids.get(j)).getWinner() 
                &&  (( TicTacPanel )grids.get(i)).getWinner()!=0 ;
    }

    public void setWinner(int winner) {
        if((( TicTacPanel )grids.get(winner)).getWinner()==1){
            JOptionPane.showMessageDialog(ticTacFrame, "X à gagner le jeu !!", "Partie términer", JOptionPane.INFORMATION_MESSAGE);
            this.winnerPrincipal=1;
            ticTacFrame.addRowToTable("Blue",ticTacFrame.getUser1().getUsername(), "Gagné le jeu", ticTacFrame.getUser1GamingTime());
        }else{
            JOptionPane.showMessageDialog(ticTacFrame, "O à gagner le jeu !!", "Partie términer", JOptionPane.INFORMATION_MESSAGE);
            this.winnerPrincipal=2;
            ticTacFrame.addRowToTable("Rouge",ticTacFrame.getUser1().getUsername(), "Gagné le jeu", ticTacFrame.getUser2GamingTime());
        }
        endGame();
    }
    
    public void endGame(){
        DesableAllPanels();
        DesactifAllPanels();
        TicTacFrame.CAN_PLAY_GAME=false;
    }
    
    public void checkWinner(){
        for (int i = 1; i < 10; i++) {
            if(i==1){
                if( isSameWinner(1,2) && isSameWinner(1,3) ){
                    setWinner(1);
                }else if( isSameWinner(1,4) && isSameWinner(1,7) ){
                    setWinner(1);
                }else if( isSameWinner(1,5) && isSameWinner(1,9) ){
                    setWinner(1);
                }
            }else if(i==2){
                if( isSameWinner(2,5) && isSameWinner(2,8) ){
                    setWinner(2);
                }
            }else if(i==3){
                if( isSameWinner(3,5) && isSameWinner(3,7) ){
                    setWinner(3);
                }
            }else if(i==4){
                if( isSameWinner(4,5) && isSameWinner(4,6) ){
                    setWinner(4);
                }
            }else if(i==9){
                if( isSameWinner(9,3) && isSameWinner(9,6) ){
                    setWinner(9);
                }else if( isSameWinner(9,7) && isSameWinner(9,8) ){
                    setWinner(9);
                }
            }
        }
        if(winnerPrincipal!=0){
            System.out.println("Winner id :"+winnerPrincipal);
        }
    }   
    
    public void EnablePanel(int numPanel){
        setGridsAvailability();
        checkGridsWinner();
        EnableAllPanels();
        if(((TicTacPanel)this.grids.get(numPanel)).isAvailable()){
            if(numPanel>=1 && numPanel<=9){ 
                for (int i = 0; i < 10; i++) {
                    if(numPanel != i)
                        ((TicTacPanel) this.grids.get(i)).DisableButtons();
                }
            }
        }
    }
    
    public void EnableAllPanels(){
        for (int i = 1; i < 10; i++) {
            ((TicTacPanel) this.grids.get(i)).EnableButtons();                        
        }
    }
    
    public void DesableAllPanels(){
        for (int i = 1; i < 10; i++) {
            ((TicTacPanel) this.grids.get(i)).DisableButtons();                        
        }
    }
    
    public void RefreshAllPanels(){
        for (int i = 1; i < 10; i++) {
            ((TicTacPanel) this.grids.get(i)).RefreshButtons();                        
        }
    }
    
    public void DesactifAllPanels(){
        for (int i = 1; i < 10; i++) {
            ( (TicTacPanel )this.grids.get(i)).DesactifButtons();
        }
    }
    
    public Vector addToPreviousMove(){
        Vector gridsVect=new Vector<>();
        Vector panelsVect=new Vector<>();
        
        for (int i = 1; i <10; i++) {
           panelsVect.add(this.grids.get(i).addToPreviousMove());
        }
        
        gridsVect.add(panelsVect);
        gridsVect.add(this.ID_SAVE_GAME);
        gridsVect.add(this.winnerPrincipal);

        return gridsVect;
    }
    
    public void setPrincipalePanel(int winnerPrincipal){
        this.winnerPrincipal=winnerPrincipal;
    }
    
    public void setPrincipalePanel(int idGame ,int winnerPrincipal){
        ID_SAVE_GAME=idGame;
        this.winnerPrincipal=winnerPrincipal;
    }
    
    public void PlayMachineMove(){
        ticTacFrame.getPan().getAlgorithme().computerMove();
        this.grids.get(IA_Algorithme.idPan+1).getButtons().get(IA_Algorithme.idButt+1).PlayMove();
//        IA_Algorithme.LastidButt=IA_Algorithme.idButt;
//        IA_Algorithme.LastidPan=IA_Algorithme.idPan;
    }    
            
    
    public TicTacPanel getPanelAt(int i){
        return grids.get(i);
    }

    public IA_Algorithme getAlgorithme() {
        return algorithme;
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Commencer à jouer", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 2, 18), new java.awt.Color(8, 49, 58))); // NOI18N
        setForeground(new java.awt.Color(102, 102, 102));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(153, 204, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 138, Short.MAX_VALUE)
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setForeground(new java.awt.Color(153, 204, 255));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 138, Short.MAX_VALUE)
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setForeground(new java.awt.Color(153, 204, 255));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 149, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 137, Short.MAX_VALUE)
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setForeground(new java.awt.Color(153, 204, 255));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 137, Short.MAX_VALUE)
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setForeground(new java.awt.Color(153, 204, 255));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 149, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 138, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setForeground(new java.awt.Color(153, 204, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 138, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setForeground(new java.awt.Color(153, 204, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 149, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 138, Short.MAX_VALUE)
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setForeground(new java.awt.Color(153, 204, 255));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 138, Short.MAX_VALUE)
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setForeground(new java.awt.Color(153, 204, 255));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 137, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(15, 15, 15)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(15, 15, 15)
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(15, 15, 15)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(15, 15, 15)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(15, 15, 15)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(15, 15, 15)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    // End of variables declaration//GEN-END:variables

    
//    public void PlayBestMove(){
//        showHint();  
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                hideHint();
//            }
//        },1500);
//    }
//    public void showHint(){
////        this.buttons.get(bestMove.position).setBackground(new Color(0, 250, 0));
//    }
//    public void hideHint(){
////        for (int i = 1; i < 10; i++) {
////            if(buttons.get(i).isActif() == true)
////                buttons.get(i).setBackground(buttons.get(i).getDefaultColor());
////        }
//    }
    
}
