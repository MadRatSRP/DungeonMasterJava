package util

import org.apache.commons.io.IOUtils
import java.io.IOException
import javax.swing.ImageIcon

class Utils {
    companion object {
        @JvmStatic
        fun getImageIconFromFile(pathToFile: String): ImageIcon {
            val inputStream = Utils::class.java.classLoader.getResourceAsStream(pathToFile)
            val resourceByteArray = IOUtils.toByteArray(inputStream)
    
            inputStream?.close()
            
            return ImageIcon(resourceByteArray)
        }
    }
}