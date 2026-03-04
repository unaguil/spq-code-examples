package donations.client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import donations.util.WindowManager;
import donations.util.DonationException;
import donations.serialization.Donation;
import donations.serialization.DonationInfo;

public class Donor implements ActionListener, Runnable {
	private JFrame frame;
	private JButton buttonEnd;
	private JButton buttonDonate;
	private JTextField donationField;
	private JTextField totalField;
	private JLabel messageLabel;

	private Client client;
	private WebTarget webTarget;

	private Thread thread;
	private final AtomicBoolean running = new AtomicBoolean(false);

	public Donor(String hostname, String port) {
		client = ClientBuilder.newClient();
		webTarget = client.target(String.format("http://%s:%s/rest", hostname, port));
		
		this.buttonDonate = new JButton("Donate");
		this.buttonDonate.addActionListener(this);
		this.buttonEnd = new JButton("End Process");
		this.buttonEnd.addActionListener(this);
		this.donationField = new JTextField(10);
		this.totalField = new JTextField(10);
		this.totalField.setEnabled(false);
		this.messageLabel = new JLabel("Welcome to the REST Donor Client");
		this.messageLabel.setOpaque(true);
		this.messageLabel.setForeground(Color.yellow);
		this.messageLabel.setBackground(Color.gray);

		JPanel panelDonativos = new JPanel();
		panelDonativos.add(new JLabel("Donation: "));
		panelDonativos.add(this.donationField);
		panelDonativos.add(new JLabel("Total Amount: "));
		panelDonativos.add(this.totalField);

		JPanel panelBotones = new JPanel();
		panelBotones.add(this.buttonDonate);
		panelBotones.add(this.buttonEnd);

		this.frame = new JFrame("Donor: REST Client");
		this.frame.setSize(400, 125);
		this.frame.setResizable(false);		
		this.frame.getContentPane().add(panelDonativos, "North");
		this.frame.getContentPane().add(panelBotones);
		this.frame.getContentPane().add(this.messageLabel, "South");
		this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		WindowManager.middleLeft(this.frame);
		this.frame.setVisible(true);

		thread = new Thread(this);
		thread.start();
	}

	public void actionPerformed(ActionEvent e) {
		JButton target = (JButton) e.getSource();
		if (target == this.buttonEnd) {
			this.stop();
			System.exit(0);
		}

		if (target == this.buttonDonate) {
			try {
				int donation = Integer.parseInt(this.donationField.getText());
				this.messageLabel.setText("Sending donation ...");
				this.sendDonation(donation);
				this.messageLabel.setText("Donation of " + donation + " already sent...");
			} catch (NumberFormatException exc) {
				this.messageLabel.setText(" # Error sending donation. Donation must be an integer.");
			} catch (DonationException exc) {
				this.messageLabel.setText(" # Error sending donation. ");
			}
		}
	}
	
	public void sendDonation(int quantity) throws DonationException {
		WebTarget donationsWebTarget = webTarget.path("collector/donations");
		Invocation.Builder invocationBuilder = donationsWebTarget.request(MediaType.APPLICATION_JSON);
		
		Donation donation = new Donation(quantity);
		Response response = invocationBuilder.post(Entity.entity(donation, MediaType.APPLICATION_JSON));
		if (response.getStatus() != Status.OK.getStatusCode()) {
			throw new DonationException("" + response.getStatus());
		}
	}

	public DonationInfo getDonationInfo() throws DonationException {
		WebTarget donationsWebTarget = webTarget.path("collector/donations");
		Invocation.Builder invocationBuilder = donationsWebTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		if (response.getStatus() == Status.OK.getStatusCode()) {
			return response.readEntity(DonationInfo.class);
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
				DonationInfo donationInfo = getDonationInfo();
				this.totalField.setText(Integer.toString(donationInfo.getTotal()));
			} catch (DonationException e) {
				System.out.println(e.getMessage());
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

		new Donor(hostname, port);
	}
}