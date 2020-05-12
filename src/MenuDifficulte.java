
import java.util.logging.Level;
import java.util.logging.Logger;


public class MenuDifficulte extends javax.swing.JFrame {
    //Troisième et dernière fenêtre du menu: contient une label avec le logo, une label qui dit "Difficulté du jeu", des labels/boutons avec les différentes difficultés du jeu
    private String nomJoueur;
    public MenuDifficulte() {
        initComponents();

    }

    public MenuDifficulte(String username){ //Surcharge du constructeur pour pouvoir obtenir les informations du TextField de la fenêtre précédente
        initComponents();
        nomJoueur = username;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelLogo = new javax.swing.JLabel();
        labelDifficulte = new javax.swing.JLabel();
        panelDifficulte = new javax.swing.JPanel();
        labelFacile = new javax.swing.JLabel();
        labelMoyen = new javax.swing.JLabel();
        labelDifficile = new javax.swing.JLabel();
        labelFond = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(400, 320));
        setResizable(false);
        getContentPane().setLayout(null);

        labelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Buttons/logoPoker2.png"))); // NOI18N
        getContentPane().add(labelLogo);
        labelLogo.setBounds(150, 0, 130, 100);

        labelDifficulte.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        labelDifficulte.setForeground(new java.awt.Color(255, 255, 255));
        labelDifficulte.setText("DIFFICULTÉ DU JEU");
        getContentPane().add(labelDifficulte);
        labelDifficulte.setBounds(130, 110, 170, 30);

        panelDifficulte.setBackground(new java.awt.Color(0, 0, 0));

        labelFacile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Buttons/Facile1.png"))); // NOI18N
        labelFacile.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labelFacile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
                    labelFacileMouseClicked(evt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                labelFacileMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                labelFacileMouseExited(evt);
            }
        });
        panelDifficulte.add(labelFacile);

        labelMoyen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Buttons/Moyen1.png"))); // NOI18N
        labelMoyen.setAlignmentY(0.0F);
        labelMoyen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labelMoyen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
                    labelMoyenMouseClicked(evt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                labelMoyenMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                labelMoyenMouseExited(evt);
            }
        });
        panelDifficulte.add(labelMoyen);

        labelDifficile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Buttons/Difficile1.png"))); // NOI18N
        labelDifficile.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labelDifficile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
                    labelDifficileMouseClicked(evt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                labelDifficileMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                labelDifficileMouseExited(evt);
            }
        });
        panelDifficulte.add(labelDifficile);

        getContentPane().add(panelDifficulte);
        panelDifficulte.setBounds(100, 160, 210, 60);

        labelFond.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Buttons/BLACKBACKGROUND.jpg"))); // NOI18N
        labelFond.setToolTipText("");
        getContentPane().add(labelFond);
        labelFond.setBounds(0, 0, 400, 300);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //pour chaque label/bouton, on change sa couleur (donc son image) quand le curseur vient sur ce bouton
    //on revient à sa couleur (donc son image) du début quand le curseur sort du bouton

    private void labelFacileMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelFacileMouseEntered
        labelFacile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/Buttons/Facile2.png")));
    }//GEN-LAST:event_labelFacileMouseEntered

    private void labelFacileMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelFacileMouseExited
        labelFacile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/Buttons/Facile1.png")));
    }//GEN-LAST:event_labelFacileMouseExited

    private void labelMoyenMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelMoyenMouseEntered
        labelMoyen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/Buttons/Moyen2.png")));
    }//GEN-LAST:event_labelMoyenMouseEntered

    private void labelMoyenMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelMoyenMouseExited
        labelMoyen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/Buttons/Moyen1.png")));
    }//GEN-LAST:event_labelMoyenMouseExited

    private void labelDifficileMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelDifficileMouseEntered
        labelDifficile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/Buttons/Difficile2.png")));
    }//GEN-LAST:event_labelDifficileMouseEntered


    //quand on appuie sur un des boutons, la fenêtre n'est plus visible et le jeu correspondant à son niveau de difficulté commence

    private void labelDifficileMouseClicked(java.awt.event.MouseEvent evt) throws Exception {//GEN-FIRST:event_labelDifficileMouseClicked
        this.dispose();
        new Jeu(nomJoueur,2);
    }//GEN-LAST:event_labelDifficileMouseClicked

    private void labelDifficileMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelDifficileMouseExited
        labelDifficile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/Buttons/Difficile1.png")));
    }//GEN-LAST:event_labelDifficileMouseExited

    private void labelFacileMouseClicked(java.awt.event.MouseEvent evt) throws Exception {//GEN-FIRST:event_labelFacileMouseClicked
        this.dispose();
        new Jeu(nomJoueur,0);
    }//GEN-LAST:event_labelFacileMouseClicked

    private void labelMoyenMouseClicked(java.awt.event.MouseEvent evt) throws Exception {//GEN-FIRST:event_labelMoyenMouseClicked
        this.dispose();
        new Jeu(nomJoueur,1);
    }//GEN-LAST:event_labelMoyenMouseClicked


    public static void main(String args[]) {


        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuDifficulte().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelDifficile;
    private javax.swing.JLabel labelDifficulte;
    private javax.swing.JLabel labelFacile;
    private javax.swing.JLabel labelFond;
    private javax.swing.JLabel labelLogo;
    private javax.swing.JLabel labelMoyen;
    private javax.swing.JPanel panelDifficulte;
    // End of variables declaration//GEN-END:variables
}
