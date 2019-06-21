//package symantec.itools.awt.image;import java.awt.Color;import java.awt.image.RGBImageFilter;import java.awt.image.ColorModel;import java.awt.image.DirectColorModel;import java.lang.IllegalArgumentException;// Written by Levi Brown and Micheal Hopkins 1.1, July 8, 1997./** * An Image filter to use for fading an Image towards a specified Color by a specified percent. * @version 1.1, July 8, 1997 * @author  Symantec */public class FadeFilter extends RGBImageFilter{	/**	 * Constructs a default FadeFilter.	 * By default the Image is faded 50% towards Color.lightGray.	 * @see #FadeFilter(double)	 * @see #FadeFilter(java.awt.Color)	 * @see #FadeFilter(double, java.awt.Color)	 * @see #setPercent	 * @see #setFadeToColor	 */	public FadeFilter()	{		this(0.50, Color.lightGray);	}	/**	 * Constructs a FadeFilter.	 * By default the Image is faded towards Color.lightGray.	 * @param percent the percent to fade towards the specified color	 * @see #FadeFilter()	 * @see #FadeFilter(java.awt.Color)	 * @see #FadeFilter(double, java.awt.Color)	 * @see #setFadeToColor	 */	public FadeFilter(double percent)	{		this(percent, Color.lightGray);	}	/**	 * Constructs a FadeFilter.	 * By default the Image is faded 50%.	 * @param fadeToColor the color to fade to	 * @see #FadeFilter()	 * @see #FadeFilter(double)	 * @see #FadeFilter(double, java.awt.Color)	 * @see #setPercent	 */	public FadeFilter(Color fadeToColor)	{		this(0.50, fadeToColor);	}	/**	 * Constructs a FadeFilter.	 * @param percent the percent to fade towards the specified color	 * @param fadeToColor the color to fade to	 * @see #FadeFilter()	 * @see #FadeFilter(double)	 * @see #FadeFilter(java.awt.Color)	 */	public FadeFilter(double percent, Color fadeToColor)	{		canFilterIndexColorModel = true;		try		{			setPercent(percent);		}		catch (IllegalArgumentException exc)		{			System.err.println("FadeFilter: Invalid parameter value passed to constructor:");			System.err.println("     " + percent + " is not a valid percentage value. It should be <= 1 && >= 0");			System.err.println("     Defaulting to 0.50.");			try { setPercent(0.50); } catch (IllegalArgumentException exc2) {}		}		try		{			setFadeToColor(fadeToColor);		}		catch (NullPointerException exc)		{			System.err.println("FadeFilter: Null parameter value passed to constructor:");			System.err.println("     fadeToColor is null");			System.err.println("     Defaulting to Color.lightGray.");			setFadeToColor(Color.lightGray);		}	}	/**	 * Sets the color to fade to when filtering.	 * @param fadeTo the color to fade to	 * @see #getFadeToColor	 */	public void setFadeToColor(Color fadeTo)	{		to_r = fadeTo.getRed();		to_g = fadeTo.getGreen();		to_b = fadeTo.getBlue();	}	/**	 * Gets the color used to fade to when filtering.	 * @return the color used to fade to	 * @see #setFadeToColor	 */	public Color getFadeToColor()	{		return new Color(to_r,	to_g, to_b);	}	/**	 * Sets the percentage to fade when filtering.	 * @param percent the percentage to fade	 * @exception IllegalArgumentException	 * if the specified percentage value is unacceptable	 * @see #getPercent	 */	public void setPercent(double percent) throws IllegalArgumentException	{		GeneralUtils.checkValidPercent(percent);		this.percent = percent;	}	/**	 * Gets the percentage to fade when filtering.	 * @return the percentage to fade	 * @see #setPercent	 */	public double getPercent()	{		return percent;	}	/**	 * Filters an RGB value using the current fade settings.	 * @param x unused	 * @param y unused	 * @param rgb the rgb value to fade	 * @return the faded rgb value	 */	public int filterRGB( int x, int y, int rgb )	{		DirectColorModel cm = (DirectColorModel)ColorModel.getRGBdefault();		int alpha	= cm.getAlpha(rgb);		int from_r	= cm.getRed(rgb);		int from_g	= cm.getGreen(rgb);		int from_b	= cm.getBlue(rgb);		int r, g, b;		if (from_r > to_r)			r = to_r + (int)((from_r - to_r)* (1 - percent));		else			r = to_r - (int)((to_r - from_r)* (1 - percent));		if (from_g > to_r)			g = to_g + (int)((from_g - to_g)* (1 - percent));		else			g = to_g - (int)((to_g - from_g)* (1 - percent));		if (from_b > to_b)			b = to_b + (int)((from_b - to_b)* (1 - percent));		else			b = to_b - (int)((to_b - from_b)* (1 - percent));		alpha	= alpha << 24;		r		= r		<< 16;		g		= g		<< 8;		return alpha | r | g | b;	}	/**	 * The percentage to fade when filtering.	 * @see #getPercent	 * @see #setPercent	 */	protected double percent;	/**	 * The red value of the color to fade to when filtering.	 * @see #getFadeToColor	 * @see #setFadeToColor	 */	protected int to_r;	/**	 * The green value of the color to fade to when filtering.	 * @see #getFadeToColor	 * @see #setFadeToColor	 */	protected int to_g;	/**	 * The blue value of the color to fade to when filtering.	 * @see #getFadeToColor	 * @see #setFadeToColor	 */	protected int to_b;}
