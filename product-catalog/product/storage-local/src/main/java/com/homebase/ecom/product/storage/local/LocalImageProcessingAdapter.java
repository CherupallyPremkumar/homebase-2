package com.homebase.ecom.product.storage.local;

import com.homebase.ecom.product.domain.port.ImageProcessingPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Local image processing using Java ImageIO.
 * Generates thumbnail, medium, and zoom variants on the local filesystem.
 * For development and testing only.
 */
public class LocalImageProcessingAdapter implements ImageProcessingPort {

    private static final Logger log = LoggerFactory.getLogger(LocalImageProcessingAdapter.class);

    private static final int THUMBNAIL_SIZE = 150;
    private static final int MEDIUM_SIZE = 600;
    private static final int ZOOM_SIZE = 1200;

    private final Path basePath;
    private final String baseUrl;

    public LocalImageProcessingAdapter(Path basePath, String baseUrl) {
        this.basePath = basePath;
        this.baseUrl = baseUrl;
    }

    @Override
    public ProcessingResult process(String sourceKey) {
        Path sourcePath = basePath.resolve(sourceKey);

        try {
            BufferedImage original = ImageIO.read(sourcePath.toFile());
            if (original == null) {
                throw new RuntimeException("Cannot read image: " + sourceKey);
            }

            int origWidth = original.getWidth();
            int origHeight = original.getHeight();
            long fileSize = Files.size(sourcePath);

            String baseName = sourceKey.substring(0, sourceKey.lastIndexOf('.'));
            String ext = "jpg";

            // Generate variants
            String thumbnailKey = baseName + "_thumb." + ext;
            resize(original, basePath.resolve(thumbnailKey), THUMBNAIL_SIZE);

            String mediumKey = baseName + "_medium." + ext;
            resize(original, basePath.resolve(mediumKey), MEDIUM_SIZE);

            String zoomKey = baseName + "_zoom." + ext;
            resize(original, basePath.resolve(zoomKey), ZOOM_SIZE);

            log.info("Local processing complete: source={}, variants generated", sourceKey);

            return new ProcessingResult(
                    baseUrl + "/product/media/local-files/" + thumbnailKey,
                    baseUrl + "/product/media/local-files/" + mediumKey,
                    baseUrl + "/product/media/local-files/" + zoomKey,
                    baseUrl + "/product/media/local-files/" + sourceKey,
                    origWidth,
                    origHeight,
                    fileSize,
                    "image/" + ext
            );

        } catch (IOException e) {
            throw new RuntimeException("Image processing failed for: " + sourceKey, e);
        }
    }

    private void resize(BufferedImage original, Path target, int maxDimension) throws IOException {
        int origW = original.getWidth();
        int origH = original.getHeight();

        double scale = Math.min((double) maxDimension / origW, (double) maxDimension / origH);
        if (scale >= 1.0) {
            scale = 1.0; // Don't upscale
        }

        int newW = (int) (origW * scale);
        int newH = (int) (origH * scale);

        BufferedImage resized = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, newW, newH, null);
        g.dispose();

        Files.createDirectories(target.getParent());
        ImageIO.write(resized, "jpg", target.toFile());
    }
}
