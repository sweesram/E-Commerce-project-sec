package com.cart.ecom_proj.service;

import com.cart.ecom_proj.model.Product;
import com.cart.ecom_proj.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

@Service
public class ProductService {

    @Autowired
    private ProductRepo repo;

    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Product getProductById(int id) {
        return repo.findById(id).orElse(null);
    }

    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageDate(imageFile.getBytes());
        return repo.save(product);
    }

    public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {
        product.setImageDate(imageFile.getBytes());
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        return repo.save(product);
    }

    public void deleteProduct(int id) {
        repo.deleteById(id);
    }

    public List<Product> searchProducts(String keyword) {
        return repo.searchProducts(keyword);
    }

    public List<Product> getProductsByCategory(String category) {
        return repo.findByCategory(category);
    }

    public List<String> getAllCategories() {
        return repo.findAllCategories();
    }

    public void updateProductImagesWithRealImages() {
        List<Product> existingProducts = repo.findAll();

        // Map product names to their corresponding image files
        Map<String, String> productImageMap = new HashMap<>();
        productImageMap.put("Wireless Headphones", "wireless_headphones.jpg");
        productImageMap.put("Smartphone X12", "smartphone_x12.jpg");
        productImageMap.put("Ultrabook Pro 14", "ultrabook_pro_14.jpg");
        productImageMap.put("Gaming Mouse GX", "gaming_mouse_gx.jpg");
        productImageMap.put("Mechanical Keyboard MK87", "mechanical_keyboard_mk87.jpg");
        productImageMap.put("4K UHD Monitor 27\"", "4k_monitor_27.jpg");
        productImageMap.put("Bluetooth Speaker Mini", "bluetooth_speaker_mini.jpg");

        for (Product product : existingProducts) {
            // Try exact match first, then case-insensitive match
            String imageFileName = productImageMap.get(product.getName());
            if (imageFileName == null) {
                // Try case-insensitive match
                for (Map.Entry<String, String> entry : productImageMap.entrySet()) {
                    if (entry.getKey().equalsIgnoreCase(product.getName())) {
                        imageFileName = entry.getValue();
                        break;
                    }
                }
            }

            if (imageFileName != null) {
                // Load real image from resources
                byte[] imageData = loadImageFromResources(imageFileName);
                product.setImageName(imageFileName);
                product.setImageType("image/jpeg");
                product.setImageDate(imageData);
                System.out.println("Updated product '" + product.getName() + "' with real image: " + imageFileName);
            } else {
                // Fallback to placeholder for unknown products
                product.setImageName(product.getName().toLowerCase().replace(" ", "_") + ".png");
                product.setImageType("image/png");
                product.setImageDate(generatePlaceholderImage(product.getName(), Color.GRAY));
                System.out.println("Updated product '" + product.getName() + "' with placeholder image");
            }
        }

        repo.saveAll(existingProducts);
        System.out.println("Updated " + existingProducts.size() + " existing products with images.");
    }

    private byte[] loadImageFromResources(String imageFileName) {
        try {
            // Load image from resources/images directory
            String resourcePath = "images/" + imageFileName;
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);

            if (inputStream == null) {
                System.err.println("Image not found in resources: " + resourcePath);
                return generatePlaceholderImage(imageFileName.replace(".jpg", ""), Color.GRAY);
            }

            return inputStream.readAllBytes();

        } catch (IOException e) {
            System.err.println("Error loading image: " + imageFileName + " - " + e.getMessage());
            return generatePlaceholderImage(imageFileName.replace(".jpg", ""), Color.GRAY);
        }
    }

    private byte[] generatePlaceholderImage(String productName, Color bgColor) {
        try {
            // Create a very small 50x50 image to fit in database constraints
            BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();

            // Fill background with the provided color
            g2d.setColor(bgColor);
            g2d.fillRect(0, 0, 50, 50);

            // Add a border
            g2d.setColor(bgColor.darker());
            g2d.drawRect(0, 0, 49, 49);

            // Add first letter of product name
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            String letter = productName.substring(0, 1).toUpperCase();
            FontMetrics fm = g2d.getFontMetrics();
            int x = (50 - fm.stringWidth(letter)) / 2;
            int y = (50 + fm.getAscent()) / 2;
            g2d.drawString(letter, x, y);

            g2d.dispose();

            // Convert to byte array with high compression
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();

        } catch (Exception e) {
            // Return minimal byte array if image generation fails
            return new byte[] { (byte) 0x89, 0x50, 0x4E, 0x47 }; // PNG header only
        }
    }
}
