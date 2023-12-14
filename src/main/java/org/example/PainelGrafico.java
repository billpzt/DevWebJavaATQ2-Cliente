//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.example;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PainelGrafico {
    JFrame frame = new JFrame("CLIENTE");
    JPanel panel = new JPanel();
    JTextField inputFieldCodigo = new JTextField(10);
    JLabel labelCodigo = new JLabel("Código:");

    public PainelGrafico() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 500);
        frame.setLocationRelativeTo(null);

        panel.setLayout(new GridLayout(4, 2));

        inputFieldCodigo.setFont(new Font("Arial", Font.PLAIN, 30));
        labelCodigo.setFont(new Font("Arial", Font.PLAIN, 30));

        panel.add(labelCodigo);
        panel.add(inputFieldCodigo);

        inputFieldCodigo.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                send();
            }
            public void removeUpdate(DocumentEvent e) {
                send();
            }
            public void insertUpdate(DocumentEvent e) {
                send();
            }

            public void send() {
                String path = "http://localhost:8080/api";
                String codigoInserido = PainelGrafico.this.inputFieldCodigo.getText();
                String content = "{ \"codigo\": \" " + codigoInserido + " \" }";

                try {
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.writeBytes(content);
                    out.flush();
                    out.close();

                    int responseCode = connection.getResponseCode();
                    System.out.println("Code: " + responseCode);
                    if (responseCode != 200) {
                        System.out.println("Got an unexpected response code");
                    }

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while((line = in.readLine()) != null) {
                        System.out.println(line);
                        JsonElement jsonElement = JsonParser.parseString(line);
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        String resposta = jsonObject.get("ack").toString().trim();

                        if (resposta.equals("\"1\"")) {
                            panel.setBackground(Color.green);
                        } else if (resposta.equals("\"0\"")) {
                            panel.setBackground(Color.red);
                        } else {
                            panel.updateUI();
                        }

                        Timer timer = new Timer(3000, e -> {
                            panel.setBackground(Color.LIGHT_GRAY);
                            PainelGrafico.this.inputFieldCodigo.setText("");
                        });
                        timer.setRepeats(false);
                        timer.start();

                    }

                    in.close();
                } catch (MalformedURLException var14) {
                    var14.printStackTrace();
                } catch (IOException var15) {
                    var15.printStackTrace();
                } catch (NumberFormatException var16) {
                    PainelGrafico.this.inputFieldCodigo.setText("Erro: Entrada invalida");
                }
            }
        });

//        String path = "http://localhost:8080/api";
//
//        try {
//            String codigoInserido = PainelGrafico.this.inputFieldCodigo.getText();
//            String content = "{ \"codigo\": \" " + codigoInserido + " \" }";
//
//            URL url = new URL(path);
//            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setDoOutput(true);
//            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
//            out.writeBytes(content);
//            out.flush();
//            out.close();
//
//            int responseCode = connection.getResponseCode();
//            System.out.println("Code: " + responseCode);
//            if (responseCode != 200) {
//                System.out.println("Got an unexpected response code");
//            }
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//
//            String line;
//            while((line = in.readLine()) != null) {
//                System.out.println(line);
//                JsonElement jsonElement = JsonParser.parseString(line);
//                JsonObject jsonObject = jsonElement.getAsJsonObject();
//                String resposta = jsonObject.get("ack").toString();
////                PainelGrafico.this.inputFieldCodigo.setText(jsonObject.get("Codigo").getAsString());
//                if (resposta.equals("1")) {
//                    panel.setBackground(Color.green);
//                    Thread.sleep(3000);
//                    panel.setBackground(Color.lightGray);
//                } else if (resposta.equals("0")) {
//                    panel.setBackground(Color.red);
//                    Thread.sleep(3000);
//                    panel.setBackground(Color.lightGray);
//                }
//            }
//
//            in.close();
//        } catch (MalformedURLException var14) {
//            var14.printStackTrace();
//        } catch (IOException var15) {
//            var15.printStackTrace();
//        } catch (NumberFormatException var16) {
//            PainelGrafico.this.inputFieldCodigo.setText("Erro: Entrada invalida");
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        String buttonLabel = "Sair";

        JButton button = new JButton(buttonLabel);
        button.setFont(new Font("Arial", Font.PLAIN, 30));
        panel.add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}
