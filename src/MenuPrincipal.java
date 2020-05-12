
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MenuPrincipal extends JFrame {

    public MenuPrincipal() { //Fenetre d'accueil du menu: possède un label/bouton de Play et un label avec le logo
        initComponents();
    }

    private void initComponents() {

        labelPlay = new JLabel();
        labelLogo = new JLabel();
        labelFond = new JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(34, 33, 32));
        setName("Menu Poker");
        setResizable(false);

        labelPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("res/Buttons/Play1.png"))); // NOI18N
        labelPlay.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labelPlay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                labelPlayMouseClicked(evt);
            }
            public void mouseEntered(MouseEvent evt) {
                labelPlayMouseEntered(evt);
            }
            public void mouseExited(MouseEvent evt) {
                labelPlayMouseExited(evt);
            }
        });

        labelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("res/Buttons/Logo.png"))); // NOI18N

        labelFond.setIcon(new javax.swing.ImageIcon(getClass().getResource("res/Buttons/BLACKBACKGROUND.jpg"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(labelLogo))
            .addGroup(layout.createSequentialGroup()
                .addGap(175, 175, 175)
                .addComponent(labelPlay, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(labelFond, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(140, 140, 140)
                .addComponent(labelLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(labelPlay, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(labelFond, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void labelPlayMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelPlayMouseEntered
       labelPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("res/Buttons/Play2.png")));  // quand on vient sur le bouton Play avec le curseur, on change le couleur du bouton (c'est une nouvelle image)
    }//GEN-LAST:event_labelPlayMouseEntered

    private void labelPlayMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelPlayMouseExited
     labelPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("res/Buttons/Play1.png")));    // quand on enlève le curseur du bouton, on revient à la première image du bouton
    }//GEN-LAST:event_labelPlayMouseExited

    private void labelPlayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelPlayMouseClicked
        // this.setVisible(false);  //quand on appuie le bouton, cette fenêtre se ferme, et on passe à la suivante fenêtre du menu (ligne suivante)
      MenuNomJoueur f = new MenuNomJoueur();
      this.dispose();
      f.setVisible(true);
    }//GEN-LAST:event_labelPlayMouseClicked

   
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelFond;
    private javax.swing.JLabel labelLogo;
    private javax.swing.JLabel labelPlay;
    // End of variables declaration//GEN-END:variables
}
