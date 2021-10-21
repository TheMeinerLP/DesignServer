package dev.themeinerlp.designserver.listener

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.client.j2se.MatrixToImageConfig
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minestom.server.event.server.ServerListPingEvent
import java.awt.Color
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.function.Consumer


class ServerPingListener : Consumer<ServerListPingEvent> {

    override fun accept(event: ServerListPingEvent) {
        val matrix: BitMatrix = MultiFormatWriter().encode(
            "https://www.twitch.tv/moulberry2", BarcodeFormat.QR_CODE, 64, 64,
            mapOf(EncodeHintType.MARGIN to 0)
        )
        val os = ByteArrayOutputStream()

        MatrixToImageWriter.writeToStream(
            matrix,
            "png",
            os,
            MatrixToImageConfig(
                NamedTextColor.RED.value(),
                Color(0,0,0,0).rgb
            )
        )
        val encoded = Base64.getEncoder().encode(os.toByteArray())
        val responseData = event.responseData
        responseData.protocol = Long.MAX_VALUE.toInt()
        responseData.favicon = "data:image/png;base64," + String(encoded)
        responseData.version = "§9DESIGN §cSERVER                                                                        "
        responseData.description = MiniMessage.get().parse(
            "<rainbow>Design Server - v1 </rainbow>"
        )
        event.responseData = responseData
    }
}