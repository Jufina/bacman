package server;

import java.io.*;

/**
 * Created by Julia on 24.05.2015.
 */
public class GameEngine {
    private byte[][] map;
    private byte[] teamsScore;

    private void tryToMove(int i, int j, int toI, int toJ) {
        int teamId = (map[i][j]%2 == 0?1:0);
        if (map[toI][toJ] == 6 || map[toI][toJ] == 0) {
            if (map[toI][toJ] == 6) {
                teamsScore[teamId]++;
            }
            map[toI][toJ] = map[i][j];
            map[i][j] = 0;
        }
    }

    public byte[] rebuildingMap(byte[] movepl) {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (map[i][j] >=1 && map[i][j] <= 4) {
                    switch (movepl[map[i][j] - 1]) {
                        case 0: // up
                            tryToMove(i, j, i - 1, j);
                            break;
                        case 1: // right
                            tryToMove(i, j, i, j + 1);
                            break;
                        case 2: // down
                            tryToMove(i, j, i + 1, j);
                            break;
                        case 3: // left
                            tryToMove(i, j, i, j - 1);
                            break;
                    }
                }
            }
        }
        return getResult();
    }
    public byte[] getResult() {
        byte[] res = new byte[20*20 + 2];
        for (int i=0; i<20; i++) {
            for (int j=0; j<20; j++) {
                res[i*20 + j] = map[i][j];
            }
        }
        res[20*20] = teamsScore[0];
        res[20*20 + 1] = teamsScore[1];
        return res;
    }
    public GameEngine(int numOfMap) {
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream("map" + numOfMap + ".txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        map = new byte[20][20];
        try {
            for (int i = 0; i < 20; i++) {
                String str = reader.readLine();
                for (int j = 0; j < 20; j++) {
                    map[i][j] = (byte) (str.charAt(j) - '0');
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        teamsScore = new byte[2];
    }
}
