package de.adorsys.rasperry.arduino;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier("/dev/ttyUSB0");
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			CommPort commPort = portIdentifier.open(App.class.getName(), 2000);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				(new Thread(new SerialReader(serialPort.getInputStream()))).start();
				(new Thread(new SerialWriter(serialPort.getOutputStream()))).start();

			} else {
				System.out.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

	public static class SerialReader implements Runnable {
		InputStream in;

		public SerialReader(InputStream in) {
			this.in = in;
		}

		@Override
		public void run() {
			try {
				int b;
				StringBuilder builder = new StringBuilder();
				while ((b = this.in.read()) > -1) {
					if (b == '>') {
						builder = new StringBuilder();
					} else if (b == '<') {
						System.out.println("Message: " + builder.toString());
					} else {
						builder.append((char) b);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static class SerialWriter implements Runnable {
		OutputStream out;

		public SerialWriter(OutputStream out) {
			this.out = out;
		}


		@Override
		public void run() {
			try {
				int counter = 1;
				while (true) {
					counter = counter % 2;
					String send = ">" + counter + "1111<";
					System.out.println("SEND: " + send);
					out.write(send.getBytes());
					out.flush();
					Thread.sleep(10);
					counter++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
