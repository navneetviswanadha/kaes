//package symantec.itools.lang;

//  02/15/97    RKM Added isSolaris
//  10/16/97    DS  Added isCaseSensitive
// 07/05/19     NV Added isWindowsXP, isWindowsVista, isWindows7, isWindows10 to support more recent operating systems

/**
 * This class identifies the operating system that a program is running under.
 * <p>
 * It does not need to be instantiated by the user.
 *
 * @version 1.0, Nov 26, 1996
 *
 * @author  Symantec
 */
public final class OS
{
    private static boolean isWindows95 = false;
    private static boolean isWindowsNT = false;
    private static boolean isMacintosh = false;
    private static boolean isSolaris   = false;
    private static boolean isCaseSensitive = false;
    private static boolean isWindowsXP = false;
    private static boolean isWindowsVista = false;
    private static boolean isWindows7 = false;
    private static boolean isWindows10 = false;

    static
    {
        String s;

        s = System.getProperty("os.name");

        if(s.equals("Windows NT"))
        {
            isWindowsNT = true;
        }
        else if(s.equals("Windows 95"))
        {
            isWindows95 = true;
        } else if(s.equals("Windows XP")){
            isWindowsXP = true;
        } else if(s.equals("Windows Vista")){
            isWindowsVista = true;
        }else if (s.equals("Windows 7")){
            isWindows7 = true;
        } else if(s.equals("Windows 10")){
            isWindows10 = true;
        }
        else if (s.equals("Macintosh") ||
                 s.equals("macos") ||       //Applet Viewer
                 s.equals("Mac OS") ||      //Netscape
                 s.equals("MacOS"))         //Internet Explorer
        {
            isMacintosh = true;
        }
        else if (s.equals("SunOS") ||
                 s.equals("Solaris"))
        {
            isSolaris       = true;
            isCaseSensitive = true;
        }
    }

    private OS()
    {
    }

    /**
     * Returns true if running under the Windows 95 or Windows NT operating system.
     */
    public static boolean isWindows()
    {
        return (isWindows95() || isWindowsNT() || isWindowsXP() || isWindowsVista() || isWindows7()|| isWindows10());
    }

    /**
     * Returns true if running under the Windows XP operating system.
     */
    public static boolean isWindowsXP()
    {
        return (isWindowsXP);
    }
    
    /**
     * Returns true if running under the Windows Vista operating system.
     */
     
    public static boolean isWindowsVista()
    {
        return (isWindowsVista);
    }

    /**
     * Returns true if running under the Windows 7 operating system.
     */
    public static boolean isWindows7()
    {
        return (isWindows7);
    }

    /**
     * Returns true if running under the Windows 10 operating system.
     */
    public static boolean isWindows10()
    {
        return (isWindows10);
    }

    
    
    /**
     * Returns true if running under the Windows 95 operating system.
     */
    public static boolean isWindows95()
    {
        return (isWindows95);
    }

    /**
     * Returns true if running under the Windows NT operating system.
     */
    public static boolean isWindowsNT()
    {
        return (isWindowsNT);
    }

    /**
     * Returns true if running under the Macintosh operating system.
     */
    public static boolean isMacintosh()
    {
        return (isMacintosh);
    }

    /**
     * Returns true if running under the Solaris operating system.
     */
    public static boolean isSolaris()
    {
        return (isSolaris);
    }
         
    /**
    * Returns true if file system is case sensitive.
    */
    public static boolean isCaseSensitive()
    {
        return (isCaseSensitive);
    }
}
