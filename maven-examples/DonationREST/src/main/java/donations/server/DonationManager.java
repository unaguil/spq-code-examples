package donations.server;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import donations.serialization.DonationInfo;
import donations.util.DonationException;
import donations.util.WindowManager;

public class DonationManager implements ActionListener, Runnable {

	private JFrame frame;
	private JButton buttonEnd;
	private JLabel donation;
	private JLabel total;
	private JLabel message;

	private Client client;
	private WebTarget webTarget;

	private Thread thread;
	private final AtomicBoolean running = new AtomicBoolean(false);

	public DonationManager(String hostname, String port) {
		client = ClientBuilder.newClient();
		webTarget = client.target(String.format("http://%s:%s/rest", hostname, port));

		this.buttonEnd = new JButton("End Donation Process");
		this.buttonEnd.addActionListener(this);
		this.donation = new JLabel("0");
		this.total = new JLabel("0");
		this.message = new JLabel("REST Donation Manager running...");
		this.message.setOpaque(true);
		this.message.setForeground(Color.yellow);
		this.message.setBackground(Color.gray);

		JPanel panelSuperior = new JPanel();
		panelSuperior.add(new JLabel("Last Donation: "));
		panelSuperior.add(this.donation);
		panelSuperior.add(new JLabel("Total Donation: "));
		panelSuperior.add(this.total);

		JPanel panelBoton = new JPanel();
		panelBoton.add(this.buttonEnd);

		this.frame = new JFrame("Donation Collector: RMI Manager");
		this.frame.setSize(475, 140);
		this.frame.setResizable(false);		
		this.frame.getContentPane().add(panelSuperior, "North");
		this.frame.getContentPane().add(panelBoton);
		this.frame.getContentPane().add(this.message, "South");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		WindowManager.middleCenter(this.frame);
		this.frame.setVisible(true);

		thread = new Thread(this);
		thread.start();
	}

	public void actionPerformed(ActionEvent e) {
		JButton target = (JButton)e.getSource();
		if (target == this.buttonEnd) {
			this.stop();
			System.exit(0);
		}
	}

	public DonationInfo getDonationInfo() throws DonationException {
		WebTarget donationsWebTarget = webTarget.path("collector/donations");
		Invocation.Builder invocationBuilder = donationsWebTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		if (response.getStatus() == Status.OK.getStatusCode()) {
			DonationInfo donationInfo = response.readEntity(DonationInfo.class);
			return donationInfo;
		} else {
			throw new DonationException("" + response.getStatus());
		}
	}

	public void run() {
		running.set(true);
		while(running.get()) {
			try { 
				Thread.sleep(2000);
				System.out.println("Obtaining data from server...");
				try {
					DonationInfo donationInfo = getDonationInfo();
					this.donation.setText(Integer.toString(donationInfo.getLast()));
					this.total.setText(Integer.toString(donationInfo.getTotal()));
				} catch (DonationException e) {
					System.out.println(e.getMessage());
				}
            } catch (InterruptedException e){ 
                Thread.currentThread().interrupt();
                System.out.println("Thread was interrupted, Failed to complete operation");
            }
		}
	}

	public void stop() {
		this.running.set(false);
	}

	public static void main(String[] args) {
		String hostname = args[0];
		String port = args[1];

		DonationManager donationManager = new DonationManager(hostname, port);
	}
}