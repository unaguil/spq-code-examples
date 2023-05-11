package com.example;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;

public class ClientApp extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	Client client = ClientBuilder.newClient();

	final WebTarget appTarget = client.target("http://localhost:8080/myapp");
	final WebTarget usersTarget = appTarget.path("users");

	public ClientApp() {
		setSize(620, 480);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JButton getUsersButton = new JButton("Get Users");
		JPanel buttonsPanel = new JPanel();

		JButton deleteUserButton = new JButton("Delete User");

		buttonsPanel.add(getUsersButton);
		buttonsPanel.add(deleteUserButton);
		add(buttonsPanel, BorderLayout.SOUTH);

		final DefaultListModel<User> userListModel = new DefaultListModel<>();
		JList<User> userList = new JList<>(userListModel);

		JScrollPane listScrollPane = new JScrollPane(userList);
		add(listScrollPane, BorderLayout.WEST);

		getUsersButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				GenericType<List<User>> genericType = new GenericType<List<User>>() {};
				List<User> users = usersTarget.request(MediaType.APPLICATION_JSON).get(genericType);

				userListModel.clear();
				for (User user : users) {
					userListModel.addElement(user);
				}

			}

		});

		JPanel rightPanel = new JPanel();
		add(rightPanel, BorderLayout.EAST);

		JButton addUserButton = new JButton("Add user");
		rightPanel.add(addUserButton);

		final JTextField codeTextField = new JTextField("", 2);
		final JTextField nameTextField = new JTextField("", 10);
		final JTextField surnameTextField = new JTextField("", 10);

		rightPanel.add(codeTextField);
		rightPanel.add(nameTextField);
		rightPanel.add(surnameTextField);

		addUserButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		deleteUserButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}

		});

		setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new ClientApp();
			}
		});
	}
}