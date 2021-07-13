
package controller;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import puzzle.GUI;

public class Management {

    private GUI pz;
    private int size = 3;
    private JButton[][] matrix;
    private int move = 0;
    private int timer = 0;
    private Timer time;

    public Management(GUI pz) {
        this.pz = pz;
        addBtn();
        timeCount();
    }

    public void newGame() { //Hàm reset tất cả các chỉ số để có game mới
        int cf = JOptionPane.showConfirmDialog(null, "New game ?", "Hi", JOptionPane.YES_NO_OPTION);
        if (cf == JOptionPane.YES_OPTION) {
            move = 0;
            pz.getLbMove().setText("0");
            pz.getLbTime().setText("0");
            timer = 0;
            addBtn();
            time.restart(); //Reset time
        }
    }

    public void timeCount() { //Hàm đếm time và hiện thị ra label
        time = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer++; //Bộ đếm time
                pz.getLbTime().setText(timer + "");
            }
        });
        time.restart(); //Reset time
    }

    public void addBtn() {
        size = pz.getCbSize().getSelectedIndex() + 3; //Lay gia tri cua size theo comboBox tuong ung
        pz.getScreen().removeAll();
//        timeCount();
        pz.getScreen().setLayout(new GridLayout(size, size, 10, 10)); //set hang, cot va khoang cach giua chung
        pz.getScreen().setPreferredSize(new Dimension(60 * size, 60 * size)); //set kich thuoc cho panel tuy thuoc vao size
        matrix = new JButton[size][size]; //Khoi tạo matrix voi size tuong ung
        for (int i = 0; i < size; i++) { //2 vòng for để đẩy button lên panel
            for (int j = 0; j < size; j++) {
                JButton btn = new JButton(i * size + j + 1 + "");//Tạo button với giá trị text tương ứng
                matrix[i][j] = btn; //Add button với giá trị text tương ứng vào matrix
                pz.getScreen().add(btn);//Đẩy từng Button vs giá trị text tương ứng lên panel
                btn.addActionListener(new ActionListener() { //Add actionListener cho Button
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!checkWin()) { //TH chưa Win thì vẫn tiếp tục game
                            movement(btn);
                            move++; //Đếm số lần move Button
                            pz.getLbMove().setText(move + "");
                        }
                        if (checkWin()) { //TH Win game thì dừng time và thông báo ra màn hình
                            time.stop();
                            JOptionPane.showMessageDialog(pz, "Win");
                        }
                    }
                });
            }
        }
        matrix[size - 1][size - 1].setText(""); //Set cho button cuối với giá trị text là rỗng
        randomMatrix();
        pz.pack(); //Giup frame co giãn theo panel bên trong

    }

    public boolean checkWin() { //Hàm check vị trí các Button để thông báo Win game
        if (matrix[size - 1][size - 1].getText().equals("")) { //Check button cuối phải rỗng mới hợp lệ
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (i == size - 1 && j == size - 1) { //Duyet den het Button cuối để return
                        return true;
                    }
                    if (!matrix[i][j].getText().equals(i * size + j + 1 + "")){ //Check xem có button nào sai vị trí de return false
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public Point getEmpty() { //Duyet matrix va tra ve button rong can tim duoi dang Point
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j].getText().equals("")) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    public void randomMatrix() { //Tron cac button lai voi nhau
        for (int k = 0; k < 100; k++) {
            int n = new Random().nextInt(4) + 1; //Tao ham random va random gia tri vao n 1-2-3-4
            Point p = getEmpty(); //Lay duoc toa do button rong
            int i = p.x;
            int j = p.y;
            if (n == 1) { //Button Up
                if (i > 0) { //De tranh TH Button rong da o hàng tren cung (i=0)
                    matrix[i][j].setText(matrix[i - 1][j].getText());
                    matrix[i - 1][j].setText("");
                }
            }
            if (n == 2) { //Button Down
                if (i < size - 1) { //De tránh TH Button rỗng đã ở hàng dưới cùng
                    matrix[i][j].setText(matrix[i + 1][j].getText());
                    matrix[i + 1][j].setText("");
                }
            }
            if (n == 3) { //Button left
                if (j > 0) { //Để tránh TH Button rỗng đã ở cột ngoài cùng bên trái
                    matrix[i][j].setText(matrix[i][j - 1].getText());
                    matrix[i][j - 1].setText("");
                }
            }
            if (n == 4) {
                if (j < size - 1) { //Để tránh TH Button rỗng đã ở cột ngoài cùng bên phải
                    matrix[i][j].setText(matrix[i][j + 1].getText());
                    matrix[i][j + 1].setText("");
                }
            }
        }
    }

    public void movement(JButton btn) {
        Point empty = getEmpty();
        int i = empty.x;
        int j = empty.y;
        int m = 0, n = 0;
        for (int a = 0; a < size; a++) { //Vòng lặp để lấy được tọa độ Button truyền vào
            for (int b = 0; b < size; b++) {
                if (btn.equals(matrix[a][b])) {
                    m = a;
                    n = b;
                }
            }
        }

        if (empty.x == m && Math.abs(empty.y - n) == 1) { //TH cùng i và j hơn kém nhau 1
            matrix[i][j].setText(btn.getText());
            btn.setText("");
        }

        if (empty.y == n && Math.abs(empty.x - m) == 1) { //TH cùng j và i hơn kém nhau 1
            matrix[i][j].setText(btn.getText());
            btn.setText("");
        }
    }

}
