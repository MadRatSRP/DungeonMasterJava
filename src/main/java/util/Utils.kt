package util

import java.io.InputStream
import javax.swing.ImageIcon

class Utils {
    companion object {
        @JvmStatic
        fun getImageIconFromFile(pathToFile: String): ImageIcon {
            val inputStream: InputStream? = Utils::class.java.classLoader.getResourceAsStream(pathToFile)
            val resourceByteArray = inputStream?.readBytes()
            inputStream?.close()
            
            return ImageIcon(resourceByteArray)
        }
    }
}