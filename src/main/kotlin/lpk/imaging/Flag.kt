package lpk.imaging

import java.awt.Color

fun main() {
    Flag().doLaunch()
}

class Flag : PictureDisplayer() {

    val magenta = Color(255, 0, 255)

    fun createPictureOfFlag(): Picture {
        val height = 2000//Use your screen
        val width = 3000//resolution here!
        val pixels = Array<Array<Color>>(height) {
            Array<Color>(width) {
                magenta
            }
        }
        return Picture(pixels)
    }

    //Don't change anything below here.
    override fun createPicture(): Picture {
        return createPictureOfFlag()
    }
}