package lpk.imaging

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun loadPictureFromFile(imageFile: File): Picture {
    val image = ImageIO.read(imageFile)
    val width = image.width
    val height = image.height
    val pixels = Array<Array<Color>>(height) {
        row ->
        Array<Color>(width) {
            column ->
            Color(image.getRGB(column, row))
        }
    }
    return Picture(pixels)
}

class Picture(val pixels: Array<Array<Color>>) {
    fun height(): Int {
        return pixels.size
    }

    fun width(): Int {
        return pixels[0].size
    }

    fun pixelByRowColumn(row: Int, column: Int): Color {
        return pixels[row][column]
    }

    fun cropTo(rowAt: Int, columnAt: Int, h: Int, w: Int): Picture {
        val cropArray = Array<Array<Color>>(h) {
            row ->
            Array<Color>(w) {
                column ->
                pixelByRowColumn(rowAt + row, columnAt + column)
            }
        }
        return Picture(cropArray)
    }

    fun chopIntoSquares(sideLength: Int): Array<Array<Picture>> {
        val resultRows = height() / sideLength
        val resultColumns = width() / sideLength
        val result = Array<Array<Picture>>(resultRows) {
            blockRow ->
            Array<Picture>(resultColumns) {
                blockColumn ->
                cropTo(blockRow * sideLength, blockColumn * sideLength, sideLength, sideLength)
            }
        }
        return result
    }

    fun averageColor(): Color {
        var totalRed = 0
        var totalGreen = 0
        var totalBlue = 0
        for (row in 0..height() - 1) {
            for (column in 0..width() - 1) {
                val pixel = pixelByRowColumn(row, column)
                totalRed = totalRed + pixel.red
                totalGreen = totalGreen + pixel.green
                totalBlue = totalBlue + pixel.blue
            }
        }
        val count = height() * width()
        return Color(totalRed / count, totalGreen / count, totalBlue / count)
    }

    fun scaleDown(factor: Int): Picture {
        //First break it into a double array
        //of factor-by-factor square sub-pictures.
        val blocks = chopIntoSquares(factor)
        //Initialise a pixel array using the blocks.
        val newPixels = Array<Array<Color>>(blocks.size) {
            blocksRow ->
            Array<Color>(blocks[blocksRow].size) {
                blocksColumn ->
                //Each pixel is the average color of the
                //corresponding block.
                blocks[blocksRow][blocksColumn].averageColor()
            }
        }
        return Picture(newPixels)
    }

    fun saveTo(file: File) {
        val image = BufferedImage(width(), height(), BufferedImage.TYPE_INT_RGB)
        val width = width()
        val height = height()
        for (row in 0..height - 1) {
            for (column in 0..width - 1) {
                image.setRGB(column, row, pixelByRowColumn(row, column).rgb)
            }
        }
        ImageIO.write(image, "png", file)
    }

    fun flipInVerticalAxis(): Picture {
        val pixels = Array<Array<Color>>(height()) {
            row ->
            Array<Color>(width()) {
                column ->
                pixelByRowColumn(row, width() - 1 - column)
            }
        }
        return Picture(pixels)
    }

    fun flipInHorizontalAxis(): Picture {
        val pixels = Array<Array<Color>>(height()) {
            row ->
            Array<Color>(width()) {
                column ->
                pixelByRowColumn(height() - 1 - row, column)
            }
        }
        return Picture(pixels)
    }

    fun transform(pixelTransformation: (Color) -> (Color)): Picture {
        val transformed = Array<Array<Color>>(height()) {
            row ->
            Array<Color>(width()) {
                column ->
                val pixel = pixelByRowColumn(row, column)
                pixelTransformation(pixel)
            }
        }
        return Picture(transformed)
    }

    fun transformByPosition(pixelTransformation: ((Int), (Int)) -> (Color)): Picture {
        val transformed = Array<Array<Color>>(height()) {
            row ->
            Array<Color>(width()) {
                column ->
                pixelTransformation(row, column)
            }
        }
        return Picture(transformed)
    }

    fun darkBorder(): Picture {
        val borderWidth = 35
        val darkSides = { row: Int, column: Int
            ->
            val originalPixel = pixelByRowColumn(row, column)
            val isInBorder = row < borderWidth || row > height() - borderWidth || column < borderWidth || width() - column < borderWidth
            if (isInBorder) {
                originalPixel.darker().darker()
            } else {
                originalPixel
            }
        }
        return transformByPosition(darkSides)
    }
}