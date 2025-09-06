package com.cart.ecom_proj.config;

import com.cart.ecom_proj.model.Product;
import com.cart.ecom_proj.repo.ProductRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class DataSeeder implements CommandLineRunner {

        private final ProductRepo productRepo;

        public DataSeeder(ProductRepo productRepo) {
                this.productRepo = productRepo;
        }

        @Override
        public void run(String... args) {
                // Only seed if no products exist, or if existing products don't have images
                long productCount = productRepo.count();
                if (productCount > 0) {
                        // Check if any existing product has real image data (more than just 1 byte
                        // placeholder)
                        boolean hasRealImagesAlready = productRepo.findAll().stream()
                                        .anyMatch(p -> p.getImageDate() != null && p.getImageDate().length > 1000);

                        if (hasRealImagesAlready) {
                                System.out.println(
                                                "Products with real images already exist. Force updating with custom placeholders...");
                                updateExistingProductsWithImages();
                                return;
                        } else {
                                System.out.println(
                                                "Products exist but no real images found. Updating existing products with real images...");
                                updateExistingProductsWithImages();
                                return;
                        }
                }

                List<Product> sampleProducts = Arrays.asList(
                                createProductWithRealImage(
                                                "Wireless Headphones",
                                                "Over-ear Bluetooth headphones with noise cancellation and 30h battery.",
                                                "SoundWave",
                                                new BigDecimal("99.99"),
                                                "Electronics",
                                                true,
                                                120,
                                                "wireless_headphones.jpg"),
                                createProductWithRealImage(
                                                "Smartphone X12",
                                                "6.5\" OLED display, 128GB storage, triple-camera system.",
                                                "NeoMobile",
                                                new BigDecimal("699.00"),
                                                "Mobiles",
                                                true,
                                                75,
                                                "smartphone_x12.jpg"),
                                createProductWithRealImage(
                                                "Ultrabook Pro 14",
                                                "14\" laptop, 16GB RAM, 512GB SSD, Intel i7 13th Gen.",
                                                "AeroTech",
                                                new BigDecimal("1299.00"),
                                                "Computers",
                                                true,
                                                40,
                                                "ultrabook_pro_14.jpg"),
                                createProductWithRealImage(
                                                "Gaming Mouse GX",
                                                "Ergonomic RGB mouse with 8 programmable buttons, 16K DPI sensor.",
                                                "HyperClick",
                                                new BigDecimal("49.99"),
                                                "Accessories",
                                                true,
                                                200,
                                                "gaming_mouse_gx.jpg"),
                                createProductWithRealImage(
                                                "Mechanical Keyboard MK87",
                                                "87-key mechanical keyboard with hot-swappable switches and RGB.",
                                                "KeyForge",
                                                new BigDecimal("89.50"),
                                                "Accessories",
                                                true,
                                                150,
                                                "mechanical_keyboard_mk87.jpg"),
                                createProductWithRealImage(
                                                "4K UHD Monitor 27\"",
                                                "27-inch IPS monitor with HDR10 and 144Hz refresh rate.",
                                                "VisionPlus",
                                                new BigDecimal("379.00"),
                                                "Monitors",
                                                true,
                                                60,
                                                "4k_monitor_27.jpg"),
                                createProductWithRealImage(
                                                "Bluetooth Speaker Mini",
                                                "Portable waterproof speaker with 12h playtime and deep bass.",
                                                "BoomBox",
                                                new BigDecimal("39.99"),
                                                "Audio",
                                                true,
                                                300,
                                                "bluetooth_speaker_mini.jpg"),
                                createProductWithRealImage(
                                                "Wireless Earbuds Pro",
                                                "True wireless earbuds with active noise cancellation and 24h battery.",
                                                "SoundWave",
                                                new BigDecimal("149.99"),
                                                "Electronics",
                                                true,
                                                180,
                                                "wireless_earbuds.jpg"),
                                createProductWithRealImage(
                                                "Tablet Pro 10\"",
                                                "10-inch tablet with 256GB storage, stylus support, and 12h battery.",
                                                "TabTech",
                                                new BigDecimal("449.00"),
                                                "Tablets",
                                                true,
                                                90,
                                                "tablet_pro.jpg"),
                                createProductWithRealImage(
                                                "Smart Watch Series 5",
                                                "Fitness tracking, heart rate monitor, GPS, and 7-day battery life.",
                                                "WearTech",
                                                new BigDecimal("299.99"),
                                                "Wearables",
                                                true,
                                                120,
                                                "smart_watch.jpg"),
                                createProductWithRealImage(
                                                "HD Webcam 4K",
                                                "4K webcam with auto-focus, noise cancellation, and privacy shutter.",
                                                "CamTech",
                                                new BigDecimal("129.99"),
                                                "Accessories",
                                                true,
                                                80,
                                                "webcam_hd.jpg"),
                                createProductWithRealImage(
                                                "Gaming Headset Pro",
                                                "7.1 surround sound gaming headset with RGB lighting and noise cancellation.",
                                                "GameAudio",
                                                new BigDecimal("179.99"),
                                                "Gaming",
                                                true,
                                                100,
                                                "gaming_headset.jpg"),
                                createProductWithRealImage(
                                                "External HDD 2TB",
                                                "2TB portable hard drive with USB 3.0 and password protection.",
                                                "StorageMax",
                                                new BigDecimal("89.99"),
                                                "Storage",
                                                true,
                                                200,
                                                "external_hdd.jpg"),
                                createProductWithRealImage(
                                                "USB-C Cable 6ft",
                                                "High-speed USB-C cable with 100W power delivery and data transfer.",
                                                "CablePro",
                                                new BigDecimal("19.99"),
                                                "Accessories",
                                                true,
                                                500,
                                                "usb_cable.jpg"),
                                createProductWithRealImage(
                                                "Power Bank 20000mAh",
                                                "High-capacity power bank with fast charging and wireless charging pad.",
                                                "PowerMax",
                                                new BigDecimal("59.99"),
                                                "Accessories",
                                                true,
                                                150,
                                                "power_bank.jpg"),
                                createProductWithRealImage(
                                                "Laptop Stand Adjustable",
                                                "Ergonomic aluminum laptop stand with adjustable height and angle.",
                                                "ErgoTech",
                                                new BigDecimal("79.99"),
                                                "Accessories",
                                                true,
                                                75,
                                                "laptop_stand.jpg"),
                                createProductWithRealImage(
                                                "Desktop PC Gaming",
                                                "High-performance gaming PC with RTX 4070, 32GB RAM, 1TB SSD.",
                                                "GamePC",
                                                new BigDecimal("1899.99"),
                                                "Computers",
                                                true,
                                                25,
                                                "desktop_pc.jpg"),
                                createProductWithRealImage(
                                                "Inkjet Printer All-in-One",
                                                "Wireless all-in-one printer with scanning, copying, and mobile printing.",
                                                "PrintTech",
                                                new BigDecimal("199.99"),
                                                "Printers",
                                                true,
                                                60,
                                                "printer_inkjet.jpg"),
                                createProductWithRealImage(
                                                "WiFi Router AC3000",
                                                "Tri-band WiFi 6 router with mesh support and advanced security.",
                                                "NetTech",
                                                new BigDecimal("249.99"),
                                                "Networking",
                                                true,
                                                40,
                                                "router_wifi.jpg"),
                                createProductWithRealImage(
                                                "USB Microphone Studio",
                                                "Professional USB microphone with cardioid pattern and zero-latency monitoring.",
                                                "AudioPro",
                                                new BigDecimal("159.99"),
                                                "Audio",
                                                true,
                                                70,
                                                "microphone_usb.jpg"),
                                createProductWithRealImage(
                                                "Graphics Card RTX 4080",
                                                "High-end graphics card with 16GB GDDR6X memory and ray tracing.",
                                                "GPUPro",
                                                new BigDecimal("1199.99"),
                                                "Components",
                                                true,
                                                15,
                                                "graphics_card.jpg"),
                                createProductWithRealImage(
                                                "SSD Drive 1TB NVMe",
                                                "Ultra-fast NVMe SSD with read speeds up to 7000 MB/s.",
                                                "StorageMax",
                                                new BigDecimal("129.99"),
                                                "Storage",
                                                true,
                                                100,
                                                "ssd_drive.jpg"));

                productRepo.saveAll(sampleProducts);
        }

        private Product createProductWithRealImage(
                        String name,
                        String description,
                        String brand,
                        BigDecimal price,
                        String category,
                        boolean available,
                        int stock,
                        String imageFileName) {
                Product product = new Product();
                product.setName(name);
                product.setDescription(description);
                product.setBrand(brand);
                product.setPrice(price);
                product.setCategory(category);
                product.setReleaseDate(new Date());
                product.setProductAvailable(available);
                product.setStockQuantity(stock);

                // Load real image from resources
                byte[] imageData = loadImageFromResources(imageFileName);
                product.setImageName(imageFileName);
                product.setImageType("image/jpeg");
                product.setImageDate(imageData);

                return product;
        }

        private void updateExistingProductsWithImages() {
                List<Product> existingProducts = productRepo.findAll();

                // Map product names to their corresponding image files
                java.util.Map<String, String> productImageMap = new java.util.HashMap<>();
                productImageMap.put("Wireless Headphones", "wireless_headphones.jpg");
                productImageMap.put("Smartphone X12", "smartphone_x12.jpg");
                productImageMap.put("Ultrabook Pro 14", "ultrabook_pro_14.jpg");
                productImageMap.put("Gaming Mouse GX", "gaming_mouse_gx.jpg");
                productImageMap.put("Mechanical Keyboard MK87", "mechanical_keyboard_mk87.jpg");
                productImageMap.put("4K UHD Monitor 27\"", "4k_monitor_27.jpg");
                productImageMap.put("Bluetooth Speaker Mini", "bluetooth_speaker_mini.jpg");
                productImageMap.put("Wireless Earbuds Pro", "wireless_earbuds.jpg");
                productImageMap.put("Tablet Pro 10\"", "tablet_pro.jpg");
                productImageMap.put("Smart Watch Series 5", "smart_watch.jpg");
                productImageMap.put("HD Webcam 4K", "webcam_hd.jpg");
                productImageMap.put("Gaming Headset Pro", "gaming_headset.jpg");
                productImageMap.put("External HDD 2TB", "external_hdd.jpg");
                productImageMap.put("USB-C Cable 6ft", "usb_cable.jpg");
                productImageMap.put("Power Bank 20000mAh", "power_bank.jpg");
                productImageMap.put("Laptop Stand Adjustable", "laptop_stand.jpg");
                productImageMap.put("Desktop PC Gaming", "desktop_pc.jpg");
                productImageMap.put("Inkjet Printer All-in-One", "printer_inkjet.jpg");
                productImageMap.put("WiFi Router AC3000", "router_wifi.jpg");
                productImageMap.put("USB Microphone Studio", "microphone_usb.jpg");
                productImageMap.put("Graphics Card RTX 4080", "graphics_card.jpg");
                productImageMap.put("SSD Drive 1TB NVMe", "ssd_drive.jpg");

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
                                System.out.println("Updated product '" + product.getName() + "' with real image: "
                                                + imageFileName);
                        } else {
                                // Generate product-specific placeholder image
                                Color bgColor = getProductColor(product.getName());
                                product.setImageName(product.getName().toLowerCase().replace(" ", "_") + ".png");
                                product.setImageType("image/png");
                                product.setImageDate(generatePlaceholderImage(product.getName(), bgColor));
                                System.out.println(
                                                "Updated product '" + product.getName()
                                                                + "' with custom placeholder image");
                        }
                }

                productRepo.saveAll(existingProducts);
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
                        // Create a larger 200x150 image for better visibility
                        BufferedImage image = new BufferedImage(200, 150, BufferedImage.TYPE_INT_RGB);
                        Graphics2D g2d = image.createGraphics();
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        // Fill background with gradient
                        GradientPaint gradient = new GradientPaint(0, 0, bgColor, 200, 150, bgColor.darker());
                        g2d.setPaint(gradient);
                        g2d.fillRect(0, 0, 200, 150);

                        // Add a border
                        g2d.setColor(bgColor.darker().darker());
                        g2d.setStroke(new BasicStroke(2));
                        g2d.drawRect(1, 1, 197, 147);

                        // Draw product-specific icons
                        drawProductIcon(g2d, productName, 200, 150);

                        // Add product name
                        g2d.setColor(Color.WHITE);
                        g2d.setFont(new Font("Arial", Font.BOLD, 12));
                        FontMetrics fm = g2d.getFontMetrics();
                        String displayName = productName.length() > 20 ? productName.substring(0, 17) + "..."
                                        : productName;
                        int textX = (200 - fm.stringWidth(displayName)) / 2;
                        int textY = 130;
                        g2d.drawString(displayName, textX, textY);

                        g2d.dispose();

                        // Convert to byte array
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(image, "png", baos);
                        return baos.toByteArray();

                } catch (Exception e) {
                        // Return minimal byte array if image generation fails
                        return new byte[] { (byte) 0x89, 0x50, 0x4E, 0x47 }; // PNG header only
                }
        }

        private void drawProductIcon(Graphics2D g2d, String productName, int width, int height) {
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(3));

                int centerX = width / 2;
                int centerY = height / 2 - 10;

                String lowerName = productName.toLowerCase();

                if (lowerName.contains("hdd") || lowerName.contains("hard drive") || lowerName.contains("external")) {
                        // Draw hard drive icon
                        g2d.drawRect(centerX - 30, centerY - 15, 60, 30);
                        g2d.drawRect(centerX - 25, centerY - 10, 50, 20);
                        g2d.drawLine(centerX - 20, centerY - 5, centerX + 20, centerY - 5);
                        g2d.drawLine(centerX - 20, centerY, centerX + 20, centerY);
                        g2d.drawLine(centerX - 20, centerY + 5, centerX + 20, centerY + 5);
                } else if (lowerName.contains("cable") || lowerName.contains("usb")) {
                        // Draw cable icon
                        g2d.drawOval(centerX - 25, centerY - 5, 10, 10);
                        g2d.drawOval(centerX + 15, centerY - 5, 10, 10);
                        g2d.drawLine(centerX - 20, centerY, centerX + 20, centerY);
                        g2d.drawLine(centerX - 20, centerY - 2, centerX + 20, centerY - 2);
                        g2d.drawLine(centerX - 20, centerY + 2, centerX + 20, centerY + 2);
                } else if (lowerName.contains("power bank") || lowerName.contains("battery")) {
                        // Draw power bank icon
                        g2d.drawRoundRect(centerX - 20, centerY - 25, 40, 50, 10, 10);
                        g2d.drawRect(centerX - 15, centerY - 20, 30, 40);
                        g2d.drawLine(centerX - 5, centerY + 25, centerX - 5, centerY + 30);
                        g2d.drawLine(centerX + 5, centerY + 25, centerX + 5, centerY + 30);
                        g2d.drawLine(centerX - 8, centerY + 30, centerX + 8, centerY + 30);
                } else if (lowerName.contains("laptop stand") || lowerName.contains("stand")) {
                        // Draw laptop stand icon
                        g2d.drawRect(centerX - 30, centerY - 10, 60, 20);
                        g2d.drawLine(centerX - 25, centerY + 10, centerX - 20, centerY + 20);
                        g2d.drawLine(centerX + 25, centerY + 10, centerX + 20, centerY + 20);
                        g2d.drawLine(centerX - 20, centerY + 20, centerX + 20, centerY + 20);
                        g2d.drawLine(centerX - 15, centerY + 20, centerX - 10, centerY + 25);
                        g2d.drawLine(centerX + 15, centerY + 20, centerX + 10, centerY + 25);
                } else if (lowerName.contains("monitor") || lowerName.contains("display")) {
                        // Draw monitor icon
                        g2d.drawRect(centerX - 35, centerY - 20, 70, 40);
                        g2d.drawRect(centerX - 30, centerY - 15, 60, 30);
                        g2d.drawLine(centerX, centerY + 20, centerX, centerY + 30);
                        g2d.drawLine(centerX - 10, centerY + 30, centerX + 10, centerY + 30);
                } else if (lowerName.contains("mouse")) {
                        // Draw mouse icon
                        g2d.drawOval(centerX - 20, centerY - 10, 40, 20);
                        g2d.drawLine(centerX - 15, centerY - 5, centerX - 10, centerY - 10);
                        g2d.drawLine(centerX + 15, centerY - 5, centerX + 10, centerY - 10);
                } else if (lowerName.contains("keyboard")) {
                        // Draw keyboard icon
                        g2d.drawRoundRect(centerX - 40, centerY - 10, 80, 20, 5, 5);
                        for (int i = 0; i < 8; i++) {
                                g2d.drawRect(centerX - 35 + i * 10, centerY - 5, 8, 10);
                        }
                } else if (lowerName.contains("headphone") || lowerName.contains("headset")) {
                        // Draw headphone icon
                        g2d.drawOval(centerX - 30, centerY - 15, 20, 20);
                        g2d.drawOval(centerX + 10, centerY - 15, 20, 20);
                        g2d.drawArc(centerX - 20, centerY - 5, 40, 20, 0, 180);
                } else if (lowerName.contains("smartphone") || lowerName.contains("phone")) {
                        // Draw smartphone icon
                        g2d.drawRoundRect(centerX - 8, centerY - 20, 16, 40, 3, 3);
                        g2d.drawRect(centerX - 6, centerY - 18, 12, 30);
                        g2d.drawOval(centerX - 2, centerY + 12, 4, 4);
                } else if (lowerName.contains("laptop") || lowerName.contains("notebook")) {
                        // Draw laptop icon
                        g2d.drawRect(centerX - 30, centerY - 15, 60, 20);
                        g2d.drawRect(centerX - 25, centerY - 10, 50, 15);
                        g2d.drawLine(centerX - 30, centerY + 5, centerX - 20, centerY + 15);
                        g2d.drawLine(centerX + 30, centerY + 5, centerX + 20, centerY + 15);
                } else {
                        // Default: first letter
                        g2d.setFont(new Font("Arial", Font.BOLD, 48));
                        FontMetrics fm = g2d.getFontMetrics();
                        String letter = productName.substring(0, 1).toUpperCase();
                        int x = (width - fm.stringWidth(letter)) / 2;
                        int y = (height + fm.getAscent()) / 2 - 10;
                        g2d.drawString(letter, x, y);
                }
        }

        private Color getProductColor(String productName) {
                String lowerName = productName.toLowerCase();
                if (lowerName.contains("hdd") || lowerName.contains("hard drive") || lowerName.contains("external")) {
                        return new Color(70, 130, 180); // Steel blue for storage
                } else if (lowerName.contains("cable") || lowerName.contains("usb")) {
                        return new Color(255, 140, 0); // Orange for cables
                } else if (lowerName.contains("power bank") || lowerName.contains("battery")) {
                        return new Color(50, 205, 50); // Green for power
                } else if (lowerName.contains("laptop stand") || lowerName.contains("stand")) {
                        return new Color(128, 128, 128); // Gray for stands
                } else if (lowerName.contains("monitor") || lowerName.contains("display")) {
                        return new Color(75, 0, 130); // Indigo for displays
                } else if (lowerName.contains("mouse")) {
                        return new Color(220, 20, 60); // Crimson for mice
                } else if (lowerName.contains("keyboard")) {
                        return new Color(255, 20, 147); // Deep pink for keyboards
                } else if (lowerName.contains("headphone") || lowerName.contains("headset")) {
                        return new Color(138, 43, 226); // Blue violet for audio
                } else if (lowerName.contains("smartphone") || lowerName.contains("phone")) {
                        return new Color(0, 191, 255); // Deep sky blue for phones
                } else if (lowerName.contains("laptop") || lowerName.contains("notebook")) {
                        return new Color(30, 144, 255); // Dodger blue for laptops
                } else {
                        return new Color(105, 105, 105); // Dim gray default
                }
        }
}
