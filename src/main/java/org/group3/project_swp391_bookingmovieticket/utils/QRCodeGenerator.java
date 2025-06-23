package org.group3.project_swp391_bookingmovieticket.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QRCodeGenerator {

    public static byte[] generateQRCodeAsBytes(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);

            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            return baos.toByteArray();

        } catch (WriterException | IOException e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }

    public static String saveQrToLocal(byte[] qrBytes, String fileName) {
        try {
            // Đường dẫn tới thư mục lưu ảnh QR trong static
            String folderPath = "src/main/resources/static/qrs/";
            Path outputPath = Paths.get(folderPath + fileName);

            // Tạo thư mục nếu chưa có
            Files.createDirectories(outputPath.getParent());

            // Ghi ảnh vào file
            Files.write(outputPath, qrBytes);

            // Trả về đường dẫn tương đối để lưu DB (Spring sẽ phục vụ ảnh từ static/)
            return "/qrs/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to save QR code image", e);
        }
    }
}
