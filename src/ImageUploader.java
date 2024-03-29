
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;


public class ImageUploader extends javax.swing.JFrame {

    private static final int GRID_ROWS = 4;
    private static final int GRID_COLS = 3;
    
    private static final int GRID_HGAP = 15;
    private static final int GRID_VGAP = 15;
    
    private static final int IMAGEPANEL_HEIGHT = 170;
    private static final int IMAGEPANEL_WIDTH = 125;
    
    private static final int MAX_IMAGES = 12;
    private static final Dimension PRODUCT_IMAGE_DIMENSIONS = new Dimension(225,225);
    
    private static final String IMAGE_PLACEHOLDER = "images\\placeholder.jpg";
    
    public boolean created = false;
    
    JPanel [] productImages;
    String [] imageFiles;
    
    
    public ImageUploader() {
        initComponents();
        createFrame();
    }
    
    public ImageUploader(List<String> fileNames){
        initComponents();
        this.imageFiles = fileNames.toArray(new String[fileNames.size()]);
        createFrame();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void initProductImages(){
         productImages = new JPanel[MAX_IMAGES];
            
        //create image panels
        for(int i = 0; i < productImages.length; i++){
            productImages[i] = new JPanel();
            productImages[i].setPreferredSize(PRODUCT_IMAGE_DIMENSIONS);
            productImages[i].setBackground(Color.WHITE);
            productImages[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            productImages[i].setLayout(null);
            productImages[i].setBounds(0, 0, 125, 125);
        }
    }
    
    private void addImagePanels(){
        //add image panels to the grid layout
        for(int i = 0; i < productImages.length; i++){
            this.add(productImages[i]);
            if(imageFiles != null){
                ImagePanel ip = null;
                try{
                    ip = new ImagePanel(i,imageFiles[i]);
                } catch(Exception ex){
                    ip = new ImagePanel(i,IMAGE_PLACEHOLDER);
                } finally {
                    if(ip != null){
                        productImages[i].add(ip);
                        Insets insets = productImages[i].getInsets();
                        ip.setBounds(insets.left,insets.top,IMAGEPANEL_WIDTH,IMAGEPANEL_HEIGHT);
                    }
                }
            } else {
                ImagePanel ip = new ImagePanel(i,IMAGE_PLACEHOLDER);
                productImages[i].add(ip);
                Insets insets = productImages[i].getInsets();
                ip.setBounds(insets.left,insets.top,IMAGEPANEL_WIDTH,IMAGEPANEL_HEIGHT);
            }
        }
    }

    private void createFrame(){
        this.setTitle("Upload images - Drag and Drop");
        if(productImages == null){
            initProductImages();
        }
         
        this.setPreferredSize(new Dimension(430,775));
            
        //set layout of uploader window to a grid
        GridLayout gl = new GridLayout(GRID_ROWS,GRID_COLS);
        this.setLayout(gl);

        //set horizontal and vertical gaps between grid elements
        gl.setHgap(GRID_HGAP);
        gl.setVgap(GRID_VGAP);

        //set to not actually destroy the window when closed
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);

        addImagePanels();
        this.pack();
        this.setVisible(true);
        created = true;
    }
    
    public void updateImages(List<String> imageFiles){
        this.removeAll();
        this.setVisible(false);
        initComponents();
        this.imageFiles = imageFiles.toArray(new String[imageFiles.size()]);
        createFrame();
        
    }
    
    public boolean uploadImages(){
        for(int i = 0; i < imageFiles.length; i++){
            ftpTransfer(imageFiles[i], imageFiles[i]);
        }
        return true;
    }
    
    public boolean ftpTransfer(String localfile, String destinationfile)
    {
	String server = "free.inkfrog.com";
	String username = "inkfrog_Ezelwire";
	String password = "un1xsurplus";
	try
	{
		FTPClient ftp = new FTPClient();
		ftp.connect(server);
		if(!ftp.login(username, password))
		{
			ftp.logout();
			return false;
		}
		int reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply))
		{
			ftp.disconnect();
			return false;
		}
		InputStream in = new FileInputStream(localfile);
                File imageFile = new File(localfile);
                String filename = imageFile.getName();
                destinationfile = filename;
		ftp.setFileType(ftp.BINARY_FILE_TYPE);
                ftp.makeDirectory("imageUploads");
		boolean Store = ftp.storeFile("/imageUploads/"+destinationfile, in);
		in.close();
		ftp.logout();
		ftp.disconnect();
	}
	catch (Exception ex)
	{
		ex.printStackTrace();
		return false;
	}
	return true;
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
