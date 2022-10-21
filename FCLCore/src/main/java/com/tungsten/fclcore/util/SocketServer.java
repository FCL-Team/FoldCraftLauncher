package com.tungsten.fclcore.util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;

public class SocketServer {

	private final DatagramPacket packet;
	private final DatagramSocket socket;
	private final Listener listener;
	private final String ip;
	private final int port;

	private boolean isReceiving = false;
	private Object result;

	public interface Listener {
		void onReceive(SocketServer server, String msg);
	}

	public SocketServer(String ip, int port, Listener listener) throws UnknownHostException, SocketException {
		this.listener = listener;
		this.ip = ip;
		this.port = port;
		byte[] bytes = new byte[1024];
		packet = new DatagramPacket(bytes, bytes.length);
		socket = new DatagramSocket(port, InetAddress.getByName(ip));
	}

	public DatagramPacket getPacket() {
		return packet;
	}

	public DatagramSocket getSocket() {
		return socket;
	}

	public Listener getListener() {
		return listener;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public void start() throws IOException {
		if (packet == null || socket == null) {
			return;
		}
		Logging.LOG.log(Level.INFO, "Socket server " + ip + ":" + port + " start!");
		isReceiving = true;
		while(isReceiving) {
			socket.receive(packet);
			String receiveMsg = new String(packet.getData(), 0, packet.getLength());
			listener.onReceive(this, receiveMsg);
		}
	}

	public void send(String msg) throws IOException {
		socket.connect(new InetSocketAddress(ip, port));
		byte[] data = msg.getBytes();
		DatagramPacket packet = new DatagramPacket(data, data.length);
		socket.send(packet);
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Object getResult() {
		return result;
	}

	public void stop() {
		isReceiving = false;
		socket.close();
		Logging.LOG.log(Level.INFO, "Socket server " + ip + ":" + port + " stopped!");
	}

}