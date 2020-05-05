
import java.util.Scanner;

public abstract class Steganography {
    protected SecretMessage message;
    protected String key;
    protected int message_length;
    protected CoverMedia image;
    protected int[][][] cover_media;
    protected int[][][] new_cover_media;

    public Steganography ()
    {
        
    }
    
    public void hide(CoverMedia image, SecretMessage message, String newDir)
    {
        
        System.out.println("Pesan berhasil disisipkan");
    }
    
    public String extractMessage(String key, CoverMedia image)
    {
        return "Pesan berhasil diekstrak";
    }

}
