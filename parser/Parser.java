package parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import model.rules.Condition;
import model.board.Coordinate;
import model.piece.PieceBehavior;
import model.piece.PieceType;
import model.piece.PieceTypeID;
import model.piece.Player;
import model.rules.MovementPattern;
import model.rules.Property;

public class Parser {

    public static final String DOES = "does";
    public static final String ONLY_DOES = "onlyDoes";
    public static final Character PIECE_DECLARATOR = '%';
    public static final Character LAYOUT_DECLARATOR = '?';
    public static final Character VECTOR_DECLARATOR = '$';
    public static final Character COORDINATE_DECLARATOR = '@';
    public static final Character CONDITION_DECLARATOR = '#';
    public static final Character PROPERTY_DECLARATOR = '!';
    public static final Character ASSIGNMENT_OPERATOR = '=';
    public static final Character DOT_OPERATOR = '.';
    public static final Character LEFT_BRACKET = '[';
    public static final Character RIGHT_BRACKET = ']';
    public static final Character LEFT_PAREN = '(';
    public static final Character RIGHT_PAREN = ')';
    public static final Character LEFT_CARROT = '<';
    public static final Character RIGHT_CARROT = '>';
    public static final Character COMMA = ',';
    public static final Character ANY = '*';
    public static final Character FRIEND = '+';
    public static final Character ENEMY = '-';
    public static final Character SELF = '/';
    public static final int ASCII_CASE_OFFSET = 32;

    private Map<String, SymbolMapper> functionTable;
    private Map<Character, SymbolMapper> symbolTable;
    private Map<Character, PieceBehavior> pieceTable;
    private Map<String, MovementPattern> vectorTable;
    private Map<String, Coordinate> coordinateTable;
    private Map<String, Condition> conditionTable;
    private Map<String, Property> propertyTable;

    public Parser() {
        functionTable = new HashMap<>();
        symbolTable = new HashMap<>();
        pieceTable = new HashMap<>();
        vectorTable = new HashMap<>();
        coordinateTable = new HashMap<>();
        conditionTable = new HashMap<>();
        initFunctionTable();
        initSymbolTable();
    }

    public void loadGameFile() throws IOException {
        FileReader fr = new FileReader("resources/game/Chess.txt");
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            processLine(line);
        }
        br.close();

        // for (String s : vectorTable.keySet()) {
        //     System.out.println(s + " " + vectorTable.get(s).toString());
        // }
    }

    private void processLine(String line) {
        if (line.length() == 0) return;
        if (symbolTable.containsKey(line.trim().charAt(0))) symbolTable.get(line.trim().charAt(0)).symbMap(line);
        else if (line.trim().charAt(1) == DOT_OPERATOR) functionTable.get(line.trim().substring(2, line.trim().indexOf(LEFT_PAREN))).symbMap(line);
    }

    private void pieceDeclaration(String line) {
        char piece = line.charAt(line.indexOf(PIECE_DECLARATOR) + 1);
        pieceTable.put(piece, new PieceBehavior(new PieceTypeID(Player.WHITE, (int)piece)));
        pieceTable.put((char)(piece - ASCII_CASE_OFFSET), new PieceBehavior(new PieceTypeID(Player.BLACK, (int)piece)));
    }

    private void layout(String line) {

    }

    private void vectorDeclaration(String line) {
        int declaration = line.indexOf(VECTOR_DECLARATOR) + 1;
        int operator = line.indexOf(ASSIGNMENT_OPERATOR);
        String vectorSymbol = line.substring(declaration, operator).trim();
        String[] args = getArgs(line, LEFT_BRACKET, RIGHT_BRACKET);
        vectorTable.put(vectorSymbol, new MovementPattern(stoi(args[0]), stoi(args[1]), stoi(args[2])));
    }

    private void coordinateDeclaration(String line) {
        int declaration = line.indexOf(COORDINATE_DECLARATOR) + 1;
        int operator = line.indexOf(ASSIGNMENT_OPERATOR);
        String coordinateSymbol = line.substring(declaration, operator).trim();
        String[] args = getArgs(line, LEFT_BRACKET, RIGHT_BRACKET);
        coordinateTable.put(coordinateSymbol, new Coordinate(stoi(args[0]), stoi(args[1])));
    }

    private void conditionDeclaration(String line) {
        int declaration = line.indexOf(CONDITION_DECLARATOR) + 1;
        int operator = line.indexOf(ASSIGNMENT_OPERATOR);
        String conditionSymbol = line.substring(declaration, operator).trim();
        String[] args = getArgs(line, LEFT_PAREN, RIGHT_PAREN);
        Optional<SimpleEntry<PieceType, Coordinate>> absoluteCondition = parseAbsoluteCondition(args[0].trim());
        Optional<SimpleEntry<PieceType, MovementPattern>> relativeCondition = parseRelativeCondition(args[1].trim());
        Optional<SimpleEntry<PieceType, Property>> propertyCondition = parsePropertyCondition(args[2].trim());
        if (absoluteCondition.isPresent()) conditionTable.get(conditionSymbol).addAbsoluteCondition(absoluteCondition.get());
        if (relativeCondition.isPresent()) conditionTable.get(conditionSymbol).addRelativeCondition(relativeCondition.get());
        if (propertyCondition.isPresent()) conditionTable.get(conditionSymbol).addPropertyCondition(propertyCondition.get());
    }

    private Optional<SimpleEntry<PieceType, Coordinate>> parseAbsoluteCondition(String args) {
        int l = args.indexOf(LEFT_CARROT);
        int r = args.indexOf(RIGHT_CARROT);
        if (l < 0 || r < 0) return Optional.empty();
        String[] absoluteConditionArgs = getArgs(args, LEFT_CARROT, RIGHT_CARROT);
        char key = absoluteConditionArgs[0].charAt(0);
        String value = absoluteConditionArgs[1];
        return Optional.of(new AbstractMap.SimpleEntry<>(determinePieceType(key), coordinateTable.get(value)));
    }

    private Optional<SimpleEntry<PieceType, MovementPattern>> parseRelativeCondition(String args) {
        int l = args.indexOf(LEFT_CARROT);
        int r = args.indexOf(RIGHT_CARROT);
        if (l < 0 || r < 0) return Optional.empty();
        String[] absoluteConditionArgs = getArgs(args, LEFT_CARROT, RIGHT_CARROT);
        char key = absoluteConditionArgs[0].charAt(0);
        String value = absoluteConditionArgs[1];
        return Optional.of(new AbstractMap.SimpleEntry<>(determinePieceType(key), vectorTable.get(value)));
    }

    private Optional<SimpleEntry<PieceType, Property>> parsePropertyCondition(String args) {
        int l = args.indexOf(LEFT_CARROT);
        int r = args.indexOf(RIGHT_CARROT);
        if (l < 0 || r < 0) return Optional.empty();
        String[] absoluteConditionArgs = getArgs(args, LEFT_CARROT, RIGHT_CARROT);
        char key = absoluteConditionArgs[0].charAt(0);
        String value = absoluteConditionArgs[1];
        return Optional.of(new AbstractMap.SimpleEntry<>(determinePieceType(key), propertyTable.get(value)));
    }

    private PieceType determinePieceType(char token) {
        if (token == ANY) return new PieceType(0, true, false, false, false);
        if (token == FRIEND) return new PieceType(0, false, true, false, false);
        if (token == ENEMY) return new PieceType(0, false, false, true, false);
        if (token == SELF) return new PieceType(0, false, false, false, true);
        return new PieceType(0, false, false, false, false);
    }

    private void propertyDeclaration(String line) {
        
    }

    private void does(String line) {
        char piece = line.charAt(0);
        char complementPiece = (char)(piece - ASCII_CASE_OFFSET);
        String[] args = getArgs(line, LEFT_PAREN, RIGHT_PAREN);
        //Write this implementation
        List<MovementPattern> movementPattern = new ArrayList<>();
        Map<MovementPattern, Condition> fulfillCond = new HashMap<>();
        Map<MovementPattern, Condition> inhibitoryCond = new HashMap<>();
        pieceTable.get(piece).setBehavior(movementPattern, fulfillCond, inhibitoryCond);
        pieceTable.get(complementPiece).setBehavior(movementPattern, fulfillCond, inhibitoryCond);
    }

    private void onlyDoes(String line) {
        char piece = line.charAt(0);
        String[] args = getArgs(line, LEFT_PAREN, RIGHT_PAREN);
        //Write this implementation
        List<MovementPattern> movementPattern = new ArrayList<>();
        Map<MovementPattern, Condition> fulfillCond = new HashMap<>();
        Map<MovementPattern, Condition> inhibitoryCond = new HashMap<>();
        pieceTable.get(piece).setBehavior(movementPattern, fulfillCond, inhibitoryCond);
    }

    private String[] getArgs(String s, Character left, Character right) {
        int argsBegin = s.indexOf(left) + 1;
        int argsEnd = s.indexOf(right);
        String argsString = s.substring(argsBegin, argsEnd).trim();
        return argsString.split("" + COMMA);
    }

    private void initSymbolTable() {
        symbolTable.put(PIECE_DECLARATOR, (s) -> pieceDeclaration(s));
        symbolTable.put(LAYOUT_DECLARATOR, (s) -> layout(s));
        symbolTable.put(VECTOR_DECLARATOR, (s) -> vectorDeclaration(s));
        symbolTable.put(COORDINATE_DECLARATOR, (s) -> coordinateDeclaration(s));
        symbolTable.put(CONDITION_DECLARATOR, (s) -> conditionDeclaration(s));
        symbolTable.put(PROPERTY_DECLARATOR, (s) -> propertyDeclaration(s));
    }

    private void initFunctionTable() {
        functionTable.put(DOES, (s) -> does(s));
        functionTable.put(ONLY_DOES, (s) -> onlyDoes(s));
    }

    private int stoi(String s) {
        return Integer.parseInt(s.trim());
    }
    
}
