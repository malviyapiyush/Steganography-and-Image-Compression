
public class SecretMessage {
    
    public String message;
    public int message_length;
    public String binaryMessage;

    public SecretMessage(String message) {
        this.message = message;
        this.message_length=this.message.length();
    }
    
    /**
     * mengubah secret data dari array berisi decimal menjadi array berisi biner
     * @return 
     */
    public String binaryMessage ()
    { 
        int[] ascii_message = new int [this.message_length];
        String res = "";
        for (int i = 0; i < ascii_message.length; i++) {
            ascii_message [i] = (int)this.message.charAt(i);
        }
        
        for (int i = 0; i < ascii_message.length; i++) {
            String bin_message = Integer.toBinaryString(ascii_message[i]);
            for (int j = bin_message.length(); j < 8; j++) {
                bin_message = "0" + bin_message;
            }
            res = res + bin_message;
        }
        return res;
    }
    
    public String convert_bin_to_message (String bin_message)
    {
        String res = "";
        int length = 0;
        while (length < bin_message.length())
        {
            int ascii_message = Integer.parseInt(bin_message.substring(length, length+8),2);
            res += (char)(ascii_message);
            length +=8;
        }
        return res;
    }
    
    public int getMessageLength()
    {
        return this.message_length*8;
    }
}
