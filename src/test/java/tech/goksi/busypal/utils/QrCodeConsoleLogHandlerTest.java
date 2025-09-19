package tech.goksi.busypal.utils;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.google.zxing.common.BitMatrix;
import it.auties.qr.QrTerminal;
import it.auties.whatsapp.api.QrHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.slf4j.LoggerFactory;
import tech.goksi.busypal.TestMemoryAppender;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

class QrCodeConsoleLogHandlerTest {

    private TestMemoryAppender appender;
    private QrHandler consoleLogHandler;

    @BeforeEach
    void setup() {
        appender = new TestMemoryAppender();
        consoleLogHandler = new QrCodeConsoleLogHandler();
        Logger logger = (Logger) LoggerFactory.getLogger(QrCodeConsoleLogHandler.class);
        logger.addAppender(appender);
        appender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        appender.start();
    }

    @Test
    void getInstance_shouldReturnSameInstance() {

        QrCodeConsoleLogHandler instance1 = QrCodeConsoleLogHandler.getInstance();
        QrCodeConsoleLogHandler instance2 = QrCodeConsoleLogHandler.getInstance();

        assertSame(instance1, instance2);
    }

    @Test
    void accept_shouldProcessQrCodeCorrectly() {
        try (MockedStatic<QrHandler> qrHandlerMocked = mockStatic(QrHandler.class);
             MockedStatic<QrTerminal> qrTerminalMocked = mockStatic(QrTerminal.class)) {
            String qrcode = "qrcode";
            BitMatrix bitMatrix = mock(BitMatrix.class);
            qrHandlerMocked.when(() -> QrHandler.createMatrix(eq(qrcode), eq(10), eq(0)))
                    .thenReturn(bitMatrix);
            qrTerminalMocked.when(() -> QrTerminal.toString(eq(bitMatrix), eq(true)))
                    .thenReturn(qrcode);
            consoleLogHandler.accept(qrcode);
            assertTrue(appender.contains("Please scan this qr code with whatsapp"));
            assertTrue(appender.contains("\nqrcode"));
        }
    }
}
