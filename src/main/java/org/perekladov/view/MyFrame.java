package org.perekladov.view;

import org.perekladov.rep.IProductParser;
import org.perekladov.rep.site.ksk.ParserKsk;
import org.perekladov.service.ProductService;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MyFrame extends JFrame {
    ProductService productService = new ProductService();
    IProductParser parser = new ParserKsk();

    private File file1;
    private File file2;
    JLabel labe = new JLabel("Что то произошло");
    JDialog dialog = createDialog("Вакенца", true);

    private JLabel label = new JLabel("Введите url каталога КСК");
    private JButton button = new JButton("Поехали!");
    private JTextField textField = new JTextField();
    private JLabel labelNull = new JLabel("");
    private JLabel labelNull1 = new JLabel("");

    private JLabel label1 = new JLabel("Файл для обновления цен");
    private JButton button1 = new JButton("Выберите файл");
    private JButton button2 = new JButton("Обновить цены КСК");
    private JTextField textField1 = new JTextField();

    private JLabel label2 = new JLabel("Файл для получения новых цен");
    private JButton button3 = new JButton("Выберите файл");
    private JButton button4 = new JButton("Обновить цены Стройки");
    private JTextField textField2 = new JTextField();

    public MyFrame(){
        super("Parser");
        this.setBounds(300, 300, 800, 180);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3,4,5,20));
        container.add(label);
        container.add(textField);
        button.addActionListener(new ButtonListener());
        container.add(button);
        container.add(labelNull);



        container.add(label1);
        container.add(textField1);
        button1.addActionListener(new ButtonListener1());
        container.add(button1);
        button2.addActionListener(new ButtonListener2());
        container.add(button2);

        container.add(label2);
        container.add(textField2);
        button3.addActionListener(new ButtonListener3());
        container.add(button3);
        button4.addActionListener(new ButtonListener4());
        container.add(button4);



    }

    public static void main(String[] args) {
        MyFrame frame = new MyFrame();
        frame.setVisible(true);
    }

    class ButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            String url = textField.getText();
            if(url == null){
                textField.setText("Вы не ввели URL");
            } else {
                productService.createXlsxFileFromSiteByCategory(parser, url);
                dialog.setVisible(true);
            }
        }
    }

    class ButtonListener1 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileopen = new JFileChooser();
            fileopen.setFileFilter(new FileNameExtensionFilter("Excel .xlsx files", "xlsx"));
            int ret = fileopen.showDialog(null, "Выбрать файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                file1 = fileopen.getSelectedFile();
                textField1.setText(file1.getName());
            }
        }
    }

    class ButtonListener2 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            productService.refreshPricesFromSite(file1.getAbsolutePath(), parser);
            dialog.setVisible(true);
        }
    }

    class ButtonListener3 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileopen = new JFileChooser();
            fileopen.setFileFilter(new FileNameExtensionFilter("Excel .xlsx files", "xlsx"));
            int ret = fileopen.showDialog(null, "Выбрать файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                file2 = fileopen.getSelectedFile();
                textField2.setText(file2.getName());
            }
        }
    }

    class ButtonListener4 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            productService.refreshPricesFromXlsx(file2.getAbsolutePath(), file1.getAbsolutePath());
            dialog.setVisible(true);
        }
    }

    private JDialog createDialog(String title, boolean modal)
    {
        JDialog dialog = new JDialog(this, title, modal);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setLayout(new GridLayout());
        dialog.add(labe);
        dialog.setSize(210, 180);
        return dialog;
    }
}
