package project;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

class Calculator3 implements ActionListener {

    JFrame frame;
    JTextField textfield;
    JButton[] numberButtons = new JButton[10];
    JButton[] functionButtons = new JButton[9];
    JButton addButton, subButton, mulButton, divButton;
    JButton decButton, equButton, delButton, clrButton, negButton;
    JPanel panel;
    //Here we defined a global Font that can be used by all.
    Font myFont = new Font("Times New Romans", Font.ITALIC, 30);

    Stack3<Double> stack = new Stack3<Double>(10);
    Stack3<Character> operatorStack = new Stack3<Character>(9);

    Calculator3() {

        frame = new JFrame("Section 2, group 4 Simple Calculator project");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Also Terminates it on the console.
        frame.setSize(420, 550);// Setting the Frame Size.
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);// To align it on the center.
        frame.setLayout(null);

        textfield = new JTextField();// Creating Object from JTextField Class.
        textfield.setBounds(50, 25, 300, 50);
        textfield.setFont(myFont);// Uses the font we declared above.
        textfield.setEditable(false);// This prevents the the user from editing on the textfield.
        // Creating Object from JButton Class.
        addButton = new JButton("+");
        subButton = new JButton("-");
        mulButton = new JButton("*");
        divButton = new JButton("/");
        decButton = new JButton(".");
        equButton = new JButton("=");
        delButton = new JButton("Del");
        clrButton = new JButton("Clr");
        negButton = new JButton("(-)");
        
        functionButtons[0] = addButton;
        functionButtons[1] = subButton;
        functionButtons[2] = mulButton;
        functionButtons[3] = divButton;
        functionButtons[4] = decButton;
        functionButtons[5] = equButton;
        functionButtons[6] = delButton;
        functionButtons[7] = clrButton;
        functionButtons[8] = negButton;

        for (int i = 0; i < 9; i++) {
            functionButtons[i].addActionListener(this);
            functionButtons[i].setFont(myFont);
            functionButtons[i].setFocusable(true);
        }

        for (int i = 0; i < 10; i++) {
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].addActionListener(this);
            numberButtons[i].setFont(myFont);
            numberButtons[i].setFocusable(true);//This focuses which button is touched.
        }
        //These 3 buttons need their own sizes, since they are a bit larger from the others.
        negButton.setBounds(50, 430, 100, 50);
        delButton.setBounds(150, 430, 100, 50);
        clrButton.setBounds(250, 430, 100, 50);

        panel = new JPanel();
        //panel.setLocationRelativeTo(null);
        panel.setBounds(50, 100, 300, 300);
        panel.setLayout(new GridLayout(4, 4, 10, 10));
        panel.setBackground(Color.BLACK);//Sets the background color for the panel.
        
        //Here We add the numbers and the signs according to the rows of the panel.
        //And we know that the panel starts adding on the new row after 4 args.
        panel.add(numberButtons[1]);
        panel.add(numberButtons[2]);
        panel.add(numberButtons[3]);
        panel.add(addButton);
        panel.add(numberButtons[4]);
        panel.add(numberButtons[5]);
        panel.add(numberButtons[6]);
        panel.add(subButton);
        panel.add(numberButtons[7]);
        panel.add(numberButtons[8]);
        panel.add(numberButtons[9]);
        panel.add(mulButton);
        panel.add(decButton);
        panel.add(numberButtons[0]);
        panel.add(equButton);
        panel.add(divButton);

        frame.add(panel);
        frame.add(negButton);
        frame.add(delButton);
        frame.add(clrButton);
        frame.add(textfield);
        frame.setVisible(true);
    }

    public static void main(String[] args) {

    	Calculator3 calc = new Calculator3();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
        	for (int i = 0; i < 10; i++) {
                if (e.getSource() == numberButtons[i]) {
                    textfield.setText(textfield.getText().concat(String.valueOf(i)));
                }
            }
        }catch(Exception NumberFormatException){
        	JOptionPane.showMessageDialog(null, "Please enter number first.");
        }
        if (e.getSource() == decButton) {
            textfield.setText(textfield.getText().concat("."));
        }
        try {
        	if (e.getSource() == addButton || e.getSource() == subButton || e.getSource() == mulButton || e.getSource() == divButton) {
                char op = ((JButton)e.getSource()).getText().charAt(0);
                	while (!operatorStack.isEmpty() && precedence(operatorStack.peek()) >= precedence(op)) {
                        calculate();
                    }
                operatorStack.push(op);
                stack.push(Double.parseDouble(textfield.getText()));
                textfield.setText("");
            }//Catches the error thrown when arthimethic operation are clicked before numbers. 
        } catch (Exception NumberFormatException) {
        	char op = ((JButton)e.getSource()).getText().charAt(0);
        	if(precedence(operatorStack.peek()) <= precedence(op)) {
        		JOptionPane.showMessageDialog(null, "Please, input number first.");
        	}
        }    
        if (e.getSource() == equButton) {
            stack.push(Double.parseDouble(textfield.getText()));
            while (!operatorStack.isEmpty()) {
                calculate();
            }
            textfield.setText(String.valueOf(stack.pop()));
        }
        if (e.getSource() == clrButton) {
            textfield.setText("");
            //stack.clear();
            //operatorStack.clear();
        }
        if (e.getSource() == delButton) {
            String text = textfield.getText();
            if (!text.isEmpty()) {  // Check if textfield is not empty
                textfield.setText(text.substring(0, text.length() - 1));
            }
        }
        if (e.getSource() == negButton) {
            double temp = Double.parseDouble(textfield.getText());
            temp *= -1;
            textfield.setText(String.valueOf(temp));
        }
    }

    private int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }

    private void calculate() {
            double num2 = stack.pop();
            double num1 = stack.pop();
            char op = operatorStack.pop();
            double result;
            switch (op) {
                case '+':
                    result = num1 + num2;
                    break;
                case '-':
                    result = num1 - num2;
                    break;
                case '*':
                    result = num1 * num2;
                    break;
                case '/':
                    result = num1 / num2;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid operator");
            }
            stack.push(result);
    }
}

class Stack3<T> {
/* The <T> syntax is used to define a type parameter,
 * which acts as a placeholder for the actual data type
 * that will be used when the class is instantiated.
 */
    private double maxSize;
    private int top;
    private T[] itemArray;

    @SuppressWarnings("unchecked")
    public Stack3(double size) {
        this.maxSize = size;
        this.top = -1;
        this.itemArray = (T[]) new Object[(int) maxSize];
    }

    public void push(T value) {
        if (top < maxSize - 1) {
            itemArray[++top] = value;
        } else {
            System.out.println("Stack is full.");
        }
    }

    public T pop() {
        if (top >= 0) {
            return itemArray[top--];
        } else {
            System.out.println("Stack is empty.");
            return null;
        }
    }

    public T peek() {
        if (top >= 0) {
            return itemArray[top];
        } else {
            System.out.println("Stack is empty.");
            return null;
        }
    }

    public boolean isEmpty() {
        return top == -1;
    }
}

