package lpk.imaging

import javax.swing.JFrame

/**
 * For showing a picture on the screen.
 * Don't edit this.
 */
abstract class PictureDisplayer {

    abstract fun createPicture() : Picture

    fun doLaunch() {
        val frame = JFrame("Imaging")
        frame.isUndecorated = true
        frame.add(PicturePanel(createPicture()))
        frame.pack()
        frame.isVisible = true
    }
}