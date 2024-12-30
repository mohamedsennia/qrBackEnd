package com.example.demo.file;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileService {

    @Value("${upload.dir}")
    private String uploadDir;
    @Value("${server_url:http://localhost:8080/}")
    private String serverUrl;

    public String dealWithFile(MultipartFile multipartFile){
        CostumeFile costumeFile=new CostumeFile();
         this.saveFile(multipartFile,costumeFile);
        try {
            this.generateQRCode(costumeFile,uploadDir+File.separator,300,300);
            return serverUrl+costumeFile.getPath().replace("\\","/");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public void  saveFile(MultipartFile multipartFile,CostumeFile costumeFile){
        try {
            String fileName = multipartFile.getOriginalFilename();
            String absoluteUploadDir = new File(uploadDir).getAbsolutePath();

            File directory = new File(absoluteUploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String[] path=uploadDir.split("/");
            costumeFile.setPath(path[path.length-1]);
            costumeFile.setExtension(fileName.split("\\.")[1]);


            File destFile = new File(absoluteUploadDir+File.separator+costumeFile.getPath().split(File.separator.replace("\\", "\\\\"))[1]);
            multipartFile.transferTo(destFile);


        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public  void generateQRCode(CostumeFile costumeFile, String filePath, int width, int height) throws Exception {
        // Set QR code parameters
       String  data=serverUrl+costumeFile.getPath().replace("\\","/");
       costumeFile.setExtension("png");
       filePath=filePath+costumeFile.getId()+"."+costumeFile.getExtension();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.MARGIN, 1);

        // Create a BitMatrix from the data
        BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height, hints);

        // Convert the BitMatrix to a BufferedImage
        BufferedImage image = this.toBufferedImage(bitMatrix);

        // Save the image to file
        File file = new File(filePath);
        ImageIO.write(image, "PNG", file);
    }


    private  BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        // Set white background
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0x000000 : 0xFFFFFF);  // Black and White
            }
        }

        return image;
    }
}
