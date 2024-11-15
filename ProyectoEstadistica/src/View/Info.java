package View;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Info extends JFrame {

    private List<ImageIcon> images = new ArrayList<>();
    private List<String> links = new ArrayList<>();
    private int currentIndex = 0;
    private Timer timer;
    private List<String> texts = new ArrayList<>();

    public Info() {
        // Configurar ventana principal
        setTitle("Guia paso a paso");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo la ventana Info
        this.setLocationRelativeTo(null);  // Center the window
        this.setResizable(false);  // Disable resizing

        // Llama a initComponents() para configurar el diseño inicial del panel y componentes
        initComponents();

        // Inicializar lógica del carrusel
        initCarouselLogic();
    }

    private void initCarouselLogic() {
        // Cargar imágenes y enlaces
        loadImagesAndLinks();

        // Mostrar primera imagen si está disponible
        if (!images.isEmpty()) {
            updateImage();
        } else {
            JOptionPane.showMessageDialog(null, "No se cargaron imágenes.");
        }

        // Configurar el Timer para cambiar imágenes automáticamente cada 3 segundos
        timer = new Timer(5000, e -> nextImage());
        timer.start();

        // Configurar eventos para botones
        btnAnterior.addActionListener(e -> previousImage());
        btnSiguiente.addActionListener(e -> nextImage());
        this.btnCerrar.addActionListener(e -> dispose());

        // Añadir MouseListener al JLabel para abrir enlaces al hacer clic
        this.imagesLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openLink(links.get(currentIndex));
            }
        });
    }

    private void loadImagesAndLinks() {
        addImageAndLink("/Images/ME,X,MO.png", "https://www.youtube.com/watch?v=5bZXpfxwHqk", "Video de Media, Mediana y Moda");
        addImageAndLink("/Images/Tabla de frecuencias.png", "https://www.youtube.com/watch?v=Nm7log51vFA", "Video de Tabla de frecuencias");
        addImageAndLink("/Images/Tabla de frecuencias2.png", "https://www.youtube.com/watch?v=BitnpiBPGc4", "Video de Tabla de frecuencias 2");
    }

    private void addImageAndLink(String imagePath, String link, String text) {
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        if (icon.getIconWidth() == -1) {
            System.err.println("No se pudo cargar la imagen: " + imagePath);
        } else {
            images.add(icon);
            links.add(link);
            texts.add(text); // Añade el texto asociado a la imagen
        }
    }

    private void updateImage() {
        this.imagesLabel.setIcon(images.get(currentIndex));  // Actualizar la imagen en el JLabel
        this.lblInfo.setText(texts.get(currentIndex));       // Actualizar el texto en el JLabel lblInfo
    }

    private void nextImage() {
        currentIndex = (currentIndex + 1) % images.size();
        updateImage();
    }

    private void previousImage() {
        currentIndex = (currentIndex - 1 + images.size()) % images.size();
        updateImage();
    }

    private void openLink(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo abrir el enlace: " + url);
        }
    }

private void openPDF() {
    try {
        // Lee el PDF desde el archivo JAR como un InputStream
        InputStream pdfStream = getClass().getResourceAsStream("/Images/Manual de usuario.pdf");

        if (pdfStream == null) {
            JOptionPane.showMessageDialog(null, "No se encontró el archivo PDF.");
            return;
        }

        // Crear un archivo temporal para guardar el PDF
        File tempPdfFile = File.createTempFile("Manual_de_usuario", ".pdf");
        tempPdfFile.deleteOnExit(); // Asegúrate de que el archivo se elimine al salir

        // Copiar el contenido del InputStream al archivo temporal
        try (FileOutputStream outStream = new FileOutputStream(tempPdfFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = pdfStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        }

        // Abre el archivo PDF temporal
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(tempPdfFile);
        } else {
            JOptionPane.showMessageDialog(null, "La apertura de archivos no es compatible en este sistema.");
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "No se pudo abrir el archivo PDF.");
    }
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
        imagesLabel = new javax.swing.JLabel();
        btnCerrar = new javax.swing.JButton();
        btnAnterior = new javax.swing.JButton();
        btnSiguiente = new javax.swing.JButton();
        lblInfo = new javax.swing.JLabel();
        btnManual = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(183, 207, 207));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        imagesLabel.setBackground(new java.awt.Color(153, 153, 153));
        imagesLabel.setForeground(new java.awt.Color(255, 0, 102));
        jPanel1.add(imagesLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 516, 218));

        btnCerrar.setBackground(new java.awt.Color(255, 0, 0));
        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });
        jPanel1.add(btnCerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, -1, -1));

        btnAnterior.setText("<");
        jPanel1.add(btnAnterior, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, -1, -1));

        btnSiguiente.setText(">");
        jPanel1.add(btnSiguiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 240, -1, -1));

        lblInfo.setBackground(new java.awt.Color(255, 255, 255));
        lblInfo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel1.add(lblInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 240, 190, 20));

        btnManual.setBackground(new java.awt.Color(0, 51, 255));
        btnManual.setForeground(new java.awt.Color(255, 255, 255));
        btnManual.setText("Manual de usuario");
        btnManual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManualActionPerformed(evt);
            }
        });
        jPanel1.add(btnManual, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 380, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void btnManualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManualActionPerformed
        openPDF();
    }//GEN-LAST:event_btnManualActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnManual;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JLabel imagesLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblInfo;
    // End of variables declaration//GEN-END:variables
}
