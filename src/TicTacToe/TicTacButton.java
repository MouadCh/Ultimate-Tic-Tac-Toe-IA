/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TicTacToe;

import Algorithms.IA_Algorithme;
import Users.User1;
import Users.User2;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.MetalBorders;

/**
 *
 * @author MouadC
 */
public class TicTacButton extends JButton implements ActionListener, MouseListener{
    private TicTacFrame ticTacFrame;
    private TicTacPanel ticTacPanel;
    
    private int idButton ;
    private int player ;
    
    private boolean actif;
    
    private Color defaultColor;
    private Color playerColor;
    
    
    public TicTacButton(TicTacPanel ticTacPanel,TicTacFrame ticTacFrame,int id) {
        this.ticTacPanel=ticTacPanel;
        this.ticTacFrame=ticTacFrame;
        this.idButton=id;
        
        this.setBorder(new MetalBorders.Flush3DBorder());
        this.setFont(new FontUIResource(new Font(Font.DIALOG , Font.ROMAN_BASELINE , 30) )); 
        
        this.defaultColor=this.getBackground();
        this.playerColor=this.getBackground();

        this.actif=true;
        this.idButton=id;
        this.player=-1;
        
        this.addMouseListener(this);
        this.addActionListener(this);
    }
    
    public void RefreshButton(){
        this.actif=true;
        this.setEnabled(true);
        this.setBackground(defaultColor);
        this.setText("");
        this.playerColor=defaultColor;
        this.player=-1;
    }
    
    
    public Vector addToPreviousMove(){
        Vector button=new Vector();
        
        button.add(this.isActif());
        button.add(this.player);
        button.add(this.isEnabled());
        button.add(defaultColor);
        
        return button;
    }
    
    public void setButton(boolean enabled,boolean actif,int player,Color defaultColor){
        this.actif=actif;
        this.player=player;
        this.defaultColor=defaultColor;
        
        if(actif)
            this.setEnabled(enabled);
        
        switch (player) {
            case 1:
                this.setText("X");
                this.setBackground(User1.USER1_COLOR);
                break;
            case 2:
                this.setText("O");
                this.setBackground(User2.USER2_COLOR);
                break;
            default:
                this.setText("");
                if(ticTacPanel.isAvailable())
                    this.setBackground(defaultColor);
                break;
        }
        
//        if(ticTacFrame.getSecondPlayer() == 2){
//            if(player == 1 ){
//                ticTacFrame.getPan().getAlgorithme().MarkBoard("X", idButton, ticTacPanel.getIdPanel());
//            }else if(player == 2 )
//                ticTacFrame.getPan().getAlgorithme().MarkBoard("O", idButton, ticTacPanel.getIdPanel());
//        }
//        ticTacFrame.getPan().getAlgorithme().printBoard();

        
//        if(ticTacPanel.isAvailable())
//            this.setBackground(playerColor);
    }
    
    public void PlayMove(){
        if(actif){
           ticTacFrame.addToGameVector();
           if(Users.User1.isPlay){
                this.setBackground(Users.User1.USER1_COLOR);  
                this.setText("X");
                this.playerColor=Users.User1.USER1_COLOR;
                this.player=1;
                
                Users.User1.isPlay=false;
                
                if(ticTacFrame.getSecondPlayer()==0)
                    Users.User2.isPlay=true;
                else{
                    ticTacFrame.getPan().getAlgorithme().setBoard_num(ticTacPanel.getIdPanel());
                    ticTacFrame.getPan().getAlgorithme().setCell_num(this.idButton);
                    ticTacFrame.getPan().getAlgorithme().userMove();
//                    IA_Algorithme.LastidButt=this.idButton;
//                    IA_Algorithme.LastidPan=ticTacPanel.getIdPanel();
                }
                
                ticTacFrame.addRowToTable("Blue",ticTacFrame.getUser1().getUsername(), "P"+ticTacPanel.getIdPanel()+"B"+this.idButton, (int) Users.User1.playTime);
           }else{
                this.setBackground(Users.User2.USER2_COLOR);  
                this.setText("O");
                this.playerColor=Users.User2.USER2_COLOR;
                this.player=2;
                
                Users.User1.isPlay=true;
                Users.User2.isPlay=false;
                
                if(ticTacFrame.getSecondPlayer()==0)
                    ticTacFrame.addRowToTable("Rouge",ticTacFrame.getUser2().getUsername(), "P"+ticTacPanel.getIdPanel()+"B"+this.idButton, (int) Users.User1.playTime);
                else
                    ticTacFrame.addRowToTable("Rouge","Machine", "P"+ticTacPanel.getIdPanel()+"B"+this.idButton, (int) Users.User1.playTime);
            }
            Users.User1.playTime=0;
            this.actif=false;
            this.setEnabled(false);
//            TicTacPrincipalePanel.currentWorkingPanel=idButton;
            ticTacFrame.getPan().EnablePanel(idButton);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(User1.isPlay || User2.isPlay)
            PlayMove();
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(this.isEnabled()){
            if(Users.User1.isPlay){
                this.setBackground(User1.USER1_COLOR);
                this.setText("X");
            }else if(Users.User2.isPlay){
                this.setBackground(User2.USER2_COLOR);
                this.setText("O");
           }   
        }
    }
    @Override
    public void mouseExited(MouseEvent e) {
        if(this.isEnabled()){
            this.setBackground(defaultColor);
            this.setText("");     
        }
    }
    
    public boolean isActif() {
        return actif;
    }
    
    public void setActif(boolean actif) {
        this.actif=actif;
    }

    public int getIdButton() {
        return idButton;
    }
    
    public int getPlayer() {
        return player;
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

}
