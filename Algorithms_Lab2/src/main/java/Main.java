import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.DataOutput;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {

        Charts.createAnalogChart("Phase A", 0);
        Charts.createAnalogChart("Phase B", 1);
        Charts.createAnalogChart("Phase C", 2);

        Charts.createDiscreteChart("Flag", 0);

        TreatmentData inD = new TreatmentData();
        inD.start();


    }
}
