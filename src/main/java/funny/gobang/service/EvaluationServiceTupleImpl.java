package funny.gobang.service;

import funny.gobang.AppConstants;
import funny.gobang.model.ChessTuple;
import funny.gobang.model.Point;
import funny.gobang.model.TupleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static funny.gobang.AppConstants.BLACK;
import static funny.gobang.AppConstants.BOARD_SIZE;
import static funny.gobang.AppConstants.EMPTY;
import static funny.gobang.model.TupleType.*;

/**
 * Created by charlie on 2016/9/3.
 */
@Service("evaluateTuple")
public class EvaluationServiceTupleImpl implements EvaluationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationServiceTupleImpl.class);

    /*public static void main(String[] args) {
        int[][] board = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
                , {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
                , {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
                , {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
                , {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
                , {0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 1, 0, 0, 0, 0}
                , {0, 0, 0, 0, 0, 1, -1, 1, 0, 0, -1, 1, 0, 0, 0}
                , {0, 0, 0, 1, -1, -1, -1, -1, 1, 0, -1, 0, 0, 0, 0}
                , {0, 0, 0, 1, 1, 1, 1, -1, -1, -1, -1, 1, 0, 0, 0}
                , {0, 0, 0, 0, 1, 0, -1, 1, -1, 1, -1, 0, 0, 0, 0}
                , {0, 0, 0, 0, 0, 0, 0, -1, 1, 1, 1, -1, 0, 0, 0}
                , {0, 0, 0, 0, 0, 0, 1, 0, -1, 0, -1, 0, -1, 0, 0}
                , {0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 1, 0}
                , {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0}
                , {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        EvaluationService es = new EvaluationServiceTupleImpl();
        long result = es.evaluate(board, -1);
        System.out.println(result);
    }*/

    public long evaluate(int[][] board, int currentStone) {
        long result = 0L;
        List<ChessTuple> tuples = parseBoard(board);
        for (ChessTuple tuple : tuples) {
            TupleType type = toType(tuple);
            if (!type.equals(NONE)) {
                result += tuple.getStone() == currentStone ? type.getScore() : -type.getOpponentScore();
            }
        }
        return result;
    }

    private List<ChessTuple> parseBoard(int[][] board) {
        List<ChessTuple> tuples = new ArrayList<>();
        List<ChessTuple> lineTuples;
        // - east
        for (int x = 0; x < BOARD_SIZE; x++) {
            lineTuples = parseLine(board, new Point(x, 0), new Point(0, 1));
            tuples.addAll(lineTuples);
        }
        // | south
        for (int y = 0; y < BOARD_SIZE; y++) {
            lineTuples = parseLine(board, new Point(0, y), new Point(1, 0));
            tuples.addAll(lineTuples);
        }
        // \ south east
        for (int y = 0; y < BOARD_SIZE - 4; y++) {
            lineTuples = parseLine(board, new Point(0, y), new Point(1, 1));
            tuples.addAll(lineTuples);
        }
        // / south west
        for (int y = 4; y < BOARD_SIZE; y++) {
            lineTuples = parseLine(board, new Point(0, y), new Point(1, -1));
            tuples.addAll(lineTuples);
        }
        // / north east
        for (int y = 1; y < BOARD_SIZE - 4; y++) {
            lineTuples = parseLine(board, new Point(BOARD_SIZE - 1, y), new Point(-1, 1));
            tuples.addAll(lineTuples);
        }
        // \ north west
        for (int y = 4; y < BOARD_SIZE - 1; y++) {
            lineTuples = parseLine(board, new Point(BOARD_SIZE - 1, y), new Point(-1, -1));
            tuples.addAll(lineTuples);
        }
        return tuples;
    }

    private List<ChessTuple> parseLine(int[][] board, Point p, Point dir) {
        List<ChessTuple> tuples = new ArrayList<>();
        int i = p.getX();
        int j = p.getY();
        int stone = 0;
        int count = 0;
        int empty = 0;
        int head = 0;
        int tail = 0;
        StringBuilder tupleBuilder = new StringBuilder();
        while (i < BOARD_SIZE && j < BOARD_SIZE && i >= 0 && j >= 0) {
            if (board[i][j] == EMPTY) {
                if (stone == 0) {
                    head++;
                } else {
                    tail++;
                    if (tail == 2) {
                        for (int m = i + dir.getX(), n = j + dir.getY(); m < BOARD_SIZE && n < BOARD_SIZE && m >= 0 && n >= 0; m += dir.getX(), n += dir.getY()) {
                            if (board[m][n] != EMPTY) {
                                break;
                            }
                            tail++;
                            i = m;
                            j = n;
                        }
                        tuples.add(new ChessTuple(stone, count, empty, head, tail, tupleBuilder.toString()));
                        stone = 0;
                        count = 0;
                        empty = 0;
                        head = tail;
                        tail = 0;
                        tupleBuilder.setLength(0);
                    }
                }
            } else {
                if (stone == 0) {
                    stone = board[i][j];
                    count++;
                } else if (stone == board[i][j]) {
                    if (tail > 0) {
                        empty += tail;
                        for (int m = 0; m < tail; m++) {
                            tupleBuilder.append(0);
                        }
                    }
                    count++;
                    tail = 0;
                } else {
                    tuples.add(new ChessTuple(stone, count, empty, head, tail, tupleBuilder.toString()));
                    tupleBuilder.setLength(0);
                    stone = board[i][j];
                    count = 1;
                    empty = 0;
                    head = tail;
                    tail = 0;
                }
                tupleBuilder.append(1);
            }
            i += dir.getX();
            j += dir.getY();
        }
        if (stone != 0) {
            tuples.add(new ChessTuple(stone, count, empty, head, tail, tupleBuilder.toString()));
        }
        return tuples;
    }

    private TupleType toType(ChessTuple tuple) {
        switch (tuple.getCount()) {
            case 5:
                switch (tuple.getEmpty()) {
                    case 0:
                        return FIVE;
                    case 1:
                        switch (tuple.getTuple()) {
                            case "101111":
                                if (tuple.getStone() == BLACK) {
                                    if (tuple.getTail() > 0) {
                                        return BLOCKED_FOUR;
                                    } else if (tuple.getHead() > 3) {
                                        return BLOCKED_ONE;
                                    }
                                } else if (tuple.getTail() > 0) {
                                    return FOUR;
                                } else {
                                    return BLOCKED_FOUR;
                                }
                                break;
                            case "111101":
                                if (tuple.getStone() == BLACK) {
                                    if (tuple.getHead() > 0) {
                                        return BLOCKED_FOUR;
                                    } else if (tuple.getTail() > 3) {
                                        return BLOCKED_ONE;
                                    }
                                } else if (tuple.getHead() > 0) {
                                    return FOUR;
                                } else {
                                    return BLOCKED_FOUR;
                                }
                                break;
                            case "110111":
                                if (tuple.getStone() == BLACK) {
                                    if (tuple.getTail() > 1) {
                                        return BLOCKED_THREE;
                                    } else if (tuple.getHead() > 2) {
                                        return BLOCKED_TWO;
                                    }
                                } else {
                                    return BLOCKED_FOUR;
                                }
                                break;
                            case "111011":
                                if (tuple.getStone() == BLACK) {
                                    if (tuple.getHead() > 1) {
                                        return BLOCKED_THREE;
                                    } else if (tuple.getTail() > 2) {
                                        return BLOCKED_TWO;
                                    }
                                } else {
                                    return BLOCKED_FOUR;
                                }
                                break;
                        }
                        break;
                    case 2:
                        switch (tuple.getTuple()) {
                            case "1010111":
                            case "1110101":
                            case "1011011":
                            case "1101101":
                            case "1011101":
                                return BLOCKED_FOUR;
                            case "1101011":
                                if (tuple.getStone() == BLACK) {
                                    if (tuple.getHead() > 0 || tuple.getTail() > 0) {
                                        return BLOCKED_THREE;
                                    }
                                } else if (tuple.getHead() > 0 || tuple.getTail() > 0) {
                                    return JUMP_THREE;
                                } else {
                                    return BLOCKED_THREE;
                                }
                                break;
                        }
                        break;
                    case 3:
                        switch (tuple.getTuple()) {
                            case "10101011":
                                if (tuple.getStone() == BLACK || tuple.getTail() == 0) {
                                    return BLOCKED_THREE;
                                } else {
                                    return JUMP_THREE;
                                }
                            case "11010101":
                                if (tuple.getStone() == BLACK || tuple.getHead() == 0) {
                                    return BLOCKED_THREE;
                                } else {
                                    return JUMP_THREE;
                                }
                            case "10101101":
                                if (tuple.getStone() == BLACK) {
                                    if (tuple.getHead() > 0 && tuple.getTail() == 0) {
                                        return BLOCKED_TWO;
                                    } else if (tuple.getTail() > 0) {
                                        return BLOCKED_THREE;
                                    }
                                } else {
                                    return THREE;
                                }
                                break;
                            case "10110101":
                                if (tuple.getStone() == BLACK) {
                                    if (tuple.getTail() > 0 && tuple.getHead() == 0) {
                                        return BLOCKED_TWO;
                                    } else if (tuple.getHead() > 0) {
                                        return BLOCKED_THREE;
                                    }
                                } else {
                                    return THREE;
                                }
                                break;
                        }
                        break;
                    case 4:
                        //101010101
                        return THREE;
                }
                break;
            case 4:
                switch (tuple.getEmpty()) {
                    case 0:
                        if (tuple.getHead() > 0 && tuple.getTail() > 0) {
                            return FOUR;
                        } else if (tuple.getHead() == 0 && tuple.getTail() > 0
                                || tuple.getHead() > 0 && tuple.getTail() == 0) {
                            return BLOCKED_FOUR;
                        }
                        break;
                    case 1:
                        return BLOCKED_FOUR;
                    case 2:
                        switch (tuple.getTuple()) {
                            case "101011":
                                if (tuple.getStone() == BLACK) {
                                    if (tuple.getTail() > 0) {
                                        return BLOCKED_THREE;
                                    } else if (tuple.getHead() > 1) {
                                        return BLOCKED_TWO;
                                    }
                                } else if (tuple.getTail() > 0) {
                                    return JUMP_THREE;
                                } else {
                                    return BLOCKED_THREE;
                                }
                                break;
                            case "110101":
                                if (tuple.getStone() == BLACK) {
                                    if (tuple.getHead() > 0) {
                                        return BLOCKED_THREE;
                                    } else if (tuple.getTail() > 1) {
                                        return BLOCKED_TWO;
                                    }
                                } else if (tuple.getHead() > 0) {
                                    return JUMP_THREE;
                                } else {
                                    return BLOCKED_THREE;
                                }
                                break;
                            case "101101":
                                if (tuple.getStone() == BLACK) {
                                    if (tuple.getHead() > 0 || tuple.getTail() > 0) {
                                        return BLOCKED_THREE;
                                    }
                                } else if (tuple.getHead() > 0 || tuple.getTail() > 0) {
                                    return JUMP_THREE;
                                } else {
                                    return BLOCKED_THREE;
                                }
                                break;
                        }
                        break;
                    case 3:
                        //[+_+_+_+] can be double blocked four
                        return THREE;
                }
                break;
            case 3:
                switch (tuple.getEmpty()) {
                    case 0:
                        if (tuple.getHead() > 0 && tuple.getTail() > 0) {
                            if (tuple.getHead() + tuple.getTail() > 2) {
                                return THREE;
                            } else {
                                return BLOCKED_THREE;
                            }
                        } else if (tuple.getHead() == 0 && tuple.getTail() > 1
                                || tuple.getHead() > 1 && tuple.getTail() == 0) {
                            return BLOCKED_THREE;
                        }
                        break;
                    case 1:
                        if (tuple.getHead() > 0 && tuple.getTail() > 0) {
                            return JUMP_THREE;
                        } else if (tuple.getHead() == 0 && tuple.getTail() > 0
                                || tuple.getHead() > 0 && tuple.getTail() == 0) {
                            return BLOCKED_THREE;
                        }
                        break;
                    case 2:
                        //10101,10011,11001
                        return BLOCKED_THREE;
                    /*case 3:
                        //101001,100101

                        break;
                    case 4:
                        //1001001
                        if(tuple.getHead()==0&&tuple.getTail()==0){
                            return TupleType.BLOCKED_TWO;
                        } else if(tuple.getHead()==0||tuple.getTail()==0){
                            return TupleType.BIG_JUMP_TWO;
                        }*/
                }
                break;
            case 2:
                if (tuple.getHead() > 0 && tuple.getTail() > 0 && tuple.getEmpty() + tuple.getHead() + tuple.getTail() >= 4) {
                    if (tuple.getEmpty() == 0) {
                        return TWO;
                    } else if (tuple.getEmpty() == 1) {
                        return JUMP_TWO;
                    } else {
                        return BIG_JUMP_TWO;
                    }
                } else if (tuple.getEmpty() + tuple.getHead() + tuple.getTail() >= 3) {
                    if (tuple.getHead() == 0 || tuple.getTail() == 0
                            || tuple.getEmpty() + tuple.getHead() + tuple.getTail() == 3) {
                        return BLOCKED_TWO;
                    }
                }
                break;
            case 1:
                if (tuple.getHead() + tuple.getTail() > 4) {
                    if (tuple.getHead() > 0 && tuple.getTail() > 0) {
                        return ONE;
                    } else if (tuple.getHead() == 0 || tuple.getTail() == 0) {
                        return BLOCKED_ONE;
                    }
                }
                break;
        }
        return NONE;
    }

}

