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

import model.rules.Action;
import model.rules.ActionType;
import model.rules.Condition;
import model.board.Board;
import model.board.Coordinate;
import model.piece.PieceBehavior;
import model.piece.PieceType;
import model.piece.PieceTypeID;
import model.piece.Player;
import model.rules.MovementPattern;
import model.rules.Property;
import model.rules.PropertyType;

public class Parser {

    public static final String DOES = "does";
    public static final String MUST = "must";
    public static final String CANNOT = "cannot";
    public static final String ACTION = "action";
    public static final String BOTH = "both";
    public static final Character PIECE_DECLARATOR = '%';
    public static final Character LAYOUT_DECLARATOR = '?';
    public static final Character VECTOR_DECLARATOR = '$';
    public static final Character COORDINATE_DECLARATOR = '@';
    public static final Character CONDITION_DECLARATOR = '#';
    public static final Character PROPERTY_DECLARATOR = '&';
    public static final Character ACTION_DECLARATOR = '!';
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
    private Map<String, Condition> conditionTable;
    private Map<String, Coordinate> coordinateTable;
    private Map<String, MovementPattern> vectorTable;
    private Map<String, Property> propertyTable;
    private Map<String, Action> actionTable;
    private String fen;

    public Parser() {
        functionTable = new HashMap<>();
        symbolTable = new HashMap<>();
        pieceTable = new HashMap<>();
        vectorTable = new HashMap<>();
        coordinateTable = new HashMap<>();
        conditionTable = new HashMap<>();
        propertyTable = new HashMap<>();
        actionTable = new HashMap<>();
        initFunctionTable();
        initSymbolTable();
    }

    public Board generateBoard() {
        Board b = new Board();
        FenParser f = new FenParser(pieceTable);
        b.initBoard(f.getDimensions(fen), f.configurePieces(fen));
        return b;
    }

    public void loadGameFile() throws IOException {
        FileReader fr = new FileReader("resources/game/Chess.txt");
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            processLine(line);
        }
        br.close();
    }

    private void processLine(String line) {
        if (line.length() == 0) return;
        if (symbolTable.containsKey(line.trim().charAt(0))) symbolTable.get(line.trim().charAt(0)).symbMap(line);
        else if (line.trim().charAt(1) == DOT_OPERATOR) functionTable.get(line.trim().substring(2, line.trim().indexOf(LEFT_PAREN))).symbMap(line);
    }

    private void pieceDeclaration(String line) {
        char piece = line.charAt(line.indexOf(PIECE_DECLARATOR) + 1);
        char symbol = getSymbol(line, piece);
        pieceTable.put(piece, new PieceBehavior(new PieceTypeID(Player.WHITE, (int)piece, symbol)));
        pieceTable.put((char)(piece + ASCII_CASE_OFFSET), new PieceBehavior(new PieceTypeID(Player.BLACK, (int)piece, symbol)));
    }

    private void layout(String line) {
        int declaration = line.indexOf(LAYOUT_DECLARATOR) + 1;
        fen = line.substring(declaration, line.length()).trim();
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
        conditionTable.putIfAbsent(conditionSymbol, new Condition(new HashMap<>(), new HashMap<>(), new HashMap<>()));
        String[] args = getArgs(line, LEFT_PAREN, RIGHT_PAREN);
        args = reconstructedArgs(args);
        Optional<SimpleEntry<PieceType, Coordinate>> absoluteCondition = parseConditionEntry(args[0].trim(), coordinateTable);
        Optional<SimpleEntry<PieceType, MovementPattern>> relativeCondition = parseConditionEntry(args[1].trim(), vectorTable);
        Optional<SimpleEntry<PieceType, Property>> propertyCondition = parseConditionEntry(args[2].trim(), propertyTable);
        if (absoluteCondition.isPresent()) conditionTable.get(conditionSymbol).addAbsoluteCondition(absoluteCondition.get());
        if (relativeCondition.isPresent()) conditionTable.get(conditionSymbol).addRelativeCondition(relativeCondition.get());
        if (propertyCondition.isPresent()) conditionTable.get(conditionSymbol).addPropertyCondition(propertyCondition.get());
    }

    private void propertyDeclaration(String line) {
        int declaration = line.indexOf(PROPERTY_DECLARATOR) + 1;
        int operator = line.indexOf(ASSIGNMENT_OPERATOR);
        String propertySymbol = line.substring(declaration, operator).trim();
        String[] args = getArgs(line, LEFT_BRACKET, RIGHT_BRACKET);
        propertyTable.put(propertySymbol, new Property(PropertyType.valueOf(args[0]), propertiesLister(args)));
    }

    private void actionDeclaration(String line) {
        int declaration = line.indexOf(ACTION_DECLARATOR) + 1;
        int operator = line.indexOf(ASSIGNMENT_OPERATOR);
        String actionSymbol = line.substring(declaration, operator).trim();
        Object[] args = getObjectArgs(line, LEFT_BRACKET, RIGHT_BRACKET);
        actionTable.put(actionSymbol, new Action(ActionType.valueOf(((String)args[0]).trim()), propertiesLister(args)));
    }

    private String[] reconstructedArgs(String[] args) {
        String[] reconstructedArgs = new String[3];
        int fixedArgs = 0;
        for (int i = 0; i < args.length; i++) {
            if (args[i].trim().charAt(0) == LEFT_CARROT) {
                reconstructedArgs[fixedArgs++] = args[i] + COMMA + args[i+1];
                i++;
            }
            else reconstructedArgs[fixedArgs++] = args[i];
        }
        return reconstructedArgs;
    }

    private Optional<String[]> parseCondition(String args) {
        int l = args.indexOf(LEFT_CARROT);
        int r = args.indexOf(RIGHT_CARROT);
        if (l < 0 || r < 0) return Optional.empty();
        String[] parsedArgs = getArgs(args, LEFT_CARROT, RIGHT_CARROT);
        return Optional.of(parsedArgs);
    }

    private <T> Optional<SimpleEntry<PieceType, T>> parseConditionEntry(String args, Map<String, T> table) {
        Optional<String[]> conditionArgs = parseCondition(args);
        if (!conditionArgs.isPresent()) return Optional.empty();
        String key = conditionArgs.get()[0].trim();
        String value = conditionArgs.get()[1].trim();
        return Optional.of(new AbstractMap.SimpleEntry<>(determinePieceType(key), table.get(value)));
    }

    private PieceType determinePieceType(String token) {
        PieceType p = new PieceType();
        if (token.charAt(0) == ANY) p.all = true;
        if (token.charAt(0) == FRIEND) p.friendly = true;
        if (token.charAt(0) == ENEMY) p.enemy = true;
        if (token.charAt(0) == SELF) p.selfInstance = true;
        if (vectorTable.containsKey(token)) p.relativeNeighbor = vectorTable.get(token);
        if (vectorTable.containsKey(token.substring(1))) p.relativeNeighbor = vectorTable.get(token.substring(1));
        return p;
    }

    private char getSymbol(String line, Character piece) {
        char symbol = '\0';
        if (line.indexOf(":") >= 0) {
            String[] args = getArgs(line, LEFT_BRACKET, RIGHT_BRACKET);
            if (args[0].length() > 0) symbol = args[0].charAt(0);
        }
        else {
            symbol = piece;
        }
        return symbol;
    }

    private void bothDoes(String line) {
        char piece = line.charAt(0);
        char complementPiece = (char)(piece + ASCII_CASE_OFFSET);
        String[] args = getArgs(line, LEFT_PAREN, RIGHT_PAREN);
        String[] arrangedArgs = {args[0], "", "", ""};
        assignBehavior(piece, arrangedArgs);
        assignBehavior(complementPiece, arrangedArgs);
    }

    private void does(String line) {
        char piece = line.charAt(0);
        String[] args = getArgs(line, LEFT_PAREN, RIGHT_PAREN);
        String[] arrangedArgs = {args[0], "", "", ""};
        assignBehavior(piece, arrangedArgs);
    }

    private void bothMust(String line) {
        char piece = line.charAt(0);
        char complementPiece = (char)(piece + ASCII_CASE_OFFSET);
        String[] args = getArgs(line, LEFT_PAREN, RIGHT_PAREN);
        String[] arrangedArgs = {args[0], args[1], "", ""};
        assignBehavior(piece, arrangedArgs);
        assignBehavior(complementPiece, arrangedArgs);
    }

    private void must(String line) {
        char piece = line.charAt(0);
        String[] args = getArgs(line, LEFT_PAREN, RIGHT_PAREN);
        String[] arrangedArgs = {args[0], args[1], "", ""};
        assignBehavior(piece, arrangedArgs);
    }

    private void bothCannot(String line) {
        char piece = line.charAt(0);
        char complementPiece = (char)(piece + ASCII_CASE_OFFSET);
        String[] args = getArgs(line, LEFT_PAREN, RIGHT_PAREN);
        String[] arrangedArgs = {args[0], "", args[1], ""};
        assignBehavior(piece, arrangedArgs);
        assignBehavior(complementPiece, arrangedArgs);
    }

    private void cannot(String line) {
        char piece = line.charAt(0);
        String[] args = getArgs(line, LEFT_PAREN, RIGHT_PAREN);
        String[] arrangedArgs = {args[0], "", args[1], ""};
        assignBehavior(piece, arrangedArgs);
    }

    private void bothAction(String line) {
        char piece = line.charAt(0);
        char complementPiece = (char)(piece + ASCII_CASE_OFFSET);
        String[] args = getArgs(line, LEFT_PAREN, RIGHT_PAREN);
        String[] arrangedArgs = {args[0], "", "", args[1]};
        assignBehavior(piece, arrangedArgs);
        assignBehavior(complementPiece, arrangedArgs);
    }

    private void action(String line) {
        char piece = line.charAt(0);
        String[] args = getArgs(line, LEFT_PAREN, RIGHT_PAREN);
        String[] arrangedArgs = {args[0], "", "", args[1]};
        assignBehavior(piece, arrangedArgs);
    }

    private void assignBehavior(char piece, String[] args) {
        MovementPattern mp = vectorTable.get(args[0].trim());
        Condition fc = conditionTable.get(args[1].trim());
        Condition ic = conditionTable.get(args[2].trim());
        Action a = actionTable.get(args[3].trim());
        if (mp == null) return;
        pieceTable.get(piece).addMovementPattern(mp);
        if (fc != null) pieceTable.get(piece).addFulfillCond(mp, fc);
        if (ic != null) pieceTable.get(piece).addInhibitoryCond(mp, ic);
        if (a != null) pieceTable.get(piece).addAction(mp, a);
    }

    private String[] getArgs(String s, Character left, Character right) {
        int argsBegin = s.indexOf(left) + 1;
        int argsEnd = s.indexOf(right);
        String argsString = s.substring(argsBegin, argsEnd).trim();
        return argsString.split("" + COMMA);
    }

    private Object[] getObjectArgs(String s, Character left, Character right) {
        int argsBegin = s.indexOf(left) + 1;
        int argsEnd = s.indexOf(right);
        String argsString = s.substring(argsBegin, argsEnd).trim();
        String[] listArgsString = argsString.split("" + COMMA);
        Object[] objectArgs = new Object[listArgsString.length];
        for (int i = 0; i < listArgsString.length; i++) {
            listArgsString[i] = listArgsString[i].trim();
            if (vectorTable.get(listArgsString[i]) != null) objectArgs[i] = vectorTable.get(listArgsString[i]);
            else objectArgs[i] = listArgsString[i];
        }
        return objectArgs;
    }

    private void initSymbolTable() {
        symbolTable.put(PIECE_DECLARATOR, (s) -> pieceDeclaration(s));
        symbolTable.put(LAYOUT_DECLARATOR, (s) -> layout(s));
        symbolTable.put(VECTOR_DECLARATOR, (s) -> vectorDeclaration(s));
        symbolTable.put(COORDINATE_DECLARATOR, (s) -> coordinateDeclaration(s));
        symbolTable.put(CONDITION_DECLARATOR, (s) -> conditionDeclaration(s));
        symbolTable.put(PROPERTY_DECLARATOR, (s) -> propertyDeclaration(s));
        symbolTable.put(ACTION_DECLARATOR, (s) -> actionDeclaration(s));
    }

    private void initFunctionTable() {
        functionTable.put(BOTH + DOT_OPERATOR + DOES, (s) -> bothDoes(s));
        functionTable.put(BOTH + DOT_OPERATOR + MUST, (s) -> bothMust(s));
        functionTable.put(BOTH + DOT_OPERATOR + CANNOT, (s) -> bothCannot(s));
        functionTable.put(BOTH + DOT_OPERATOR + ACTION, (s) -> bothAction(s));
        functionTable.put(DOES, (s) -> does(s));
        functionTable.put(MUST, (s) -> must(s));
        functionTable.put(CANNOT, (s) -> cannot(s));
        functionTable.put(ACTION, (s) -> action(s));
    }

    private int stoi(String s) {
        return Integer.parseInt(s.trim());
    }

    private List<Object> propertiesLister(Object[] args) {
        List<Object> properties = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            if (args[i] instanceof String) args[i] = ((String)args[i]).trim();
            properties.add(args[i]);
        }
        return properties;
    }
    
}
