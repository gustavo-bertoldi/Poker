
public class MenuNomJoueur extends javax.swing.JFrame {

    //Deuxième fenêtre du menu: contient une label avec le logo, une label qui dit "Username", un TextField pour que le joueur écrive son nom, et une label/bouton de Next

    public MenuNomJoueur() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelLogo = new javax.swing.JLabel();
        labelUsername = new javax.swing.JLabel();
        fieldUsername = new javax.swing.JTextField();
        labelNext = new javax.swing.JLabel();
        labelFond = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(400, 320));
        setResizable(false);
        setSize(new java.awt.Dimension(600, 600));
        getContentPane().setLayout(null);

        labelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Buttons/logoPoker2.png"))); // NOI18N
        getContentPane().add(labelLogo);
        labelLogo.setBounds(150, 10, 120, 90);

        labelUsername.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        labelUsername.setForeground(new java.awt.Color(255, 255, 255));
        labelUsername.setText("USERNAME");
        getContentPane().add(labelUsername);
        labelUsername.setBounds(160, 130, 120, 30);

        fieldUsername.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        fieldUsername.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldUsername.setToolTipText("");
        fieldUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldUsernameActionPerformed(evt);
            }
        });
        getContentPane().add(fieldUsername);
        fieldUsername.setBounds(130, 170, 150, 30);

        labelNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Buttons/Next1.png"))); // NOI18N
        labelNext.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labelNext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelNextMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                labelNextMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                labelNextMouseExited(evt);
            }
        });
        getContentPane().add(labelNext);
        labelNext.setBounds(150, 220, 110, 50);

        labelFond.setBackground(new java.awt.Color(0, 0, 0));
        labelFond.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Buttons/BLACKBACKGROUND.jpg"))); // NOI18N
        getContentPane().add(labelFond);
        labelFond.setBounds(-10, -20, 420, 320);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fieldUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldUsernameActionPerformed

    private void labelNextMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelNextMouseEntered
        labelNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/Buttons/Next2.png")));    // quand on vient sur le bouton Next avec le curseur, on change le couleur du bouton (c'est une nouvelle image)
    }//GEN-LAST:event_labelNextMouseEntered

    private void labelNextMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelNextMouseExited
        labelNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/Buttons/Next1.png")));         // quand on enlève le curseur du bouton, on revient à la première image du bouton
    }//GEN-LAST:event_labelNextMouseExited

    private void labelNextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelNextMouseClicked
        //quand on appuie le bouton, cette fenêtre se ferme, et on passe à la suivante fenêtre du menu (ligne suivante), qui admet comme paramètre le nom entré par le joueur sur le TextField
        this.setVisible(false);
        String username = fieldUsername.getText();
        MenuDifficulte f = new MenuDifficulte(username);
        f.setVisible(true);

    }//GEN-LAST:event_labelNextMouseClicked


    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuDifficulte().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField fieldUsername;
    private javax.swing.JLabel labelFond;
    private javax.swing.JLabel labelLogo;
    private javax.swing.JLabel labelNext;
    private javax.swing.JLabel labelUsername;
    // End of variables declaration//GEN-END:variables
}