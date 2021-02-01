package lpk.imaging

import java.awt.Graphics
import java.awt.Dimension
import java.awt.image.BufferedImage
import javax.swing.JPanel

/**
 * Does the actual drawing of a picture.
 * Don't edit this.
 */
class PicturePanel(private val picture: Picture) : JPanel() {
    override fun getPreferredSize(): Dimension {
        return Dimension(picture.width(), picture.height())
    }

    override fun paintComponent(graphics: Graphics) {
        super.paintComponent(graphics)

        val width = picture.width()
        val height = picture.height()
        val image = BufferedImage(width, height,BufferedImage.TYPE_INT_RGB )
        for (column in 0 until width) {
            for (row in 0 until height) {
                val awtColor = picture.pixelByRowColumn(row, column)
                image.setRGB(column, row, awtColor.rgb)
            }
        }
        graphics.drawImage(image, 0, 0, width, height, null)
    }
}