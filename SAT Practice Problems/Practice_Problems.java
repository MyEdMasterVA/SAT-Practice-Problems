import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * The purpose of the software is to help users practice and learn through a quiz format.
 * It presents one problem at a time, lets the user submit an answer, and then provides immediate feedback on whether the answer was correct or not.
 * If correct, the user earns points and sees their total points. 
 * If incorrect, the user sees the correct answer and the steps to solve the problem, along with their running total of points.
 *
 * @Barath Balaji
 * @06/02/24
 */

public class Practice_Problems implements ActionListener {
    private int currentProblemIndex = 0;
    private int score = 0;
    private JLabel problemLabel;
    private JTextField answerField;
    private JLabel feedbackLabel;
    private JLabel scoreLabel;
    private JFrame frame;
    private JPanel panel;
    private ButtonGroup answerGroup;
    private ArrayList<JRadioButton> answerButtons;
    private JButton submitButton;

    private ArrayList<Problem> problems;

    public Practice_Problems() {
        problems = loadProblems();

        frame = new JFrame();
        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);

        problemLabel = new JLabel(problems.get(currentProblemIndex).getQuestion());
        feedbackLabel = new JLabel("");
        scoreLabel = new JLabel("Score: 0");

        answerGroup = new ButtonGroup();
        answerButtons = new ArrayList<>();

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(0, 1));
        panel.add(problemLabel);

        // Add answer choices or text field to the panel
        if (problems.get(currentProblemIndex).isMultipleChoice()) {
            for (String choice : problems.get(currentProblemIndex).getChoices()) {
                JRadioButton radioButton = new JRadioButton(choice);
                answerGroup.add(radioButton);
                answerButtons.add(radioButton);
                panel.add(radioButton);
            }
        } else {
            answerField = new JTextField(10);
            panel.add(answerField);
        }

        panel.add(submitButton);
        panel.add(feedbackLabel);
        panel.add(scoreLabel);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Practice Problems");
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Practice_Problems();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Problem currentProblem = problems.get(currentProblemIndex);
        String userAnswer = currentProblem.isMultipleChoice() ? getSelectedButtonText(answerGroup) : answerField.getText();

        if (userAnswer != null && userAnswer.equals(currentProblem.getAnswer())) {
            score += 100;
            feedbackLabel.setText("Feedback: Correct! You've earned 100 points.");
        } else {
            feedbackLabel.setText("Feedback: Incorrect. The correct answer is: " + currentProblem.getAnswer() + ". " + currentProblem.getExplanation());
        }
        scoreLabel.setText("Score: " + score);

        currentProblemIndex++;
        if (currentProblemIndex < problems.size()) {
            updateProblem();
        } else {
            problemLabel.setText("Quiz Completed! Final Score: " + score);
            disableAnswerInputs();
        }
    }

    private void updateProblem() {
        problemLabel.setText(problems.get(currentProblemIndex).getQuestion());
        answerGroup.clearSelection();
        panel.removeAll();

        panel.add(problemLabel);
        if (problems.get(currentProblemIndex).isMultipleChoice()) {
            answerButtons.clear();
            for (String choice : problems.get(currentProblemIndex).getChoices()) {
                JRadioButton radioButton = new JRadioButton(choice);
                answerGroup.add(radioButton);
                answerButtons.add(radioButton);
                panel.add(radioButton);
            }
        } else {
            answerField = new JTextField(10);
            panel.add(answerField);
        }

        panel.add(submitButton);
        panel.add(feedbackLabel);
        panel.add(scoreLabel);
        panel.revalidate();
        panel.repaint();
    }

    private void disableAnswerInputs() {
        if (problems.get(currentProblemIndex).isMultipleChoice()) {
            for (JRadioButton button : answerButtons) {
                button.setEnabled(false);
            }
        } else {
            answerField.setEnabled(false);
        }
    }

    private String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return null;
    }

    private ArrayList<Problem> loadProblems() {
        ArrayList<Problem> problems = new ArrayList<>();
        problems.add(new Problem("1/17] \nWhich of the following is not equal to 5/9?", "30/45", "Every other choice is equal to 0.555 (5 repeating) except for 30/45 = 0.666 (6 repeating)",
                new String[]{"10/18", "20/36", "30/45", "35/63", "50/90"}, true));
        problems.add(new Problem("2/17] \nIn which of the following choices would the symbol > create an incorrect statement?", "1/4 3/10", "Write a fraction as a decimal.\n1/4 = 0.25 < 3/10 = 0.3",
                new String[]{"1/4 3/10", "2/3 1/2", "4/7 5/9", "7/8 3/4", "5/6 7/9"}, true));
        problems.add(new Problem("3/17] \n(17 - 3) / 7 - 2 (-4 - 8) =", "26", "Use the correct order of operations\n(17 - 3) / 7 - 2(-12) = 2 + 24 = 26",
                new String[]{"-20", "-2", "26", "31", "34"}, true));
        problems.add(new Problem("4/17] \n(3/4) / (3/8) =", "2", "Multiply by the reciprocal.\n(3/4)/(3/8) = (3/4) * (8/3) = (3 * 8) / (4 * 3) = 2",
                new String[]{"9/32", "1/2", "32/9", "2", "3/2"}, true));
        problems.add(new Problem("5/17] \nWhat is the next term in the sequence: 2, 8, 32, 128 …?", "512", "Multiply by 4 to find the next term in the sequence. Therefore, 128 * 4 = 512",
                null, false));
        problems.add(new Problem("6/17] \nIf 3^x+2 = 81, then x =", "2", "Use the rules of exponents. 81 = 3^4 so 3^4 = 3^(x+2). Therefore, x+2 = 4 → x = 2",
                new String[]{"5", "4", "3", "2", "1"}, true));
        problems.add(new Problem("7/17] \nWhich of the following has the smallest result?", "5/8 + 1/2 - 5/4", "Using the calculator you can add each set of fractions and get the following results. \n2/3 + 5/6 - 1/2 = 1\n3/4 - 3/2 + 2/3 = -0.0833.\n5/8 + 1/2 - 5/4 = -0.125\n4/7 - 9/14 + 5/2 ≈ 2.42857\n3/5 + 7/5 + 2/5 = 2.4",
                new String[]{"2/3 + 5/6 - 1/2", "3/4 - 3/2 + 2/3", "5/8 + 1/2 - 5/4", "4/7 - 9/14 + 5/2", "3/5 + 7/5 + 2/5"}, true));
        problems.add(new Problem("8/17] \nWhat is the difference between the largest and smallest number in the set below? {3/7, 5/8, 7/9, 2/5}", "17/45", "Use your calculator. Divide the numerator of each fraction by the denominator to find the decimal equivalent. You will be able to determine that 7/9 is the largest number and 2/5 is the smallest number.\n(7/9) - (2/5) = (35/45) - (18/45) = (17/45)",
                new String[]{"11/56", "11/72", "9/40", "1/35", "17/45"}, true));
        problems.add(new Problem("9/17] \nSet X = even integers and Set Y = odd integers. Therefore X ∩ Y =", "Empty set", "When two sets have nothing in common, we refer to their intersection as the empty set. There are two appropriate ways to denote the empty set X ∩ Y = { } or X ∩ Y = ⌀",
                new String[]{"Prime numbers", "Integers", "Empty set", "Composite numbers", "Whole numbers"}, true));
        problems.add(new Problem("10/17] \nWhich of the following number sets has the property that the sum of any two numbers in the set is also in the set? I. Even integers II. Odd integers III. Composite numbers", "I", "Correct. The sum of two even numbers is always an even number.\nNot correct because the sum of two odd integers is always an even number. For example, 7 + 5 = 12, which is even\nNot correct because there are several examples in which the sum of two composite numbers is a prime number. For example, 20 + 9 = 29.",
                new String[]{"I", "II", "III", "I and II", "I and III"}, true));
        problems.add(new Problem("11/17] \n(3/5 + 1/3) / 1 2/5 = \n\n8\t8\n5\t5\na\tb\n+4\t+4\n24\t36\n\nWhat is the value of b - a", "12", "Because all the numbers being added in both columns are the same except for the a and b. Therefore, the difference in the two sums must be the difference between a and b.\nTherefore, b - a = 36 - 24 = 12",
                null, false));
        problems.add(new Problem("13/17] \nThe first term in a geometric sequence is 3 and the 4th term is 81. What is the 10th term of the sequence?", "59,049", "a_n = a_1 * r^(n-1) → a_4 = 81 = 3 * r^(4 - 1) → 81 = 3 * r^3 → 27 = r^3 → 3 = r\nTherefore,\na10 = 3 * 3(10-1) = 3 * 39 = 59,049",
                new String[]{"177,147", "59,049", "19,683", "6,561", "2,187"}, true));
        problems.add(new Problem("14/17] \nMultiplying a number by 4/5 and then dividing by 2/5 is the same as doing what to the number?", "Multiplying by 2", "Divide 4/5 by 2/5\n(4/5) / (2/5) = (4/5) * (5/2) = 2\nThe result is the same as multiplying by 2",
                new String[]{"Dividing by 4", "Multiplying by 1/2", "Multiplying by 2", "Dividing by 5", "Multiplying by 5"}, true));
        problems.add(new Problem("15/17] \nWhich of the following choices could be equal to set Z if\n\tX = {2,5,6,7,9} and Y = {2,5,7}\n\tX ∪ Y ∪ Z = {1, 2,3,4,5,6,7,8,9}\n\nI. Z = {1,3,4,8}\nII. Z = {1,2,3,4,8}\nIII. Z = {1,3,4,5,8}\n\nI\nII\nIII\nI and II\nI and III", "I", "The union of the three sets must equal {1,2,3,4,5,6,7,8,9}\nTherefore Z must equal {1,3,4,8}\nIt cannot contain any elements already found in either X or Y.\nBoth II and III have elements that already exist in X or Y.",
                new String[]{"I", "II", "III", "I and II", "I and III"}, true));
        //problems.add(new Problem("16/17] \nFor which of the following values of x is x^3 < x not a true statement?",Answer: C Find the cube (x^3) of each answer choice. (-3)3 = -27 < -3 (-2)3 = -8 < -2 (-1/2)3 = -⅛ > -½. The cube is not less than the answer  (1/3)3 = 1/27 < ⅓ (1/2)3 = ⅛ < ½
                //new String[]{"-3", "-2", "-1/2", "1/3", "1/2"}, true));
        //problems.add(new Problem("17/17] The first term in a geometric sequence is 2, and the common ratio is 3. The first term in an arithmetic sequence is 3, and the common difference is 3. Let set X be the set containing the first six terms of the geometric sequence and set Y be the set containing the first six terms of the arithmetic sequence. What is the sum of the elements in X ∩ Y?, Answer: 24 Geometric sequence: X = {2,6,18,54,162,456} Arithmetic sequence: Y = {3,6,9,12,15,18} X ∩ Y = {6,18} → 6 + 18 = 24, null, false));
        return problems;
    }

    
    private class Problem {
        private String question;
        private String answer;
        private String explanation;
        private String[] choices;
        private boolean isMultipleChoice;

        public Problem(String question, String answer, String explanation, String[] choices, boolean isMultipleChoice) {
            this.question = question;
            this.answer = answer;
            this.explanation = explanation;
            this.choices = choices;
            this.isMultipleChoice = isMultipleChoice;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswer() {
            return answer;
        }

        public String getExplanation() {
            return explanation;
        }

        public String[] getChoices() {
            return choices;
        }

        public boolean isMultipleChoice() {
            return isMultipleChoice;
        }
    }
}
