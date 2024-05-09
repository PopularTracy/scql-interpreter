package com.kpi.scql.socket;

import com.sun.javacard.apduio.Apdu;
import com.sun.javacard.apduio.CadClientInterface;
import com.sun.javacard.apduio.CadDevice;
import com.sun.javacard.apduio.CadTransportException;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SmartCardConnector implements Closeable {

    private final byte[] SELECT_APPLET_COMMAND = new byte[]{(byte) 0x00, (byte) 0xA4, (byte) 0x04, (byte) 0x00};
    private final byte[] APPLET_AID = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x01, (byte) 0x02};
    private final byte Le = 0x7F;

    private String address = "localhost";
    private int port = 9025;

    private CadClientInterface cad;
    private Socket socket;

    public SmartCardConnector() {
    }

    public SmartCardConnector(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void openAndSelect() throws IOException, CadTransportException {

        System.out.println("Connecting to the SmartCard");
        socket = new Socket(address, port);
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        cad = CadDevice.getCadClientInstance(CadDevice.PROTOCOL_T1, is, os);

        System.out.println("Connected to the SmartCard in " + address + ":" + port);

        System.out.println("Powering up the SmartCard");
        // Powering up the card
        byte[] atr = cad.powerUp();
        System.out.println("ATR: " + Arrays.toString(atr));

        Apdu apduSelect = new Apdu();
        apduSelect.command = SELECT_APPLET_COMMAND;

        apduSelect.setDataIn(APPLET_AID, APPLET_AID.length);
        apduSelect.le = Le;

        System.out.println("Selecting the applet with AID:");
        System.out.println(Arrays.toString(APPLET_AID));

        cad.exchangeApdu(apduSelect);
        System.out.println(apduSelect);

        byte[] sws = apduSelect.getSw1Sw2();

        if (sws[0] == 0x90 && sws[1] == 0x00) {
            System.out.println("Applet selected");
        }
    }

    public void exchangeApdu(Apdu apdu) throws CadTransportException, IOException {
        if (cad == null) {
            throw new NullPointerException("Cad client interface is not initialized!");
        }

        if (apdu == null) {
            throw new NullPointerException("APDU is not initialized!");
        }

        cad.exchangeApdu(apdu);
        System.out.println(apdu);
        System.out.println("Status: " + apdu.getStatus());

        if (apdu.le != 0) {
            System.out.println("Response Le: " + Arrays.toString(apdu.getDataOut()));
            System.out.printf("Response Le decoded: " + new String(apdu.getDataOut(), StandardCharsets.UTF_8));
        }
    }

    public void powerDown() throws IOException, CadTransportException {
        if (cad == null) {
            throw new NullPointerException("Cad client interface is not initialized!");
        }

        cad.powerDown();
    }

    @Override
    public void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
    }
}
