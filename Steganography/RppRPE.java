
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class RppRPE extends Steganography {

    private String seed;
    private final int[][] array_of_seed1 = new int[][]{{0}, {1}, {2}, {0, 1}, {0, 2}, {1, 2}, {0, 1, 2}};

    public RppRPE() {
        super();
    }

    public int seed1() {

        Random rand = new Random();
        int seed1 = rand.nextInt(7);
        return seed1;
    }

    public int seed2() {
        Random rand = new Random();
        int seed2 = rand.nextInt(2) + 1;
        return seed2;
    }

    public boolean message_validation(SecretMessage message, CoverMedia image) {
        boolean res = true;
        if (message.getMessageLength() > (image.imageHeight * image.imageWidth * 3)) {
            res = false;
        } else {
            if (message.getMessageLength() % 2 == 0) {

                res = true;
            }
        }
        return res;
    }

    private BufferedImage setNewImage(CoverMedia image) {
        BufferedImage newImage = new BufferedImage(image.imageHeight, image.imageWidth, BufferedImage.TYPE_INT_RGB);
        int[][][] cover_media = image.getPixels();
        for (int y = 0; y < image.imageHeight; y++) {
            for (int x = 0; x < image.imageWidth; x++) {
                int value = cover_media[x][y][0] << 16 | cover_media[x][y][1] << 8 | cover_media[x][y][2];
                newImage.setRGB(x, y, value);
            }
        }
        return newImage;
    }

    @Override
    public void hide(CoverMedia image, SecretMessage message, String newDir) {
        this.message = message;
        this.seed = "";
        int seed1, seed2;
        int count = message.getMessageLength();
        String binary_secret_message = message.binaryMessage();
        this.cover_media = image.getPixels();
        this.new_cover_media = image.getPixels();
        this.message_length = message.getMessageLength();
        int value;
        BufferedImage newImage = setNewImage(image);

        String[] rgb = new String[]{"R", "G", "B"};

        //Patch A (bagian sebelah kiri)
        for (int y = 0; y < image.imageHeight; y++) {
            for (int x = 0; x < (image.imageWidth / 2) - 1; x++) {
                if (this.message_length > 0) {
                    seed1 = this.seed1();
                    seed2 = this.seed2();
                    //int[] pixel = this.new_cover_media[x][y];
                    while ((this.array_of_seed1[seed1].length * seed2 > message_length)) {
                        seed1 = this.seed1();
                        seed2 = this.seed2();
                    }
                    int[] temp_seed = this.array_of_seed1[seed1];

                    for (int i = 0; i < temp_seed.length; i++) {
                        int current_channel = this.cover_media[x][y][this.array_of_seed1[seed1][i]];

                        //turn decimal pixel into binary
                        String current_channel_binary = image.decimalTo_Binary(current_channel);

                        //temp var subtitution 
                        String substr = current_channel_binary.substring(0, current_channel_binary.length() - seed2);

                        //bit message to hide
                        String message_to_hide = binary_secret_message.substring(0, seed2);

                        //concat substr + message_to_hide
                        String new_channel_binary = substr + message_to_hide;
                        binary_secret_message = binary_secret_message.substring(seed2, binary_secret_message.length());
                        message_length -= seed2;

                        if (message_length >= 0) {
                            new_cover_media[x][y][this.array_of_seed1[seed1][i]] = image.bin_to_decimal(new_channel_binary);
                        }
                        
                        value = new_cover_media[x][y][0] << 16 | new_cover_media[x][y][1] << 8 | new_cover_media[x][y][2];
                        newImage.setRGB(x, y, value);
                    }

                    //key
                    System.out.println("SEED = " + seed1 + "" + seed2);
                    this.seed += seed1 + "" + seed2 + "";

                }
            }
        }

        //Patch B (bagian sebelah kanan)
        for (int y = 0; y < image.imageHeight; y++) {
            for (int x = (image.imageWidth / 2); x < image.imageWidth; x++) {
                if (this.message_length > 0) {
                    
                    seed1 = this.seed1();
                    seed2 = this.seed2();
                    //int[] pixel = this.new_cover_media[x][y];
                    while ((this.array_of_seed1[seed1].length * seed2 > message_length)) {
                        seed1 = this.seed1();
                        seed2 = this.seed2();
                    }
                    int[] temp_seed = this.array_of_seed1[seed1];

                    for (int i = 0; i < temp_seed.length; i++) {
                        int current_channel = this.cover_media[x][y][this.array_of_seed1[seed1][i]];

                        //turn decimal pixel into binary
                        String current_channel_binary = image.decimalTo_Binary(current_channel);

                        //temp var subtitution 
                        String substr = current_channel_binary.substring(0, current_channel_binary.length() - seed2);

                        //bit message to hide
                        String message_to_hide = binary_secret_message.substring(0, seed2);

                        //concat substr + message_to_hide
                        String new_channel_binary = substr + message_to_hide;
                        binary_secret_message = binary_secret_message.substring(seed2, binary_secret_message.length());
                        message_length -= seed2;

                        if (message_length >= 0) {
                            new_cover_media[x][y][this.array_of_seed1[seed1][i]] = image.bin_to_decimal(new_channel_binary);
                        }
                        
                        value = new_cover_media[x][y][0] << 16 | new_cover_media[x][y][1] << 8 | new_cover_media[x][y][2];
                        newImage.setRGB(x, y, value);
                    }

                    //key
                    System.out.println("SEED = " + seed1 + "" + seed2);
                    this.seed += seed1 + "" + seed2 + "";

                }
            }
        }
        
        File output = new File(newDir + "\\Citra Stego RPP+RPE.bmp");
        try {
            ImageIO.write(newImage, "bmp", output);
        } catch (IOException e) {
            System.out.println("ERROR");
        }
    }

    public String Get_Seed() {
        return this.seed;
    }

    public boolean keyValidation(String key) {
        if (key.length() % 2 != 0) {
            return false;
        }

        for (int i = 0; i < key.length(); i++) {
            if (i % 2 == 0) {
                int seed1 = Integer.parseInt(key.charAt(i) + "");
                if (seed1 > 6) {
                    return false;
                }
            } else {
                int seed2 = Integer.parseInt(key.charAt(i) + "");
                if (seed2 > 3 || seed2 < 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public String extractMessage(String key, CoverMedia image) {
        String seed = key;
        String message = "";
        String result = "";
        int count = 1;
        this.cover_media = image.getPixels();
        for (int y = 0; y < image.imageHeight; y++) {
            for (int x = 0; x < image.imageWidth; x++) {
                if (seed.length() > 0) {
                    String pair = seed.substring(0, 2);
                    int seed1 = Integer.parseInt(pair.charAt(0) + "");
                    int seed2 = Integer.parseInt(pair.charAt(1) + "");
                    int[] temp_seed = this.array_of_seed1[seed1];

                    for (int i = 0; i < temp_seed.length; i++) {

                        int current_channel = this.cover_media[x][y][temp_seed[i]];
                        String rgb_binary = image.decimalTo_Binary(current_channel);
                        message += rgb_binary.substring(rgb_binary.length() - seed2, rgb_binary.length());
                        count++;
                    }
                    seed = seed.substring(2, seed.length());
                }
            }
        }
        SecretMessage sm = new SecretMessage(message);
        result = sm.convert_bin_to_message(message);
        return result;
    }
}
