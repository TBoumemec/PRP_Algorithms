import java.awt.*;
import java.util.Vector;
import java.awt.geom.*;


public class ExperimentChart {

        // Constructor
        ExperimentChart( int x, int y, int width, int height )
        {
            m_X = x;
            m_Y = y;
            m_Width = width;
            m_Height = height;

            m_Plots = new Vector();
            m_Colors = new Vector();
            m_Strokes = new Vector();

            m_MaximumValue = 0.0f;

        }


        // Add a plot
        public void Add( double[] plot, Color color, Stroke stroke )
        {
            m_Plots.add( plot );
            m_Colors.add( color );
            m_Strokes.add( stroke );

            // Update maximum value is necessary
            for ( int i = 0; i < plot.length; i++ )
            {
                if ( plot[ i ] > m_MaximumValue )
                {
                    m_MaximumValue = plot[ i ];
                    //System.out.println( "Updated maximum value to " + m_MaximumValue );
                }

            }

        }


        // Plot
        public static void Plot(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

            // Draw the surrounding box; clear the inside of the box
            g2.setColor( Color.black );
            // Draw the plots
            g2.draw(new Line2D.Double(0, 0, 1, 1));

        }

        // Data members
        private int  m_X;
        private int  m_Y;
        private int  m_Width;
        private int  m_Height;
        private Vector  m_Plots;
        private Vector  m_Colors;
        private Vector  m_Strokes;
        private double  m_MaximumValue;

    }
