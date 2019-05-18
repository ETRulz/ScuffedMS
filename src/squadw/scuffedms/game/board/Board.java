package squadw.scuffedms.game.board;

import squadw.scuffedms.game.tile.Mine;
import squadw.scuffedms.game.tile.Tile;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Board {
    private int size;
    private int diff;
    private Tile[][] board;

    public Board(int size, int diff) {
        this.size = size;
        this.diff = diff;
        board = new Tile[this.size][this.size];
        initBoard();
        tileMouseListener();
    }

    public int getSize() {
        return size;
    }

    public Tile[][] getBoard() {
        return board;
    }

    private void checkForBombs(int x, int y) {
        int mines = 0;
        int xMax = x+1;
        int yMax = y+1;
        int xMin = x-1;
        int yMin = y-1;

        if (y == size-1) yMax = size-1;
        if (x == size-1) xMax = size-1;
        if (x == 0) xMin = 0;
        if (y == 0) yMin = 0;

        for (int k = xMin; k <= xMax; k++)
            for (int l = yMin; l <= yMax; l++)
                if (board[k][l] instanceof Mine) mines++;

        board[x][y].setNumBombs(mines);
    }

    private void tileMouseListener() {
        for (Tile[] t1: board)
            for (Tile t2 : t1) {
                t2.getButton().addMouseListener(new MouseAdapter() {
                    boolean pressed;

                    @Override
                    public void mousePressed(MouseEvent e) {
                        pressed = true;
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (pressed) {
                            if (t2.getTileState() == Tile.MARKED && SwingUtilities.isRightMouseButton(e)) t2.setClosed();
                            else if (SwingUtilities.isRightMouseButton(e) && t2.getTileState() != Tile.OPENED) t2.setMarked();
                            else t2.setOpened();
                            t2.setImage();
                            pressed = false;
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        pressed = false;
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        pressed = true;
                    }
                });
            }
    }

    private void initBoard() {
        Random r = new Random();
        int n = (size * size) * (diff * 2 - 1) / 8;
        int x;
        int y;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new Tile();
            }
        }

        for (int i = 0; i < n; i++) {
            do {
                x = r.nextInt(size);
                y = r.nextInt(size);
                board[x][y] = new Mine();
            } while (!(board[x][y] instanceof Mine));
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                checkForBombs(i, j);
            }
        }
    }
}
