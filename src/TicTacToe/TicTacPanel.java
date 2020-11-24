/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TicTacToe;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author MouadC
 */
public class TicTacPanel extends javax.swing.JPanel implements Serializable{

    public HashMap<Integer,TicTacButton> buttons=new HashMap<>();
    private TicTacFrame ticTacFrame;
    
    private int idPanel;
    private int winner;
    
    private boolean availablePanel;    

    /**
     * Creates new form TicTacPanel
     */
    public TicTacPanel(TicTacFrame ticTacFrame, int idPanel) {
        initComponents();
        this.idPanel=idPanel;
        this.winner=0;
        this.availablePanel=true;
        
        this.ticTacFrame=ticTacFrame;
        
        this.setLayout(new GridLayout(3, 3));
        for (int i = 1; i <= 9; i++) {
            TicTacButton button=new TicTacButton(this,ticTacFrame,i);
            this.buttons.put(i, button);
            this.add(button);
        }
    }
    
    public void setAvailability(){
        boolean available=false;
        for (int i = 1; i < 10; i++) {
            if(buttons.get(i).isActif()){
                available=true;
                break;
            }
        }
        this.availablePanel=available;
    }
    
    public boolean isSamePlayer(int i,int j){
        return buttons.get(i).getPlayer() == buttons.get(j).getPlayer() && buttons.get(i).getPlayer() == 1
            || buttons.get(i).getPlayer() == buttons.get(j).getPlayer() && buttons.get(i).getPlayer() == 2;
    }
    
    public void setWinner(int i){
        if(availablePanel){
            if(buttons.get(i).getBackground()==Users.User1.USER1_COLOR){
                if(ticTacFrame.getMode() == 0)
                    JOptionPane.showMessageDialog(ticTacFrame, " 'X' à gagner le paneau : "+getIdPanel(), "Paneau éliminer", JOptionPane.INFORMATION_MESSAGE);
                MarkWinnerPannel(1);
                winner=1;
                ticTacFrame.addRowToTable("Blue",ticTacFrame.getUser1().getUsername(), "Gagné P"+getIdPanel(), ticTacFrame.getUser1GamingTime());
            }else{
                winner=2;
                if(ticTacFrame.getMode() == 0)
                    JOptionPane.showMessageDialog(ticTacFrame, " 'O' à gagner le paneau : "+getIdPanel(), "Paneau éliminer", JOptionPane.INFORMATION_MESSAGE);
                MarkWinnerPannel(2);
                ticTacFrame.addRowToTable("Rouge",ticTacFrame.getUser2().getUsername(), "Gagné P"+getIdPanel(), ticTacFrame.getUser2GamingTime());
            }
            
            DesactifButtons();
            DisableButtons();
            this.availablePanel=false;

            if(ticTacFrame.getMode() == 1){
                ticTacFrame.getPan().endGame();
                if(buttons.get(i).getBackground()==Users.User1.USER1_COLOR){
                    JOptionPane.showMessageDialog(ticTacFrame, " 'X' à gagner le jeu !!", "Jeu términer", JOptionPane.INFORMATION_MESSAGE);
                    ticTacFrame.addRowToTable("Blue",ticTacFrame.getUser1().getUsername(), "Gagné le jeu", ticTacFrame.getUser1GamingTime());
                }else{
                    JOptionPane.showMessageDialog(ticTacFrame, " 'O' à gagner le jeu !!", "Jeu términer", JOptionPane.INFORMATION_MESSAGE);
                    ticTacFrame.addRowToTable("Rouge",ticTacFrame.getUser2().getUsername(), "Gagné le jeu", ticTacFrame.getUser2GamingTime());
                }
            }
        }
    }
    
    public void MarkWinnerPannel(int winner){
        for (int i = 1; i < 10; i++) {
            if(buttons.get(i).getBackground() == buttons.get(i).getDefaultColor()){
                if(winner == 1)
                    buttons.get(i).setBackground(new Color(0, 0, 255,75));
                else if(winner == 2)
                    buttons.get(i).setBackground(new Color(255, 0, 0,85));
            }
        }
        repaint();
    }
    
    public void checkWinner(){
        for (int i = 1; i < 10; i++) {
            if(i==1){
                if( isSamePlayer(1,2) && isSamePlayer(1,3) ){
                    setWinner(1);
                }else if( isSamePlayer(1,4) && isSamePlayer(1,7) ){
                    setWinner(1);
                }else if( isSamePlayer(1,5) && isSamePlayer(1,9) ){
                    setWinner(1);
                }
            }else if(i==2){
                if( isSamePlayer(2,5) && isSamePlayer(2,8) ){
                    setWinner(2);
                }
            }else if(i==3){
                if( isSamePlayer(3,5) && isSamePlayer(3,7) ){
                    setWinner(3);
                }
            }else if(i==4){
                if( isSamePlayer(4,5) && isSamePlayer(4,6) ){
                    setWinner(4);
                }
            }else if(i==9){
                if( isSamePlayer(9,3) && isSamePlayer(9,6) ){
                    setWinner(9);
                }else if( isSamePlayer(9,7) && isSamePlayer(9,8) ){
                    setWinner(9);
                }
            }
        }
        if(winner!=0){
            System.out.println("Winner id :"+winner+" in Panel : "+idPanel);
        }
    }
    
    public void DesactifButtons(){
        for (int i = 1; i < 10; i++) {
            this.buttons.get(i).setActif(false);
        }
    }
    public void DisableButtons(){
        for (int i = 1; i < 10; i++) {
            this.buttons.get(i).setEnabled(false);
        }
    }
    public void EnableButtons(){
        for (int i = 1; i < 10; i++) {
            if(buttons.get(i).isActif())
                this.buttons.get(i).setEnabled(true);
        }
    }
    public void RefreshButtons(){
        this.winner=0;
        this.availablePanel=true;
        for (int i = 1; i < 10; i++) {
            this.buttons.get(i).RefreshButton();
        }
    }
    
    public Vector addToPreviousMove(){
        Vector panelVect=new Vector<>();
        Vector buttonsVect=new Vector<>();
        
        //L'ajout de toutes les bouttons
        for (int i = 0; i <9; i++) 
           buttonsVect.add(buttons.get(i+1).addToPreviousMove());
        
        //L'ajout des attributs du panel
        panelVect.add(buttonsVect);
        panelVect.add(this.winner);
        panelVect.add(this.availablePanel);

        return panelVect;
    }
    public void setPanel(int winner,boolean available){
        this.winner=winner;
        this.availablePanel=available;

        if(winner == 1)
            MarkWinnerPannel(1);
        else if(winner == 2)
            MarkWinnerPannel(2);
    }

    
    //-------------------------Getters And Setters-------------------------
//    public TicTacButton getButton(int numButton){
//        return buttons.get(numButton);
//    }
    
    public HashMap<Integer, TicTacButton> getButtons() {
        return buttons;
    }
    public boolean isAvailable() {
        return availablePanel;
    }
    
    public int getWinner() {
        return winner;
    }

    public int getIdPanel() {
        return idPanel;
    }
    
    public TicTacButton getButtonAt(int i){
        return buttons.get(i);
    }
        
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
