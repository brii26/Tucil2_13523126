import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.RenderedImage;
import java.io.IOException;

public class GifWriter {
    private ImageWriter gifWriter;
    private ImageWriteParam imageWriteParam;
    private IIOMetadata imageMetaData;

    public GifWriter(ImageOutputStream outputStream, int imageType, int delay, boolean loops) throws IOException {
        gifWriter = ImageIO.getImageWritersBySuffix("gif").next();
        imageWriteParam = gifWriter.getDefaultWriteParam();
        ImageTypeSpecifier imageTypeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(imageType);
        imageMetaData = gifWriter.getDefaultImageMetadata(imageTypeSpecifier, imageWriteParam);

        String metaFormatName = imageMetaData.getNativeMetadataFormatName();
        IIOMetadataNode root = new IIOMetadataNode(metaFormatName);
        IIOMetadataNode gce = new IIOMetadataNode("GraphicControlExtension");
        gce.setAttribute("disposalMethod", "none");
        gce.setAttribute("userInputFlag", "FALSE");
        gce.setAttribute("transparentColorFlag", "FALSE");
        gce.setAttribute("delayTime", Integer.toString(delay / 10));
        gce.setAttribute("transparentColorIndex", "0");
        root.appendChild(gce);

        IIOMetadataNode appExtensions = new IIOMetadataNode("ApplicationExtensions");
        IIOMetadataNode appExtension = new IIOMetadataNode("ApplicationExtension");
        appExtension.setAttribute("applicationID", "NETSCAPE");
        appExtension.setAttribute("authenticationCode", "2.0");
        int loop = loops ? 0 : 1;
        appExtension.setUserObject(new byte[]{0x1, (byte) (loop & 0xFF), 0x0});
        appExtensions.appendChild(appExtension);
        root.appendChild(appExtensions);

        imageMetaData.mergeTree(metaFormatName, root);

        gifWriter.setOutput(outputStream);
        gifWriter.prepareWriteSequence(null);
    }

    public void writeToSequence(RenderedImage img) throws IOException {
        javax.imageio.IIOImage frame = new javax.imageio.IIOImage(img, null, imageMetaData);
        gifWriter.writeToSequence(frame, imageWriteParam);
    }

    public void close() throws IOException {
        gifWriter.endWriteSequence();
    }
}