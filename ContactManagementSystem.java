import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ContactManagementSystem {
    private List<Contact> contacts;
    private JFrame frame;
    private JList<String> contactList;
    private DefaultListModel<String> listModel;

    public ContactManagementSystem() {
        contacts = new ArrayList<>();
        listModel = new DefaultListModel<>();
        contactList = new JList<>(listModel);

        loadContacts();

        frame = new JFrame("Contact Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 400);

        JButton addButton = new JButton("Add Contact");
        JButton editButton = new JButton("Edit Contact");
        JButton deleteButton = new JButton("Delete Contact");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addContact();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editContact();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteContact();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        frame.add(contactList, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void addContact() {
        String name = JOptionPane.showInputDialog(frame, "Enter Name:");
        if (name != null && !name.isEmpty()) {
            String phoneNumber = JOptionPane.showInputDialog(frame, "Enter Phone Number:");
            Contact contact = new Contact(name, phoneNumber);
            contacts.add(contact);
            listModel.addElement(contact.toString());
            saveContacts();
        }
    }

    private void editContact() {
        int selectedIndex = contactList.getSelectedIndex();
        if (selectedIndex != -1) {
            String newName = JOptionPane.showInputDialog(frame, "Edit Name:", contacts.get(selectedIndex).getName());
            if (newName != null && !newName.isEmpty()) {
                String newPhoneNumber = JOptionPane.showInputDialog(frame, "Edit Phone Number:", contacts.get(selectedIndex).getPhoneNumber());
                Contact newContact = new Contact(newName, newPhoneNumber);
                contacts.set(selectedIndex, newContact);
                listModel.set(selectedIndex, newContact.toString());
                saveContacts();
            }
        }
    }

    private void deleteContact() {
        int selectedIndex = contactList.getSelectedIndex();
        if (selectedIndex != -1) {
            contacts.remove(selectedIndex);
            listModel.remove(selectedIndex);
            saveContacts();
        }
    }

    private void loadContacts() {
        try (BufferedReader reader = new BufferedReader(new FileReader("contacts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    Contact contact = new Contact(parts[0], parts[1]);
                    contacts.add(contact);
                    listModel.addElement(contact.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveContacts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("contacts.txt"))) {
            for (Contact contact : contacts) {
                writer.write(contact.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ContactManagementSystem();
            }
        });
    }
}

class Contact {
    private String name;
    private String phoneNumber;

    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return name + "," + phoneNumber;
    }
}
